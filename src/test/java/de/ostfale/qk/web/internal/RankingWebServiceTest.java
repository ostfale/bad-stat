// File: RankingWebServiceTest.java
package de.ostfale.qk.web.internal;

import de.ostfale.qk.web.common.CookieDialogHandler;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RankingWebServiceTest {

    /**
     * Tests the {@link RankingWebService#getCalendarWeekForLastUpdate()} method.
     * This method extracts the "calendar week" (KW) from a specific anchor text on the loaded webpage.
     */

    @Test
    void testGetCalendarWeekForLastUpdate_WebsiteNotAvailable() {
        // given
        CookieDialogHandler mockHandler = mock(CookieDialogHandler.class);
        when(mockHandler.loadWebsite(any())).thenReturn(null);

        RankingWebService service = new RankingWebService();
        service.cookieDialogHandler = mockHandler;

        // when
        String result = service.getCalendarWeekForLastUpdate();

        // then
        assertNull(result);
        verify(mockHandler, times(1)).loadWebsite(any());
    }

    @Test
    void testGetCalendarWeekForLastUpdate_NoAnchors() {
        // given
        CookieDialogHandler mockHandler = mock(CookieDialogHandler.class);
        HtmlPage mainPage = mock(HtmlPage.class);
        FrameWindow frameWindow = mock(FrameWindow.class);
        HtmlPage framePage = mock(HtmlPage.class);

        when(mockHandler.loadWebsite(any())).thenReturn(mainPage);
        when(mainPage.getFrames()).thenReturn(List.of(frameWindow));
        when(frameWindow.getEnclosedPage()).thenReturn(framePage);
        when(framePage.getAnchors()).thenReturn(Collections.emptyList());

        RankingWebService service = new RankingWebService();
        service.cookieDialogHandler = mockHandler;

        // when
        String result = service.getCalendarWeekForLastUpdate();

        // then
        assertEquals("??", result);
        verify(mockHandler, times(1)).loadWebsite(any());
        verify(mainPage, times(1)).getFrames();
        verify(framePage, times(1)).getAnchors();
    }

    @Test
    void testGetCalendarWeekForLastUpdate_FoundValidKW() {
        // given
        CookieDialogHandler mockHandler = mock(CookieDialogHandler.class);
        HtmlPage mainPage = mock(HtmlPage.class);
        FrameWindow frameWindow = mock(FrameWindow.class);
        HtmlPage framePage = mock(HtmlPage.class);
        HtmlAnchor anchor = mock(HtmlAnchor.class);

        when(mockHandler.loadWebsite(any())).thenReturn(mainPage);
        when(mainPage.getFrames()).thenReturn(List.of(frameWindow));
        when(frameWindow.getEnclosedPage()).thenReturn(framePage);
        when(framePage.getAnchors()).thenReturn(List.of(anchor));
        when(anchor.getTextContent()).thenReturn("Some text with KW42");

        RankingWebService service = new RankingWebService();
        service.cookieDialogHandler = mockHandler;

        // when
        String result = service.getCalendarWeekForLastUpdate();

        // then
        assertEquals("42", result);
        verify(mockHandler, times(1)).loadWebsite(any());
        verify(mainPage, times(1)).getFrames();
        verify(framePage, times(2)).getAnchors();
    }

    @Test
    void testGetCalendarWeekForLastUpdate_FoundKWButMalformedText() {
        // Arrange
        CookieDialogHandler mockHandler = mock(CookieDialogHandler.class);
        HtmlPage mainPage = mock(HtmlPage.class);
        FrameWindow frameWindow = mock(FrameWindow.class);
        HtmlPage framePage = mock(HtmlPage.class);
        HtmlAnchor anchor = mock(HtmlAnchor.class);

        when(mockHandler.loadWebsite(any())).thenReturn(mainPage);
        when(mainPage.getFrames()).thenReturn(List.of(frameWindow));
        when(frameWindow.getEnclosedPage()).thenReturn(framePage);
        when(framePage.getAnchors()).thenReturn(List.of(anchor));
        when(anchor.getTextContent()).thenReturn("KW text malformed");

        RankingWebService service = new RankingWebService();
        service.cookieDialogHandler = mockHandler;

        // Act
        String result = service.getCalendarWeekForLastUpdate();

        // Assert
        assertEquals("text malformed", result);
        verify(mockHandler, times(1)).loadWebsite(any());
        verify(mainPage, times(1)).getFrames();
        verify(framePage, times(2)).getAnchors();
        verify(anchor, times(1)).getTextContent();
    }

    @Test
    void testGetCalendarWeekForLastUpdate_FramePageIsNull() {
        // given
        CookieDialogHandler mockHandler = mock(CookieDialogHandler.class);
        HtmlPage mainPage = mock(HtmlPage.class);
        FrameWindow frameWindow = mock(FrameWindow.class);

        when(mockHandler.loadWebsite(any())).thenReturn(mainPage);
        when(mainPage.getFrames()).thenReturn(List.of(frameWindow));
        when(frameWindow.getEnclosedPage()).thenReturn(null);

        RankingWebService service = new RankingWebService();
        service.cookieDialogHandler = mockHandler;

        // when & then
        assertThrows(NullPointerException.class, () -> service.getCalendarWeekForLastUpdate());
    }
}
