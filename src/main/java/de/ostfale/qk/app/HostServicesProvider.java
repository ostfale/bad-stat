package de.ostfale.qk.app;

import io.quarkiverse.fx.FxApplicationStartupEvent;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import javafx.application.Application;
import javafx.application.HostServices;

@ApplicationScoped
public class HostServicesProvider {

    private HostServices hostServices;

    void onApplicationStartup(@Observes final FxApplicationStartupEvent event) {
        Log.info("HostServicesProvider :: Initializing HostServices");
        Application application = event.getApplication();
        this.hostServices = application.getHostServices();
        Log.info("HostServices initialized: " + (hostServices != null));
    }

    public HostServices getHostServices() {
        return hostServices;
    }
}
