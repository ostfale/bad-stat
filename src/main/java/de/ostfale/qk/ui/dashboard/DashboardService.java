package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import de.ostfale.qk.ui.dashboard.model.DashboardUIModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DashboardService {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    @Inject
    RankingDownloader rankingDownloader;

    public boolean downloadRankingFile() {
        log.info("Downloading ranking file");
        rankingDownloader.downloadRankingFile();
        return true;
    }

    public DashboardUIModel getDashboardUIModel(){
        log.debug("DashboardService :: retrieve DashboardUIModel");
        return null;
    }
}
