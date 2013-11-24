/**
 * @author Maxim Gorshkov
 */
package comp303.fivehundred.model;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.util.Deck;
import comp303.fivehundred.util.Card.Suit;

public class TestBid
{

	@Test(expected = ModelException.class)
	public void testSuit()
	{

		// setting up the different tricks and suits
		Bid myBid1 = new Bid(6, Suit.CLUBS);
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		Bid myBid3 = new Bid(8, Suit.SPADES);
		Bid myBid4 = new Bid(9, Suit.DIAMONDS);
		Bid myBid5 = new Bid(10, null);
		Bid myBid6 = new Bid();

		// making sure that the suits works
		assertEquals(myBid1.getSuit(), Suit.CLUBS);
		assertEquals(myBid2.getSuit(), Suit.HEARTS);
		assertEquals(myBid3.getSuit(), Suit.SPADES);
		assertEquals(myBid4.getSuit(), Suit.DIAMONDS);

		// making sure that null is thrown
		assertEquals(myBid5.getSuit(), null);
		
		// making sure a passing bid throws a ModelException
		assertEquals(myBid6.getSuit(), null);
		
	}

	@Test
	public void testIsPass()
	{
		// No suit being compared
		Bid myBid1 = new Bid(10, null);
		assertTrue(!myBid1.isPass());

		// Bid compared that's not a pass
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		assertTrue(!myBid2.isPass());

		// Passed bid
		Bid myBid3 = new Bid();
		assertTrue(myBid3.isPass());
	}

	@Test
	public void testNoTrump()
	{
		// No trump selected
		Bid myBid1 = new Bid(10, null);
		assertTrue(myBid1.isNoTrump());

		// Regular trump selected
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		assertTrue(!myBid2.isNoTrump());

		// Passed bid
		Bid myBid3 = new Bid();
		assertTrue(myBid3.isNoTrump());
	}

	@Test(expected = ModelException.class)
	public void testGetTricksBid()
	{
		// setting up the different tricks and suits
		Bid myBid1 = new Bid(6, Suit.CLUBS);
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		Bid myBid3 = new Bid(8, Suit.SPADES);
		Bid myBid4 = new Bid(9, Suit.DIAMONDS);
		Bid myBid5 = new Bid(10, null);
		Bid myBid6 = new Bid();

		// making sure that the number of tricks
		assertEquals(myBid1.getTricksBid(), 6);
		assertEquals(myBid2.getTricksBid(), 7);
		assertEquals(myBid3.getTricksBid(), 8);
		assertEquals(myBid4.getTricksBid(), 9);

		// make sure that we return that no bid
		// was placed
		assertEquals(myBid6.getTricksBid(), 0);

		// making sure that null is thrown
		assertEquals(myBid5.getTricksBid(), null);
	}

	@Test
	public void testBidIndex()
	{ // setting up different pIndices
		Bid myBid2 = new Bid(6);
		Bid myBid3 = new Bid(12);
		Bid myBid4 = new Bid(18);
		Bid myBid5 = new Bid(24);

		// comparing them to actual values (tricks) assertEquals(myBid1.getTricksBid(), 6);
		assertEquals(myBid2.getTricksBid(), 7);
		assertEquals(myBid3.getTricksBid(), 8);
		assertEquals(myBid4.getTricksBid(), 9);
		assertEquals(myBid5.getTricksBid(), 10);

		// comparing them to actual values (suits) assertEquals(myBid1.getSuit(), Suit.SPADES);
		assertEquals(myBid2.getSuit(), Suit.CLUBS);
		assertEquals(myBid3.getSuit(), Suit.DIAMONDS);
		assertEquals(myBid4.getSuit(), Suit.HEARTS);

		// check if it catches error
		assertEquals(myBid5.getSuit(), null);

	}

	@Test
	public void testCompareTo()
	{

		// corner bids (10 spades & 9 no trump)
		Bid lowTen = new Bid(10, Suit.SPADES);
		Bid highNine = new Bid(9, null);

		assertEquals(lowTen.compareTo(highNine), 1);

		// same suit different trick
		Bid sevenH = new Bid(7, Suit.HEARTS);
		Bid tenH = new Bid(10, Suit.HEARTS);

		assertEquals(sevenH.compareTo(tenH), -1);

		// same trick different suit
		Bid sixC = new Bid(6, Suit.CLUBS);
		Bid sixH = new Bid(6, Suit.HEARTS);

		assertEquals(sixC.compareTo(sixH), -1);

		// same trick and suit
		Bid eightD = new Bid(8, Suit.DIAMONDS);
		Bid eightD2 = new Bid(8, Suit.DIAMONDS);

		assertEquals(eightD.compareTo(eightD2), 0);
	}

	@Test
	public void testToString()
	{
		// setting up the different tricks and suits
		Bid myBid1 = new Bid(6, Suit.CLUBS);
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		Bid myBid3 = new Bid();
		Bid myBid4 = new Bid(0, null);

		/*
		 * ANDREW JAME DASHIEL: ARE THE LAST TWO VALID OR SHOULD THEY BE DIFFERENT
		 */
		assertEquals(myBid1.toString(), "6 CLUBS");
		assertEquals(myBid2.toString(), "7 HEARTS");
		assertEquals(myBid3.toString(), "-1 null");
		assertEquals(myBid4.toString(), "0 null");
	}

	@Test
	public void testEquals()
	{
		// setting up tricks and suits
		Bid myBid1 = new Bid(6, Suit.CLUBS);
		Bid myBid2 = new Bid(6, Suit.CLUBS);
		Bid myBid3 = new Bid(6, Suit.SPADES);
		Bid myBid4 = new Bid(7, Suit.SPADES);
		Bid myBid5 = new Bid();
		Bid myBid6 = new Bid();

		// check the same
		assertTrue(myBid1.equals(myBid2));

		// check the same trick
		assertTrue(!myBid2.equals(myBid3));

		// check the same suit
		assertTrue(!myBid3.equals(myBid4));

		// check empty
		// jamie, shouldn't passing bid be equal?
		assertTrue(myBid5.equals(myBid6));

		// empty vs non-empty
		assertTrue(!myBid5.equals(myBid4));
		
		//test exception
		assertTrue(!myBid5.equals(new Deck()));

	}

	@Test
	public void testHashcode()
	{
		// setting up different tricks and suits
		Bid myBid1 = new Bid(6, Suit.CLUBS);
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		Bid myBid3 = new Bid(8, Suit.SPADES);
		Bid myBid4 = new Bid(9, Suit.DIAMONDS);
		Bid myBid5 = new Bid(10, null);
		Bid myBid6 = new Bid();

		// check with the hashvalue
		assertEquals(myBid1.hashCode(), 1);
		assertEquals(myBid2.hashCode(), 8);
		assertEquals(myBid3.hashCode(), 10);
		assertEquals(myBid4.hashCode(), 17);
		assertEquals(myBid5.hashCode(), 24);
		assertEquals(myBid6.hashCode(), 25);

	}

	@Test
	public void testToIndex()
	{
		// set up the different tricks and suits
		Bid myBid1 = new Bid(6, Suit.CLUBS);
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		Bid myBid3 = new Bid(8, Suit.SPADES);
		Bid myBid4 = new Bid(9, Suit.DIAMONDS);
		Bid myBid5 = new Bid(10, null);

		// comparing them to actual values (tricks)
		assertEquals(myBid1.toIndex(), 1);
		assertEquals(myBid2.toIndex(), 8);
		assertEquals(myBid3.toIndex(), 10);
		assertEquals(myBid4.toIndex(), 17);
		assertEquals(myBid5.toIndex(), 24);

	}

	@Test
	public void testGetMax()
	{
		// set up different tricks and suits
		Bid myBid1 = new Bid(6, Suit.CLUBS);
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		Bid myBid3 = new Bid(8, Suit.SPADES);
		Bid myBid4 = new Bid(9, Suit.DIAMONDS);
		Bid myBid6 = new Bid();

		Bid[] myBID = { myBid1, myBid2, myBid3, myBid4 };
		Bid[] myBID2 = { myBid6, myBid6, myBid6, myBid6 };

		// can you guys make sure that this actually makes sense?
		assertTrue(Bid.max(myBID2).isPass());
		assertEquals(Bid.max(myBID), myBid4);
	}

	@Test (expected = ModelException.class)
	public void testGetScore()
	{
		// set up the different tricks and suits
		Bid myBid1 = new Bid(6, Suit.CLUBS);
		Bid myBid2 = new Bid(7, Suit.HEARTS);
		Bid myBid3 = new Bid(8, Suit.SPADES);
		Bid myBid4 = new Bid(9, Suit.DIAMONDS);
		Bid myBid5 = new Bid(10, null);
		Bid myBid6 = new Bid();

		// comparing them to actual values (tricks)
		assertEquals(myBid1.getScore(), 60);
		assertEquals(myBid2.getScore(), 200);
		assertEquals(myBid3.getScore(), 240);
		assertEquals(myBid4.getScore(), 380);
		assertEquals(myBid5.getScore(), 520);
		
		//testing to throw exception
		myBid6.getScore();

	}

}
