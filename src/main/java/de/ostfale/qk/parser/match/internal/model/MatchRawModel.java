package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.player.PlayerRawModel;
import de.ostfale.qk.parser.set.SetRawModel;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class MatchRawModel implements Match {

    private static final Logger log = Logger.getLogger(MatchRawModel.class);

    public abstract Discipline getDiscipline();

    public abstract List<String> getPlayerNames();

    protected final List<SetRawModel> playersSets = new ArrayList<>();

    // match general infos
    private String roundName;
    private String roundDate;
    private String roundLocation;
    private String roundDuration;

    // no regular match
    protected Boolean isMatchWalkover = null;
    protected Boolean isMatchRetired = null;
    protected Boolean isTreeSystem = Boolean.TRUE;

    protected Boolean hasFirstPlayerWonProp = null;

    public boolean hasFirstPlayerWon() {
        if (hasFirstPlayerWonProp != null) {
            log.debugf("hasFirstPlayerWon property not null -> returned: {} ", hasFirstPlayerWonProp);
            return hasFirstPlayerWonProp;
        }

        long firstWins = playersSets.stream().filter(SetRawModel::firstIsBetterThanSecond).count();
        long secondWins = playersSets.size() - firstWins;

        if (firstWins > secondWins) {
            hasFirstPlayerWonProp = Boolean.TRUE;
        }
        else  {
            hasFirstPlayerWonProp = Boolean.FALSE;
        }

        var result = firstWins > secondWins;
        log.debugf("hasFirstPlayerWon property not set -> calculated: {} ", result);
        return result;
    }

    public void setMatchInfoDTO(MatchInfoRawModel matchInfoRawModel) {
        this.roundName = matchInfoRawModel.roundName();
        this.roundDate = matchInfoRawModel.roundDate();
        this.roundLocation = matchInfoRawModel.roundLocation();
        this.roundDuration = matchInfoRawModel.roundDuration();
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getRoundDate() {
        return roundDate;
    }

    public void setRoundDate(String roundDate) {
        this.roundDate = roundDate;
    }

    public String getRoundLocation() {
        return roundLocation;
    }

    public void setRoundLocation(String roundLocation) {
        this.roundLocation = roundLocation;
    }

    public String getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(String roundDuration) {
        this.roundDuration = roundDuration;
    }

    public List<SetRawModel> getPlayersSets() {
        return playersSets;
    }

    public void setHasFirstPlayerWonProp(Boolean hasFirstPlayerWonProp) {
        this.hasFirstPlayerWonProp = hasFirstPlayerWonProp;
    }

    public void setMatchRetired(Boolean matchRetired) {
        isMatchRetired = matchRetired;
    }

    public Boolean getMatchRetired() {
        return isMatchRetired;
    }

    protected String formatPlayersNames(PlayerRawModel firstPlayer, PlayerRawModel secondPlayer) {
        String firstPlayerName = getValidPlayerName(firstPlayer);
        String secondPlayerName = getValidPlayerName(secondPlayer);
        return String.format("%s / %s", firstPlayerName, secondPlayerName);
    }

    protected String getValidPlayerName(PlayerRawModel player) {
        return Optional.ofNullable(player.getName())
                .filter(name -> !name.isBlank())
                .orElse("");
    }
}
