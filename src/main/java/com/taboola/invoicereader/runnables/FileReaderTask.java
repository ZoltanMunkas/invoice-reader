package com.taboola.invoicereader.runnables;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/***
 * This Task reads the file from the path given in the constructor and returns the lines
 */
public class FileReaderTask implements Callable<List<String>> {
    private static final Logger logger = Logger.getLogger(FileReaderTask.class);

    private String path;

    public FileReaderTask(String path) {
        this.path = path;
    }

    @Override
    public List<String> call() {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            logger.error("Error reading file at " + path, e);
        }
        return lines;
    }
}
