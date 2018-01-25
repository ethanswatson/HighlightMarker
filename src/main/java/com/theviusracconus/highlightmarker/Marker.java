package main.java.com.theviusracconus.highlightmarker;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Marker
{
    public static void writeFile(Uptime uptime, String title, int count)
    {
        if(count == 1)
        {
            List<String> lines = Arrays.asList(count + ") " + uptime.toString());
            Path file = Paths.get(title + ".txt");
            try
            {
                Files.write(file, lines, Charset.forName("UTF-8"));
                count++;
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            List<String> lines = Arrays.asList(count + ") " + uptime.toString());
            Path file = Paths.get(title + ".txt");
            try
            {
                Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
                count++;
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void getMarkers(String title, ArrayList<JLabel> list)
    {
        int counter = 1;

        for(JLabel label: list)
        {
            label.setText("");
        }

        try {
            File file = new File(title + ".txt");
            Scanner scanner = new Scanner(file);
            list.get(0).setText(title + ": ");
            while (scanner.hasNextLine()) {
                list.get(counter).setText(scanner.nextLine());
                counter++;
            }
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}