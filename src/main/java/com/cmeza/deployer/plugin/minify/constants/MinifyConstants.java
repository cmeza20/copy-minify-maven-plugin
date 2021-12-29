package com.cmeza.deployer.plugin.minify.constants;

import com.cmeza.deployer.plugin.minify.enums.Engine;
import com.google.javascript.jscomp.CompilerOptions;

import java.nio.charset.Charset;

public class MinifyConstants {

    public static final String CHARSET = Charset.defaultCharset().name();
    public static final int BUFFER_SIZE = 4096;
    public static final boolean VERBOSE = false;

    //CSS
    public static final Engine CSS_ENGINE = Engine.YUI;
    public static final int CSS_YUI_LINE_BREAK = -1;
    public static final boolean CSS_YUI_NO_MUNGE = false;
    public static final boolean CSS_YUI_PRESERVE_SEMICOLONS = false;
    public static final boolean CSS_YUI_DISABLE_OPTIMIZATIONS = false;

    //JS
    public static final Engine JS_ENGINE = Engine.CLOSURE;
    public static final CompilerOptions.LanguageMode JS_CLOSURE_LANGUAGE_IN = CompilerOptions.LanguageMode.STABLE_IN;
    public static final CompilerOptions.LanguageMode JS_CLOSURE_LANGUAGE_OUT = CompilerOptions.LanguageMode.ECMASCRIPT5;
    public static final CompilerOptions.Environment JS_CLOSURE_ENVIRONMENT = CompilerOptions.Environment.BROWSER;
    public static final boolean JS_CLOSURE_CREATE_SOURCEMAP = false;
    public static final boolean JS_CLOSURE_ANGULAR_PASS = false;


}
