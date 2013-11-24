/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.ai;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

public class TestRandomCardExchangeStrategy
{
	@Test
	public void testSelectCardsToDiscard()
	{
		//Setting up test objects
		Hand testHand = new Hand();
		CardList discard = new CardList();
		RandomCardExchangeStrategy strat = new RandomCardExchangeStrategy();
		Bid[] bids = {new Bid(), new Bid(6, Suit.SPADES), new Bid(8, Suit.HEARTS), new Bid()};
		
		//Filling up the hand
		testHand.add(aKS);
		testHand.add(aJS);
		testHand.add(aJC);
		testHand.add(aJD);
		testHand.add(aJH);
		testHand.add(aHJo);
		testHand.add(aLJo);
		testHand.add(a8H);
		testHand.add(a9D);
		testHand.add(aTC);
		testHand.add(aQS);
		testHand.add(aAH);
		testHand.add(a7D);
		testHand.add(a7C);
		testHand.add(a6S);
		testHand.add(a4H);
		
		//Create a clones of the hand for testing
		Hand clone1 = testHand.clone();
		
		//Testing functionality
		discard = strat.selectCardsToDiscard(bids, 2, testHand);
		assertTrue(discard.size() == 6 && testHand.size() == 16);
		for (int i = 0; i < 6; i++)
		{
			clone1.remove(discard.getFirst());
			discard.remove(discard.getFirst());
		}
		assertTrue(testHand.size() == 16);
	}
}
