package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.data.player.model.FavPlayerData;
import de.ostfale.qk.domain.tournament.RecentYears;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import de.ostfale.qk.ui.playerstats.info.favplayer.FavPlayerService;
import de.ostfale.qk.ui.playerstats.info.filter.FavPlayerChangeListener;
import de.ostfale.qk.ui.playerstats.info.filter.FavPlayerStringConverter;
import de.ostfale.qk.ui.playerstats.info.filter.PlayerTextSearchComponent;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoMasterDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTournamentsService;
import de.ostfale.qk.ui.playerstats.matches.PlayerStatisticsController;
import de.ostfale.qk.ui.playerstats.matches.PlayerStatisticsHandler;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.Year;
import java.util.List;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoController extends BaseController<PlayerInfoDTO> {

    private static final String INITIAL_TOURNAMENT_RESULT = "0/0";

    @Inject
    FavPlayerService favPlayerService;

    @Inject
    PlayerInfoService playerInfoService;

    @Inject
    PlayerTournamentsService playerTournamentsService;

    @Inject
    PlayerStatisticsHandler playerStatisticsHandler;

    @Inject
    FavPlayerChangeListener favPlayerChangeListener;

    // TODO use event bus to handle messages
    @Inject
    PlayerStatisticsController playerTourStatsController;

    @FXML
    GridPane gpPlayerInfo;

    DataModel<FavPlayerData> dataModelFavPlayer = new DataModel<>();

    // filter
    @FXML
    private ComboBox<FavPlayerData> cbPlayer;

    @FXML
    private TextField tfSearchPlayer;

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
    private Label lblPlayerName;

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
    private Label lblYearMinusThree;

    @FXML
    private Label lblYearMinusTwo;

    @FXML
    private Label lblYear;

    @FXML
    private Label lblYearMinusOne;

    @FXML
    private Button btnDownloadMinusOne;

    @FXML
    private Button btnDownloadMinusThree;

    @FXML
    private Button btnDownloadMinusTwo;

    @FXML
    private Button btnDownloadThisYear;

    @FXML
    private Button btnFavUpdate;

    @FXML
    public void initialize() {
        Log.info("Initialize PlayerInfoStatisticsController");
        initFavPlayerComboboxModel();
        new PlayerTextSearchComponent(playerInfoService, tfSearchPlayer).initialize();
        initYearLabel();
        initBinding();
    }

    @FXML
    void downloadThisYearsTournaments(ActionEvent event) {
        int year = Year.now().getValue();
        loadTournamentDataForYear(year);
    }

    @FXML
    void downloadThisYearMinusOneTournaments(ActionEvent event) {
        int year = Year.now().minusYears(1).getValue();
        loadTournamentDataForYear(year);
    }

    @FXML
    void downloadThisYearMinusTwoTournaments(ActionEvent event) {
        int year = Year.now().minusYears(2).getValue();
        loadTournamentDataForYear(year);
    }

    @FXML
    void downloadThisYearMinusThreeTournaments(ActionEvent event) {
        int year = Year.now().minusYears(3).getValue();
        loadTournamentDataForYear(year);
    }

    @FXML
    void addToFavorites(ActionEvent event) {
        Log.debug("Add player to favorites");
        favPlayerService.addFavPlayer(tfSearchPlayer.getText());
        updateFavorites();
        tfSearchPlayer.clear();
    }

    @FXML
    void removeFromFavorites(ActionEvent actionEvent) {
        Log.debug("Remove player from favorites");
        favPlayerService.removeFavoritePlayer(tfSearchPlayer.getText());
        updateFavorites();
    }

    @FXML
    void viewPlayerInfo(ActionEvent event) {
        Log.debugf("View player info");
        resetFavoriteCombobox();
        updatePlayerInfoUI();
    }

    @FXML
    void updateFavoritesWebData(ActionEvent event) {
        Log.debug("Update favorites web data");
    }

    public void clearPlayerSearchField() {
        tfSearchPlayer.clear();
    }

    private void resetFavoriteCombobox() {
        cbPlayer.getSelectionModel().clearSelection();
        cbPlayer.setValue(null);
    }

    private void initBinding() {
        btnDownloadThisYear.disableProperty().bind(dataModelFavPlayer.currentObjectProperty().isNull());
        btnDownloadMinusOne.disableProperty().bind(dataModelFavPlayer.currentObjectProperty().isNull());
        btnDownloadMinusTwo.disableProperty().bind(dataModelFavPlayer.currentObjectProperty().isNull());
        btnDownloadMinusThree.disableProperty().bind(dataModelFavPlayer.currentObjectProperty().isNull());
    }

    private void loadTournamentDataForYear(int year) {
        FavPlayerData currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
        var playerInfo = playerInfoService.getPlayerInfoDTO(currentSelectedPlayer.playerId());
        playerTournamentsService.loadAndSavePlayerTournamentsForYear(year, playerInfo);
        var uiModel = playerTournamentsService.readPlayerTournamentsForLastFourYears(playerInfo);
        playerStatisticsHandler.updatePlayerMatchStatistics(uiModel);
    }

    // TODO check selected player from dataModel
    private void initFavPlayerComboboxModel() {
        Log.debug("Initialize DataModel for player combobox");
        List<FavPlayerData> favPlayers = favPlayerService.getFavoritePlayerListData().getFavoritePlayers().stream().toList();

        dataModelFavPlayer.setStringConverter(new FavPlayerStringConverter());
        dataModelFavPlayer.setChangeListener(favPlayerChangeListener);
        dataModelFavPlayer.updateModel(favPlayers, cbPlayer);

        // reset combobox to show the prompt if nothing is selected
        cbPlayer.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(FavPlayerData favPlayerData, boolean empty) {
                super.updateItem(favPlayerData, empty);
                if (favPlayerData == null || empty) {
                    setText(cbPlayer.getPromptText());
                } else {
                    setText(favPlayerData.playerName());
                }
            }
        });
    }

    public void updateFavorites() {
        Log.debug("PlayerInfoStatisticsController :: Update favorites");
        var favoritePlayers = favPlayerService.getFavoritePlayerListData().getFavoritePlayers().stream().toList();
        dataModelFavPlayer.updateModel(favoritePlayers, cbPlayer);
    }

    private void updatePlayerInfoUI() {
        String searchedPlayerName = tfSearchPlayer.getText();
        PlayerInfoDTO playerInfo = playerInfoService.getPlayerInfosForPlayerName(searchedPlayerName);
        Log.debugf("Update player info for : %s", playerInfo.getPlayerInfoMasterDataDTO().getPlayerName());
        resetPlayerInfo();
        updateDynamicPlayerInfo(playerInfo);
        setPlayersMasterData(playerInfo);
        updateDynamicPlayerInfo(playerInfo);
    }

    public void updateDynamicPlayerInfo(PlayerInfoDTO playerInfoDTO) {
        lblSTours.setText(playerInfoDTO.getSingleDiscStat().tournaments().toString());
        lblDTours.setText(playerInfoDTO.getDoubleDiscStat().tournaments().toString());
        lblMTours.setText(playerInfoDTO.getMixedDiscStat().tournaments().toString());

        lblSPoints.setText(playerInfoDTO.getSingleDiscStat().points().toString());
        lblDPoints.setText(playerInfoDTO.getDoubleDiscStat().points().toString());
        lblMPoints.setText(playerInfoDTO.getMixedDiscStat().points().toString());

        lblSRank.setText(playerInfoDTO.getSingleDiscStat().fullRank().toString());
        lblDRank.setText(playerInfoDTO.getDoubleDiscStat().fullRank().toString());
        lblMRank.setText(playerInfoDTO.getMixedDiscStat().fullRank().toString());

        lblSAKRank.setText(playerInfoDTO.getSingleDiscStat().ageClassRank().toString());
        lblDAKRank.setText(playerInfoDTO.getDoubleDiscStat().ageClassRank().toString());
        lblMAKRank.setText(playerInfoDTO.getMixedDiscStat().ageClassRank().toString());
    }

    public void updateTreeMatchStatistics(PlayerInfoDTO playerInfoDTO) {
        Log.debugf("UI :: Update player matches statistics for player %s ", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName());
        var uiModel = playerTournamentsService.readPlayerTournamentsForLastFourYears(playerInfoDTO);
        playerTourStatsController.updateTreeTable(uiModel);
    }

    public void updatePlayerMatchesStatsForYear(FavPlayerData favPlayerData) {
        Log.debugf("UI :: Update player matches statistics for player %s ", favPlayerData.playerName());
        lblYear.setText(favPlayerData.getPlayerTournamentsStatForYear(RecentYears.CURRENT_YEAR.getValue()));
        lblYearMinusOne.setText(favPlayerData.getPlayerTournamentsStatForYear(RecentYears.YEAR_MINUS_1.getValue()));
        lblYearMinusTwo.setText(favPlayerData.getPlayerTournamentsStatForYear(RecentYears.YEAR_MINUS_2.getValue()));
        lblYearMinusThree.setText(favPlayerData.getPlayerTournamentsStatForYear(RecentYears.YEAR_MINUS_3.getValue()));
    }

    public void updatePlayerMasterData(PlayerInfoMasterDTO playerInfoMasterDTO) {
        Log.debugf("UI :: Update player master data for player %s ", playerInfoMasterDTO.getPlayerName());
        lblName.setText(playerInfoMasterDTO.getPlayerName());
        lblPlayerId.setText(playerInfoMasterDTO.getPlayerId());
        lblBirthYear.setText(String.valueOf(playerInfoMasterDTO.getBirthYear()));
        lblAgeClass.setText(playerInfoMasterDTO.getAgeClass());
        lblClub.setText(playerInfoMasterDTO.getClubName() == null ? "" : playerInfoMasterDTO.getClubName());
        lblDistrict.setText(playerInfoMasterDTO.getDistrictName() == null ? "" : playerInfoMasterDTO.getDistrictName());
        lblState.setText(playerInfoMasterDTO.getStateName() == null ? "" : playerInfoMasterDTO.getStateName());
        lblGroup.setText(playerInfoMasterDTO.getStateGroup() == null ? "" : playerInfoMasterDTO.getStateGroup());
        lblPlayerName.setText(playerInfoMasterDTO.getPlayerName() == null ? "" : playerInfoMasterDTO.getPlayerName());
    }

    void setPlayersMasterData(PlayerInfoDTO playerInfoDTO) {
        var masterData = playerInfoDTO.getPlayerInfoMasterDataDTO();
        lblName.setText(masterData.getPlayerName());
        lblGender.setText(masterData.getGender());
        lblPlayerId.setText(masterData.getPlayerId());
        lblBirthYear.setText(String.valueOf(masterData.getBirthYear()));
        lblAgeClass.setText(masterData.getAgeClass());
        lblClub.setText(masterData.getClubName() == null ? "" : masterData.getClubName());
        lblDistrict.setText(masterData.getDistrictName() == null ? "" : masterData.getDistrictName());
        lblState.setText(masterData.getStateName() == null ? "" : masterData.getStateName());
        lblGroup.setText(masterData.getStateGroup() == null ? "" : masterData.getStateGroup());
        lblPlayerName.setText(masterData.getPlayerName() == null ? "" : masterData.getPlayerName());
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
        Label[] labels = {lblName, lblPlayerId, lblBirthYear, lblAgeClass, lblClub, lblDistrict, lblPlayerName, lblGroup, lblState, lblGender, lblSTours, lblDTours, lblMTours, lblSPoints, lblDPoints, lblMPoints, lblSRank, lblDRank, lblMRank, lblSAKRank, lblDAKRank, lblMAKRank, lblYear, lblYearMinusOne, lblYearMinusTwo, lblYearMinusThree};

        // Clear all labels using a helper method
        for (Label label : labels) {
            clearLabel(label);
        }

        // Handle remaining individual text components not part of same type
        lblYear.setText(INITIAL_TOURNAMENT_RESULT);
        lblYearMinusOne.setText(INITIAL_TOURNAMENT_RESULT);
        lblYearMinusTwo.setText(INITIAL_TOURNAMENT_RESULT);
        lblYearMinusThree.setText(INITIAL_TOURNAMENT_RESULT);
    }

    private void clearLabel(Label label) {
        label.setText("");
    }
}
