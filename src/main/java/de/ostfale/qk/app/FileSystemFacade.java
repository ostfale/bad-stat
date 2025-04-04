package de.ostfale.qk.app;

import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface FileSystemFacade extends ApplicationFacade {

    Logger log = Logger.getLogger(FileSystemFacade.class);

    String SEP = FileSystems.getDefault().getSeparator();
    String USER_HOME = "user.home";


    default boolean directoryExists(String dirPath) {
        Path path = Paths.get(dirPath);
        var result = Files.exists(path) && java.nio.file.Files.isDirectory(path);
        log.debugf("Directory %s exists: %s", dirPath, result);
        return result;
    }

    default String getHomeDir() {
        var result =  System.getProperty(USER_HOME);
        log.debugf("User home directory: %s", result);
        return result;
    }

    default String getApplicationHomeDir() {
        var result = getHomeDir() + SEP + APP_DIR_NAME;
        log.debugf("Application home directory: %s", result);
        return result;
    }

    default List<File> readAllFiles(String dirPath) {
        log.debugf("Read all files from directory: {}", dirPath);
        return Stream.ofNullable(new File(dirPath).listFiles())
                .flatMap(Stream::of)
                .filter(File::isFile)
                .collect(Collectors.toList());
    }



    default boolean deleteFile(String filePath) {
        log.debugf("Delete file: {}", filePath);
        var fileToDelete = new File(filePath);
        return fileToDelete.delete();
    }

    static String readFileContentAsText(String filePath) throws IOException {
        log.debugf("Read file content from: {}", filePath);
        return Files.readString(Path.of(filePath));
    }

    default boolean deleteAllFiles(String dirPath) {
        var filesToDelete = readAllFiles(dirPath);
        log.debugf("Delete all files from directory: {} found: {}", dirPath, filesToDelete.size());
        return filesToDelete.stream().allMatch(File::delete);
    }

    default boolean deleteAllFiles(List<File> files) {
        log.debugf("Delete {} files", files.size());
        return files.stream().allMatch(File::delete);
    }

    static String getUserHome() {
        return System.getProperty(USER_HOME);
    }

    static String getApplicationHome() {
        return getUserHome() + SEP + APP_DIR_NAME;
    }

    static void writeToFile(String fileName, String content) throws IOException {
        Files.writeString(Paths.get(fileName), content);
    }


}
