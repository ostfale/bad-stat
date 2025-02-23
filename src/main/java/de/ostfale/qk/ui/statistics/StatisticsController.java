package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.ui.ApplicationController;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jboss.logging.Logger;

import java.util.List;

@Dependent
public class StatisticsController {

    private static final Logger log = Logger.getLogger(ApplicationController.class);

    @Inject
    PlayerStatisticsHandler playerTourStatsHandler;

    private VBox vBoxContainer;

    private Accordion accordionContainer;

    private TitledPane playerInfoPane;
    private TitledPane playerStatisticsPane;
    private TitledPane playerGraphPane;

    @PostConstruct
    private void initialize() {
        log.info("Initialize StatisticsController");

        var playerPanes = configurePlayerPanes();
        accordionContainer = configureAccordion(playerPanes);
        vBoxContainer = configureVBox(accordionContainer);
    }

    public VBox getUI() {
        return vBoxContainer;
    }

    private List<TitledPane> configurePlayerPanes() {
        playerInfoPane = new TitledPane("Spieler - Auswahl und Infos", new Label("Spieler - Auswahl und Informationen"));
        playerStatisticsPane = new TitledPane("Spieler - Turnier Statistik", playerTourStatsHandler.getUI());
        playerGraphPane = new TitledPane("Spieler - Grafische Übersicht", new Label("Spieler - Grafiken und Diagramme"));
        return List.of(playerInfoPane, playerStatisticsPane, playerGraphPane);
    }

    private Accordion configureAccordion(List<TitledPane> playerPanes) {
        log.debug("Configure StatisticsController Accordion");
        var accordion = new Accordion();
        accordion.setPrefHeight(800);
        accordion.getPanes().addAll(playerPanes);
        accordion.setExpandedPane(this.playerInfoPane);
        return accordion;
    }

    private VBox configureVBox(Accordion accordion) {
        log.debug("Configure StatisticsController VBox");
        var vbox = new VBox(accordion);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        return vbox;
    }
}
