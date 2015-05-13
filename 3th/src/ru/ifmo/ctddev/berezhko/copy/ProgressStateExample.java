package ru.ifmo.ctddev.berezhko.copy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by root on 14.05.15.
 */
public final class ProgressStateExample {

    public static final void main(final String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        final JFrame sampleFrame = new JFrame();
        sampleFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel body = new JPanel();

        body.setLayout(new BorderLayout());

        final JSlider slider = new JSlider(0, 60);
        body.add(slider, BorderLayout.NORTH);

        @SuppressWarnings("serial")
        final Action action = new AbstractAction("Click Here") {

            @Override
            public final void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(sampleFrame, "As you can see, the UI is responsive and there are no repaint problems!");
            }

        };

        final JButton button = new JButton(action);
        body.add(button, BorderLayout.SOUTH);

        sampleFrame.add(body);
        sampleFrame.pack();
        sampleFrame.setVisible(true);

        final SwingWorker<Integer, Integer> backgroundWork = new SwingWorker<Integer, Integer>() {

            @Override
            protected final Integer doInBackground() throws Exception {
                for (int i = 0; i < 61; i++) {
                    Thread.sleep(1000);
                    this.publish(i);
                }

                return 60;
            }

            @Override
            protected final void process(final List<Integer> chunks) {
                slider.setValue(chunks.get(0));
            }

        };

        backgroundWork.execute();
    }
}
