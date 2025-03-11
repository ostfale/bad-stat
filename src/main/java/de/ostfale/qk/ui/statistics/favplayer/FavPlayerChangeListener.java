package de.ostfale.qk.ui.statistics.favplayer;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.jboss.logging.Logger;

public class FavPlayerChangeListener implements ChangeListener<Player> {

    private static final Logger log = Logger.getLogger(FavPlayerChangeListener.class);

    private final PlayerInfoController controller;

    public FavPlayerChangeListener(PlayerInfoController controller) {
        this.controller = controller;
    }

    @Override
    public void changed(ObservableValue<? extends Player> observableValue, Player oldPlayer, Player newPlayer) {
        if (newPlayer != null) {
            log.infof("Player %s is now a selected", newPlayer.getName());
            controller.updatePlayerInfo(newPlayer);
        } else {
            log.infof("Player %s is no longer selected", oldPlayer.getName());
        }
    }
}
