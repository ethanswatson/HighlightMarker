package main.java.com.theviusracconus.highlightmarker;



import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

public class HighlightMarker extends JFrame implements Runnable
{
    public static final int MAX_MARKERS = 30;

    private Uptime uptime;
    private int runCount;
    private int markerCount;
    private boolean timer;
    private static String streamTitle;
    private String twitchName;
    private Last10 last10;
    private SwitchTimer switchTimer;
    private String[] recent;
    private JComboBox streamSelect;
    private ArrayList<JLabel> markers;
    private JLabel label;
    private JLabel recentStreams;
    private JLabel selectPrompt;
    private JButton markerButton;
    private JButton refreshButton;
    //private JButton saveButton;
    private JButton timerButton;
    private JButton settingsButton;
    private static JTextField textField;
    private CreateMarker createMarker;
    private ShowMarkers showMarkers;
    private OpenSettings openSettings;
    private RefreshTitle refreshTitle;


    public HighlightMarker()
    {
        Settings.readName();
        boolean jsonExist = Settings.loadJSON();
        uptime = new Uptime();
        timer = false;
        runCount = 0;
        markerCount = 1;

        textField = new JTextField(15);

        if(jsonExist)
        {
            setStreamTitle();
        }
        else
        {
            streamTitle = "Untitled";
            textField.setText(streamTitle);
        }
        last10 = new Last10();
        String[] temp = last10.reverse();
        recent = new String[temp.length + 1];
        recent[0] = "";
        for(int i = 0; i < temp.length; i++)
        {
            recent[i+1] = temp[i];
        }

        markers = new ArrayList<JLabel>(MAX_MARKERS);

        for(int i = 0; i < MAX_MARKERS; i++)
        {
            markers.add(i, new JLabel());
        }

        setLayout(new FlowLayout());

        label = new JLabel("Enter the stream title:");
        recentStreams = new JLabel(last10.toString());
        selectPrompt = new JLabel("Select a Stream to view Highlight Markers:");


        markerButton = new JButton("Create Marker");
        //saveButton = new JButton("Save");
        timerButton = new JButton("Start Timer");
        settingsButton = new JButton("Settings");
        refreshButton = new JButton("Refresh");

        streamSelect = new JComboBox(recent);
        showMarkers = new ShowMarkers();
        streamSelect.addActionListener(showMarkers);
        openSettings = new OpenSettings();
        settingsButton.addActionListener(openSettings);
        refreshTitle = new RefreshTitle();
        refreshButton.addActionListener(refreshTitle);

        add(settingsButton);
        add(label);
        add(textField);
        add(refreshButton);
        //add(saveButton);
        add(timerButton);
        add(markerButton);
        //add(recentStreams);
        add(selectPrompt);
        add(streamSelect);

        for(int i = 0; i < MAX_MARKERS; i++)
        {
            add(markers.get(i));
        }

        createMarker = new CreateMarker();
        switchTimer = new SwitchTimer();
        markerButton.addActionListener(createMarker);
        //saveButton.addActionListener(saveField);
        timerButton.addActionListener(switchTimer);
    }


    public void run()
    {
        if(timer)
        {
            uptime.run();
        }

        if(runCount == 5 && !streamSelect.isPopupVisible())
        {
            String[] temp = last10.reverse();
            recent = new String[temp.length + 1];
            recent[0] = "";
            for(int i = 0; i < temp.length; i++)
            {
                recent[i + 1] = temp[i];
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(recent);
            model.setSelectedItem(streamSelect.getSelectedItem());
            streamSelect.setModel(model);
            runCount = 0;
        }
        else
        {
            runCount++;
        }
    }

    public void setIcon()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Icon.png")));
    }

    public void setTwitchName(String name)
    {
        twitchName = name;
    }

    public static void setStreamTitle()
    {
        String title = (String) Settings.jsonObject.get("status");
        streamTitle = title;
        textField.setText(title);
    }

    public class CreateMarker implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
                Marker.writeFile(uptime, streamTitle, markerCount);
                markerCount++;
        }
    }

    /*public class SaveField implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == saveButton)
            {
                streamTitle = textField.getText();
                last10.add(streamTitle);
                last10.writeFile();
            }
        }
    }*/

    public class ShowMarkers implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

                JComboBox cb = (JComboBox) e.getSource();
                String title = (String) cb.getSelectedItem();

                Marker.getMarkers(title, markers);
        }
    }

    public class SwitchTimer implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(timer)
            {
                timer = false;
                timerButton.setText("Start Timer");
            }
            else
            {
                timer = true;
                timerButton.setText("Stop Timer");
                streamTitle = textField.getText();
                markerCount = 1;
                last10.add(streamTitle);
            }
        }
    }

    public class OpenSettings implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Settings settings = new Settings();
        }
    }

    public class RefreshTitle implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Settings.refreshJSON();
            setStreamTitle();
        }
    }
}

