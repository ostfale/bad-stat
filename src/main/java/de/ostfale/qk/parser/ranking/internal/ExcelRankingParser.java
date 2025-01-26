package de.ostfale.qk.parser.ranking.internal;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ExcelRankingParser implements RankingParser {

    private static final Logger log = LoggerFactory.getLogger(ExcelRankingParser.class);

    @Override
    public List<Player> parseRankingFile(File rankingFile) {
        final Map<String, Player> playerMap = new HashMap<>();
        Sheet sheet = readFirstSheet(rankingFile);
        for (Row row : sheet) {
            // Skip first row if it contains headers
            if (row.getRowNum() == 0) {
                continue;
            }

            // Stop processing if the row is empty
            if (isRowEmpty(row)) {
                break;
            }

            parseRow(row, playerMap);
        }
        return List.copyOf(playerMap.values());
    }

    private Sheet readFirstSheet(File excelFile) {
        try {
            FileInputStream file = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(file);
            return workbook.getSheetAt(0);
        } catch (IOException e) {
            log.error("PARSER :: Player : could not parse file: {} because of: {}", excelFile.getName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void parseRow(Row row, Map<String, Player> playerMap) {
        try {
            String playerId = getCellValue(row, RankingFileColIndex.PLAYER_ID_INDEX);
            String firstName = getCellValue(row, RankingFileColIndex.FIRST_NAME_INDEX);
            String lastName = getCellValue(row, RankingFileColIndex.LAST_NAME_INDEX);

            String gender = getCellValue(row, RankingFileColIndex.GENDER_INDEX);
            GenderType genderType = GenderType.lookup(gender);

            String birthYearString = getCellValue(row, RankingFileColIndex.BIRTH_YEAR_INDEX);
            Integer birthYear = Integer.parseInt(birthYearString);

            String ageClassGeneral = getCellValue(row, RankingFileColIndex.AGE_CLASS_GENERAL_INDEX);
            String ageClassDetail = getCellValue(row, RankingFileColIndex.AGE_CLASS_DETAIL_INDEX);
            String clubName = getCellValue(row, RankingFileColIndex.CLUB_NAME_INDEX);
            String districtName = getCellValue(row, RankingFileColIndex.DISTRICT_NAME_INDEX);
            String stateName = getCellValue(row, RankingFileColIndex.STATE_NAME_INDEX);

            String stateGroup = getCellValue(row, RankingFileColIndex.STATE_GROUP_INDEX);
            Group group = Group.lookup(stateGroup);


            String disciplineString = getCellValue(row, RankingFileColIndex.DISCIPLINE_INDEX);
            Discipline discipline = Discipline.lookup(disciplineString);

            String ranking = getCellValue(row, RankingFileColIndex.RANKING_INDEX);
            Integer rankingInt = Integer.parseInt(ranking);

            String validPoints = getCellValue(row, RankingFileColIndex.VALID_POINTS_INDEX);
            Integer points = Integer.parseInt(validPoints);

            String tournaments = getCellValue(row, RankingFileColIndex.TOURNAMENTS_INDEX);
            Integer noOfTournaments = Integer.parseInt(tournaments);

            Player existingOrNewPlayer = getOrCreatePlayer(playerId, firstName, lastName, genderType, birthYear, ageClassGeneral,
                    ageClassDetail, clubName, districtName, stateName, group, playerMap);

            switch (discipline) {
                case SINGLE -> existingOrNewPlayer.setSinglePointsAndRanking(points, rankingInt, noOfTournaments);
                case DOUBLE -> existingOrNewPlayer.setDoublePointsAndRanking(points, rankingInt, noOfTournaments);
                case MIXED -> existingOrNewPlayer.setMixedPointsAndRanking(points, rankingInt, noOfTournaments);
            }

        } catch (Exception e) {
            log.error("Failed to parse row {} because of: {}", row.getRowNum(), e.getMessage());
        }
    }

    private Player getOrCreatePlayer(String playerId, String firstName, String lastName, GenderType genderType, Integer birthYear,
                                     String ageClassGeneral, String ageClassDetail, String clubName, String districtName,
                                     String stateName, Group group, Map<String, Player> playerMap) {
        return playerMap.computeIfAbsent(playerId, id ->
                new Player(id, firstName, lastName, genderType, birthYear, ageClassGeneral, ageClassDetail, clubName, districtName, stateName, group));
    }

    private String getCellValue(Row row, RankingFileColIndex colIndex) {
        log.debug("Parse row for column index  {}", colIndex.name());
        Cell cell = row.getCell(colIndex.getIndex());
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                log.warn("Found a cell formula!!");
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
