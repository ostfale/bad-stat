package de.ostfale.qk.web.internal;

import de.ostfale.qk.web.api.BaseWebUrlFacade;
import de.ostfale.qk.web.common.CookieDialogHandler;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.HtmlPage;

import java.util.List;

@Singleton
public class RankingWebService implements BaseWebUrlFacade {

    @Inject
    CookieDialogHandler cookieDialogHandler;

    public String getCalendarWeekForLastUpdate() {
        HtmlPage page = cookieDialogHandler.loadWebsite(DBV_YOUTH_RANKING_LIST_URL);
        List<FrameWindow> frames = page.getFrames();
        FrameWindow first = frames.getFirst();
        HtmlPage framePage = (HtmlPage) first.getEnclosedPage();

        if (framePage.getAnchors().isEmpty()) {
            Log.error("HTML DBV RANKING :: Website not available");
            return "??";
        }

        String textContent = framePage.getAnchors().getFirst().getTextContent();
        String foundKW = textContent.substring(textContent.indexOf("KW") + 2).trim();
        Log.debugf("HTML DBV RANKING :: found KW: %s", foundKW);
        return foundKW;
    }
}
