package de.ostfale.qk.parser.set;

import java.util.Objects;

public class SetDTO {

    private SetNo setNo;
    private Boolean isRegular = true;
    private final  Integer firstValue;
    private final  Integer secondValue;

    public SetDTO(SetNo setNo, Integer firstValue, Integer secondValue) {
        this.setNo = setNo;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public boolean firstIsBetterThanSecond() {
        return firstValue > secondValue;
    }

    public Integer getFirstValue() {
        return firstValue;
    }

    public Integer getSecondValue() {
        return secondValue;
    }

    public SetNo getSetNo() {
        return setNo;
    }

    public void setSetNo(SetNo setNo) {
        this.setNo = setNo;
    }

    public Boolean getRegular() {
        return isRegular;
    }

    public void setRegular(Boolean regular) {
        isRegular = regular;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SetDTO setDTO = (SetDTO) o;
        return setNo == setDTO.setNo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(setNo);
    }
}
