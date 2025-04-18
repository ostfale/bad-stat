package de.ostfale.qk.db.app;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

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

    public void saveConfig(BadStatConfig badStatConfig) {
        if (badStatConfig.getId() == null) {
            log.debug("BadStatConfigService :: save new config");
            repository.persist(badStatConfig);
        } else {
            log.debug("BadStatConfigService :: update existing config");
            var foundEntity = repository.findById(badStatConfig.getId());
            foundEntity.update(badStatConfig);
            repository.persist(foundEntity);
            log.debug("BadStatConfigService :: updated existing config");
        }
    }
}
