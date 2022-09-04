package com.geekbrains.cloud.netty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class maiTest {
    private static Path currentDir;
    public static void main(String[] args) throws IOException {
        currentDir = Path.of("server_files");
        String s = "Jak";
        Files.createDirectory(currentDir.resolve(s));

    }
}
