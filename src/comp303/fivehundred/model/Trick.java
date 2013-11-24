/**@author James McCorriston
 */
package comp303.fivehundred.model;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;


/**
 * A card list specialized for handling cards discarded
 * as part of the play of a trick.
 */
public class Trick extends CardList
{	
	//The contract for the trick.
	private Bid aContract;
	
	/**
	 * Constructs a new empty trick for the specified contract.
	 * @param pContract The contract that this trick is played for.
	 */
	public Trick(Bid pContract)
	{
		aContract = pContract;
	}
	
	/**
	 * @return Can be null for no-trump.
	 */
	public Suit getTrumpSuit()
	{
		return aContract.getSuit();
	}
	
	
	/**
	 * @return The effective suit led.
	 */
	public Suit getSuitLed()
	{
		if(jokerLed())
		{
			//If the contract is no trump, then a leading joker has no suit.
			//Otherwise, a leading joker is of the trump suit.
			if(getTrumpSuit() == null)
			{
				return null;
			}
			else
			{
				return getTrumpSuit();
			}
		}
		else if(size() > 0)
		{
			//Returns the effective suit of the card led.
			return getFirst().getEffectiveSuit(getTrumpSuit());
		}
		else
		{
			//Returns null if the trick has no cards yet.
			return null;
		}
	}
	
	/**
	 * @return True if a joker led this trick
	 */
	public boolean jokerLed()
	{
		if(size() > 0)
		{
			return getFirst().isJoker();
		}
		else
		{
			//returns false if the trick is empty.
			return false;
		}
	}
	
	/**
	 * @return The card that led this trick
	 * @pre size() > 0
	 */
	public Card cardLed()
	{
		assert size() > 0;
		return getFirst();
	}

	/**
	 * @return Highest card that actually follows suit (or trumps it).
	 * I.e., the card currently winning the trick.
	 * @pre size() > 0
	 */
	public Card highest()
	{
		assert size() > 0;
		Card highestCard = cardLed();
		Card.BySuitComparator suitComparator = new Card.BySuitComparator();
		suitComparator.setTrump(getTrumpSuit());
		Card.ByRankComparator rankComparator = new Card.ByRankComparator();
		
		//Iterate over the cards in the trick.
		for(Card card : this)
		{
			//If the contract is no trump.
			if(getTrumpSuit() == null)
			{
				//If the card being analyzed is of the leading suit and a higher rank
				//than the previous highest card.
				if((card.getEffectiveSuit(getTrumpSuit()) == getSuitLed() && rankComparator.compare(card, highestCard) > 0) || 
						card.isJoker() && rankComparator.compare(card, highestCard) > 0)
				{
					highestCard = card;
				}
			}
			//If the card being analyzed is trump.
			else if(card.getEffectiveSuit(getTrumpSuit()) == getTrumpSuit())
			{
				//If the highest card is also trump.		
				if(highestCard.getEffectiveSuit(getTrumpSuit()) == getTrumpSuit())
				{
					//If the card being analyzed is of higher rank.
					if(suitComparator.compare(card, highestCard) > 0)
					{
						highestCard = card;
					}
				}
				//Trump beats non-trump.
				else
				{
					highestCard = card;
				}
				
			}
			//If the card being analyzed is of the suit led.
			else if(card.getEffectiveSuit(getTrumpSuit()) == getSuitLed())
			{
				//And if it is of higher rank.
				if(suitComparator.compare(card, highestCard) > 0)
				{
					highestCard = card;
				}
			}
		}
		return highestCard;
	}
	
	/**
	 * @return The index of the card that wins the trick.
	 */
	public int winnerIndex()
	{
		int winningCardHash = -1;
		int index = -1;
		if(size() >= 1)
		{
			//the hashCode of the highest card in the trick
			winningCardHash = highest().hashCode();
			//iterates through the list of cards looking for the card whose
			//hashCode matches winningCardHash
			for(Card card : this)
			{
				index++;
				if(card.hashCode() == winningCardHash)
				{
					break;
				}
			}
			return index;
		}
		else
		{
			//throws a model exception if the trick is empty
			throw new ModelException("Tried to get the winner index of an empty trick.");
		}
	}
}
