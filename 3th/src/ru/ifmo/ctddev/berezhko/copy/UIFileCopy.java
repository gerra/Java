package ru.ifmo.ctddev.berezhko.copy;

import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class UIFileCopy implements PropertyChangeListener {
    public static final String passedTimePrefix = "Времени прошло: ";
    public static final String remainingTimePrefix = "Времени осталось: ";
    public static final String currentSpeedPrefix = "Текущая скорость: ";
    public static final String averageSpeedPrefix = "Средняя скорость: ";
    public static final String speedUnit = " МБ/сек";

    private static final String fileDoesNotExistFormat = "Файл %s не существует";

    private static final int BORDER = 5;

    private String sourcePathString;
    private String destinationPathString;

    private File sourceFile;
    private File destinationFile;

    private Copier copier;

    private JFrame mainWindow;

    private JProgressBar progressBar;

    private JTextField passedTimeView;
    private JTextField remainingTimeView;
    private JTextField currentSpeedView;
    private JTextField averageSpeedView;


    public UIFileCopy(@NotNull String sourcePathString, @NotNull String destinationPathString) {
        this.sourcePathString = sourcePathString;
        this.destinationPathString = destinationPathString;
    }

    public static String getTimeAsString(long millis) {
        return String.format("%d мин, %d сек",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public void updatePassedTime(long millis) {
        passedTimeView.setText(passedTimePrefix + getTimeAsString(millis));
    }

    public void updateRemainingTime(long millis) {
        remainingTimeView.setText(remainingTimePrefix + getTimeAsString(millis));
    }

    public void updateCurrentSpeed(long speed) {
        currentSpeedView.setText(currentSpeedPrefix + speed + speedUnit);
    }

    public void updateAverageSpeed(long speed) {
        averageSpeedView.setText(averageSpeedPrefix + speed + speedUnit);
    }

    public void closeWindow() {
        mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == "progress") {
            int currentProgress = (Integer) evt.getNewValue();
            progressBar.setValue(currentProgress);
        }
    }

    private void createFiles() {
        Path sourcePath = Paths.get(sourcePathString);
        sourceFile = sourcePath.toFile();
        if (!sourceFile.exists()) {
            String error = String.format(fileDoesNotExistFormat, sourceFile.getAbsolutePath());
            System.err.println("Source file " + sourcePathString + " doesn't exist");
            showErrorInMainWindow(error);
        }

        Path destinationPath = Paths.get(destinationPathString);
        destinationFile = destinationPath.toFile();
        if (destinationFile.exists() && destinationFile.isDirectory()) {
            String path = destinationPathString + "/" + sourcePath.getName(sourcePath.getNameCount() - 1);
            destinationPath = Paths.get(path);
            destinationFile = destinationPath.toFile();
            try {
                destinationFile.createNewFile();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                showErrorInMainWindow("Невозможно скопировать в " + path);
                System.exit(1);
            }
        }
        if (!destinationFile.exists()) {
            try {
                destinationFile.createNewFile();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                showErrorInMainWindow("Невозможно скопировать в "
                        + destinationFile.getAbsolutePath()
                        + ". Данного файла или директории не существует");
            }
        }


        if (!destinationFile.exists()) {
            String error = String.format(fileDoesNotExistFormat, destinationPathString);
            System.err.println("Destination file " + destinationFile.getAbsolutePath() + " doesn't exist");
            showErrorInMainWindow(error);
        }
    }

    private void startCopying() {
        SwingUtilities.invokeLater(() -> {
            mainWindow = new JFrame("UIFileCopy");
            mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(Box.createVerticalGlue());

            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);

            passedTimeView = new JTextField(passedTimePrefix);
            passedTimeView.setEditable(false);

            remainingTimeView = new JTextField(remainingTimePrefix);
            remainingTimeView.setEditable(false);

            currentSpeedView = new JTextField(currentSpeedPrefix);
            currentSpeedView.setEditable(false);

            averageSpeedView = new JTextField(averageSpeedPrefix);
            averageSpeedView.setEditable(false);

            JButton cancelButton = new JButton("Отмена");
            cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            cancelButton.addActionListener(e -> {
                System.out.println("Copying has canceled");
                copier.cancel(true);
                closeWindow();
            });

            panel.add(progressBar);
            panel.add(passedTimeView);
            panel.add(remainingTimeView);
            panel.add(currentSpeedView);
            panel.add(averageSpeedView);
            panel.add(cancelButton);

            panel.add(Box.createVerticalGlue());

            mainWindow.getContentPane().setLayout(new BorderLayout());
            mainWindow.getContentPane().add(panel, BorderLayout.CENTER);

            mainWindow.setResizable(false);
            mainWindow.setPreferredSize(new Dimension(260, 150));
            mainWindow.pack();
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setVisible(true);

            mainWindow.setVisible(true);

            createFiles();

            try {
                copier = new Copier(sourceFile, destinationFile, this);
                copier.addPropertyChangeListener(this);
                copier.execute();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public void showErrorInMainWindow(String error) {
        JOptionPane.showMessageDialog(mainWindow, error, "Ошибка", JOptionPane.ERROR_MESSAGE);
        closeWindow();
    }

    public static void main(String[] args) {
        if (args == null || args.length < 2 || Arrays.stream(args).anyMatch(Predicate.isEqual(null))) {
            System.err.println("Usage: UIFileCopy <source> <destination>");
            System.exit(1);
        }
        UIFileCopy copier = new UIFileCopy(args[0], args[1]);
        copier.startCopying();
    }
}
