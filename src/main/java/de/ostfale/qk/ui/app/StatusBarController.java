package de.ostfale.qk.ui.app;

import io.quarkus.logging.Log;
import jakarta.inject.Singleton;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.controlsfx.control.StatusBar;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

@Singleton
public class StatusBarController extends StatusBar {
    
    private final Label lblInternet = new Label();
    private final Label lblDBVRankingSite = new Label();

    private final BooleanProperty internetStatus = new SimpleBooleanProperty();
    private final BooleanProperty rankingWebsiteStatus = new SimpleBooleanProperty();
    

    public StatusBarController() {
        initUI();
        addBinding();
    }

    public void setInternetStatus(boolean internetConnectionStatus) {
        this.internetStatus.set(internetConnectionStatus);
    }

    public void setDBVRankingWebsiteStatus(boolean rankingWebsiteStatus) {
        this.rankingWebsiteStatus.set(rankingWebsiteStatus);
    }
    
    private FontIcon greenConnected() {
        FontIcon fontIcon = new FontIcon(FontAwesomeSolid.LOCK);
        fontIcon.setIconColor(Color.GREEN);
        return fontIcon;
    }

    private FontIcon redDisconnected() {
        FontIcon fontIcon = new FontIcon(FontAwesomeSolid.LOCK_OPEN);
        fontIcon.setIconColor(Color.RED);
        return fontIcon;
    }

    private void addBinding() {
        Log.debug("Init status bar binding");
        lblInternet.graphicProperty().bind(new When(internetStatus).then(greenConnected()).otherwise(redDisconnected()));
        lblDBVRankingSite.graphicProperty().bind(new When(rankingWebsiteStatus).then(greenConnected()).otherwise(redDisconnected()));
    }

    private void initUI() {
        lblInternet.setText(" Internet");
        lblDBVRankingSite.setText(" DBV Ranking Site");
        getLeftItems().addAll(lblInternet, getSpaceLabel(),lblDBVRankingSite);
        setText(" ");
    }

    private Label getSpaceLabel() {
        Label lbl = new Label(" ");
        lbl.setPrefWidth(10);
        return lbl;
    }
}
