package de.ostfale.qk.parser.file.csv;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.points.PointsTourType;
import de.ostfale.qk.domain.points.RankingPoint;
import de.ostfale.qk.domain.points.TourPointsViewModel;
import de.ostfale.qk.domain.points.TourTypePointsList;
import de.ostfale.qk.parser.file.FileParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@ApplicationScoped
public class RankingPointsParser implements FileParser {

    private static final String U09_PATH = "/data/u09points.csv";
    private static final String U11_PATH = "/data/u11points.csv";
    private static final String U13_PATH = "/data/u13points.csv";
    private static final String U15_PATH = "/data/u15points.csv";
    private static final String U17_PATH = "/data/u17points.csv";
    private static final String U19_PATH = "/data/u19points.csv";
    private static final String HEADER_START_MARKER = "Platz";
    private List<String> RankingPointsList = List.of(U09_PATH, U11_PATH, U13_PATH, U15_PATH, U17_PATH, U19_PATH);

    private TourPointsViewModel tourPointsViewModel;

    public TourPointsViewModel getTourPointsViewModel() throws URISyntaxException {
        if (tourPointsViewModel == null) {
            TourPointsViewModel model = new TourPointsViewModel();
            populateModelWithParsedPoints(model);
            tourPointsViewModel = model;
        }
        return tourPointsViewModel;
    }

    public List<String> getRankingPointsList() {
        return RankingPointsList;
    }

    public void setRankingPointsList(List<String> rankingPointsList) {
        this.RankingPointsList = rankingPointsList;
    }

    private void populateModelWithParsedPoints(TourPointsViewModel model) throws URISyntaxException {
        for (String pointsFilePath : RankingPointsList) {
            model.addTourTypePointsList(parseRankingPointsFile(pointsFilePath));
        }
    }

    private List<TourTypePointsList> parseRankingPointsFile(String filePath) throws URISyntaxException {
        Log.debugf("RankingPointsParser :: Parsing file: %s", filePath);
        File csvFile = readFile(filePath);
        String fileName = csvFile.getName();
        AgeClass ageClass = AgeClass.fromString(fileName);
        List<TourTypePointsList> ageClassTourPointsList = new ArrayList<>();

        try (Scanner scanner = new Scanner(csvFile)) {
            while (scanner.hasNext()) {
                String currentLine = scanner.nextLine().trim();
                if (currentLine.startsWith(HEADER_START_MARKER)) {
                    readHeader(currentLine, ageClass, ageClassTourPointsList);
                } else {
                    Log.tracef("RankingPointsParser :: Read Points line: %s", currentLine);
                    readPoints(currentLine, ageClassTourPointsList);
                }
            }
        } catch (FileNotFoundException e) {
            Log.errorf("RankingPointsParser :: File not found: %s", filePath);
        }

        return ageClassTourPointsList;
    }

    private void readHeader(String line, AgeClass ageClass, List<TourTypePointsList> ageClassTourPointsList) {
        Log.debugf("RankingPointsParser :: Read Header line: %s", line);
        String[] headerElements = line.split(",");
        Arrays.stream(headerElements)
                .filter(headerElement -> !headerElement.equalsIgnoreCase(HEADER_START_MARKER))
                .forEach(headerElement -> {
                    Log.debugf("RankingPointsParser :: process header element: %s", headerElement);
                    var pointsTourType = PointsTourType.fromDisplayName(headerElement.trim());
                    pointsTourType.ifPresent(tourType -> {
                        ageClassTourPointsList.add(new TourTypePointsList(ageClass, tourType));
                    });
                });
    }

    private void readPoints(String line, List<TourTypePointsList> tourTypePointsList) {
        Log.tracef("RankingPointsParser :: Read Points line: %s", line);
        String[] pointsElements = line.split(",");
        for (int i = 1; i < pointsElements.length; i++) {
            var currentPoint = pointsElements[i].trim();
            var listElement = tourTypePointsList.get(i - 1).rankingPointList();
            listElement.add(new RankingPoint(currentPoint));
        }
    }
}
