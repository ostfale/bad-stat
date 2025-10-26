package de.ostfale.qk.data.json;

import de.ostfale.qk.app.io.json.ConfiguredObjectMapper;

import java.nio.file.Path;
import java.util.Optional;

public interface JsonFileWriter<T> {

    boolean writeToFile(T object, String path);

    boolean writeToFile(T object, Path path);

    Optional<Path> writeToFileWithResult(T object, String path);

    ConfiguredObjectMapper getObjectMapper();
}
