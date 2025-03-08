package de.ostfale.qk.parser;

import de.ostfale.qk.web.ConfiguredWebClient;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public abstract class BaseParserTest {

    protected final WebClient webClient = ConfiguredWebClient.getWebClient();

    protected HtmlPage loadHtmlPage(String fileName) {
        try {
            var htmlString = readFileToString(fileName);
            return webClient.loadHtmlCodeIntoCurrentWindow(htmlString);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFileToString(String fileName) throws IOException, URISyntaxException {
        var file = readFile(fileName);
        return Files.readString(file.toPath());
    }

    protected File readFile(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource(fileName), "file not found! " + fileName);
        return new File(resource.toURI());
    }
}
