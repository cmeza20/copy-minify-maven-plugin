package com.cmeza.deployer.plugin;

import com.cmeza.deployer.plugin.copy.CopyTarget;
import com.cmeza.deployer.plugin.minify.MinifyTarget;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class CommonsMojo extends AbstractMojo {

    @Parameter(name = "outputFolder", defaultValue = "${project.basedir}")
    protected File outputFolder;

    @Parameter(name = "copy")
    protected CopyTarget[] copy;

    @Parameter(name = "minify")
    protected MinifyTarget minify;

}
