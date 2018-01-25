package main.java.com.theviusracconus.highlightmarker;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Settings extends JFrame
{
    private JTextField twitchName;
    private JLabel namePrompt;
    private JButton save;
    private SaveChanges saveChanges;
    public static JSONObject jsonObject;
    private static String name;

    public Settings()
    {
        setIcon();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 150);
        setVisible(true);
        setTitle("Highlight Marker: Settings");

        setLayout(new FlowLayout());
        saveChanges = new SaveChanges();

        namePrompt = new JLabel("Enter your Twitch username to automatically detect stream titles: ");
        twitchName = new JTextField(name, 15);
        save = new JButton("Save Changes");

        add(namePrompt);
        add(twitchName);
        add(save);

        save.addActionListener(saveChanges);
    }

    public String getTwitchName()
    {
        return name;
    }

    public JSONObject getJsonObject()
    {
        return jsonObject;
    }

    public static boolean loadJSON()
    {
        try
        {
            downloadFile();
            FileReader reader = new FileReader("twitch.json");
            jsonObject = (JSONObject) new JSONParser().parse(reader);
            return true;
        }catch(ParseException | IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static void readName()
    {
        try {
            File file = new File("name.thlm");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
            {
                name = scanner.nextLine();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void setIcon()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Icon.png")));
    }

    private static void downloadFile()
    {
        try
        {
            if(name != null)
            {
                URL url = new URL("https://api.twitch.tv/kraken/channels/" + name);
                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                FileOutputStream fos = new FileOutputStream("twitch.json");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        }catch(IOException e1)
        {
            e1.printStackTrace();
        }
    }

    public static void refreshJSON()
    {
        try
        {
            downloadFile();
            FileReader reader = new FileReader("twitch.json");
            jsonObject = (JSONObject) new JSONParser().parse(reader);

        }catch(ParseException | IOException e1)
        {
            e1.printStackTrace();
        }
    }

    public class SaveChanges implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            name = twitchName.getText();
            java.util.List<String> lines = Arrays.asList(name);
            Path file = Paths.get("name.thlm");
            try
            {
                Files.write(file, lines, Charset.forName("UTF-8"));
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            refreshJSON();
            HighlightMarker.setStreamTitle();
            dispose();
        }
    }
}
