package de.ostfale.qk.app;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped
public class Initializer {

    private static final Logger log = LoggerFactory.getLogger(Initializer.class);

    private static final String APP_DIR_NAME = ".bad_stat";

    private  String rootDir = FileSystemFacade.getUserHome();

    @Startup
    public void init() {
        log.info("Check existence of application home directory");
        var result = createApplicationDirectories(FileSystemFacade.getUserHome());
        log.debug("Application directory: {}", result.toFile().getPath());
    }

    public Path createApplicationDirectories(String rootDir) {
        try {
            if (!Files.isDirectory(getAppDir())) {
                log.info("Initialize application directories...");
                Files.createDirectory(getAppDir());
                var CPath = Files.createDirectory(getAppDir().resolve(DirTypes.CONFIG.displayName));
                var DPath = Files.createDirectory(getAppDir().resolve(DirTypes.DATA.displayName));
                var LPath = Files.createDirectory(getAppDir().resolve(DirTypes.LOG.displayName));
                log.info("Created: \n\tConfig Path: {} \n\tData Path: {} \n\t Log Path; {}", CPath.toFile().getPath(), DPath.toFile().getPath(), LPath.toFile().getPath());
                Files.createDirectory(getAppDir().resolve(DirTypes.DATA.displayName));
                Files.createDirectory(getAppDir().resolve(DirTypes.LOG.displayName));
                Files.createDirectory(getAppDir().resolve(DirTypes.CONFIG.displayName));
                return getAppDir();
            }
        } catch (IOException e) {
            log.error("Error creating application directories: {}", e.getMessage());
        }
        return getAppDir();
    }

    public Path getAppDir() {
        var appDir = Paths.get(rootDir).resolve(APP_DIR_NAME);
        log.info("Created application directory: {}", appDir.toFile().getPath());
        return appDir;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }
}
