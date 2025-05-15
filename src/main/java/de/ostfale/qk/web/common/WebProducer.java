package de.ostfale.qk.web.common;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class WebProducer {

    @Produces
    CookieDialogHandler cookieDialogHandler = new CookieDialogHandler();
}
