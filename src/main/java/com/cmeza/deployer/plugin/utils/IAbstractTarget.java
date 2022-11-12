package com.cmeza.deployer.plugin.utils;

import com.cmeza.deployer.plugin.minify.enums.TaskType;

import java.util.List;

public interface IAbstractTarget<T extends AbstractBundle> {
    String getBundleConfiguration();

    List<T> getBundles();

    TaskType getTaskType();

    String getName();
}
