/**
f * @author James McCorriston
 */
package comp303.fivehundred.ai;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card.Suit;
import static comp303.fivehundred.util.AllCards.*;

public class TestBasicBiddingStrategy
{
	@Test
	public void testSelectBid()
	{
		//Creating test objects
		Hand testHand = new Hand();
		Bid[] testBids = new Bid[0];
		Bid bid1 = new Bid(6, Suit.SPADES);
		Bid bid2 = new Bid(7, Suit.DIAMONDS);
		Bid bid3 = new Bid(8, Suit.SPADES);
		Bid bid4 = new Bid(10, null);
		Bid returnBid;
		
		//Filling up the hand
		testHand.add(aKS);
		testHand.add(a4H);
		testHand.add(a8D);
		testHand.add(aQD);
		testHand.add(aHJo);
		testHand.add(aLJo);
		testHand.add(aJD);
		testHand.add(aJS);
		testHand.add(aJH);
		testHand.add(aJC);
		
		//Testing constructors
		BasicBiddingStrategy strat1 = new BasicBiddingStrategy();
		
		//Testing functionality with empty bid list
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.isPass() || (returnBid.toIndex() >= 0 && returnBid.toIndex() <= 24));
		
		//Testing functionality with no previous bids
		testBids = new Bid[0];
		//Expecting a bid of 8 no trump since no one has bid yet.
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.isNoTrump() && returnBid.getTricksBid() == 8);
		
		//Testing functionality with a 6 spades bid as the only previous bid
		testBids = new Bid[1];
		testBids[0] = bid1;
		//Expecting a bid of 8 diamonds (based on the basic formula provided in the 
		//assignment specifications)
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.getSuit() == Suit.DIAMONDS && returnBid.getTricksBid() == 8);
		
		//Testing functionality when the only bid so far is an opponent passing bid.
		testBids[0] = new Bid();
		//Expecting a bid of 8 no trump.
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.isNoTrump() && returnBid.getTricksBid() == 8);
		
		//Testing functionality with the addition of another bid to the list (7 clubs)
		testBids = new Bid[2];
		testBids[0] = bid1;
		testBids[1] = bid2;
		//Expecting a bid of 10 diamonds since at this point the partner has bid
		//spades but the opponent has bid diamonds.
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.getSuit() == Suit.SPADES && returnBid.getTricksBid() == 10);
		
		//Testing functionality with 3 bids in the list
		testBids = new Bid[3];
		testBids[0] = bid1;
		testBids[1] = bid2;
		testBids[2] = bid3;
		//Expecting a bid of 10 diamonds since at this point the partner has bid
		//diamonds and both opponents have bid spades.
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.getSuit() == Suit.DIAMONDS && returnBid.getTricksBid() == 10);	
		
		//Testing functionality with 3 bids, the highest of which is
		//a 10 no trump bid.
		testBids[2] = bid4;
		//Expecting a passing bid since the robot would bid 10 diamonds but the opponent
		//has already bid 10 no trump.
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.isPass());
		
		//Replacing the 8 of clubs and 4 of hearts with the queen of clubs and
		//king of hearts to create an even score for each suit so a no trump bid
		//will be made.
		testHand.remove(a8D);
		testHand.remove(a4H);
		testHand.add(aQC);
		testHand.add(aKH);
		
		//Testing functionality with 3 bids, the highest of which is
		//a 10 no trump bid. Expecting a passing bid since the robot would
		//bid 8 no trump but the opponent has already bid 10 no trump.
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.isPass());
		
		//Testing functionality with no previous bids
		testBids = new Bid[0];
		//Expecting a bid of 8 no trump.
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.isNoTrump() && returnBid.getTricksBid() == 8);
		
		//Replaces the Queen of clubs with the Ace of clubs.
		testHand.remove(aQC);
		testHand.add(aAC);
		
		//Testing functionality when the only bid so far is an opponent bid of 8 spades.
		testBids = new Bid[1];
		testBids[0] = bid3;
		//Expecting a bid of 8 no trump.
		returnBid = strat1.selectBid(testBids, testHand);
		assertTrue(returnBid.isNoTrump() && returnBid.getTricksBid() == 8);
		
		//Creating a second set of test objects with a hand that has mostly
		//spades.
		Hand testHand2 = new Hand();
		Bid[] testBids2 = new Bid[0];
		bid1 = new Bid(6, Suit.DIAMONDS);
		bid2 = new Bid(7, Suit.SPADES);
		bid3 = new Bid(8, Suit.DIAMONDS);
		bid4 = new Bid(7, Suit.DIAMONDS);
		Bid bid5 = new Bid(8, Suit.SPADES);
		
		//Filling up the hand
		testHand2.add(aKD);
		testHand2.add(a4D);
		testHand2.add(a8C);
		testHand2.add(aQD);
		testHand2.add(aQS);
		testHand2.add(aLJo);
		testHand2.add(aJS);
		testHand2.add(aJD);
		testHand2.add(aAD);
		testHand2.add(aJH);
		
		//Testing functionality with a 6 diamonds bid as the only previous bid
		testBids2 = new Bid[1];
		testBids2[0] = bid1;
		//Expecting a passing bid due to the fact that this hand would normally
		//bid 6 diamonds but and a bid of this value has already been made.
		returnBid = strat1.selectBid(testBids2, testHand2);
		assertTrue(returnBid.isPass());
		
		//Testing functionality when the only bid so far is an opponent bid of 7 no trump.
		testBids2[0] = new Bid();
		//Expecting a bid of 8 diamonds.
		returnBid = strat1.selectBid(testBids2, testHand2);
		assertTrue(returnBid.getSuit() == Suit.DIAMONDS && returnBid.getTricksBid() == 8);
		
		//Testing functionality when the only bid so far is an opponent bid of 8 spades.
		testBids2[0] = bid5;
		//Expecting a bid of 8 diamonds.
		returnBid = strat1.selectBid(testBids2, testHand2);
		assertTrue(returnBid.getSuit() == Suit.DIAMONDS && returnBid.getTricksBid() == 8);
		
		//Testing functionality with a 6 diamonds bid by the partner and an 8 diamonds
		//bid by the first opponent.
		testBids2 = new Bid[2];
		testBids2[0] = bid1;
		testBids2[1] = bid4;
		//Expecting a bid of 8 diamonds.
		returnBid = strat1.selectBid(testBids2, testHand2);
		assertTrue(returnBid.getSuit() == Suit.DIAMONDS && returnBid.getTricksBid() == 8);
		
		//Testing functionality with a 6 diamonds bid by the first opponent, a passing
		//bid by the partner and a 7 diamonds bid by the second opponent.
		testBids2 = new Bid[3];
		testBids2[0] = bid1;
		testBids2[1] = new Bid();
		testBids2[2] = bid4;
		//Expecting a passing bid due to overwhelming opponent spades bids.
		returnBid = strat1.selectBid(testBids2, testHand2);
		assertTrue(returnBid.isPass());
		
		//Creating a third set of test objects with a hand that contains mostly
		//clubs.
		Hand testHand3 = new Hand();
		Bid[] testBids3 = new Bid[0];
		bid1 = new Bid(6, Suit.CLUBS);
		bid2 = new Bid(7, Suit.DIAMONDS);
		bid3 = new Bid(10, Suit.CLUBS);
		
		//Filling up the hand
		testHand3.add(a4H);
		testHand3.add(a4D);
		testHand3.add(a4C);
		testHand3.add(a4S);
		testHand3.add(aAC);
		testHand3.add(aLJo);
		testHand3.add(aJC);
		testHand3.add(aJS);
		testHand3.add(a5C);
		testHand3.add(aJH);
		
		//Testing functionality with no previous bids
		testBids3 = new Bid[0];
		//Expecting a bid of 6 clubs
		returnBid = strat1.selectBid(testBids3, testHand3);
		assertTrue(returnBid.getSuit() == Suit.CLUBS && returnBid.getTricksBid() == 6);
		
		//Testing functionality with a bid of 6 clubs by the partner and a passing
		//bid by the first opponent.
		testBids3 = new Bid[2];
		testBids3[0] = bid1;
		testBids3[1] = new Bid();
		//Expecting a bid of 9 clubs
		returnBid = strat1.selectBid(testBids3, testHand3);
		assertTrue(returnBid.getSuit() == Suit.CLUBS && returnBid.getTricksBid() == 9);
		
		//Testing functionality with a bid of 10 clubs by the partner and a passing
		//bid by the first opponent.
		testBids3 = new Bid[2];
		testBids3[0] = bid3;
		testBids3[1] = new Bid();
		//Expecting a passing bid since this would normally result in a 9 clubs bid
		//but 10 clubs has already been bid.
		returnBid = strat1.selectBid(testBids3, testHand3);
		assertTrue(returnBid.isPass());
		
		//Replacing the jack of hearts with the 6 of clubs
		testHand3.remove(aJH);
		testHand3.add(a6C);
		
		//Testing functionality with no previous bids
		testBids3 = new Bid[0];
		//Expecting a bid of 7 clubs
		returnBid = strat1.selectBid(testBids3, testHand3);
		assertTrue(returnBid.getSuit() == Suit.CLUBS && returnBid.getTricksBid() == 7);
		
		//Testing functionality with no previous bids
		testBids3 = new Bid[1];
		testBids3[0] = bid2;
		//Expecting a passing bid since the robot would bid 7 clubs but a bid of 
		//a higher suit has already been made (7 clubs)
		returnBid = strat1.selectBid(testBids3, testHand3);
		assertTrue(returnBid.isPass());
		
		Hand testHand4 = new Hand();
		Bid[] testBids4 = new Bid[0];
		
		//Filling up the hand
		testHand4.add(a4H);
		testHand4.add(a4D);
		testHand4.add(a4C);
		testHand4.add(a4S);
		testHand4.add(a5H);
		testHand4.add(a5D);
		testHand4.add(a5C);
		testHand4.add(a5S);
		testHand4.add(a6H);
		testHand4.add(a6D);
		
		//Testing functionality with no previous bids.
		//Expecting a passing bid.
		returnBid = strat1.selectBid(testBids4, testHand4);
		assertTrue(returnBid.isPass());
		
		//Testing functionality with one previous passing bid.
		testBids4 = new Bid[1];
		testBids4[0] = new Bid();
		//Expecting a passing bid.
		returnBid = strat1.selectBid(testBids4, testHand4);
		assertTrue(returnBid.isPass());
	}
}
