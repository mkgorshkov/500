package comp303.fivehundred.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

import org.slf4j.LoggerFactory;

/**
 * A Logger(observer) to keep update based on which fields are changed with those being observed.
 * 
 * @author Maxim Gorshkov
 * 
 */
public class Logger implements Observer
{
	
	// Magic Numbers
	// Total number of tricks that are playable during the game.
	private static final int NUMTRICKS = 10;
	
	// Initialises boolean passed from GameEngine to find out whether or not we actually want to log.
	private boolean aLoggingEnabled;

	// Initialises the SLF4J logging framework.
	private org.slf4j.Logger aLog = LoggerFactory.getLogger(Logger.class);
	
	// Initialises the default location and filename for the log file.
	private String aFileLocation = "miscFiles/transcript.log";
	
	// Initialises the short logger filename and location
	private String aShortLog = "miscFiles/log.txt";

	// Initialises the file-writer for log file.
	private FileWriter aFstream;
	
	// Each Player is stored in aPlayers at an index between 0 and 3. This index represents the player.
	private ArrayList<String> aPlayers = new ArrayList<String>();
	// Stores the index of the dealer.
	private int aDealerIndex;
	// Stores the index of the leader.
	private int aLeaderIndex;
	// Stores the index of the contract winner.
	private int aContractWinner;
	// Stores the contract to the current trick.
	private Bid aContract;
	// Stores the widow in the current bids.
	private Hand aWidow;
	// Stores whether or not the game is over (true = game over).
	private boolean aGameOver = true;
	// Each Hand is stored in aHands at an index between 0 and 3. This Hand belongs to the Player with that index.
	private ArrayList<Hand> aHands;
	// Stores current scores of the round.
	private int[] aScore = { 0, 0 };
	// Stores scores of the current game.
	private int[] aTotalScore = { 0, 0 };
	// Stores the current bids to the current round.
	private Bid[] aBids;
	// Stores the current trick.
	private Trick aTrick;
	
	// Temporary object array to hold the current fields being logged.
	private Object[] aTmp = new Object[2];

	// Internal variables to track games and tricks.
	// Current number of games played.
	private int aTotalGames = 0;
	// Current number of tricks.
	private int aNumTricks = 0;
	// List of cards that are to be discarded.
	private CardList aDiscards;
	// List of cards that were played.
	private CardList aCardsPlayed;

	/**
	 * Constructor for the logger.
	 * 
	 * @param pLoggerOn - Defines whether the program should be logging.
	 * 
	 */
	public Logger(boolean pLoggerOn)
	{
		File f = new File(aShortLog);
		f.delete();
		
		// Sets whether or not logging is on.
		aLoggingEnabled = pLoggerOn;
		
		aPlayers.add("");
		aPlayers.add("");
		aPlayers.add("");
		aPlayers.add("");
		
	}

	/**
	 * Logs the information required at the appropriate step.
	 * 
	 * @param pToUpdate
	 *            - Object which contains the string to the field that we need to identify where changes were made.
	 */
	@SuppressWarnings("unchecked")
	public void logToSLF(Object pToUpdate)
	{
		
		// If specified, updates Tricks Won.
		if (aTmp[0].equals("aNamesL"))
		{
		}
		
		// Checks to see if it's the first trick and if the previous game is over to
		// log that it's a new game starting.
		else if (aTmp[0].equals("aGameOverL"))
		{
			if(aTotalGames == 0 && aGameOver)
			{
				aLog.info("============================ NEW GAME ============================ ");
				printFullLog("============================ NEW GAME ============================ ");
				printShortForm("New game was started.");
			}
			// Resets the score of the game.
			aTotalScore[0] = 0;
			aTotalScore[1] = 0;
			// Increments the number of games played.
			aTotalGames++;
			// Resets the number of tricks.
			aNumTricks = 1;

		}
		else if (aTmp[0].equals("aHandsL"))
		{

			// Logs that it's a new round to the game.
			aLog.info("Game initized. Dealer: " + aPlayers.get(aDealerIndex));
			printFullLog("Game initized. Dealer: " + aPlayers.get(aDealerIndex));
			
			// As long as we're at the first trick, we can log that a new round has started.
			if (aNumTricks == 1)
			{
				aLog.info("============================ NEW ROUND ============================ ");
				printFullLog("============================ NEW ROUND ============================ ");
				printShortForm("New round was started.");
			}
			aLog.info("******************* NEW DEAL ******************");
			printFullLog("******************* NEW DEAL ******************");
			printShortForm("New hands were dealt.");

			// Puts the current hands into Logger variable to use.
			aHands = (ArrayList<Hand>) aTmp[1];

			// Logs that the cards are dealt.
			aLog.info("Players dealt cards.");
			printFullLog("Players dealt cards.");

			// Logs what cards everyone has.
			for (int i = aDealerIndex; i < aDealerIndex + 4; i++)
			{
				aLog.info(aPlayers.get(i % 4) + " cards: " + aHands.get(i % 4).toString());
				printFullLog(aPlayers.get(i % 4) + " cards: " + aHands.get(i % 4).toString());
			}

		}
		
		else if (aTmp[0].equals("aPlayersL"))
		{
			aPlayers = (ArrayList<String>) aTmp[1];
		}
		
		else if (aTmp[0].equals("aBidsL"))
		{
			// Puts the current bids into Logger variable to use.
			aBids = (Bid[]) aTmp[1];

			// Goes through and logs each of the players' bids.
			for (int i = aDealerIndex + 1; i < (aDealerIndex + 1) + 4; i++)
			{
				aLog.info(aPlayers.get(i % 4) + " cards: " + aHands.get(i % 4) + " Bid: "+ 
						aBids[(i - (aDealerIndex + 1)) % 4]);
				printFullLog(aPlayers.get(i % 4) + " cards: " + aHands.get(i % 4) + " Bid: "+ 
						aBids[(i - (aDealerIndex + 1)) % 4]);
			}

			// Logs the winner of the bid.
			aLog.info(aPlayers.get(aContractWinner) + " has the contract of " + aContract.toString());
			printFullLog(aPlayers.get(aContractWinner) + " has the contract of " + aContract.toString());

		}
		else if (aTmp[0].equals("aWidowL"))
		{
			// Casts the widow from the GameEngine into a logger local variable for use within Observer.
			aWidow = (Hand) aTmp[1];
			
			// Logs what the widow contains.
			aLog.info("The widow contains: " + aWidow.toString());
			printFullLog("The widow contains: " + aWidow.toString());
		}
		else if (aTmp[0].equals("aExchangeL"))
		{
			// Puts the current hands into Logger variable to use within the Observer.
			aHands = (ArrayList<Hand>) aTmp[1];

			// Logs which cards were discarded by the contract winner.
			aLog.info(aPlayers.get(aContractWinner) + " discards " + aDiscards.toString());
			printFullLog(aPlayers.get(aContractWinner) + " discards " + aDiscards.toString());
			// Logs which cards are left in winners hand.
			aLog.info(aPlayers.get(aContractWinner) + " cards: " + aHands.get(aLeaderIndex).toString());
			printFullLog(aPlayers.get(aContractWinner) + " cards: " + aHands.get(aLeaderIndex).toString());
		}
		else if (aTmp[0].equals("aTrickL"))
		{
			// Puts the current Trick into a logger variable for use within the Observer
			aTrick = (Trick) aTmp[1];
			// Logs the number of tricks and increments the number of tricks.
			// IF: The number of tricks is less than 10, logs as-is.
			if(aNumTricks <= NUMTRICKS)
			{
				aLog.info("---- TRICK " + aNumTricks + " ----");
				printFullLog("---- TRICK " + aNumTricks + " ----");
			}
			// ELSE: If the number of tricks is higher than 10, based on counting within the same game,
			// logs with %11 to preserve proper trick number.
			else
			{
				aLog.info("---- TRICK " + (aNumTricks+1)%(NUMTRICKS+1) + " ----");
				printFullLog("---- TRICK " + (aNumTricks+1)%(NUMTRICKS+1) + " ----");
			}
			aNumTricks++;

			
			// Iterates through the cards that were played by each player.
			Iterator<Card> trickIterator = aCardsPlayed.iterator();

			for (int i = aLeaderIndex; i < aLeaderIndex + 4; i++)
			{
				Card nextCard = trickIterator.next();
				
				aLog.info(aPlayers.get(i % 4) + " cards: " + aHands.get(i % 4).toString() + " plays " +
						nextCard);
				printFullLog(aPlayers.get(i % 4) + " cards: " + aHands.get(i % 4).toString() + " plays " +
						nextCard);
			}

			// Logs which player won the trick.
			aLog.info(aPlayers.get((aLeaderIndex + aTrick.winnerIndex()) % 4) + " wins the trick");
			printFullLog(aPlayers.get((aLeaderIndex + aTrick.winnerIndex()) % 4) + " wins the trick");
			
		}
		else if (aTmp[0].equals("aDealerIndexL"))
		{
			// Updates the dealer to the current value through GameEngine.
			aDealerIndex = (Integer) aTmp[1];
			printShortForm("New dealer: "+ aPlayers.get(aDealerIndex));
		}
		else if (aTmp[0].equals("aLeaderIndexL"))
		{
			// Updates the leader to the current value through GameEngine.
			aLeaderIndex = (Integer) aTmp[1];
			printShortForm("New leader: "+ aPlayers.get(aLeaderIndex));
		}
		else if (aTmp[0].equals("aContractWinnerL"))
		{
			// Updates the the contract winner to the current value through GameEngine.
			aContractWinner = (Integer) aTmp[1];
			printShortForm("Contract winner: "+ aPlayers.get(aContractWinner));
		}
		else if (aTmp[0].equals("aContractL"))
		{
			// Updates the contract to the current value through GameEngine.
			aContract = (Bid) aTmp[1];
			printShortForm("New contract: "+ aContract.toString());
		}
		else if (aTmp[0].equals("aDiscardsL"))
		{
			// Updates the cards that are being discarded after the contract winner chooses with widow.
			aDiscards = (CardList) aTmp[1];
		}
		else if (aTmp[0].equals("aCardsPlayedL"))
		{
			// Updates the the CardList with those cards that were played from GameEngine.
			aCardsPlayed = (CardList) aTmp[1];
		}
		else if (aTmp[0].equals("aPassedL"))
		{
			// Logs that all players have passed.
			aLog.info("All of the players passed.");
			printFullLog("All of the players passed.");
			printShortForm("All players have passed.");
		}
		else if (aTmp[0].equals("aEndScoreL"))
		{
			// Logs the score at the end of each game. IF: Not the end of the first game, no logging occurs.
			if (aGameOver)
			{
				// Updates the score of the end of the game.
				aTotalScore = (int[]) aTmp[1];

				aLog.info("============================ END GAME " + aTotalGames + " ============================ ");
				printFullLog("============================ END GAME " + aTotalGames + " ============================ ");
				
				// Logs the players final scores
				aLog.info(aPlayers.get(0) + " and " + aPlayers.get(2)+
						" total score: " + aTotalScore[0]);
				printFullLog(aPlayers.get(0) + " and " + aPlayers.get(2)+
						" total score: " + aTotalScore[0]);
				aLog.info(aPlayers.get(1) + " and " + aPlayers.get(3)+
						" total score: " + aTotalScore[1]);
				printFullLog(aPlayers.get(1) + " and " + aPlayers.get(3)+
						" total score: " + aTotalScore[1]);
			}
		}
		else if (aTmp[0].equals("aCrtScoreL"))
		{
			// Updates the current score throughout the game
			aScore = (int[]) aTmp[1];

			// Sets temporary variables with current scores and logs the current score as necessary.
			aLog.info(aPlayers.get(0) + " and " + aPlayers.get(2)+
					" round score: " + aScore[0]);
			printFullLog(aPlayers.get(0) + " and " + aPlayers.get(2)+
					" round score: " + aScore[0]);
			aLog.info(aPlayers.get(1) + " and " + aPlayers.get(3)+
					" round score: " + aScore[1]);
			printFullLog(aPlayers.get(1) + " and " + aPlayers.get(3)+
					" round score: " + aScore[1]);

		}

	}

	@Override
	/**
	 * When an LoggerObserver is changed, this method is called. It stores the LoggerObserver 
	 * in the field specified by parameter pToUpdate.
	 * @param pObj
	 * 				The ObservableStat that just changed.
	 * @param pToUpdate
	 * 				A String denoting which field in which to store the LoggerObserver.
	 */
	public void update(Observable pObj, Object pToUpdate)
	{
		
		// Checks that the parameter passed is an array of objects
		assert pToUpdate instanceof Object[];
		// aPassed should now contain the object that was being passed - the KEY.
		aTmp = (Object[]) pToUpdate;
		// Ensures that the first entry is also the String containing which field to store in observer
		assert aTmp[0] instanceof String;

		// As long as logging enabled, logs changes within specified fields.
		if (aLoggingEnabled)
		{
			logToSLF(pToUpdate);
		}
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
			Scanner logIn = new Scanner(f);
			
			// Append to the beginning of the logger file.
			aFstream = new FileWriter(pLocation, true);
			BufferedWriter logOut = new BufferedWriter(aFstream);

			// Writes everything from the old to the new logger file.
			while (logIn.hasNextLine())
			{
				logOut.write(logIn.nextLine());
				logOut.write("\n");
				
			}
			
			// Close the filestreams
			logIn.close();
			logOut.close();
			
			// Deletes the old log-file
			f.delete();
			
			aFileLocation = pLocation;
			
	}
	/**
	 * Sets the location and filename to which to log the game.
	 * @param pLocation - location and filename as a string.
	 */
	public void setLogLocation(String pLocation)
	{
		aFileLocation = pLocation;
	}
	
	/**
	 * Gets the location and filename to which to log the game.
	 * @return the logfile location and filename.
	 */
	public String getLogLocation()
	{
		return aFileLocation;
	}
	
	/**
	 * Resets the short non-verbose logger file.
	 */
	public void resetShortFile()
	{
		File f = new File(aShortLog);
		f.delete();
	}
	
	/**
	 * Writes the string which is passed to a file. Before printing ensures that proper
	 * file handling is taking place and catches any errors. Prints to the top of the file.
	 * @param pFileText - The string which is printed to the log file.
	 */
	private void printFullLog(String pFileText)
	{
		try
		{
			// Append to the beginning of the log file.
			aFstream = new FileWriter(aFileLocation, true);
			BufferedWriter out = new BufferedWriter(aFstream);
			out.write(pFileText);
			out.write("\n");
			out.close();
		}
		catch (IOException e)
		{
			System.exit(0);
		}
	}
	
	/**
	 * Writes only the most important information pertaining to the game, not the full, verbose
	 * form as printFullLog().
	 * @pre pFileText - String input to write to a file.
	 */
	private void printShortForm(String pFileText)
	{
		try
		{
			// Append to the beginning of the log file.
			aFstream = new FileWriter(aShortLog, true);
			BufferedWriter out = new BufferedWriter(aFstream);
			out.write(pFileText);
			out.write("\n");
			out.close();
		}
		catch (IOException e)
		{
			System.exit(0);
		}
	}

}

