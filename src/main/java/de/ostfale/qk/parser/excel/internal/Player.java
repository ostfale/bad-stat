package de.ostfale.qk.parser.excel.internal;

public record Player(
        String playerId,
        String firstName,
        String lastName,
        GenderType genderType,
        Integer yearOfBirth,
        String ageClassGeneral,
        String ageClassDetail,
        String clubName,
        String districtName,
        String stateName,
        Group stateGroup,
        Integer singlePoints,
        Integer singleRanking,
        Integer doublePoints,
        Integer doubleRanking,
        Integer mixedPoints,
        Integer mixedRanking
) {
}
