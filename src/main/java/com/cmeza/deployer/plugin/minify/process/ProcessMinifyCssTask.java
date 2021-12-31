package com.cmeza.deployer.plugin.minify.process;

import com.cmeza.deployer.plugin.minify.configurations.MinifyConfiguration;
import com.yahoo.platform.yui.compressor.CssCompressor;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.*;

public class ProcessMinifyCssTask extends ProcessMinifyTask {

    public ProcessMinifyCssTask(Builder builder) throws MojoExecutionException {
        super(builder);
    }

    @Override
    void minify(File mergedFile, File minifiedFile, MinifyConfiguration configuration) throws IOException {

        try (InputStream in = new FileInputStream(mergedFile);
             OutputStream out = new FileOutputStream(minifiedFile);
             InputStreamReader reader = new InputStreamReader(in, charset);
             OutputStreamWriter writer = new OutputStreamWriter(out, charset)) {
            log.info("Creating the minified file [" + (configuration.isVerbose() ? minifiedFile.getPath() : minifiedFile.getName())
                    + "].");

            switch (engine) {
                case YUI:
                    log.debug("Using YUI Compressor engine.");
                    CssCompressor compressor = new CssCompressor(reader);
                    compressor.compress(writer, configuration.getYuiLineBreak());
                    break;
                case CLOSURE:
                    log.warn("CSS engine not supported.");
                    break;
            }
        } catch (IOException e) {
            log.error("Failed to compress the CSS file [" + (configuration.isVerbose() ? mergedFile.getPath() : mergedFile.getName())
                    + "].", e);
            throw e;
        }
    }
}
