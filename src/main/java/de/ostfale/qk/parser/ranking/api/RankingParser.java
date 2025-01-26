package de.ostfale.qk.parser.ranking.api;

import de.ostfale.qk.parser.ranking.internal.Player;

import java.io.File;
import java.util.List;

public interface RankingParser {

    List<Player> parseRankingFile(File rankingFile);
}
