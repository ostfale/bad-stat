package de.ostfale.qk.ui.popup.points;

import de.ostfale.qk.ui.app.BaseHandler;
import de.ostfale.qk.ui.popup.tourdetails.TourCalDetailsController;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.scene.Node;

@ApplicationScoped
public class PointsViewHandler implements BaseHandler {

    private static final String VIEW_FXML = "points-view";

    private final FxViewRepository fxViewRepository;

    @Inject
    public PointsViewHandler(FxViewRepository fxViewRepository) {
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
