package comp303.fivehundred.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * An observer to keep track of all observable fields.
 * 
 * @author Dashiel Siegel
 * 
 */
public class GameStatistics implements Observer
{
	// Magic Numbers
	private static final int FIVE = 5;
	private static final int FIVE_HUNDRED = 500;
	private static final int ONE_HUNDRED = 100;

	// Stores the names of the players.
	private String[] aNames = {"", "", "", ""};

	// Initializes ObservableStat to store the tricks won.
	private int[] aTricksWon = { 0, 0, 0, 0 };
	// Initializes ObservableStat to store the contracts won
	private int[] aContractsWon = { 0, 0, 0, 0 };
	// Initializes ObservableStat to store the contracts made
	private int[] aContractsMade = { 0, 0, 0, 0 };
	// Initializes ObservableStat to store the games won
	private int[] aGamesWon = { 0, 0, 0, 0 };
	// Initializes ObservableStat to store the scores
	private int[] aScore = { 0, 0, 0, 0 };

	private Object[] aTmp = new Object[2];

	// Initializes array to store ratio of TricksWon/TotalTricks
	private String[] aTricksWonRatio = new String[4];
	// Initializes array to store ratio of ContractsWon/TotalContracts
	private String[] aContractsWonRatio = new String[4];
	// Initializes array to store ratio of ContractsMade/ContractsWon
	private String[] aContractsMadeRatio = new String[4];
	// Initializes array to store ratio of GamesWon/TotalGames
	private String[] aGamesWonRatio = new String[4];
	// Initializes array to store ratio of Score/(TotalGames * 500)
	private String[] aScoreIndex = new String[4];

	// Initializes int to store total number of Games played.
	private int aTotalGames = 0;
	// Initializes int to store total number of Tricks played.
	private int aTotalTricks = 0;
	// Initializes int to store total number of Contracts played.
	private int aTotalContracts = 0;

	// Initializes printing to file for statistics
	private FileWriter aStream;
	
	// Initializes the output file.
	private String aFileLocation = "miscFiles/stats.txt";

	// Initializes the boolean to track if a ratio calculation has divided-by-zero.
	private String[][] aDivZero = new String[4][FIVE];

	/**
	 * Constructs the GameStatistics object.
	 */
	public GameStatistics()
	{
		super();
	}

	/**
	 * Resets the divide-by-zero tracker aDivZero. Helper method for caluclateStatistics().
	 */
	public void resetDiv()
	{
		// Resets the divide-by-zero tracker
		for (int player = 0; player < 4; player++)
		{
			for (int stat = 0; stat < FIVE; stat++)
			{
				aDivZero[player][stat] = "%";
			}
		}
	}

	/**
	 * Calculates final statistics before printStatistics().
	 */
	public void calculateStatistics()
	{
		// Resets the divide-by-0 tracker aDivZero
		resetDiv();

		// Formats the rounding.
		DecimalFormat tenth = new DecimalFormat("0.0");
		DecimalFormat hundreth = new DecimalFormat("0.00");

		// Calculates tabulation
		for (int i = 0; i<4; i++)
		{
			if (aNames[i].length() > 13)
			{
				aNames[i] = aNames[i].substring(0, 10);
				aNames[i] += "...";
			}
		}
		
		
		aTotalTricks = 0;
		aTotalContracts = 0;
		// Calculates total tricks, contracts and games played.
		for (int i = 0; i < 4; i++)
		{
			aTotalTricks += aTricksWon[i];
			aTotalContracts += aContractsWon[i];
		}

		aTotalGames = 0;
		for (int i = 0; i < 2; i++)
		{
			aTotalGames += aGamesWon[i];
		}

		// Calculates the ratio fields.
		for (int i = 0; i < 4; i++)
		{
			aTricksWonRatio[i] = String.valueOf(tenth.format((float) aTricksWon[i] / (float) aTotalTricks *
					(float) ONE_HUNDRED));
			aContractsWonRatio[i] = String.valueOf(tenth.format((float) aContractsWon[i] / (float) aTotalContracts *
					(float) ONE_HUNDRED));
			aContractsMadeRatio[i] = String.valueOf(tenth.format((float) aContractsMade[i] / (float) aContractsWon[i] *
					(float) ONE_HUNDRED));
			aGamesWonRatio[i] = String.valueOf(tenth.format((float) aGamesWon[i] / (float) aTotalGames *
					(float) ONE_HUNDRED));
			aScoreIndex[i] = String.valueOf(hundreth.format((float) aScore[i] /
					((float) aTotalGames * (float) FIVE_HUNDRED)));

			calcDivByZero();
		}
	}

	private void calcDivByZero()
	{
		for (int i = 0; i < 4; i++)
		{
			// IF: the ratio calculation involves dividing by 0, just set the ratio to 0.
			if (aTotalTricks == 0)
			{
				aTricksWonRatio[i] = "--";
				aDivZero[i][0] = "";
			}
			// IF: the ratio calculation involves dividing by 0, just set the ratio to 0.
			if (aTotalContracts == 0)
			{
				aContractsWonRatio[i] = "--";
				aDivZero[i][1] = "";
			}

			// IF: the ratio calculation involves dividing by 0, just set the ratio to 0.
			if (aContractsWon[i] == 0)
			{
				aContractsMadeRatio[i] = "--";
				aDivZero[i][2] = "";
			}

			// IF: the ratio calculation involves dividing by 0, just set the ratio to 0.
			if (aTotalGames == 0)
			{
				aGamesWonRatio[i] = "--";
				aDivZero[i][3] = "";
			}

			// IF: the ratio calculation involves dividing by 0, just set the ratio to 0.
			if (aTotalGames == 0)
			{
				aScoreIndex[i] = "--";
				aDivZero[i][4] = "";
			}
		}
	}

	/**
	 * Prints the statistics.
	 */
	public void printStatistics()
	{
		// Calculates the ratios of statistics before it prints them.
		calculateStatistics();

		// Prints header.
		System.out.println("\n\n");
		System.out.println("\n \nGames Played: " + aTotalGames);
		System.out.println("\t\tTrick \t Cont \t Made \t Game \t Score");

		// Prints a line of statistics for each player in Team 1.
		for (int i = 0; i < 4; i += 2)
		{
			System.out.print(aNames[i] + ": \t");
			System.out.print(aTricksWonRatio[i] + aDivZero[i][0]);
			System.out.print("\t" + aContractsWonRatio[i] + aDivZero[i][1]);
			System.out.print("\t" + aContractsMadeRatio[i] + aDivZero[i][2]);
			System.out.print("\t" + aGamesWonRatio[i] + aDivZero[i][3]);
			System.out.print("\t" + aScoreIndex[i] + "\n");
		}

		// Prints a line of statistics for each player in Team 2.
		for (int i = 1; i < 4; i += 2)
		{
			System.out.print(aNames[i] + ": \t");
			System.out.print(aTricksWonRatio[i] + aDivZero[i][0]);
			System.out.print("\t" + aContractsWonRatio[i] + aDivZero[i][1]);
			System.out.print("\t" + aContractsMadeRatio[i] + aDivZero[i][2]);
			System.out.print("\t" + aGamesWonRatio[i] + aDivZero[i][3]);
			System.out.print("\t" + aScoreIndex[i] + "\n");
		}
	}
		
	/**
	 * Prints the statistics to a file defined by GUIListener class.
	 */
	public void printStatisticsFile()
	{
		calculateStatistics();
		
		try
		{
			// Append to the beginning of the logger file.
			aStream = new FileWriter(aFileLocation, false);
			BufferedWriter statsOut = new BufferedWriter(aStream);

			// Prints header.
			statsOut.write("Games Played: " + aTotalGames + "\n");
			statsOut.write("\t\tTrick \t Cont \t Made \t Game \t Score\n");

			// Prints a line of statistics for each player in Team 1.
			for (int i = 0; i < 4; i += 2)
			{
				statsOut.write(aNames[i] + ":");
				statsOut.write("\t" + aTricksWonRatio[i] + aDivZero[i][0]);
				statsOut.write("\t" + aContractsWonRatio[i] + aDivZero[i][1]);
				statsOut.write("\t" + aContractsMadeRatio[i] + aDivZero[i][2]);
				statsOut.write("\t" + aGamesWonRatio[i] + aDivZero[i][3]);
				statsOut.write("\t" + aScoreIndex[i] + "\n");
			}

			// Prints a line of statistics for each player in Team 2.
			for (int i = 1; i < 4; i += 2)
			{
				statsOut.write(aNames[i] + ":");
				statsOut.write("\t" + aTricksWonRatio[i] + aDivZero[i][0]);
				statsOut.write("\t" + aContractsWonRatio[i] + aDivZero[i][1]);
				statsOut.write("\t" + aContractsMadeRatio[i] + aDivZero[i][2]);
				statsOut.write("\t" + aGamesWonRatio[i] + aDivZero[i][3]);
				statsOut.write("\t" + aScoreIndex[i] + "\n");
			}

			statsOut.close();
		}
		catch (IOException pException)
		{
			System.out.println("Cannot write statistics to a file.");
		}

	}

	@Override
	/**
	 * When an ObservableStat is changed, this method is called. It stores the ObservableStat 
	 * in the field specified by parameter pToUpdate.
	 * @param pObj
	 * 				The ObservableStat that just changed.
	 * @param pToUpdate
	 * 				A String denoting which field in which to store the ObservableStat.
	 * @pre assert that pToUpdate is of the object array type.
	 */
	public void update(Observable pObj, Object pToUpdate)
	{
		// Checks that the object types are correct.
		assert pObj.getClass().equals(GameEngine.class);
		assert pToUpdate.getClass().equals(Object[].class);

		aTmp = (Object[]) pToUpdate;
		
		// If specified, updates Tricks Won.
		if (aTmp[0].equals("aNamesO"))
		{
			aNames = (String[]) aTmp[1];
		}
		
		// If specified, updates Tricks Won.
		if (aTmp[0].equals("aTricksWonO"))
		{
			aTricksWon = (int[]) aTmp[1];
		}

		// If specified, updates Contracts Won.
		else if (aTmp[0].equals("aContractsWonO"))
		{
			aContractsWon = (int[]) aTmp[1];
		}

		// If specified, updates Contracts Made.
		else if (aTmp[0].equals("aContractsMadeO"))
		{
			aContractsMade = (int[]) aTmp[1];
		}

		// If specified, updates Games Won.
		else if (aTmp[0].equals("aGamesWonO"))
		{
			aGamesWon = (int[]) aTmp[1];
			printStatisticsFile();
		}

		// If specified, updates the Score.
		else if (aTmp[0].equals("aScoreO"))
		{
			aScore = (int[]) aTmp[1];
		}
	}

	/**
	 * Writes the string which is passed to a file. Before printing ensures that proper file handling is taking place
	 * and catches any errors. Prints to the top of the file.
	 * 
	 * @param pFileText
	 *            - The string which is printed to the stats file.
	 */
	public void printToFile(String pFileText)
	{
		try
		{
			// Append to the beginning of the statistics file.
			aStream = new FileWriter(aFileLocation, true);
			BufferedWriter statsOut = new BufferedWriter(aStream);
			statsOut.write(pFileText);
			statsOut.close();
		}
		catch (IOException e)
		{
			System.exit(0);
		}
	}
	/**
	 * Returns the current score of the game.
	 * @return - array of integers representing score of each player.
	 */
	public int[] getScore()
	{
		return aScore;
	}

	/**
	 * Sets the output file.
	 * @param pLocation
	 * 		The new location of the file.
	 * @throws IOException
	 * 		If the move failed.
	 */
	public void moveFileLocation(String pLocation) throws IOException
	{
		// Read from the old logger file
		File f = new File(aFileLocation);
		Scanner oldFile = null;
		if (!f.exists())
		{
			f.createNewFile();
		}
		oldFile = new Scanner(f);
		
		// Append to the beginning of the logger file.
		FileWriter fStream = new FileWriter(pLocation, true);
		BufferedWriter newFile = new BufferedWriter(fStream);

		// Writes everything from the old to the new logger file.
		while (oldFile.hasNextLine())
		{
			newFile.write(oldFile.nextLine());
			newFile.write("\n");
		}
		
		// Close the filestreams
		oldFile.close();
		newFile.close();
		
		// Deletes the old log-file
		f.delete();
		
		aFileLocation = pLocation;
	}
	
	/**
	 * Getter for the output file location.
	 * @return
	 * 		the file location.
	 */
	public String getFileLocation()
	{
		return aFileLocation;
	}
}
