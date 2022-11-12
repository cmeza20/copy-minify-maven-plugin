package com.cmeza.deployer.plugin.utils;

import com.cmeza.deployer.plugin.minify.enums.TaskType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractTarget<E extends AbstractBundle> implements IAbstractTarget<E> {

    @Parameter(name = "name")
    private String name;

    @Parameter(name = "type")
    private TaskType type;

    @Parameter(name = "bundleConfiguration")
    private String bundleConfiguration;

    @Parameter(name = "bundles")
    private List<E> bundles;

    @Override
    public String getBundleConfiguration() {
        return this.bundleConfiguration;
    }

    public AbstractTarget<E> setBundleConfiguration(String bundleConfiguration) {
        this.bundleConfiguration = bundleConfiguration;
        return this;
    }

    @Override
    public List<E> getBundles() {
        return bundles;
    }

    public AbstractTarget<E> setBundles(List<E> bundles) {
        this.bundles = bundles;
        return this;
    }

    @Override
    public TaskType getTaskType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
}
