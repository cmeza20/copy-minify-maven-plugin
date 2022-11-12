package com.cmeza.deployer.plugin.exec;

import com.cmeza.deployer.plugin.exec.configurations.ExecBundle;
import com.cmeza.deployer.plugin.exec.configurations.ExecConfiguration;
import com.cmeza.deployer.plugin.utils.AbstractTarget;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ExecTarget extends AbstractTarget<ExecBundle> {

    @Builder.Default
    private ExecConfiguration configuration = new ExecConfiguration();

}
