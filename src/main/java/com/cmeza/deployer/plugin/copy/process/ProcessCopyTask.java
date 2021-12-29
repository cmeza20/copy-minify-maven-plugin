package com.cmeza.deployer.plugin.copy.process;

import com.cmeza.deployer.plugin.copy.CopyTarget;
import com.cmeza.deployer.plugin.utils.Utils;
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

    private final CopyTarget[] copy;

    public ProcessCopyTask(Log log, Path outputFolder, CopyTarget[] copy) {
        this.log = log;
        this.outputFolder = outputFolder;
        this.copy = copy;
    }

    @Override
    public Object call() throws Exception {
        synchronized (log) {
            Utils.printTitle("Starting COPY task", log);

            for (CopyTarget target : copy) {
                this.executeTarget(outputFolder, target);
            }
            log.info("");
        }
        return null;
    }

    private void executeTarget(Path outputFolder, CopyTarget copyTarget) throws MojoExecutionException {
        this.validateRequired(copyTarget);

        try {
            Path source = Utils.getAbsolutePath(copyTarget.isFindInParent(), copyTarget.getSearchIn());
            if (!Files.exists(source)) {
                throw new MojoExecutionException("The source file [" + source + "] does not exist.");
            }

            Path destinationFolder = Utils.concat(outputFolder, copyTarget.getDestinationFolder());
            Utils.createDirectoryIfNotExists(destinationFolder.toFile());

            if (Files.isDirectory(source)) {
                List<Path> files = Utils.filter(source, copyTarget.getPrefix(), copyTarget.getSuffix());
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

    private void validateRequired(CopyTarget copyTarget) throws MojoExecutionException {
        if (Objects.isNull(copyTarget)) {
            throw new MojoExecutionException("Target is empty");
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

}
