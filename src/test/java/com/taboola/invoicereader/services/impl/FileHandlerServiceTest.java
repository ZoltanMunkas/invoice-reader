package com.taboola.invoicereader.services.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileHandlerServiceTest {

    @InjectMocks
    FileHandlerService fileHandlerService;

    private static final String OUTPUT_PATH = "src/test/resources/test_output.txt";

    @AfterAll
    public static void cleanUp() {
        File file = new File(OUTPUT_PATH);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        }
    }


    @Test
    void writeFileTo() {
        fileHandlerService.writeFileTo(OUTPUT_PATH, List.of("test1\n", "test2"));
        File file = new File(OUTPUT_PATH);
        assertTrue(file.exists());

        List<String> lines = fileHandlerService.readFileAt(OUTPUT_PATH);
        assertEquals(2, lines.size());
        assertEquals("test1", lines.get(0));
        assertEquals("test2", lines.get(1));
    }
}