package de.ostfale.qk.db.internal.player;

import org.jboss.logging.Logger;

import de.ostfale.qk.parser.ranking.internal.GenderType;
import de.ostfale.qk.parser.ranking.internal.Group;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Player {

    private static final Logger log = Logger.getLogger(Player.class);

    @Id
    @GeneratedValue
    private Long id;

    private String playerId;
    private String firstName;
    private String lastName;
    private String fullName;
    private Integer yearOfBirth;
    private Boolean favorite = false;
    private String playerTournamentId = "";

    // general info
    private String clubName;
    private String districtName;
    private String stateName;
    private String ageClassGeneral;
    private String ageClassDetail;

    @Enumerated(EnumType.STRING)
    private Group stateGroup;

    // points info
    private Integer singlePoints = 0;
    private Integer singleRanking = 0;
    private Integer singleAgeRanking = 0;
    private Integer singleTournaments = 0;
    private Integer doublePoints = 0;
    private Integer doubleRanking = 0;
    private Integer doubleAgeRanking = 0;
    private Integer doubleTournaments = 0;
    private Integer mixedPoints = 0;
    private Integer mixedRanking = 0;
    private Integer mixedAgeRanking = 0;
    private Integer mixedTournaments = 0;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    public void updatePlayer(Player rankingPlayer) {
        this.singlePoints = rankingPlayer.getSinglePoints();
        this.singleRanking = rankingPlayer.getSingleRanking();
        this.singleAgeRanking = rankingPlayer.getSingleAgeRanking();
        this.singleTournaments = rankingPlayer.getSingleTournaments();
        this.doublePoints = rankingPlayer.getDoublePoints();
        this.doubleRanking = rankingPlayer.getDoubleRanking();
        this.doubleAgeRanking = rankingPlayer.getDoubleAgeRanking();
        this.doubleTournaments = rankingPlayer.getDoubleTournaments();
        this.mixedPoints = rankingPlayer.getMixedPoints();
        this.mixedRanking = rankingPlayer.getMixedRanking();
        this.mixedAgeRanking = rankingPlayer.getMixedAgeRanking();
        this.mixedTournaments = rankingPlayer.getMixedTournaments();
        this.clubName = rankingPlayer.getClubName();
        this.districtName = rankingPlayer.getDistrictName();
        this.stateName = rankingPlayer.getStateName();
        this.stateGroup = rankingPlayer.getStateGroup();
        this.ageClassGeneral = rankingPlayer.getAgeClassGeneral();
        this.ageClassDetail = rankingPlayer.getAgeClassDetail();
        this.favorite = rankingPlayer.getFavorite();
    }

    public Player(RankingPlayer rankingPlayer) {
        this.playerId = rankingPlayer.getPlayerId();
        this.firstName = rankingPlayer.getFirstName();
        this.lastName = rankingPlayer.getLastName();
        this.fullName = rankingPlayer.getName();
        this.yearOfBirth = rankingPlayer.getYearOfBirth();
        this.gender = rankingPlayer.getGenderType();
        this.singlePoints = rankingPlayer.getSinglePoints();
        this.singleRanking = rankingPlayer.getSingleRanking();
        this.singleAgeRanking = rankingPlayer.getSingleAgeRanking();
        this.singleTournaments = rankingPlayer.getSingleTournaments();
        this.doublePoints = rankingPlayer.getDoublePoints();
        this.doubleRanking = rankingPlayer.getDoubleRanking();
        this.doubleAgeRanking = rankingPlayer.getDoubleAgeRanking();
        this.doubleTournaments = rankingPlayer.getDoubleTournaments();
        this.mixedPoints = rankingPlayer.getMixedPoints();
        this.mixedRanking = rankingPlayer.getMixedRanking();
        this.mixedAgeRanking = rankingPlayer.getMixedAgeRanking();
        this.mixedTournaments = rankingPlayer.getMixedTournaments();
        this.clubName = rankingPlayer.getClubName();
        this.districtName = rankingPlayer.getDistrictName();
        this.stateName = rankingPlayer.getStateName();
        this.stateGroup = rankingPlayer.getStateGroup();
        this.ageClassGeneral = rankingPlayer.getAgeClassGeneral();
        this.ageClassDetail = rankingPlayer.getAgeClassDetail();
        this.favorite = rankingPlayer.getFavorite();
    }

    public Player() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerId == null) ? 0 : playerId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if ((playerId == null && other.playerId != null) || (!playerId.equals(other.playerId))) {
            log.debug("diff player-id");
            return false;
        }

        if (favorite == null) {
            if (other.favorite != null)
                return false;
        } else if (!favorite.equals(other.favorite))
            return false;
        if (clubName == null) {
            if (other.clubName != null)
                return false;
        } else if (!clubName.equals(other.clubName))
            return false;
        if (districtName == null) {
            if (other.districtName != null)
                return false;
        } else if (!districtName.equals(other.districtName))
            return false;
        if (stateName == null) {
            if (other.stateName != null)
                return false;
        } else if (!stateName.equals(other.stateName))
            return false;
        if (ageClassGeneral == null) {
            if (other.ageClassGeneral != null)
                return false;
        } else if (!ageClassGeneral.equals(other.ageClassGeneral))
            return false;
        if (ageClassDetail == null) {
            if (other.ageClassDetail != null)
                return false;
        } else if (!ageClassDetail.equals(other.ageClassDetail))
            return false;
        if (stateGroup != other.stateGroup)
            return false;
        if (singlePoints == null) {
            if (other.singlePoints != null)
                return false;
        } else if (!singlePoints.equals(other.singlePoints))
            return false;
        if (singleRanking == null) {
            if (other.singleRanking != null)
                return false;
        } else if (!singleRanking.equals(other.singleRanking))
            return false;
        if (singleAgeRanking == null) {
            if (other.singleAgeRanking != null)
                return false;
        } else if (!singleAgeRanking.equals(other.singleAgeRanking))
            return false;
        if (singleTournaments == null) {
            if (other.singleTournaments != null)
                return false;
        } else if (!singleTournaments.equals(other.singleTournaments))
            return false;
        if (doublePoints == null) {
            if (other.doublePoints != null)
                return false;
        } else if (!doublePoints.equals(other.doublePoints))
            return false;
        if (doubleRanking == null) {
            if (other.doubleRanking != null)
                return false;
        } else if (!doubleRanking.equals(other.doubleRanking))
            return false;
        if (doubleAgeRanking == null) {
            if (other.doubleAgeRanking != null)
                return false;
        } else if (!doubleAgeRanking.equals(other.doubleAgeRanking))
            return false;
        if (doubleTournaments == null) {
            if (other.doubleTournaments != null)
                return false;
        } else if (!doubleTournaments.equals(other.doubleTournaments))
            return false;
        if (mixedPoints == null) {
            if (other.mixedPoints != null)
                return false;
        } else if (!mixedPoints.equals(other.mixedPoints))
            return false;

        if (((mixedRanking == null) && (other.mixedRanking != null)) || (!mixedRanking.equals(other.mixedRanking))) {
            return false;
        }
        if (((mixedAgeRanking == null) && (other.mixedAgeRanking != null)) || (!mixedAgeRanking.equals(other.mixedAgeRanking))) {
            return false;
        }
        if (((mixedTournaments == null) && (other.mixedTournaments != null)) || (!mixedTournaments.equals(other.mixedTournaments))) {
            return false;
        }
        return true;
    }

    public String getPlayerTournamentId() {
        return playerTournamentId;
    }

    public void setPlayerTournamentId(String playerTournamentId) {
        this.playerTournamentId = playerTournamentId;
    }

    public Integer getSingleAgeRanking() {
        return singleAgeRanking;
    }

    public Integer getDoubleAgeRanking() {
        return doubleAgeRanking;
    }

    public Integer getMixedAgeRanking() {
        return mixedAgeRanking;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public Group getStateGroup() {
        return stateGroup;
    }

    public void setStateGroup(Group stateGroup) {
        this.stateGroup = stateGroup;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAgeClassGeneral() {
        return ageClassGeneral;
    }

    public void setAgeClassGeneral(String ageClassGeneral) {
        this.ageClassGeneral = ageClassGeneral;
    }

    public String getAgeClassDetail() {
        return ageClassDetail;
    }

    public void setAgeClassDetail(String ageClassDetail) {
        this.ageClassDetail = ageClassDetail;
    }

    public Integer getSinglePoints() {
        return singlePoints;
    }

    public void setSinglePoints(Integer singlePoints) {
        this.singlePoints = singlePoints;
    }

    public Integer getSingleRanking() {
        return singleRanking;
    }

    public void setSingleRanking(Integer singleRanking) {
        this.singleRanking = singleRanking;
    }

    public Integer getSingleTournaments() {
        return singleTournaments;
    }

    public void setSingleTournaments(Integer singleTournaments) {
        this.singleTournaments = singleTournaments;
    }

    public Integer getDoublePoints() {
        return doublePoints;
    }

    public void setDoublePoints(Integer doublePoints) {
        this.doublePoints = doublePoints;
    }

    public Integer getDoubleRanking() {
        return doubleRanking;
    }

    public void setDoubleRanking(Integer doubleRanking) {
        this.doubleRanking = doubleRanking;
    }

    public Integer getDoubleTournaments() {
        return doubleTournaments;
    }

    public void setDoubleTournaments(Integer doubleTournaments) {
        this.doubleTournaments = doubleTournaments;
    }

    public Integer getMixedPoints() {
        return mixedPoints;
    }

    public void setMixedPoints(Integer mixedPoints) {
        this.mixedPoints = mixedPoints;
    }

    public Integer getMixedRanking() {
        return mixedRanking;
    }

    public void setMixedRanking(Integer mixedRanking) {
        this.mixedRanking = mixedRanking;
    }

    public Integer getMixedTournaments() {
        return mixedTournaments;
    }

    public void setMixedTournaments(Integer mixedTournaments) {
        this.mixedTournaments = mixedTournaments;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }
}
