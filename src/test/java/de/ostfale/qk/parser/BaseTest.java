package de.ostfale.qk.parser;

import de.ostfale.qk.domain.player.*;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoMasterDTO;
import de.ostfale.qk.web.common.ConfiguredWebClient;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public abstract class BaseTest {

    // player specific test constants
    protected static final String PLAYER_ID = "04-098314";
    protected static final String PLAYER_TOURNAMENT_ID = "bd337124-44d1-42c1-9c30-8bed91781a9b";
    protected static final String PLAYER_FIRST_NAME = "Louis";
    protected static final String PLAYER_SECOND_NAME = "Sauerbrei";
    protected static final GenderType PLAYER_GENDER = GenderType.MALE;
    protected static final int PLAYER_YEAR_OF_BIRTH = 2010;
    // player general information
    protected static final String PLAYER_AGE_CLASS_GENERAL = "U17";
    protected static final String PLAYER_AGE_CLASS_DETAIL = "U17-1";
    protected static final String PLAYER_CLUB_NAME = "Horner-TV";
    protected static final String PLAYER_DISTRICT_NAME = "Hamburg";
    protected static final String PLAYER_STATE_NAME = "Hamburg";
    protected static final Group PLAYER_GROUP_NAME = Group.NORTH;
    // single ranking data
    protected static final int PLAYER_SINGLE_RANKING_POINTS = 28456;
    protected static final int PLAYER_SINGLE_RANKING_POSITION = 153;
    protected static final int PLAYER_SINGLE_RANKING_AGE_POSITION = 53;
    protected static final int PLAYER_SINGLE_RANKING_TOURNAMENTS = 17;
    // double ranking data
    protected static final int PLAYER_DOUBLE_RANKING_POINTS = 40345;
    protected static final int PLAYER_DOUBLE_RANKING_POSITION = 135;
    protected static final int PLAYER_DOUBLE_RANKING_AGE_POSITION = 35;
    protected static final int PLAYER_DOUBLE_RANKING_TOURNAMENTS = 14;
    // mixed ranking data
    protected static final int PLAYER_MIXED_RANKING_POINTS = 34567;
    protected static final int PLAYER_MIXED_RANKING_POSITION = 145;
    protected static final int PLAYER_MIXED_RANKING_AGE_POSITION = 45;
    protected static final int PLAYER_MIXED_RANKING_TOURNAMENTS = 15;
    protected static final String PLAYER_NAME = "Victoria Braun";
    protected final WebClient webClient = ConfiguredWebClient.getWebClient();

    protected HtmlPage loadHtmlPage(String fileName) {
        try {
            var htmlString = readFileToString(fileName);
            return webClient.loadHtmlCodeIntoCurrentWindow(htmlString);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected File readFile(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource(fileName), "file not found! " + fileName);
        return new File(resource.toURI());
    }


    // prepare player test object
    protected Player createPlayer() {
        Player player = new Player(PLAYER_ID, PLAYER_FIRST_NAME, PLAYER_SECOND_NAME, PLAYER_GENDER, PLAYER_YEAR_OF_BIRTH);
        player.setPlayerInfo(new PlayerInfo(PLAYER_AGE_CLASS_GENERAL, PLAYER_AGE_CLASS_DETAIL, PLAYER_CLUB_NAME, PLAYER_DISTRICT_NAME, PLAYER_STATE_NAME, PLAYER_GROUP_NAME));
        player.setSingleRankingInformation(createSingleRankingInformation());
        player.setDoubleRankingInformation(createDoubleRankingInformation());
        player.setMixedRankingInformation(createMixedRankingInformation());
        return player;
    }

    private RankingInformation createSingleRankingInformation() {
        return new RankingInformation(PLAYER_SINGLE_RANKING_POINTS, PLAYER_SINGLE_RANKING_POSITION, PLAYER_SINGLE_RANKING_AGE_POSITION, PLAYER_SINGLE_RANKING_TOURNAMENTS);
    }

    private RankingInformation createDoubleRankingInformation() {
        return new RankingInformation(PLAYER_DOUBLE_RANKING_POINTS, PLAYER_DOUBLE_RANKING_POSITION, PLAYER_DOUBLE_RANKING_AGE_POSITION, PLAYER_DOUBLE_RANKING_TOURNAMENTS);
    }

    private RankingInformation createMixedRankingInformation() {
        return new RankingInformation(PLAYER_MIXED_RANKING_POINTS, PLAYER_MIXED_RANKING_POSITION, PLAYER_MIXED_RANKING_AGE_POSITION, PLAYER_MIXED_RANKING_TOURNAMENTS);
    }

    protected PlayerInfoDTO createPlayerInfoDTO() {
        var player = createPlayer();
        return new PlayerInfoDTO(player);
    }

    private PlayerInfoMasterDTO createPlayerInfoMasterDTO() {
        var player = createPlayer();
        return new PlayerInfoMasterDTO(player);
    }

    private String readFileToString(String fileName) throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource(fileName), "file not found! " + fileName);
        var file = new File(resource.toURI());
        return Files.readString(file.toPath());
    }
}
