package de.ostfale.qk.domain.points;

public record RankingPoint(
        int value,
        String displayValue) {

    public RankingPoint(String displayValue) {
        this(Integer.parseInt(displayValue), displayValue);
    }

    public RankingPoint(int value) {
        this(value, String.valueOf(value));
    }
}
