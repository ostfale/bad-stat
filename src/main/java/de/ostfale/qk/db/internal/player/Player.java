package de.ostfale.qk.db.internal.player;

import de.ostfale.qk.domain.player.GenderType;
import de.ostfale.qk.domain.player.Group;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Player {

    private static final Logger log = Logger.getLogger(Player.class);

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    public LocalDateTime updatedAt;

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
        if ((playerId == null && other.playerId != null) || (!Objects.equals(playerId, other.playerId))) {
            log.trace("diff player-id");
            return false;
        }

        if (((stateGroup == null) && (other.stateGroup != null)) || (!Objects.equals(stateGroup, other.stateGroup))) {
            log.trace("diff state-group");
            return false;
        }
        if (((stateName == null) && (other.stateName != null)) || (!Objects.requireNonNull(stateName).equalsIgnoreCase(other.stateName))) {
            log.trace("diff state-name");
            return false;
        }
        if (((clubName == null) && (other.clubName != null)) || (!Objects.requireNonNull(clubName).equalsIgnoreCase(other.clubName))) {
            log.trace("diff club-name");
            return false;
        }
        if (((districtName == null) && (other.districtName != null)) || (!Objects.requireNonNull(districtName).equalsIgnoreCase(other.districtName))) {
            log.trace("diff district-name");
            return false;
        }
        if (((ageClassGeneral == null) && (other.ageClassGeneral != null)) || (!Objects.requireNonNull(ageClassGeneral).equalsIgnoreCase(other.ageClassGeneral))) {
            log.trace("diff age-class-general");
            return false;
        }
        if (((ageClassDetail == null) && (other.ageClassDetail != null)) || (!Objects.requireNonNull(ageClassDetail).equalsIgnoreCase(other.ageClassDetail))) {
            log.trace("diff age-class-detail");
            return false;
        }
        if (((singlePoints == null) && (other.singlePoints != null)) || (!Objects.equals(singlePoints, other.singlePoints))) {
            log.trace("diff single-points");
            return false;
        }
        if (((singleRanking == null) && (other.singleRanking != null)) || (!Objects.equals(singleRanking, other.singleRanking))) {
            log.trace("diff single-ranking");
            return false;
        }
        if (((singleAgeRanking == null) && (other.singleAgeRanking != null)) || (!Objects.equals(singleAgeRanking, other.singleAgeRanking))) {
            log.trace("diff single-age-ranking");
            return false;
        }
        if (((singleTournaments == null) && (other.singleTournaments != null)) || (!Objects.equals(singleTournaments, other.singleTournaments))) {
            log.trace("diff single-tournaments");
            return false;
        }
        if (((doublePoints == null) && (other.doublePoints != null)) || (!Objects.equals(doublePoints, other.doublePoints))) {
            log.trace("diff double-points");
            return false;
        }
        if (((doubleRanking == null) && (other.doubleRanking != null)) || (!Objects.equals(doubleRanking, other.doubleRanking))) {
            log.trace("diff double-ranking");
            return false;
        }
        if (((doubleAgeRanking == null) && (other.doubleAgeRanking != null)) || (!Objects.equals(doubleAgeRanking, other.doubleAgeRanking))) {
            log.trace("diff double-age-ranking");
            return false;
        }
        if (((doubleTournaments == null) && (other.doubleTournaments != null)) || (!Objects.equals(doubleTournaments, other.doubleTournaments))) {
            log.trace("diff double-tournaments");
            return false;
        }
        if (((mixedPoints == null) && (other.mixedPoints != null)) || (!Objects.equals(mixedPoints, other.mixedPoints))) {
            log.trace("diff mixed-points");
            return false;
        }
        if (((mixedRanking == null) && (other.mixedRanking != null)) || (!Objects.equals(mixedRanking, other.mixedRanking))) {
            log.trace("diff mixed-ranking");
            return false;
        }
        if (((mixedAgeRanking == null) && (other.mixedAgeRanking != null)) || (!Objects.equals(mixedAgeRanking, other.mixedAgeRanking))) {
            log.trace("diff mixed-age-ranking");
            return false;
        }
        if (((mixedTournaments == null) && (other.mixedTournaments != null)) || (!Objects.equals(mixedTournaments, other.mixedTournaments))) {
            log.trace("diff mixed-tournaments");
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
