package de.ostfale.qk.ui;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import de.ostfale.qk.ui.app.StatusBarController;
import de.ostfale.qk.ui.dashboard.DashboardHandler;
import de.ostfale.qk.ui.statistics.PlayerStatisticsHandler;
import de.ostfale.qk.ui.statistics.StatisticsController;
import io.quarkiverse.fx.FxPostStartupEvent;
import io.quarkiverse.fx.views.FxView;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@Singleton
@FxView("app-view")
public class ApplicationController {

    private static final Logger log = Logger.getLogger(ApplicationController.class);

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    @FXML
    Parent root;

    @FXML
    private BorderPane bpApp;

    @FXML
    AnchorPane centerAnchorPane;


    @Inject
    FxViewRepository fxViewRepository;

    @Inject
    DashboardHandler dashboardHandler;


    @Inject
    PlayerStatisticsHandler playerTourStatsHandler;


    @Inject
    StatisticsController statisticsController;

    @Inject
    StatusBarController statusBarController;

    @FXML
    public void initialize() {
        Stage dashboardStage = createStageWithScene();
        log.info("Dashboard stage successfully initialized and shown.");
        bpApp.setBottom(statusBarController);
        dashboardStage.show();
    }


    void onPostStartup(@Observes final FxPostStartupEvent event) {
        bpApp.setCenter(dashboardHandler.getRootNode());
    }

    // menu actions
    @FXML
    void closeAppByMenu(ActionEvent event) {
        closeApplication(event);
    }

    @FXML
    void showAboutDialog(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bad-Stat Programm - Badminton Statistik");
        alert.setHeaderText("Version: " + version);
        alert.setContentText("Kontakt: badminton@uwe-sauerbrei.de");
        alert.showAndWait();
    }

    @FXML
    void showDashboardView(ActionEvent event) {
        log.info("Show Dashboard View");
        bpApp.setCenter(dashboardHandler.getRootNode());
    }

    @FXML
    void showRankingView(ActionEvent event) {
        log.info("Show Ranking View");
    }

    @FXML
    void showStatisticsView(ActionEvent event) {
        log.info("Show Statistics View");
        bpApp.setCenter(statisticsController.getUI());
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
        stage.setTitle("Badminton Statistics");
        stage.getIcons().add(new Image(ICON_PATH));

        Scene scene = new Scene(this.root);
        scene.getStylesheets().add(STYLESHEET_PATH);
        stage.setScene(scene);
        return stage;
    }
}
