package comp303.fivehundred.gui.external;

import java.util.Observer;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * @author Martin Robillard
 *
 */
public interface GameObserver extends Observer
{
	void newHand(CardList pNewHand);
	void discard(Card pCard);
}
