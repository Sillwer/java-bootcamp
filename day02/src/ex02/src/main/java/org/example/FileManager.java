package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManager {
    private File currentDir;
    private Scanner scanner = null;

    FileManager(String path, Scanner scanner) throws FileNotFoundException, IllegalArgumentException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("Directory '%s' not found", path));
        } else if (!file.isDirectory()) {
            throw new IllegalArgumentException(String.format("It's '%s' not a directory", path));
        }
        currentDir = file;
        this.scanner = scanner;
    }

    public String getCurrentDirPath() {
        return currentDir.getPath();
    }

    public void ls(String[] args) {
        switch (args.length) {
            case 0:
                showDirContent();
                break;
            case 1:
                showDirContent(args[0]);
                break;
            default:
                System.out.println("Usage: ls [path_to_directory]");
        }
    }

    private void showDirContent(String dirPath) {
        try {
            File file = currentDir.toPath().resolve(Paths.get(dirPath)).toFile();
            if (!file.exists()) {
                System.out.printf("File '%s' not found\n", dirPath);
                return;
            }

            File[] filesList = null;
            if (file.isDirectory()) {
                filesList = file.listFiles();
            } else {
                filesList = new File[1];
                filesList[0] = file;
            }


            if (filesList != null && filesList.length == 0) {
                System.out.println("ls: Directory is empty");
                return;
            }

            for (File f : filesList) {
                String name = f.getName();
                if (f.isDirectory()) {
                    name += File.separator;
                }
                if (name.split("\\s+").length > 1) {
                    name = "'" + name + "'";
                }

                String sizeStr = "-";
                if (!f.isDirectory()) {
                    if (f.length() / 1024 >= 1) {
                        sizeStr = String.format("%d KB", f.length() / 1024);
                    } else {
                        sizeStr = String.format("%d B", f.length());
                    }
                }
                System.out.printf("%-30s %s\n", name, sizeStr);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void pwd() {
        System.out.println(currentDir.getPath());
    }

    private void showDirContent() {
        showDirContent(currentDir.getPath());
    }

    public void cd(String[] args) {
        switch (args.length) {
            case 0:
                changeDir();
                break;
            case 1:
                changeDir(args[0]);
                break;
            default:
                System.out.println("Usage: cd [path_to_directory]");
        }
    }

    private void changeDir(String dirPath) {
        try {
            File dirFile = Paths.get(currentDir.getPath()).resolve(Paths.get(dirPath)).normalize().toFile();
            if (!dirFile.exists()) {
                System.out.printf("Directory '%s' not found\n", dirPath);
                return;
            } else if (!dirFile.isDirectory()) {
                System.out.printf("File '%s' isn't directory\n", dirPath);
                return;
            }
            currentDir = dirFile;
            System.out.println(getCurrentDirPath());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeDir() {
        changeDir(System.getProperty("user.home"));
    }

    public void mv(String[] args) {
        switch (args.length) {
            case 2:
                moveFile(args[0], args[1]);
                break;
            default:
                System.out.println("Usage: mv file_old_name file_new_name");
        }
    }

    private void moveFile(String filePathFrom, String filePathTo) {
        Path pathFrom = currentDir.toPath().resolve(Paths.get(filePathFrom));
        Path pathTo = currentDir.toPath().resolve(Paths.get(filePathTo));


        File FileFrom = pathFrom.toFile();
        if (!FileFrom.exists()) {
            System.out.printf("File '%s' not fond\n", filePathFrom);
        }
        File FileTo = pathTo.toFile();

        try {
            String type = FileFrom.isDirectory() ? "Directory" : "File";
            if (FileTo.exists() && FileTo.isDirectory() == FileFrom.isDirectory()) {

                System.out.printf("%s '%s' exists\nReplace? (y/n): ", type, filePathTo);
                String answer = scanner.nextLine().trim();
                if (!answer.equalsIgnoreCase("y")) {
                    return;
                }
                if (!FileTo.delete()) {
                    System.out.printf("%s '%s' cant be replaced\n", type, filePathTo);
                    return;
                }
            }


            if (!FileFrom.renameTo(FileTo)) {
                System.out.printf("%s '%s' cant be moved\n", type, filePathFrom);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
