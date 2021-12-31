package com.cmeza.deployer.plugin.copy.process;

import com.cmeza.deployer.plugin.copy.CopyTarget;
import com.cmeza.deployer.plugin.copy.configurations.CopyBundle;
import com.cmeza.deployer.plugin.utils.Utils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class ProcessCopyTask implements Callable<Object> {

    private final Log log;

    private final Path outputFolder;

    private final CopyBundle copyBundle;

    public ProcessCopyTask(Builder builder) {
        this.log = builder.getLog();
        this.outputFolder = builder.getOutputFolder();
        this.copyBundle = builder.getCopyBundle();
    }

    @Override
    public Object call() throws Exception {
        synchronized (log) {
            if (StringUtils.isEmpty(copyBundle.getDestinationFolder())) {
                Utils.printTitle("Starting COPY task", log);
            } else {
                Utils.printTitle("Starting COPY task: " + copyBundle.getDestinationFolder(), log);
            }

            this.executeTarget(outputFolder, copyBundle);

            log.info("");
        }
        return null;
    }

    private void executeTarget(Path outputFolder, CopyBundle copyBundle) throws MojoExecutionException {
        this.validateRequired(copyBundle);

        try {
            Path source = Utils.getAbsolutePath(copyBundle.isFindInParent(), copyBundle.getSearchIn());
            if (!Files.exists(source)) {
                throw new MojoExecutionException("The source file [" + source + "] does not exist.");
            }

            Path destinationFolder = Utils.concat(outputFolder, copyBundle.getDestinationFolder());
            Utils.createDirectoryIfNotExists(destinationFolder.toFile());

            if (Files.isDirectory(source)) {
                List<Path> files = Utils.filter(source, copyBundle.getPrefix(), copyBundle.getSuffix());
                if (files.isEmpty()) {
                    log.warn("No files or folders found");
                } else {
                    this.copyFiles(files, source, destinationFolder);
                }
            } else {
                this.copyFile(source, destinationFolder);
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    private void validateRequired(CopyBundle copyBundle) throws MojoExecutionException {
        if (Objects.isNull(copyBundle)) {
            throw new MojoExecutionException("Bundle is empty");
        }
    }

    private void copyFiles(List<Path> paths, Path sources, Path destinationFolder) throws Exception {
        for (Path path : paths) {
            Path destination = Paths.get(destinationFolder.toString(), path.toString().substring(sources.toString().length()));
            Utils.createDirectoryIfNotExists(destination.getParent().toFile());
            this.copyFile(path, destination);
        }
    }

    private void copyFile(Path path, Path destinationFolder) throws Exception {
        Files.copy(path, destinationFolder);
        log.info("Copied: " + destinationFolder);
    }

    @Getter
    public static class Builder {
        private CopyBundle copyBundle;
        private CopyTarget copyTarget;
        private Log log;
        private Path outputFolder;

        public Builder withCopyBundle(CopyBundle copyBundle) {
            this.copyBundle = copyBundle;
            return this;
        }

        public Builder withCopyTarget(CopyTarget copyTarget) {
            this.copyTarget = copyTarget;
            return this;
        }

        public Builder withLog(Log log) {
            this.log = log;
            return this;
        }

        public Builder withOutputFolder(Path outputFolder) {
            this.outputFolder = outputFolder;
            return this;
        }

        public ProcessCopyTask build() throws MojoExecutionException {
            Objects.requireNonNull(copyBundle, "CopyBundle is required!");
            Objects.requireNonNull(copyTarget, "CopyTarget is required!");
            Objects.requireNonNull(log, "Log is required!");
            Objects.requireNonNull(outputFolder, "OutputFolder is required!");
            return new ProcessCopyTask(this);
        }

    }

}
