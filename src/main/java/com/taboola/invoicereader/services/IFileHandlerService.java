package com.taboola.invoicereader.services;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface IFileHandlerService {

    /***
     * Read a single file on a single thread using a streamlined version of the multithreaded reader
     * @param path Path of the file to be read
     * @return A list containing the file's lines
     */
    public List<String> readFileAt(String path);


    /***
     * Reads multiple files from the parameter each on a separate thread and returns the values in a map
     * @param filePaths A list containing all files to be read
     * @return A map containing Key-Value pairs where the filename is the key and the content is the value
     */
    public Map<String, List<String>> readFilesAt(List<String> filePaths);

    /***
     * Writes all data from a string list to the desired location
     * @param path The output path
     * @param content The content of the file
     */
    public void writeFileTo(String path, List<String> content);
}
