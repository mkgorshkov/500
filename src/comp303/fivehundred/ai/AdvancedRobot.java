/**
 * @Author James McCorriston
 */

package comp303.fivehundred.ai;

import java.util.Observable;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.model.MemoryList;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.engine.GameEngine;

/**
 * An AI robot that plays with advanced strategy.
 */
public class AdvancedRobot extends Player
{
	//The original hand size of a player.
	private static final int HANDSIZE = 10;
	
	private AdvancedBiddingStrategy aBiddingStrategy = new AdvancedBiddingStrategy();
	private AdvancedCardExchangeStrategy aCardExchangeStrategy = new AdvancedCardExchangeStrategy();
	private AdvancedPlayingStrategy aPlayingStrategy = new AdvancedPlayingStrategy();
	
	//A memory list that stores a list of all cards that could still be in play.
	private MemoryList aMemory = new MemoryList();
	//The index of this advanced AI player, set in the selectBid method.
	private int aIndex = -1;
	//This player's index in the array of bids.
	private int aBidIndex = -1;
	//An array of the bids that were made this round.
	private Bid[] aBids;
	//An array of the team scores where the 0th element is the score of the first
	//bidder's team and the 1st element is the score of the other team.
	private int[] aScores = new int[2];
	
	/**
	 * A constructor which sets the difficulty.
	 */
	public AdvancedRobot()
	{
		super();
		setDifficulty("Advanced");
	}
	
	/**Overrides the update memory in the Player class.
	 * Updates the memory of the advanced robot using the pull method to obtain the state
	 * of the game, and the push method to get the player's index.
	 * 
	 * @param pGame = the instance of GameEngine (Observable)
	 * @param pToUpdate = An object array where the first element is an identifying string and
	 * 					the second element is the object being pushed to this update method.
	 * @pre pGame must be a GameEngine
	 * @pre pToUpdate must be an object array.
	 * @pre The index of the player has been set.
	 */
	public void update(Observable pGame, Object pToUpdate)
	{
		assert pGame instanceof GameEngine;
		assert pToUpdate instanceof Object[];
		assert ((Object[])pToUpdate)[0] instanceof String;
		
		if(((String)((Object[])pToUpdate)[0]).equals("Player"))
		{
			assert aIndex >= 0;
			GameEngine game = (GameEngine) pGame;
			aMemory.setTrump(game.getTrumpSuit());
			Hand hand = game.getHand(aIndex);
			if(hand.size() == HANDSIZE && game.getTrick().size() == 0)
			{
				//If the hand size is 10, then removes the cards in the AI player's hand
				//from the list of cards that other players could have.
				aMemory = new MemoryList(hand);
				aMemory.setTrump(game.getTrumpSuit());
				aBids = game.getBids();
				//Also removes the widow from the MemoryList if this player is the contract
				//holder.
				if(aIndex == game.getContractHolderIndex())
				{
					aMemory.updateMemory(game.getWidow());
				}
				//Updates the team scores.
				aScores[(0 + game.getDealerIndex()) % 2] = game.getScore(0);
				aScores[(1 + game.getDealerIndex()) % 2] = game.getScore(1);
			}
			//Removes cards in the trick from the list of cards that other players could have.
			if(game.getTrick() != null && game.getTrick().size() > 0)
			{
				aMemory.updateMemory(game.getTrick());
				if(game.getTrick().size()>1)
				{
					//A player is void in a suit if they don't follow the leading suit.
					if(game.getTrick().getLast().getEffectiveSuit(game.getTrumpSuit()) !=
							game.getTrick().getSuitLed())
					{	
						updateVoids(game.getTrick().getLast().getEffectiveSuit(game.getTrumpSuit()), 
								(game.getLeaderIndex() + game.getTrick().size() - 1) % 4);
					}				
				}
				
				if(game.getTrick().size() == 4)
				{
					CardList trick = game.getTrick().clone();
					trick.remove(trick.getFirst());
					//Looking at the 2nd, 3rd and 4th cards in the trick.
					for(int i = 1; i < 4; i++)
					{
						//If the card followed the leading suit.
						if(trick.getFirst().getEffectiveSuit(game.getTrick().getTrumpSuit()) == 
								game.getTrick().getSuitLed())
						{
							//If the card was the highest in its suit.
							if(aMemory.getCards(trick.getFirst().getEffectiveSuit(
									game.getTrick().getTrumpSuit()), trick.getFirst()).size() == 0)
							{
								updateVoids(trick.getFirst().getEffectiveSuit(game.getTrick().getTrumpSuit()),
										(game.getLeaderIndex() + game.getTrick().size() - 1) % 4);
							}
						}
					}
				}
			}	
		}
	}
	
	@Override
	public void setIndex(int pIndex)
	{
		aIndex = pIndex;
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		aBiddingStrategy.setScores(aScores);
		aBidIndex = pPreviousBids.length;
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
		aPlayingStrategy.setBids(aBids);
		aPlayingStrategy.setBidIndex(aBidIndex);
		aPlayingStrategy.setMemory(aMemory);
		return aPlayingStrategy.play(pTrick, pHand);
	}
	
	/**
	 * Helper method that sets the appropriate player/suit combinations to void 
	 * in the MemoryList.
	 * @param pSuit = The suit of the most recent card played.
	 * @param pIndex = The index of the player who is void in the suit.
	 */
	private void updateVoids(Suit pSuit, int pIndex)
	{
		if(pSuit != null)
		{
			//If an opponent is the one who is void in pSuit.
			if(((aIndex + pIndex)%2) == 1)
			{
				if(pIndex < 2)
				{
					aMemory.setFirstOpponentVoid(pSuit);
				}
				else
				{
					aMemory.setSecondOpponentVoid(pSuit);
				}
			}
			else
			{
				//If the partner is the one who is void in pSuit.
				if(aIndex != pIndex)
				{
					aMemory.setPartnerVoid(pSuit);
				}
			}
		}
	}
}
