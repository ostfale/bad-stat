package de.ostfale.qk.parser.tournament.model;

import de.ostfale.qk.parser.discipline.model.DisciplineParserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TournamentParserModel {

    private List<DisciplineParserModel> tournamentDisciplines = new ArrayList<>();

    private String tournamentId;
    private String tournamentName;
    private String tournamentOrganisation;
    private String tournamentLocation;
    private String tournamentDate;


    public TournamentParserModel(String tournamentId, String tournamentName, String tournamentOrganisation, String tournamentLocation, String tournamentDate) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.tournamentOrganisation = tournamentOrganisation;
        this.tournamentLocation = tournamentLocation;
        this.tournamentDate = tournamentDate;
    }

    public void addDiscipline(DisciplineParserModel tournamentDiscipline) {
        this.tournamentDisciplines.add(tournamentDiscipline);
    }

    public List<DisciplineParserModel> getTournamentDisciplines() {
        return tournamentDisciplines;
    }

    public void setTournamentDisciplines(List<DisciplineParserModel> tournamentDisciplines) {
        this.tournamentDisciplines = tournamentDisciplines;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentOrganisation() {
        return tournamentOrganisation;
    }

    public void setTournamentOrganisation(String tournamentOrganisation) {
        this.tournamentOrganisation = tournamentOrganisation;
    }

    public String getTournamentLocation() {
        int bracketIndex = tournamentLocation.indexOf("[");
        var strippedLocation = (bracketIndex != -1) ? tournamentLocation.substring(0, bracketIndex) : tournamentLocation;
        return strippedLocation.trim();
    }

    public void setTournamentLocation(String tournamentLocation) {
        this.tournamentLocation = tournamentLocation;
    }

    public String getTournamentDate() {
        return tournamentDate;
    }

    public void setTournamentDate(String tournamentDate) {
        this.tournamentDate = tournamentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TournamentParserModel that = (TournamentParserModel) o;
        return Objects.equals(tournamentId, that.tournamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tournamentId);
    }
}
