package com.cmeza.deployer.plugin.exec.process;

import com.cmeza.deployer.plugin.exec.ExecTarget;
import com.cmeza.deployer.plugin.exec.configurations.ExecBundle;
import com.cmeza.deployer.plugin.exec.configurations.ExecConfiguration;
import com.cmeza.deployer.plugin.utils.Utils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class ProcessExecTask implements Callable<Object> {

    private final Log log;
    private final ExecBundle execBundle;
    private final ExecConfiguration configuration;
    private final String name;

    public ProcessExecTask(Builder builder) {
        this.log = builder.getLog();
        this.execBundle = builder.getExecBundle();
        this.configuration = builder.getExecTarget().getConfiguration();
        this.name = builder.execTarget.getName();
    }

    @Override
    public Object call() throws Exception {
        synchronized (log) {

            Utils.printTitle("Starting EXEC task: " + execBundle.getCommand() + Utils.concatName(name), log);

            Path folder = Utils.getAbsolutePath(execBundle.isFindInParent(), execBundle.getSearchIn());

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(folder.toFile());
            if (SystemUtils.IS_OS_WINDOWS) {
                processBuilder.command("cmd.exe", "/c", execBundle.getCommand());
            } else {
                processBuilder.command("bash", "-c", execBundle.getCommand());
            }

            Process process = processBuilder.start();
            List<String> lines = new LinkedList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                if (configuration.isVerbose()) {
                    lines.forEach(System.out::println);
                }
                log.info("Executed!");
            } else {
                lines.forEach(System.out::println);
                log.error("Executed with errors!");
                System.exit(0);
            }

            log.info("");
        }
        return null;
    }

    @Getter
    public static class Builder {
        private ExecTarget execTarget;
        private ExecBundle execBundle;
        private Log log;

        public Builder withExecBundle(ExecBundle execBundle) {
            this.execBundle = execBundle;
            return this;
        }

        public Builder withExecTarget(ExecTarget execTarget) {
            this.execTarget = execTarget;
            return this;
        }

        public Builder withLog(Log log) {
            this.log = log;
            return this;
        }

        public ProcessExecTask build() throws MojoExecutionException {
            Objects.requireNonNull(execBundle, "ExecBundle is required!");
            Objects.requireNonNull(execTarget, "ExecTarget is required!");
            Objects.requireNonNull(log, "Log is required!");
            if (StringUtils.isEmpty(execBundle.getCommand())) {
                throw new MojoExecutionException("Command field is required!");
            }
            return new ProcessExecTask(this);
        }
    }
}
