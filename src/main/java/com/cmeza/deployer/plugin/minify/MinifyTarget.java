package com.cmeza.deployer.plugin.minify;

import com.cmeza.deployer.plugin.minify.configurations.MinifyBundle;
import com.cmeza.deployer.plugin.minify.configurations.MinifyConfiguration;
import com.cmeza.deployer.plugin.minify.constants.MinifyConstants;
import com.cmeza.deployer.plugin.utils.AbstractTarget;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

@Data
public class MinifyTarget extends AbstractTarget<MinifyBundle> {

    @Parameter(name = "destinationFolder")
    private String destinationFolder;

    @Parameter(name = "searchIn")
    private String searchIn;

    @Parameter(name = "findInParent")
    private boolean findInParent = MinifyConstants.FIND_IN_PARENT;

    @Parameter(name = "configuration")
    private MinifyConfiguration configuration = new MinifyConfiguration();

    @Parameter(name = "bundles")
    private List<MinifyBundle> bundles = new ArrayList<>();

}
