package de.ostfale.qk.parser.ranking.internal;

public enum RankingFileColIndex {
    GENDER_INDEX(0),
    DISCIPLINE_INDEX(1),
    RANKING_INDEX(2),
    AGE_RANKING_INDEX(3),
    LAST_NAME_INDEX(4),
    FIRST_NAME_INDEX(5),
    PLAYER_ID_INDEX(6),
    BIRTH_YEAR_INDEX(7),
    AGE_CLASS_DETAIL_INDEX(8),
    AGE_CLASS_GENERAL_INDEX(9),
    VALID_POINTS_INDEX(10),
    TOURNAMENTS_INDEX(11),
    CLUB_NAME_INDEX(12),
    DISTRICT_NAME_INDEX(13),
    STATE_NAME_INDEX(14),
    STATE_GROUP_INDEX(15);

    private final int index;

    RankingFileColIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
