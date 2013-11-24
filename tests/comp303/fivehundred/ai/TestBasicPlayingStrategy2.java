package comp303.fivehundred.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

public class TestBasicPlayingStrategy2
{
	private BasicPlayingStrategy aStrategy = new BasicPlayingStrategy();
	
	/**
	 * Test case 1: leading is not a joker. Also tests that
	 * the returned card is always in the hand.
	 */
	@Test 
	public void test1()
	{
		Trick lTrick = new Trick(new Bid(7,Suit.SPADES));
				
		Hand lHand = new Hand();
		lHand.add(AllCards.a6D); 
		lHand.add(AllCards.a7D); 
		lHand.add(AllCards.a7H); 
		lHand.add(AllCards.a7C); 
		lHand.add(AllCards.a7S); 
		lHand.add(AllCards.a8D); 
		lHand.add(AllCards.a8C); 
		lHand.add(AllCards.aLJo);
		lHand.add(AllCards.aHJo);
		lHand.add(AllCards.a8H);
		lHand.add(AllCards.aAS);

		for( int i = 0; i < 10000; i++ )
		{
			Card lCard = aStrategy.play(lTrick, lHand);
			assertFalse(lCard.isJoker());
			assertTrue(contains(lHand,lCard));
		}
	}
	
	/**
	 * Test case 2a: Picks the lowest card that can follow
	 * suit and win. Not a trump played.
	 */
	@Test 
	public void test2a()
	{
		Trick lTrick = new Trick(new Bid(7,Suit.SPADES));
		lTrick.add(AllCards.a6D);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.a7D); 
		lHand.add(AllCards.a8D); 
		lHand.add(AllCards.a9D); 
		lHand.add(AllCards.a7H); 
		lHand.add(AllCards.a7S); 
		lHand.add(AllCards.a8H); 
		lHand.add(AllCards.a8C); 
		lHand.add(AllCards.aLJo);
		lHand.add(AllCards.aHJo);
		lHand.add(AllCards.aAC);
		lHand.add(AllCards.aAS);

		assertEquals(AllCards.a7D,aStrategy.play(lTrick, lHand));
	}
	
	/**
	 * Test case 2b: Picks the lowest card that can follow
	 * suit and win. A trump is played.
	 */
	@Test 
	public void test2b()
	{
		Trick lTrick = new Trick(new Bid(7,Suit.SPADES));
		lTrick.add(AllCards.aJC);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.aJS); // Next in line 
		lHand.add(AllCards.aLJo); 
		lHand.add(AllCards.aHJo); 
		lHand.add(AllCards.a7H); 
		lHand.add(AllCards.a7S); 
		lHand.add(AllCards.a8H); 
		lHand.add(AllCards.a8C); 
		lHand.add(AllCards.a8C);
		lHand.add(AllCards.a8S);
		lHand.add(AllCards.aAC);
		lHand.add(AllCards.aAS);

		assertEquals(AllCards.aJS,aStrategy.play(lTrick, lHand));
	}
	
	/**
	 * Test case 3: Picks the lowest card that can follow
	 * suit, in the case where no card can follow suit
	 * and win.
	 */
	@Test 
	public void test3()
	{
		Trick lTrick = new Trick(new Bid(7,Suit.SPADES));
		lTrick.add(AllCards.aQH);
		lTrick.add(AllCards.aAH);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.a4H); // Should be played
		lHand.add(AllCards.a5H); 
		lHand.add(AllCards.a6H); 
		lHand.add(AllCards.a7H); 
		lHand.add(AllCards.a4C); 
		lHand.add(AllCards.a5C); 
		lHand.add(AllCards.aLJo); 
		lHand.add(AllCards.aHJo);
		lHand.add(AllCards.aJS);
		lHand.add(AllCards.aJC);
		lHand.add(AllCards.aAS);

		assertEquals(AllCards.a4H,aStrategy.play(lTrick, lHand));
	}
	
	/**
	 * Test case 4a: No card can follow suit. Play the lowest 
	 * trump card, no trump cards played so far.
	 */
	@Test 
	public void test4a()
	{
		Trick lTrick = new Trick(new Bid(7,Suit.SPADES));
		lTrick.add(AllCards.aQH);
		lTrick.add(AllCards.aAH);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.a4S); // Should be played
		lHand.add(AllCards.a5S); 
		lHand.add(AllCards.a6S); 
		lHand.add(AllCards.a7S); 
		lHand.add(AllCards.a4C); 
		lHand.add(AllCards.a5C); 
		lHand.add(AllCards.a6C); 
		lHand.add(AllCards.a7C);
		lHand.add(AllCards.a8C);
		lHand.add(AllCards.a9C);
		lHand.add(AllCards.aTC);

		assertEquals(AllCards.a4S,aStrategy.play(lTrick, lHand));
	}
	
	/**
	 * Test case 4b: No card can follow suit. Play the lowest 
	 * trump card, a trump card was played.
	 */
	@Test 
	public void test4b()
	{
		Trick lTrick = new Trick(new Bid(7,Suit.SPADES));
		lTrick.add(AllCards.aQH);
		lTrick.add(AllCards.aAH);
		lTrick.add(AllCards.a5S);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.a4S); 
		lHand.add(AllCards.a9S); 
		lHand.add(AllCards.a6S); // Should be played
		lHand.add(AllCards.a7S); 
		lHand.add(AllCards.a4C); 
		lHand.add(AllCards.a5C); 
		lHand.add(AllCards.a6C); 
		lHand.add(AllCards.a7C);
		lHand.add(AllCards.a8C);
		lHand.add(AllCards.a9C);
		lHand.add(AllCards.aTC);
		
		assertEquals(AllCards.a6S,aStrategy.play(lTrick, lHand));
	}
	
	/**
	 * Test case 5a: No card can follow suit and no trump card.
	 */
	@Test 
	public void test5a()
	{
		Trick lTrick = new Trick(new Bid(7,Suit.SPADES));
		lTrick.add(AllCards.aQH);
		lTrick.add(AllCards.aAH);
		lTrick.add(AllCards.a5S);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.a4D); // Should be played
		lHand.add(AllCards.a5D); 
		lHand.add(AllCards.a6D); 
		lHand.add(AllCards.a7D); 
		lHand.add(AllCards.a8D); 
		lHand.add(AllCards.a9D); 
		lHand.add(AllCards.a5C); 
		lHand.add(AllCards.a7C);
		lHand.add(AllCards.a8C);
		lHand.add(AllCards.a9C);
		lHand.add(AllCards.aTC);

		assertEquals(AllCards.a4D,aStrategy.play(lTrick, lHand));
	}
	
	/**
	 * Test case 5b: No card can follow suit and no sufficiently
	 * high trump card.
	 */
	@Test 
	public void test5b()
	{
		Trick lTrick = new Trick(new Bid(7,Suit.SPADES));
		lTrick.add(AllCards.aQH);
		lTrick.add(AllCards.aAH);
		lTrick.add(AllCards.aQS);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.a4D); // Should be played
		lHand.add(AllCards.a5D); 
		lHand.add(AllCards.a6D); 
		lHand.add(AllCards.a7D); 
		lHand.add(AllCards.a8S); 
		lHand.add(AllCards.a9S); 
		lHand.add(AllCards.a5C); 
		lHand.add(AllCards.a7C);
		lHand.add(AllCards.a8C);
		lHand.add(AllCards.a9C);
		lHand.add(AllCards.aTC);

		assertEquals(AllCards.a4D,aStrategy.play(lTrick, lHand));
	}
	
	/**
	 * Test case 6a: Joker led: dump the lowest card.
	 */
	@Test 
	public void test6a()
	{
		Trick lTrick = new Trick(new Bid(7,null));
		lTrick.add(AllCards.aHJo);
		lTrick.add(AllCards.aAH);
		lTrick.add(AllCards.a4S);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.a4D); // Should be played
		lHand.add(AllCards.a5D); 
		lHand.add(AllCards.a6D); 
		lHand.add(AllCards.a7D); 
		lHand.add(AllCards.a8S); 
		lHand.add(AllCards.a9S); 
		lHand.add(AllCards.a5C); 
		lHand.add(AllCards.a7C);
		lHand.add(AllCards.a8C);
		lHand.add(AllCards.a9C);
		lHand.add(AllCards.aTC);

		assertEquals(AllCards.a4D,aStrategy.play(lTrick, lHand));
	}
	
	/**
	 * Test case 6b: Joker led: beat it
	 */
	@Test 
	public void test6b()
	{
		Trick lTrick = new Trick(new Bid(7,null));
		lTrick.add(AllCards.aLJo);
		lTrick.add(AllCards.aAH);
		lTrick.add(AllCards.a4S);
				
		Hand lHand = new Hand();
		lHand.add(AllCards.aHJo); // Should be played
		lHand.add(AllCards.a5D); 
		lHand.add(AllCards.a6D); 
		lHand.add(AllCards.a7D); 
		lHand.add(AllCards.a8S); 
		lHand.add(AllCards.a9S); 
		lHand.add(AllCards.a5C); 
		lHand.add(AllCards.a7C);
		lHand.add(AllCards.a8C);
		lHand.add(AllCards.a9C);
		lHand.add(AllCards.aTC);

		assertEquals(AllCards.aHJo,aStrategy.play(lTrick, lHand));
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
