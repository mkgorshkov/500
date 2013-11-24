/**
 * @author James McCorriston
 */
package comp303.fivehundred.ai;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Suit;
import static comp303.fivehundred.util.AllCards.*;
import comp303.fivehundred.ai.BasicPlayingStrategy;

public class TestBasicPlayingStrategy
{
	@Test
	public void testPlay(){
		//Hand to test returning non-joker when leading and no trump contract.
		Hand test1 = new Hand();
		test1.add(aHJo);
		test1.add(aLJo);
		test1.add(aQD);
		test1.add(a9D);
		
		//All the same rank, one of each suit.
		Hand test2 = new Hand();
		test2.add(a5C);
		test2.add(a5D);
		test2.add(a5S);
		test2.add(a5H);
		
		//All the same rank, one of each suit.
		Hand test3 = new Hand();
		test3.add(a6C);
		test3.add(a6D);
		test3.add(a6S);
		test3.add(a6H);
		
		//Hand with 2 clubs of different rank, no diamonds.
		Hand test4 = new Hand();
		test4.add(a7C);
		test4.add(a8C);
		test4.add(a7S);
		test4.add(a7H);

		//Sets the contract to 10 no trump		
		Trick trick = new Trick(new Bid(24));
		
		BasicPlayingStrategy myRobot = new BasicPlayingStrategy();
		
		//Should not lead with a joker when the contract is no trump.
		assertFalse(myRobot.play(trick, test1).isJoker());
		assertFalse(myRobot.play(trick, test2).isJoker());

		//myRobot leads with a diamond (only non-joker suit in test1).
		Card card1 = myRobot.play(trick, test1);
		assertFalse(card1.isJoker());
		trick.add(card1);
		assertEquals(trick.size(), 1);
	
		//test2 must follow suit (diamonds) which means test2 must
		//play the 5 of diamonds (only diamond in test2)
		Card card2 = myRobot.play(trick, test2);
		assertEquals(card2, a5D);
		trick.add(card2);
		assertEquals(trick.size(), 2);
		
		//test3 must follow with the 6 of diamonds
		Card card3 = myRobot.play(trick, test3);
		assertEquals(card3, a6D);
		trick.add(card3);
		assertEquals(trick.size(), 3);
		
		//test4 is void in the trump suit so the lowest non-trump should be
		//played (7 of spades)
		Card card4 = myRobot.play(trick, test4);
		assertEquals(card4, a7S);
		trick.add(card4);
		assertEquals(trick.size(), 4);
			
		
		//Sets the contract to 7 diamonds.		
		trick = new Trick(new Bid(7));
		
		//Swaps the 5 of diamonds in test2 for the jack of hearts and the
		//6 of diamonds in test3 for the jack of diamonds
		test2.remove(a5D);
		test2.add(aJH);
		
		test3.remove(a6D);
		test3.add(aJD);

		//myRobot leads with a diamond or a joker (jokers are considered part of
		//the trump suit which is diamonds at this point).
		card1 = myRobot.play(trick, test1);
		assertTrue(card1.isJoker() || card1.getEffectiveSuit(Suit.DIAMONDS) == Suit.DIAMONDS);
		trick.add(card1);
	
		//test2 must follow suit (diamonds) which means test2 must
		//play the jack of hearts (converse jack).
		card2 = myRobot.play(trick, test2);
		assertEquals(card2, aJH);
		trick.add(card2);
		
		//test3 must follow with the jack of diamonds.
		card3 = myRobot.play(trick, test3);
		assertEquals(card3, aJD);
		trick.add(card3);
		
		//test4 is void in the trump suit so the lowest non-trump should be
		//played (7 of spades).
		card4 = myRobot.play(trick, test4);
		assertEquals(card4, a7S);
		trick.add(card4);
		
		
		//Sets the contract to 7 clubs.		
		trick = new Trick(new Bid(6));
		
		//Swaps the 6 of clubs in test3 for the 4 of hearts and the 5 of clubs
		//in test2 for the jack of spades.
		test3.remove(a6C);
		test3.add(a4H);
		
		test2.remove(a5C);
		test2.add(aJS);
		
		//Removes cards from all 4 hands so everyone only has 2 cards left.
		//The remaining hands are:
		//test1 = High Joker, Low Joker
		//test2 = 5 of Spades, Jack of Spades
		//test3 = 6 of Spades, 4 of Hearts
		//test4 = 7 of Clubs, 8 of Clubs
		test1.remove(aQD);
		test1.remove(a9D);
		test2.remove(aJH);
		test2.remove(a5H);
		test3.remove(a6H);
		test3.remove(aJD);
		test4.remove(a7S);
		test4.remove(a7H);
		
		test1.remove(aHJo);
		test2.remove(aJS);
		test2.add(aHJo);

		//myRobot leads with a joker (jokers are considered part of
		//the trump suit which is clubs at this point).
		card1 = myRobot.play(trick, test1);
		assertEquals(card1, aLJo);
		trick.add(card1);
	
		//test2 must follow suit (clubs) which means test2 must
		//play the jack of spades (converse jack).
		card2 = myRobot.play(trick, test2);
		assertEquals(card2, aHJo);
		trick.add(card2);
		
		//test3 is void in the trump suit so they will play their lowest card
		//(4 of hearts).
		card3 = myRobot.play(trick, test3);
		assertEquals(card3, a4H);
		trick.add(card3);
		
		//test4 must follow with the lower of their two clubs (7 of clubs).
		card4 = myRobot.play(trick, test4);
		assertEquals(card4, a7C);
		trick.add(card4);
		
		
		//Sets the contract to 6 clubs.		
		trick = new Trick(new Bid(1));
		
		//A new set of hands.
		test1 = new Hand();
		test1.add(aTC);
		test1.add(aQC);
		
		test2 = new Hand();
		test2.add(a9S);
		test2.add(aLJo);
		
		test3 = new Hand();
		test3.add(aKC);
		test3.add(aHJo);
		
		test4 = new Hand();
		test4.add(a6H);
		test4.add(aJD);
		
		//myRobot leads with a trump card (10 or Queen of clubs).
		card1 = myRobot.play(trick, test1);
		assertEquals(card1.getEffectiveSuit(trick.getTrumpSuit()), Suit.CLUBS);
		trick.add(card1);
	
		//test2 must follow suit and play the low joker.
		card2 = myRobot.play(trick, test2);
		assertEquals(card2, aLJo);
		trick.add(card2);
		
		//test3 has two clubs and should play the lowest one that can win the trick
		//(high joker).
		card3 = myRobot.play(trick, test3);
		assertEquals(card3, aHJo);
		trick.add(card3);
		
		//test4 is void in the leading suit (trump) and should play the lowest card
		//in their hand (6 of hearts).
		card4 = myRobot.play(trick, test4);
		assertEquals(card4, a6H);
		trick.add(card4);
		
		
		//Sets the contract to 6 clubs.		
		trick = new Trick(new Bid(1));
		
		//A new set of hands.
		test1 = new Hand();
		test1.add(aTC);
		test1.add(aQC);
		
		test2 = new Hand();
		test2.add(a9S);
		test2.add(aHJo);
		
		test3 = new Hand();
		test3.add(aKC);
		test3.add(aLJo);
		
		test4 = new Hand();
		test4.add(a6H);
		test4.add(aJD);
		
		//myRobot leads with a trump card (10 or Queen of clubs).
		card1 = myRobot.play(trick, test1);
		assertEquals(card1.getEffectiveSuit(trick.getTrumpSuit()), Suit.CLUBS);
		trick.add(card1);
	
		//test2 must follow suit and play the low joker.
		card2 = myRobot.play(trick, test2);
		assertEquals(card2, aHJo);
		trick.add(card2);
		
		//test3 has two clubs but neither is high enough to win the trick so the
		//lower of the two should be played.
		card3 = myRobot.play(trick, test3);
		assertEquals(card3, aKC);
		trick.add(card3);
		
		//test4 is void in the leading suit (trump) and should play the lowest card
		//in their hand (6 of hearts).
		card4 = myRobot.play(trick, test4);
		assertEquals(card4, a6H);
		trick.add(card4);
		
		
		//Sets the contract to 6 hearts.		
		trick = new Trick(new Bid(3));
		
		//A new set of hands.
		test1 = new Hand();
		test1.add(aTD);
		test1.add(a9D);
		
		test2 = new Hand();
		test2.add(aQC);
		test2.add(a7D);
		
		test3 = new Hand();
		test3.add(aKC);
		test3.add(aQD);
		
		test4 = new Hand();
		test4.add(a6H);
		test4.add(aJD);
		
		//myRobot leads with a non-trump card (10 or 9 of diamonds).
		card1 = myRobot.play(trick, test1);
		assertEquals(card1.getSuit(), Suit.DIAMONDS);
		trick.add(card1);
	
		//test2 must follow suit and play the 7 of diamonds.
		card2 = myRobot.play(trick, test2);
		assertEquals(card2, a7D);
		trick.add(card2);
		
		//test3 has two diamonds and should play the lowest one that can win the trick
		//(Queen of diamonds).
		card3 = myRobot.play(trick, test3);
		assertEquals(card3, aQD);
		trick.add(card3);
		
		//test4 has two trump cards and should play the lower of the two (6 of hearts)
		//to win the trick.
		card4 = myRobot.play(trick, test4);
		assertEquals(card4, a6H);
		trick.add(card4);
		
		//Sets the contract to 6 hearts.		
		trick = new Trick(new Bid(3));
		
		//A new set of hands.
		test1 = new Hand();
		test1.add(aTD);
		test1.add(a9D);
		
		test2 = new Hand();
		test2.add(aQC);
		test2.add(a6H);
		
		test3 = new Hand();
		test3.add(aJD);
		test3.add(aQD);
		
		test4 = new Hand();
		test4.add(a7H);
		test4.add(a4C);
		
		//myRobot leads with a non-trump card (10 or 9 of diamonds).
		card1 = myRobot.play(trick, test1);
		assertEquals(card1.getSuit(), Suit.DIAMONDS);
		trick.add(card1);
	
		//test2 is void in the leading suit and has a trump card so it should be played
		//(6 of hearts).
		card2 = myRobot.play(trick, test2);
		assertEquals(card2, a6H);
		trick.add(card2);
		
		//test3 has a diamond and a trump card (jack of diamonds) but the diamond must
		//be played as diamonds were led.
		card3 = myRobot.play(trick, test3);
		assertEquals(card3, aQD);
		trick.add(card3);
		
		//test4 has a trump card that can win the trick so it should be played (7 of hearts).
		card4 = myRobot.play(trick, test4);
		assertEquals(card4, a7H);
		trick.add(card4);
		
		
		//Sets the contract to 6 hearts.		
		trick = new Trick(new Bid(3));
		
		//A new set of hands.
		test1 = new Hand();
		test1.add(aTD);
		test1.add(a9D);
		
		test2 = new Hand();
		test2.add(a6D);
		test2.add(a4D);
		
		test3 = new Hand();
		test3.add(aQC);
		test3.add(a6H);
		
		test4 = new Hand();
		test4.add(a5H);
		test4.add(a4C);
		
		//myRobot leads with a non-trump card (10 or 9 of diamonds).
		card1 = myRobot.play(trick, test1);
		assertEquals(card1.getSuit(), Suit.DIAMONDS);
		trick.add(card1);
	
		//test2 has two cards of the leading suit but neither is higher than the highest
		//card in the trick so the lower of the two should be played (4 of diamonds).
		card2 = myRobot.play(trick, test2);
		assertEquals(card2, a4D);
		trick.add(card2);
		
		//test3 is void in the leading suit and has a trump card so it should be played
		//(6 of hearts).
		card3 = myRobot.play(trick, test3);
		assertEquals(card3, a6H);
		trick.add(card3);
		
		//test4 has a trump card but it is not high enough to win the trick so 
		//the lowest card in the hand should be played (4 of clubs).
		card4 = myRobot.play(trick, test4);
		assertEquals(card4, a4C);
		trick.add(card4);
		
		
		//Sets the contract to 6 hearts.		
		trick = new Trick(new Bid(3));
		
		//A new set of hands.
		test1 = new Hand();
		test1.add(aTD);
		test1.add(a9D);
		
		test2 = new Hand();
		test2.add(a6D);
		test2.add(a4D);
		
		test3 = new Hand();
		test3.add(aQC);
		test3.add(a5D);
		
		test4 = new Hand();
		test4.add(aHJo);
		test4.add(a4C);
		
		//myRobot leads with a non-trump card (10 or 9 of diamonds).
		card1 = myRobot.play(trick, test1);
		assertEquals(card1.getSuit(), Suit.DIAMONDS);
		trick.add(card1);
	
		//test2 has two cards of the leading suit but neither is higher than the highest
		//card in the trick so the lower of the two should be played (4 of diamonds).
		card2 = myRobot.play(trick, test2);
		assertEquals(card2, a4D);
		trick.add(card2);
		
		//test3 must follow suit with the 5 of diamonds.
		card3 = myRobot.play(trick, test3);
		assertEquals(card3, a5D);
		trick.add(card3);
		
		//test4 is void in the leading suit but has a trump card (high joker)
		//so that card should be played.
		card4 = myRobot.play(trick, test4);
		assertEquals(card4, aHJo);
		trick.add(card4);
	}
}
