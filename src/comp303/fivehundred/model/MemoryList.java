/**
 * @author Andrew Borodovski, James McCorriston
 */

package comp303.fivehundred.model;

import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Deck;

/**
 * This class provides methods useful for managing, storing and using memory.
 *
 */
public class MemoryList extends Hand
{
	//private CardList aCardsLeft = new CardList();
	private Suit aTrump;
	private boolean[] aPartnerIsVoid = new boolean[4];
	private boolean[] aFirstOpponentIsVoid = new boolean[4];
	private boolean[] aSecondOpponentIsVoid = new boolean[4];
	
	/**
	 * Creates a MemoryList.
	 * Places all cards in the field that stores cards left in play. 
	 */
	public MemoryList()
	{
		resetCards(new CardList());
	}
	
	/**
	 * Creates a MemoryList.
	 * Places all cards except those specified in the field which stores cards left in play.
	 * 
	 * @param pCards The Cards that should not be added to the list of cards still in play.
	 */
	public MemoryList(CardList pCards)
	{
		resetCards(pCards);
	}
	
	/**
	 * Returns all of the cards left in play of the given suit, which 
	 * are also higher than the given card. If the given card is null
	 * all cards of the given suit still in play are returned.
	 * 
	 * @param pSuit The suit of which cards still in play are desired
	 * @param pCard The card to compare all of the cards of the given suit to. If pCard
	 * 				is null, all cards of pSuit are returned.
	 * @return The cards of the specified suit still left in play
	 * @pre If the given card is not null, its suit should be equal to the given suit.
	 */
	public CardList getCards(Suit pSuit, Card pCard)
	{
		if (pCard != null)
		{
			assert pSuit == pCard.getEffectiveSuit(aTrump);
		}
		//A comparator for finding the cards higher than the given card
		Card.ByRankComparator comparator = new Card.ByRankComparator();
		//Comparators for sorting the cards
		Card.BySuitComparator trumpComparator = new Card.BySuitComparator();
		Card.BySuitNoTrumpComparator noTrumpComparator = new Card.BySuitNoTrumpComparator();
		trumpComparator.setTrump(aTrump);
		//The cards to be returned
		CardList lCards = new CardList();
		//The cards still in play
		CardList cardsLeft = clone();
		//Sorting the cards still in play
		if (aTrump == null)
		{
			cardsLeft = cardsLeft.sort(noTrumpComparator);
		}
		else
		{
			cardsLeft = cardsLeft.sort(trumpComparator);
		}
		//The number of cards left
		int numCardsLeft = cardsLeft.size();
		for (int i = 0; i < numCardsLeft; i++)
		{
			//If the suit of the card still in play matches the given suit, add it to the cards to be returned
			if (cardsLeft.getFirst().getEffectiveSuit(aTrump) == pSuit)
			{
				lCards.add(cardsLeft.getFirst());
			}
			else
			{
				//If you already have cards to be returned and you reach a card of a different suit, then there can be no more cards
				//of the given suit (since the list is sorted by suit) and therefore, no more comparisons are needed.
				if (lCards.size() > 0)
				{
					break;
				}
			}
			cardsLeft.remove(cardsLeft.getFirst());
			
		}
		//If the given card is not null, then all cards lower than it are removed from the cards to be returned
		if (pCard != null)
		{
			int numCards = lCards.size();
			for (int i = 0; i < numCards; i ++)
			{
				if (comparator.compare(pCard, lCards.getFirst()) > 0)
				{
					lCards.remove(lCards.getFirst());
				}
				else
				{
					break;
				}
			}
		}
		return lCards;
	}
	
	/**
	 * Returns true if it is known that the partner does not have any cards of the 
	 * given suit, false otherwise.
	 * 
	 * @param pSuit The suit being queried.
	 * @return Whether the partner is void for the given suit or not.
	 */
	public boolean partnerIsVoid(Suit pSuit)
	{
		return aPartnerIsVoid[pSuit.ordinal()];
	}
	
	/**
	 * Returns true if the first opponent is void in the given suit.
	 * 
	 * @param pSuit The suit being queried.
	 * @return True if the first opponent is void in the suit, false otherwise.
	 */
	public boolean firstOpponentIsVoid(Suit pSuit)
	{
		return aFirstOpponentIsVoid[pSuit.ordinal()];
	}
	
	/**
	 * Returns true if the second opponent is void in the given suit.
	 * 
	 * @param pSuit The suit being queried.
	 * @return True if the second opponent is void in the suit, false otherwise.
	 */
	public boolean secondOpponentIsVoid(Suit pSuit)
	{
		return aSecondOpponentIsVoid[pSuit.ordinal()];
	}
	
	/**
	 * Returns true if either opponent is void in the given suit.
	 * 
	 * @param pSuit The suit being queried.
	 * @return True if either opponent is void in the suit, false otherwise.
	 */
	public boolean eitherOpponentIsVoid(Suit pSuit)
	{
		return aFirstOpponentIsVoid[pSuit.ordinal()] || aSecondOpponentIsVoid[pSuit.ordinal()];
	}
	
	/**
	 * Sets that the partner is void for the given suit.
	 * 
	 * @param pSuit The suit that the partner is void in.
	 */
	public void setPartnerVoid(Suit pSuit)
	{
		aPartnerIsVoid[pSuit.ordinal()] = true;
	}
	
	/**
	 * Sets that the first opponent is void for the given suit.
	 * 
	 * @param pSuit The suit that the first opponent is void in.
	 */
	public void setFirstOpponentVoid(Suit pSuit)
	{
		aFirstOpponentIsVoid[pSuit.ordinal()] = true;
	}
	
	/**
	 * Sets that the second opponent is void for the given suit.
	 * 
	 * @param pSuit The suit that the second opponent is void in.
	 */
	public void setSecondOpponentVoid(Suit pSuit)
	{
		aSecondOpponentIsVoid[pSuit.ordinal()] = true;
	}
	
	/**
	 * Takes a list of cards and removes them from the list of cards left.
	 * 
	 * @param pCards The list of cards to be removed.
	 */
	public void updateMemory(CardList pCards)
	{
		for(Card card: pCards)
		{
			if (size() > 0 && contains(card))
			{
				remove(card);
			}
			if(aTrump != null && getCards(card.getEffectiveSuit(aTrump), card).size() == 0)
			{
				aPartnerIsVoid[card.getEffectiveSuit(aTrump).ordinal()] = true;
				aFirstOpponentIsVoid[card.getEffectiveSuit(aTrump).ordinal()] = true;
				aSecondOpponentIsVoid[card.getEffectiveSuit(aTrump).ordinal()] = true;
			}
		}
	}
	
	/**
	 * Sets the trump suit.
	 * 
	 * @param pSuit The trump suit
	 */
	public void setTrump(Suit pSuit)
	{
		aTrump = pSuit;
	}
	
	/**
	 * Resets the cards left. Adds all cards to the cards left. Sorts the cards left by suit.
	 * 
	 */
	private void resetCards(CardList pCards)
	{
			//The assumption is that no on is void in a suit at the start of a round.
			for(int i = 0; i < 4; i++)
			{
				aPartnerIsVoid[i] = false;
				aFirstOpponentIsVoid[i] = false;
				aSecondOpponentIsVoid[i] = false;
			}
			
			//Creating a deck in order to fill up the card list
			Deck deck = new Deck();
			deck.shuffle();
			//Resetting the cards left
			for (Card card: this)
			{
				remove(card);
			}
			//Adding all cards to the cards left
			int deckSize = deck.size();
			for (int i = 0; i < deckSize; i++ )
			{
				Card card = deck.draw();
				if (pCards.size() == 0 || !pCards.contains(card))
				{
					add(card);
				}
			}			
		}
	
}
