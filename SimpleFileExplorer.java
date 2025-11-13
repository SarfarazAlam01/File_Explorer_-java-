// SimpleFileExplorer.java
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Stream;

public class SimpleFileExplorer {
    private static Path currentDir = Paths.get("").toAbsolutePath();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = 0;
        do {
            displayMenu();
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1 -> listFiles();
                case 2 -> createDirectory();
                case 3 -> deleteFile();
                case 4 -> moveFile();
                case 5 -> openFolder();
                case 6 -> copyFolder();
                case 7 -> goToParentDirectory();
                case 8 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 8);
    }

    private static void displayMenu() {
        System.out.println("\n=== Simple File Explorer ===");
        System.out.println("Current directory: " + currentDir.toString());
        System.out.println();
        System.out.println("1. List files in current directory");
        System.out.println("2. Create a new directory");
        System.out.println("3. Delete a file");
        System.out.println("4. Move file");
        System.out.println("5. Open folder");
        System.out.println("6. Copy folder");
        System.out.println("7. Go back to parent directory");
        System.out.println("8. Exit");
        System.out.print("Enter your choice (1-8): ");
    }

    private static void listFiles() {
        System.out.println("\nFiles in current directory:");
        System.out.printf("%-40s %-10s %-12s %s%n", "Name", "Type", "Size(B)", "Last Modified");
        System.out.println("--------------------------------------------------------------------------------");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(currentDir)) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
            for (Path p : ds) {
                String name = p.getFileName().toString();
                String type = Files.isDirectory(p) ? "Dir" : "File";
                long size = 0L;
                try {
                    if (!Files.isDirectory(p)) size = Files.size(p);
                } catch (IOException ignored) {}
                String lm = "";
                try {
                    lm = fmt.format(Instant.ofEpochMilli(Files.getLastModifiedTime(p).toMillis()));
                } catch (IOException ignored) {}
                System.out.printf("%-40s %-10s %-12d %s%n", name, type, size, lm);
            }
        } catch (IOException e) {
            System.out.println("Error listing files: " + e.getMessage());
        }
    }

    private static void createDirectory() {
        System.out.print("Enter directory name: ");
        String dirName = scanner.nextLine().trim();
        if (dirName.isEmpty()) {
            System.out.println("Directory name cannot be empty.");
            return;
        }
        Path newDir = currentDir.resolve(dirName);
        try {
            Files.createDirectories(newDir);
            System.out.println("Directory created successfully: " + newDir);
        } catch (IOException e) {
            System.out.println("Failed to create directory: " + e.getMessage());
        }
    }

    private static void deleteFile() {
        System.out.print("Enter file name to delete: ");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        Path target = currentDir.resolve(fileName);
        try {
            if (!Files.exists(target)) {
                System.out.println("File/folder does not exist.");
                return;
            }
            if (Files.isDirectory(target)) {
                System.out.print("Target is a directory. Delete recursively? (y/n): ");
                String resp = scanner.nextLine().trim();
                if (!resp.equalsIgnoreCase("y")) {
                    System.out.println("Delete cancelled.");
                    return;
                }
                // recursive delete
                Files.walkFileTree(target, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                System.out.println("Directory deleted successfully.");
            } else {
                boolean removed = Files.deleteIfExists(target);
                if (removed) System.out.println("File deleted successfully!");
                else System.out.println("Failed to delete file.");
            }
        } catch (IOException e) {
            System.out.println("Error deleting: " + e.getMessage());
        }
    }

    private static void displayFileInfo() {
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        Path target = currentDir.resolve(fileName);
        try {
            if (!Files.exists(target)) {
                System.out.println("File does not exist.");
                return;
            }
            System.out.println("\nFile Information:");
            System.out.println("Name: " + target.getFileName());
            System.out.println("Path: " + target.toAbsolutePath());
            System.out.println("Type: " + (Files.isDirectory(target) ? "Directory" : "File"));
            try {
                System.out.println("Size: " + (Files.isDirectory(target) ? 0 : Files.size(target)) + " bytes");
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
                String lm = fmt.format(Instant.ofEpochMilli(Files.getLastModifiedTime(target).toMillis()));
                System.out.println("Last modified: " + lm);
            } catch (IOException ignored) {}
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void moveFile() {
        System.out.print("Enter source file name (relative or absolute): ");
        String source = scanner.nextLine().trim();
        System.out.print("Enter destination path (directory): ");
        String dest = scanner.nextLine().trim();
        if (source.isEmpty() || dest.isEmpty()) {
            System.out.println("Source and destination must be provided.");
            return;
        }
        Path sourcePath = currentDir.resolve(source).normalize();
        Path destDir = Paths.get(dest).isAbsolute() ? Paths.get(dest) : currentDir.resolve(dest).normalize();

        try {
            if (!Files.exists(sourcePath)) {
                System.out.println("Source does not exist.");
                return;
            }
            if (!Files.exists(destDir)) {
                Files.createDirectories(destDir);
            }
            Path destFilePath = destDir.resolve(sourcePath.getFileName());
            if (Files.exists(destFilePath)) {
                System.out.print("A file with the same name exists. Overwrite? (y/n): ");
                String resp = scanner.nextLine().trim();
                if (!resp.equalsIgnoreCase("y")) {
                    System.out.println("Move cancelled.");
                    return;
                }
                Files.delete(destFilePath);
            }
            Files.move(sourcePath, destFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully!");
        } catch (IOException e) {
            System.out.println("Error moving file: " + e.getMessage());
        }
    }

    private static void openFolder() {
        System.out.print("Enter folder name to open (relative or absolute): ");
        String folderName = scanner.nextLine().trim();
        if (folderName.isEmpty()) {
            System.out.println("Folder name cannot be empty.");
            return;
        }
        Path folderPath = Paths.get(folderName).isAbsolute() ? Paths.get(folderName) : currentDir.resolve(folderName);
        if (!Files.exists(folderPath)) {
            System.out.println("Folder does not exist.");
            return;
        }
        if (!Files.isDirectory(folderPath)) {
            System.out.println("'" + folderName + "' is not a directory.");
            return;
        }
        currentDir = folderPath.toAbsolutePath().normalize();
        System.out.println("Navigated to: " + currentDir);
    }

    private static void goToParentDirectory() {
        Path parent = currentDir.getParent();
        if (parent == null) {
            System.out.println("Already at root directory.");
            return;
        }
        currentDir = parent;
        System.out.println("Navigated to: " + currentDir);
    }

    private static void copyFolder() {
        System.out.print("Enter source folder name (relative or absolute): ");
        String source = scanner.nextLine().trim();
        System.out.print("Enter destination path (directory): ");
        String dest = scanner.nextLine().trim();
        if (source.isEmpty() || dest.isEmpty()) {
            System.out.println("Source and destination must be provided.");
            return;
        }

        Path sourcePath = Paths.get(source).isAbsolute() ? Paths.get(source) : currentDir.resolve(source).normalize();
        Path destDir = Paths.get(dest).isAbsolute() ? Paths.get(dest) : currentDir.resolve(dest).normalize();

        try {
            if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
                System.out.println("Source folder does not exist or is not a directory.");
                return;
            }
            if (!Files.exists(destDir)) {
                Files.createDirectories(destDir);
            }
            Path destFolderPath = destDir.resolve(sourcePath.getFileName());
            if (Files.exists(destFolderPath)) {
                System.out.print("A folder with the same name exists in destination. Overwrite? (y/n): ");
                String resp = scanner.nextLine().trim();
                if (!resp.equalsIgnoreCase("y")) {
                    System.out.println("Copy cancelled.");
                    return;
                }
                // delete target folder recursively
                Files.walkFileTree(destFolderPath, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            // copy recursively
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = destFolderPath.resolve(sourcePath.relativize(dir));
                    if (!Files.exists(targetDir)) {
                        Files.createDirectories(targetDir);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = destFolderPath.resolve(sourcePath.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    System.out.println("Copied: " + sourcePath.relativize(file));
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("Folder copied successfully!");
        } catch (IOException e) {
            System.out.println("Error copying folder: " + e.getMessage());
        }
    }
}
