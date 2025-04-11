package de.ostfale.qk.ui.tasks;

import de.ostfale.qk.ui.app.StatusBarController;
import de.ostfale.qk.web.HttpHandler;
import io.quarkiverse.fx.RunOnFxThread;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class WebStatusChecker {

    private static final Logger log = Logger.getLogger(WebStatusChecker.class);
    private static final String WEB_STATUS_URL = "https://www.heise.de";

    @Inject
    StatusBarController statusBarController;

    @Inject
    HttpHandler httpHandler;

   @Scheduled(every = "300s", delay = 30, delayUnit = TimeUnit.SECONDS)
    void checkInternetConnection() {
        log.info("Checking health status of the website...");
        boolean isConnected = checkWebsiteHealth(WEB_STATUS_URL);
        updateWSHealthStatus(isConnected);
    }

    private boolean checkWebsiteHealth(String websiteUrl) {
        try {
            HttpURLConnection connection = httpHandler.openHttpConnection(websiteUrl);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                log.tracef("Successfully reached %s with response code %d", websiteUrl, responseCode);
                return true;
            } else {
                log.warnf("Failed to reach %s with response code %d", websiteUrl, responseCode);
                return false;
            }
        } catch (IOException e) {
            log.errorf("Error checking status for %s: %s", websiteUrl, e.getMessage());
            return false;
        }
    }

    @RunOnFxThread
    void updateWSHealthStatus(Boolean status) {
        statusBarController.setInternetStatus(status);
    }
}
