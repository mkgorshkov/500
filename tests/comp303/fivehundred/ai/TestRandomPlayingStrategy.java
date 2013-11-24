/**
 * @author Maxim Gorshkov
 */
package comp303.fivehundred.ai;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.ai.RandomPlayingStrategy;

public class TestRandomPlayingStrategy
{
	@Test
	public void testPlay(){
		//hand to test returning anything but joker
		Hand test1 = new Hand();
		test1.add(AllCards.aHJo);
		test1.add(AllCards.aLJo);
		test1.add(AllCards.a4C);
		test1.add(AllCards.a4D);

		
		//only two left, select non joker
		//select leading suit
		Hand test2 = new Hand();
		test2.add(AllCards.a5C);
		test2.add(AllCards.a5D);
		test2.add(AllCards.a5S);
		test2.add(AllCards.a5H);
		
		//if has leading suit
		Hand test3 = new Hand();
		test3.add(AllCards.a6C);
		test3.add(AllCards.a6D);
		test3.add(AllCards.a6S);
		test3.add(AllCards.a6H);
		
		Hand test4 = new Hand();
		test4.add(AllCards.a7C);
		test4.add(AllCards.a8C);
		test4.add(AllCards.a7S);
		test4.add(AllCards.a7H);

		//most powerful hand to make sure ai first
		
		Trick first = new Trick(new Bid(24));
		
		RandomPlayingStrategy myRobot = new RandomPlayingStrategy();
		
		//make sure that we return anything but a joker
		//prelim tests
		assertFalse(myRobot.play(first, test1).isJoker());

		//first we get myRobot to lead with a nonjoker.
		assertEquals(first.size(), 0);
		Card card1 = myRobot.play(first, test1);
		assertTrue(!card1.isJoker());
		first.add(card1);
		assertEquals(first.size(), 1);
	
		//check same suit 
		Card card2 = myRobot.play(first, test2);
		assertEquals(card1.getSuit(), card2.getSuit());
		first.add(card2);
		assertEquals(first.size(), 2);
		
		//check same suit
		Card card3 = myRobot.play(first, test3);
		assertEquals(card2.getSuit(), card3.getSuit());
		first.add(card3);
		assertEquals(first.size(), 3);
		
		//check same suit, for not found, give other random card
		Card card4 = myRobot.play(first, test4);
		first.add(card4);
		assertEquals(first.size(), 4);
		
		
	}
}
