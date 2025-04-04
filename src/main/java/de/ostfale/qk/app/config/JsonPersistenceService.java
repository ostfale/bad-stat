package de.ostfale.qk.app.config;

import java.util.Optional;

public interface JsonPersistenceService {

    void writeConfiguration(String targetFilePath,AppConfiguration appConfiguration);

    Optional<AppConfiguration> readConfiguration(String targetFilePath);
}
