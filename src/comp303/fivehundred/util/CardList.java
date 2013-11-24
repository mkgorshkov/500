/**
 * @author Maxim Gorshkov
 */
package comp303.fivehundred.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A mutable list of cards. Does not support duplicates. The cards are maintained in the order added.
 */
public class CardList implements Iterable<Card>, Cloneable
{
	private List<Card> aCards;

	/**
	 * Creates a new, empty card list.
	 */
	public CardList()
	{
		// create a new, empty card list
		aCards = new LinkedList<Card>();
	}

	/**
	 * Adds a card if it is not already in the list. Has no effect if the card is already in the list.
	 * 
	 * @param pCard
	 *            The card to add.
	 * @pre pCard != null
	 */
	public void add(Card pCard)
	{
		// make sure the precondition is true
		assert pCard != null;
		aCards.add(pCard);
	}

	/**
	 * @return The number of cards in the list.
	 */
	public int size()
	{
		return aCards.size();
	}

	/**
	 * @return The first card in the list, according to whatever order is currently being used.
	 * @throws GameUtilException
	 *             if the list is empty.
	 */
	public Card getFirst()
	{
		if (aCards.isEmpty())
		{
			throw new GameUtilException("The Card list is empty...");
		}
		else
		{
			return aCards.get(0);
		}
	}

	/**
	 * @return The last card in the list, according to whatever order is currently being used.
	 * 
	 *             If the list is empty.
	 */
	public Card getLast()
	{
		if (aCards.isEmpty())
		{
			throw new GameUtilException("The Card list is empty...");
		}
		return aCards.get(aCards.size() - 1);
	}

	/**
	 * Removes a card from the list. Has no effect if the card is not in the list.
	 * 
	 * @param pCard
	 *            The card to remove.
	 * @pre pCard != null;
	 */
	public void remove(Card pCard)
	{
		// check tosee if the @pre is true
		assert pCard != null;

		// by definition, if the item doesn't exist, nothing will happen...
		aCards.remove(pCard);
	}

	/**
	 * @see java.lang.Object#clone() {@inheritDoc}
	 * @return clone or null/errorcatch if not supported
	 */
	public CardList clone()
	{
		CardList aClone;
		try
		{
			// try to clone the cardlist, if not, we catch
			// with a CloneNotSupportedException and return null
			aClone = (CardList) super.clone();
			aClone.aCards = new LinkedList<Card>(this.aCards);
			return aClone;
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}

	/**
	 * @see java.lang.Iterable#iterator() {@inheritDoc}
	 * @return iterater of the cardlist
	 */
	@Override
	public Iterator<Card> iterator()
	{
		return aCards.iterator();
	}

	/**
	 * @see java.lang.Object#toString() {@inheritDoc}
	 * @return string of cards
	 */
	public String toString()
	{
		return aCards.toString();
	}

	/**
	 * @pre aCards.size() > 0
	 * @return A randomly-chosen card from the set.
	 */
	public Card random()
	{
		// check that the precondition is true
		assert aCards.size() > 0;

		// generate a random number between 1 and the
		// the number of cards in the list.
		int aRandomElement = (int) (Math.random() * (aCards.size()));
		return aCards.get(aRandomElement);
	}
	/**
	 * Returns another CardList, sorted as desired. This method has no side effects.
	 * 
	 * @param pComparator
	 *            Defines the sorting order.
	 * @return the sorted CardList
	 * @pre pComparator != null
	 */
	public CardList sort(Comparator<Card> pComparator)
	{
		// make sure precondition true
		assert pComparator != null;

		// clone it so we can make changes without affecting
		CardList aSortedCards = this.clone();
		
		// sort as necessary
		Collections.sort(aSortedCards.aCards, pComparator);

		// return
		return aSortedCards;
	}
	
	/**
	 * Determines if a card (pCard) is in the CardList.
	 * 
	 * @param pCard = The card being searched for in the CardList.
	 * @pre pCard != null
	 * @pre aCards.size() > 0
	 * @return true if pCard is in the CardList, false otherwise.
	 */
	public boolean contains(Card pCard)
	{
		assert pCard != null;
		assert aCards.size() > 0;
		for(Card card : aCards)
		{
			if(card.equals(pCard))
			{
				return true;
			}
		}
		return false;
	}
}
