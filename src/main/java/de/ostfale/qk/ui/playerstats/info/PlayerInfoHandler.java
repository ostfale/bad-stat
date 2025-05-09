package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.tournament.model.TournamentParserModel;
import de.ostfale.qk.parser.tournament.model.TournamentYearParserModel;
import de.ostfale.qk.ui.app.BaseHandler;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.scene.Node;
import org.jboss.logging.Logger;

@Singleton
public class PlayerInfoHandler implements BaseHandler {

    private static final Logger log = Logger.getLogger(PlayerInfoHandler.class);

    private static final String PLAYER_INFO_FXML = "player-stat-info";

    @Inject
    FxViewRepository fxViewRepository;

    @Override
    public Node getRootNode() {
        log.debug("PlayerInfoHandler :: Get player info view");
        return fxViewRepository.getViewData(PLAYER_INFO_FXML).getRootNode();
    }

    // TODO - find better solution
    private static Tournament getTournamentInfos(TournamentYearParserModel tournamentYearParserModel, TournamentParserModel tInfo) {
        String tournamentId = tInfo.getTournamentId();
        String tournamentName = tInfo.getTournamentName();
        String tournamentOrganisation = tInfo.getTournamentOrganisation();
        String tournamentLocation = tInfo.getTournamentLocation();
        String tournamentDate = tInfo.getTournamentDate();
        Integer year = Integer.parseInt(tournamentYearParserModel.year());
        return new Tournament(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate, year);
    }
}
