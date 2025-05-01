package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.domain.tournament.RecentYears;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import de.ostfale.qk.ui.statistics.favplayer.FavPlayerChangeListener;
import de.ostfale.qk.ui.statistics.favplayer.FavPlayerStringConverter;
import de.ostfale.qk.ui.statistics.playerinfo.filter.PlayerTextSearchComponent;
import de.ostfale.qk.ui.statistics.playerinfo.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.statistics.playerinfo.tournamentdata.TournamentsStatisticDTO;
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
import org.controlsfx.control.textfield.CustomTextField;
import org.jboss.logging.Logger;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoController extends BaseController<PlayerInfoDTO> {

    private static final Logger log = Logger.getLogger(PlayerInfoController.class);

    private static final String INITIAL_TOURNAMENT_RESULT = "0/0";

    @Inject
    PlayerInfoService playerInfoService;

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
    private Button btnFavRemove;

    @FXML
    private Button btnFavAdd;

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
        new PlayerTextSearchComponent(playerInfoService, ctfSearchPlayer).initialize();
        //  initSearchPlayerTextField();
        initYearLabel();
    }

    @FXML
    void onEnterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            var enteredUrl = txtTourURL.getText();
            String playerTourID = playerInfoService.extractPlayerTournamentIdFromUrl(enteredUrl);
            log.infof("Enter key pressed with text: %s and extracted player tournament ID: %s", enteredUrl, playerTourID);
            PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
            if (currentSelectedPlayer == null) {
                log.info("No player selected");
                return;
            }
            currentSelectedPlayer.setPlayerTournamentId(playerTourID);
            playerInfoService.updatePlayerTournamentId(currentSelectedPlayer);


            //    TournamentsStatistic tournamentsStatistic = playerInfoHandler.updateOrCreatePlayerTournamentsStatistics(currentSelectedPlayer);
            //    updateTournamentInfosForPlayerAndYear(tournamentsStatistic);
            lblIdTurnier.setText(playerTourID);
        }
    }

    @FXML
    void downloadThisYearsTournaments(ActionEvent event) {
        Integer year = Year.now().getValue();
        PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        //   playerInfoHandler.updateAndSavePlayerTournamentsStatistics(currentSelectedPlayer, year);
    }

    @FXML
    void downloadThisYearMinusOneTournaments(ActionEvent event) {
        Integer year = Year.now().minusYears(1).getValue();
        PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        //   playerInfoHandler.updateAndSavePlayerTournamentsStatistics(currentSelectedPlayer, year);
    }

    @FXML
    void downloadThisYearMinusTwoTournaments(ActionEvent event) {
        Integer year = Year.now().minusYears(2).getValue();
        PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        //   playerInfoHandler.updateAndSavePlayerTournamentsStatistics(currentSelectedPlayer, year);
    }

    @FXML
    void downloadThisYearMinusThreeTournaments(ActionEvent event) {
        Integer year = Year.now().minusYears(3).getValue();
        PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        //   playerInfoHandler.updateAndSavePlayerTournamentsStatistics(currentSelectedPlayer, year);
    }

    private void updateTournamentInfosForPlayerAndYear(TournamentsStatisticDTO tournamentsStatisticDTO) {
        if (tournamentsStatisticDTO != null) {
            log.debugf("UI :: Update tournament infos for player %s ", tournamentsStatisticDTO.getPlayerId());
            lblYear.setText(tournamentsStatisticDTO.getTournamentsStatisticAsString().getFirst());
            lblYearMinusOne.setText(tournamentsStatisticDTO.getTournamentsStatisticAsString().get(1));
            lblYearMinusTwo.setText(tournamentsStatisticDTO.getTournamentsStatisticAsString().get(2));
            lblYearMinusThree.setText(tournamentsStatisticDTO.getTournamentsStatisticAsString().get(3));
        }
    }

    // init button to toggle player as favorites
    @FXML
    void addToFavorites(ActionEvent event) {
        log.debug("Add player to favorites");
        var selectedPlayer = ctfSearchPlayer.getText();
        playerInfoService.addPlayerToFavoriteList(selectedPlayer);
        var playerInfo = playerInfoService.getPlayerInfosForPlayer(selectedPlayer);
        updateFavorites();
        ctfSearchPlayer.clear();
        cbPlayer.getSelectionModel().select(playerInfo);
    }

    @FXML
    void removeFromFavorites(ActionEvent actionEvent) {
        log.debug("Remove player from favorites");
        String selectedPlayerName = getSelectedFavoritePlayer();
        if (selectedPlayerName.isEmpty()) {
            log.warn("No player selected");
        }
        playerInfoService.removePlayerFromFavoriteList(selectedPlayerName);
        updateFavorites();
    }

    @FXML
    void viewPlayerInfo(ActionEvent event) {
        log.debugf("View player info");
        PlayerInfoDTO playerInfo = playerInfoService.getPlayerInfosForPlayer(ctfSearchPlayer.getText());
        if (playerInfo != null) {
            updatePlayerInfo(playerInfo);
        } else {
            log.warn("No player selected");
        }
    }

    private String getSelectedFavoritePlayer() {
        return Optional.ofNullable(cbPlayer.getSelectionModel().getSelectedItem())
                .map(item -> Optional.ofNullable(item.getPlayerName()).orElse(""))
                .orElse("");
    }


    private void initFavPlayerComboboxModel() {
        log.debug("Initialize DataModel for player combobox");
        List<PlayerInfoDTO> favPlayers = playerInfoService.getAllFavoritePlayers();

        dataModel = new DataModel<>();
        dataModel.setStringConverter(new FavPlayerStringConverter());
        dataModel.setChangeListener(new FavPlayerChangeListener(this));
        dataModel.updateModel(favPlayers, cbPlayer);
    }

    public void updateFavorites() {
        log.debug("PlayerInfoStatisticsController :: Update favorites");
        List<PlayerInfoDTO> favPlayers = playerInfoService.getAllFavoritePlayers();
        dataModel.updateModel(favPlayers, cbPlayer);
    }

    public void updatePlayerInfo(PlayerInfoDTO playerInfoDTO) {
        log.debugf("Update player info: %s", playerInfoDTO.getPlayerName());
        resetPlayerInfo();
        updateTournamentInfosForPlayerAndYear(playerInfoDTO.getTournamentsStatisticDTO());

        lblName.setText(playerInfoDTO.getPlayerName());
        lblPlayerId.setText(playerInfoDTO.getPlayerId());
        lblBirthYear.setText(String.valueOf(playerInfoDTO.getBirthYear()));
        lblAgeClass.setText(playerInfoDTO.getAgeClass());
        lblClub.setText(playerInfoDTO.getClubName() == null ? "" : playerInfoDTO.getClubName());
        lblDistrict.setText(playerInfoDTO.getDistrictName() == null ? "" : playerInfoDTO.getDistrictName());
        lblIdTurnier.setText(playerInfoDTO.getPlayerTournamentId() == null ? "" : playerInfoDTO.getPlayerTournamentId());
        lblGender.setText(playerInfoDTO.getGender());
        lblState.setText(playerInfoDTO.getStateName() == null ? "" : playerInfoDTO.getStateName());
        lblGroup.setText(playerInfoDTO.getStateGroup());

        lblSTours.setText(playerInfoDTO.getSingleDisciplineStatistics().tournaments().toString());
        lblDTours.setText(playerInfoDTO.getDoubleDisciplineStatistics().tournaments().toString());
        lblMTours.setText(playerInfoDTO.getMixedDisciplineStatistics().tournaments().toString());

        lblSPoints.setText(playerInfoDTO.getSingleDisciplineStatistics().points().toString());
        lblDPoints.setText(playerInfoDTO.getDoubleDisciplineStatistics().points().toString());
        lblMPoints.setText(playerInfoDTO.getMixedDisciplineStatistics().points().toString());

        lblSRank.setText(playerInfoDTO.getSingleDisciplineStatistics().fullRank().toString());
        lblDRank.setText(playerInfoDTO.getDoubleDisciplineStatistics().fullRank().toString());
        lblMRank.setText(playerInfoDTO.getMixedDisciplineStatistics().fullRank().toString());

        lblSAKRank.setText(playerInfoDTO.getSingleDisciplineStatistics().ageClassRank().toString());
        lblDAKRank.setText(playerInfoDTO.getDoubleDisciplineStatistics().ageClassRank().toString());
        lblMAKRank.setText(playerInfoDTO.getMixedDisciplineStatistics().ageClassRank().toString());

        txtTourURL.setText(""); // reset url since it is not saved in DB
    }

    private void initYearLabel() {
        lblDisplayCurrentYear.setText(RecentYears.CURRENT_YEAR.getDisplayValue());
        lblDisplayCurrentYearMinusOne.setText(RecentYears.YEAR_MINUS_1.getDisplayValue());
        lblDisplayCurrentYearMinusTwo.setText(RecentYears.YEAR_MINUS_2.getDisplayValue());
        lblDisplayCurrentYearMinusThree.setText(RecentYears.YEAR_MINUS_3.getDisplayValue());
    }


    // Extracted method to encapsulate the repetitive label reset logic
    private void resetPlayerInfo() {
        // Group repetitive label components into one array for iteration
        Label[] labels = {lblName, lblPlayerId, lblBirthYear, lblAgeClass, lblClub, lblDistrict, lblIdTurnier, lblGroup, lblState, lblGender, lblSTours,
                lblDTours, lblMTours, lblSPoints, lblDPoints, lblMPoints, lblSRank, lblDRank, lblMRank, lblSAKRank, lblDAKRank, lblMAKRank, lblYear,
                lblYearMinusOne, lblYearMinusTwo, lblYearMinusThree};

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

    private void clearLabel(Label label) {
        label.setText("");
    }
}
