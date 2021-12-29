package com.cmeza.deployer.plugin.minify;

import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

@Data
public class MinifyTarget {
    @Parameter(name = "bundleConfiguration")
    private String bundleConfiguration;

    @Parameter(name = "destinationFolder")
    private String destinationFolder;

    @Parameter(name = "searchIn")
    private String searchIn;

    @Parameter(name = "findInParent", defaultValue = "false")
    private boolean findInParent;
}
