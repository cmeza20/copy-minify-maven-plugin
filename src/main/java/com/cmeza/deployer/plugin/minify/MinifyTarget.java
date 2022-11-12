package com.cmeza.deployer.plugin.minify;

import com.cmeza.deployer.plugin.minify.configurations.MinifyBundle;
import com.cmeza.deployer.plugin.minify.configurations.MinifyConfiguration;
import com.cmeza.deployer.plugin.minify.constants.MinifyConstants;
import com.cmeza.deployer.plugin.utils.AbstractTarget;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MinifyTarget extends AbstractTarget<MinifyBundle> {

    private String destinationFolder;
    private String searchIn;
    @Builder.Default
    private boolean findInParent = MinifyConstants.FIND_IN_PARENT;
    @Builder.Default
    private MinifyConfiguration configuration = new MinifyConfiguration();

}
