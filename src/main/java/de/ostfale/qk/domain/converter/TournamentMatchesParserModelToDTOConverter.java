package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.discipline.DisciplineMatchesDTO;
import de.ostfale.qk.domain.match.MatchesDTO;
import de.ostfale.qk.domain.tournament.TournamentMatchesDTO;
import de.ostfale.qk.parser.discipline.model.DisciplineParserModel;
import de.ostfale.qk.parser.match.internal.model.Match;
import de.ostfale.qk.parser.set.SetRawModel;
import de.ostfale.qk.parser.tournament.model.TournamentParserModel;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class TournamentMatchesParserModelToDTOConverter implements Converter<TournamentParserModel, TournamentMatchesDTO> {

    private static final Logger log = Logger.getLogger(TournamentMatchesParserModelToDTOConverter.class);

    @Override
    public TournamentMatchesDTO convertTo(TournamentParserModel parserData) {
        log.debug("Convert Tournament matches parser model to DTO");
        var dto = new TournamentMatchesDTO();
        dto.setTournamentName(parserData.getTournamentName());
        dto.setTournamentLocation(parserData.getTournamentLocation());
        dto.setTournamentDate(parserData.getTournamentDate());
        var discoplineList = parserData.getTournamentDisciplines().stream().map(this::convertTo).toList();
        dto.setDisciplineMatches(discoplineList);
        return dto;
    }

    private DisciplineMatchesDTO convertTo(DisciplineParserModel parserData) {
        log.debug("Convert Discipline matches parser model to DTO");
        var dto = new DisciplineMatchesDTO();
        dto.setDisciplineName(parserData.getDisciplineName());
        dto.setAgeClass(parserData.getAgeClass().name());
        var matchList = parserData.getMatches().stream().map(this::convertTo).toList();
        dto.setMatchesDTOs(matchList);
        return dto;
    }

    private MatchesDTO convertTo(Match match) {
        log.debug("Convert Match parser model to DTO");
        var dto = new MatchesDTO();
        dto.setPtOneName(match.getFirstPlayerOrTeamName());
        dto.setPtTwoName(match.getSecondPlayerOrTeamName());
        dto.setRoundName(match.getRoundName());
        dto.setSetResults(match.getPlayersSets().stream().map(SetRawModel::toString).toList());
        return dto;
    }
}
