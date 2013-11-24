/**
 * @author James McCorriston
 */

package comp303.fivehundred.ai;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;

/**
 * If leading, picks a card at random except a joker if the contract is in no trump. If not leading and the hand
 * contains cards that can follow suit,W pick a suit-following card at random. If not leading and the hand does not
 * contain cards that can follow suit, pick a card at random (including trumps, if available).
 */
public class RandomPlayingStrategy implements IPlayingStrategy
{
	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		assert pTrick != null;
		assert pHand != null;
		
		//If the trick is empty.
		if (pTrick.size() == 0)
		{
			//Leads with a random card.
			return pHand.canLead(pTrick.getTrumpSuit() == null).random();
		}
		//If someone has already led.
		else
		{
			//Plays a random playable card.
			return pHand.playableCards(pTrick.getSuitLed(), pTrick.getTrumpSuit()).random();
		}
	}
}
