package com.cmeza.deployer.plugin.utils;

import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractTarget<E extends AbstractBundle> implements IAbstractTarget<E> {

    @Parameter(name = "bundleConfiguration")
    private String bundleConfiguration;

    @Override
    public String getBundleConfiguration() {
        return this.bundleConfiguration;
    }

    public AbstractTarget<E> setBundleConfiguration(String bundleConfiguration) {
        this.bundleConfiguration = bundleConfiguration;
        return this;
    }
}
