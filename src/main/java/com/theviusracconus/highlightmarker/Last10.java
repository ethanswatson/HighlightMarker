package main.java.com.theviusracconus.highlightmarker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Last10
{
    private String[] titles;
    private int count;

    public Last10()
    {
        titles = new String[10];
        count = 0;
        readFile();
    }

    private void readFile()
    {
        try {
            File file = new File("RecentStreams.thlm");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                titles[count] = scanner.nextLine();
                count++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            List<String> lines = Arrays.asList();
            Path file = Paths.get("RecentStreams.thlm");
            try
            {
                Files.write(file, lines, Charset.forName("UTF-8"));
                count++;
            }catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void writeFile()
    {
        List<String> lines = Arrays.asList(removeNull());
        Path file = Paths.get("RecentStreams.thlm");
        try
        {
            Files.write(file, lines, Charset.forName("UTF-8"));
            count++;
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public String[] removeNull()
    {
        String[] result = new String[titles.length - countNull()];
        int pos = 0;

        for(int i = 0; i < titles.length; i++)
        {
            if(titles[i] != null)
            {
                result[pos] = titles[i];
                pos++;
            }
        }

        return result;
    }

    private int countNull()
    {
        int count = 0;

        for(String str: titles)
        {
            if(str == null)
            {
                count++;
            }
        }
        return count;
    }

    public String get(int i)
    {
        return titles[i];
    }

    public void add(String str)
    {
        if(count < 10)
        {
            titles[count] = str;
        }
        else
        {
            String[] temp = new String[titles.length];
            for(int i = 0; i < titles.length - 1; i++)
            {
                temp[i] = titles[i+1];
            }
            temp[titles.length - 1] = str;

            titles = temp;
        }
        writeFile();
    }

    public String[] reverse()
    {
        String[] noNull = removeNull();
        String[] temp = new String[noNull.length];

        for(int i = 0; i < temp.length; i++)
        {
            temp[i] = noNull[noNull.length - 1 - i];
        }

        return temp;
    }

    public String toString()
    {
        String[] temp = reverse();
        String result = "Last 10 Streams:";
        for(int i = 0; i < temp.length; i++)
        {
            result += "\n" + temp[i];
        }

        return result;
    }

}
