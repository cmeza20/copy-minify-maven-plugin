package com.cmeza.deployer.plugin.minify.process;

import com.cmeza.deployer.plugin.minify.MinifyTarget;
import com.cmeza.deployer.plugin.minify.configurations.MinifyBundle;
import com.cmeza.deployer.plugin.minify.configurations.MinifyConfiguration;
import com.cmeza.deployer.plugin.minify.enums.Engine;
import com.cmeza.deployer.plugin.minify.enums.MinifyType;
import com.cmeza.deployer.plugin.utils.SourceFilesEnumeration;
import com.cmeza.deployer.plugin.utils.Utils;
import com.google.javascript.jscomp.SourceMap;
import lombok.Getter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

public abstract class ProcessMinifyTask implements Callable<Object> {

    protected final Log log;
    protected final Charset charset;
    protected final Engine engine;
    private final Path targetFolder;
    private final MinifyBundle minifyBundle;
    private final MinifyTarget minifyTarget;
    private final String mergedFilename;
    private final boolean isVerbose;
    private final MinifyConfiguration configuration;
    private final List<File> files = new ArrayList<>();

    public ProcessMinifyTask(Builder builder) throws MojoExecutionException {
        try {
            Objects.requireNonNull(builder.getTarget().getConfiguration(), "Configuration is required!");

            this.log = builder.getLog();
            this.minifyTarget = builder.getTarget();
            this.configuration = minifyTarget.getConfiguration();
            this.isVerbose = configuration.isVerbose();
            this.minifyBundle = builder.getMinifyBundle();
            this.charset = Charset.forName(configuration.getCharset());
            this.targetFolder = Utils.concat(builder.getOutputFolder(), minifyTarget.getDestinationFolder());
            Path sourceFolder = Utils.getAbsolutePath(minifyTarget.isFindInParent(), minifyTarget.getSearchIn());

            if (builder.getMinifyBundle().getType().equals(MinifyType.css)) {
                this.engine = configuration.getCssEngine();
            } else {
                this.engine = configuration.getJsEngine();
            }
            this.mergedFilename = builder.getMinifyBundle().getName();

            for (String sourceFilename : builder.getMinifyBundle().getFiles()) {
                addNewSourceFile(mergedFilename, new File(sourceFolder.toString(), sourceFilename));
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }


    @Override
    public Object call() throws IOException {
        synchronized (log) {

            Utils.printTitle("Starting MINIFY " + minifyBundle.getType().getDescription() + " task:", log);

            if (Files.notExists(targetFolder) && !targetFolder.toFile().mkdirs()) {
                throw new RuntimeException("Unable to create target directory for: " + targetFolder);
            }

            if (!files.isEmpty()) {

                File mergedFile = new File(targetFolder.toString(), mergedFilename);
                merge(mergedFile);

                File minifiedFile = new File(targetFolder.toString(), FileUtils.removeExtension(mergedFilename) + ".min." + FileUtils.extension(mergedFilename));
                if (!minifiedFile.getParentFile().exists() && !minifiedFile.getParentFile().mkdirs()) {
                    throw new RuntimeException("Unable to create target directory for: " + minifiedFile.getParentFile());
                }

                minify(mergedFile, minifiedFile, configuration);

                this.logCompressionGains(mergedFile, minifiedFile);

                log.info("");
            } else if (!minifyBundle.getFiles().isEmpty()) {
                log.error("No valid " + minifyBundle.getType().getDescription() + " source files found to process.");
            }
        }

        return null;
    }

    private void merge(File mergedFile) throws IOException {
        if (!mergedFile.getParentFile().exists() && !mergedFile.getParentFile().mkdirs()) {
            throw new RuntimeException("Unable to create target directory for: " + mergedFile.getParentFile());
        }

        try (InputStream sequence = new SequenceInputStream(new SourceFilesEnumeration(log, files, isVerbose));
             OutputStream out = new FileOutputStream(mergedFile);
             InputStreamReader sequenceReader = new InputStreamReader(sequence, charset);
             OutputStreamWriter outWriter = new OutputStreamWriter(out, charset)) {
            if (configuration.isKeepMerged()) {
                log.info("Creating the merged file [" + (isVerbose ? mergedFile.getPath() : mergedFile.getName()) + "].");
            }
            IOUtil.copy(sequenceReader, outWriter, configuration.getBufferSize());
        } catch (IOException e) {
            log.error("Failed to concatenate files.", e);
            throw e;
        }
    }

    abstract void minify(File mergedFile, File minifiedFile, MinifyConfiguration configuration) throws IOException;

    private void logCompressionGains(File mergedFile, File minifiedFile) {
        try {
            File temp = File.createTempFile(minifiedFile.getName(), ".gz");

            try (InputStream in = new FileInputStream(minifiedFile);
                 OutputStream out = new FileOutputStream(temp);
                 GZIPOutputStream outGZIP = new GZIPOutputStream(out)) {
                IOUtil.copy(in, outGZIP, configuration.getBufferSize());
            }

            log.info("Uncompressed size: " + mergedFile.length() + " bytes.");
            log.info("Compressed size: " + minifiedFile.length() + " bytes minified (" + temp.length() + " bytes gzipped).");

            temp.deleteOnExit();
            if (!configuration.isKeepMerged()) {
                mergedFile.deleteOnExit();
            }
        } catch (IOException e) {
            log.debug("Failed to calculate the gzipped file size.", e);
        }
    }

    private void addNewSourceFile(String finalFilename, File sourceFile) throws FileNotFoundException {
        if (sourceFile.exists()) {
            if (finalFilename.equalsIgnoreCase(sourceFile.getName())) {
                log.warn("The source file [" + (isVerbose ? sourceFile.getPath() : sourceFile.getName())
                        + "] has the same name as the final file.");
            }
            log.debug("Adding source file [" + (isVerbose ? sourceFile.getPath() : sourceFile.getName()) + "].");
            files.add(sourceFile);
        } else {
            throw new FileNotFoundException("The source file ["
                    + (isVerbose ? sourceFile.getPath() : sourceFile.getName()) + "] does not exist.");
        }
    }

    protected void flushSourceMap(File sourceMapOutputFile, String minifyFileName, SourceMap sourceMap) {
        try (FileWriter out = new FileWriter(sourceMapOutputFile)) {
            sourceMap.appendTo(out, minifyFileName);
        } catch (IOException e) {
            log.error("Failed to write the JavaScript Source Map file ["
                    + (isVerbose ? sourceMapOutputFile.getPath() : sourceMapOutputFile.getName()) + "].", e);
        }
    }

    @Getter
    public static class Builder {
        private MinifyBundle minifyBundle;
        private MinifyTarget target;
        private Log log;
        private Path outputFolder;

        public Builder withMinifyBundle(MinifyBundle minifyBundle) {
            this.minifyBundle = minifyBundle;
            return this;
        }

        public Builder withMinifyTarget(MinifyTarget target) {
            this.target = target;
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

        public ProcessMinifyTask build() throws MojoExecutionException {
            Objects.requireNonNull(minifyBundle, "MinifyBundle is required!");
            Objects.requireNonNull(target, "MinifyTarget is required!");
            Objects.requireNonNull(log, "Log is required!");
            Objects.requireNonNull(outputFolder, "OutputFolder is required!");
            Objects.requireNonNull(minifyBundle.getType(), "Type of process is required!");
            switch (minifyBundle.getType()) {
                case css:
                    return new ProcessMinifyCssTask(this);
                case js:
                    return new ProcessMinifyJsTask(this);
            }
            return null;
        }
    }
}
