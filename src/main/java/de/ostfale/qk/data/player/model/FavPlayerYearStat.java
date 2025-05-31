package de.ostfale.qk.data.player.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record FavPlayerYearStat(
        int year,
        int played,
        int loaded
) {
    @JsonIgnore
    public String getDisplayFormat() {
        return String.format("%d | %d", played, loaded);
    }
}
