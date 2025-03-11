package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.ui.ApplicationController;
import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoController;
import io.quarkiverse.fx.views.FxViewData;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import org.jboss.logging.Logger;

import java.util.List;

@Singleton
public class StatisticsController {

    private static final Logger log = Logger.getLogger(ApplicationController.class);

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
        log.info("Initialize StatisticsController");

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
        log.debug("Configure StatisticsController Accordion");
        var accordion = new Accordion();
        accordion.getPanes().addAll(playerPanes);
        accordion.setExpandedPane(this.playerInfoPane);
        return accordion;
    }
}
