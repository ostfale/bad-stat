package de.ostfale.qk.ui;

import de.ostfale.qk.ui.statistics.PlayerTournamentsStatisticsHandler;
import de.ostfale.qk.ui.statistics.PlayerTournamentsStatisticsModel;
import de.ostfale.qk.ui.statistics.PlayerTournamentsStatisticsTreeTableController;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jboss.logging.Logger;

@FxView("app-view")
@Dependent
public class ApplicationController {

    private static final Logger log = Logger.getLogger(ApplicationController.class);

    @FXML
    Parent root;

    @FXML
    AnchorPane centerAnchorPane;

    @Inject
    PlayerTournamentsStatisticsHandler playerTourStatsHandler;

    @FXML
    public void initialize() {
        Stage dashboardStage = createStageWithScene();
        configureStage(dashboardStage);
        log.info("Dashboard stage successfully initialized and shown.");
        dashboardStage.show();
    }

    @FXML
    void showDashboardView(ActionEvent event) {
        log.info("Show Dashboard View");
    }

    @FXML
    void showRankingView(ActionEvent event) {
        log.info("Show Ranking View");
    }

    @FXML
    void showStatisticsView(ActionEvent event) {
        log.info("Show Statistics View");
        centerAnchorPane.getChildren().setAll(playerTourStatsHandler.getUI());
        playerTourStatsHandler.refreshUI();
    }

    @FXML
    void showTournaments(ActionEvent event) {
        log.info("Show Tournaments View");
    }

    @FXML
    void showFavorites(ActionEvent event) {
        log.info("Show Favorites View");
    }

    @FXML
    void closeApplication(ActionEvent event) {
        System.exit(0);
    }

    private Stage createStageWithScene() {
        final String STYLESHEET_PATH = "/static/css/app-view.css";
        final String ICON_PATH = "images/shuttle_logo.jpg";
        Stage stage = new Stage();
        Scene scene = new Scene(this.root);
        scene.getStylesheets().add(STYLESHEET_PATH);
        stage.setScene(scene);
        stage.getIcons().add(new Image(ICON_PATH));
        return stage;
    }

    private void configureStage(Stage stage) {
        stage.setTitle("Badminton Statistics");
    }
}
