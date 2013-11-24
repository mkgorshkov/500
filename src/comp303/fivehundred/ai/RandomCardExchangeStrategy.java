/**
 * @author James McCorriston
 */

package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.CardList;

/**
 * Picks six cards at random. 
 */
public class RandomCardExchangeStrategy implements ICardExchangeStrategy
{
	public static final int HANDSIZE = 10;
	public static final int INITIAL_HAND_SIZE = 16;
	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		boolean oneNonPass = false;
		for (Bid bid : pBids)
		{
			//Checks if there has been at least one non-passing bid.
			if (!bid.isPass())
			{
				oneNonPass = true;
			}
		}
		//There must be at least one non-passing bid for cards to be discarded.
		assert oneNonPass;
		assert pBids.length == 4;
		assert pIndex >= 0 && pIndex <= 3;
		assert pHand.size() == INITIAL_HAND_SIZE;
		
		//returnList is a copy of the hand.
		CardList returnList = pHand.clone();
		int i;
		//Removes 10 cards from the copy of the hand leaving 6 (to be discarded).
		for(i = 0; i<HANDSIZE; i++)
		{
			returnList.remove(returnList.random());
		}
		return returnList;
	}

}
