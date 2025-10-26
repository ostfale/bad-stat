package de.ostfale.qk.app.config;

import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigRoot
@ConfigMapping(prefix = "app.directories")
public interface DirectoryConfiguration {

    String basePath();

    List<DirectoryEntry> structure();

    interface DirectoryEntry {
        String path();
        boolean createIfMissing();
        boolean required();
    }
}
