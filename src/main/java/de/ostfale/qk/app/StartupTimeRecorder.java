package de.ostfale.qk.app;

import io.quarkiverse.fx.FxPostStartupEvent;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class StartupTimeRecorder {

    private long startupEventTime;

    void onStartup(@Observes StartupEvent ev) {
        startupEventTime = System.currentTimeMillis();
        Log.info("=========== Quarkus StartupEvent fired ===========");
    }

    void onFxPostStartup(@Observes FxPostStartupEvent event) {
        long fxReadyTime = System.currentTimeMillis();
        long totalTime = fxReadyTime - startupEventTime;
        Log.infof("=========== JavaFX fully ready: %d ms after StartupEvent ===========", totalTime);
    }
}
