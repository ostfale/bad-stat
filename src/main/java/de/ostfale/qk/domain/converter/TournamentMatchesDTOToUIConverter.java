package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.tournament.TournamentMatchesDTO;
import de.ostfale.qk.ui.playerstats.matches.PlayerMatchStatisticsUIModel;
import org.jboss.logging.Logger;

public class TournamentMatchesDTOToUIConverter implements Converter<PlayerMatchStatisticsUIModel, TournamentMatchesDTO> {

    private static final Logger log = Logger.getLogger(PlayerMatchStatisticsUIModel.class);

    @Override
    public TournamentMatchesDTO convertTo(PlayerMatchStatisticsUIModel source) {
        log.debug("Convert UIModel to DTO");
        return null;
    }
}
