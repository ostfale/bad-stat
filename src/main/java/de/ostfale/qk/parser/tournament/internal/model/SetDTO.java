package de.ostfale.qk.parser.tournament.internal.model;

public record SetDTO(
        int firstPoints,
        int secondPoints,
        String setStatus
) {
    public SetDTO(int firstPoints, int secondPoints) {
        this(firstPoints, secondPoints, null);
    }
}
