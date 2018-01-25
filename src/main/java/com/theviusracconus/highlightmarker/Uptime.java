package main.java.com.theviusracconus.highlightmarker;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Uptime implements Runnable
{
    private int hr, min, sec;

    public Uptime()
    {
        hr = 0;
        min = 0;
        sec = 0;
    }

    public void increment()
    {
        if(!moveUp(sec))
        {
            if(moveUp(min) && moveUp(sec))
            {
                sec = 0;
                min = 0;
                hr++;
            }
            else
            {
                sec++;
            }
        }
        else
        {
            sec = 0;
            min++;
        }
    }

    public void run()
    {
        increment();
        List<String> lines = Arrays.asList(this.toString());
        Path file = Paths.get("uptime.txt");
        try
        {
            Files.write(file, lines, Charset.forName("UTF-8"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private boolean moveUp(int num)
    {
        return num == 59;
    }

    private String addZero(int num)
    {
        if(num < 10)
        {
            return "0" + num;
        }
        else
        {
            return "" + num;
        }
    }

    public String toString()
    {
        String hrStr, minStr, secStr;
        hrStr = addZero(hr);
        minStr = addZero(min);
        secStr = addZero(sec);

        return hrStr + ":" + minStr + ":" + secStr;
    }
}
