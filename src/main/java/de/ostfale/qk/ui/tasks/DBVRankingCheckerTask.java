package de.ostfale.qk.ui.tasks;

import de.ostfale.qk.ui.app.StatusBarController;
import de.ostfale.qk.web.api.BaseWebUrlFacade;
import de.ostfale.qk.web.common.HttpHandler;
import io.quarkiverse.fx.RunOnFxThread;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class DBVRankingCheckerTask implements BaseWebUrlFacade {

    @Inject
    StatusBarController statusBarController;

    @Inject
    HttpHandler httpHandler;

    @Scheduled(every = "300s", delay = 15, delayUnit = TimeUnit.SECONDS)
    void checkInternetConnection() {
        boolean isConnected = httpHandler.checkConnection(DBV_YOUTH_RANKING_LIST_URL);
        Log.tracef("Checking health status of internet connection: %s", isConnected ? "UP" : "DOWN");
        updateStatus(isConnected);
    }

    @RunOnFxThread
    void updateStatus(Boolean status) {
        statusBarController.setDBVRankingWebsiteStatus(status);
    }
}
