/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.engine;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.ai.BasicRobot;
import comp303.fivehundred.ai.HumanPlayer;
import comp303.fivehundred.ai.Player;
import comp303.fivehundred.ai.RandomRobot;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;

public class TestGameEngine
{
	@Test
	public void testResetGame()
	{
		//Setting up a game that should just play 1 round.
		//Initializes the players.
		Player playerA = new BasicRobot();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		game.newRound(false);
		game.deal();
		game.bid();
		while(game.allPasses())
		{
			game.newRound(false);
			game.deal();
			game.bid();
		}
		game.exchange();
		for (int i = 0; i < 10; i++)
		{
			game.playTrick();
		}
		game.computeScore();
		
		//If the game ended after 1 round, the reset method cannot be properly tested, and therefore it is called again.
		boolean test1 = true;
		for (int i = 0; i < 2; i++)
		{
			if (game.getScore(i) >= 500 || game.getScore(i) <= -500)
			{
				test1 = false;
				break;
			}
		}
		if (test1)
		{
			assertFalse(game.isGameOver());
		}
		else
		{
			assertTrue(game.isGameOver());
			testResetGame();
		}
		//Seeing that the game actually has values for its various fields
		assertTrue(game.getWidow().size() == 6);
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 0);
		}
		assertFalse(game.getBids().length == 0);
		assertFalse(game.allPasses());
		boolean test2 = true;
		for (int i = 0; i < 4; i++)
		{
			if (!(game.getScore(i) == 0))
			{
				test2 = false;
				break;
			}
		}
		assertFalse(test2);
		
		//Resetting the game
		game.resetGame();
		
		//Seeing if the various fields were reset
		assertFalse(game.isGameOver());
		assertTrue(game.getWidow().size() == 0);
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 0);
		}
		assertTrue(game.getBids().length == 0);
		
		boolean test3 = true;
		for (int i = 0; i < 2; i++)
		{
			if (!(game.getScore(i) == 0))
			{
				test3 = false;
				break;
			}
		}
		assertTrue(test3);
	}
	
	@Test
	public void testNewRound()
	{
		//Setting up testing variables
		boolean test1;
		boolean test2;
		boolean test3;
		int[] testScores = new int[4];
		
		//Setting up a game that should be mid-play.
		//Initializes the players.
		Player playerA = new BasicRobot();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		game.newRound(false);
		game.deal();
		game.bid();
		while(game.allPasses())
		{
			game.newRound(false);
			game.deal();
			game.bid();
		}
		game.exchange();
		for (int i = 0; i < 5; i++)
		{
			game.playTrick();
		}
		
		//Seeing that the game actually has values for its various fields
		assertFalse(game.isGameOver());
		assertTrue(game.getWidow().size() == 6);
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 5);
		}
		assertFalse(game.getBids().length == 0);
		assertFalse(game.allPasses());
		test1 = true;
		for (int i = 0; i < 2; i++)
		{
			if (!(game.getScore(i) == 0))
			{
				test1 = false;
				break;
			}
		}
		assertTrue(test1);
		
		//Starting a new round without changing dealers
		game.newRound(false);
		
		//Seeing if the various fields were reset
		assertFalse(game.isGameOver());
		assertTrue(game.getWidow().size() == 0);
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 0);
		}
		assertTrue(game.getBids().length == 0);
		
		test2 = true;
		for (int i = 0; i < 2; i++)
		{
			if (!(game.getScore(i) == 0))
			{
				test2 = false;
				break;
			}
		}
		assertTrue(test2);
		
		//Setting up the round for mid-play again
		game.deal();
		game.bid();
		while(game.allPasses())
		{
			game.newRound(false);
			game.deal();
			game.bid();
		}
		game.exchange();
		for (int i = 0; i < 5; i++)
		{
			game.playTrick();
		}
		
		//Starting a new round with changing dealers
		game.newRound(true);
		
		//Seeing if everything was reset again
		assertFalse(game.isGameOver());
		assertTrue(game.getWidow().size() == 0);
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 0);
		}
		assertTrue(game.getBids().length == 0);
		
		test3 = true;
		for (int i = 0; i < 2; i++)
		{
			if (!(game.getScore(i) == 0))
			{
				test3 = false;
				break;
			}
		}
		assertTrue(test3);
		
		//Setting up a third game
		game.deal();
		game.bid();
		while(game.allPasses())
		{
			game.newRound(false);
			game.deal();
			game.bid();
		}
		game.exchange();
				
		for (int i = 0; i < 10; i++)
		{
			game.playTrick();
		}
		game.computeScore();
		
		//Recording the scores after the first round
		for (int i = 0; i < 4; i++)
		{
			testScores[i] = game.getScore(i);
		}
		
		//Resetting the round to make sure that, resetting around doesn't affect the score.
		game.newRound(false);
		
		for (int i = 0; i < 4; i++)
		{
			assertTrue(testScores[i] == game.getScore(i));
		}		
	}
	
	@Test
	public void testDeal()
	{
		//Setting up a game to the point of dealing
		//Initializes the players.
		Player playerA = new BasicRobot();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		game.newRound(false);
		game.deal();
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 10);
		}
		assertTrue(game.getWidow().size() == 6);
	}
	
	@Test
	public void testBid()
	{
		//Creating a game object
		//Initializes the players.
		Player playerA = new BasicRobot();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		
		//Limiting the number of times the game will be reset
		//Although this could cause a potential false negative, it is highly unlikely.
		for (int i = 0; i < 100; i++)
		{
			//Setting up a game to the point of bidding
			game.newRound(false);
			game.deal();
			game.bid();
			//Make sure that 4 bids are made
			assertTrue(game.getBids().length == 4);
			if (!game.allPasses())
			{
				break;
			}
			//If you bid 100 times, and they are always all passes, there is likely an error, and the test fails
			if (i == 99)
			{
				fail();
			}
		}			
	}
	
	@Test
	public void testExchange()
	{
		//Creating a tester hand/widow
		Hand test;
		
		//Setting up a game to the point of exchange the first time
		//Initializes the players.
		Player playerA = new BasicRobot();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		game.newRound(false);
		game.deal();
		game.bid();
		while(game.allPasses())
		{
			game.newRound(false);
			game.deal();
			game.bid();
		}
		test = game.getWidow();
		
		//Entering a loop that will test exchange multiple times, making sure it actually does something.
		for (int i = 0; i < 50; i++)
		{
			//Calling exchange
			game.exchange();
			//Making sure exchange leaves the hands of all players at size 10
			for (int j = 0; j < 4; j++)
			{
				assertTrue(game.getHand(j).size() == 10);
			}
			
			//Checking to see if the widow actually changed
			if (!test.equals(game.getWidow()))
			{
				break;
			}
			//If on last iteration of for loop, fail
			if (i == 49)
			{
				fail();
			}
			//resetting round for next iteration
			game.newRound(false);
			game.deal();
			game.bid();
			while(game.allPasses())
			{
				game.newRound(false);
				game.deal();
				game.bid();
			}
			test = game.getWidow();
		}
	}
	
	@Test
	public void testComputeScore()
	{
		//Setting up a tester variable
		int[] testScores = new int[4];
		
		//Setting up a game
		//Initializes the players.
		Player playerA = new BasicRobot();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		game.newRound(false);
		game.deal();
		game.bid();
		while(game.allPasses())
		{
			game.newRound(false);
			game.deal();
			game.bid();
		}
		game.exchange();
		
		//Making sure the score is zero
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getScore(i) == 0);
		}
		
		//Playing 1 round
		for (int i = 0; i < 10; i++)
		{
			game.playTrick();
		}
		game.computeScore();
		
		//Make sure the score changed properly
		if (game.getTricksWon(game.getContractHolderIndex()) >= Bid.max(game.getBids()).getTricksBid())
		{
			assertTrue(game.getScore(game.getContractHolderIndex()) == Bid.max(game.getBids()).getScore());
			assertTrue(game.getTricksWon((game.getContractHolderIndex() + 1) % 4) <= Bid.max(game.getBids()).getTricksBid());
		}
		else
		{
			assertTrue(game.getScore(game.getContractHolderIndex()) == -Bid.max(game.getBids()).getScore());
			assertTrue(game.getTricksWon((game.getContractHolderIndex() + 1) % 4) > 10 - Bid.max(game.getBids()).getTricksBid());
			assertTrue(game.getScore((game.getContractHolderIndex() + 1) % 4) == game.getTricksWon((game.getContractHolderIndex() + 1) % 4) * 10);
		}
		assertTrue(game.getTricksWon(game.getContractHolderIndex()) + game.getTricksWon((game.getContractHolderIndex() + 1) % 4) == 10);
		
		//Recording the scores after the first round
		for (int i = 0; i < 4; i++)
		{
			testScores[i] = game.getScore(i);
		}
		
		if (game.isGameOver())
		{
			//One round with nobody winning is necessary to test computeScore properly
			testComputeScore();
		}
		else
		{
			//Setting up a second round to make sure scores add properly
			game.newRound(false);
			game.deal();
			game.bid();
			while(game.allPasses())
			{
				game.newRound(false);
				game.deal();
				game.bid();
			}
			game.exchange();
			//Playing 2nd round
			for (int i = 0; i < 10; i++)
			{
				game.playTrick();
			}
			game.computeScore();
			//Make sure the score changed properly
			if (game.getTricksWon(game.getContractHolderIndex()) >= Bid.max(game.getBids()).getTricksBid())
			{
				assertTrue(game.getScore(game.getContractHolderIndex()) ==
						Bid.max(game.getBids()).getScore() + testScores[game.getContractHolderIndex()]);
				assertTrue(game.getTricksWon((game.getContractHolderIndex() + 1) % 4) <= Bid.max(game.getBids()).getTricksBid());
			}
			else
			{
				assertTrue(game.getScore(game.getContractHolderIndex()) ==
						-Bid.max(game.getBids()).getScore() + testScores[game.getContractHolderIndex()]);
				assertTrue(game.getTricksWon((game.getContractHolderIndex() + 1) % 4) > 10 - Bid.max(game.getBids()).getTricksBid());
				assertTrue(game.getScore((game.getContractHolderIndex() + 1) % 4) ==
						game.getTricksWon((game.getContractHolderIndex() + 1) % 4) * 10 + testScores[(game.getContractHolderIndex() + 1) % 4]);
			}
			assertTrue(game.getTricksWon(game.getContractHolderIndex()) + game.getTricksWon((game.getContractHolderIndex() + 1) % 4) == 10);
			
			//Making sure the game ends properly by someone losing
			while (!game.isGameOver() || !(game.getScore(game.getContractHolderIndex()) <= -500))
			{
				if (game.getScore(game.getContractHolderIndex()) >= 500)
				{
					game.resetGame();
				}
				game.newRound(false);
				game.deal();
				game.bid();
				while(game.allPasses())
				{
					game.newRound(false);
					game.deal();
					game.bid();
				}
				game.exchange();
				//Playing next round
				for (int i = 0; i < 10; i++)
				{
					game.playTrick();
				}
				game.computeScore();
			}
			//Making sure that the losing team actually lost the given round.
			assertTrue(game.getTricksWon(game.getContractHolderIndex()) < Bid.max(game.getBids()).getTricksBid());
			
			//Making sure the game ends properly by someone winning
			while (!game.isGameOver() || !(game.getScore(game.getContractHolderIndex()) >= 500))
			{
				if (game.getScore(game.getContractHolderIndex()) <= -500)
				{
					game.resetGame();
				}
				game.newRound(false);
				game.deal();
				game.bid();
				while(game.allPasses())
				{
					game.newRound(false);
					game.deal();
					game.bid();
				}
				game.exchange();
				//Playing next round
				for (int i = 0; i < 10; i++)
				{
					game.playTrick();
				}
				game.computeScore();
			}
			//Making sure that the winning team actually won the given round.
			assertTrue(game.getTricksWon(game.getContractHolderIndex()) >= Bid.max(game.getBids()).getTricksBid());
			assertFalse(game.getTricksWon((game.getContractHolderIndex() + 1) % 4) <= -500);
			
			//Making sure the game doesn't end if a team reaches 500 points without winning a contract
			while (!(game.getScore(game.getContractHolderIndex()) < 500) || !(game.getScore((game.getContractHolderIndex() + 1) % 4) >= 500) ||
					!(game.getScore(game.getContractHolderIndex()) > -500))
			{
				if (game.getScore(game.getContractHolderIndex()) <= -500 || game.getScore(game.getContractHolderIndex()) >= 500)
				{
					game.resetGame();
				}
				game.newRound(false);
				game.deal();
				game.bid();
				while(game.allPasses())
				{
					game.newRound(false);
					game.deal();
					game.bid();
				}
				game.exchange();
				//Playing next round
				for (int i = 0; i < 10; i++)
				{
					game.playTrick();
				}
				game.computeScore();
			}
			assertFalse(game.isGameOver());
		}
		
	}
	
	@Test
	public void testAllPasses()
	{
		//Setting up a game
		//Initializes the players.
		Player playerA = new BasicRobot();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		
		//Performing many iterations looking for a situation when all players will pass
		for (int i = 0; i < 1000; i++)
		{
			//Making sure allPasses() works
			game.newRound(false);
			game.deal();
			game.bid();
			if (Bid.max(game.getBids()).isPass())
			{
				assertTrue(game.allPasses());
				break;
			}
			
			//If in 1000 rounds, there is not even 1 where everyone passes, there is likely a problem
			if (i == 999)
			{
				fail();
			}
		}
	}
	
	@Test
	public void testInteractiveBid()
	{
		//Setting up a Basic Robot to fill in for the human
		Player testRobot = new BasicRobot();
		
		//Creating a game object
		//Initializes the players.
		Player playerA = new HumanPlayer();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		
		//Limiting the number of times the game will be reset
		//Although this could cause a potential false negative, it is highly unlikely.
		for (int i = 0; i < 100; i++)
		{
			//Setting up a game to the point of bidding
			game.newRound(true);
			game.deal();
			game.bid();
			game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
			//Make sure that 4 bids are made
			assertTrue(game.getBids().length == 4);
			if (!game.allPasses())
			{
				break;
			}
			//If you bid 100 times, and they are always all passes, there is likely an error, and the test fails
			if (i == 99)
			{
				fail();
			}
		}		
	}
	
	@Test
	public void testInteractiveExchange()
	{
		//Creating a tester hand/widow
		Hand test;
		
		//Setting up a Basic Robot to fill in for the human
		Player testRobot = new BasicRobot();
		
		//Setting up a game to the point of exchange the first time
		//Initializes the players.
		Player playerA = new HumanPlayer();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		game.newRound(false);
		game.deal();
		game.bid();
		game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
		while(game.allPasses())
		{
			game.newRound(false);
			game.deal();
			game.bid();
			game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
		}
		test = game.getWidow();
		
		//Entering a loop that will test human exchange multiple times, making sure it actually does something.
		for (int i =0; i < 50; i++)
		{
			//Keeps resetting the round until the human wins the contract
			while(!(game.getContractHolderIndex() == 0))
			{
				//resetting round for next iteration
				game.newRound(false);
				game.deal();
				game.bid();
				game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
				while(game.allPasses())
				{
					game.newRound(false);
					game.deal();
					game.bid();
					game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
				}
				test = game.getWidow();
			}
				
			//Calling exchange
			game.exchange(testRobot.selectCardsToDiscard(game.getBids(), 0, game.getHand(0)));
			
			//Making sure exchange leaves the hands of all players at size 10
			for (int j = 0; j < 4; j++)
			{
				assertTrue(game.getHand(j).size() == 10);
			}
			
			if (test != game.getWidow())
			{
				break;
			}
			
			//resetting round for next iteration
			game.newRound(false);
			game.deal();
			game.bid();
			game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
			while(game.allPasses())
			{
				game.newRound(false);
				game.deal();
				game.bid();
				game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
			}
			test = game.getWidow();
		}
	}
	
	@Test
	public void testPlayTrick()
	{
		//Setting up the game
		//Initializes the players.
		Player playerA = new BasicRobot();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		game.newRound(false);
		game.deal();
		game.bid();
		
		//Resetting the round if all players passed
		while(game.allPasses())
		{
			game.newRound(false);
			game.deal();
			game.bid();
		}
		
		//Performing card exchange
		game.exchange();
		
		//Making sure hands are of proper size before the trick
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 10);
		}
		//Playing the trick
		game.playTrick();
		
		//Making sure the hands of proper size after the trick is played
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 9);
		}
	}
	
	@Test
	public void testInteractivePlayTrick()
	{
		//Setting up a Basic Robot to fill in for the human
		Player testRobot = new BasicRobot();
		
		//Setting up the game
		//Initializes the players.
		Player playerA = new HumanPlayer();
		Player playerB = new RandomRobot();
		Player playerC = new BasicRobot();
		Player playerD = new RandomRobot();
		
		//Construct a GameEngine object with the mode passed as parameters.
		GameEngine game = new GameEngine(false);
		game.changePlayer(playerA, 0);
		game.changePlayer(playerB, 1);
		game.changePlayer(playerC, 2);
		game.changePlayer(playerD, 3);
		game.newRound(true);
		game.deal();
		game.bid();
		game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
		
		//Resetting the round if all players passed
		while(game.allPasses())
		{
			game.newRound(true);
			game.deal();
			game.bid();
			game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
		}
		
		while (!(game.getContractHolderIndex() == 0))
		{
			game.newRound(true);
			game.deal();
			game.bid();
			game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
			
			//Resetting the round if contract holder is not the human.
			while(game.allPasses())
			{
				game.newRound(true);
				game.deal();
				game.bid();
				game.bid(testRobot.selectBid(game.getBids(), game.getHand(0)));
			}
		}
		
		//Performing appropriate card exchange
		if (game.getContractHolderIndex() == 0)
		{
			game.exchange(testRobot.selectCardsToDiscard(game.getBids(), 0, game.getHand(0)));
		}
		else
		{
			game.exchange();
		}
		
		//Make sure hands are of proper size before the trick is played
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 10);
		}
		
		//Playing the trick
		game.playTrick();
		game.playTrick(testRobot.play(game.getTrick(), game.getHand(0)));
		
		//Making sure all players played 1 card
		for (int i = 0; i < 4; i++)
		{
			assertTrue(game.getHand(i).size() == 9);
		}
	}
}
