package de.ostfale.qk.ui.statistics.favplayer;

import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoController;
import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoDTO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.jboss.logging.Logger;

public class FavPlayerChangeListener implements ChangeListener<PlayerInfoDTO> {

    private static final Logger log = Logger.getLogger(FavPlayerChangeListener.class);

    private final PlayerInfoController controller;

    public FavPlayerChangeListener(PlayerInfoController controller) {
        this.controller = controller;
    }

    @Override
    public void changed(ObservableValue<? extends PlayerInfoDTO> observableValue, PlayerInfoDTO oldPlayer, PlayerInfoDTO newPlayer) {
        if (newPlayer != null) {
            log.infof("Player %s is now a selected", newPlayer.getPlayerName());
            controller.updatePlayerInfo(newPlayer);
        } else {
            log.debugf("Player %s is no longer selected", oldPlayer.getPlayerName());
        }
    }
}
