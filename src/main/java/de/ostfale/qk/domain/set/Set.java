package de.ostfale.qk.domain.set;

public class Set {

    private SetNumber setNumber;
    private int firstValue;
    private int secondValue;
    private String nonRegularResult;

    public SetNumber getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(SetNumber setNumber) {
        this.setNumber = setNumber;
    }

    public int getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(int firstValue) {
        this.firstValue = firstValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(int secondValue) {
        this.secondValue = secondValue;
    }

    public String getNonRegularResult() {
        return nonRegularResult;
    }

    public void setNonRegularResult(String nonRegularResult) {
        this.nonRegularResult = nonRegularResult;
    }
}
