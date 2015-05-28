package ru.ifmo.ctddev.berezhko.copy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Utils {
    /**
     * Calculate size of file or directory
     *
     * @param dirOrFile file to calc
     * @return size of file or directory {@code dirOrFile}
     */
    public static long dirOrFileSize(File dirOrFile) {
        final long[] length = {0};
        try {
            Files.walk(Paths.get(dirOrFile.getAbsolutePath()))
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        File file = filePath.toFile();
                        length[0] += file.length();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (!dirOrFile.isDirectory()) {
//            return dirOrFile.length();
//        }
//
//        for (File file : dirOrFile.listFiles()) {
//            length[0] += dirOrFileSize(file);
//        }
        return length[0];
    }

    /**
     * Get String in format "X мин, Y сек" by the time in milliseconds
     *
     * @param millis time in milliseconds
     * @return formatted string
     */
    public static String getTimeAsString(long millis) {
        return String.format("%d мин, %d сек",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
