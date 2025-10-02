package de.ostfale.qk.parser.file.csv;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.points.PointsTourType;
import de.ostfale.qk.domain.points.RankingPoint;
import de.ostfale.qk.domain.points.TourPointsViewModel;
import de.ostfale.qk.domain.points.TourTypePointsList;
import de.ostfale.qk.parser.file.FileParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@ApplicationScoped
public class RankingPointsParser implements FileParser {

    private static final String HEADER_START_MARKER = "Platz";
    private TourPointsViewModel tourPointsViewModel;

    private List<String> rankingPointsPaths = new ArrayList<>();

    public TourPointsViewModel getTourPointsViewModel() throws URISyntaxException {
        if (tourPointsViewModel == null) {
            TourPointsViewModel model = new TourPointsViewModel();
            if (rankingPointsPaths.isEmpty()) {
                rankingPointsPaths = List.of(
                        "data/u09points.csv",
                        "data/u11points.csv",
                        "data/u13points.csv",
                        "data/u15points.csv",
                        "data/u17points.csv",
                        "data/u19points.csv"
                );
            }
            populateModelWithParsedPoints(model, rankingPointsPaths);
            tourPointsViewModel = model;
        }
        return tourPointsViewModel;
    }

    public List<String> getRankingPointsList() {
        return rankingPointsPaths;
    }

    public void setRankingPointsList(List<String> rankingPointsPathList) {
        this.rankingPointsPaths = rankingPointsPathList;
    }

    private void populateModelWithParsedPoints(TourPointsViewModel model, List<String> pointsFilePaths) throws URISyntaxException {
        for (String pointsFilePath : pointsFilePaths) {
            model.addTourTypePointsList(parseRankingPointsFile(pointsFilePath));
        }
    }

    private List<TourTypePointsList> parseRankingPointsFile(String filePath) {
        Log.debugf("RankingPointsParser :: Parsing file: %s", filePath);
        InputStream csvFileStream = readFileFromResource(filePath);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        AgeClass ageClass = AgeClass.fromString(fileName);

        Scanner scanner = new Scanner(csvFileStream);
        return parseCsvContent(scanner, ageClass);
    }

    private List<TourTypePointsList> parseCsvContent(Scanner scanner, AgeClass ageClass) {
        List<TourTypePointsList> ageClassTourPointsList = new ArrayList<>();

        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine().trim();
            if (currentLine.startsWith(HEADER_START_MARKER)) {
                readHeader(currentLine, ageClass, ageClassTourPointsList);
            } else {
                Log.tracef("RankingPointsParser :: Read Points line: %s", currentLine);
                readPoints(currentLine, ageClassTourPointsList);
            }
        }
        return ageClassTourPointsList;
    }

    private void readHeader(String line, AgeClass ageClass, List<TourTypePointsList> ageClassTourPointsList) {
        Log.tracef("RankingPointsParser :: Read Header line: %s", line);
        String[] headerElements = line.split(",");
        Arrays.stream(headerElements)
                .filter(headerElement -> !headerElement.equalsIgnoreCase(HEADER_START_MARKER))
                .forEach(headerElement -> {
                    Log.tracef("RankingPointsParser :: process header element: %s", headerElement);
                    var pointsTourType = PointsTourType.fromDisplayName(headerElement.trim());
                    pointsTourType.ifPresent(tourType -> {
                        ageClassTourPointsList.add(new TourTypePointsList(ageClass, tourType));
                    });
                });
    }

    private void readPoints(String line, List<TourTypePointsList> tourTypePointsList) {
        Log.tracef("RankingPointsParser :: Read Points line: %s", line);
        List<String> pointsList = splitLineToPoints(line);
        for (int i = 1; i < pointsList.size(); i++) {
            var listElement = tourTypePointsList.get(i - 1).rankingPointList();

            try {
                listElement.add(new RankingPoint(pointsList.get(i)));
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<String> splitLineToPoints(String line) {
        Log.tracef("RankingPointsParser :: Splitting line: %s", line);
        var splitValues = line.split(",");
        return Arrays.stream(splitValues).map(String::trim).toList();
    }
}
