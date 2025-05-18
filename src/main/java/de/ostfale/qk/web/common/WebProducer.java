package de.ostfale.qk.web.common;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class WebProducer {

    @Startup
    @Produces
    CookieDialogHandler cookieDialogHandler = new CookieDialogHandler();
}
