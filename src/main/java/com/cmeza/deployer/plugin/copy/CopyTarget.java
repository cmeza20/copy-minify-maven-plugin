package com.cmeza.deployer.plugin.copy;

import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

@Data
public class CopyTarget {

    @Parameter(name = "destinationFolder")
    private String destinationFolder;

    @Parameter(name = "searchIn")
    private String searchIn;

    @Parameter(name = "findInParent", defaultValue = "false")
    private boolean findInParent;

    @Parameter(name = "prefix")
    private String prefix;

    @Parameter(name = "suffix")
    private String suffix;

}
