package de.ostfale.qk.app;

import io.quarkus.logging.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface FileSystemFacade extends ApplicationFacade {

    String SEP = FileSystems.getDefault().getSeparator();
    String FILE_NAME_SEPARATOR = "_";
    String DATE_SEPARATOR = "-";
    String USER_HOME = "user.home";
    String EMPTY_STRING = "";

    default boolean directoryExists(String dirPath) {
        Path path = Paths.get(dirPath);
        var result = Files.exists(path) && Files.isDirectory(path);
        Log.debugf("Directory %s exists: %s", dirPath, result);
        return result;
    }

    default String getHomeDir() {
        var result = System.getProperty(USER_HOME);
        Log.debugf("User home directory: %s", result);
        return result;
    }

    default String getApplicationHomeDir() {
        var result = getHomeDir() + SEP + APP_DIR_NAME;
        Log.debugf("Application home directory: %s", result);
        return result;
    }

    default String getApplicationSubDir(String subDirName) {
        var result = getApplicationHomeDir() + SEP + subDirName;
        Log.debugf("Application sub directory: %s", result);
        return result;
    }

    default List<File> readAllFiles(String dirPath) {
        Log.debugf("Read all files from directory: {}", dirPath);
        return Stream.ofNullable(new File(dirPath).listFiles()).flatMap(Stream::of).filter(File::isFile).collect(Collectors.toList());
    }

    default void downloadIntoFile(URL url, String fileName) throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }

    default URL getURL(String urlString) throws MalformedURLException {
        URI uri = URI.create(urlString);
        return uri.toURL();
    }

    static String readFileContentAsText(String filePath) throws IOException {
        Log.debugf("Read file content from: {}", filePath);
        return Files.readString(Path.of(filePath));
    }

    default boolean deleteAllFiles(String dirPath) {
        var filesToDelete = readAllFiles(dirPath);
        if (filesToDelete.isEmpty()) {
            Log.debug("No files found to be deleted -> return");
            return true;
        }
        Log.debugf("Delete all files from directory: %s found: %d", dirPath, filesToDelete.size());
        return filesToDelete.stream().allMatch(File::delete);
    }

    default String extractDateFromFileName(String fileName) {
        int startIndex = fileName.lastIndexOf(FILE_NAME_SEPARATOR) + 1;
        int endIndex = fileName.lastIndexOf(".");
        String fileNameWithoutExtension = fileName.substring(startIndex, endIndex);
        String[] dateComponents = fileNameWithoutExtension.split(DATE_SEPARATOR);

        String day = dateComponents[2];
        String month = dateComponents[1];
        String year = dateComponents[0];
        String dateResult = day + "." + month + "." + year;

        Log.debugf("Extract date from file name %s: %s", fileName, dateResult);
        return dateResult;
    }


    static void writeToFile(String fileName, String content) throws IOException {
        Files.writeString(Paths.get(fileName), content);
    }
}
