package de.ostfale.qk.parser.set;

public enum SetNo {
    FIRST (1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5);

    private final int setNo;

    SetNo(int i) {
        this.setNo = i;
    }

    public int getSetNo() {
        return setNo;
    }
}
