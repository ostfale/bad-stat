package de.ostfale.qk.app;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class Initializer {

    private static final Logger log = Logger.getLogger(Initializer.class);

    @Inject
    ApplicationSetup applicationSetup;

    @Startup
    public void init() {
        log.info("Check existence of application home directory");
        var result = applicationSetup.createApplicationDirectories(FileSystemFacade.getUserHome());
        log.debugf("Application directory: {}", result.toFile().getPath());
    }
}
