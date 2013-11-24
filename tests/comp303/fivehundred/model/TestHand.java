/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.model;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.model.ModelException;

public class TestHand
{
	@Test
	public void testClone()
	{
		//Setting up original object and clone
		Hand test = new Hand();
		test.add(aKS);
		test.add(a4H);
		Hand testClone = test.clone();
				
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
	public void testCanLead()
	{
		//Setting up the test object
		Hand test = new Hand();
		test.add(aKS);
		test.add(a8H);
		test.add(aHJo);
		test.add(aLJo);
		Hand test2 = new Hand();
		test2.add(aHJo);
		test2.add(aLJo);
		Card.ByRankComparator comparator = new  Card.ByRankComparator();
		
		//Setting up comparable objects
		CardList test3 = new CardList();
		test3.add(aKS);
		test3.add(a8H);
		CardList test4 = new CardList();
		test4.add(aKS);
		test4.add(a8H);
		test4.add(aHJo);
		test4.add(aLJo);
		CardList test5 = new CardList();
		test5.add(aHJo);
		test5.add(aLJo);
		
		//Testing with no trump suit
		assertEquals(test.canLead(true).sort(comparator).toString(), test3.sort(comparator).toString());
		assertEquals(test2.canLead(true).sort(comparator).toString(), test5.sort(comparator).toString());
		
		//Testing with trump suit
		assertEquals(test.canLead(false).sort(comparator).toString(), test4.sort(comparator).toString());
		assertEquals(test2.canLead(false).sort(comparator).toString(), test5.sort(comparator).toString());
	}
	
	@Test
	public void testGetJokers()
	{
		//Creating test objects
		Hand test = new Hand();
		CardList test2 = new CardList();
		Card.ByRankComparator comparator = new  Card.ByRankComparator();
		
		//Testing empty object
		assertEquals(test2.sort(comparator).toString(), test.getJokers().sort(comparator).toString());
		
		//Filling up test object
		test.add(aKS);
		test.add(aHJo);
		test.add(a7H);
		test.add(aLJo);
		test.add(aTC);
		
		//Testing non-empty object
		test2.add(aHJo);
		test2.add(aLJo);
		assertEquals(test2.sort(comparator).toString(), test.getJokers().sort(comparator).toString());
	}
	
	@Test
	public void testGetNonJokers()
	{
		//Creating test objects
		Hand test = new Hand();
		Hand test2 = new Hand();
		CardList test3 = new CardList();
		CardList test4 = new CardList();
		Card.ByRankComparator comparator = new  Card.ByRankComparator();
		
		//Testing empty object
		assertEquals(test3.sort(comparator).toString(), test.getNonJokers().sort(comparator).toString());
		
		//Adding elements to test objects
		test.add(aKS);
		test.add(aHJo);
		test.add(a7H);
		test.add(aLJo);
		test.add(aTC);
		test2.add(aHJo);
		test2.add(aLJo);
		test4.add(aKS);
		test4.add(a7H);
		test4.add(aTC);
		
		//Testing non-empty objects
		assertEquals(test4.sort(comparator).toString(), test.getNonJokers().sort(comparator).toString());
		assertEquals(test3.sort(comparator).toString(), test2.getNonJokers().sort(comparator).toString());
	}
	
	@Test
	public void testGetTrumpCards()
	{
		//Setting up test objects
		Hand test = new Hand();
		CardList test2 = new CardList();
		Card.ByRankComparator comparator = new  Card.ByRankComparator();
		
		//Testing empty hand
		assertEquals(test2.sort(comparator).toString(), test.getTrumpCards(Suit.SPADES).sort(comparator).toString());
		
		//Filling up test objects
		test.add(aKS);
		test.add(aHJo);
		test.add(a7H);
		test.add(aJC);
		test.add(aTD);
		test.add(aJS);
		test.add(aQC);
		test.add(aLJo);
		test2.add(aKS);
		test2.add(aHJo);
		test2.add(aJC);
		test2.add(aJS);
		test2.add(aLJo);
		
		//Testing functionality
		assertEquals(test2.sort(comparator).toString(), test.getTrumpCards(Suit.SPADES).sort(comparator).toString());
	}
	
	@Test
	public void testGetNonTrumpCards()
	{
		//Setting up test objects
		Hand test = new Hand();
		CardList test2 = new CardList();
		Card.ByRankComparator comparator = new  Card.ByRankComparator();
		
		//Testing empty hand
		assertEquals(test2.sort(comparator).toString(), test.getNonTrumpCards(Suit.SPADES).sort(comparator).toString());
		
		//Filling up test objects
		test.add(aKS);
		test.add(aHJo);
		test.add(a7H);
		test.add(aJC);
		test.add(aTD);
		test.add(aJS);
		test.add(aQC);
		test.add(aLJo);
		test2.add(a7H);
		test2.add(aTD);
		test2.add(aQC);
		
		//Testing functionality
		assertEquals(test2.sort(comparator).toString(), test.getNonTrumpCards(Suit.SPADES).sort(comparator).toString());
	}
	
	@Test (expected = ModelException.class)
	public void testSelectLowest()
	{
		//Creating test objects
		Hand test = new Hand();
		Hand test2 = new Hand();
		
		//Adding elements into hand
		test.add(aHJo);
		test.add(aLJo);
		//Testing with no trump
		assertSame(aLJo, test.selectLowest(null));
		
		//Testing with trump
		assertSame(aLJo, test.selectLowest(Suit.CLUBS));
		
		//Adding more elements
		test.add(aJC);
		
		//Testing with no trump
		assertSame(aJC, test.selectLowest(null));
		
		//Testing with trump
		assertSame(aJC, test.selectLowest(Suit.CLUBS));
		
		//Adding more elements
		test.add(aTH);
		test.add(aJS);
		test.add(a4C);
		test.add(a5H);
		
		//Testing with no trump
		assertSame(a4C, test.selectLowest(null));
		
		//Testing with trump
		assertSame(a5H, test.selectLowest(Suit.CLUBS));
		
		//Checking empty hand functionality
		test2.selectLowest(null);
	}
	
	@Test
	public void testPlayableCards()
	{
		//Creating test object
		Hand test = new Hand();
		Hand emptyHand = new Hand();
		Hand noTrumps = new Hand();
		CardList empty = new CardList();
		CardList spadesTrumpSolution = new CardList();
		CardList clubsTrumpSolution = new CardList();
		CardList heartsTrumpSolution = new CardList();
		CardList diamondsTrumpSolution = new CardList();
		CardList spadesNoTrumpSolution = new CardList();
		CardList clubsNoTrumpSolution = new CardList();
		CardList heartsNoTrumpSolution = new CardList();
		CardList diamondsNoTrumpSolution = new CardList();
		CardList nullTrumpSolution = new CardList();
		CardList nullNoTrumpSolution = new CardList();
		CardList noTrumpsHandSolution = new CardList();
		Card.ByRankComparator comparator = new  Card.ByRankComparator();
		
		//Testing empty hand
		assertEquals(empty.toString(), emptyHand.playableCards(null, null).toString());
		
		//Filling test object
		test.add(aJS);
		test.add(a8S);
		test.add(aHJo);
		test.add(aJC);
		test.add(aAC);
		test.add(aLJo);
		test.add(aJD);
		test.add(aTD);
		noTrumps.add(a8H);
		noTrumps.add(aJD);
		noTrumps.add(aAC);
		
		//Filling out solutions assuming Spades to be trump suit
		spadesTrumpSolution.add(aJS);
		spadesTrumpSolution.add(a8S);
		spadesTrumpSolution.add(aHJo);
		spadesTrumpSolution.add(aJC);
		spadesTrumpSolution.add(aLJo);
		clubsTrumpSolution.add(aAC);
		heartsTrumpSolution.add(aJS);
		heartsTrumpSolution.add(a8S);
		heartsTrumpSolution.add(aHJo);
		heartsTrumpSolution.add(aJC);
		heartsTrumpSolution.add(aAC);
		heartsTrumpSolution.add(aLJo);
		heartsTrumpSolution.add(aJD);
		heartsTrumpSolution.add(aTD);
		diamondsTrumpSolution.add(aJD);
		diamondsTrumpSolution.add(aTD);
		spadesNoTrumpSolution.add(aJS);
		spadesNoTrumpSolution.add(a8S);
		spadesNoTrumpSolution.add(aHJo);
		spadesNoTrumpSolution.add(aLJo);
		clubsNoTrumpSolution.add(aHJo);
		clubsNoTrumpSolution.add(aJC);
		clubsNoTrumpSolution.add(aAC);
		clubsNoTrumpSolution.add(aLJo);
		heartsNoTrumpSolution.add(aJS);
		heartsNoTrumpSolution.add(a8S);
		heartsNoTrumpSolution.add(aHJo);
		heartsNoTrumpSolution.add(aJC);
		heartsNoTrumpSolution.add(aAC);
		heartsNoTrumpSolution.add(aLJo);
		heartsNoTrumpSolution.add(aJD);
		heartsNoTrumpSolution.add(aTD);
		diamondsNoTrumpSolution.add(aHJo);
		diamondsNoTrumpSolution.add(aLJo);
		diamondsNoTrumpSolution.add(aJD);
		diamondsNoTrumpSolution.add(aTD);
		nullTrumpSolution.add(aJS);
		nullTrumpSolution.add(a8S);
		nullTrumpSolution.add(aHJo);
		nullTrumpSolution.add(aJC);
		nullTrumpSolution.add(aAC);
		nullTrumpSolution.add(aLJo);
		nullTrumpSolution.add(aJD);
		nullTrumpSolution.add(aTD);
		nullNoTrumpSolution.add(aJS);
		nullNoTrumpSolution.add(a8S);
		nullNoTrumpSolution.add(aJC);
		nullNoTrumpSolution.add(aAC);
		nullNoTrumpSolution.add(aJD);
		nullNoTrumpSolution.add(aTD);
		noTrumpsHandSolution.add(a8H);
		noTrumpsHandSolution.add(aJD);
		noTrumpsHandSolution.add(aAC);
		
		//Checking Functionality
		assertEquals(spadesTrumpSolution.sort(comparator).toString(),
					test.playableCards(Suit.SPADES, Suit.SPADES).sort(comparator).toString());
		assertEquals(clubsTrumpSolution.sort(comparator).toString(), 
					test.playableCards(Suit.CLUBS, Suit.SPADES).sort(comparator).toString());
		assertEquals(heartsTrumpSolution.sort(comparator).toString(), 
					test.playableCards(Suit.HEARTS, Suit.SPADES).sort(comparator).toString());
		assertEquals(diamondsTrumpSolution.sort(comparator).toString(), 
					test.playableCards(Suit.DIAMONDS, Suit.SPADES).sort(comparator).toString());
		assertEquals(spadesNoTrumpSolution.sort(comparator).toString(), 
					test.playableCards(Suit.SPADES, null).sort(comparator).toString());
		assertEquals(clubsNoTrumpSolution.sort(comparator).toString(), 
					test.playableCards(Suit.CLUBS, null).sort(comparator).toString());
		assertEquals(heartsNoTrumpSolution.sort(comparator).toString(), 
					test.playableCards(Suit.HEARTS, null).sort(comparator).toString());
		assertEquals(diamondsNoTrumpSolution.sort(comparator).toString(), 
					test.playableCards(Suit.DIAMONDS, null).sort(comparator).toString());
		assertEquals(nullTrumpSolution.sort(comparator).toString(), 
					test.playableCards(null, Suit.SPADES).sort(comparator).toString());
		assertEquals(nullNoTrumpSolution.sort(comparator).toString(), 	
					test.playableCards(null, null).sort(comparator).toString());
		assertEquals(noTrumpsHandSolution.sort(comparator).toString(),
					noTrumps.playableCards(Suit.SPADES, Suit.SPADES).sort(comparator).toString());
	}
	
	@Test
	public void testNumberOfCards()
	{
		//Creating test object
		Hand test = new Hand();
		
		//Testing empty hand
		assertTrue(test.numberOfCards(Suit.SPADES, Suit.SPADES) == 0);
		
		//Creating a hand
		test.add(aJS);
		test.add(a8S);
		test.add(aHJo);
		test.add(aJC);
		test.add(aAC);
		test.add(a4C);
		test.add(aLJo);
		test.add(aJD);
		test.add(a5H);
		test.add(a6H);
		test.add(a7H);
		test.add(a8H);
		
		//Testing functionality with Spades as the trump suit
		assertTrue(test.numberOfCards(Suit.SPADES, Suit.SPADES) == 3);
		assertTrue(test.numberOfCards(Suit.CLUBS, Suit.SPADES) == 2);
		
		assertTrue(test.numberOfCards(Suit.HEARTS, Suit.SPADES) == 4);
		assertTrue(test.numberOfCards(Suit.DIAMONDS, Suit.SPADES) == 1);
		
	}
}
