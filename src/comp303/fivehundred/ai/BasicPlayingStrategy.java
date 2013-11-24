/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.ai;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.CardList;

/**
 * If leading pick a random card (except for the joker, if no trump). If not leading pick the lowest legal card that wins the trick.
 * If you cannot win the trick, play the lowest legal card.
 */
public class BasicPlayingStrategy implements IPlayingStrategy
{

	/**
	 * @param pTrick
	 *            current trick
	 * @param pHand
	 *            current player hand
	 * @pre pTrick != null
	 * @pre pHand != null
	 * @return card that should be played
	 */
	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		assert pTrick != null;
		assert pHand != null;
		
		//The card to be returned. By default, just the first card in the hand.
		Card lCard = pHand.getFirst();
		//The cards that are legal to play.
		CardList playable = pHand.playableCards(pTrick.getSuitLed(), pTrick.getTrumpSuit());
		//A by suit comparator, with the trump either set to the trump suit, or if none, the leading suit.
		Card.BySuitComparator suitComparator = new Card.BySuitComparator();
		//A by rank comparator
		Card.ByRankComparator rankComparator = new Card.ByRankComparator();
		//Setting the trump suit for the comparator.
		if (pTrick.getTrumpSuit() == null)
		{
			suitComparator.setTrump(pTrick.getSuitLed());
		}
		else
		{
			suitComparator.setTrump(pTrick.getTrumpSuit());
		}
		
		// If the trick is empty, we lead with a random, legal card.
		if (pTrick.size() == 0)
		{
			lCard = pHand.canLead(pTrick.getTrumpSuit()==null).random();
		}
		// If someone has already led.
		else
		{
			//Change lCard default value to the lowest of legal cards.
			lCard = playable.sort(rankComparator).getFirst();
			
			//If a Joker is highest in the trick
			if (pTrick.highest().isJoker())
			{
				//If there is a high Joker in the cards that can be played, it should be played
				for (Card c : pHand)
				{
					if (c.isJoker() && c.getJokerValue() == Joker.HIGH)
					{
						lCard = c;
						break;
					}
				}
			}
			//The situation where the highest card in the trick is not a joker
			else
			{
				//If the leading suit was not the trump suit, but the highest card in the trick is a trump
				if (pTrick.highest().getEffectiveSuit(pTrick.getTrumpSuit()) != pTrick.getSuitLed())
				{
					//If there are any cards of the leading suit in the hand, dump the lowest
					if (playable.getFirst().getEffectiveSuit(pTrick.getTrumpSuit()) == pTrick.getSuitLed())
					{
						playable = playable.sort(suitComparator);
						lCard = playable.getFirst();
					}
					//There are no cards of the leading suit in the hand
					else
					{
						//Sort the cards by suit, using the trump suit
						playable = playable.sort(suitComparator);
						
						int numPlayable = playable.size();
						//Look through all playable cards.
						for (int i = 0; i < numPlayable; i++)
						{
							//First card which can beat the highest card of the trick (must be trump) gets played
							if (suitComparator.compare(playable.getFirst(), pTrick.highest()) > 0)
							{
								lCard = playable.getFirst();
								break;
							}
							else
							{
								playable.remove(playable.getFirst());
							}
						}
					}
				}
				//The situation where the highest card in the trick is of the leading suit
				else
				{
					//When there are cards of the leading suit in the hand, find the lowest that can beat
					//the current highest card in the trick, and play it.
					if (playable.getFirst().getEffectiveSuit(pTrick.getTrumpSuit()) == pTrick.getSuitLed())
					{
						//Sort the playable cards, using the leading suit as the trump.
						playable = playable.sort(suitComparator);
						
						//Cycle through all playable cards.
						for (Card c : playable)
						{
							//Take the first that can beat the current highest card in the trick.
							if (suitComparator.compare(c, pTrick.highest()) > 0)
							{
								lCard = c;
								break;
							}
						}
						//If no cards that can beat the highest card were found, the default lowest card will be played.
					}
					//When there are no cards of the leading suit in the hand - looking for the lowest trump card.
					else
					{
						//Sort cards by suit using the trump suit.
						playable = playable.sort(suitComparator);
						int numPlayable = playable.size();
						for (int i = 0; i < numPlayable; i++)
						{
							//Find the first card that is either a joker or is of the trump suit.
							if(playable.getFirst().isJoker() || playable.getFirst().getSuit() == pTrick.getTrumpSuit())
							{
								lCard = playable.getFirst();
								break;
							}
							else
							{
								playable.remove(playable.getFirst());
							}
						}
					}
				}
			}	
		}
		//Return the card that should be played.
		return lCard;
	}
}
