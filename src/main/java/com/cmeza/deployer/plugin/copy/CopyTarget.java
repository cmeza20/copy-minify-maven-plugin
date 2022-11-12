package com.cmeza.deployer.plugin.copy;

import com.cmeza.deployer.plugin.copy.configurations.CopyBundle;
import com.cmeza.deployer.plugin.utils.AbstractTarget;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CopyTarget extends AbstractTarget<CopyBundle> {

}
