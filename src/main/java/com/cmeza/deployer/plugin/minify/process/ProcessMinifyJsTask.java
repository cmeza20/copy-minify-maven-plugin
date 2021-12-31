package com.cmeza.deployer.plugin.minify.process;

import com.cmeza.deployer.plugin.minify.configurations.MinifyConfiguration;
import com.cmeza.deployer.plugin.utils.JavaScriptErrorReporter;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.jarjar.com.google.common.collect.Lists;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.*;
import java.util.List;

public class ProcessMinifyJsTask extends ProcessMinifyTask {

    public ProcessMinifyJsTask(Builder builder) throws MojoExecutionException {
        super(builder);
    }

    @Override
    void minify(File mergedFile, File minifiedFile, MinifyConfiguration configuration) throws IOException {
        try (InputStream in = new FileInputStream(mergedFile);
             OutputStream out = new FileOutputStream(minifiedFile);
             InputStreamReader reader = new InputStreamReader(in, charset);
             OutputStreamWriter writer = new OutputStreamWriter(out, charset)) {

            log.info("Creating the minified file [" + (configuration.isVerbose() ? minifiedFile.getPath() : minifiedFile.getName()) + "].");

            switch (engine) {
                case CLOSURE:
                    log.debug("Using Google Closure Compiler engine.");

                    CompilerOptions options = new CompilerOptions();

                    CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

                    options.setOutputCharset(charset);
                    options.setLanguageIn(configuration.getClosureLanguageIn());
                    options.setLanguageOut(configuration.getClosureLanguageOut());
                    options.setColorizeErrorOutput(Boolean.TRUE);
                    options.setAngularPass(configuration.isClosureAngularPass());

                    File sourceMapResult = null;
                    if (configuration.isClosureCreateSourceMap()) {
                        sourceMapResult = new File(minifiedFile.getPath() + ".map");
                        options.setSourceMapFormat(SourceMap.Format.V3);
                        options.setSourceMapOutputPath(sourceMapResult.getPath());
                    }

                    SourceFile.Builder builder = SourceFile.builder()
                            .withCharset(charset)
                            .withContent(in)
                            .withPath(mergedFile.getName());
                    SourceFile input = builder.build();

                    Compiler compiler = new Compiler();
                    compiler.compile(CommandLineRunner.getBuiltinExterns(configuration.getClosureEnvironment()), Lists.newArrayList(input), options);
                    compiler.disableThreads();

                    List<JSError> errors = compiler.getErrors();
                    if (!errors.isEmpty()) {
                        StringBuilder msg = new StringBuilder("JSCompiler errors\n");
                        MessageFormatter formatter = new LightweightMessageFormatter(compiler);
                        for (JSError e : errors) {
                            msg.append(formatter.formatError(e));
                        }
                        throw new RuntimeException(msg.toString());
                    }

                    writer.append(compiler.toSource());

                    if (configuration.isClosureCreateSourceMap() && sourceMapResult != null) {
                        log.info("Creating the minified file map ["
                                + (configuration.isVerbose() ? sourceMapResult.getPath() : sourceMapResult.getName()) + "].");

                        if (sourceMapResult.createNewFile()) {
                            flushSourceMap(sourceMapResult, minifiedFile.getName(), compiler.getSourceMap());

                            writer.append(System.getProperty("line.separator"));
                            writer.append("//# sourceMappingURL=").append(sourceMapResult.getName());
                        }
                    }

                    break;
                case YUI:
                    log.debug("Using YUI Compressor engine.");

                    JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new JavaScriptErrorReporter(log,
                            mergedFile.getName()));
                    compressor.compress(writer, configuration.getYuiLineBreak(), configuration.isYuiNoMunge(), configuration.isVerbose(),
                            configuration.isYuiPreserveSemicolons(), configuration.isYuiDisableOptimizations());
                    break;
                default:
                    log.warn("JavaScript engine not supported.");
                    break;
            }
        } catch (IOException e) {
            log.error(
                    "Failed to compress the JavaScript file ["
                            + (configuration.isVerbose() ? mergedFile.getPath() : mergedFile.getName()) + "].", e);
            throw e;
        }
    }
}
