/**
 * @author James McCorriston
 */
package comp303.fivehundred.ai;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

public class TestBasicCardExchangeStrategy
{
	@Test
	public void testSelectCardsToDiscard()
	{
		//Setting up test objects
		Hand testHand = new Hand();
		CardList discard = new CardList();
		BasicCardExchangeStrategy strat = new BasicCardExchangeStrategy();
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

		//Create a clone of the hand for testing
		Hand clone1 = testHand.clone();
		
		//Testing functionality
		discard = strat.selectCardsToDiscard(bids, 2, testHand);
		assertTrue(discard.size() == 6 && testHand.size() == 16 && clone1.size() == 16);
		
		Card.ByRankComparator comparator = new  Card.ByRankComparator();
		
		//The cards that should be discarded when hearts is trump.
		CardList testDiscardHeartsTrump = new CardList();
		testDiscardHeartsTrump.add(a9D);
		testDiscardHeartsTrump.add(a7D);
		testDiscardHeartsTrump.add(aTC);
		testDiscardHeartsTrump.add(a7C);
		testDiscardHeartsTrump.add(aJS);
		testDiscardHeartsTrump.add(a6S);

		//Tests to see if the correct cards were selected for discard.
		assertEquals(discard.sort(comparator).toString(), testDiscardHeartsTrump.sort(comparator).toString());
		
		//Testing to see if altering the list of cards to discard or the cloned
		//hand mutates the original hand object.
		assertEquals(clone1.sort(comparator).toString(), testHand.sort(comparator).toString());
		clone1.remove(discard.getFirst());
		discard.remove(discard.getFirst());
		assertTrue(testHand.size() == 16);
		
		//Testing to make sure the method can handle a hand that has fewer than
		//6 non-trump cards (when there is a trump suit)
		Hand testHand2 = new Hand();
		CardList discard2 = new CardList();
		Bid[] bids2 = {new Bid(), new Bid(6, Suit.SPADES), new Bid(7, Suit.SPADES), new Bid()};

		testHand2.add(aKS);
		testHand2.add(aJS);
		testHand2.add(aJC);
		testHand2.add(aJD);
		testHand2.add(aJH);
		testHand2.add(aHJo);
		testHand2.add(aLJo);
		testHand2.add(a8S);
		testHand2.add(a9D);
		testHand2.add(aTS);
		testHand2.add(aQS);
		testHand2.add(aAS);
		testHand2.add(a5S);
		testHand2.add(a7S);
		testHand2.add(a6S);
		testHand2.add(a4H);
		
		//The cards that should be discarded when spades is trump.
		CardList testDiscardSpadesTrump = new CardList();
		testDiscardSpadesTrump.add(a9D);
		testDiscardSpadesTrump.add(aJH);
		testDiscardSpadesTrump.add(aJD);
		testDiscardSpadesTrump.add(a4H);
		testDiscardSpadesTrump.add(a5S);
		testDiscardSpadesTrump.add(a6S);
		
		//Create a clones of the hand for testing
		Hand clone2 = testHand2.clone();

		//Testing functionality
		discard2 = strat.selectCardsToDiscard(bids2, 1, testHand2);
		assertTrue(discard2.size() == 6 && testHand2.size() == 16 && clone2.size() == 16);
		
		//Tests to see if the correct cards were selected for discard.
		assertEquals(discard2.sort(comparator).toString(), testDiscardSpadesTrump.sort(comparator).toString());
		
		//Testing to see if altering the list of cards to discard or the cloned
		//hand mutates the original hand object.
		assertEquals(clone2.sort(comparator).toString(), testHand2.sort(comparator).toString());
		clone2.remove(discard2.getFirst());
		discard2.remove(discard2.getFirst());
		assertTrue(testHand2.size() == 16);
		
	}
}
