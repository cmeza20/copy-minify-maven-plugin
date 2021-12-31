package com.cmeza.deployer.plugin.copy;

import com.cmeza.deployer.plugin.copy.configurations.CopyBundle;
import com.cmeza.deployer.plugin.utils.AbstractTarget;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

@Data
public class CopyTarget extends AbstractTarget<CopyBundle> {

    @Parameter(name = "bundles")
    private List<CopyBundle> bundles = new ArrayList<>();

}
