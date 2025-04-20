package de.ostfale.qk.domain.player;

public class PlayerInfo {

    private String ageClassGeneral;
    private String ageClassSpecific;
    private String clubName;
    private String districtName;
    private String stateName;
    private Group  groupName;

    public String getAgeClassGeneral() {
        return ageClassGeneral;
    }

    public void setAgeClassGeneral(String ageClassGeneral) {
        this.ageClassGeneral = ageClassGeneral;
    }

    public String getAgeClassSpecific() {
        return ageClassSpecific;
    }

    public void setAgeClassSpecific(String ageClassSpecific) {
        this.ageClassSpecific = ageClassSpecific;
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

    public Group getGroupName() {
        return groupName;
    }

    public void setGroupName(Group groupName) {
        this.groupName = groupName;
    }
}
