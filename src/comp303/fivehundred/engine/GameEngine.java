/**
 * @author Dashiel Siegel, James McCorriston
 * @author Maxim Gorshkov (Logger)
 */
package comp303.fivehundred.engine;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import comp303.fivehundred.ai.Player;
import comp303.fivehundred.ai.BasicRobot;
import comp303.fivehundred.ai.AdvancedRobot;
import comp303.fivehundred.ai.HumanPlayer;
import comp303.fivehundred.gui.FHGUI;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Deck;

/**
 * The GameEngine class. Stand-in for GUI. Constructed with 4 PlayerConsolidaters as parameters.
 */
public class GameEngine extends Observable
{

	// Magic Numbers
	private static final int HANDSIZE = 10;
	private static final int WIDOW_SIZE = 6;
	private static final int SCORELIMIT = 500;
	private static final int WINWORTH = 10;

	// Each Player is stored in aPlayers at an index between 0 and 3. This index represents the player.
	private ArrayList<Player> aPlayers = new ArrayList<Player>();
	// Each Hand is stored in aHands at an index between 0 and 3. This Hand belongs to the Player with that index.
	private ArrayList<Hand> aHands = new ArrayList<Hand>();
	// Each Team has a number of trick wins that is stored in aTrickWins between index 0 and 1.
	private int[] aTrickWins = new int[2];
	// Each Team has a number of game wins that is stored in aGameWins between index 0 and 1.
	private int[] aGameWins = { 0, 0 };
	// Each Team has a score that is stored in aScore between index 0 and 1.
	private int[] aScore = new int[2];
	// Stores the index of the leader
	private int aLeaderIndex = 0;
	// Stores the index of the dealer
	private int aDealerIndex = 0;
	// Stores the index of the contract winner.
	private int aContractWinner = 0;
	// The player who will make automated decision for the human
	private Player aAdvancedSupport = new AdvancedRobot();
	//A boolean representing whether the game is in open hand mode or not, default is closed hands.
	private boolean aOpen = false;

	// Initializes the deck of cards.
	private Deck aDeck = new Deck();
	// Initializes the widow.
	private Hand aWidow = new Hand();

	//Stores past bids in a round of bidding.
	private ArrayList<Bid> aBids = new ArrayList<Bid>(); 
	//Stores the winning bid of a round of bidding.
	private Bid aContract; 
	//Stores the current trick being played. Reset every trick.
	private Trick aTrick;

	// Initialises the integer arrays that store the statistics relevant to GameStatistics.
	private int[] aTricksWonO = {0, 0, 0, 0};
	private int[] aContractsWonO = {0, 0, 0, 0};
	private int[] aContractsMadeO = {0, 0, 0, 0};
	private int[] aGamesWonO = {0, 0, 0, 0};
	private int[] aScoreO = {0, 0, 0, 0};
	
	// Initializes the boolean that tracks if the game is over.
	private boolean aGameOver = false;

	/**
	 * Constructor for GameEngine. Creates 4 players and 4 hands.
	 * 
	 * @param pInteractive
	 *            = A boolean indicating whether a human is playing (true) or if the game is fully automatic (false).
	 * */
	public GameEngine(boolean pInteractive)
	{
		//Adds the support advanced robot as an observer of the game engine
		addObserver(aAdvancedSupport);
		
		//Sets the advanced robot's index to be zero
		aAdvancedSupport.setIndex(0);
		
		Player playerA;
		Player playerB;
		Player playerC;
		Player playerD;
		// IF: this game is being played with a human,
		if (pInteractive)
		{
			playerA = new HumanPlayer();
			playerA.setName("Human 0");
			aPlayers.add(playerA);
		}
		//ELSE: the game is fully automated
		else
		{
			// Names and adds the first robot player.
			playerA = new AdvancedRobot();
			playerA.setName("Advanced 0");
			aPlayers.add(playerA);
		}
		
		// Names and adds the rest of the players.
		playerB = new BasicRobot();
		playerB.setName("Basic 1");
		aPlayers.add(playerB);
		
		playerC = new AdvancedRobot();
		playerC.setName("Advanced 2");
		aPlayers.add(playerC);
		
		playerD = new BasicRobot();
		playerD.setName("Basic 3");
		aPlayers.add(playerD);

		setPlayerIndices();
		
		//Fills aHands.
		aHands.add(new Hand());
		aHands.add(new Hand());
		aHands.add(new Hand());
		aHands.add(new Hand());
		
	}

	/**
	 * Resets the game after it finishes.
	 */
	public void resetGame()
	{
		// Resets the dealer.
		aDealerIndex = (int) (Math.random() * 4);
		
		// Logs the new dealer.
		setupNotify("aDealerIndexL", aDealerIndex);
		// Logs that the game is over. Needs to be after new dealer is chosen to display new dealer.
		setupNotify("aGameOverL", aGameOver);				
		// Resets the round (see resetRound() for details)
		newRound(false);
		// Resets the score.
		for (int i = 0; i < 2; i++)
		{
			aScore[i] = 0;
		}
		// Resets if the game is over.
		aGameOver = false;
		
		//Notify FHGUI
		String[] players = {aPlayers.get(0).getName(), aPlayers.get(1).getName(), aPlayers.get(2).getName(),
				aPlayers.get(3).getName(), aPlayers.get(0).getDifficulty(), aPlayers.get(1).getDifficulty(),
				aPlayers.get(2).getDifficulty(), aPlayers.get(3).getDifficulty()};

		ArrayList<String> playerNames = new ArrayList<String>();
		for(int i = 0; i < 4; i++)
		{
			playerNames.add(players[i]);
		}
		
		setupNotify("aPlayersL", playerNames); 
		setupNotify("newGameG", players);
	}

	/**
	 * Starts a new round. 
	 * 
	 * @param pDealerSwitch
	 *            boolean that toggles if the dealer changes
	 */
	public void newRound(boolean pDealerSwitch)
	{
		// Resets the Deck.
		aDeck.shuffle();
		// Resets the Widow.
		aWidow = new Hand();

		// Resets the Hands.
		for (int i = 0; i < 4; i++)
		{
			aHands.set(i, new Hand());
		}

		// Resets the list of bids.
		aBids = new ArrayList<Bid>();
		// Resets the current contract.
		aContract = null;
		
		// Resets the current trick.
		aTrick = null;
		// Resets the number of trick wins per team.
		aTrickWins[0] = 0;
		aTrickWins[1] = 0;

		//IF: the dealer needs to be switched, switches him.
		if (pDealerSwitch)
		{
			// Sets dealer to the next player
			aDealerIndex++;
			aDealerIndex = aDealerIndex % 4;
			// Logs that the dealer changed.
			setupNotify("aDealerIndexL", aDealerIndex);
			setupNotify("aDealerIndexG", aDealerIndex);
		}
		
		setupNotify("newRoundG", new Integer(aDealerIndex));
	}

	/**
	 * Deals 10 cards each to the players and 6 cards to the widow.
	 */
	public void deal()
	{
		// Loops while the Hands have not been filled: deals each player one card.
		for (int i = 0; i < HANDSIZE; i++)
		{
			// Loops while players still need to be dealt cards, starting left of the dealer and rotating clockwise.
			for (int j = aDealerIndex + 1; j < aDealerIndex + 1 + 4; j++)
			{
				//Deals the player a card.
				aHands.get(j % 4).add(aDeck.draw());
			}
		}

		// For the remaining 6 cards: Draws them from the deck and places them into the widow.
		for (int i = 0; i < WIDOW_SIZE; i++)
		{
			aWidow.add(aDeck.draw());
		}

		// Sends the current hands and widow to the Logger.
		setupNotify("aHandsL", aHands);
		setupNotify("aWidowL", aWidow);
		setupNotify("handsG", null);
	}

	/**
	 * Handles the bidding stage.
	 * 
	 * @pre Each of the four players' hands have 10 cards in them.
	 * @return True if a human player made a bid, false if all bids were made by AIs.
	 */
	public boolean bid()
	{
		//Asserts that all players have the proper number of cards in their hand.
		assert aHands.get(0).size() == HANDSIZE;
		assert aHands.get(1).size() == HANDSIZE;
		assert aHands.get(2).size() == HANDSIZE;
		assert aHands.get(3).size() == HANDSIZE;

		Bid newBid;

		// Bids are made in order starting with the player to the left of the dealer.
		for (int i = aDealerIndex + 1; i < aDealerIndex + 4 + 1; i++)
		{
			if (aPlayers.get(i % 4).isHuman())
			{
				// Signals that a human player has made a bid, returns out of GameEngine.
				setupNotify("humanBidG", aBids.toArray(new Bid[aBids.size()]));
				return true;
			}
			else
			{
				// Constructs a bid for the current AI player and adds it to aBids.
				newBid = aPlayers.get(i % 4).selectBid(aBids.toArray(new Bid[aBids.size()]), aHands.get(i % 4));
				aBids.add(newBid);
			}
		}

		// Finds the highest non-passing bid. Uses it to set up the first trick of the contract.
		aContract = Bid.max(aBids.toArray(new Bid[aBids.size()]));
		
		// Sets up the first trick, the leader index and the contract winner index for the
		// new round.
		setupRound();

		// Signals that all 4 bids were made by AI players.
		return false;
	}

	/**
	 * This method adds the bid made by the human player to the current list of bids and completes the round of AI
	 * bidding if necessary.
	 * 
	 * @param pPlayerBid
	 *            = The bid made by the human player.
	 */
	public void bid(Bid pPlayerBid)
	{
		// Adds the human players bid to the list of bids.
		aBids.add(pPlayerBid);

		// Completes the round of AI bidding if necessary. Starts with the first robot after human
		// and continues until the list of bids has size 4. The number of loops = 4 - aBids.size().
		Bid newBid;
		for (int i = aBids.size() + aDealerIndex + 1; i < aDealerIndex + 1 + 4; i++)
		{
			// Constructs a bid for the current AI player and adds it to aBids.
			newBid = aPlayers.get(i%4).selectBid(aBids.toArray(new Bid[aBids.size()]), aHands.get(i%4));
			aBids.add(newBid);
		}

		// Finds the highest non-passing bid. Uses it to set up the first trick of the contract
		aContract = Bid.max(aBids.toArray(new Bid[aBids.size()]));
		
		// Sets up the first trick, the leader index and the contract winner index for the
		// new round.
		setupRound();
	}
	
	/**
	 * This method makes a bid using an advanced robot, on the part of the human.
	 */
	public void supportBid()
	{
		//Asserting the human hand has 10 cards
		assert aHands.get(0).size() == HANDSIZE;
		
		//Making a new bid on the part of the human
		Bid newBid = aAdvancedSupport.selectBid(aBids.toArray(new Bid[aBids.size()]), aHands.get(0));
		
		//Passing the bid to the human bid method
		bid(newBid);
	}

	/**
	 * Makes the contract winner exchange his cards. 
	 */
	public void exchange()
	{
		CardList toDiscard = new CardList();

		// The leader chooses 6 cards to discard
		toDiscard = aPlayers.get(aContractWinner).selectCardsToDiscard(aBids.toArray(new Bid[aBids.size()]),
				getRoundIndex(aContractWinner), aHands.get(aContractWinner));
		
		// For each of the 6 cards to discard:
		for (Card w : toDiscard)
		{
			//Moves the card from the Leader's hand to the Widow.
			aWidow.add(w);
			//Removes the card from the Leader's hand.
			aHands.get(aContractWinner).remove(w);
		}

		// Log which cards got discarded and which cards are left in players' hands.
		setupNotify("aDiscardsL", toDiscard);
		setupNotify("aExchangeL", aHands);
		
		//Updates the advanced AI players.
		setupNotify("Player", "ignore");
		setupNotify("handsG", "ignore");
		
		//Updates FHGUI
		setupNotify("robotExchangeG", aHands.toArray());
	}

	/**
	 * Makes the contract winner exchange his cards. Adds 6 from widow to contract winners hand and they discard 6 cards
	 * from their hand.
	 * 
	 * @param pToDiscard
	 *            = A CardList representing the cards that the human Player chose to discard.
	 */
	public void exchange(CardList pToDiscard)
	{
		//For each card the Human wants to discard:
		for (Card w : pToDiscard)
		{
			// Adds the discarded card to the widow.
			aWidow.add(w);
			// Removes the card from the Human's hand (the human is always at index 0)
			aHands.get(0).remove(w);
		}

		// Logs which cards got discarded and which cards are left in players' hands.
		setupNotify("aDiscardsL", pToDiscard);
		setupNotify("aExchangeL", aHands);
		
		//Updates the advanced AI players.
		setupNotify("Player", "ignore");
	
		//Updates the Card Panels
		setupNotify("handsG", "ignore");
	
		//Updates FHGUI
		setupNotify("contractMadeG", "ignore");

	}
	
	/**
	 * This method performs exchange using an advanced robot on the part of the human.
	 */
	public void supportExchange()
	{
		//Selects cards to discard
		CardList discards = aAdvancedSupport.selectCardsToDiscard(aBids.toArray(new Bid[aBids.size()]),
							getRoundIndex(aContractWinner), aHands.get(0));
		//Uses the human exchange method to make the exchange
		exchange(discards);
	}

	/**
	 * Plays the trick. 
	 * 
	 * @return Returns true if a human player played a card.
	 */
	public boolean playTrick()
	{		
		// Resets the trick object.
		aTrick = new Trick(aContract);
		
		Card cardToPlay;
		
		// For each player, starting with the Leader and rotating clockwise,
		for (int i = aLeaderIndex; i < aLeaderIndex + 4; i++)
		{
			//IF: the player is a human, return true and use the playTrick method for human.
			if (aPlayers.get(i % 4).isHuman())
			{
				setupNotify("humanPlayG", aTrick.clone());
				return true;
			}
			//ELSE: the player is a robot, and selects which card he wants to play
			cardToPlay = aPlayers.get(i % 4).play(aTrick, aHands.get(i % 4));

			//Plays the selected card and removes it from the player's hand.
			aTrick.add(cardToPlay);
			aHands.get(i % 4).remove(cardToPlay);
			//Updates the advanced AI players.
			setupNotify("Player", "ignore");
			setupNotify("handsG", "ignore");
		}

		//Notify FHGUI
		setupNotify("robotPlayG", aTrick);

		
		// Performs necessary logging and statistics for the current trick and then sets up
		// a new trick.
		endTrick();

		// Signals to the caller that all 4 cards were played by AI players.
		return false;
	}

	/**
	 * Plays the card chosen by the human player and completes the round of AI play if necessary.
	 * 
	 * @param pHumanPlay
	 *            = The card to be played as chosen by the human player.
	 */
	public void playTrick(Card pHumanPlay)
	{		
		// Plays the humans card.
		// Adds the human card to the trick.
		aTrick.add(pHumanPlay);
		aHands.get((aLeaderIndex + aTrick.size() - 1) % 4).remove(pHumanPlay);

		//Updating the GUI
		setupNotify("handsG", "ignore");
		
		Card cardToPlay;
		for (int i = aTrick.size(); i < 4; i++)
		{
			// Selects the card to be played by the AI player.
			cardToPlay = aPlayers.get((i + aLeaderIndex) % 4).play(aTrick, aHands.get((i + aLeaderIndex) % 4));

			// Plays the card and removes it from the hand of the player who played it.
			aTrick.add(cardToPlay);
			aHands.get((i + aLeaderIndex) % 4).remove(cardToPlay);
			//Updates the advanced AI players.
			setupNotify("Player", "ignore");
			
			//Updating the GUI
			setupNotify("handsG", "ignore");
		}		
		// Performs necessary logging and statistic for the current trick and then sets up
		// a new trick.
		endTrick();
	}
	
	/**
	 * This method selects a card to play, using an advance robot, on the part of the human.
	 */
	public void supportPlay()
	{
		//Advanced robot used to pick a card to play
		Card toPlay = aAdvancedSupport.play(aTrick, aHands.get(0));
		
		//The card to play is passed to the human play method
		playTrick(toPlay);
	}

	/**
	 * Checks if the contract winner has satisfied his contract, and awards points accordingly.
	 */
	public void computeScore()
	{

		// IF: the contract winner has satisfied his contract, awards him the contract worth.
		if (aTrickWins[aContractWinner % 2] >= aContract.getTricksBid())
		{
			aScore[aContractWinner % 2] += aContract.getScore();

			// The GameStatistics Observer logs the contract as made and keeps track of scores.
			aContractsMadeO[aContractWinner] += 1;
			setupNotify("aContractsMadeO", aContractsMadeO);
			aScoreO[aContractWinner] += aContract.getScore();
			aScoreO[(aContractWinner+2)%4] += aContract.getScore();
			setupNotify("aScoreO", aScoreO);
			
		}
		// ELSE: the contract winner has NOT satisfied his contract, changes scores accordingly
		else
		{
			//Docks the ContractWinner's team the worth of the contract.
			aScore[aContractWinner % 2] -= aContract.getScore();
			//Awards the opposite team the worth of the tricks they won.
			aScore[(aContractWinner + 1) % 2] += WINWORTH * aTrickWins[(aContractWinner + 1) % 2];

			// The GameStatistics Observer keeps track of scores.
			aScoreO[aContractWinner] -= aContract.getScore();
			aScoreO[(aContractWinner + 2) % 4] -= aContract.getScore();
			aScoreO[(aContractWinner + 1) % 4] += WINWORTH * aTrickWins[(aContractWinner + 1) % 2];
			aScoreO[(aContractWinner + 3) % 4] += WINWORTH * aTrickWins[(aContractWinner + 1) % 2];
			setupNotify("aScoreO", aScoreO);

		}

		// IF: someone's score is over 500, and he is also the leader, the game is over.
		if (aScore[aContractWinner % 2] >= SCORELIMIT)
		{
			// The GameStatistics Observer keeps track of games won.
			aGamesWonO[aContractWinner] += 1;
			aGamesWonO[(aContractWinner + 2) % 4] += 1;
			setupNotify("aGamesWonO", aGamesWonO);
			//The Game is set to over.
			aGameOver = true;
			//The field for gameWins in GameEngine is adjusted.
			aGameWins[aContractWinner % 2]++;
			// Logs the score at the end of the game.
			setupNotify("aEndScoreL", aScore);
			//Notifies the GUI
			setupNotify("gameOverG", "ignore");
		}
		else if (aScore[aContractWinner % 2] <= -SCORELIMIT)
		{
			// The GameStatistics Observer keeps track of games won.
			aGamesWonO[(aContractWinner + 1) % 4] += 1;
			aGamesWonO[(aContractWinner + 3) % 4] += 1;
			setupNotify("aGamesWonO", aGamesWonO);
			//The Game is set to over.
			aGameOver = true;
			//The field for gameWins in GameEngine is adjusted.
			aGameWins[(aContractWinner + 1) % 2]++;
			// Logs the score at the end of the game.
			setupNotify("aEndScoreL", aScore);
			//Notifies the GUI
			setupNotify("gameOverG", "ignore");
		}
		else
		{
			//Notifies the GUI
			setupNotify("betweenRoundsG", "ignore");
		}
		//Notify FHGUI
		setupNotify("robotPlay", aScore);
	}

	/**
	 * Used to determine if all players have passed.
	 * 
	 * @pre At least one bid has been made.
	 * @return True if all the players have passed, false if otherwise.
	 */
	public boolean allPasses()
	{
		//Asserts that at least one Bid has been made.
		assert aBids.size() > 0;

		//Notifying the GUI
		setupNotify("allPassesG", new Boolean(Bid.max(aBids.toArray(new Bid[aBids.size()])).isPass()));
		
		return Bid.max(aBids.toArray(new Bid[aBids.size()])).isPass();
	}

	/**
	 * @return The current list of bids made.
	 */
	public Bid[] getBids()
	{
		return aBids.toArray(new Bid[aBids.size()]);
	}

	/**
	 * @return The index of the player who made the contract (the highest bid).
	 */
	public int getContractHolderIndex()
	{
		return aContractWinner;
	}

	/**
	 * @return The list of cards in the widow.
	 * @pre aWidow.size() == 6
	 */
	public Hand getWidow()
	{
		//Asserts that the Widow is the correct size, then returns it a clone of it.
		assert aWidow.size() == WIDOW_SIZE;
		return aWidow.clone();
	}
	
	/**
	 * Returns the index of the player, counting from the player to the left of the dealer.
	 * 
	 * @param pIndex = the player's concrete index (i.e. 0 for human player)
	 * @return the player's index for this round (i.e. 0 for the player left of the dealer).
	 */
	public int getRoundIndex(int pIndex)
	{
		return (pIndex + 4 - aDealerIndex - 1) % 4;
	}

	/**
	 * Returns the hand of the player at the given index in the game.
	 * 
	 * @param pPlayerIndex
	 *            = the index of the player whose hand should be returned.
	 * @return The hand of the player of index pPlayerIndex.
	 */
	public Hand getHand(int pPlayerIndex)
	{
		return aHands.get(pPlayerIndex).clone();
	}

	/**
	 * Returns the state of aGameOver.
	 * 
	 * @return aGameOver the state of aGameOver
	 */
	public boolean isGameOver()
	{
		return aGameOver;
	}

	/**
	 * Returns the score of a player given the player index.
	 * 
	 * @param pPlayerIndex
	 *            - Index of the player of which you would like to know the score.
	 * @return The score of player at pPlayerIndex.
	 */
	public int getScore(int pPlayerIndex)
	{
		return aScore[pPlayerIndex % 2];
	}
	
	/**
	 * Returns the dealer index.
	 * 
	 * @return aDealerIndex.
	 */
	public int getDealerIndex()
	{
		return aDealerIndex;
	}
	
	/**
	 * Returns the leader index.
	 * 
	 * @return aLeaderIndex.
	 */
	public int getLeaderIndex()
	{
		return aLeaderIndex;
	}
	
	/**
	 * Returns the current trick.
	 * 
	 * @return The current Trick being played
	 */
	public Trick getTrick()
	{
		return aTrick;
	}
	
	/**
	 * Returns the trump suit.
	 * 
	 * @return The current trump suit.
	 * @pre There is a trick
	 */
	public Suit getTrumpSuit()
	{
		assert aTrick != null;
		return aTrick.getTrumpSuit();
	}
	
	/**
	 * Returns the number of tricks won by the team of the player passed as a parameter.
	 * 
	 * @param pPlayerIndex
	 *            - Index of the player whose team's tricks won is returned..
	 * @return The number of tricks win by the player's team.
	 */
	public int getTricksWon(int pPlayerIndex)
	{
		return aTrickWins[pPlayerIndex%2];
	}
	
	/**
	 * This method is called when a round of bidding ends with at least one non-passing bid. This method creates the
	 * first trick for the new round and sets the index of the contract winner and the index of the leader for the first
	 * trick.
	 * 
	 * @pre aContract is not null.
	 */
	private void setupRound()
	{
		assert aContract != null;

		if (!aContract.isPass())
		{
			// Creates the first trick.
			aTrick = new Trick(aContract);

			// Sets the leader and contract winner to the index of highest bidder
			for (int i = 0; i < 4; i++)
			{
				if (aBids.get(i).equals(aContract))
				{
					aLeaderIndex = (i + aDealerIndex + 1) % 4;
					aContractWinner = (i + aDealerIndex + 1) % 4;
					// For each of the 6 cards in the Widow:
					for (int j = 0; j < WIDOW_SIZE; j++)
					{
						//Moves the card from the Widow to the Leader's hand.
						aHands.get(aContractWinner).add(aWidow.getFirst());
						//Removes the card from the widow .
						aWidow.remove(aWidow.getFirst());
					}
					
					//Notifying the CardPanels
					setupNotify("handsG", "ignore");
					
					// The GameStatistics Observer tracks who won the Contract,
					aContractsWonO[aContractWinner] += 1;
					setupNotify("aContractsWonO", aContractsWonO);

					// Logs the bids that all of the players made.
					setupNotify("aContractWinnerL", aContractWinner);
					setupNotify("aContractL", aContract);
					setupNotify("aBidsL", aBids.toArray(new Bid[aBids.size()]));
				}
			}
			if(aContractWinner == 0)
			{
				setupNotify("humanExchangeG", "ignore");
			}
			else
			{
				setupNotify("contractMadeG", "ignore");
			}

		}
		//IF: Everybody has passed, print so and log it.
		else
		{
			setupNotify("aPassedL", true);
			setupNotify("aGameOverL", aGameOver);
			setupNotify("newRoundG", new Integer(aDealerIndex));
		}
	}

	/**
	 * 
	 * @pre The trick must have exactly 4 cards in it.
	 */
	private void endTrick()
	{
		//Asserts that the trick has been fully played.
		assert aTrick.size() == 4;

		// Logs what all of the players have played.
		setupNotify("aCardsPlayedL", aTrick.clone());
		
		//Notifies the GUI
		setupNotify("humanPlayG", aTrick.clone());

		// Awards a trick win to the team that played the highest card.
		aTrickWins[(aTrick.winnerIndex() + aLeaderIndex) % 2] += 1;

		// The GameStatistics Observer tracks who wins the trick,
		aTricksWonO[(aTrick.winnerIndex() + aLeaderIndex) % 4] += 1;
		setupNotify("aTricksWonO", aTricksWonO);
		
		// Logs the leader.
		setupNotify("aLeaderIndexL", aLeaderIndex);
		
		// Logs what everyone has played
		setupNotify("aTrickL", aTrick);

		// Sets the leader of the following trick to the winner of this trick. 
		aLeaderIndex = (aTrick.winnerIndex() + aLeaderIndex) % 4;
	}
	
	/**
	 * Sets the notifier to update an observer with a field and object.
	 * @param pField - Specific field to be updated in statistics observer.
	 * @param pData - Integer array containing statistics to be updated.
	 */
	private void setupNotify(String pField, Object pData)
	{
		//Initialises the Object[] to pass via notifyObservers(). 
		Object[] aToNotify = new Object[2]; 

		//Sets up the Object[] to pass via notifyObservers().
		aToNotify[0] = pField;
		aToNotify[1] = pData;
		
		//Do to notifying.
		this.setChanged();
		notifyObservers(aToNotify);
		this.clearChanged();
	}
	
	/**
	 * Updates the advanced players on the state of the game.
	 */
	private void setPlayerIndices()
	{
		for(int i = 0; i < 4; i++)
		{
			if(aPlayers.get(i).isAdvanced())
			{
				aPlayers.get(i).setIndex(i);
			}
		}
	}
	
	/**
	 * Changes the player at the specified index of the aPlayers ArrayList.
	 * @param pPlayer
	 * 		The new player to put it that array.
	 * @param pIndex
	 * 		The index of to put him in.
	 */
	public void changePlayer(Player pPlayer, int pIndex)
	{
		aPlayers.remove(pIndex);
		aPlayers.add(pIndex, pPlayer);
		setPlayerIndices();
		String[] playerNames = {aPlayers.get(0).getName(), aPlayers.get(1).getName(), aPlayers.get(2).getName(), aPlayers.get(3).getName()};
		//setupNotify("aNewPlayersL", playerNames);
		setupNotify("aNamesO", playerNames); 
	}

	/**
	 * Gets the array list of players.
	 * @return An array list storing the four players.
	 */
	public ArrayList<Player> getPlayers()
	{
		return aPlayers;
	}
	
	/**
	 * Returns the player at the specified index (not relative to the dealer, nor the leader).
	 * @param pIndex = The index of the player.
	 * @return The player at the specified index.
	 */
	public Player getPlayer(int pIndex)
	{
		return aPlayers.get(pIndex);
	}
	
	/**
	 * Adding observers with the possibility of updating them right away.
	 * @param pObserver The observer to be added to the list of observers.
	 */
	@Override
	public void addObserver(Observer pObserver)
	{
		super.addObserver(pObserver);
		if(pObserver instanceof FHGUI)
		{
			setupNotify("addCardPanelsG", "Ignore");
		}
	}
	
	/**
	 * This method notifies observers that a round has started.
	 */
	public void newRound()
	{
		setupNotify("startRoundG", "ignore");
	}
	
	/**
	 * Sets the visibility of computer hands.
	 * @param pOpen True for open hands, false for closed.
	 */
	public void setOpenHands(boolean pOpen)
	{
		aOpen = pOpen;
		setupNotify("toggleHandVisibilityG", "ignore");
	}
	
	/**
	 * Gets computer hand visibility.
	 * @return True if computer hands visible, false if not.
	 */
	public boolean getOpenHands()
	{
		return aOpen;
	}
}
