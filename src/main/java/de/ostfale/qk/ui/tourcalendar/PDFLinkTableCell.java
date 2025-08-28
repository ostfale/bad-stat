package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.app.HostServicesProvider;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkus.logging.Log;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class PDFLinkTableCell extends TableCell<TourCalUIModel, String> {

    private final HostServicesProvider hostServicesProvider;

    public PDFLinkTableCell(HostServicesProvider hostServicesProvider) {
        Log.trace("Initialize PDFLinkTableCell");
        this.hostServicesProvider = hostServicesProvider;
    }

    @Override
    protected void updateItem(String item, boolean isEmpty) {
        super.updateItem(item, isEmpty);
        setGraphic(null);
        setText(null);

        if (item != null && !item.isEmpty()) {
            Button btnPdfLink = createButton();
            if (hostServicesProvider != null) {
                var hostServices = hostServicesProvider.getHostServices();
                btnPdfLink.setOnAction(actionEvent -> hostServices.showDocument(item));
            }
            setGraphic(btnPdfLink);
        }
        this.setItem(item);
    }

    private Button createButton(FontIcon icon) {
        var btn = new Button();
        btn.setId("pdfBtn");
        btn.setPrefWidth(30);
        btn.setPrefHeight(30);
        btn.setGraphic(icon);
        btn.setText("");
        btn.setTooltip(new Tooltip("Ausschreibung"));
        return btn;
    }

    private Button createButton() {
        var ic = createFontIcon();
        return createButton(ic);
    }

    private FontIcon createFontIcon() {
        var ic = new FontIcon("far-file-pdf");
        ic.setIconSize(16);
        ic.setIconColor(Color.BLUE);
        return ic;
    }
}
