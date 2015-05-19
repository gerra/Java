package ru.ifmo.ctddev.berezhko.copy;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Utils {
    /**
     * Calculate size of file or directory
     *
     * @param dirOrFile file to calc
     * @return size of file or directory {@code dirOrFile}
     */
    public static long dirOrFileSize(File dirOrFile) {
        if (!dirOrFile.isDirectory()) {
            return dirOrFile.length();
        }
        long length = 0;
        for (File file : dirOrFile.listFiles()) {
            length += dirOrFileSize(file);
        }
        return length;
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
