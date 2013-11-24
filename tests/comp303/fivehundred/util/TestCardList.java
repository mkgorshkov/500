/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.util.Card.Suit;

import java.util.Iterator;

public class TestCardList
{
	@Test
	public void testBasics()
	{
		//Creating a test object
		CardList test = new CardList();
		
		//Checking size and add methods agree
		assertTrue(test.size() == 0);
		
		test.add(aKS);
		assertTrue(test.size() == 1);
		
		test.add(aKH);
		assertTrue(test.size() == 2);
	}
	
	@Test (expected = GameUtilException.class)
	public void testGetFirst()
	{
		//Creating testing objects
		CardList test = new CardList();
		CardList test2 = new CardList();
		
		//Checking getFirst() with proper inputs
		test.add(aKS);
		assertSame(test.getFirst(), aKS);
		
		test.add(aKH);
		assertSame(test.getFirst(), aKS);
		
		//Checking getFirst() with improper input
		test2.getFirst();
	}
	
	@Test (expected = GameUtilException.class)
	public void testGetLast()
	{
		//Creating testing objects
		CardList test = new CardList();
		CardList test2 = new CardList();
		
		//Checking getFirst() with proper inputs
		test.add(aKS);
		assertSame(test.getLast(), aKS);
				
		test.add(aKH);
		assertSame(test.getLast(), aKH);
				
		//Checking getFirst() with improper input
		test2.getLast();
	}
	
	@Test (expected = GameUtilException.class)
	public void testRemove()
	{
		//Creating test objects
		CardList test = new CardList();
		Card test2 = new Card(null, null);
		
		//Testing that removing from an empty list isn't a problem
		test.remove(a8C);
		
		//Adding some card in order to test removal
		test.add(aKS);
		test.add(a4C);
		test.add(aTH);
		
		//Testing with improper input
		test.remove(test2);
		assertTrue(test.size() == 3);
		
		//Testing with proper input
		test.remove(a4C);
		assertTrue(test.size() == 2);
		assertSame(test.getFirst(), aKS);
		assertSame(test.getLast(), aTH);
		test.remove(aKS);
		assertTrue(test.size() == 1);
		assertSame(test.getFirst(), aTH);
		assertSame(test.getLast(), aTH);
		test.remove(a4C);
		assertTrue(test.size() == 1);
		assertSame(test.getFirst(), aTH);
		assertSame(test.getLast(), aTH);
		test.remove(aTH);
		assertTrue(test.size() == 0);
		
		test.getFirst();
	}
	
	@Test
	public void testClone()
	{
		//Setting up original object and clone
		CardList test = new CardList();
		test.add(aKS);
		test.add(a4H);
		CardList testClone = test.clone();
		
		//Testing the clone for similarity
		assertTrue(test.size() == testClone.size());
		assertTrue(testClone.size() == 2);
		assertEquals(test.getFirst().getRank(), testClone.getFirst().getRank());
		assertEquals(test.getFirst().getSuit(), testClone.getFirst().getSuit());
		assertEquals(test.getLast().getRank(), testClone.getLast().getRank());
		assertEquals(test.getLast().getSuit(), testClone.getLast().getSuit());
		
		//Testing for independence
		assertNotSame(test, testClone);
		test.add(aHJo);
		assertTrue(test.size() == 3);
		assertTrue(testClone.size() == 2);
		testClone.remove(aKS);
		assertTrue(test.size() == 3);
		assertTrue(testClone.size() == 1);
		
	}
	
	@Test
	public void testToString()
	{
		//Setting up the object
		CardList test = new CardList();
		test.add(aKS);
		test.add(aAC);
		
		//Testing toString()
		assertEquals("[KING of SPADES" + ", " +"ACE of CLUBS]", test.toString());
	}
	
	@Test
	public void testIterator()
	{
		//Setting up the object and the iterator
		CardList test = new CardList();
		test.add(aKS);
		test.add(a8H);
		test.add(aHJo);
		
		Iterator<Card> test2 = test.iterator();
		
		//Testing the iterator
		assertTrue(test2.hasNext());
		assertSame(test2.next(), test.getFirst());
		test2.next();
		assertSame(test2.next(), test.getLast());
		assertTrue(!test2.hasNext());
	}
	
	@Test
	public void testRandom()
	{
		//Setting up test object
		CardList test = new CardList();
		test.add(aKS);
		
		//Testing that random works properly on a list of 1 element
		assertSame(aKS, test.random());
	}
	
	@Test
	public void testSort()
	{
		//Setting up test objects and comparators
		CardList test1 = new CardList();
		CardList test2;
		CardList test3;
		CardList test4;
		Card.ByRankComparator comparator1 = new Card.ByRankComparator();
		Card.BySuitComparator comparator2 = new Card.BySuitComparator();
		comparator2.setTrump(Suit.SPADES);
		Card.BySuitNoTrumpComparator comparator3 = new Card.BySuitNoTrumpComparator();
		test1.add(aKS);
		test1.add(aTH);
		test1.add(aTC);
		
		//Testing sort with ByRankComparator
		test2 = test1.sort(comparator1);
		assertSame(test1.getFirst(), aKS);
		assertSame(test1.getLast(), aTC);
		assertSame(test2.getFirst(), aTC);
		assertSame(test2.getLast(), aKS);
		
		//Testing sort with BySuitNoTrumpComparator
		test3 = test1.sort(comparator3);
		assertSame(test1.getFirst(), aKS);
		assertSame(test1.getLast(), aTC);
		assertSame(test3.getFirst(), aKS);
		assertSame(test3.getLast(), aTH);
		
		//Testing sort with BySuitComparator using SPADES as a trump
		test4 = test1.sort(comparator2);
		assertSame(test4.getFirst(), aTC);
		assertSame(test4.getLast(), aKS);
		
		
	}
	
	
}
