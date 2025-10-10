package de.ostfale.qk.parser.pointstable;


import de.ostfale.qk.domain.pointstable.PointsDisplayTypes;
import de.ostfale.qk.domain.pointstable.TourTypeTablePoints;
import de.ostfale.qk.parser.FileParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@ApplicationScoped
public class CSVPointsTableParser implements FileParser<List<TourTypeTablePoints>> {

    private static final String HEADER_START_MARKER = PointsDisplayTypes.INDEX.getDisplayName();

    @Override
    public List<TourTypeTablePoints> parse(String fileContent) {
        Log.debug("CSVRankingPointParser :: parse ranking points file content");
        try (Scanner scanner = new Scanner(fileContent)) {
            return parseLines(scanner);
        }
    }

    private List<TourTypeTablePoints> parseLines(Scanner scanner) {
        List<TourTypeTablePoints> tourTypePoints = new ArrayList<>();
        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine().trim();
            if (currentLine.startsWith(HEADER_START_MARKER)) {
                readHeader(currentLine, tourTypePoints);
            } else {
                Log.tracef("RankingPointsParser :: Read Points line: %s", currentLine);
                readPoints(currentLine, tourTypePoints);
            }
        }
        return tourTypePoints;
    }

    private void readHeader(String line, List<TourTypeTablePoints> ageClassTourPointsList) {
        Log.tracef("CSVRankingPointParser :: Read Header line: %s", line);
        List<String> headerList = splitLine(line);

        headerList
                .forEach(headerElement -> {
                    Log.tracef("CSVRankingPointParser :: process header element: %s", headerElement);
                    var pointsTourType = PointsDisplayTypes.fromDisplayName(headerElement.trim());
                    pointsTourType.ifPresent(tourType -> {
                        ageClassTourPointsList.add(new TourTypeTablePoints(tourType));
                    });
                });
    }

    private void readPoints(String line, List<TourTypeTablePoints> tourTypePointsList) {
        Log.tracef("CSVRankingPointParser :: Read Points line: %s", line);
        List<String> pointsList = splitLine(line);

        for (int i = 0; i < pointsList.size(); i++) {
            var listElement = tourTypePointsList.get(i);
            listElement.rankingPoints().add(pointsList.get(i));
        }
    }

    private List<String> splitLine(String line) {
        Log.tracef("CSVRankingPointParser :: Splitting line: %s", line);
        var splitValues = line.split(",");
        return Arrays.stream(splitValues).map(String::trim).toList();
    }
}
