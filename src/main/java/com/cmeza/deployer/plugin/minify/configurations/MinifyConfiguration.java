package com.cmeza.deployer.plugin.minify.configurations;

import com.cmeza.deployer.plugin.minify.constants.MinifyConstants;
import com.cmeza.deployer.plugin.minify.enums.Engine;
import com.google.javascript.jscomp.CompilerOptions;
import lombok.Data;

@Data
public class MinifyConfiguration {
    private String charset = MinifyConstants.CHARSET;
    private boolean verbose = MinifyConstants.VERBOSE;
    private int bufferSize = MinifyConstants.BUFFER_SIZE;
    private boolean keepMerged = MinifyConstants.KEPP_MERGED;

    //YUI
    private int yuiLineBreak = MinifyConstants.CSS_YUI_LINE_BREAK;
    private boolean yuiNoMunge = MinifyConstants.CSS_YUI_NO_MUNGE;
    private boolean yuiPreserveSemicolons = MinifyConstants.CSS_YUI_PRESERVE_SEMICOLONS;
    private boolean yuiDisableOptimizations = MinifyConstants.CSS_YUI_DISABLE_OPTIMIZATIONS;

    //CLOSURE
    private CompilerOptions.LanguageMode closureLanguageIn = MinifyConstants.JS_CLOSURE_LANGUAGE_IN;
    private CompilerOptions.LanguageMode closureLanguageOut = MinifyConstants.JS_CLOSURE_LANGUAGE_OUT;
    private CompilerOptions.Environment closureEnvironment = MinifyConstants.JS_CLOSURE_ENVIRONMENT;
    private boolean closureCreateSourceMap = MinifyConstants.JS_CLOSURE_CREATE_SOURCEMAP;
    private boolean closureAngularPass = MinifyConstants.JS_CLOSURE_ANGULAR_PASS;
    private Engine cssEngine = MinifyConstants.CSS_ENGINE;
    private Engine jsEngine = MinifyConstants.JS_ENGINE;
}
