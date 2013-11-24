/**
 * @author James McCorriston, Dashiel Siegel
 */
package comp303.fivehundred.engine;

import comp303.fivehundred.ai.AdvancedRobot;
import comp303.fivehundred.ai.Player;
import comp303.fivehundred.ai.BasicRobot;
import comp303.fivehundred.ai.HumanPlayer;

/**
 * A program which calls into GameEngine to simulate games. Standin for the GUI.
 * NOTE: Logging can be turned on and off in the GameEngine class.
 * 
 */
public class Driver
{
		
	//Initializes the number of games to be played.
	public static final int NUM_GAMES = 10000; 
	
	//Initializes the number of tricks per game.
	public static final int NUM_TRICKS = 10;
	
	// Enables and disables the logging framework. (True = ON, False = OFF).
	private static final boolean ENABLE_LOGGING = false;
	
	/**
	 * Overrides the default constructor, making sure it is neither default nor public.
	 */
	protected Driver()
	{	
	}
	
	/**
	 * Loops through the games that we need to play.
	 * @param pArgs is the arguments passed when this class is executed.
	 */
	public static void main(String[] pArgs)
	{
		//Initializes the number of games that have been played thus far.
		int aGamesPlayed = 0;
	
		//Initializes if Driver is running in interactive mode. True if interactive, false if not.
		boolean interactiveMode = false;
		
		//Initializes the players.
		Player playerA;
		Player playerB;
		Player playerC;
		Player playerD;
		//This basic robot will make all moves for the human player.
		Player humanMoveMaker = new BasicRobot();
		
		if (interactiveMode)
		{
			 playerA = new HumanPlayer();
		}
		else
		{
			playerA = new BasicRobot();
		}
		playerB = new AdvancedRobot();
		playerC = new BasicRobot();
		playerD = new AdvancedRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(interactiveMode);
		
		//Constructs the GameStatistics Observer
		GameStatistics aStats = new GameStatistics();
		game.addObserver(aStats);
		
		//Constructs the Logger Observer
		Logger aLogs = new Logger(ENABLE_LOGGING);
		game.addObserver(aLogs);
		
		playerA.setName("BasicA");
		playerB.setName("AdvancedA");
		playerC.setName("BasicB");
		playerD.setName("AdvancedB");
		
		game.addObserver(playerA);
		game.addObserver(playerB);
		game.addObserver(playerC);
		game.addObserver(playerD);
		
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		
		while (aGamesPlayed < NUM_GAMES)
		{
			//Resets the game.
			game.resetGame();
			
			//Loops while the game is not over:
			while (!game.isGameOver())
			{
				//Starts a new round, switches dealer. Handles bidding.
				game.newRound(true);
				game.deal();
				if(game.bid())
				{
					//The human bids like a basic robot.
					game.bid(humanMoveMaker.selectBid(game.getBids(), game.getHand(0)));
				}
				
				//Loops while everybody has passed and no contract has been established.
				while(game.allPasses()) 
				{
					//Starts a new round but does not switch the dealer. Handles bidding.
					game.newRound(false);		
					game.deal();	
					//If the current player bidding is human:
					if(game.bid())
					{
						//The human bids like a basic robot.
						game.bid(humanMoveMaker.selectBid(game.getBids(), game.getHand(0)));
					}
				}
				
				//IF: the winner of the contract is a Human: the human exchanges his cards.

				if(game.getContractHolderIndex() == 0)
				{
					//The human exchanges cards like a basic robot.
					game.exchange(humanMoveMaker.selectCardsToDiscard(game.getBids(), 0, game.getHand(0)));
				}
				//ELSE: the winner of the contract is a Robot: the Robot exchanges his cards.
				else 
				{
					game.exchange();
				}
				
				//Plays 10 tricks.
				for (int i = 0; i < NUM_TRICKS; i++)
				{
					//Plays the trick.
					if(game.playTrick())
					{

						//The human plays like a basic robot.
						game.playTrick(humanMoveMaker.play(game.getTrick(), game.getHand(0)));
					}
				}
				
				//Computes the winner and score.
				game.computeScore();
				
			}
			//Increments the number of games played.
			aGamesPlayed++;
		}
		//Print the statistics.
		aStats.printStatistics();
	}
}
