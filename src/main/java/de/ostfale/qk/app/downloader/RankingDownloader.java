package de.ostfale.qk.app.downloader;

import de.ostfale.qk.app.FileSystemFacade;
import org.jboss.logging.Logger;

public class RankingDownloader implements FileSystemFacade {

    private static final Logger log = Logger.getLogger(RankingDownloader.class);

    @Override
    public String getDownloadDirectory() {
        return "";
    }
}
