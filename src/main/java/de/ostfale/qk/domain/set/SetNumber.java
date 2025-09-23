package de.ostfale.qk.domain.set;

public enum SetNumber {

    FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5);

    private final int setNo;

    SetNumber(int i) {
        this.setNo = i;
    }

    public int getSetNo() {
        return setNo;
    }
}
