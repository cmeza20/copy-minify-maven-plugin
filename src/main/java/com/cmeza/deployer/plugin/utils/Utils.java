package com.cmeza.deployer.plugin.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HHmmss");

    public static Path getAbsolutePath(boolean isParent) throws Exception {
        File root = new File(".");
        if (isParent) {
            return Paths.get(root.getCanonicalPath()).getParent();
        }
        return Paths.get(root.getCanonicalPath());
    }

    public static Path getAbsolutePath(boolean isParent, String folder) throws Exception {
        if (StringUtils.isEmpty(folder)) {
            return getAbsolutePath(isParent);
        }
        return Paths.get(FilenameUtils.concat(getAbsolutePath(isParent).toString(), folder));
    }

    public static Path concat(Path path, String destinationFolder) {
        if (!StringUtils.isEmpty(destinationFolder)) {
            return Paths.get(FilenameUtils.concat(path.toString(), destinationFolder));
        }
        return path;
    }

    public static List<Path> filter(Path path, String prefix, String suffix) {
        try {
            return Files.find(
                            path,
                            Integer.MAX_VALUE,
                            ((p, basicFileAttributes) -> {
                                if (Files.isDirectory(p) || !Files.isReadable(p) || p.equals(path)) {
                                    return false;
                                }
                                if (!StringUtils.isEmpty(prefix) || !StringUtils.isEmpty(suffix)) {
                                    boolean startsWith = false;
                                    boolean endsWith = false;
                                    if (!StringUtils.isEmpty(prefix)) {
                                        startsWith = p.getFileName().toString().startsWith(prefix);
                                    }
                                    if (!StringUtils.isEmpty(suffix)) {
                                        endsWith = p.getFileName().toString().endsWith(suffix);
                                    }
                                    return startsWith || endsWith;
                                }
                                return true;
                            }))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }

    public static void printTitle(String title, Log log) {
        String lines = String.join("", Collections.nCopies(title.length(), "-"));
        log.info(lines);
        log.info(title);
        log.info(lines);
    }

    public static Path getOutputFolder(File outputFolder, boolean versioned) throws MojoExecutionException {
        if (versioned) {
            outputFolder = new File(FilenameUtils.concat(outputFolder.getAbsolutePath(), format.format(new Date())));
        }
        FileUtils.deleteQuietly(outputFolder);
        createDirectoryIfNotExists(outputFolder);
        return Paths.get(outputFolder.getAbsolutePath());
    }

    public static void createDirectoryIfNotExists(File file) throws MojoExecutionException {
        if (!file.exists()) {
            boolean createFolder = file.mkdirs();
            if (!createFolder) {
                throw new MojoExecutionException(file.getAbsolutePath() + " - The folder cannot be created");
            }
        }
    }

    public static String concatName(String name) {
        if (StringUtils.isEmpty(name)) return "";
        return String.format(" - [Name: %s]", name);
    }
}
