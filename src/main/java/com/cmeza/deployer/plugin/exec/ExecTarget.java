package com.cmeza.deployer.plugin.exec;

import com.cmeza.deployer.plugin.exec.configurations.ExecBundle;
import com.cmeza.deployer.plugin.exec.configurations.ExecConfiguration;
import com.cmeza.deployer.plugin.utils.AbstractTarget;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExecTarget extends AbstractTarget<ExecBundle> {

    @Parameter(name = "configuration")
    private ExecConfiguration configuration = new ExecConfiguration();

    @Parameter(name = "bundles")
    private List<ExecBundle> bundles = new ArrayList<>();

}
