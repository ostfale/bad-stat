package de.ostfale.qk.app;

import de.ostfale.qk.db.api.MatchRepository;
import de.ostfale.qk.db.api.PlayerInfoRepository;
import de.ostfale.qk.db.api.PlayerRepository;
import de.ostfale.qk.db.api.TournamentRepository;
import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.db.internal.match.Match;
import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.internal.player.PlayerInfo;
import de.ostfale.qk.db.service.TournamentServiceProvider;
import de.ostfale.qk.parser.ConfiguredWebClient;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineRawModel;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchRawModel;
import de.ostfale.qk.parser.match.internal.model.MixedMatchRawModel;
import de.ostfale.qk.parser.match.internal.model.SingleMatchRawModel;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import de.ostfale.qk.parser.tournament.internal.TournamentParserService;
import de.ostfale.qk.parser.tournament.internal.model.TournamentRawModel;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearRawModel;
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
import java.util.List;
import java.util.Objects;

@Singleton
public class DevSimulation {

    private static final Logger log = Logger.getLogger(DevSimulation.class);

    private static final String PLAYER_RANKING_FILE = "src/main/resources/simulation/Ranking_2025_Full_Sim.xlsx";
    private static final String TOURNAMENTS_FILE = "simulation/Tournaments.html";

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
    MatchRepository matchRepository;

    @Inject
    TournamentServiceProvider tournamentServiceProvider;

    public void loadSimulationData() {
        log.info("Load simulation data");

        Path path = Paths.get(PLAYER_RANKING_FILE);
        List<RankingPlayer> rankingPlayerList = rankingParser.parseRankingFile(path.toFile());
        log.infof("Loaded %d players", rankingPlayerList.size());
        savePlayer(rankingPlayerList);

        // load and save tournament data
        HtmlPage page = loadHtmlPage(TOURNAMENTS_FILE);
        HtmlElement htmlElement = page.getActiveElement();
        TournamentYearRawModel tournamentYearRawModel = tournamentParserService.parseTournamentYear("2024", htmlElement);
        saveTournament(tournamentYearRawModel);

        // find all tournaments for 2024 and Louis Sauerbrei
        var result = tournamentServiceProvider.getAllTournamentsForYearAndPlayer(2024, "Louis Sauerbrei");
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
        }
    }

    @Transactional
    public void saveTournament(TournamentYearRawModel tournamentYearRawModel) {
        tournamentYearRawModel.tournaments().forEach(tournamentRawModel -> {
            log.infof("Save tournament %s", tournamentRawModel.getTournamentName());
            Tournament tournament = getTournamentInfos(tournamentYearRawModel, tournamentRawModel);
            tournamentRepository.persist(tournament);

            // save matches for all disciplines
            saveMatches(tournament.getTournamentID(), tournamentRawModel.getTournamentDisciplines());
        });
    }

    private static Tournament getTournamentInfos(TournamentYearRawModel tournamentYearRawModel, TournamentRawModel tInfo) {
        String tournamentId = tInfo.getTournamentId();
        String tournamentName = tInfo.getTournamentName();
        String tournamentOrganisation = tInfo.getTournamentOrganisation();
        String tournamentLocation = tInfo.getTournamentLocation();
        String tournamentDate = tInfo.getTournamentDate();
        Integer year = Integer.parseInt(tournamentYearRawModel.year());
        return new Tournament(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate, year);
    }

    public void saveMatches(String tournamentId, List<DisciplineRawModel> disciplines) {
        log.info("Save match disciplines:");

        // loop all disciplines and retrieve single matches
        disciplines.forEach(disciplineDTO -> {
            switch (disciplineDTO.getDiscipline()) {
                case SINGLE -> saveSingleMatches(tournamentId, disciplineDTO);
                case DOUBLE -> saveDoubleMatches(tournamentId, disciplineDTO);
                case MIXED -> saveMixedMatches(tournamentId, disciplineDTO);
            }
        });
    }

    @Transactional()
    public void saveSingleMatches(String tournamentId, DisciplineRawModel dto) {
        log.infof("Save single matches #: %d", dto.getMatches().size());
        Tournament tournament = tournamentRepository.findByTournamentId(tournamentId);
        List<Match> matchList = new ArrayList<>();
        dto.getMatches().forEach(match -> {
            SingleMatchRawModel singleMatchRawModel = (SingleMatchRawModel) match;
            Match actualMatch = new Match(tournament, singleMatchRawModel, dto.getDisciplineName());
            matchList.add(actualMatch);
        });
        matchRepository.persist(matchList);
    }

    @Transactional()
    public void saveDoubleMatches(String tournamentId, DisciplineRawModel dto) {
        log.infof("Save double matches #: %d", dto.getMatches().size());
        Tournament tournament = tournamentRepository.findByTournamentId(tournamentId);
        List<Match> matchList = new ArrayList<>();
        dto.getMatches().forEach(match -> {
            DoubleMatchRawModel doubleMatchRawModel = (DoubleMatchRawModel) match;
            Match actualMatch = new Match(tournament, doubleMatchRawModel, dto.getDisciplineName());
            matchList.add(actualMatch);
        });
        matchRepository.persist(matchList);
    }

    @Transactional()
    public void saveMixedMatches(String tournamentId, DisciplineRawModel dto) {
        log.infof("Save mixed matches #: %d", dto.getMatches().size());
        Tournament tournament = tournamentRepository.findByTournamentId(tournamentId);
        List<Match> matchList = new ArrayList<>();
        dto.getMatches().forEach(match -> {
            MixedMatchRawModel mixedMatchRawModel = (MixedMatchRawModel) match;
            Match actualMatch = new Match(tournament, mixedMatchRawModel, dto.getDisciplineName());
            matchList.add(actualMatch);
        });
        matchRepository.persist(matchList);
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
