/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.util.Card.Suit;

public class TestBySuitComparator
{
	@Test (expected = GameUtilException.class)
	public void testBySuitComparator()
	{
		Card.BySuitComparator test = new Card.BySuitComparator();
		
		//Setting Spades as Trump
		test.setTrump(Suit.SPADES);
		
		//Comparing cards of equal value
		assertTrue(test.compare(aKS, aKS) == 0);
		assertTrue(test.compare(aLJo, aLJo) == 0);
		assertTrue(test.compare(aHJo, aHJo) == 0);
		assertTrue(test.compare(aJS, aJS) == 0);
		
		//Checking joker hierarchy
		assertTrue(test.compare(aHJo, aLJo) > 0);
		assertTrue(test.compare(aLJo, aHJo) < 0);
		
		//Testing superiority of jokers
		assertTrue(test.compare(aAS, aLJo) < 0);
		assertTrue(test.compare(aAH, aHJo) < 0);
		assertTrue(test.compare(aLJo, aAC) > 0);
		assertTrue(test.compare(aHJo, aAD) > 0);
		assertTrue(test.compare(aJS, aLJo) < 0);
		assertTrue(test.compare(aJS, aHJo) < 0);
		assertTrue(test.compare(aLJo, aJC) > 0);
		assertTrue(test.compare(aHJo, aJC) > 0);
		
		//Testing superiority of jacks
		assertTrue(test.compare(aJC, aAC) > 0);
		assertTrue(test.compare(aAC, aJC) < 0);
		assertTrue(test.compare(aAS, aJS) < 0);
		assertTrue(test.compare(aJS, aAS) > 0);
		assertTrue(test.compare(aJS, aJC) > 0);
		assertTrue(test.compare(aJC, aJS) < 0);
		assertTrue(test.compare(aJC, aJH) > 0);
		assertTrue(test.compare(aJH, aJC) < 0);
		assertTrue(test.compare(aJS, aJH) > 0);
		assertTrue(test.compare(aJH, aJS) < 0);
		assertTrue(test.compare(aJC, aAH) > 0);
		assertTrue(test.compare(aAD, aJC) < 0);
		assertTrue(test.compare(aJS, aAH) > 0);
		assertTrue(test.compare(aAC, aJS) < 0);
		
		
		//Checking that, at equal rank, suit comparison works
		assertTrue(test.compare(a7S, a7H) > 0);
		assertTrue(test.compare(a7H, a7D) > 0);
		assertTrue(test.compare(a7D, a7C) > 0);
		assertTrue(test.compare(a7H, a7S) < 0);
		assertTrue(test.compare(a7D, a7H) < 0);
		assertTrue(test.compare(a7C, a7D) < 0);
		
		//Checking that suit comparison has priority over rank comparison
		assertTrue(test.compare(a6S, a7H) > 0);
		assertTrue(test.compare(a6H, a7D) > 0);
		assertTrue(test.compare(a6D, a7C) > 0);
		assertTrue(test.compare(a7H, a6S) < 0);
		assertTrue(test.compare(a7D, a6H) < 0);
		assertTrue(test.compare(a7C, a6D) < 0);
		
		//Checking that at equal suit, rank comparison works
		assertTrue(test.compare(a7H, a6H) > 0);
		assertTrue(test.compare(a6H, a7H) < 0);
		
		//Testing null Trump
		test.setTrump(null);
		test.compare(aKS, aKS);
	}
}
