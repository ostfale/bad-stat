package de.ostfale.qk.db.internal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Tournament {

    @Id
    @GeneratedValue
    private Long id;

    private String tournamentID;
    private String tournamentName;
    private String tournamentOrganizer;
    private String tournamentLocation;
    private String tournamentDate;
    private Integer tournamentYear;

    public Tournament(String tournamentID, String tournamentName, String tournamentOrganizer, String tournamentLocation, String tournamentDate, Integer tournamentYear) {
        this.tournamentID = tournamentID;
        this.tournamentName = tournamentName;
        this.tournamentOrganizer = tournamentOrganizer;
        this.tournamentLocation = tournamentLocation;
        this.tournamentDate = tournamentDate;
        this.tournamentYear = tournamentYear;
    }

    public Tournament() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(String tournamentID) {
        this.tournamentID = tournamentID;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentOrganizer() {
        return tournamentOrganizer;
    }

    public void setTournamentOrganizer(String tournamentOrganizer) {
        this.tournamentOrganizer = tournamentOrganizer;
    }

    public String getTournamentLocation() {
        return tournamentLocation;
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

    public Integer getTournamentYear() {
        return tournamentYear;
    }

    public void setTournamentYear(Integer tournamentYear) {
        this.tournamentYear = tournamentYear;
    }
}
