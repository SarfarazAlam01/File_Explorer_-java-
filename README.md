# Simple File Explorer

A basic file explorer application written in **Java** for Wipro placement preparation. This project demonstrates fundamental programming concepts including input/output operations, file system manipulation, error handling, and control structures.

## Features

- List files in the current directory with details (name, type, size, last modified date)
- Create new directories
- Delete files and directories (with recursive option)
- Move files to different directories
- Navigate into folders (open folder)
- Copy entire folders with all contents recursively
- Navigate back to parent directory
- Display current directory path
- Cross-platform compatible (Windows, macOS, Linux)

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Basic understanding of command line/terminal

## Installation and Setup

### Windows

1. Download and install JDK from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
2. Add Java to your PATH environment variable
3. Verify installation:
   ```cmd
   java -version
   javac -version
   ```

### macOS

Install via Homebrew:
```bash
brew install openjdk
```

Or download from Oracle/OpenJDK websites.

Verify installation:
```bash
java -version
javac -version
```

### Linux

Ubuntu/Debian:
```bash
sudo apt update
sudo apt install default-jdk
```

Fedora:
```bash
sudo dnf install java-latest-openjdk
```

Arch Linux:
```bash
sudo pacman -S jdk-openjdk
```

Verify installation:
```bash
java -version
javac -version
```

## Compiling and Running

### On Windows

1. Open Command Prompt or PowerShell
2. Navigate to the project directory:
   ```cmd
   cd path\to\assignment 1
   ```
3. Compile:
   ```cmd
   javac SimpleFileExplorer.java
   ```
4. Run:
   ```cmd
   java SimpleFileExplorer
   ```

### On macOS / Linux

1. Open Terminal
2. Navigate to the project directory:
   ```bash
   cd /path/to/assignment\ 1
   ```
3. Compile:
   ```bash
   javac SimpleFileExplorer.java
   ```
4. Run:
   ```bash
   java SimpleFileExplorer
   ```

## How to Use the Program

When you run the program, you'll see this menu:
```
=== Simple File Explorer ===
Current directory: /mnt/d/assignment 1

1. List files in current directory
2. Create a new directory
3. Delete a file
4. Move file
5. Open folder
6. Copy folder
7. Go back to parent directory
8. Exit
Enter your choice (1-8):
```

### Option 1: List Files
- Type `1` and press Enter
- Shows all files and directories with their types and sizes
- Directories will show size as 0
- Example output shows file names, types (File/Dir), and sizes in bytes

### Option 2: Create Directory
- Type `2` and press Enter
- Enter the name for your new directory
- Example: `my_folder`
- The directory will be created in the current location

### Option 3: Delete File
- Type `3` and press Enter
- Enter the name of the file to delete
- Example: `test.txt`
- Be careful: deletion is permanent!

### Option 4: Move File
- Type `4` and press Enter
- Enter the source file name (e.g., `document.txt`)
- Enter the destination path (e.g., `backup` or `../parent_folder`)
- If destination doesn't exist, it will be created
- You'll be asked to confirm if a file with the same name exists

### Option 5: Open Folder
- Type `5` and press Enter
- Enter the folder name to navigate into
- Example: `my_folder`
- Your current directory will change to the specified folder
- The current path is shown at the top of the menu

### Option 6: Copy Folder
- Type `6` and press Enter
- Enter the source folder name (e.g., `project`)
- Enter the destination path (e.g., `backup` or `.`)
- Copies the entire folder and all its contents recursively
- Shows progress while copying files
- You'll be asked to confirm if a folder with the same name exists

### Option 7: Go Back to Parent Directory
- Type `7` and press Enter
- Navigates up one level in the directory structure
- Useful for moving back after opening a folder

### Option 8: Exit
- Type `8` to close the program

Example Output:
```
=== Simple File Explorer ===
Current directory: D:\assignment 1

1. List files in current directory
2. Create a new directory
3. Delete a file
4. Move file
5. Open folder
6. Copy folder
7. Go back to parent directory
8. Exit
Enter your choice (1-8): 1

Files in current directory:
Name                                     Type       Size(B)      Last Modified
--------------------------------------------------------------------------------
SimpleFileExplorer.java                  File       10245        2025-11-13 14:23:45
SimpleFileExplorer.class                 File       3421         2025-11-13 14:24:12
README.md                                File       5200         2025-11-13 15:10:33
test_folder                              Dir        0            2025-11-13 14:30:15

=== Simple File Explorer ===
Current directory: D:\assignment 1

1. List files in current directory
2. Create a new directory
3. Delete a file
4. Move file
5. Open folder
6. Copy folder
7. Go back to parent directory
8. Exit
Enter your choice (1-8): 5
Enter folder name to open (relative or absolute): test_folder
Navigated to: D:\assignment 1\test_folder
```

## Code Structure

The program is organized into several methods within the `SimpleFileExplorer` class:

- `main()`: Contains the main program loop and menu handling
- `displayMenu()`: Shows the main menu options and current directory path
- `listFiles()`: Lists all files in the current directory with type, size, and last modified date
- `createDirectory()`: Creates a new directory
- `deleteFile()`: Deletes a specified file or directory (with recursive option for directories)
- `moveFile()`: Moves a file from source to destination path
- `openFolder()`: Navigates into a specified directory
- `copyFolder()`: Recursively copies a folder and all its contents
- `goToParentDirectory()`: Navigates back to the parent directory

## Learning Points

This project demonstrates several important Java concepts:

1. **File I/O and NIO.2** using `java.nio.file` package
   - Path manipulation and navigation
   - File and directory operations (create, delete, move, copy)
   - Directory traversal using `DirectoryStream` and `Files.walkFileTree()`
   - File attributes (size, last modified time)
2. **Error Handling** with try-catch blocks
3. **User Input** handling with `Scanner` class
4. **Switch Expressions** (Java 12+)
5. **Date/Time API** for formatting timestamps
6. **Visitor Pattern** using `SimpleFileVisitor` for recursive operations
7. **Lambda Expressions** and method references
8. **String formatting** using `printf`

## Notes for Interviewers

This project showcases:
- Understanding of Java I/O and NIO.2 APIs
- Advanced file system manipulation capabilities
- Proper error handling and exception management
- User input validation and sanitization
- Clean code organization using methods
- Use of modern Java features (switch expressions, try-with-resources)
- Cross-platform file path handling
- Ability to handle edge cases (overwrite confirmations, path validation)
- Recursive algorithms (folder copying and deletion)
- Understanding of the Visitor design pattern