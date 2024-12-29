package de.ostfale.qk.parser.match.internal.model;

public class MixedMatchDTO extends MatchDTO {

    @Override
    Discipline getDiscipline() {
        return Discipline.MIXED;
    }
}
