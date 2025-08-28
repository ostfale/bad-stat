package de.ostfale.qk.ui;

import de.ostfale.qk.ui.app.StatusBarController;
import de.ostfale.qk.ui.dashboard.DashboardHandler;
import de.ostfale.qk.ui.playerstats.info.PlayerStatsHandler;
import de.ostfale.qk.ui.tourcalendar.TourCalHandler;
import io.quarkiverse.fx.FxPostStartupEvent;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
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
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
@FxView("app-view")
public class ApplicationController {
    
    @ConfigProperty(name = "quarkus.application.version")
    String version;

    @FXML
    Parent root;

    @FXML
    private BorderPane bpApp;

    @FXML
    AnchorPane centerAnchorPane;

    @Inject
    DashboardHandler dashboardHandler;

    @Inject
    PlayerStatsHandler playerStatsHandler;

    @Inject
    TourCalHandler tourCalHandler;

    @Inject
    StatusBarController statusBarController;

    @FXML
    public void initialize() {
        Stage dashboardStage = createStageWithScene();
        Log.info("Dashboard stage successfully initialized and shown.");
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
        Log.info("Show Dashboard View");
        bpApp.setCenter(dashboardHandler.getRootNode());
    }

    @FXML
    void showRankingView(ActionEvent event) {
        Log.info("Show Ranking View");
    }

    @FXML
    void showStatisticsView(ActionEvent event) {
        Log.info("Show Statistics View");
        bpApp.setCenter(playerStatsHandler.getRootNode());
    }

    @FXML
    void showTournamentsView(ActionEvent event) {
        Log.info("Show Tournaments View");
        bpApp.setCenter(tourCalHandler.getRootNode());
        var result = tourCalHandler.loadFuturePlannedTournaments();
        tourCalHandler.update(result);
    }

    @FXML
    void showFavorites(ActionEvent event) {
        Log.info("Show Favorites View");
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
