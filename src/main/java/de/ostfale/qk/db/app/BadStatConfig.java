package de.ostfale.qk.db.app;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BadStatConfig {

    @Id
    @GeneratedValue
    private Long id;

    private String rankingFileName;
    private String tournamentFileName;

    private LocalDateTime lastRankingFileDownload;
    private LocalDateTime lastTournamentFileDownload;

    
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
