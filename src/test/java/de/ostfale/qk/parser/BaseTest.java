package de.ostfale.qk.parser;

import de.ostfale.qk.web.common.ConfiguredWebClient;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public abstract class BaseTest {

    protected final WebClient webClient = ConfiguredWebClient.getWebClient();

    protected static final String PLAYER_ID = "06-153648";
    protected static final String PLAYER_TOURNAMENT_ID = "65c792c7-e2a7-47d0-95eb-c8b591022523";
    protected static final String PLAYER_NAME = "Victoria Braun";

    protected HtmlPage loadHtmlPage(String fileName) {
        try {
            var htmlString = readFile(fileName);
            return webClient.loadHtmlCodeIntoCurrentWindow(htmlString);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFile(String fileName) throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource(fileName), "file not found! " + fileName);
        var file = new File(resource.toURI());
        return Files.readString(file.toPath());
    }
}
