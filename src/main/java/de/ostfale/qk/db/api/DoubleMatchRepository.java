package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.DoubleMatch;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoubleMatchRepository implements PanacheRepository<DoubleMatch> {
}


