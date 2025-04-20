package de.ostfale.qk.db.app;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "bad_stat_config")
public class BadStatConfig {

    @Id
    @GeneratedValue
    private Long id;

    private String rankingFileName;
    private String tournamentFileName;
    private String databaseCW;


    private LocalDateTime lastRankingFileDownload;
    private LocalDateTime lastTournamentFileDownload;

    public void update(BadStatConfig badStatConfig) {
        this.rankingFileName = badStatConfig.getRankingFileName();
        this.tournamentFileName = badStatConfig.getTournamentFileName();
        this.lastRankingFileDownload = badStatConfig.getLastRankingFileDownload();
        this.lastTournamentFileDownload = badStatConfig.getLastTournamentFileDownload();
        this.databaseCW = badStatConfig.getDatabaseCW();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatabaseCW() {
        return databaseCW;
    }

    public void setDatabaseCW(String databaseCW) {
        this.databaseCW = databaseCW;
    }

    public String getRankingFileName() {
        return rankingFileName;
    }

    public void setRankingFileName(String rankingFileName) {
        this.rankingFileName = rankingFileName;
    }

    public String getTournamentFileName() {
        return tournamentFileName;
    }

    public void setTournamentFileName(String tournamentFileName) {
        this.tournamentFileName = tournamentFileName;
    }

    public LocalDateTime getLastRankingFileDownload() {
        return lastRankingFileDownload;
    }

    public void setLastRankingFileDownload(LocalDateTime lastRankingFileDownload) {
        this.lastRankingFileDownload = lastRankingFileDownload;
    }

    public LocalDateTime getLastTournamentFileDownload() {
        return lastTournamentFileDownload;
    }

    public void setLastTournamentFileDownload(LocalDateTime lastTournamentFileDownload) {
        this.lastTournamentFileDownload = lastTournamentFileDownload;
    }
}
