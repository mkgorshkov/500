/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestBySuitNoTrumpComparator
{
	@Test
	public void testBySuitNoTrumpComparator()
	{
		Card.BySuitNoTrumpComparator test = new Card.BySuitNoTrumpComparator();
		//Comparing cards of equal value
		assertTrue(test.compare(aKS, aKS) == 0);
		assertTrue(test.compare(aLJo, aLJo) == 0);
		assertTrue(test.compare(aHJo, aHJo) == 0);
		
		//Checking joker hierarchy
		assertTrue(test.compare(aHJo, aLJo) > 0);
		assertTrue(test.compare(aLJo, aHJo) < 0);
		
		//Testing superiority of jokers
		assertTrue(test.compare(aAS, aLJo) < 0);
		assertTrue(test.compare(aAH, aHJo) < 0);
		assertTrue(test.compare(aLJo, aAC) > 0);
		assertTrue(test.compare(aHJo, aAD) > 0);
		
		//Making sure jacks are not special
		assertTrue(test.compare(aJC, aQC) < 0);
		assertTrue(test.compare(aQH, aJH) > 0);
		
		//Checking that, at equal rank, suit comparison works
		assertTrue(test.compare(a7H, a7D) > 0);
		assertTrue(test.compare(a7D, a7C) > 0);
		assertTrue(test.compare(a7C, a7S) > 0);
		assertTrue(test.compare(a7D, a7H) < 0);
		assertTrue(test.compare(a7C, a7D) < 0);
		assertTrue(test.compare(a7S, a7C) < 0);
		
		//Checking that suit comparison has priority over rank comparison
		assertTrue(test.compare(a6H, a7D) > 0);
		assertTrue(test.compare(a6D, a7C) > 0);
		assertTrue(test.compare(a6C, a7S) > 0);
		assertTrue(test.compare(a7D, a6H) < 0);
		assertTrue(test.compare(a7C, a6D) < 0);
		assertTrue(test.compare(a7S, a6C) < 0);
		
		//Checking that at equal suit, rank comparison works
		assertTrue(test.compare(a7H, a6H) > 0);
		assertTrue(test.compare(a6H, a7H) < 0);
		
	}
}
