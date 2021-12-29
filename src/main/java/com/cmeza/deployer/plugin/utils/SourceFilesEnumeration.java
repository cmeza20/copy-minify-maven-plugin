package com.cmeza.deployer.plugin.utils;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

public class SourceFilesEnumeration implements Enumeration<InputStream> {

    private final List<File> files;

    private int current = 0;

    public SourceFilesEnumeration(Log log, List<File> files, boolean verbose) {
        this.files = files;

        for (File file : files) {
            log.info("Processing source file [" + ((verbose) ? file.getPath() : file.getName()) + "].");
        }
    }

    @Override
    public boolean hasMoreElements() {
        return (current < files.size());
    }

    @Override
    public InputStream nextElement() {
        InputStream is;

        if (!hasMoreElements()) {
            throw new NoSuchElementException("No more files!");
        } else {
            File nextElement = files.get(current);
            current++;

            try {
                is = new FileInputStream(nextElement);
            } catch (FileNotFoundException e) {
                throw new NoSuchElementException("The path [" + nextElement.getPath() + "] cannot be found.");
            }
        }

        return is;
    }
}
