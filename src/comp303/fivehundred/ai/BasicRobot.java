/**
 * @Author Dashiel Siegel
 */

package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * An AI robot that plays with basic strategy.
 */
public class BasicRobot extends Player
{
	private BasicBiddingStrategy aBiddingStrategy = new BasicBiddingStrategy();
	private BasicCardExchangeStrategy aCardExchangeStrategy = new BasicCardExchangeStrategy();
	private BasicPlayingStrategy aPlayingStrategy = new BasicPlayingStrategy();
	
	/**
	 * A constructor which sets the difficulty.
	 */
	public BasicRobot()
	{
		super();
		setDifficulty("Basic");
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
