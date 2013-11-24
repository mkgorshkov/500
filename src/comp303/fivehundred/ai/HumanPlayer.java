/**
 * @author Dashiel Siegel
 */ 
package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * The HumanPlayer is a subclass of the abstract class Player. The methods currently have
 * default return statements and will be filled in for milestone 3.
 *
 */
public class HumanPlayer extends Player
{

	/**
	 * A constructor which sets the difficulty.
	 */
	public HumanPlayer()
	{
		setDifficulty("User");
	}

	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		return new Bid();
	}

	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		return null;
	}

	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		return null;
	}

}
