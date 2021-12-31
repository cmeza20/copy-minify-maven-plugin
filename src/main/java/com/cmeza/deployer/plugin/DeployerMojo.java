package com.cmeza.deployer.plugin;

import com.cmeza.deployer.plugin.copy.CopyTarget;
import com.cmeza.deployer.plugin.copy.configurations.CopyBundle;
import com.cmeza.deployer.plugin.copy.process.ProcessCopyTask;
import com.cmeza.deployer.plugin.exec.ExecTarget;
import com.cmeza.deployer.plugin.exec.configurations.ExecBundle;
import com.cmeza.deployer.plugin.exec.process.ProcessExecTask;
import com.cmeza.deployer.plugin.minify.MinifyTarget;
import com.cmeza.deployer.plugin.minify.configurations.MinifyBundle;
import com.cmeza.deployer.plugin.minify.process.ProcessMinifyTask;
import com.cmeza.deployer.plugin.utils.AbstractBundle;
import com.cmeza.deployer.plugin.utils.AbstractTarget;
import com.cmeza.deployer.plugin.utils.Utils;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mojo(name = "deploy", defaultPhase = LifecyclePhase.COMPILE)
public class DeployerMojo extends CommonsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        this.validateOutputFolder();

        Path output = Utils.getOutputFolder(outputFolder, versioned);
        Collection<Callable<Object>> processFilesTasks = new LinkedList<>();

        if (Objects.nonNull(exec)) {
            ExecTarget target = this.readTarget(exec, ExecTarget.class, ((fromProperties, fromJson) -> {
                fromProperties.setBundles(fromJson.getBundles());
                fromProperties.setConfiguration(fromJson.getConfiguration());
            }));

            for (ExecBundle bundle : target.getBundles()) {
                ProcessExecTask.Builder builder = new ProcessExecTask.Builder()
                        .withLog(getLog())
                        .withExecTarget(target)
                        .withExecBundle(bundle);
                processFilesTasks.add(builder.build());
            }
        }


        if (Objects.nonNull(copy)) {
            CopyTarget target = this.readTarget(copy, CopyTarget.class, ((fromProperties, fromJson) -> {
                fromProperties.setBundles(fromJson.getBundles());
            }));

            for (CopyBundle bundle : target.getBundles()) {
                ProcessCopyTask.Builder builder = new ProcessCopyTask.Builder()
                        .withLog(getLog())
                        .withCopyTarget(target)
                        .withCopyBundle(bundle)
                        .withOutputFolder(output);
                processFilesTasks.add(builder.build());
            }
        }

        if (Objects.nonNull(minify)) {
            MinifyTarget target = this.readTarget(minify, MinifyTarget.class, (fromProperties, fromJson) -> {
                fromProperties.setConfiguration(fromJson.getConfiguration());
                fromProperties.setBundles(fromJson.getBundles());
            });

            for (MinifyBundle bundle : target.getBundles()) {
                ProcessMinifyTask.Builder builder = new ProcessMinifyTask.Builder()
                        .withLog(getLog())
                        .withMinifyTarget(target)
                        .withMinifyBundle(bundle)
                        .withOutputFolder(output);
                processFilesTasks.add(builder.build());
            }
        }

        if (!processFilesTasks.isEmpty()) {
            ExecutorService executor = Executors.newFixedThreadPool(processFilesTasks.size());
            try {
                for (Callable<Object> object : processFilesTasks) {
                    executor.submit(object).get();
                }
                executor.shutdown();
            } catch (InterruptedException | ExecutionException e) {
                executor.shutdownNow();
                throw new MojoExecutionException(e.getMessage(), e);
            }
        } else {
            getLog().warn("No tasks were defined!");
        }
    }

    private void validateOutputFolder() throws MojoExecutionException {
        if (Objects.isNull(outputFolder)) {
            throw new MojoExecutionException("destinationFolder is required");
        }
    }

    private <T extends AbstractTarget<E>, E extends AbstractBundle> T readTarget(T target, Class<T> clazz, TargetInterface<T, E> matcher) throws MojoExecutionException, MojoFailureException {
        if (!StringUtils.isEmpty(target.getBundleConfiguration())) {
            try (Reader bundleConfigurationReader = new FileReader(target.getBundleConfiguration())) {
                T config = new Gson().fromJson(bundleConfigurationReader, clazz);
                if (matcher != null) {
                    matcher.matcher(target, config);
                }
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to open the bundle configuration file [" + target.getBundleConfiguration() + "].", e);
            }
        }
        return target;
    }

    @FunctionalInterface
    public interface TargetInterface<T extends AbstractTarget<E>, E extends AbstractBundle> {
        void matcher(T fromProperties, T fromJson) throws MojoFailureException, MojoExecutionException;
    }
}
