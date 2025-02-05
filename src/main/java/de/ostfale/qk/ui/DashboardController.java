package de.ostfale.qk.ui;

import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jboss.logging.Logger;

@FxView("dashboard-view")
@Dependent
public class DashboardController {

    private static final Logger log = Logger.getLogger(DashboardController.class);

    @FXML
    Parent root;

    @FXML
    public void initialize() {
        Stage stage = new Stage();
        Scene scene = new Scene(this.root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        log.info("Dashboard initialized");
        stage.show();
    }
}
