package de.ostfale.qk.parser.web.match;

import java.util.Objects;

public record MatchPlayers(
        String firstPlayerName,
        String secondPlayerName
) {
    public MatchPlayers(String firstPlayerName) {
        this(firstPlayerName, null);
    }

    public boolean isSingleMatch() {
        return secondPlayerName == null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MatchPlayers that = (MatchPlayers) o;
        return Objects.equals(firstPlayerName, that.firstPlayerName) && Objects.equals(secondPlayerName, that.secondPlayerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPlayerName, secondPlayerName);
    }
}
