package de.ostfale.qk.parser.pointstable;

import de.ostfale.qk.app.async.SyncDataLoaderService;
import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.pointstable.AgeClassTablePoints;
import de.ostfale.qk.domain.pointstable.TourTypeTablePoints;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CSVPointsTableService {

    private static final List<String> agePointsFileNames = List.of(
            "data/u09points.csv",
            "data/u11points.csv",
            "data/u13points.csv",
            "data/u15points.csv",
            "data/u17points.csv",
            "data/u19points.csv"
    );

    private final SyncDataLoaderService syncDataLoaderService;
    private final CSVPointsTableParser csvPointsTableParser;

    private AgeClassTablePoints ageClassTablePoints;

    public CSVPointsTableService(SyncDataLoaderService syncDataLoaderService, CSVPointsTableParser csvPointsTableParser) {
        this.syncDataLoaderService = syncDataLoaderService;
        this.csvPointsTableParser = csvPointsTableParser;
    }

    public AgeClassTablePoints getAgeClassTablePoints() {
        if (ageClassTablePoints == null || ageClassTablePoints.ageClassTablePointsMap().isEmpty()) {
            Log.info("CSVPointsTableService :: AgeClassTablePoints not initialized yet -> sync read from files");
            this.ageClassTablePoints = readTablePoints(agePointsFileNames);
        }

        return ageClassTablePoints;
    }

    public AgeClassTablePoints readTablePoints(List<String> filePathsList) {
        Map<AgeClass, List<TourTypeTablePoints>> ageClassTablePointsMap = new ConcurrentHashMap<>();

        for (String agePointsPath : filePathsList) {
            var ageClass = getAgeClassFromFileName(agePointsPath);

            // Using readResourceAndParse synchronously
            List<TourTypeTablePoints> points = syncDataLoaderService.readResourceAndParse(agePointsPath, csvPointsTableParser);
            ageClassTablePointsMap.put(ageClass, points);
        }

        return new AgeClassTablePoints(ageClassTablePointsMap);
    }

    private AgeClass getAgeClassFromFileName(String fileName) {
        Log.tracef("CSVPointsTableService :: Get age class from file name %s", fileName);
        return AgeClass.fromString(fileName.substring(fileName.lastIndexOf("/") + 1));
    }
}
