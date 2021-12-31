package com.cmeza.deployer.plugin.utils;

import java.util.List;

public interface IAbstractTarget<T extends AbstractBundle> {
    String getBundleConfiguration();

    List<T> getBundles();
}
