package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import de.ostfale.qk.ui.dashboard.model.DashboardUIModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.File;
import java.util.List;

import org.jboss.logging.Logger;

@ApplicationScoped
public class DashboardService {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    @Inject
    RankingDownloader rankingDownloader;

    public String downloadRankingFile() {
        log.info("Downloading ranking file");
        rankingDownloader.downloadRankingFile();
        List<File> rankingFiles = rankingDownloader.getRankingFiles();
        if (rankingFiles.size() == 1) {
            return rankingFiles.getFirst().getName();
        }
        return "";
    }

    public DashboardUIModel getDashboardUIModel() {
        log.debug("DashboardService :: retrieve DashboardUIModel");
        return null;
    }
}
