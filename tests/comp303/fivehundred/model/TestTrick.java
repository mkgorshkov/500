/**
 * @author Dashiel Siegel
 */

package comp303.fivehundred.model;

import static org.junit.Assert.*;
import static comp303.fivehundred.util.AllCards.*;

import org.junit.Test;


import comp303.fivehundred.model.Trick;
import comp303.fivehundred.model.Bid;

import comp303.fivehundred.util.Card.Suit;

public class TestTrick
{
	Bid bid = new Bid(10,Suit.DIAMONDS);
	Trick trick = new Trick(bid);
	
	Bid bidNoTrump = new Bid(10, null);
	Trick trickNoTrump = new Trick(bidNoTrump);
	
	@Test
	public void testGetTrumpSuit()
	{
		assertSame(trick.getTrumpSuit(),Suit.DIAMONDS);
		assertSame(trickNoTrump.getTrumpSuit(),null);

	}
	
	@Test
	public void testGetSuitLed()
	{
		//check null return when CardList<> is empty
		assertSame(trick.getSuitLed(),null);
		assertSame(trickNoTrump.getSuitLed(),null);

		//NO_TRUMP
		//check null return when Joker is led
		trickNoTrump.add(aHJo);
		assertSame(trickNoTrump.getSuitLed(),null);
		trickNoTrump.remove(aHJo);
		
		//TRUMP
		//check trump return when Joker is led
		trick.add(aHJo);
		assertSame(trick.getSuitLed(),Suit.DIAMONDS);
		trick.remove(aHJo);
	
		//check same suit returned as led (diamonds)
		//check for both a normal card and a jack (to test possible effective suit errors)
		trick.add(aAD);
		assertSame(trick.getSuitLed(),Suit.DIAMONDS);
		trick.remove(aAD);
		trick.add(aJD);
		assertSame(trick.getSuitLed(),Suit.DIAMONDS);
		trick.remove(aJD);

		//check it again (hearts, since it is converse trump to test possible effective suit errors)
		trick.add(a7H);
		assertSame(trick.getSuitLed(),Suit.HEARTS);
		trick.remove(a7H);
		trick.add(aJH);
		assertSame(trick.getSuitLed(),Suit.DIAMONDS);
		trick.remove(aJH);			
	
		//check it again (clubs)
		trick.add(aKC);
		assertSame(trick.getSuitLed(),Suit.CLUBS);
		trick.remove(aKC);
		trick.add(aJC);
		assertSame(trick.getSuitLed(),Suit.CLUBS);
		trick.remove(aJC);
	}		

	@Test
	public void testJokerLed()
	{
		//ONLY CHECKING TRUMP CONTRACT, NO_TRUMP SHOULD BE THE SAME
		//test false if empty
		assertFalse(trick.jokerLed());
		
		//test false if non-joker led
		trick.add(a7H);
		assertFalse(trick.jokerLed());
		trick.remove(a7H);

		//test true if HJo led
		trick.add(aHJo);
		assertTrue(trick.jokerLed());
		trick.remove(aHJo);

		//test true if LJo led
		trick.add(aLJo);
		assertTrue(trick.jokerLed());
		trick.remove(aLJo);
	}
	
	@Test
	public void testCardLed()
	{
		//ONLY CHECKING TRUMP CONTRACT, NO_TRUMP SHOULD BE THE SAME
		//check for a random normal card and a joker
		trick.add(a4C);
		assertSame(a4C,trick.cardLed());
		trick.remove(a4C);
		trick.add(aHJo);
		assertSame(aHJo,trick.cardLed());
		trick.remove(aHJo);
	}
	
	@Test
	public void testHighest()
	{
		//ONLY CHECKING TRUMP CONTRACT, NO_TRUMP SHOULD BE THE SAME
		//superiority of jokers
		trick.add(a4C);
		trick.add(a9C);
		trick.add(aJC);
		trick.add(aLJo);
		assertSame(trick.highest(),aLJo);
		trick.remove(a4C);
		trick.add(aHJo);
		assertSame(trick.highest(),aHJo);
		trick.remove(aHJo);
		trick.remove(aLJo);
		trick.remove(aJC);
		trick.remove(a9C);

		//superiority of special jacks (bowers)
		trick.add(a4D);
		trick.add(aQD);
		trick.add(aJS);
		trick.add(aJH);
		assertSame(trick.highest(),aJH);
		trick.remove(a4D);
		trick.add(aJD);
		assertSame(trick.highest(),aJD);
		trick.remove(aJD);
		trick.remove(aJH);
		trick.remove(aJS);
		trick.remove(aQD);
		
		//superiority of trumps
		trick.add(a8H);
		trick.add(a8C);
		trick.add(a9H);
		trick.add(a7D);
		assertSame(trick.highest(),a7D);
		trick.remove(a8C);
		trick.add(a9D);
		assertSame(trick.highest(),a9D);
		trick.remove(a9D);
		trick.remove(a7D);
		trick.remove(a9H);
		trick.remove(a8H);
		
		//superiority of suit, no trumps
		trick.add(a8C);
		trick.add(a8S);
		trick.add(a9H);
		trick.add(a5H);
		assertSame(trick.highest(),a8C);
		trick.remove(a8S);
		trick.add(a9C);
		assertSame(trick.highest(),a9C);
		trick.remove(a9C);
		trick.remove(a5H);
		trick.remove(a9H);
		trick.remove(a8C);

		//superiority of rank
		trick.add(a6C);
		trick.add(a7C);
		trick.add(a5C);
		trick.add(a6H);
		assertSame(trick.highest(),a7C);
		trick.remove(a6H);
		trick.remove(a5C);
		trick.remove(a7C);
		trick.remove(a6C);
		
		//NO TRUMP
		//superiority of rank
		trickNoTrump.add(a6C);
		trickNoTrump.add(a7C);
		trickNoTrump.add(a5C);
		trickNoTrump.add(a6H);
		assertSame(trickNoTrump.highest(),a7C);
		//superiority of suit
		trickNoTrump.remove(a6H);
		trickNoTrump.add(a9H);
		assertSame(trickNoTrump.highest(),a7C);
		trickNoTrump.remove(a9H);
		trickNoTrump.remove(a5C);
		trickNoTrump.remove(a7C);
		trickNoTrump.remove(a6C);
	}
	
	@Test(expected = ModelException.class)
	public void testWinnerIndex()
	{
		//TRUMP IN CONTRACT
		//test no trump
		trick.add(a5S);
		trick.add(a4S);
		trick.add(a6S);
		trick.add(a6C);
		assertSame(trick.winnerIndex(),2);
		//test trump
		trick.remove(a4S);
		trick.add(a4D);
		assertSame(trick.winnerIndex(),3);
		//test right bower jack
		trick.remove(a5S);
		trick.add(aJH);
		assertSame(trick.winnerIndex(),3);
		//test left bower jack;
		trick.remove(a6S);
		trick.add(aJD);
		assertSame(trick.winnerIndex(),3);
		//test low joker
		trick.remove(a6C);
		trick.add(aLJo);
		assertSame(trick.winnerIndex(),3);
		//test high joker
		trick.remove(a4D);
		trick.add(aHJo);
		assertSame(trick.winnerIndex(),3);
		trick.remove(aJH);
		assertSame(trick.winnerIndex(),2);
		trick.remove(aJD);
		trick.remove(aLJo);
		trick.remove(aHJo);
		assertEquals(null,trick.winnerIndex());
	}
}
