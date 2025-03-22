package de.ostfale.qk.ui.tasks;

import de.ostfale.qk.ui.app.StatusBarController;
import io.quarkiverse.fx.RunOnFxThread;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@ApplicationScoped
public class WebStatusChecker {

    private static final Logger log = Logger.getLogger(WebStatusChecker.class);
    private static final String WEB_STATUS_URL = "https://www.heise.de";

    @Inject
    StatusBarController statusBarController;

    @Scheduled(every = "5m", delay = 5)
    public void checkInternetConnection() {
        log.info("Checking health status of the website...");

        try {
            URI uri = URI.create(WEB_STATUS_URL); // Create and validate the URI.
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                log.tracef("Successfully reached %s with response code %d", WEB_STATUS_URL, responseCode);
                updateWSHealthStatus(true);
            } else {
                log.warnf("Failed to reach %s with response code %d", WEB_STATUS_URL, responseCode);
                updateWSHealthStatus(false);
                statusBarController.setInternetStatus(false);
            }

        } catch (IOException e) {
            log.errorf("Error checking status for %s: %s", WEB_STATUS_URL, e.getMessage());
            updateWSHealthStatus(false);
        }
    }

    @RunOnFxThread
    void updateWSHealthStatus(Boolean status) {
        statusBarController.setInternetStatus(status);

    }
}
