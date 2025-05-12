package de.ostfale.qk.ui.playerstats.info.filter;

import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.jboss.logging.Logger;

@ApplicationScoped
public class FavPlayerChangeListener implements ChangeListener<PlayerInfoDTO> {

    private static final Logger log = Logger.getLogger(FavPlayerChangeListener.class);

    @Inject
    EventBus eventBus;

    @Override
    public void changed(ObservableValue<? extends PlayerInfoDTO> observableValue, PlayerInfoDTO oldPlayer, PlayerInfoDTO newPlayer) {
        if (newPlayer != null) {
            log.infof("Player %s is now a selected", newPlayer.getPlayerInfoMasterDataDTO().getPlayerName());
            eventBus.send("player-selected", newPlayer);
       //     playerInfoController.updatePlayerInfo(newPlayer);
        } else if (oldPlayer != null) {
            log.debugf("Player %s is no longer selected", oldPlayer.getPlayerInfoMasterDataDTO().getPlayerName());
      //     playerInfoController.updatePlayerInfo(oldPlayer);
        }
    }
}
