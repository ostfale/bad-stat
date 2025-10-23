package de.ostfale.qk.web.common;

import io.quarkus.logging.Log;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;

public class CookieDialogHandler {

    private static final String COOKIE_WALL = "cookiewall";
    private static final String COOKIE_BUTTON_XPATH = "/html/body/div/div/div/main/form/div[1]/button[1]";

    WebClient webClient = ConfiguredWebClient.getWebClient();

    public HtmlPage loadWebsite(String url) {
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
            Log.debug("HTMLUnit :: Check site for cookie wall...");
            if (page.getBaseURL().toExternalForm().contains(COOKIE_WALL)) {
                Log.debugf("HTMLUnit :: Found cookie wall in {}", url);
                acceptCookieWall(webClient, url);
                return webClient.getPage(url);
            }
        } catch (Exception e) {
            Log.errorf("Could not load page: {} because {}", url, e.getMessage());
        }
        return page;
    }

    private void acceptCookieWall(WebClient webClient, String url) throws IOException {
        HtmlPage cookieDialog = webClient.getPage(url);
        var button = cookieDialog.getByXPath(COOKIE_BUTTON_XPATH);
        HtmlButton acceptButton = (HtmlButton) button.getFirst();

        if (acceptButton != null) {
            Log.debug("Cookie Wall :: Accept cookie wall");
            acceptButton.click();
        }
    }
}
