package de.ostfale.qk.web;

import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Singleton
public class HttpHandler {

    private static final Logger log = Logger.getLogger(HttpHandler.class);

    private static final int CONNECTION_TIMEOUT_MS = 5000;


    public HttpURLConnection openHttpConnection(String websiteUrl) throws IOException {
        log.debugf("Opening connection to %s", websiteUrl);
        URI uri = URI.create(websiteUrl); // Create and validate the URI.
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.setConnectTimeout(CONNECTION_TIMEOUT_MS);
        connection.setReadTimeout(CONNECTION_TIMEOUT_MS);
        return connection;
    }
}
