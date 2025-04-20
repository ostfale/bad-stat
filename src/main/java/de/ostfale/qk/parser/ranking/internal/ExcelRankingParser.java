package de.ostfale.qk.parser.ranking.internal;

import de.ostfale.qk.domain.player.GenderType;
import de.ostfale.qk.domain.player.Group;
import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ExcelRankingParser implements RankingParser {

    private static final Logger log = Logger.getLogger(ExcelRankingParser.class);

    @Override
    public List<RankingPlayer> parseRankingFile(InputStream rankingFile) {
        final Map<String, RankingPlayer> playerMap = new HashMap<>();
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

    @Override
    public List<Player> parseRankingFileToPlayers(InputStream rankingFile) {
        return List.of();
    }

    private Sheet readFirstSheet(InputStream excelInputStream) {
        final int FIRST_SHEET_INDEX = 0;
        try (Workbook workbook = createWorkbook(excelInputStream)) {
            return workbook.getSheetAt(FIRST_SHEET_INDEX);
        } catch (IOException e) {
            log.errorf("Excel PARSER :: Player : could not parse file. Reason: %s", e.getMessage());
            throw new RuntimeException("Failed to read the first sheet from Excel file", e);
        }
    }

    private Workbook createWorkbook(InputStream excelInputStream) throws IOException {
        return new XSSFWorkbook(excelInputStream);
    }

    private void parseRow(Row row, Map<String, RankingPlayer> playerMap) {
        try {
            String playerId = extractCellValue(row, RankingFileColIndex.PLAYER_ID_INDEX);
            String firstName = extractCellValue(row, RankingFileColIndex.FIRST_NAME_INDEX);
            String lastName = extractCellValue(row, RankingFileColIndex.LAST_NAME_INDEX);

            String gender = extractCellValue(row, RankingFileColIndex.GENDER_INDEX);
            GenderType genderType = GenderType.lookup(gender);

            String birthYearString = extractCellValue(row, RankingFileColIndex.BIRTH_YEAR_INDEX);
            Integer birthYear = Integer.parseInt(birthYearString);

            String ageClassGeneral = extractCellValue(row, RankingFileColIndex.AGE_CLASS_GENERAL_INDEX);
            String ageClassDetail = extractCellValue(row, RankingFileColIndex.AGE_CLASS_DETAIL_INDEX);
            String clubName = extractCellValue(row, RankingFileColIndex.CLUB_NAME_INDEX);
            String districtName = extractCellValue(row, RankingFileColIndex.DISTRICT_NAME_INDEX);
            String stateName = extractCellValue(row, RankingFileColIndex.STATE_NAME_INDEX);

            String stateGroup = extractCellValue(row, RankingFileColIndex.STATE_GROUP_INDEX);
            Group group = Group.lookup(stateGroup);

            String disciplineString = extractCellValue(row, RankingFileColIndex.DISCIPLINE_INDEX);
            Discipline discipline = Discipline.lookup(disciplineString);

            String ranking = extractCellValue(row, RankingFileColIndex.RANKING_INDEX);
            Integer rankingInt = Integer.parseInt(ranking);

            String ageRanking = extractCellValue(row, RankingFileColIndex.AGE_RANKING_INDEX);
            Integer ageRankingInt = Integer.parseInt(ageRanking);

            String validPoints = extractCellValue(row, RankingFileColIndex.VALID_POINTS_INDEX);
            Integer points = Integer.parseInt(validPoints);

            String tournaments = extractCellValue(row, RankingFileColIndex.TOURNAMENTS_INDEX);
            Integer noOfTournaments = Integer.parseInt(tournaments);

            RankingPlayer existingOrNewRankingPlayer = getOrCreatePlayer(playerId, firstName, lastName, genderType, birthYear, ageClassGeneral,
                    ageClassDetail, clubName, districtName, stateName, group, playerMap);

            switch (discipline) {
                case SINGLE ->
                        existingOrNewRankingPlayer.setSinglePointsAndRanking(points, rankingInt, ageRankingInt, noOfTournaments);
                case DOUBLE ->
                        existingOrNewRankingPlayer.setDoublePointsAndRanking(points, rankingInt, ageRankingInt, noOfTournaments);
                case MIXED ->
                        existingOrNewRankingPlayer.setMixedPointsAndRanking(points, rankingInt, ageRankingInt, noOfTournaments);
            }

        } catch (Exception e) {
            log.errorf("Failed to parse row {} because of: {}", row.getRowNum(), e.getMessage());
        }
    }

    private RankingPlayer getOrCreatePlayer(String playerId, String firstName, String lastName, GenderType genderType, Integer birthYear,
                                            String ageClassGeneral, String ageClassDetail, String clubName, String districtName,
                                            String stateName, Group group, Map<String, RankingPlayer> playerMap) {
        return playerMap.computeIfAbsent(playerId, id ->
                new RankingPlayer(id, firstName, lastName, genderType, birthYear, ageClassGeneral, ageClassDetail, clubName, districtName, stateName, group));
    }

    private String extractCellValue(Row row, RankingFileColIndex colIndex) {
        log.tracef("Extracting value from row %d for column index %s", row.getRowNum(), colIndex.name());
        Cell cell = row.getCell(colIndex.getIndex());

        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        return switch (cellType) {
            case STRING -> getStringCellValue(cell);
            case NUMERIC -> getNumericCellValue(cell);
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> handleFormulaCell(cell);
            default -> "";
        };
    }
    
    private String getStringCellValue(Cell cell) {
        return cell.getStringCellValue();
    }

    private String getNumericCellValue(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toString();
        } else {
            return String.valueOf((int) cell.getNumericCellValue());
        }
    }

    private String handleFormulaCell(Cell cell) {
        log.warn("Found a cell with a formula!");
        return cell.getCellFormula();
    }


    private boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (isCellNonEmpty(cell)) {
                return false;
            }
        }
        return true;
    }


    private boolean isCellNonEmpty(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK;
    }

}
