package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import de.ostfale.qk.ui.statistics.favplayer.FavPlayerChangeListener;
import de.ostfale.qk.ui.statistics.favplayer.FavPlayerStringConverter;
import de.ostfale.qk.ui.statistics.model.SearchableYears;
import de.ostfale.qk.web.api.WebService;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.CustomTextField;
import org.jboss.logging.Logger;

import java.util.List;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoController extends BaseController<PlayerInfoDTO> {

    private static final Logger log = Logger.getLogger(PlayerInfoController.class);

    private static final String PATH_SEPARATOR = "/";

    @Inject
    PlayerInfoHandler playerInfoHandler;

    @Inject
    PlayerServiceProvider playerServiceProvider;

    @Inject
    WebService webService;

    @FXML
    GridPane gpPlayerInfo;

    // filter
    @FXML
    private ComboBox<PlayerInfoDTO> cbPlayer;

    @FXML
    private CheckComboBox<SearchableYears> ccbYear;

    @FXML
    private CustomTextField ctfSearchPlayer;

    @FXML
    private Button btnPlayerView;

    @FXML
    private Button btnFavorite;

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
        ccbYear.getItems().addAll(FXCollections.observableArrayList(SearchableYears.values()));
        initFavPlayerComboboxModel();
        initSearchPlayerTextField();
    }

    @FXML
    void onEnterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            var enteredUrl = txtTourURL.getText();
            String playerTourID = extractLastPathSegment(enteredUrl);
            log.infof("Enter key pressed with text: %s and extracted player tournament ID: %s", enteredUrl, playerTourID);
            PlayerInfoDTO currentSelectedPlayer = cbPlayer.getSelectionModel().getSelectedItem();
           /* playerServiceProvider.updatePlayersTournamentId(currentSelectedPlayer, playerTourID);
            lblIdTurnier.setText(playerTourID);

            var result = webService.getNumberOfTournamentsForYearAndPlayer(2025, "bd337124-44d1-42c1-9c30-8bed91781a9b");
            log.infof("Tournaments Found: %d", result);*/
        }
    }

    // init button to toggle player as favorites
    @FXML
    void togglePlayerFavoriteStatus(ActionEvent event) {
        log.debug("Toggle player favorite status");
        String playerName = ctfSearchPlayer.getText();
      /*  Player foundPlayer = players.stream()
                .filter(player -> player.getName().equalsIgnoreCase(playerName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player not found"));

        foundPlayer.setFavorite(true);*/
     /*   playerServiceProvider.updatePlayerAsFavorite(foundPlayer);
        dataModel.setItemList(playerServiceProvider.findFavoritePlayers());*/
    }

    @FXML
    void viewPlayerInfo(ActionEvent event) {
        log.debugf("View player info");
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
        // var player = playerInfoHandler.calculatePlayersAgeClassRanking(playerInfoDTO);
        lblName.setText(player.getPlayerName());
        lblPlayerId.setText(player.getPlayerId());
        lblBirthYear.setText(player.getBirthYear());
        lblAgeClass.setText(player.getAgeClass());
        lblClub.setText(player.getClubName());
        lblDistrict.setText(player.getDistrictName());
        lblIdTurnier.setText(player.getPlayerTournamentId());

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

        txtTourURL.setText("");                                          // reset url since it is not saved in DB
    }


    // init text field to search player from all players list
    private void initSearchPlayerTextField() {
        ctfSearchPlayer.setPromptText("Spieler suchen");

      /*  if (players.isEmpty()) {
            players = playerInfoHandler.findAllPlayers();
            log.infof("Initialized  %d players", players.size());
        }

        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<PlayerInfoDTO>> suggestionProvider =
                request -> players.stream()
                        .filter(suggestion -> suggestion.getName().toLowerCase().contains(request.getUserText().toLowerCase()))
                        .toList();*/

        // TextFields.bindAutoCompletion(ctfSearchPlayer, suggestionProvider);
    }


    private String extractLastPathSegment(String url) {
        if (url == null || !url.contains(PATH_SEPARATOR)) {
            return ""; // Return empty string if URL is null or doesn't contain slashes
        }
        int lastSeparatorIndex = url.lastIndexOf(PATH_SEPARATOR);
        return url.substring(lastSeparatorIndex + 1);
    }
}
