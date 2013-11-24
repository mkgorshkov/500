package comp303.fivehundred.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDeck
{
	@Test
	public void testInit()
	{
		Deck deck = new Deck();
		assertEquals(46, deck.size());
		deck.draw();
		assertEquals(45, deck.size());
		for( int i = 0; i < 45; i++ )
		{
			deck.draw();
		}
		assertEquals(0, deck.size());
	}
}
