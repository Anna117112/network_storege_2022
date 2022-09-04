package com.geekbrains.cloud;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Data
// список файлов
public class ListFiles implements CloudMessage {

    private final List<String> files;
    private final String name;

    public ListFiles(Path path) throws IOException {

        // берем список файов
        files = Files.list(path)
                .map(p -> p.getFileName().toString())// проходим по всем файлам и берем их имя
                .collect(Collectors.toList());// складываем в коллекцию лист
        name = path.toString();
    }



}
