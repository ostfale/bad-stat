package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.tournament.TournamentPlayer;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public class TournamentPlayerRepository implements PanacheRepositoryBase<TournamentPlayer, Long> {
}
