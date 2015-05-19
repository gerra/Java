package ru.ifmo.ctddev.berezhko.copy;

import javax.swing.*;
import java.util.List;

/**
 * Implements timer for sharing information to UIFileCopy for update progress
 */
public class MyTimer extends SwingWorker<Void, Long> implements AutoCloseable {
    private UIFileCopy uiFileCopy;

    /**
     * Create instance of timer
     *
     * @param uiFileCopy UIFileCopy for updating progress
     */
    public MyTimer(UIFileCopy uiFileCopy) {
        this.uiFileCopy = uiFileCopy;
    }

    @Override
    protected Void doInBackground() throws Exception {
        long time = 0;
        publish(time);
        while (!isCancelled()) {
            Thread.sleep(1000);
            time += 1000;
            publish(time);
        }
        return null;
    }

    @Override
    protected void process(List<Long> chunks) {
        if (chunks.size() > 0) {
            uiFileCopy.updatePassedTime(chunks.get(0));
        }
    }

    @Override
    public void close() throws Exception {
        cancel(true);
    }
}