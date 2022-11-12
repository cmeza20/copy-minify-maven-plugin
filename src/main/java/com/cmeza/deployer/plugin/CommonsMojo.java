package com.cmeza.deployer.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class CommonsMojo extends AbstractMojo {

    @Parameter(name = "outputFolder", defaultValue = "${project.basedir}")
    protected File outputFolder;

    @Parameter(name = "versioned", defaultValue = "true")
    protected boolean versioned;

    @Parameter(name = "tasks")
    protected Tasks<?>[] tasks;
}
