package com.taboola.wordcounter;

import com.taboola.invoicereader.services.impl.FileHandlerService;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordsCounter {
    private static final Logger logger = Logger.getLogger(WordsCounter.class);
    private static final String WORD_SEPARATOR = " ";

    private static ConcurrentHashMap<String, Integer> wordCount;
    private static ExecutorService executorService;

    public static void main (String [] args) {
        wordCount = new ConcurrentHashMap<String, Integer>();
        WordsCounter wc = new WordsCounter();
        wc.load("D:\\Documents\\Prog\\invoice-reader\\invoice-reader\\input\\word-count\\input1.txt",
                "D:\\Documents\\Prog\\invoice-reader\\invoice-reader\\input\\word-count\\input2.txt",
                "D:\\Documents\\Prog\\invoice-reader\\invoice-reader\\input\\word-count\\input3.txt");
        wc.displayStatus();
    }

    private static void load(String... filePaths) {
        executorService = Executors.newFixedThreadPool((int) Arrays.stream(filePaths).count());
        Map<String, List<String>> fileContents = new FileHandlerService().readFilesAt(List.of(filePaths));
        logger.info("Read " + fileContents.size() + " files");
        logger.info(fileContents.toString());
        executeTasks(fileContents); // since I created the file handler in a way that it waits for the content to be read, I start new threads to utilize the concurrent hashmap
    }

    private static List<Callable<Void>> prepareTasks(Map<String, List<String>> fileContents) {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (List<String> current : fileContents.values()) {
            tasks.add(() -> {
                countOccurrencesInFile(current);
                return null;
            });
        }
        return tasks;
    }

    private static void executeTasks(Map<String, List<String>> fileContents) {
        try {
            executorService.invokeAll(prepareTasks(fileContents));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    private static void countOccurrencesInFile(List<String> lines) {
        List<String> words = collectWordsFromLines(lines);
        words.forEach(word -> {
            wordCount.putIfAbsent(word, 0);
            wordCount.computeIfPresent(word, (key, count) -> count + 1);
        });
    }

    private static List<String> collectWordsFromLines(List<String> lines) {
        return lines.stream()
                .flatMap(line -> Arrays.stream(line.split(WORD_SEPARATOR)))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private static void displayStatus() {
        for(Map.Entry<String, Integer> current : wordCount.entrySet()) {
            logger.info(String.join(" ", current.getKey(), current.getValue().toString()));
        }
    }
}

