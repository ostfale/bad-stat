package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import de.ostfale.qk.ui.statistics.favplayer.FavPlayerChangeListener;
import de.ostfale.qk.ui.statistics.favplayer.FavPlayerStringConverter;
import de.ostfale.qk.ui.statistics.model.SearchableYears;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoStatisticsController extends BaseController<Player> {

    private static final Logger log = Logger.getLogger(PlayerInfoStatisticsController.class);

    private List<Player> players = new ArrayList<>();

    @Inject
    PlayerServiceProvider playerServiceProvider;

    @FXML
    GridPane gpPlayerInfo;

    // filter
    @FXML
    private ComboBox<Player> cbPlayer;

    @FXML
    private CheckComboBox<SearchableYears> ccbYear;

    @FXML
    private CustomTextField ctfSearchPlayer;

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
        readPlayersFromDB();
        initFavPlayerComboboxModel();
        initSearchPlayerTextField();
    }

    private void initFavPlayerComboboxModel() {
        log.debug("Initialize DataModel for player combobox");
        dataModel = new DataModel<>();
        dataModel.setStringConverter(new FavPlayerStringConverter());
        dataModel.setChangeListener(new FavPlayerChangeListener(this));
        dataModel.updateModel(readFavoritePlayers(), cbPlayer);
    }

    public void updatePlayerInfo(Player player) {
        log.debugf("Update player info: %s", player.getName());
        lblName.setText(player.getName());
        lblPlayerId.setText(player.getPlayerId());
        lblBirthYear.setText(player.getYearOfBirth().toString());
        lblAgeClass.setText(player.getAgeClassGeneral());
        lblClub.setText(player.getClubName());
        lblDistrict.setText(player.getDistrictName());

        lblSTours.setText(player.getSingleTournaments().toString());
        lblDTours.setText(player.getDoubleTournaments().toString());
        lblMTours.setText(player.getMixedTournaments().toString());

        lblSPoints.setText(player.getSinglePoints().toString());
        lblDPoints.setText(player.getDoublePoints().toString());
        lblMPoints.setText(player.getMixedPoints().toString());

        lblSRank.setText(player.getSingleRanking().toString());
        lblDRank.setText(player.getDoubleRanking().toString());
        lblMRank.setText(player.getMixedRanking().toString());

        lblSAKRank.setText(player.getSingleAgeRanking().toString());
        lblMAKRank.setText(player.getMixedAgeRanking().toString());
        lblDAKRank.setText(player.getDoubleAgeRanking().toString());
    }

    // init text field to search player from all players list
    private void initSearchPlayerTextField() {
        ctfSearchPlayer.setPromptText("Spieler suchen");

        if (players.isEmpty()) {
            players = readPlayersFromDB();
            log.infof("Initialized  %d players", players.size());
        }

        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<Player>> suggestionProvider =
                request -> players.stream()
                        .filter(suggestion -> suggestion.getName().toLowerCase().contains(request.getUserText().toLowerCase()))
                        .toList();

        TextFields.bindAutoCompletion(ctfSearchPlayer, suggestionProvider);
    }

    // init button to toggle player as favorites
    @FXML
    void togglePlayerFavoriteStatus(ActionEvent event) {
        log.debug("Toggle player favorite status");
        String playerName = ctfSearchPlayer.getText();
        Player foundPlayer = players.stream()
                .filter(player -> player.getName().equalsIgnoreCase(playerName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player not found"));

        foundPlayer.setFavorite(true);
        playerServiceProvider.updatePlayerAsFavorite(foundPlayer);
        dataModel.setItemList(playerServiceProvider.findFavoritePlayers());
    }

    private List<Player> readFavoritePlayers() {
        var favPlayers = playerServiceProvider.findFavoritePlayers();
        log.debugf("PlayerInfoStatisticsController :: Read all favorite players  %d players", favPlayers.size());
        return favPlayers;
    }


    private List<Player> readPlayersFromDB() {
        List<Player> allPlayers = playerServiceProvider.getAllPlayers();
        log.debugf("PlayerInfoStatisticsController :: Read all players  %d players", allPlayers.size());
        return allPlayers;
    }
}
