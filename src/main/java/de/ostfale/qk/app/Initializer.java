package de.ostfale.qk.app;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class Initializer {

    private static final Logger log = Logger.getLogger(Initializer.class);

    @Inject
    ApplicationSetup applicationSetup;

    @Inject
    DevSimulation devSimulation;

    @Startup
    public void init() {
        log.info("Check existence of application home directory");
        var result = applicationSetup.createApplicationDirectories(FileSystemFacade.getUserHome());

        var profile = ConfigUtils.isProfileActive("dev");
        if (profile) {
            log.infof("Running Application in DEV mode. Application directory: %s");
            devSimulation.loadSimulationData();
        }

        log.debugf("Application directory: {}", result.toFile().getPath());
    }
}
