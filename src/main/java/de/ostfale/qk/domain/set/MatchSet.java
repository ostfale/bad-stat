package de.ostfale.qk.domain.set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.ostfale.qk.domain.match.MatchResultType;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchSet {
    private SetNumber setNumber;
    private MatchResultType matchResultType = MatchResultType.REGULAR;
    private int firstValue;
    private int secondValue;

    public MatchSet(SetNumber setNumber, int firstValue, int secondValue) {
        this.setNumber = setNumber;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public MatchSet(MatchResultType matchResultType, SetNumber setNumber, int firstValue, int secondValue) {
        this.matchResultType = matchResultType;
        this.setNumber = setNumber;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public MatchSet() {
    }

    @JsonIgnore
    public String getDisplayString() {
        String firstValueString = firstValue < 10 ? " " + firstValue : String.valueOf(firstValue);
        String secondValueString = secondValue < 10 ? " " + secondValue : String.valueOf(secondValue);
        return String.format("[Satz %d] %s : %s", setNumber.getSetNo(), firstValueString, secondValueString);
    }

    public MatchResultType getMatchResultType() {
        return matchResultType;
    }

    public void setMatchResultType(MatchResultType matchResultType) {
        this.matchResultType = matchResultType;
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
