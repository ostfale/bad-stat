package de.ostfale.qk.domain.set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchSet {
    private SetNumber setNumber;
    private int firstValue;
    private int secondValue;
    private boolean isRegularSet = true;
    private String nonRegularResult;

    public MatchSet(SetNumber setNumber, int firstValue, int secondValue) {
        this.setNumber = setNumber;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public MatchSet(String nonRegularResult) {
        this.nonRegularResult = nonRegularResult;
        this.isRegularSet = false;
    }

    public MatchSet() {
    }

    @JsonIgnore
    public String getDisplayString() {
        if (isRegularSet) {
            String firstValueString = firstValue < 10 ? " " + firstValue : String.valueOf(firstValue);
            String secondValueString = secondValue < 10 ? " " + secondValue : String.valueOf(secondValue);
            return String.format("[Satz %d] %s : %s", setNumber.getSetNo(), firstValueString, secondValueString);
        }
        return "";
    }

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

    public boolean isRegularSet() {
        return isRegularSet;
    }

    public void setRegularSet(boolean regularSet) {
        isRegularSet = regularSet;
    }

    public String getNonRegularResult() {
        return nonRegularResult;
    }

    public void setNonRegularResult(String nonRegularResult) {
        this.nonRegularResult = nonRegularResult;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MatchSet matchSet = (MatchSet) o;
        return setNumber == matchSet.setNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(setNumber);
    }
}
