package de.ostfale.qk.domain.player;

public record RankingInformation(
        int rankingPoints,
        int rankingPosition,
        int ageRankingPoints,
        int tournaments
) {
}
