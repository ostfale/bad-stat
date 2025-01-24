package de.ostfale.qk.parser.excel.internal;

import de.ostfale.qk.parser.excel.api.RankingParser;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class ExcelRankingParser  implements RankingParser {

    private static final Logger log = LoggerFactory.getLogger(ExcelRankingParser.class);

    @Override
    public List<Player> parseRankingFile(String filePath) {
        return List.of();
    }
}
