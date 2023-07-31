package com.taboola.invoicereader.services.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.taboola.invoicereader.runnables.FileReaderTask;
import com.taboola.invoicereader.services.IFileHandlerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FileHandlerService implements IFileHandlerService {
    private static final Logger logger = Logger.getLogger(FileHandlerService.class);

    @Override
    public List<String> readFileAt(String path) {
        logger.info("Reading file at " + path);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<String>> future = executor.submit(new FileReaderTask(path));
        return processSingularFuture(future, path);
    }

    @Override
    public Map<String, List<String>> readFilesAt(List<String> filePaths) {
        Map<String, Future<List<String>>> futuresPerPath = readFilesOnThreads(filePaths);
        return processFutures(futuresPerPath);
    }

    @Override
    public void writeFileTo(String path, List<String> content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String line : content) {
                writer.write(line);
            }
        } catch (IOException e) {
            logger.error("Could not save file at " + path, e);
        }
    }

    /***
     * Read all files from the list of input paths, one file/thread
     * @param filePaths A list containing all files to be read
     * @return A map containing the filename and the future holding the file content
     */
    private Map<String, Future<List<String>>> readFilesOnThreads(List<String> filePaths) {
        ExecutorService executor = Executors.newFixedThreadPool(filePaths.size());
        Map<String, Future<List<String>>> results = new HashMap<>();

        for (String path : filePaths) {
            Future<List<String>> future = executor.submit(new FileReaderTask(path));
            results.put(path, future);
        }
        executor.shutdown();
        return results;
    }

    /***
     * Processes the Futures that hold the file values
     * @param rawResult A map contining Key-Value pairs where the filename is the key and the Future of the content is the value
     * @return A map containing Key-Value pairs where the filename is the key and the content is the value
     */
    private Map<String, List<String>> processFutures(Map<String, Future<List<String>>> rawResult) {
        Map<String, List<String>> processedResult = new HashMap<>();
        for (String current : rawResult.keySet()) {
            Future<List<String>> future = rawResult.get(current);
            try {
                List<String> fileContents = future.get();
                processedResult.put(current, fileContents);
            } catch (Exception e) {
                logger.error("Error while processing future for " + current, e);
            }
        }
        return processedResult;
    }

    private List<String> processSingularFuture(Future<List<String>> rawResult, String path) {
        Map<String, List<String>> processedResult = processFutures(Map.of(path, rawResult));
        return processedResult.get(path);
    }

}
