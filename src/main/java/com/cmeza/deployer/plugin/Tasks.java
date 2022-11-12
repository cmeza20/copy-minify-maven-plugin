package com.cmeza.deployer.plugin;

import com.cmeza.deployer.plugin.exec.configurations.ExecConfiguration;
import com.cmeza.deployer.plugin.minify.configurations.MinifyConfiguration;
import com.cmeza.deployer.plugin.minify.constants.MinifyConstants;
import com.cmeza.deployer.plugin.utils.AbstractBundle;
import com.cmeza.deployer.plugin.utils.AbstractTarget;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.maven.plugins.annotations.Parameter;

@Data
@NoArgsConstructor
public class Tasks<E extends AbstractBundle> extends AbstractTarget<E> {

    @Parameter(name = "exec-configuration")
    private ExecConfiguration execConfiguration = new ExecConfiguration();

    @Parameter(name = "minify-configuration")
    private MinifyConfiguration minifyConfiguration = new MinifyConfiguration();

    @Parameter(name = "destinationFolder")
    private String destinationFolder;

    @Parameter(name = "searchIn")
    private String searchIn;

    @Parameter(name = "findInParent")
    private boolean findInParent = MinifyConstants.FIND_IN_PARENT;

}
