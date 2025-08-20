package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.ui.app.BaseHandler;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.scene.Node;

@Singleton
public class TourCalHandler implements BaseHandler {

    private static final String APP_VIEW = "tour-cal-view";

    @Inject
    FxViewRepository fxViewRepository;


    @Override
    public Node getRootNode() {
        return fxViewRepository.getViewData(APP_VIEW).getRootNode();
    }
}
