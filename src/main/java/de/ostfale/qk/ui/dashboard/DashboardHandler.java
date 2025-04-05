package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.ui.app.BaseHandler;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.scene.Node;
import org.jboss.logging.Logger;

@Singleton
public class DashboardHandler implements BaseHandler {

    private static final Logger log = Logger.getLogger(DashboardHandler.class);

    private static final String DASHBOARD_FXML = "dashboard-view";

    @Inject
    FxViewRepository fxViewRepository;

    @Override
    public Node getRootNode() {
        return fxViewRepository.getViewData(DASHBOARD_FXML).getRootNode();
    }

}
