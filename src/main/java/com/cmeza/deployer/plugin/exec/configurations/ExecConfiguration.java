package com.cmeza.deployer.plugin.exec.configurations;

import com.cmeza.deployer.plugin.exec.constants.ExecConstants;
import lombok.Data;

@Data
public class ExecConfiguration {
    private boolean verbose = ExecConstants.VERBOSE;
}
