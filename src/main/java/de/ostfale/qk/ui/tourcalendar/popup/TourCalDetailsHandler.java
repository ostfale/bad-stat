package de.ostfale.qk.ui.tourcalendar.popup;

import de.ostfale.qk.ui.app.BaseHandler;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.scene.Node;

@ApplicationScoped
public class TourCalDetailsHandler implements BaseHandler {

    private static final String VIEW_FXML = "tour-cal-details";

    private final FxViewRepository fxViewRepository;

    @Inject
    public TourCalDetailsHandler(FxViewRepository fxViewRepository) {
        this.fxViewRepository = fxViewRepository;
    }

    @Override
    public Node getRootNode() {
        return fxViewRepository.getViewData(VIEW_FXML).getRootNode();
    }

    public TourCalDetailsController getController() {
        return fxViewRepository.getViewData(VIEW_FXML).getController();
    }
}
