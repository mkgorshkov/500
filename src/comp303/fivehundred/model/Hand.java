/**
 * @author James McCorriston
 */

package comp303.fivehundred.model;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Additional services to manage a card list that corresponds to
 * the cards in a player's hand.
 */
public class Hand extends CardList
{
	/**
	 * @see java.lang.Object#clone()
	 * {@inheritDoc}
	 */
	@Override
	public Hand clone()
	{
		//Since hand is a CardList, calls super.clone and casts it to a hand.
		return (Hand)super.clone();
	}
	
	/**
	 * @param pNoTrump If the contract is in no-trump
	 * @return A list of cards that can be used to lead a trick.
	 */
	public CardList canLead(boolean pNoTrump)
	{
		//If the contract is no trump.
		if(pNoTrump)
		{
			//If the hand has no jokers or is only jokers.
			if(getNonJokers().size() == 0 || getJokers().size() == 0)
			{
				return clone();
			}
			else
			{
				//Can't lead with a joker when the contract is no trump.
				return getNonJokers();
			}
		}
		else
		{
			//When there is a trump, anything can be led.
			return clone();
		}
	}
	
	/**
	 * @return The cards that are jokers.
	 */
	public CardList getJokers()
	{
		CardList lCardList = new CardList();
		//Iterates through the cards in the Hand.
		for(Card card : this)
		{
			if(card.isJoker())
			{
				lCardList.add(card);
			}
		}
		return lCardList;
	}
	
	/**
	 * @return The cards that are not jokers.
	 */
	public CardList getNonJokers()
	{
		CardList lCardList = new CardList();
		//Iterates through the cards in the Hand.
		for(Card card : this)
		{
			if(!card.isJoker())
			{
				lCardList.add(card);
			}
		}
		return lCardList;
	}
	
	/**
	 * Returns all the trump cards in the hand, including jokers.
	 * Takes jack swaps into account.
	 * @param pTrump The trump to check for. Cannot be null.
	 * @return All the trump cards and jokers.
	 * @pre pTrump != null
	 */
	public CardList getTrumpCards(Suit pTrump)
	{
		assert pTrump != null;
		CardList lCardList = new CardList();
		//Iterates through the cards in the Hand.
		for(Card card : this)
		{
			//Jokers are considered trump.
			if(card.isJoker())
			{
				lCardList.add(card);
			}
			else if(card.getEffectiveSuit(pTrump) == pTrump)
			{
				lCardList.add(card);
			}
		}
		return lCardList;
	}
	
	/**
	 * Returns all the cards in the hand that are not trumps or jokers.
	 * Takes jack swaps into account.
	 * @param pTrump The trump to check for. Cannot be null.
	 * @return All the cards in the hand that are not trump cards.
	 * @pre pTrump != null
	 */
	public CardList getNonTrumpCards(Suit pTrump)
	{
		assert pTrump != null;
		CardList lCardList = new CardList();
		//Iterates through the cards in the Hand.
		for(Card card : this)
		{
			if(!card.isJoker() && card.getEffectiveSuit(pTrump) != pTrump)
			{
				lCardList.add(card);
			}
		}
		return lCardList;
	}
	
	
	/**
	 * Selects the least valuable card in the hand, if pTrump is the trump.
	 * @param pTrump The trump suit. Can be null to indicate no-trump.
	 * @return The least valuable card in the hand.
	 */
	public Card selectLowest(Suit pTrump)
	{
		if(size()<1)
		{
			//Throws a ModelException if the hand is empty when this method is called.
			throw new ModelException("Attempted to selectLowest from an empty hand.");
		}
		//Initializes lowestCard as the first card in the hand.
		Card lowestCard = getFirst();
		
		Card.ByRankComparator rankComparator = new Card.ByRankComparator();
		Card.BySuitComparator suitComparator = new Card.BySuitComparator();
		//Sets the trump suit for the BySuitComparator.
		suitComparator.setTrump(pTrump);
		
		//Iterate over the cards in the trick.
		for(Card card : this)
		{
			//If the contract is no trump.
			if(pTrump == null)
			{
				//If the card being analyzed is lower than lowestCard.
				if(rankComparator.compare(card, lowestCard) < 0)
				{
					lowestCard = card;
				}
			}
			//If the card being analyzed is trump.
			else if(card.getEffectiveSuit(pTrump) == pTrump)
			{
				//If the lowest card is also trump.		
				if(lowestCard.getEffectiveSuit(pTrump) == pTrump)
				{
					//If the card being analyzed is of lower rank.
					if(suitComparator.compare(card, lowestCard) < 0)
					{
						lowestCard = card;
					}
				}
			}
			//If the card being analyzed is not of the trump suit.
			else
			{
				//If the lowest card is trump.
				if(lowestCard.getEffectiveSuit(pTrump) == pTrump)
				{
					lowestCard = card;
				}
				//Both are non-trump, compare by rank.
				else if(rankComparator.compare(card, lowestCard) < 0)
				{
					lowestCard = card;
				}
			}
		}
		return lowestCard;
	}
	
	/**
	 * @param pLed The suit led.
	 * @param pTrump Can be null for no-trump
	 * @return All cards that can legally be played given a lead and a trump.
	 */
	public CardList playableCards( Suit pLed, Suit pTrump )
	{
		//If nothing has been led.
		if(pLed == null)
		{
			return canLead(pTrump == null);
		}
		//If trump was led.
		else if(pLed == pTrump)
		{
			//If the hand still has trump cards.
			if(getTrumpCards(pTrump).size() > 0)
			{
				return getTrumpCards(pTrump);
			}
			//If the hand has no trump cards.
			else
			{
				return getNonTrumpCards(pTrump);
			}
		}
		//If non-trump was led.
		else
		{
			CardList lCardList = new CardList();
			//Iterates over the list of cards in the hand.
			for(Card card : this)
			{
				//Adds cards of the suit led (not jokers since they are trump) 
				//to the list of playable cards.
				if(card.getEffectiveSuit(pTrump) == pLed)
				{
					lCardList.add(new Card(card.getRank(), card.getSuit()));
				}
			}
			/*if(lCardList.size() > 0)
			{
				//Jokers can be played at any time when the contract is no trump
				//as long as they are not the leading card.
				for(Card card : getJokers())
				{
					//Adds the jokers to the list of playable cards if the contract
					//is no trump.
					if(card.isJoker() && pTrump == null)
					{
						lCardList.add(new Card(card.getJokerValue()));
					}
				}
				return lCardList;
			}*/
			if(lCardList.size() == 0)
			{
				//Otherwise anything in the hand can be played.
				lCardList = clone();
			}
			return lCardList;
		}
	}
	
	/**
	 * Returns the number of cards of a certain suit 
	 * in the hand, taking jack swaps into account.
	 * Excludes jokers.
	 * @param pSuit Cannot be null.
	 * @param pTrump Cannot be null.
	 * @return pSuit Can be null.
	 */
	public int numberOfCards(Suit pSuit, Suit pTrump)
	{
		int numCards = 0;
		//Iterates over the list of cards in the hand.
		for(Card card : this)
		{
			//Counts the cards in the suit excluding jokers.
			if(!card.isJoker() && card.getEffectiveSuit(pTrump) == pSuit)
			{
				numCards += 1;
			}
		}
		return numCards;
	}
}
