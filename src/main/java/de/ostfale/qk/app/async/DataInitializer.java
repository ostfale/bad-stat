package de.ostfale.qk.app.async;

import de.ostfale.qk.parser.pointstable.CSVPointsTableService;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Startup
@ApplicationScoped
public class DataInitializer {

    @Inject
    CSVPointsTableService csvPointsTableService;

    @PostConstruct
    public void loadData() {
        Log.info("ASYNC :: DataInitializer :: load data");
    }
}
