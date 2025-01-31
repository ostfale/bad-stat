package de.ostfale.qk.app;

import de.ostfale.qk.db.api.PlayerInfoRepository;
import de.ostfale.qk.db.api.PlayerRepository;
import de.ostfale.qk.db.api.TournamentRepository;
import de.ostfale.qk.db.internal.Player;
import de.ostfale.qk.db.internal.PlayerInfo;
import de.ostfale.qk.db.internal.Tournament;
import de.ostfale.qk.parser.ConfiguredWebClient;
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

    public void loadSimulationData() {
        log.info("Load simulation data");

        Path path = Paths.get(PLAYER_RANKING_FILE);
        List<RankingPlayer> rankingPlayerList = rankingParser.parseRankingFile(path.toFile());
        log.infof("Loaded %d players", rankingPlayerList.size());
        savePlayer(rankingPlayerList);

        // load tournament data
        HtmlPage page = loadHtmlPage(TOURNAMENT_BONN_FILE);
        HtmlElement htmlElement = page.getActiveElement();
        TournamentYearDTO tournamentYearDTO = tournamentParserService.parseTournamentYear("2024", htmlElement);
        saveTournament(tournamentYearDTO);
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
    public void saveTournament(TournamentYearDTO tournamentYearDTO) {
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
