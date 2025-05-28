package de.ostfale.qk.ui.tasks;

import de.ostfale.qk.ui.app.StatusBarController;
import de.ostfale.qk.web.common.HttpHandler;
import io.quarkiverse.fx.RunOnFxThread;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class WebStatusCheckerTask {

    private static final String WEB_STATUS_URL = "https://www.heise.de";

    @Inject
    StatusBarController statusBarController;

    @Inject
    HttpHandler httpHandler;

   @Scheduled(every = "300s", delay = 15, delayUnit = TimeUnit.SECONDS)
    void checkInternetConnection() {
        boolean isConnected = httpHandler.checkConnection(WEB_STATUS_URL);
        Log.tracef("Checking health status of internet connection: %s", isConnected ? "UP" : "DOWN");
        updateWSHealthStatus(isConnected);
    }

    @RunOnFxThread
    void updateWSHealthStatus(Boolean status) {
        statusBarController.setInternetStatus(status);
    }
}
