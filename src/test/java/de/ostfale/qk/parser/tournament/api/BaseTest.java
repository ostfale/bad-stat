package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.ConfiguredWebClient;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

abstract class BaseTest {

    protected final WebClient webClient = ConfiguredWebClient.getWebClient();

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
