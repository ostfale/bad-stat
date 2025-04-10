package de.ostfale.qk.parser.ranking.api;

import java.io.InputStream;
import java.util.List;

import de.ostfale.qk.parser.ranking.internal.RankingPlayer;

public interface RankingParser {

    List<RankingPlayer> parseRankingFile(InputStream rankingFile);

}
