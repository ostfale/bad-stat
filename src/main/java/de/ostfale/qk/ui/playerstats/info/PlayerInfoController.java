package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.data.player.model.FavPlayerData;
import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.tournament.RecentYears;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import de.ostfale.qk.ui.playerstats.info.favplayer.FavPlayerService;
import de.ostfale.qk.ui.playerstats.info.filter.FavPlayerChangeListener;
import de.ostfale.qk.ui.playerstats.info.filter.FavPlayerStringConverter;
import de.ostfale.qk.ui.playerstats.info.filter.PlayerTextSearchComponent;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTournamentsService;
import de.ostfale.qk.ui.playerstats.matches.PlayerStatisticsController;
import de.ostfale.qk.ui.playerstats.matches.PlayerStatisticsHandler;
import io.quarkiverse.fx.RunOnFxThread;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.jboss.logging.Logger;

import java.time.Year;
import java.util.List;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoController extends BaseController<PlayerInfoDTO> {

    private static final Logger log = Logger.getLogger(PlayerInfoController.class);

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
    public void initialize() {
        log.info("Initialize PlayerInfoStatisticsController");
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
        log.debug("Add player to favorites");
        favPlayerService.addFavPlayer(tfSearchPlayer.getText());
        updateFavorites();
        tfSearchPlayer.clear();
    }

    @FXML
    void removeFromFavorites(ActionEvent actionEvent) {
        log.debug("Remove player from favorites");
        favPlayerService.removeFavPlayer(tfSearchPlayer.getText());
        updateFavorites();
    }

    @FXML
    void viewPlayerInfo(ActionEvent event) {
        log.debugf("View player info");
        cbPlayer.getSelectionModel().clearSelection();
        cbPlayer.setValue(null);
        String searchedPlayerName = tfSearchPlayer.getText();
        PlayerInfoDTO playerInfo = playerInfoService.getPlayerInfosForPlayerName(searchedPlayerName);
        if (playerInfo == null) return;
        PlayerId playerId = new PlayerId(playerInfo.getPlayerInfoMasterDataDTO().getPlayerId());
        updatePlayerInfoUI(playerId);
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
        log.debug("Initialize DataModel for player combobox");
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
        log.debug("PlayerInfoStatisticsController :: Update favorites");
        var favoritePlayers = favPlayerService.getFavoritePlayerListData().getFavoritePlayers().stream().toList();
        dataModelFavPlayer.updateModel(favoritePlayers, cbPlayer);
    }

    @RunOnFxThread
    public void updatePlayerInfoUI(PlayerId playerId) {
        var playerInfo = playerInfoService.getPlayerInfoDTO(playerId);
        updatePlayerInfoUI(playerInfo);

        if (isTournamentIdMissing(playerInfo)) {
            log.debugf("PlayerInfoStatisticsController :: Update player info for player %s with tournament ID", playerId.playerId());
            updatePlayerTournamentId(playerId);
        }
    }

    private boolean isTournamentIdMissing(PlayerInfoDTO playerInfo) {
        return playerInfo.getPlayerInfoMasterDataDTO().getPlayerTournamentId() == null;
    }

    private void updatePlayerTournamentId(PlayerId playerId) {
        playerInfoService.fetchPlayerTournamentIdAsync(playerId.playerId())
                .subscribe().with(
                        plMap -> handleTournamentUpdate(playerId, plMap),
                        error -> log.errorf("Error processing tournament ID: %s", error.getMessage())
                );
    }

    private void handleTournamentUpdate(PlayerId playerId, PlayerTourStatDTO statsByTournament) {
        // Extract tournament ID and tournament statistics
        var tournamentsId = statsByTournament.getPlayerTournamentId();
        if (tournamentsId != null) {
            PlayerInfoDTO updatedPlayerInfo = playerInfoService.getPlayerInfoDTO(playerId);

            // Update player's tournament data
            updatedPlayerInfo.getPlayerInfoMasterDataDTO().setPlayerTournamentId(tournamentsId.tournamentId());
            updatedPlayerInfo.setTournamentsStatisticDTO(statsByTournament);

            // Perform UI update and log the operation
            updatePlayerInfoUI(updatedPlayerInfo);
            log.debugf("PlayerInfoStatisticsController :: Updated player info for player %s with tournament ID %s"
                    , playerId.playerId(), tournamentsId.tournamentId());
        } else {
            log.warnf("PlayerInfoStatisticsController :: Tournament ID is missing for player %s", playerId.playerId());
        }
    }

    private void updatePlayerInfoUI(PlayerInfoDTO playerInfoDTO) {
        log.debugf("Update player info: %s", playerInfoDTO.toString());
        resetPlayerInfo();
        updateTournamentInfosForPlayerAndYear(playerInfoDTO.getTournamentsStatisticDTO());
        setPlayersMasterData(playerInfoDTO);
        updatePlayerMatchesStatics(playerInfoDTO);

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

    public void updatePlayerMatchesStatics(PlayerInfoDTO playerInfoDTO) {
        log.debugf("UI :: Update player matches statistics for player %s ", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName());
        var uiModel = playerTournamentsService.readPlayerTournamentsForLastFourYears(playerInfoDTO);
        playerTourStatsController.updateTreeTable(uiModel);
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
        lblIdTurnier.setText(masterData.getPlayerTournamentId() == null ? "" : masterData.getPlayerTournamentId());
    }


    private void updateTournamentInfosForPlayerAndYear(PlayerTourStatDTO playerTourStatDTO) {
        if (playerTourStatDTO != null) {
            log.debugf("UI :: Update tournament infos for player %s ", playerTourStatDTO.getPlayerId());
            lblYear.setText(playerTourStatDTO.getTournamentsStatisticAsString().getFirst());
            lblYearMinusOne.setText(playerTourStatDTO.getTournamentsStatisticAsString().get(1));
            lblYearMinusTwo.setText(playerTourStatDTO.getTournamentsStatisticAsString().get(2));
            lblYearMinusThree.setText(playerTourStatDTO.getTournamentsStatisticAsString().get(3));
        }
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
        Label[] labels = {lblName, lblPlayerId, lblBirthYear, lblAgeClass, lblClub, lblDistrict, lblIdTurnier, lblGroup, lblState, lblGender, lblSTours, lblDTours, lblMTours, lblSPoints, lblDPoints, lblMPoints, lblSRank, lblDRank, lblMRank, lblSAKRank, lblDAKRank, lblMAKRank, lblYear, lblYearMinusOne, lblYearMinusTwo, lblYearMinusThree};

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
