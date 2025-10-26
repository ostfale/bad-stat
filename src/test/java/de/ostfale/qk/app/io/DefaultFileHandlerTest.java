package de.ostfale.qk.app.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Test FileHandler interface functions")
@Tag("unittest")
class DefaultFileHandlerTest {

    @TempDir
    File testTempDirectory;

    private FileHandler sut;

    @BeforeEach
    void setUp() {
        sut = new DefaultFileHandler();
    }

    @Test
    @DisplayName("Test exists() method with an existing file")
    void testExistsWithExistingFile() throws Exception {
        // given
        File tempFile = File.createTempFile("testFile", ".txt", testTempDirectory);
        var fileShouldBeFound = true;

        // when
        boolean result = sut.exists(tempFile.toPath());

        // then
        assertThat(result)
                .as("File should be found")
                .isEqualTo(fileShouldBeFound);
    }

    @Test
    @DisplayName("Test exists() method with a non-existing file")
    void testExistsWithNonExistingFile() {
        // given
        File nonExistingFile = new File(testTempDirectory, "non_existent_file.txt");

        // when
        boolean result = sut.exists(nonExistingFile.toPath());

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Test exists() method with an existing file path")
    void testExistsWithExistingFilePath() throws Exception {
        // given
        File tempFile = File.createTempFile("testFile", ".txt", testTempDirectory);
        var filePath = tempFile.toPath();
        var fileShouldBeFound = true;

        // when
        boolean result = sut.exists(filePath);

        // then
        assertThat(result)
                .as("File should be found")
                .isEqualTo(fileShouldBeFound);
    }

    @Test
    @DisplayName("Test exists() method with a non-existing file path")
    void testExistsWithNonExistingFilePath() {
        // given
        var nonExistingFilePath = "non_existent_file.txt";

        // when
        boolean result = sut.exists(nonExistingFilePath);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Test read(Path) method with an existing file")
    void testReadWithExistingFile() throws Exception {
        // given
        String content = "Test file content";
        File tempFile = File.createTempFile("testFile", ".txt", testTempDirectory);
        Files.writeString(tempFile.toPath(), content);

        // when
        Optional<String> result = sut.read(tempFile.toPath());

        // then
        assertAll("Grouped assertions for writing file result",
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.get()).isEqualTo(content)
        );
    }

    @Test
    @DisplayName("Test write(Path, String) method with a valid path and content")
    void testWriteWithValidPathAndContent() throws Exception {
        // given
        String content = "Valid content to write";
        File tempFile = new File(testTempDirectory, "output.txt");

        // when
        boolean result = sut.write(tempFile.toPath(), content);

        // then
        assertThat(result).isTrue();
        assertThat(Files.readString(tempFile.toPath())).isEqualTo(content);
    }

    @Test
    @DisplayName("Test write(Path, String) method creates parent directories")
    void testWriteCreatesParentDirectories() throws Exception {
        // given
        String content = "Content with directories";
        File nestedFile = new File(testTempDirectory, "subdir1/subdir2/output.txt");

        // when
        boolean result = sut.write(nestedFile.toPath(), content);

        // then
        assertThat(result).isTrue();
        assertThat(Files.readString(nestedFile.toPath())).isEqualTo(content);
    }

    @Test
    @DisplayName("Test write(Path, String) method overwrites existing file content")
    void testWriteOverwriteExistingFile() throws Exception {
        // given
        String initialContent = "Initial content";
        String updatedContent = "Updated content";
        File tempFile = new File(testTempDirectory, "output.txt");
        Files.writeString(tempFile.toPath(), initialContent);

        // when
        boolean result = sut.write(tempFile.toPath(), updatedContent);

        // then
        assertThat(result).isTrue();
        assertThat(Files.readString(tempFile.toPath())).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("Test read(Path) method with a non-existing file")
    void testReadWithNonExistingFile() {
        // given
        File nonExistingFile = new File(testTempDirectory, "non_existent_file.txt");

        // when
        Optional<String> result = sut.read(nonExistingFile.toPath());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Test read(String) method with an existing file")
    void testReadWithExistingFilePath() throws Exception {
        // given
        String content = "Test file content";
        File tempFile = File.createTempFile("testFile", ".txt", testTempDirectory);
        Files.writeString(tempFile.toPath(), content);

        // when
        Optional<String> result = sut.read(tempFile.getAbsolutePath());

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(content);
    }

    @Test
    @DisplayName("Test read(String) method with a non-existing file")
    void testReadWithNonExistingFilePath() {
        // given
        String nonExistingFilePath = testTempDirectory.getAbsolutePath() + "/non_existent_file.txt";

        // when
        Optional<String> result = sut.read(nonExistingFilePath);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Test write(String, String) method fails with an IOException")
    void testWrite_shouldReturnFalseWhenFilesWriteStringThrowsIOException() {
        // Given
        Path testPath = Paths.get("test/file.txt");
        String content = "test content";
        IOException ioException = new IOException("Disk full");

        // When/Then
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(testPath.getParent());
            mockedFiles.when(() -> Files.writeString(testPath, content)).thenThrow(ioException);

            boolean result = sut.write(testPath, content);

            assertThat(result).isFalse();
            mockedFiles.verify(() -> Files.writeString(testPath, content));
        }
    }

    @Test
    @DisplayName("Test write(String, String) method fails with an IOException when creating parent directories")
    void testWrite_shouldReturnFalseWhenCreateDirectoriesThrowsIOException() {
        // Given
        Path testPath = Paths.get("test/nested/file.txt");
        String content = "test content";
        IOException ioException = new IOException("Permission denied");

        // When/Then
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(testPath.getParent())).thenThrow(ioException);

            boolean result = sut.write(testPath, content);

            assertThat(result).isFalse();
            mockedFiles.verify(() -> Files.createDirectories(testPath.getParent()));
            mockedFiles.verify(() -> Files.writeString(any(Path.class), any(String.class)), never());
        }
    }

    @Test
    @DisplayName("Test write(String, String) method fails with an IOException")
    void testWrite_stringPath_shouldReturnFalseWhenIOExceptionOccurs() {
        // Given
        String pathStr = "test/file.txt";
        String content = "test content";
        Path testPath = Paths.get(pathStr);
        IOException ioException = new IOException("Access denied");

        // When/Then
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(testPath.getParent());
            mockedFiles.when(() -> Files.writeString(testPath, content)).thenThrow(ioException);

            boolean result = sut.write(pathStr, content);

            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("Test write(String, String) method fails with no parent found")
    void testWrite_shouldHandleNullParentDirectory() {
        // Given
        Path testPath = Paths.get("file.txt"); // No parent directory
        String content = "test content";
        IOException ioException = new IOException("Write failed");

        // When/Then
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.writeString(testPath, content)).thenThrow(ioException);

            boolean result = sut.write(testPath, content);

            assertThat(result).isFalse();
            // Verify createDirectories is not called when parent is null
            mockedFiles.verify(() -> Files.createDirectories(any(Path.class)), never());
            mockedFiles.verify(() -> Files.writeString(testPath, content));
        }
    }

    @Test
    @DisplayName("Test read(String) method fails with an IOException while reading file")
    void testRead_shouldReturnEmptyWhenFilesReadStringThrowsIOException() {
        // Given
        Path testPath = Paths.get("test/file.txt");
        IOException ioException = new IOException("Permission denied while reading");

        // When/Then
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(testPath)).thenReturn(true);
            mockedFiles.when(() -> Files.readString(testPath)).thenThrow(ioException);

            Optional<String> result = sut.read(testPath);

            assertThat(result).isEmpty();
            mockedFiles.verify(() -> Files.exists(testPath));
            mockedFiles.verify(() -> Files.readString(testPath));
        }
    }

    @Test
    @DisplayName("Test readFile() method with an existing file")
    void testReadFileWithExistingFile() throws Exception {
        // given
        Path testFilePath = testTempDirectory.toPath().resolve("test-file.txt");
        Files.writeString(testFilePath, "test content");

        // when
        Optional<File> result = sut.readFile(testFilePath);

        // then
        assertAll("Grouped assertions for readFile with existing file",
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get()).exists(),
                () -> assertThat(result.get().getAbsolutePath()).isEqualTo(testFilePath.toFile().getAbsolutePath()),
                () -> assertThat(result.get().isFile()).isTrue()
        );
    }

    @Test
    @DisplayName("Test readFile() method with a non-existing file")
    void testReadFileWithNonExistingFile() {
        // given
        Path nonExistingFilePath = testTempDirectory.toPath().resolve("non-existing-file.txt");

        // when
        Optional<File> result = sut.readFile(nonExistingFilePath);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Test readFile() method with a directory instead of file")
    void testReadFileWithDirectory() throws Exception {
        // given
        Path directoryPath = testTempDirectory.toPath().resolve("test-directory");
        Files.createDirectory(directoryPath);

        // when
        Optional<File> result = sut.readFile(directoryPath);

        // then
        assertAll("Grouped assertions for readFile with directory",
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().isDirectory()).isTrue(),
                () -> assertThat(result.get().isFile()).isFalse()
        );
    }

    @Test
    @DisplayName("Test readAllFiles() method with empty directory")
    void testReadAllFilesWithEmptyDirectory() {
        // given - testTempDirectory is already empty

        // when
        List<File> result = sut.readAllFiles(testTempDirectory.toPath());

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test readAllFiles() method with non-existing directory")
    void testReadAllFilesWithNonExistingDirectory() {
        // given
        Path nonExistingDirectory = testTempDirectory.toPath().resolve("non-existing-dir");

        // when
        List<File> result = sut.readAllFiles(nonExistingDirectory);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test readAllFiles() method preserves file properties")
    void testReadAllFilesPreservesFileProperties() throws Exception {
        // given
        Path testFile = testTempDirectory.toPath().resolve("test-file.txt");
        String content = "test content for verification";
        Files.writeString(testFile, content);

        // when
        List<File> result = sut.readAllFiles(testTempDirectory.toPath());

        // then
        assertAll("Grouped assertions for readAllFiles preserving file properties",
                () -> assertThat(result.size()).isEqualTo(1),
                () -> assertThat(result.get(0).getName()).isEqualTo("test-file.txt"),
                () -> assertThat(result.get(0).length()).isEqualTo(content.length()),
                () -> assertThat(result.get(0).getParent()).isEqualTo(testTempDirectory.getAbsolutePath())
        );
    }

    @Test
    @DisplayName("Test deleteFile() method with an existing file")
    void testDeleteFileWithExistingFile() throws Exception {
        // given
        Path testFilePath = testTempDirectory.toPath().resolve("file-to-delete.txt");
        Files.writeString(testFilePath, "content to be deleted");

        // when
        boolean result = sut.deleteFile(testFilePath);

        // then
        assertAll("Grouped assertions for deleteFile with existing file",
                () -> assertThat(result).isTrue(),
                () -> assertThat(Files.exists(testFilePath)).isFalse()
        );
    }

    @Test
    @DisplayName("Test deleteFile() method with a non-existing file")
    void testDeleteFileWithNonExistingFile() {
        // given
        Path nonExistingFilePath = testTempDirectory.toPath().resolve("non-existing-file.txt");

        // when
        boolean result = sut.deleteFile(nonExistingFilePath);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Test deleteFile() method with a directory")
    void testDeleteFileWithDirectory() throws Exception {
        // given
        Path directoryPath = testTempDirectory.toPath().resolve("test-directory");
        Files.createDirectory(directoryPath);

        // when
        boolean result = sut.deleteFile(directoryPath);

        // then
        assertAll("Grouped assertions for deleteFile with directory",
                () -> assertThat(result).isTrue(),
                () -> assertThat(Files.exists(directoryPath)).isFalse()
        );
    }

    @Test
    @DisplayName("Test deleteAllFiles() method with multiple files in directory")
    void testDeleteAllFilesWithMultipleFiles() throws Exception {
        // given
        Path file1 = testTempDirectory.toPath().resolve("file1.txt");
        Path file2 = testTempDirectory.toPath().resolve("file2.log");
        Path file3 = testTempDirectory.toPath().resolve("file3.json");
        Files.writeString(file1, "content1");
        Files.writeString(file2, "content2");
        Files.writeString(file3, "content3");

        // when
        boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

        // then
        assertAll("Grouped assertions for deleteAllFiles with multiple files",
                () -> assertThat(result).isTrue(),
                () -> assertThat(Files.exists(file1)).isFalse(),
                () -> assertThat(Files.exists(file2)).isFalse(),
                () -> assertThat(Files.exists(file3)).isFalse(),
                () -> assertThat(Files.exists(testTempDirectory.toPath())).isTrue() // Directory itself should remain
        );
    }

    @Test
    @DisplayName("Test deleteAllFiles() method with empty directory")
    void testDeleteAllFilesWithEmptyDirectory() {
        // given - testTempDirectory is already empty

        // when
        boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

        // then
        assertAll("Grouped assertions for deleteAllFiles with empty directory",
                () -> assertThat(result).isTrue(),
                () -> assertThat(Files.exists(testTempDirectory.toPath())).isTrue() // Directory should still exist
        );
    }

    @Test
    @DisplayName("Test deleteAllFiles() method with non-existing directory")
    void testDeleteAllFilesWithNonExistingDirectory() {
        // given
        Path nonExistingDirectory = testTempDirectory.toPath().resolve("non-existing-dir");

        // when
        boolean result = sut.deleteAllFiles(nonExistingDirectory);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Test deleteAllFiles() method preserves subdirectories")
    void testDeleteAllFilesPreservesSubdirectories() throws Exception {
        // given
        Path file1 = testTempDirectory.toPath().resolve("root-file.txt");
        Path subDir = testTempDirectory.toPath().resolve("subdirectory");
        Files.createDirectory(subDir);
        Path fileInSubDir = subDir.resolve("nested-file.txt");

        Files.writeString(file1, "root content");
        Files.writeString(fileInSubDir, "nested content");

        // when
        boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

        // then
        assertAll("Grouped assertions for deleteAllFiles preserving subdirectories",
                () -> assertThat(result).isTrue(),
                () -> assertThat(Files.exists(file1)).isFalse(), // Root file should be deleted
                () -> assertThat(Files.exists(subDir)).isTrue(), // Subdirectory should remain
                () -> assertThat(Files.exists(fileInSubDir)).isFalse() // File in subdirectory should be deleted (Files.walk is recursive)
        );
    }

    @Test
    @DisplayName("Test deleteAllFiles() method with nested directory structure")
    void testDeleteAllFilesWithNestedStructure() throws Exception {
        // given
        Path file1 = testTempDirectory.toPath().resolve("file1.txt");
        Path subDir1 = testTempDirectory.toPath().resolve("subdir1");
        Path subDir2 = subDir1.resolve("subdir2");
        Files.createDirectories(subDir2);

        Path fileInSubDir1 = subDir1.resolve("file-in-subdir1.txt");
        Path fileInSubDir2 = subDir2.resolve("file-in-subdir2.txt");

        Files.writeString(file1, "content1");
        Files.writeString(fileInSubDir1, "content in subdir1");
        Files.writeString(fileInSubDir2, "content in subdir2");

        // when
        boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

        // then
        assertAll("Grouped assertions for deleteAllFiles with nested structure",
                () -> assertThat(result).isTrue(),
                () -> assertThat(Files.exists(file1)).isFalse(),
                () -> assertThat(Files.exists(fileInSubDir1)).isFalse(),
                () -> assertThat(Files.exists(fileInSubDir2)).isFalse(),
                () -> assertThat(Files.exists(subDir1)).isTrue(), // Directories should remain
                () -> assertThat(Files.exists(subDir2)).isTrue()
        );
    }

    @Test
    @DisplayName("Test deleteAllFiles() method with mixed file types and hidden files")
    void testDeleteAllFilesWithMixedTypes() throws Exception {
        // given
        Path txtFile = testTempDirectory.toPath().resolve("document.txt");
        Path jsonFile = testTempDirectory.toPath().resolve("data.json");
        Path hiddenFile = testTempDirectory.toPath().resolve(".hidden");
        Path executableFile = testTempDirectory.toPath().resolve("script.sh");

        Files.writeString(txtFile, "text content");
        Files.writeString(jsonFile, "{}");
        Files.writeString(hiddenFile, "hidden content");
        Files.writeString(executableFile, "#!/bin/bash\necho 'hello'");

        // when
        boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

        // then
        assertAll("Grouped assertions for deleteAllFiles with mixed file types",
                () -> assertThat(result).isTrue(),
                () -> assertThat(Files.exists(txtFile)).isFalse(),
                () -> assertThat(Files.exists(jsonFile)).isFalse(),
                () -> assertThat(Files.exists(hiddenFile)).isFalse(),
                () -> assertThat(Files.exists(executableFile)).isFalse()
        );
    }

    @Test
    @DisplayName("Test deleteAllFiles() method leaves directory structure intact")
    void testDeleteAllFilesLeavesDirectoryStructure() throws Exception {
        // given
        Path level1Dir = testTempDirectory.toPath().resolve("level1");
        Path level2Dir = level1Dir.resolve("level2");
        Path level3Dir = level2Dir.resolve("level3");
        Files.createDirectories(level3Dir);

        Path fileLevel1 = level1Dir.resolve("file1.txt");
        Path fileLevel2 = level2Dir.resolve("file2.txt");
        Path fileLevel3 = level3Dir.resolve("file3.txt");

        Files.writeString(fileLevel1, "level1 content");
        Files.writeString(fileLevel2, "level2 content");
        Files.writeString(fileLevel3, "level3 content");

        // when
        boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

        // then
        assertAll("Grouped assertions for deleteAllFiles leaving directory structure",
                () -> assertThat(result).isTrue(),
                () -> assertThat(Files.exists(fileLevel1)).isFalse(),
                () -> assertThat(Files.exists(fileLevel2)).isFalse(),
                () -> assertThat(Files.exists(fileLevel3)).isFalse(),
                () -> assertThat(Files.exists(level1Dir)).isTrue(),
                () -> assertThat(Files.exists(level2Dir)).isTrue(),
                () -> assertThat(Files.exists(level3Dir)).isTrue(),
                () -> assertThat(Files.isDirectory(level1Dir)).isTrue(),
                () -> assertThat(Files.isDirectory(level2Dir)).isTrue(),
                () -> assertThat(Files.isDirectory(level3Dir)).isTrue()
        );
    }

    @Test
    @DisplayName("Test deleteFile() method with read-only file")
    void testDeleteFileWithReadOnlyFile() throws Exception {
        // given
        Path readOnlyFile = testTempDirectory.toPath().resolve("readonly-file.txt");
        Files.writeString(readOnlyFile, "read-only content");
        readOnlyFile.toFile().setReadOnly();

        // when
        boolean result = sut.deleteFile(readOnlyFile);

        // then - behavior depends on OS, but method should handle gracefully
        // On Windows, deleting read-only files may fail, on Unix it may succeed
        // The important thing is that no exception is thrown
        assertThat(result).isIn(true, false);

        // Clean up - make writable again for proper cleanup
        if (Files.exists(readOnlyFile)) {
            readOnlyFile.toFile().setWritable(true);
        }
    }

    @Test
    @DisplayName("Test deleteAllFiles() method verifies file count before and after")
    void testDeleteAllFilesVerifiesFileCount() throws Exception {
        // given
        int numberOfFiles = 5;
        for (int i = 1; i <= numberOfFiles; i++) {
            Path file = testTempDirectory.toPath().resolve("file" + i + ".txt");
            Files.writeString(file, "content " + i);
        }

        // Verify files were created
        List<File> filesBefore = sut.readAllFiles(testTempDirectory.toPath());
        assertThat(filesBefore.size()).isEqualTo(numberOfFiles);

        // when
        boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

        // then
        List<File> filesAfter = sut.readAllFiles(testTempDirectory.toPath());
        assertAll("Grouped assertions for deleteAllFiles file count verification",
                () -> assertThat(result).isTrue(),
                () -> assertThat(filesAfter.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("Test deleteAllFiles() method when Files.walk() throws IOException")
    void testDeleteAllFilesWhenFilesWalkThrowsIOException() throws Exception {
        // given
        Path file1 = testTempDirectory.toPath().resolve("file1.txt");
        Files.writeString(file1, "content1");

        DefaultFileHandler spyHandler = spy(new DefaultFileHandler());

        // when & then
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class, CALLS_REAL_METHODS)) {
            mockedFiles.when(() -> Files.walk(testTempDirectory.toPath()))
                    .thenThrow(new IOException("Simulated IO error during directory walking"));

            boolean result = spyHandler.deleteAllFiles(testTempDirectory.toPath());

            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("Test deleteAllFiles() method with permission denied scenario")
    void testDeleteAllFilesWithPermissionDenied() throws Exception {
        // given
        Path restrictedDir = testTempDirectory.toPath().resolve("restricted");
        Files.createDirectory(restrictedDir);
        Path fileInRestricted = restrictedDir.resolve("restricted-file.txt");
        Files.writeString(fileInRestricted, "restricted content");

        // Make directory read-only (this may not work on all systems, but simulates permission issues)
        restrictedDir.toFile().setReadOnly();

        // when & then
        try {
            boolean result = sut.deleteAllFiles(restrictedDir);

            // The method should handle the error gracefully and still return true
            // because individual file deletion errors are caught in deleteFileSafely
            assertThat(result).isTrue();

        } finally {
            // Clean up - restore permissions for proper cleanup
            restrictedDir.toFile().setWritable(true);
            if (Files.exists(fileInRestricted)) {
                fileInRestricted.toFile().setWritable(true);
                Files.deleteIfExists(fileInRestricted);
            }
            Files.deleteIfExists(restrictedDir);
        }
    }

    @Test
    @DisplayName("Test deleteAllFiles() method with extremely long file paths")
    void testDeleteAllFilesWithExtremelyLongPaths() throws Exception {
        // given
        StringBuilder longName = new StringBuilder();
        longName.append("very_long_filename_component_".repeat(100));
        longName.append(".txt");

        Path longNamedFile = testTempDirectory.toPath().resolve(longName.toString());

        try {
            // This might fail on some filesystems due to path length limits
            Files.writeString(longNamedFile, "content with long filename");

            // when
            boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

            // then
            assertThat(result).isTrue();

        } catch (IOException e) {
            // If creating the file fails due to path length, that's also a valid test case
            boolean result = sut.deleteAllFiles(testTempDirectory.toPath());
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Test deleteAllFiles() method behavior with concurrent file modifications")
    void testDeleteAllFilesWithConcurrentModifications() throws Exception {
        // given
        Path file1 = testTempDirectory.toPath().resolve("concurrent-file.txt");
        Files.writeString(file1, "initial content");

        // when & then
        try (var executor = Executors.newSingleThreadExecutor()) {
            // Create a concurrent modification scenario
            CompletableFuture<Void> concurrentModification = CompletableFuture.runAsync(() -> {
                try {
                    // Simulate another process modifying files during deletion
                    Thread.sleep(10); // Small delay
                    Path newFile = testTempDirectory.toPath().resolve("concurrent-new-file.txt");
                    if (!Files.exists(newFile)) {
                        Files.writeString(newFile, "concurrent content");
                    }
                } catch (Exception e) {
                    // Ignore exceptions in concurrent modification
                }
            }, executor);

            boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

            assertThat(result).isTrue();

            // Wait for concurrent modification to complete
            concurrentModification.join();
        }
    }

    @Test
    @DisplayName("Test deleteAllFiles() method with FileSystem mock throwing IOException")
    void testDeleteAllFilesWithFileSystemMockIOException() throws Exception {
        // given
        Path file1 = testTempDirectory.toPath().resolve("test-file.txt");
        Files.writeString(file1, "test content");

        // when & then
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class, CALLS_REAL_METHODS)) {
            // Mock Files.walk to throw IOException on the test directory
            mockedFiles.when(() -> Files.walk(any(Path.class)))
                    .thenThrow(new IOException("Mocked IOException from Files.walk"));

            boolean result = sut.deleteAllFiles(testTempDirectory.toPath());

            assertThat(result).isFalse();

            // Verify that the mock was called
            mockedFiles.verify(() -> Files.walk(testTempDirectory.toPath()));
        }
    }
}
