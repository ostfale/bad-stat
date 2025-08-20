package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.ui.playerstats.matches.PlayerStatisticsHandler;
import io.quarkiverse.fx.views.FxViewData;
import io.quarkiverse.fx.views.FxViewRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

import java.util.List;

@ApplicationScoped
public class PlayerStatsController {

    @Inject
    FxViewRepository fxViewRepository;

    @Inject
    PlayerStatisticsHandler playerTourStatsHandler;

    @Inject
    PlayerInfoController playerInfoController;

    private Accordion accordionContainer;

    private TitledPane playerInfoPane;
    private TitledPane playerStatisticsPane;
    private TitledPane playerGraphPane;

    private void initialize() {
        Log.info("Initialize StatisticsController");

        var playerPanes = configurePlayerPanes();
        accordionContainer = configureAccordion(playerPanes);
    }

    public Accordion getUI() {
        if (accordionContainer == null) {
            initialize();
        }
        return accordionContainer;
    }

    private List<TitledPane> configurePlayerPanes() {
        FxViewData playerGrid = fxViewRepository.getViewData("player-stat-info");
        playerInfoPane = new TitledPane("Spieler - Auswahl und Infos", playerGrid.getRootNode());

        playerStatisticsPane = new TitledPane("Spieler - Turnier Statistik", playerTourStatsHandler.getUI());
        playerGraphPane = new TitledPane("Spieler - Grafische Übersicht", new Label("Spieler - Grafiken und Diagramme"));
        return List.of(playerInfoPane, playerStatisticsPane, playerGraphPane);
    }

    private Accordion configureAccordion(List<TitledPane> playerPanes) {
        Log.debug("Configure StatisticsController Accordion");
        var accordion = new Accordion();
        accordion.getPanes().addAll(playerPanes);
        accordion.setExpandedPane(this.playerInfoPane);
        return accordion;
    }
}
