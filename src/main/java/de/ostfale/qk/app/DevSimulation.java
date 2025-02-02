package de.ostfale.qk.app;

import de.ostfale.qk.db.api.*;
import de.ostfale.qk.db.internal.*;
import de.ostfale.qk.parser.ConfiguredWebClient;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchDTO;
import de.ostfale.qk.parser.match.internal.model.MixedMatchDTO;
import de.ostfale.qk.parser.match.internal.model.SingleMatchDTO;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import de.ostfale.qk.parser.tournament.internal.TournamentParserService;
import de.ostfale.qk.parser.tournament.internal.model.TournamentInfoDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Singleton
public class DevSimulation {

    private static final Logger log = Logger.getLogger(DevSimulation.class);

    private static final String PLAYER_RANKING_FILE = "src/main/resources/simulation/Ranking_2025_Sim.xlsx";
    private static final String TOURNAMENT_BONN_FILE = "simulation/TournamentDMBonn.html";

    protected final WebClient webClient = ConfiguredWebClient.getWebClient();


    @Inject
    RankingParser rankingParser;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    TournamentRepository tournamentRepository;

    @Inject
    TournamentParserService tournamentParserService;

    @Inject
    PlayerInfoRepository playerInfoRepository;

    @Inject
    SingleMatchRepository singleMatchRepository;

    @Inject
    DoubleMatchRepository doubleMatchRepository;

    @Inject
    MixedMatchRepository mixedMatchRepository;

    public void loadSimulationData() {
        log.info("Load simulation data");

        Path path = Paths.get(PLAYER_RANKING_FILE);
        List<RankingPlayer> rankingPlayerList = rankingParser.parseRankingFile(path.toFile());
        log.infof("Loaded %d players", rankingPlayerList.size());
        savePlayer(rankingPlayerList);

        // load and save tournament data
        HtmlPage page = loadHtmlPage(TOURNAMENT_BONN_FILE);
        HtmlElement htmlElement = page.getActiveElement();
        TournamentYearDTO tournamentYearDTO = tournamentParserService.parseTournamentYear("2024", htmlElement);
        Tournament tournament = saveTournament(tournamentYearDTO);

        // save matches for all disciplines
        saveMatches(tournament.getTournamentID(), tournamentYearDTO.tournaments().getFirst().getTournamentDisciplines());
    }

    @Transactional
    public void savePlayer(List<RankingPlayer> rankingPlayerList) {
        log.infof("Number of players to save: %d", rankingPlayerList.size());
        for (RankingPlayer rankingPlayer : rankingPlayerList) {
            Player player = new Player(rankingPlayer);
            PlayerInfo playerInfo = createPlayerInfo(rankingPlayer);
            playerInfo.setPlayer(player);

            playerRepository.persist(player);
            playerInfoRepository.persist(playerInfo);
            log.infof("Saved player: %s", rankingPlayer.getName());
        }
    }

    @Transactional
    public Tournament saveTournament(TournamentYearDTO tournamentYearDTO) {
        TournamentInfoDTO tInfo = tournamentYearDTO.tournaments().getFirst().getTournamentInfo();
        String tournamentId = tInfo.tournamentId();
        String tournamentName = tInfo.tournamentName();
        String tournamentOrganisation = tInfo.tournamentOrganisation();
        String tournamentLocation = tInfo.tournamentLocation();
        String tournamentDate = tInfo.tournamentDate();
        Integer year = Integer.parseInt(tournamentYearDTO.year());
        log.infof("Save tournament: %s", tInfo.tournamentName());
        Tournament tournament = new Tournament(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate, year);
        tournamentRepository.persist(tournament);
        return tournament;
    }

    public void saveMatches(String tournamentId, List<DisciplineDTO> disciplines) {
        log.info("Save match disciplines:");

        disciplines.forEach(disciplineDTO -> {
            switch (disciplineDTO.getDiscipline()) {
                case SINGLE -> saveSingleMatches(tournamentId, disciplineDTO);
                case DOUBLE -> saveDoubleMatches(tournamentId, disciplineDTO);
                case MIXED -> saveMixedMatches(tournamentId, disciplineDTO);
            }
        });
    }

    private Collection<SingleMatch> mapToSingleMatches(Tournament tournament, DisciplineDTO disciplineDTO) {
        log.info("Save single matches");
        Collection<SingleMatch> matches = new ArrayList<>();
        disciplineDTO.getMatches().forEach(singleMatch -> {
            SingleMatchDTO dto = (SingleMatchDTO) singleMatch;
            matches.add(new SingleMatch(tournament, dto));
        });
        return matches;
    }

    public void saveDoubleMatches(String tournamentId, DisciplineDTO dto) {
        log.info("Save double matches");
        dto.getMatches().forEach(match -> {
            DoubleMatchDTO doubleMatchDTO = (DoubleMatchDTO) match;
            saveDoubleMatch(tournamentId, doubleMatchDTO);
        });
    }

    public void saveMixedMatches(String tournamentId, DisciplineDTO dto) {
        log.info("Save mixed matches");
        dto.getMatches().forEach(match -> {
            MixedMatchDTO mixedMatchDTO = (MixedMatchDTO) match;
            saveMixedMatch(tournamentId, mixedMatchDTO);
        });
    }

    public void saveSingleMatches(String tournamentId, DisciplineDTO dto) {
        log.info("Save single matches");
        dto.getMatches().forEach(match -> {
            SingleMatchDTO singleMatchDTO = (SingleMatchDTO) match;
            saveSingleMatch(tournamentId, singleMatchDTO);
        });
    }

    @Transactional()
    public void saveSingleMatch(String tournamentId, SingleMatchDTO singleMatchDTO) {
        Tournament tournament = tournamentRepository.findByTournamentId(tournamentId);
        var match = new SingleMatch(tournament, singleMatchDTO);
        singleMatchRepository.persist(match);
    }

    @Transactional()
    public void saveDoubleMatch(String tournamentId, DoubleMatchDTO doubleMatchDTO) {
        Tournament tournament = tournamentRepository.findByTournamentId(tournamentId);
        var match = new DoubleMatch(tournament, doubleMatchDTO);
        doubleMatchRepository.persist(match);
    }

    @Transactional()
    public void saveMixedMatch(String tournamentId, MixedMatchDTO mixedMatchDTO) {
        Tournament tournament = tournamentRepository.findByTournamentId(tournamentId);
        var match = new MixedMatch(tournament, mixedMatchDTO);
        mixedMatchRepository.persist(match);
    }

    private PlayerInfo createPlayerInfo(RankingPlayer rankingPlayer) {
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setClubName(rankingPlayer.getClubName());
        playerInfo.setDistrictName(rankingPlayer.getDistrictName());
        playerInfo.setStateName(rankingPlayer.getStateName());
        playerInfo.setStateGroup(rankingPlayer.getStateGroup());
        playerInfo.setAgeClassGeneral(rankingPlayer.getAgeClassGeneral());
        playerInfo.setAgeClassDetail(rankingPlayer.getAgeClassDetail());
        return playerInfo;
    }

    private HtmlPage loadHtmlPage(String fileName) {
        try {
            var htmlString = readFile(fileName);
            return webClient.loadHtmlCodeIntoCurrentWindow(htmlString);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFile(String fileName) throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource(fileName), "file not found! " + fileName);
        var file = new File(resource.toURI());
        return Files.readString(file.toPath());
    }
}
