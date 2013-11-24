/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.ai;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card.Suit;
import static comp303.fivehundred.util.AllCards.*;

public class TestRandomBiddingStrategy
{
	@Test
	public void testSelectBid()
	{
		//Creating test objects
		Hand testHand = new Hand();
		Bid[] testBids = new Bid[0];
		Bid bid1 = new Bid(6, Suit.SPADES);
		Bid bid2 = new Bid(7, Suit.SPADES);
		Bid bid3 = new Bid(9, Suit.SPADES);
		Bid bid4 = new Bid(10, Suit.HEARTS);
		Bid bid5 = new Bid(10, null);
		Bid returnBid;
		
		//Filling up the hand
		testHand.add(aKS);
		testHand.add(a4H);
		testHand.add(a8C);
		testHand.add(aQD);
		testHand.add(aHJo);
		testHand.add(aLJo);
		testHand.add(aJD);
		testHand.add(aJS);
		testHand.add(aJH);
		testHand.add(aJC);
		
		//Testing constructors
		RandomBiddingStrategy strat1 = new RandomBiddingStrategy();
		strat1 = new RandomBiddingStrategy(50);
		
		//Testing functionality with empty bid list
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.isPass() || (returnBid.toIndex() >= 0 && returnBid.toIndex() <= 24));
		
		//Testing functionality with only lowest possible bid in the list
		testBids = new Bid[1];
		testBids[0] = bid1;
		//Testing 3 times to try and eliminate chance results
		for (int i = 0; i < 3; i++)
		{
			returnBid = strat1.selectBid(testBids, testHand);
			assertTrue(returnBid.isPass() || (returnBid.toIndex() >= 1 && returnBid.toIndex() <= 24));
		}
		
		//Testing functionality with the addition of another bid to the list
		testBids = new Bid[2];
		testBids[0] = bid1;
		testBids[1] = bid2;
		//Testing 3 times to try and eliminate chance results
		for (int i = 0; i < 3; i++)
		{
			returnBid = strat1.selectBid(testBids, testHand);
			assertTrue(returnBid.isPass() || (returnBid.toIndex() >= 6 && returnBid.toIndex() <= 24));
		}
		
		//Testing functionality with 3 bids in the list
		testBids = new Bid[3];
		testBids[0] = bid1;
		testBids[1] = bid2;
		testBids[2] = bid3;
		//Testing 3 times to try and eliminate chance results
		for (int i = 0; i < 3; i++)
		{
			returnBid = strat1.selectBid(testBids, testHand);
			assertTrue(returnBid.isPass() || (returnBid.toIndex() >= 16 && returnBid.toIndex() <= 24));
		}
		
		//Testing functionality with a full bid list
		testBids[2] = bid4;
		//Testing 3 times to try and eliminate chance results
		for (int i = 0; i < 3; i++)
		{
			returnBid = strat1.selectBid(testBids, testHand);
			assertTrue(returnBid.isPass() || (returnBid.toIndex() >= 21 && returnBid.toIndex() <= 24));
		}
		
		//Testing functionality when only available bid is a passing bid
		testBids[2] = bid5;
		//Testing 3 times to try and eliminate chance results
		for (int i = 0; i < 3; i++)
		{
			returnBid = strat1.selectBid(testBids, testHand);
			assertTrue(returnBid.isPass());
		}
	}
}
