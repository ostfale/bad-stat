package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.app.HostServicesProvider;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import jakarta.enterprise.context.ApplicationScoped;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

@ApplicationScoped
public class WebLinkTableCell extends TableCell<TourCalUIModel, String> {

    private final HostServicesProvider hostServicesProvider;

    final Image webIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hyperlink.png")));

    public WebLinkTableCell(HostServicesProvider hostServicesProvider) {
        this.hostServicesProvider = hostServicesProvider;
    }

    @Override
    protected void updateItem(String item, boolean isEmpty) {
        super.updateItem(item, isEmpty);
        setGraphic(null);
        setText(null);

        if (item != null && !item.isEmpty()) {
            Button btnWebLink = new Button();
            btnWebLink.setId("webBtn");
            btnWebLink.setGraphic(createImageView(webIcon));
            if (hostServicesProvider != null) {
            var hostServices = hostServicesProvider.getHostServices();
                btnWebLink.setOnAction(actionEvent -> hostServices.showDocument(item));
            }
            setGraphic(btnWebLink);
        }
        this.setItem(item);
    }

    private ImageView createImageView(Image webIcon) {
        var iv = new ImageView();
        iv.setFitWidth(16);
        iv.setFitHeight(16);
        iv.setPreserveRatio(true);
        iv.setStyle("-fx-background-color: transparent");
        iv.setImage(webIcon);
        return iv;
    }
}
