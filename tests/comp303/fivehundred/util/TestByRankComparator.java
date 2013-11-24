/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestByRankComparator
{
	@Test
	public void testByRankComparator()
	{
		Card.ByRankComparator test = new Card.ByRankComparator();
		//Comparing cards of equal value.
		assertTrue(test.compare(aKS, aKS) == 0);
		assertTrue(test.compare(aLJo, aLJo) == 0);
		assertTrue(test.compare(aHJo, aHJo) == 0);
		
		//Testing superiority of jokers
		assertTrue(test.compare(aAS, aLJo) < 0);
		assertTrue(test.compare(aAH, aHJo) < 0);
		assertTrue(test.compare(aLJo, aAC) > 0);
		assertTrue(test.compare(aHJo, aAD) > 0);
		
		//Testing joker hierarchy
		assertTrue(test.compare(aLJo, aHJo) < 0);
		assertTrue(test.compare(aHJo, aLJo) > 0);
		
		//Testing that jacks are not special
		assertTrue(test.compare(aJC, aQC) < 0);
		assertTrue(test.compare(aQH, aJH) > 0);
		
		//Testing that rank has priority over suit
		assertTrue(test.compare(a7D, a6H) > 0);
		assertTrue(test.compare(a6H, a7D) < 0);
		assertTrue(test.compare(a7D, a6S) > 0);
		assertTrue(test.compare(a6S, a7D) < 0);
		
		//Testing that rank comparison works within a suit
		assertTrue(test.compare(a7D, a6D) > 0);
		assertTrue(test.compare(a6D, a7D) < 0);
		
		//Testing that, with equal rank, suit comparison works
		assertTrue(test.compare(a6S, a6C) < 0);
		assertTrue(test.compare(a6C, a6D) < 0);
		assertTrue(test.compare(a6D, a6H) < 0);
		assertTrue(test.compare(a6C, a6S) > 0);
		assertTrue(test.compare(a6D, a6C) > 0);
		assertTrue(test.compare(a6H, a6D) > 0);
	}
	
}
