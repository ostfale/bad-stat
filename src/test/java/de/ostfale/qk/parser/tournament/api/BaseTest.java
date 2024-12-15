package de.ostfale.qk.parser.tournament.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

abstract class BaseTest {

    protected String readFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource(fileName), "file not found! " + fileName);

        try {
            var file = new File(resource.toURI());
            return Files.readString(file.toPath());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
