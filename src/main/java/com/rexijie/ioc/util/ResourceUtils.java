package com.rexijie.ioc.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResourceUtils {
    public static String CLASSPATH_URL_PREFIX = "classpath:";


    public static String classPathRelative(String path) {
        return CLASSPATH_URL_PREFIX + "/" + path;
    }
    public static List<File> findDirectories(File baseDir) {
        List<File> files = new ArrayList<>();
        File[] dirFiles = baseDir.listFiles(dirFilter());

        if (!baseDir.exists()) return files;
        if (dirFiles == null) return files;

        for (File file : dirFiles) {
            if (file.getName().startsWith(".") | file.getName().startsWith("target")) continue;
            if (file.isDirectory()) {
                files.add(file);
                files.addAll(findDirectories(file));
            }
        }

        return files;
    }

    public static List<File> loadResources(File dir) {
        List<File> files = new ArrayList<>();
        File[] dirFiles = dir.listFiles();

        if (!dir.exists()) return files;
        if (dirFiles == null) return files;

        for (File file : dirFiles) {
            if (file.getName().startsWith(".")) continue;
            if (file.isDirectory())
                files.addAll(loadResources(file));
            else
                files.add(file);
        }

        return files;
    }


    public static List<File> loadFilesWithExtension(File dir, String extension) {
        File[] files = dir.listFiles(extensionFilter(extension));
        List<File> fileList = new ArrayList<>();

        if (files == null) return fileList;

        for (File file : files) {
            if (file == null) continue;
            fileList.add(file);
        }
        return fileList;
    }

    private static FilenameFilter extensionFilter(String extension) {
        return (dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(extension);
    }

    private static FilenameFilter nameFilter(String filename) {
        return (dir, names) -> names.toLowerCase(Locale.ROOT).equals(filename);
    }

    private static FileFilter dirFilter() {
        return File::isDirectory;
    }
}
