/**
 * @author Dashiel Siegel (comparator objects)
 * @author Maxim Gorshkov (everything else)
 */

package comp303.fivehundred.util;

import java.util.Comparator;
/**
 * An immutable description of a playing card.
 */
public final class Card implements Comparable<Card>
{

	public static final int HIJO_HASH = 45;
	public static final int LOJO_HASHCODE = 44;
	public static final int NUMBER_SUITS = 11;

	/**
	 * Represents the rank of the card.
	 */
	public enum Rank
	{
		FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
	}

	/**
	 * Represents the suit of the card.
	 */
	public enum Suit
	{
		SPADES, CLUBS, DIAMONDS, HEARTS;

		/**
		 * @return the other suit of the same color.
		 */
		public Suit getConverse()
		{
			Suit lReturn = this;
			switch (this)
			{
			case SPADES:
				lReturn = CLUBS;
				break;
			case CLUBS:
				lReturn = SPADES;
				break;
			case DIAMONDS:
				lReturn = HEARTS;
				break;
			case HEARTS:
				lReturn = DIAMONDS;
				break;
			default:
				lReturn = this;
			}
			return lReturn;
		}
	}

	/**
	 * Represents the value of the card, if the card is a joker.
	 */
	public enum Joker
	{
		LOW, HIGH
	}

	// If this field is null, it means the card is a joker, and vice-versa.
	private final Rank aRank;

	// If this field is null, it means the card is a joker, and vice-versa.
	private final Suit aSuit;

	// If this field is null, it means the card is not a joker, and vice-versa.
	private final Joker aJoker;

	/**
	 * Create a new card object that is not a joker.
	 * 
	 * @param pRank
	 *            The rank of the card.
	 * @param pSuit
	 *            The suit of the card.
	 * @pre pRank != null
	 * @pre pSuit != null
	 */
	public Card(Rank pRank, Suit pSuit)
	{
		assert pRank != null;
		assert pSuit != null;
		aRank = pRank;
		aSuit = pSuit;
		aJoker = null;
	}

	/**
	 * Creates a new joker card.
	 * 
	 * @param pValue
	 *            Whether this is the low or high joker.
	 * @pre pValue != null
	 */
	public Card(Joker pValue)
	{
		assert pValue != null;
		aRank = null;
		aSuit = null;
		aJoker = pValue;
	}

	/**
	 * @return True if this Card is a joker, false otherwise.
	 */
	public boolean isJoker()
	{
		return aJoker != null;
	}

	/**
	 * @return Whether this is the High or Low joker.
	 */
	public Joker getJokerValue()
	{
		assert isJoker();
		return aJoker;
	}

	/**
	 * Obtain the rank of the card.
	 * 
	 * @return An object representing the rank of the card. Can be null if the card is a joker.
	 * @pre !isJoker();
	 */
	public Rank getRank()
	{
		assert !isJoker();
		return aRank;
	}

	/**
	 * Obtain the suit of the card.
	 * 
	 * @return An object representing the suit of the card
	 * @pre !isJoker();
	 */
	public Suit getSuit()
	{
		assert !isJoker();
		return aSuit;
	}

	/**
	 * Returns the actual suit of the card if pTrump is the trump suit. Takes care of the suit swapping of jacks.
	 * 
	 * @param pTrump
	 *            The current trump. Null if no trump.
	 * @return The suit of the card, except if the card is a Jack and its converse suit is trump. Then, returns the
	 *         trump. Returns null for jokers when there is no trump.
	 */
	public Suit getEffectiveSuit(Suit pTrump)
	{
		if (pTrump == null)
		{
			if(isJoker())
			{
				return null;
			}
			else
			{
				return aSuit;
			}
		}
		else if (aRank == Rank.JACK && aSuit == pTrump.getConverse())
		{
			return pTrump;
		}
		else if(isJoker())
		{
			return pTrump;
		}
		else
		{
			return aSuit;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return See above.
	 */
	public String toString()
	{
		if (!isJoker())
		{
			//Testing for null cards
			if (aRank == null || aSuit == null)
			{
				throw new GameUtilException("Tried to get the String for a null card");
			}
			else
			{
				return aRank + " of " + aSuit;
			}
		}
		else
		{
			return aJoker + " " + "Joker";
		}
	}

	/**
	 * @return A short textual representation of the card
	 */
	public String toShortString()
	{
		String lReturn = "";
		if (isJoker())
		{
			lReturn = aJoker.toString().charAt(0) + "J";
		}
		else
		{
			//Testing for null cards
			if (aRank == null || aSuit == null)
			{
				throw new GameUtilException("Tried to get the string of a null card");
			}
			else
			{
				if (aRank.ordinal() <= Rank.NINE.ordinal())
				{
					lReturn += new Integer(aRank.ordinal() + 4).toString();
				}
				else
				{
					lReturn += aRank.toString().charAt(0);
				}
				lReturn += aSuit.toString().charAt(0);
			}			
		}
		return lReturn;
	}

	/**
	 * Compares two cards according to their rank.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param pCard
	 *            The card to compare to
	 * @return Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater
	 *         than pCard
	 */
	public int compareTo(Card pCard)
	{
		// Cast to be a card object
		Card crt = pCard;
		int aValue = 0;
		
		//Testing for null cards
		if (!isJoker() && (aRank == null || aSuit == null))
		{
			throw new GameUtilException("Trying to compare null card");
		}
		if (!crt.isJoker() && (crt.getRank() == null || crt.getSuit() == null))
		{
			throw new GameUtilException("Trying to compare null card");
		}
		
		// test for jokers: 2 Jokers of equal Rank; 2 Jokers with dif. Ranks; only one is a joker
		if (isJoker() || pCard.isJoker() )
		{
			if (isJoker() && pCard.isJoker())
			{
				if (getJokerValue()==pCard.getJokerValue())
				{
					aValue = 0;
				}
				else if (getJokerValue()==Joker.HIGH)
				{
					aValue = 1;
				}
				else if (pCard.getJokerValue()==Joker.HIGH)
				{
					aValue = -1;
				}
			}
			else if (isJoker()) 
			{
				aValue = 1;
			}
			else if (pCard.isJoker())
			{
				aValue = -1;
			}
			return aValue;
		}
		
		// if we reached here, no jokers. now do a simple rank check.
		return aRank.compareTo(crt.aRank);

	}

	/**
	 * Two cards are equal if they have the same suit and rank or if they are two jokers of the same value.
	 * 
	 * @param pCard
	 *            The card to test.
	 * @return true if the two cards are equal
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pCard)
	{
		Card crt;
		
		// Cast to be a card object
		try
		{
			crt = (Card) pCard;
		}
		catch (ClassCastException e)
		{
			return false;
		}
		

		// first check to see if it's a joker or not
		// boolean can be compared without .equals()
		// need to add second condition since both not being is also the same
		if ((crt.isJoker() == isJoker()) && crt.isJoker())
		{
			// if it is a joker, deal with value
			if (crt.getJokerValue().equals(getJokerValue()))
			{
				return true;
			}
			return false;
		}
		else if (crt.isJoker() || isJoker())
		{
			return false;
		}
		else
		{
			//Testing for null cards
			if (aRank == null || aSuit == null || crt.getRank() == null || crt.getSuit() == null)
			{
				return false;
			}
			// if case where not a joker, compare both rank and suit...
			if (crt.aRank.equals(aRank) && crt.aSuit.equals(aSuit))
			{
				return true;
			}
		}
		return false;
		
	}

	/**
	 * The hashcode for a card is the suit*number of ranks + that of the rank (perfect hash).
	 * 
	 * @return the hashcode
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{

		// every card should have a unique identifier...

		// check to see if we have jokers or not
		if (this.isJoker())
		{
			if (this.getJokerValue() == Joker.HIGH)
			{
				// Ordinality is 2 higher that highest non-joker card.
				return HIJO_HASH;
			}
			else
			{
				// Ordinality is 1 higher than the highest non-joker card.
				return LOJO_HASHCODE;
			}
		}
		//Testing for null cards
		if(aRank == null || aSuit == null)
		{
			throw new GameUtilException("Trying to get hash code of null card");
		}
		
		// if not a joker, follow regular hashcode suit*number suits + rank
		return aSuit.ordinal() * NUMBER_SUITS + aRank.ordinal();
	}

	/**
	 * Compares cards using their rank as primary key, then suit. Jacks rank between 10 and queens of their suit.
	 */
	public static class ByRankComparator implements Comparator<Card>
	{
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			//check if a comparison of rank leads to them being the same
			if (pCard1.compareTo(pCard2) == 0)
			{
				//if so, check if the cards are equal (covers jokers)
				if (pCard1.equals(pCard2))
				{
					return 0;
				}
				//if the ranks are the same, test by suit
				else 
				{
					return pCard1.aSuit.compareTo(pCard2.aSuit);
				}
				
			}
			else
			{
				//if ranks are different compare enums
				return pCard1.compareTo(pCard2);
			}
		}
	}

	/**
	 * Compares cards using their suit as primary key, then rank. Jacks
	 * rank between 10 and queens of their suit.
	 */
	public static class BySuitNoTrumpComparator implements Comparator<Card>
	{
		//aValue stores the final output of compare(), and is returned at the end of it
		private int aValue = 2;
		
		@Override
		public int compare(Card pCard1, Card pCard2)
		{	
			//check for jokers
			if ( pCard1.isJoker() || pCard2.isJoker() )
			{
				//if both jokers
				if (pCard1.isJoker() && pCard2.isJoker())
				{
					//both hi or both low
					if (pCard1.getJokerValue()==pCard2.getJokerValue())
					{
						aValue = 0;
					}
					//first high
					else if (pCard1.getJokerValue()==Joker.HIGH)
					{
						aValue = 1;
					}
					//second high
					else if (pCard2.getJokerValue()==Joker.HIGH)
					{
						aValue = -1;
					}
				}
				//first is joker, second not
				else if (pCard1.isJoker()) 
				{
					aValue = 1;
				}
				//second is joker, first not
				else if (pCard2.isJoker())
				{
					aValue = -1;
				}
			}
			
			//if suits are equal:
			else if (pCard1.aSuit.compareTo(pCard2.aSuit) == 0)
			{
				//compare and return by rank
				return pCard1.aRank.compareTo(pCard2.aRank);
			}
			//if suits are different
			else
			{
				//compare and return by suit
				return pCard1.aSuit.compareTo(pCard2.aSuit);
			} 

			return aValue;
		}
	}
	
	/**  
	 * Compares cards using their suit as primary key, then rank. Jacks
	 * rank above aces if they are in the trump suit. The trump suit becomes the
	 * highest suit.
	 */
	public static class BySuitComparator implements Comparator<Card>
	{
		//aTrump stores the current Trump suit. It is set when BySuitComparator is constructed
		//aValue stores the final output of compare(), and is returned at the end of it
		private int aValue ;
		private Suit aTrump = null;
		
		
		/**
		 * Sets the private Suit aTrump.
		 * @param pTrump
		 * 			The Suit we want to change aTrump to.
		 */
		public void setTrump(Suit pTrump)
		{
			aTrump = pTrump;
		}
		
		@Override
		/**
		 * Compares pCard1 and pCard2.
		 * If aTrump null: works exactly as BySuitNoTrumpComparator
		 * Otherwise: checks for Jokers (HI/LO), Jacks (left/right bower), Trumps, and finally Rank
		 * @param pCard1
		 * 			the card to test
		 * @param pCard2
		 * 			the card to compare pCard1 against
		 * @return Returns a negative integer, zero, or a positive integer if pCard1 is
		 *         less than, equal to or greater than pCard2 respectively. 
		 */
		public int compare(Card pCard1, Card pCard2)
		{					
			//if no trump suit specified: compare exactly as in BySuitNoTrumpComparator.compare
			if (aTrump == null)
			{
				throw new GameUtilException("Use the bySuitNoTrumpComparator!");
			}
			
			//check for jokers
			else if ( pCard1.isJoker() || pCard2.isJoker() )
			{
				//if both are jokers
				if (pCard1.isJoker() && pCard2.isJoker())
				{
					//and share the same rank, return 0
					if (pCard1.getJokerValue()==pCard2.getJokerValue())
					{
						aValue = 0;
					}
					//first high, return 1
					else if (pCard1.getJokerValue()==Joker.HIGH)
					{
						aValue = 1;
					}
					//second high, return 0
					else if (pCard2.getJokerValue()==Joker.HIGH)
					{
						aValue = -1;
					}
				}
				//only first is a joker, return 1
				else if (pCard1.isJoker()) 
				{
					aValue = 1;
				}
				//only second is a joker
				else if (pCard2.isJoker())
				{
					aValue = -1;
				}
			}

			//check for jacks
			//this if statement returns true if either card is in the left or right bower
			else if ( (pCard1.getRank()==Rank.JACK) && (pCard1.getEffectiveSuit(aTrump)==aTrump) ||
					(pCard2.getRank()==Rank.JACK) && (pCard2.getEffectiveSuit(aTrump)==aTrump) )
			{
				//if both jacks
				if (pCard1.getRank() == pCard2.getRank())
				{
					//and same suit, return 0
					if(pCard1.getSuit() == pCard2.getSuit())
					{
						aValue = 0;
					}
					//only card1 is left bower, return 1
					else if (pCard1.getSuit() == aTrump) 
					{
						aValue = 1;
					}
					//only card2 is left bower, return -1
					else if (pCard2.getSuit() == aTrump) 
					{
						aValue = -1;
					}
					//only card 1 is right bower, return 1
					else if (pCard1.getEffectiveSuit(aTrump) == aTrump && pCard2.getEffectiveSuit(aTrump) != aTrump)
					{
						aValue = 1;
					}
					//only card2 is right bower, return -1
					else
					{
						aValue = -1;
					}
				}
				//only card1 is a jack, return 1
				else if (pCard1.getRank() == Rank.JACK )
						{
							aValue = 1;
						}
				//only card2 is a jack, return -1
				else if (pCard2.getRank() == Rank.JACK)
						{
							aValue = -1;
						}
			}

			//check for trumps
			//if either card is a trump
			else if ( (pCard1.getSuit() == aTrump) || (pCard2.getSuit() == aTrump) )
			{
				//card1 is a trump
				if (pCard1.getSuit() == aTrump)
				{
					//and card2 is not, return 1
					if (pCard2.getSuit() != aTrump)
					{
						aValue = 1;
					}
					//card2 is also trump, compare by rank and return
					else 
					{
						aValue = pCard1.compareTo(pCard2);
					}
				}
				//only card2 is a trump, return -1
				else if (pCard2.getSuit() == aTrump) 
				{
					aValue = -1;
				}
			}
			
			//we know there are no jokers, bowers or trumps: now compare normally by suit then rank
			//if the suits are the same
			else if (pCard1.aSuit.compareTo(pCard2.aSuit) == 0) 
			{
				//compare by rank
				aValue = pCard1.compareTo(pCard2);
			}
			//but if the suits are different
			else 
			{
				//compare by suit
				aValue = pCard1.aSuit.compareTo(pCard2.aSuit);
			}	
			
			//return the stored value of return
			return aValue;
		}
	}
	
}
