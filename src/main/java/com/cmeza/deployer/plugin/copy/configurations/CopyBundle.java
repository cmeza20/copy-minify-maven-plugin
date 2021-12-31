package com.cmeza.deployer.plugin.copy.configurations;

import com.cmeza.deployer.plugin.utils.AbstractBundle;
import lombok.Data;

@Data
public class CopyBundle extends AbstractBundle {
    private String destinationFolder;
    private String searchIn;
    private boolean findInParent;
    private String prefix;
    private String suffix;
}
