package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.discipline.DisciplineMatchesDTO;
import de.ostfale.qk.domain.match.MatchesDTO;
import de.ostfale.qk.domain.tournament.TournamentMatchesDTO;
import de.ostfale.qk.ui.playerstats.matches.PlayerMatchStatisticsUIModel;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class TournamentMatchesDTOToUIConverter implements Converter<TournamentMatchesDTO, PlayerMatchStatisticsUIModel> {

    private static final Logger log = Logger.getLogger(PlayerMatchStatisticsUIModel.class);

    private static final String EMPTY_STRING = "";

    @Override
    public PlayerMatchStatisticsUIModel convertTo(TournamentMatchesDTO source) {
        log.trace("TournamentMatchesDTOToUIConverter :: Convert JSON TournamentMatchesDTO to UI model");
        var uiModel = new PlayerMatchStatisticsUIModel();
        uiModel.setTournamentName(source.getTournamentName());
        uiModel.setTournamentLocation(source.getTournamentLocation());
        uiModel.setTournamentDate(source.getTournamentDate());
        source.getDisciplineMatches().forEach(disciplineMatchesDTO -> convertTo(disciplineMatchesDTO, uiModel));
        return uiModel;
    }

    private void convertTo(DisciplineMatchesDTO source, PlayerMatchStatisticsUIModel parentModel) {
        log.trace("TournamentMatchesDTOToUIConverter :: Convert JSON DisciplineMatchesDTO to UI model");
        parentModel.getMatchDetails().addAll(source.getMatchesDTOs()
                .stream()
                .map(matchesDTO -> convertTo(matchesDTO, source.getDisciplineName()))
                .toList());
    }

    private PlayerMatchStatisticsUIModel convertTo(MatchesDTO source, String disciplineName) {
        log.trace("TournamentMatchesDTOToUIConverter :: Convert JSON MatchesDTO to UI model");
        var uiModel = new PlayerMatchStatisticsUIModel();
        uiModel.setTournamentName(EMPTY_STRING);
        uiModel.setTournamentLocation(EMPTY_STRING);
        uiModel.setTournamentDate(EMPTY_STRING);
        uiModel.setDisciplineName(disciplineName);
        uiModel.setRoundName(source.getRoundName());
        uiModel.setPtOneName(source.getPtOneName());
        uiModel.setPtTwoName(source.getPtTwoName());
        uiModel.setMatchResult(String.join(" - ", source.getSetResults()));
        return uiModel;
    }
}
