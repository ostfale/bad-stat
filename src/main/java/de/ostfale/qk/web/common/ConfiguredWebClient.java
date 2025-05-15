package de.ostfale.qk.web.common;


import org.htmlunit.WebClient;

public class ConfiguredWebClient {
    private static final WebClient webClient = new WebClient();

    static {
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setPopupBlockerEnabled(false);
    }

    public static WebClient getWebClient() {
        webClient.reset();
        return webClient;
    }
}
