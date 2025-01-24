package de.ostfale.qk.parser.excel.api;

import de.ostfale.qk.parser.excel.internal.Player;

import java.util.List;

public interface RankingParser {

    List<Player> parseRankingFile(String filePath);
}
