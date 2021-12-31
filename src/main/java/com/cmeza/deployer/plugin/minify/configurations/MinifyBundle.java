package com.cmeza.deployer.plugin.minify.configurations;

import com.cmeza.deployer.plugin.minify.enums.MinifyType;
import com.cmeza.deployer.plugin.utils.AbstractBundle;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class MinifyBundle extends AbstractBundle {
    private MinifyType type;
    private String name;
    private List<String> files = Collections.emptyList();
}
