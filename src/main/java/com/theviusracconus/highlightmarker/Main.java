package main.java.com.theviusracconus.highlightmarker;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main
{
    private static HighlightMarker gui = new HighlightMarker();

    public static void main(String[] args)
    {
        initGui();
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(gui, 0, 1, TimeUnit.SECONDS);
    }

    public static void initGui()
    {
        gui.setIcon();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(600, 600);
        gui.setVisible(true);
        gui.setTitle("Highlight Marker");
    }
}
