package de.ostfale.qk.web.internal;

import de.ostfale.qk.web.CookieDialogHandler;
import jakarta.inject.Singleton;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.HtmlPage;
import org.jboss.logging.Logger;

import java.util.List;

@Singleton
public class RankingWebService {

    private static final Logger log = Logger.getLogger(RankingWebService.class);

    String BASE_RANKING_DOWNLOAD_URL = "https://www.badminton.de/der-dbv/jugend-wettkampf/ranglistentabelle/";

    private final CookieDialogHandler cookieDialogHandler = new CookieDialogHandler();

    public String getCalendarWeekForLastUpdate() {
        HtmlPage page = cookieDialogHandler.loadWebsite(BASE_RANKING_DOWNLOAD_URL);
        List<FrameWindow> frames = page.getFrames();
        FrameWindow first = frames.getFirst();
        HtmlPage framePage = (HtmlPage) first.getEnclosedPage();
        String textContent = framePage.getAnchors().getFirst().getTextContent();
        String foundKW = textContent.substring(textContent.indexOf("KW") + 2).trim();
        log.debugf("HTML DBV RANKING :: found KW: %s", foundKW);
        return foundKW;
    }
}
