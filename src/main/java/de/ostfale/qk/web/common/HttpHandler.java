package de.ostfale.qk.web.common;

import io.quarkus.logging.Log;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Singleton
public class HttpHandler {

    private static final int CONNECTION_TIMEOUT_MS = 5*1000;
    
    public HttpURLConnection openHttpConnection(String websiteUrl) throws IOException {
        Log.debugf("Opening connection to %s", websiteUrl);
        URI uri = URI.create(websiteUrl); // Create and validate the URI.
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.setConnectTimeout(CONNECTION_TIMEOUT_MS);
        connection.setReadTimeout(CONNECTION_TIMEOUT_MS);
        return connection;
    }

    public boolean checkConnection(String websiteUrl) {
        HttpURLConnection connection = null;
        try {
            connection = openHttpConnection(websiteUrl);
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 400;
        } catch (IOException e) {
            Log.errorf("Error checking connection to %s: %s", websiteUrl, e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
