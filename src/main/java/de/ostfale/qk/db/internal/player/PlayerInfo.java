package de.ostfale.qk.db.internal.player;

import de.ostfale.qk.parser.ranking.internal.Group;
import jakarta.persistence.*;

@Entity
public class PlayerInfo {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Player player;

    private String clubName;
    private String districtName;
    private String stateName;
    private  String ageClassGeneral;
    private  String ageClassDetail;

    @Enumerated(EnumType.STRING)
    private Group stateGroup;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public Group getStateGroup() {
        return stateGroup;
    }

    public void setStateGroup(Group stateGroup) {
        this.stateGroup = stateGroup;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}
