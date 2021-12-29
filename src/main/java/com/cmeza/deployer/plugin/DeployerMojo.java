package com.cmeza.deployer.plugin;

import com.cmeza.deployer.plugin.copy.process.ProcessCopyTask;
import com.cmeza.deployer.plugin.minify.MinifyTarget;
import com.cmeza.deployer.plugin.minify.configurations.MinifyBundles;
import com.cmeza.deployer.plugin.minify.process.ProcessMinifyTask;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Mojo(name = "deploy", defaultPhase = LifecyclePhase.COMPILE)
public class DeployerMojo extends CommonsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        this.validateOutputFolder();

        Path output = Utils.getOutputFolder(outputFolder);

        Collection<Callable<Object>> processFilesTasks = new ArrayList<>();
        if (Objects.nonNull(copy)) {
            processFilesTasks.add(new ProcessCopyTask(getLog(), output, copy));
        }

        if (Objects.nonNull(minify)) {
            this.validateBundleConfigurationFile(minify);

            MinifyBundles bundles = this.readBundleConfiguration(minify);
            for (MinifyBundles.MinifyBundle bundle : bundles.getBundles()) {
                ProcessMinifyTask.Builder builder = new ProcessMinifyTask.Builder()
                        .withLog(getLog())
                        .withMinifyTarget(minify)
                        .withMinifyBundle(bundle)
                        .withMinifyConfiguration(bundles.getConfiguration())
                        .withOutputFolder(output);
                processFilesTasks.add(builder.build());
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(processFilesTasks.size());
        try {
            List<Future<Object>> futures = executor.invokeAll(processFilesTasks);
            for (Future<Object> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            }
            executor.shutdown();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void validateOutputFolder() throws MojoExecutionException {
        if (Objects.isNull(outputFolder)) {
            throw new MojoExecutionException("destinationFolder is required");
        }
    }

    private void validateBundleConfigurationFile(MinifyTarget target) throws MojoExecutionException {
        if (StringUtils.isEmpty(target.getBundleConfiguration())) {
            throw new MojoExecutionException("bundleConfiguration is required");
        }
    }

    private MinifyBundles readBundleConfiguration(MinifyTarget target) throws MojoFailureException {
        try (Reader bundleConfigurationReader = new FileReader(target.getBundleConfiguration())) {
            return new Gson().fromJson(bundleConfigurationReader, MinifyBundles.class);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to open the bundle configuration file [" + target.getBundleConfiguration() + "].", e);
        }
    }
}
