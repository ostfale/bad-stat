package de.ostfale.qk.parser.ranking.api;

import de.ostfale.qk.parser.ranking.internal.RankingPlayer;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface RankingParser {

    List<RankingPlayer> parseRankingFile(InputStream rankingFile);

}
