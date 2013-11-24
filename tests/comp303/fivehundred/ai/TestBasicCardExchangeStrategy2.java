package comp303.fivehundred.ai;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;

public class TestBasicCardExchangeStrategy2
{
	private BasicCardExchangeStrategy aStrategy = new BasicCardExchangeStrategy();
	
	/**
	 * A basic test to check the normal conditions of the strategy.
	 * Here the strategy is applied with SPADE as trump and there's
	 * not jack of clubs.
	 * If this fails, there's probably a major bug in the code.
	 */
	@Test 
	public void test1()
	{
		Bid[] lBids = {new Bid(7,Suit.SPADES), new Bid(), new Bid(), new Bid() };
		int lIndex = 0;
		Hand lHand = new Hand();
		lHand.add(new Card(Rank.FIVE,Suit.SPADES));
		lHand.add(new Card(Rank.KING,Suit.SPADES));
		lHand.add(new Card(Rank.JACK,Suit.SPADES));
		lHand.add(new Card(Rank.ACE,Suit.SPADES));
		lHand.add(new Card(Rank.JACK,Suit.HEARTS)); //
		lHand.add(new Card(Rank.NINE,Suit.DIAMONDS)); //
		lHand.add(new Card(Rank.SIX,Suit.SPADES));
		lHand.add(new Card(Rank.EIGHT,Suit.SPADES));
		lHand.add(new Card(Joker.HIGH));
		lHand.add(new Card(Rank.ACE,Suit.CLUBS)); //
		lHand.add(new Card(Rank.NINE,Suit.SPADES));
		lHand.add(new Card(Rank.TEN,Suit.SPADES));
		lHand.add(new Card(Rank.QUEEN,Suit.HEARTS));
		lHand.add(new Card(Rank.QUEEN,Suit.DIAMONDS)); //
		lHand.add(new Card(Rank.KING,Suit.DIAMONDS)); //
		lHand.add(new Card(Joker.LOW)); 
		CardList lCards = aStrategy.selectCardsToDiscard(lBids, lIndex, lHand);
		assertEquals( 6, lCards.size() );
		assertTrue(contains(lCards,AllCards.aJH));
		assertTrue(contains(lCards,AllCards.a9D));
		assertTrue(contains(lCards,AllCards.aAC));
		assertTrue(contains(lCards,AllCards.aQD));
		assertTrue(contains(lCards,AllCards.aKD));
		assertTrue(contains(lCards,AllCards.aQH));
		
	}
	
	/**
	 * Another test for normal conditions. Here some of the
	 * ranks are equal.
	 */
	@Test 
	public void test2()
	{
		Bid[] lBids = {new Bid(6,Suit.SPADES), new Bid(6,Suit.HEARTS), new Bid(), new Bid() };
		int lIndex = 0;
		Hand lHand = new Hand();
		lHand.add(AllCards.a6D); // D
		lHand.add(AllCards.a7D); // D
		lHand.add(AllCards.a7H); 
		lHand.add(AllCards.a7C); // D
		lHand.add(AllCards.a7S); // D
		lHand.add(AllCards.a8S); // D
		lHand.add(AllCards.a8C); // D
		lHand.add(AllCards.aLJo);
		lHand.add(AllCards.aHJo);
		lHand.add(AllCards.a8H);
		lHand.add(AllCards.aAS);
		lHand.add(AllCards.aAH);
		lHand.add(AllCards.aAC);
		lHand.add(AllCards.aAD);
		lHand.add(AllCards.aKD);
		lHand.add(AllCards.aJC);
		CardList lCards = aStrategy.selectCardsToDiscard(lBids, lIndex, lHand);
		assertEquals( 6, lCards.size() );
		assertTrue(contains(lCards,AllCards.a6D));
		assertTrue(contains(lCards,AllCards.a7D));
		assertTrue(contains(lCards,AllCards.a7C));
		assertTrue(contains(lCards,AllCards.a7S));
		assertTrue(contains(lCards,AllCards.a8S));
		assertTrue(contains(lCards,AllCards.a8C));
		
	}
	
	/**
	 * This test ensures that Jack swaps are taken into account.
	 */
	@Test 
	public void test3()
	{
		Bid[] lBids = {new Bid(6,Suit.SPADES), new Bid(6,Suit.HEARTS), new Bid(), new Bid() };
		int lIndex = 0;
		Hand lHand = new Hand();
		lHand.add(AllCards.aJH); 
		lHand.add(AllCards.aJD); 
		lHand.add(AllCards.aQD); // D 
		lHand.add(AllCards.aQS); // D
		lHand.add(AllCards.aKD); // D
		lHand.add(AllCards.aKS); // D
		lHand.add(AllCards.aAD); // D
		lHand.add(AllCards.aAS); // D
		lHand.add(AllCards.aHJo);
		lHand.add(AllCards.a8H);
		lHand.add(AllCards.a9H);
		lHand.add(AllCards.aTH);
		lHand.add(AllCards.aQH);
		lHand.add(AllCards.aKH);
		lHand.add(AllCards.aAH);
		lHand.add(AllCards.aLJo);
		CardList lCards = aStrategy.selectCardsToDiscard(lBids, lIndex, lHand);
		assertEquals( 6, lCards.size() );
		assertTrue(contains(lCards,AllCards.aQD));
		assertTrue(contains(lCards,AllCards.aQS));
		assertTrue(contains(lCards,AllCards.aKD));
		assertTrue(contains(lCards,AllCards.aKS));
		assertTrue(contains(lCards,AllCards.aAD));
		assertTrue(contains(lCards,AllCards.aAS));
		
	}
	
	/**
	 * This test ensures that no trumps works
	 */
	@Test 
	public void test4()
	{
		Bid[] lBids = {new Bid(6,Suit.SPADES), new Bid(6,Suit.HEARTS), new Bid(6,null), new Bid(7,null) };
		int lIndex = 0;
		Hand lHand = new Hand();
		lHand.add(AllCards.aJH); // D
		lHand.add(AllCards.aJD); // D
		lHand.add(AllCards.aJS); // D  
		lHand.add(AllCards.aQS); 
		lHand.add(AllCards.aKD); 
		lHand.add(AllCards.aKS); 
		lHand.add(AllCards.aAD); 
		lHand.add(AllCards.aAS); 
		lHand.add(AllCards.aHJo);
		lHand.add(AllCards.a8H);  // D
		lHand.add(AllCards.a9H);  // D
		lHand.add(AllCards.aTH);  // D
		lHand.add(AllCards.aQH);
		lHand.add(AllCards.aKH);
		lHand.add(AllCards.aAH);
		lHand.add(AllCards.aLJo);
		CardList lCards = aStrategy.selectCardsToDiscard(lBids, lIndex, lHand);
		assertEquals( 6, lCards.size() );
		assertTrue(contains(lCards,AllCards.aJH));
		assertTrue(contains(lCards,AllCards.aJD));
		assertTrue(contains(lCards,AllCards.aJS));
		assertTrue(contains(lCards,AllCards.a8H));
		assertTrue(contains(lCards,AllCards.a9H));
		assertTrue(contains(lCards,AllCards.aTH));
		
	}
	
	/**
	 * This is an advanced test. It checks the situation where almost 
	 * all the cards are trump, and some trump cards are discarded.
	 */
	@Test 
	public void test5()
	{
		Bid[] lBids = {new Bid(6,Suit.SPADES), new Bid(6,Suit.HEARTS), new Bid(7,Suit.HEARTS), new Bid() };
		int lIndex = 0;
		Hand lHand = new Hand();
		lHand.add(AllCards.a4H); // D
		lHand.add(AllCards.a5H); // D
		lHand.add(AllCards.a6H); // D
		lHand.add(AllCards.a7H); // D
		lHand.add(AllCards.a8H); 
		lHand.add(AllCards.a9H); 
		lHand.add(AllCards.aTH); 
		lHand.add(AllCards.aJH); 
		lHand.add(AllCards.aJD);
		lHand.add(AllCards.aQH);  
		lHand.add(AllCards.aKH);  
		lHand.add(AllCards.aAH);  
		lHand.add(AllCards.aLJo);
		lHand.add(AllCards.aHJo);
		lHand.add(AllCards.aAS); // D
		lHand.add(AllCards.aAC); // D
		CardList lCards = aStrategy.selectCardsToDiscard(lBids, lIndex, lHand);
		assertEquals( 6, lCards.size() );
		assertTrue(contains(lCards,AllCards.a4H));
		assertTrue(contains(lCards,AllCards.a5H));
		assertTrue(contains(lCards,AllCards.a6H));
		assertTrue(contains(lCards,AllCards.a7H));
		assertTrue(contains(lCards,AllCards.aAS));
		assertTrue(contains(lCards,AllCards.aAC));
		
	}
	
	private static boolean contains(CardList pCardList, Card pCard)
	{
		for( Card card : pCardList)
		{
			if( card.equals(pCard))
			{
				return true;
			}
		}
		return false;
	}
}
