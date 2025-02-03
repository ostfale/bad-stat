package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.match.SingleMatch;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SingleMatchRepository implements PanacheRepository<SingleMatch> {

}


