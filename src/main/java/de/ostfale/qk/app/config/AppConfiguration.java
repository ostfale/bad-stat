package de.ostfale.qk.app.config;

import org.jboss.logging.Logger;

import java.time.LocalDateTime;

public class AppConfiguration {

    Logger log = Logger.getLogger(AppConfiguration.class);

    private LocalDateTime lastApplicationStart ;
    private LocalDateTime lastRankingDownload;
    private LocalDateTime lastTournamentDownload;
    private String lastRankingFileName;

    public String getLastRankingFileName() {
        return lastRankingFileName;
    }

    public void setLastRankingFileName(String lastRankingFileName) {
        this.lastRankingFileName = lastRankingFileName;
    }

    public LocalDateTime getLastRankingDownload() {
        return lastRankingDownload;
    }

    public AppConfiguration() {
        this.lastApplicationStart = LocalDateTime.now();
    }

    public void setLastRankingDownload(LocalDateTime lastRankingDownload) {
        log.debugf("Set last ranking download to %s", lastRankingDownload);
        this.lastRankingDownload = lastRankingDownload;
    }

    public LocalDateTime getLastTournamentDownload() {
        return lastTournamentDownload;
    }

    public void setLastTournamentDownload(LocalDateTime lastTournamentDownload) {
        log.debugf("Set last tournament download to %s", lastTournamentDownload);
        this.lastTournamentDownload = lastTournamentDownload;
    }

    public LocalDateTime getLastApplicationStart() {
        return lastApplicationStart;
    }

    public void setLastApplicationStart(LocalDateTime lastApplicationStart) {
        this.lastApplicationStart = lastApplicationStart;
    }
}

