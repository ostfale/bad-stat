package de.ostfale.qk.ui.playerstats.info.filter;

import de.ostfale.qk.data.player.model.FavPlayerData;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

@ApplicationScoped
public class FavPlayerChangeListener implements ChangeListener<FavPlayerData> {

    @Inject
    Event<FavPlayerData> event;

    @Override
    public void changed(ObservableValue<? extends FavPlayerData> observableValue, FavPlayerData oldPlayer, FavPlayerData newPlayer) {
        if (newPlayer != null) {
            Log.infof("Player %s is now a selected", newPlayer.playerName());
            event.fire(newPlayer);      // PlayerInfoHandler
        } else if (oldPlayer != null) {
            Log.debugf("Player %s is no longer selected", oldPlayer.playerName());
        }
    }
}
