package de.ostfale.qk.parser.ranking.api;

import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;

import java.io.InputStream;
import java.util.List;

public interface RankingParser {

    List<RankingPlayer> parseRankingFile(InputStream rankingFile);

    List<Player> parseRankingFileToPlayers(InputStream rankingFile);

}
