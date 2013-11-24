/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;

public class TestCard
{
	@Test (expected = GameUtilException.class)
	public void testToString()
	{
		//Setting up a testing object
		Card test = new Card(null, null);
		
		//Testing with proper inputs
		assertEquals( "ACE of CLUBS", aAC.toString());
		assertEquals( "TEN of CLUBS", aTC.toString());
		assertEquals( "JACK of CLUBS", aJC.toString());
		assertEquals( "QUEEN of HEARTS", aQH.toString());
		assertEquals( "KING of SPADES", aKS.toString());
		assertEquals( "QUEEN of DIAMONDS", aQD.toString());
		assertEquals( "HIGH Joker", aHJo.toString());
		
		//Testing with improper input
		test.toString();
	}
	
	@Test
	public void testbasics()
	{
		//Testing isJoker()
		assertTrue(aHJo.isJoker());
		assertTrue(aLJo.isJoker());
		assertTrue(!(aKS.isJoker()));
		
		//Testing getJokerValue()
		assertSame(Joker.HIGH, aHJo.getJokerValue());
		assertSame(Joker.LOW, aLJo.getJokerValue());
		
		//Testing getRank()/getSuit()
		assertSame(Rank.EIGHT, a8S.getRank());
		assertSame(Suit.HEARTS, a8H.getSuit());
		
		//Testing getEffectiveSuit()
		assertSame(Suit.CLUBS, aJS.getEffectiveSuit(Suit.CLUBS));
		assertSame(Suit.HEARTS, a8H.getEffectiveSuit(Suit.CLUBS));
		assertSame(Suit.DIAMONDS, aHJo.getEffectiveSuit(Suit.DIAMONDS));
		assertSame(Suit.SPADES, aLJo.getEffectiveSuit(Suit.SPADES));
		assertSame(Suit.SPADES, aKS.getEffectiveSuit(null));
		assertNull("null okay", aHJo.getEffectiveSuit(null));
	}
	
	@Test (expected = GameUtilException.class)
	public void testToShortString()
	{
		//Setting up a testing object
		Card test = new Card(null, null);
		
		//Testing with proper inputs
		assertEquals( "AC", aAC.toShortString());
		assertEquals( "TC", aTC.toShortString());
		assertEquals( "JC", aJC.toShortString());
		assertEquals( "4H", a4H.toShortString());
		assertEquals( "KS", aKS.toShortString());
		assertEquals( "QD", aQD.toShortString());
		assertEquals( "HJ", aHJo.toShortString());
		
		//Testing with improper input
		test.toShortString();
	}
	
	@Test
	public void testGetConverse()
	{		
		//Testing expected conditions
		assertSame(Suit.SPADES, Suit.CLUBS.getConverse());
		assertSame(Suit.CLUBS, Suit.SPADES.getConverse());
		assertSame(Suit.HEARTS, Suit.DIAMONDS.getConverse());
		assertSame(Suit.DIAMONDS, Suit.HEARTS.getConverse());
	}
	
	@Test (expected = GameUtilException.class)
	public void testCompareTo()
	{
		//Creating a testing object
		Card test = new Card(null, null);
		
		//Checking functionality with proper inputs
		
		//Testing comparison of 2 identical cards
		assertTrue(aHJo.compareTo(aHJo) == 0);
		assertTrue(aLJo.compareTo(aLJo) == 0);
		assertTrue(aKS.compareTo(aKS) == 0);
		
		//Checking that comparison only depends on rank
		assertTrue(aAD.compareTo(aAS) == 0);
		
		//Checking joker hierarchy
		assertTrue(aLJo.compareTo(aHJo) < 0);
		assertTrue(aHJo.compareTo(aLJo) > 0);
		
		//Testing functionality with various generic inputs
		assertTrue(aKS.compareTo(a6D) > 0);
		assertTrue(a4S.compareTo(a5C) < 0);
		assertTrue(aLJo.compareTo(a6D) > 0);
		assertTrue(a9S.compareTo(aLJo) < 0);
		assertTrue(aHJo.compareTo(aTS) > 0);
		assertTrue(aAS.compareTo(aHJo) < 0);
		
		//Testing functionality with improper input
		test.compareTo(a4C);
	}
	
	//Testing other improper inputs for exceptions
	@Test (expected = GameUtilException.class)
	public void testCompareTo2()
	{
		Card test = new Card(null, Suit.CLUBS);
		
		a4C.compareTo(test);
	}
	
	@Test (expected = GameUtilException.class)
	public void testCompareTo3()
	{
		Card test = new Card(Rank.ACE, null);
		
		a4C.compareTo(test);
	}
	
	@Test (expected = GameUtilException.class)
	public void testCompareTo4()
	{
		Card test = new Card(null, null);
		
		a4C.compareTo(test);
	}
	
	@Test (expected = GameUtilException.class)
	public void testCompareTo5()
	{
		Card test = new Card(null, Suit.CLUBS);
		
		test.compareTo(aKS);
	}
	
	@Test (expected = GameUtilException.class)
	public void testCompareTo6()
	{
		Card test = new Card(Rank.ACE, null);
		
		test.compareTo(a8H);
	}
	
	@Test
	public void testEquals()
	{
		//Creation of test objects
		Deck test = new Deck();
		Card test2 = new Card(null, null);
		Card test3 = new Card(Rank.ACE, null);
		Card test4 = new Card(null, Suit.CLUBS);
		
		//Testing functionality under expected conditions
		assertTrue(aKS.equals(aKS));
		assertTrue(a6D.equals(a6D));
		assertTrue(aLJo.equals(aLJo));
		assertTrue(aHJo.equals(aHJo));
		assertTrue(!a6D.equals(a6H));
		assertTrue(!a7S.equals(a7C));
		assertTrue(!aTH.equals(aJH));
		assertTrue(!aKS.equals(aHJo));
		assertTrue(!aHJo.equals(aKS));
		assertTrue(!aKS.equals(aLJo));
		assertTrue(!aLJo.equals(aKS));
		assertTrue(!aLJo.equals(aHJo));
		assertTrue(!aHJo.equals(aLJo));
		
		//Testing reaction to improper parameters
		assertTrue(!aKS.equals(test));		
		assertTrue(!aKS.equals(test2));
		assertTrue(!test2.equals(aKS));
		assertTrue(!aAC.equals(test3));
		assertTrue(!aAC.equals(test4));
		assertTrue(!test3.equals(aAC));
		assertTrue(!test4.equals(aAC));
			
	}
	
	@Test (expected = GameUtilException.class)
	public void testHashCode()
	{
		//Creating test objects
		Deck test = new Deck();
		Card[] cards = new Card[46];
		Card test2 = new Card(Rank.FOUR, Suit.SPADES);
		Card test3 = new Card(Rank.FOUR, Suit.SPADES);
		Card test4 = new Card(null, null);
		
		//Setting up the deck
		test.shuffle();
		
		//Extracting the cards in an array for manageability.
		for(int i = 0; i < 46; i++)
		{
			cards[i] = test.draw();
		}
		
		//Comparing all cards to each other to make sure that all hash codes are unique.
		for(int j = 0; j < 46; j++)
		{
			for(int k = j+1; k < 46; k++)
			{
				assertTrue(!(cards[j].hashCode() == cards[k].hashCode()));
			}
		}
		
		//Testing that 2 identical cards will have the same hash code
		assertTrue(test2.hashCode() == test3.hashCode());
		
		//Expecting an exception with improper input
		test4.hashCode();
			
		
	}
	
	//Testing other improper inputs for exceptions
	@Test (expected = GameUtilException.class)
	public void testHashCode2()
	{
		Card test = new Card(Rank.ACE, null);
		
		test.hashCode();
	}
	
	@Test (expected = GameUtilException.class)
	public void testHashCode3()
	{
		Card test  =  new Card(null, Suit.CLUBS);
		
		test.hashCode();
	}
			
}
