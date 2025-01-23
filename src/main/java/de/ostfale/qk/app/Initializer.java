package de.ostfale.qk.app;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class Initializer {

    private static final Logger log = LoggerFactory.getLogger(Initializer.class);

    @Inject
    ApplicationSetup applicationSetup;

    @Startup
    public void init() {
        log.info("Check existence of application home directory");
        var result = applicationSetup.createApplicationDirectories(FileSystemFacade.getUserHome());
        log.debug("Application directory: {}", result.toFile().getPath());
    }
}
