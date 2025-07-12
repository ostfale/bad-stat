package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.discipline.Discipline;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.domain.match.Match;
import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.ui.playerstats.matches.PlayerMatchStatisticsUIModel;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TournamentModelToUIConverter implements Converter<Tournament, PlayerMatchStatisticsUIModel> {

    private static final String EMPTY_STRING = "";
    private static final String GROUP_FORMAT = "%s (%s)";

    @Override
    public PlayerMatchStatisticsUIModel convertTo(Tournament tournament) {
        Log.trace("TournamentModelToUIConverter :: Convert domain tournament model to UI model");
        var uiModel = new PlayerMatchStatisticsUIModel();
        uiModel.setTournamentName(tournament.getTournamentInfo().tournamentName());
        uiModel.setTournamentLocation(tournament.getTournamentInfo().tournamentLocation());
        uiModel.setTournamentDate(tournament.getTournamentInfo().tournamentDate());
        tournament.getDisciplines().forEach(discipline -> convertTo(discipline, uiModel));
        return uiModel;
    }


    private void convertTo(Discipline discipline, PlayerMatchStatisticsUIModel uiModel) {
        Log.trace("TournamentModelToUIConverter :: Converting discipline model to UI model");
        String baseDisciplineName = discipline.getDisciplineType().getDisplayString();

        if (discipline.hasEliminationMatches()) {
            processMatches(discipline.getEliminationMatches(), baseDisciplineName, uiModel);
        }

        if (discipline.hasGroupMatches()) {
            String disciplineName = formatDisciplineNameWithGroup(baseDisciplineName, discipline.getGroupName());
            processMatches(discipline.getGroupMatches(), disciplineName, uiModel);
        }
    }

    private String formatDisciplineNameWithGroup(String disciplineName, String groupName) {
        return groupName.isEmpty() ? disciplineName :
                String.format(GROUP_FORMAT, disciplineName, groupName);
    }

    private void processMatches(List<DisciplineMatch> matches, String disciplineName, PlayerMatchStatisticsUIModel uiModel) {
        Log.tracef("TournamentModelToUIConverter :: Processing %d matches for discipline: %s", matches.size(), disciplineName);
        convertMatches(uiModel, matches, disciplineName);
    }


    private void convertMatches(PlayerMatchStatisticsUIModel uiModel, List<DisciplineMatch> matchList, String disciplineName) {
        for (Match match : matchList) {
            if (match instanceof DisciplineMatch disciplineMatch) {
                uiModel.getMatchDetails().add(convertTo(disciplineMatch, disciplineName));
            } else {
                Log.warnf("TournamentModelToUIConverter :: Match is not a discipline match: %s", match.getClass().getName());
            }
        }
    }

    private PlayerMatchStatisticsUIModel convertTo(DisciplineMatch disciplineMatch, String disciplineName) {
        Log.trace("TournamentModelToUIConverter :: Convert domain discipline match model to UI model");
        var uiModel = new PlayerMatchStatisticsUIModel();
        uiModel.setTournamentName(EMPTY_STRING);
        uiModel.setTournamentLocation(EMPTY_STRING);
        uiModel.setTournamentDate(EMPTY_STRING);
        uiModel.setDisciplineName(disciplineName);
        uiModel.setRoundName(disciplineMatch.getRoundName());
        uiModel.setPtOneName(disciplineMatch.getFirstPlayerOrWithPartnerName());
        uiModel.setPtTwoName(disciplineMatch.getSecondPlayerOrWithPartnerName());
        uiModel.setMatchResult(String.join(" - ", disciplineMatch.getSetResults()));
        return uiModel;
    }
}
