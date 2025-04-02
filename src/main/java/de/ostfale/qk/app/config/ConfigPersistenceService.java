package de.ostfale.qk.app.config;

import java.nio.file.Path;
import java.util.Optional;

public interface ConfigPersistenceService {

    Path writeConfiguration(String targetFilePath,AppConfiguration appConfiguration);

    Optional<AppConfiguration> readConfiguration();
}
