package de.ostfale.qk.db.app;

import java.util.List;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class BadStatConfigService {

    private static final Logger log = Logger.getLogger(BadStatConfigService.class);

    @Inject
    BadStatConfigRepository repository;

    public BadStatConfig readConfiguration() {
        List<BadStatConfig> configList = repository.listAll();

        if (configList.size() == 1) {
            log.trace("BadStatConfigService :: found one config to be returned!");
            return configList.getFirst();
        }
        log.info("BadStatConfigService :: No config found -> will be created and returned");
        return new BadStatConfig();
    }

    public void saveConfiguration(BadStatConfig badStatConfig) {
        log.debug("BadStatConfigService :: save configuration");
        repository.persist(badStatConfig);
    }
}
