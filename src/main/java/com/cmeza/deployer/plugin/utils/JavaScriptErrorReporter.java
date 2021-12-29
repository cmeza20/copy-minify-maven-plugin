package com.cmeza.deployer.plugin.utils;

import org.apache.maven.plugin.logging.Log;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

public class JavaScriptErrorReporter implements ErrorReporter {

    private final Log log;

    private final String filename;

    public JavaScriptErrorReporter(Log log, String filename) {
        this.log = log;
        this.filename = filename;
    }

    @Override
    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        log.warn(constructMessage(message, sourceName, line, lineSource, lineOffset));
    }

    @Override
    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        log.error(constructMessage(message, sourceName, line, lineSource, lineOffset));
    }

    @Override
    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
                                           int lineOffset) {
        log.error(message);

        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    private String constructMessage(String message, String sourceName, int line, String lineSource, int lineOffset) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(message).append(" at ");
        if (sourceName != null) {
            stringBuilder.append(sourceName);
        } else if (filename != null) {
            stringBuilder.append(filename);
        } else {
            stringBuilder.append("(unknown source)");
        }
        stringBuilder.append(" line ");
        if (line > 0) {
            stringBuilder.append(line);
        } else {
            stringBuilder.append("(unknown line)");
        }
        stringBuilder.append(":");
        if (lineOffset >= 0) {
            stringBuilder.append(lineOffset);
        } else {
            stringBuilder.append("(unknown column)");
        }
        if (lineSource != null) {
            stringBuilder.append('\n');
            stringBuilder.append(lineSource);
            if (lineOffset >= 0 && lineOffset <= lineSource.length()) {
                stringBuilder.append('\n');
                for (int i = 0; i < lineOffset; i++) {
                    char c = lineSource.charAt(i);
                    if (Character.isWhitespace(c)) {
                        stringBuilder.append(c);
                    } else {
                        stringBuilder.append(' ');
                    }
                }
                stringBuilder.append("^\n");
            }
        }

        return stringBuilder.toString();
    }
}
