package com.rexijie.ioc.util;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReflectionUtils {
    public static Class<?>[] getClassesInPackage(String _package, Class<?> assignableClass) {
        Class<?>[] classes = findClassesInPackage(_package, null);
        if (classes == null) return new Class[0];
        if (assignableClass == null) return classes;

        List<Class<?>> assignableClasses = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (assignableClass.isAssignableFrom(clazz))
                assignableClasses.add(clazz);
        }

        return assignableClasses.toArray(new Class[0]);
    }

    public static Class<?>[] findClassesInPackage(String _package) {
        return findClassesInPackage(_package, null);
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     * Adapted from http://snippets.dzone.com/posts/show/4831 and extended to support use of JAR files
     * adapted from: http://www.java2s.com/example/java/reflection/find-by-package-via-class-loader.html
     *
     * @param _package The base package
     * @param filter   an optional class name pattern.
     * @return The classes
     */
    public static Class<?>[] findClassesInPackage(String _package, String filter) {
        Pattern regexPattern = null;
        if (filter != null) {
            regexPattern = Pattern.compile(filter);
        }

        try {
            ClassLoader classLoader = ClassUtils.getRootClassLoader();
            String path = _package.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path); // this gets all the classes
            List<String> directories = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                directories.add(resource.getFile());
            }

            TreeSet<String> classes = new TreeSet<>();
            for (String directory : directories) {
                classes.addAll(findClasses(directory, _package, regexPattern));
            }

            ArrayList<Class<?>> classArray = new ArrayList<>();
            for (String clazz : classes) {
                classArray.add(Class.forName(clazz));
            }

            return classArray.toArray(new Class[0]);
        } catch (Exception ex) {
            // handle error
            return null;
        }
    }

    /**
     * Recursive method used to find all classes in a given path (directory or zip file url).  Directories
     * are searched recursively.  (zip files are
     * Adapted from http://snippets.dzone.com/posts/show/4831 and extended to support use of JAR files
     * Adapted from: http://www.java2s.com/example/java/reflection/find-by-package-via-class-loader.html
     *
     * @param path        The base directory or url from which to search.
     * @param packageName The package name for classes found inside the base directory
     * @param regex       an optional class name pattern.  e.g. .*Test
     * @return The classes
     */
    public static TreeSet<String> findClasses(String path,
                                              String packageName,
                                              Pattern regex) throws Exception {
        TreeSet<String> classes = new TreeSet<>();
        if (path.startsWith("file:") && path.contains("!")) {
            String[] split = path.split("!");
            URL jar = new URL(split[0]);
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                String className = entry.getName()
                        .replaceAll("[$].*", "")
                        .replaceAll("[.]class", "")
                        .replace('/', '.');

                if (className.startsWith(packageName)
                        && regexMatches(regex, className)) {
                    classes.add(className);
                }
            }
        }

        File dir = new File(path);
        if (!dir.exists()) {
            return classes;
        }

        File[] files = dir.listFiles();
        if (files == null) return classes;

        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                String filePath = file.getAbsolutePath();
                String filePackage = packageName
                        .concat(".")
                        .concat(file.getName());
                classes.addAll(
                        findClasses(filePath, filePackage, regex));
            } else if (file.getName().endsWith(".class")) {
                String fileNameWithoutClass = file.getName()
                        .substring(0, file.getName().length() - 6);
                String className = packageName
                        .concat(".")
                        .concat(fileNameWithoutClass);
                if (regexMatches(regex, className)) {
                    classes.add(className);
                }
            }
        }
        return classes;
    }

    private static boolean regexMatches(Pattern regexPattern, String input) {
        return regexPattern == null || regexPattern.matcher(input).matches();
    }
}
