package ru.ifmo.ctddev.berezhko.copy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class UIFileCopy implements PropertyChangeListener {
    public static final String passedTimePrefix = "Времени прошло: ";
    public static final String remainingTimePrefix = "Времени осталось: ";
    public static final String currentSpeedPrefix = "Текущая скорость: ";
    public static final String averageSpeedPrefix = "Средняя скорость: ";
    public static final String speedUnit = " МБ/сек";

    private static final int BORDER = 5;

    private File sourceFile;
    private File destinationFile;
    private Copier copier;

    private JFrame mainWindow;

    private JProgressBar progressBar;

    private JTextField passedTimeView;
    private JTextField remainingTimeView;
    private JTextField currentSpeedView;
    private JTextField averageSpeedView;

    public UIFileCopy(File sourceFile, File destinationFile) {
        this.sourceFile = sourceFile;
        this.destinationFile = destinationFile;
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



    private void startCopying() {
        SwingUtilities.invokeLater(() ->{
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

            copier = new Copier(sourceFile, destinationFile, this);
            copier.addPropertyChangeListener(this);

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

            try {
                copier.execute();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        Path sourcePath = Paths.get(args[0]);
        Path destinationPath = Paths.get(args[1]);

        File sourceFile = sourcePath.toFile();
        File destinationFile = destinationPath.toFile();

        if (destinationFile.exists() && destinationFile.isDirectory()) {
            String path = args[1] + "/" + sourcePath.getName(sourcePath.getNameCount() - 1);
            destinationPath = Paths.get(path);
            destinationFile = destinationPath.toFile();
        }
        UIFileCopy copier = new UIFileCopy(sourceFile, destinationFile);
        copier.startCopying();
    }
}
