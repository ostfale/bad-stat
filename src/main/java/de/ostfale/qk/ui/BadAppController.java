package de.ostfale.qk.ui;

import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@FxView("app-view")
@Dependent
public class BadAppController {

    @FXML
    Parent root;

    @FXML
    public void initialize() {
        Stage stage = new Stage();
        Scene scene = new Scene(this.root);
        stage.setScene(scene);
        stage.show();
    }

   /* @FXML
    private void handleClickMeAction() {
        // Roll a d20
        int value = RANDOM.nextInt(0, 21);
        this.rollResultLabel.setText(String.valueOf(value));
    }*/
}
