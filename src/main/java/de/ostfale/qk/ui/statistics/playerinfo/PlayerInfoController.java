package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.internal.match.TournamentsStatistic;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import de.ostfale.qk.ui.app.RecentYears;
import de.ostfale.qk.ui.statistics.favplayer.FavPlayerChangeListener;
import de.ostfale.qk.ui.statistics.favplayer.FavPlayerStringConverter;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.jboss.logging.Logger;

import java.time.Year;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoController extends BaseController<PlayerInfoDTO> {

    private static final Logger log = Logger.getLogger(PlayerInfoController.class);

    private static final String PATH_SEPARATOR = "/";
    private static final String INITIAL_TOURNAMENT_RESULT = "0/0";

    @Inject
    PlayerInfoHandler playerInfoHandler;

    @FXML
    GridPane gpPlayerInfo;

    // filter
    @FXML
    private ComboBox<PlayerInfoDTO> cbPlayer;

    @FXML
    private CustomTextField ctfSearchPlayer;

    @FXML
    private Button btnPlayerView;

    @FXML
    private Button btnFavorite;


    @FXML
    private Label lblLastUpdate;

    // player general info
    @FXML
    private Label lblName;

    @FXML
    private Label lblPlayerId;

    @FXML
    private Label lblAgeClass;

    @FXML
    private Label lblBirthYear;

    @FXML
    private Label lblClub;

    @FXML
    private Label lblDistrict;

    @FXML
    private Label lblIdTurnier;

    @FXML
    private Label lblGroup;

    @FXML
    private Label lblState;

    @FXML
    private Label lblGender;

    // player ranking ,points and tournaments

    @FXML
    private Label lblDAKRank;

    @FXML
    private Label lblDPoints;

    @FXML
    private Label lblDRank;

    @FXML
    private Label lblDTours;

    @FXML
    private Label lblMAKRank;

    @FXML
    private Label lblMRank;

    @FXML
    private Label lblMTours;

    @FXML
    private Label lblMPoints;

    @FXML
    private Label lblSAKRank;

    @FXML
    private Label lblSPoints;

    @FXML
    private Label lblSRank;

    @FXML
    private Label lblSTours;

    // tournaments per year

    @FXML
    private Label lblDisplayCurrentYear;

    @FXML
    private Label lblDisplayCurrentYearMinusOne;

    @FXML
    private Label lblDisplayCurrentYearMinusThree;

    @FXML
    private Label lblDisplayCurrentYearMinusTwo;

    @FXML
    private TextField txtTourURL;

    @FXML
    private Label lblYearMinusThree;

    @FXML
    private Label lblYearMinusTwo;

    @FXML
    private Label lblYear;

    @FXML
    private Label lblYearMinusOne;

    @FXML
    public void initialize() {
        log.info("Initialize PlayerInfoStatisticsController");
        initFavPlayerComboboxModel();
        initSearchPlayerTextField();
        initYearLabel();
    }

    @FXML
    void onEnterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            var enteredUrl = txtTourURL.getText();
            String playerTourID = extractLastPathSegment(enteredUrl);
            log.infof("Enter key pressed with text: %s and extracted player tournament ID: %s", enteredUrl, playerTourID);
            PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
            if (currentSelectedPlayer == null) {
                log.warn("No player selected");
                return;
            }
            currentSelectedPlayer.setPlayerTournamentId(playerTourID);
            TournamentsStatistic tournamentsStatistic = playerInfoHandler.updateOrCreatePlayerTournamentsStatistics(currentSelectedPlayer);
            updateTournamentInfosForPlayerAndYear(tournamentsStatistic);
            lblIdTurnier.setText(playerTourID);
        }
    }

    @FXML
    void downloadThisYearsTournaments(ActionEvent event) {
        Integer year = Year.now().getValue();
        PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        playerInfoHandler.updateAndSavePlayerTournamentsStatistics(currentSelectedPlayer, year);
    }

    @FXML
    void downloadThisYearMinusOneTournaments(ActionEvent event) {
        Integer year = Year.now().minusYears(1).getValue();
        PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        playerInfoHandler.updateAndSavePlayerTournamentsStatistics(currentSelectedPlayer, year);
    }

    @FXML
    void downloadThisYearMinusTwoTournaments(ActionEvent event) {
        Integer year = Year.now().minusYears(2).getValue();
        PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        playerInfoHandler.updateAndSavePlayerTournamentsStatistics(currentSelectedPlayer, year);
    }

    @FXML
    void downloadThisYearMinusThreeTournaments(ActionEvent event) {
        Integer year = Year.now().minusYears(3).getValue();
        PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        playerInfoHandler.updateAndSavePlayerTournamentsStatistics(currentSelectedPlayer, year);
    }

    private void updateTournamentInfosForPlayerAndYear(TournamentsStatistic tournamentsStatistic) {
        Objects.requireNonNull(tournamentsStatistic, "Tournaments statistic must not be null");
        log.debugf("UI :: Update tournament infos for player %s ", tournamentsStatistic.getPlayerId());
        lblYear.setText(tournamentsStatistic.getTournamentsStatisticAsString().getFirst());
        lblYearMinusOne.setText(tournamentsStatistic.getTournamentsStatisticAsString().get(1));
        lblYearMinusTwo.setText(tournamentsStatistic.getTournamentsStatisticAsString().get(2));
        lblYearMinusThree.setText(tournamentsStatistic.getTournamentsStatisticAsString().get(3));
    }

    // init button to toggle player as favorites
    @FXML
    void togglePlayerFavoriteStatus(ActionEvent event) {
        log.debug("Toggle player favorite status");
        var selectedPlayer = getSelectedPlayerByName(ctfSearchPlayer.getText());
        if (selectedPlayer != null) {
            playerInfoHandler.toggleAndSavePlayerAsFavorite(selectedPlayer);
            dataModel.setItemList(playerInfoHandler.findAllFavoritePlayers());
            cbPlayer.getSelectionModel().select(selectedPlayer);
            ctfSearchPlayer.setText("");
        }
    }

    private PlayerInfoDTO getSelectedPlayerByName(String playerName) {
        List<PlayerInfoDTO> selectedPlayer = playerInfoHandler.findPlayerByName(playerName);
        if (selectedPlayer.isEmpty()) {
            log.warnf("No player found with name %s", playerName);
            return null;
        } else if (selectedPlayer.size() > 1) {
            // TODO handle multiple results
            log.warnf("More than one player found with name %s", playerName);
            return null;
        } else {
            return selectedPlayer.getFirst();
        }
    }

    @FXML
    void viewPlayerInfo(ActionEvent event) {
        log.debugf("View player info");
        PlayerInfoDTO player = getSelectedPlayerByName(ctfSearchPlayer.getText());
        if (player != null) {
            updatePlayerInfo(player);
        } else {
            log.warn("No player selected");
        }
    }

    private void initFavPlayerComboboxModel() {
        log.debug("Initialize DataModel for player combobox");
        List<PlayerInfoDTO> favPlayers = playerInfoHandler.findAllFavoritePlayers();
        dataModel = new DataModel<>();
        dataModel.setStringConverter(new FavPlayerStringConverter());
        dataModel.setChangeListener(new FavPlayerChangeListener(this));
        dataModel.updateModel(favPlayers, cbPlayer);
    }

    public void updatePlayerInfo(PlayerInfoDTO player) {
        log.debugf("Update player info: %s", player.getPlayerName());
        resetPlayerInfo();
        if (player.getPlayerTournamentId() != null && !player.getPlayerTournamentId().isEmpty()) {
            TournamentsStatistic tournamentsStatistic = playerInfoHandler.updateOrCreatePlayerTournamentsStatistics(player);
            updateTournamentInfosForPlayerAndYear(tournamentsStatistic);
        }

        lblLastUpdate.setText(player.getLastUpdate());
        lblName.setText(player.getPlayerName());
        lblPlayerId.setText(player.getPlayerId());
        lblBirthYear.setText(player.getBirthYear());
        lblAgeClass.setText(player.getAgeClass());
        lblClub.setText(player.getClubName());
        lblDistrict.setText(player.getDistrictName());
        lblIdTurnier.setText(player.getPlayerTournamentId());
        lblGender.setText(player.getGender());
        lblState.setText(player.getStateName());
        lblGroup.setText(player.getStateGroup());

        lblSTours.setText(player.getSingleDisciplineStatistics().tournaments().toString());
        lblDTours.setText(player.getDoubleDisciplineStatistics().tournaments().toString());
        lblMTours.setText(player.getMixedDisciplineStatistics().tournaments().toString());

        lblSPoints.setText(player.getSingleDisciplineStatistics().points().toString());
        lblDPoints.setText(player.getDoubleDisciplineStatistics().points().toString());
        lblMPoints.setText(player.getMixedDisciplineStatistics().points().toString());

        lblSRank.setText(player.getSingleDisciplineStatistics().fullRank().toString());
        lblDRank.setText(player.getDoubleDisciplineStatistics().fullRank().toString());
        lblMRank.setText(player.getMixedDisciplineStatistics().fullRank().toString());

        lblSAKRank.setText(playerInfoHandler.getSingleRankingForAgeClass(player).toString());
        lblDAKRank.setText(playerInfoHandler.getDoubleRankingForAgeClass(player).toString());
        lblMAKRank.setText(playerInfoHandler.getMixedRankingForAgeClass(player).toString());

        txtTourURL.setText(""); // reset url since it is not saved in DB
    }

    // init text field to search player from all players list
    private void initSearchPlayerTextField() {
        ctfSearchPlayer.setPromptText("Spieler suchen");

        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<PlayerInfoDTO>> suggestionProvider = request -> playerInfoHandler.getAllPlayer().stream()
                .filter(suggestion -> suggestion.getPlayerName().toLowerCase().contains(request.getUserText().toLowerCase())).toList();

        TextFields.bindAutoCompletion(ctfSearchPlayer, suggestionProvider);
    }

    private String extractLastPathSegment(String url) {
        if (url == null || !url.contains(PATH_SEPARATOR)) {
            return ""; // Return empty string if URL is null or doesn't contain slashes
        }
        int lastSeparatorIndex = url.lastIndexOf(PATH_SEPARATOR);
        return url.substring(lastSeparatorIndex + 1);
    }

    private void initYearLabel() {
        lblDisplayCurrentYear.setText(RecentYears.CURRENT_YEAR.toString());
        lblDisplayCurrentYearMinusOne.setText(RecentYears.YEAR_MINUS_1.toString());
        lblDisplayCurrentYearMinusTwo.setText(RecentYears.YEAR_MINUS_2.toString());
        lblDisplayCurrentYearMinusThree.setText(RecentYears.YEAR_MINUS_3.toString());
    }

    private void resetPlayerInfo() {
        // Group repetitive label components into one array for iteration
        Label[] labels = { lblName, lblPlayerId, lblBirthYear, lblAgeClass, lblClub, lblDistrict, lblIdTurnier, lblGroup, lblState, lblGender, lblSTours,
                lblDTours, lblMTours, lblSPoints, lblDPoints, lblMPoints, lblSRank, lblDRank, lblMRank, lblSAKRank, lblDAKRank, lblMAKRank, lblYear,
                lblYearMinusOne, lblYearMinusTwo, lblYearMinusThree };

        // Clear all labels using a helper method
        for (Label label : labels) {
            clearLabel(label);
        }

        // Handle remaining individual text components not part of same type
        lblYear.setText(INITIAL_TOURNAMENT_RESULT);
        lblYearMinusOne.setText(INITIAL_TOURNAMENT_RESULT);
        lblYearMinusTwo.setText(INITIAL_TOURNAMENT_RESULT);
        lblYearMinusThree.setText(INITIAL_TOURNAMENT_RESULT);
        txtTourURL.setText("");
    }

    // Extracted method to encapsulate the repetitive label reset logic
    private void clearLabel(Label label) {
        label.setText("");
    }
}
