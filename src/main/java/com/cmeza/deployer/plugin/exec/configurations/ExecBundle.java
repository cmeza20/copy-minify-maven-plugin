package com.cmeza.deployer.plugin.exec.configurations;

import com.cmeza.deployer.plugin.utils.AbstractBundle;
import lombok.Data;

@Data
public class ExecBundle extends AbstractBundle {
    private String searchIn;
    private boolean findInParent;
    private String command;
}
