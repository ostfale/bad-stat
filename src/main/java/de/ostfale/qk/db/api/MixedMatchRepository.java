package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.match.MixedMatch;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MixedMatchRepository implements PanacheRepository<MixedMatch> {
}
