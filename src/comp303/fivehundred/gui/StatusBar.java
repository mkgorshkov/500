package comp303.fivehundred.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JLabel;

/**
 * Defines the status bar at the bottom of the GUI. Logger updates the status bar with current 
 * and relevant information for the game.
 * @author Maxim Gorshkov, Dashiel Siegel
 */
@SuppressWarnings("serial")
public class StatusBar extends JLabel
{
	// Initializes the short logger filename and location
	private String aShortLog = "MiscFiles/log.txt";
	
	/**
	 * Constructor for the status bar. 
	 */
    public StatusBar()
    {	
    	// Use the default label constructor
        super();
        super.setAlignmentY(LEFT_ALIGNMENT);
    }
    
    /**
     * Refreshes the status bar at the bottom of the user interface with the most current information.
     */
    public void refresh()
    {
        // Open and read from the file at loggerLocation.
		Scanner logScanner = null;
		try
		{
			// Last logger entry goes in the status bar
			File logFile = new File(aShortLog);
			if (!logFile.exists())
			{
				logFile.createNewFile();
			}
			logScanner = new Scanner(logFile);
			logScanner.useDelimiter("\n");
			if (logScanner.hasNext())
			{
				while (logScanner.hasNext())
				{
					String toSet = "";
					toSet += logScanner.next();
					setText(toSet);
				}
			}
			else
			{
				setText("*The log file is empty!*");
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
