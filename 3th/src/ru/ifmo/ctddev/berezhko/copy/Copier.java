package ru.ifmo.ctddev.berezhko.copy;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Implements foreground process for copying files
 */
public class Copier extends SwingWorker<Void, Copier.Result> {
    /**
     * Describes current state of downloading
     * Consist of current speed, average speed and remaining time
     */
    public class Result {
        private long currentSpeed;
        private long averageSpeed;
        private long remainingTime;
        private String currentFile;

        public void setCurrentSpeed(long currentSpeed) {
            this.currentSpeed = currentSpeed;
        }

        public void setAverageSpeed(long averageSpeed) {
            this.averageSpeed = averageSpeed;
        }

        public long getCurrentSpeed() {
            return currentSpeed;
        }

        public long getAverageSpeed() {
            return averageSpeed;
        }

        public long getRemainingTime() {
            return remainingTime;
        }

        public void setRemainingTime(long remainingTime) {
            this.remainingTime = remainingTime;
        }

        public String getCurrentFile() {
            return currentFile;
        }

        public void setCurrentFile(String currentFile) {
            this.currentFile = currentFile;
        }
    }

    private File sourceFile, destinationFile;
    private UIFileCopy uiFileCopy;
    private long sourceSize;
    private long totalCopied;
    private Result result;
    long remainingTime;

    long lengthForSec = 0;
    long currentSpeed;

    long totalPassed = 0;
    long averageSpeed;

    /**
     * Create copier
     *
     * @param sourceFile file copied from
     * @param destinationFile file copied to
     * @param uiFileCopy window for updating view information
     */
    public Copier(File sourceFile, File destinationFile, UIFileCopy uiFileCopy) {
        this.sourceFile = sourceFile;
        this.destinationFile = destinationFile;
        this.uiFileCopy = uiFileCopy;
    }

    private void copyRecursively(File sourceFile, File destinationFile) throws Exception {
        //System.out.println(destinationFile.getAbsolutePath());
        //System.out.println(sourceFile.getName());
        if (!sourceFile.isDirectory()) {
            Path sourcePath = Paths.get(sourceFile.getAbsolutePath());
            destinationFile = Paths.get(destinationFile.getAbsolutePath() + "/" + sourcePath.getName(sourcePath.getNameCount() - 1)).toFile();
            if (!destinationFile.exists()) {
                destinationFile.createNewFile();
            }
            result.setCurrentFile(sourceFile.getName());
//            result.setCurrentFile(sourceFile.getPath());
            publish(result);
            try (
                    FileInputStream is = new FileInputStream(sourceFile);
                    FileOutputStream os = new FileOutputStream(destinationFile)) {
                byte[] buf = new byte[1024];

                long time1 = System.currentTimeMillis();
                long time2;
                int length;
                while (!isCancelled() && (length = is.read(buf)) > 0) {
                    os.write(buf, 0, length);
                    time2 = System.currentTimeMillis();
                    if (time2 - time1 >= 1000) {
                        double ds = lengthForSec / 1e6;     // B -> MB
                        double dt = (time2 - time1) / 1e3;  // ms -> s
                        currentSpeed = (long) (ds / dt);    // MB/s

                        totalPassed += dt;
                        averageSpeed = (long) (totalCopied / 1e6 / totalPassed);

                        time1 = System.currentTimeMillis();
                        lengthForSec = 0;

                        remainingTime = (long) (1e-3 * (sourceSize - totalCopied)  / averageSpeed);

                        result.setAverageSpeed(averageSpeed);
                        result.setCurrentSpeed(currentSpeed);
                        result.setRemainingTime(remainingTime);
                        publish(result);
                    }
                    lengthForSec += length;
                    totalCopied += length;
                    setProgress((int) (100.0 * totalCopied / sourceSize));
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                uiFileCopy.showErrorInMainWindow(e.getLocalizedMessage());
            }
        } else {
            Path sourcePath = Paths.get(sourceFile.getAbsolutePath());
            String path = destinationFile.getAbsolutePath() + "/" + sourcePath.getName(sourcePath.getNameCount() - 1) + "/";
            File newDestFile = Paths.get(path).toFile();
            if (newDestFile.exists() && !newDestFile.isDirectory()) {
                newDestFile.delete();
            }
            newDestFile.mkdir();
            for (File file : sourceFile.listFiles()) {
                copyRecursively(file, newDestFile);
            }
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        System.out.println(String.format("Copying %s to %s", sourceFile.getCanonicalPath(), destinationFile.getCanonicalPath()));
        result = new Result();
        publish(result);
        sourceSize = Utils.dirOrFileSize(sourceFile);
        try (MyTimer timer = new MyTimer(uiFileCopy)) {
            timer.execute();
            copyRecursively(sourceFile, destinationFile);
        }
        return null;
    }

    @Override
    protected void process(List<Result> chunks) {
        if (chunks.size() > 0) {
            Result result = chunks.get(chunks.size() - 1);
            uiFileCopy.updateInfo(result);
        }
    }

    @Override
    protected void done() {
        super.done();
        System.out.println("Copying has finished");
        uiFileCopy.closeWindow();
    }
}