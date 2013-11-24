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
 * An AI robot that plays with random strategy.
 */
public class RandomRobot extends Player
{
	private RandomBiddingStrategy aBiddingStrategy = new RandomBiddingStrategy();
	private RandomCardExchangeStrategy aCardExchangeStrategy = new RandomCardExchangeStrategy();
	private RandomPlayingStrategy aPlayingStrategy = new RandomPlayingStrategy();
	
	/**
	 * A constructor which sets the difficulty.
	 */
	public RandomRobot()
	{
		super();
		setDifficulty("Random");
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		return aBiddingStrategy.selectBid(pPreviousBids, pHand);
	}

	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		return aCardExchangeStrategy.selectCardsToDiscard(pBids, pIndex, pHand);
	}

	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		return aPlayingStrategy.play(pTrick, pHand);
	}
}
