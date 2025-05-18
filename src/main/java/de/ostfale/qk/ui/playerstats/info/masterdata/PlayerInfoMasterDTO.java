package de.ostfale.qk.ui.playerstats.info.masterdata;

import de.ostfale.qk.domain.player.Player;
import org.jboss.logging.Logger;

public class PlayerInfoMasterDTO {

    private static final Logger log = Logger.getLogger(PlayerInfoMasterDTO.class);

    private final String playerName;
    private final String gender;
    private final String playerId;
    private final String birthYear;
    private final String ageClass;
    private final String ageClassDetail;
    private final String clubName;
    private final String districtName;
    private final String stateName;
    private final String stateGroup;

    private String playerTournamentId;

    public PlayerInfoMasterDTO(Player player) {
        log.tracef("PlayerInfoDTO :: init from player %d", player.getPlayerId());
        this.playerName = player.getFullName();
        this.gender = player.getGender().toString();
        this.playerId = player.getPlayerId().playerId();
        this.birthYear = String.valueOf(player.getYearOfBirth());
        this.ageClass = player.getPlayerInfo().getAgeClassGeneral();
        this.ageClassDetail = player.getPlayerInfo().getAgeClassSpecific();
        this.clubName = player.getPlayerInfo().getClubName() == null ? "" : player.getPlayerInfo().getClubName();
        this.districtName = player.getPlayerInfo().getDistrictName() == null ? "" : player.getPlayerInfo().getDistrictName();
        this.stateName = player.getPlayerInfo().getStateName() == null ? "" : player.getPlayerInfo().getStateName();
        this.stateGroup = player.getPlayerInfo().getGroupName() == null ? "" : player.getPlayerInfo().getGroupName().getDisplayName();
    }

    @Override
    public String toString() {
        return playerName;
    }

    public String getPlayerTournamentId() {
        return playerTournamentId;
    }

    public void setPlayerTournamentId(String playerTournamentId) {
        this.playerTournamentId = playerTournamentId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getGender() {
        return gender;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getAgeClass() {
        return ageClass;
    }

    public String getAgeClassDetail() {
        return ageClassDetail;
    }

    public String getClubName() {
        return clubName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateGroup() {
        return stateGroup;
    }
}
