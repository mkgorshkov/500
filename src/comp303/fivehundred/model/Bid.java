/**
 * @author James McCorriston
 */

package comp303.fivehundred.model;

import comp303.fivehundred.util.Card.Suit;

/**
 * Represents a bid or a contract for a player. Immutable.
 */
public class Bid implements Comparable<Bid>
{
	//The maximum number of tricks that can be bid.
	public static final int MAX_TRICKS = 10;
	//The minimum number of tricks that can be bid.
	public static final int MIN_TRICKS = 6;
	//A value used for ranking bids.
	public static final int NO_TRUMP_VALUE = 5;
	//The number of suits.
	public static final int NUM_SUITS = 4;
	//The minimum number of points that a contract is worth.
	public static final int MIN_BID_SCORE = 40;
	//The difference between two consecutively ranked bids.
	public static final int BID_SCORE_INCREMENT = 20;
	//The hash value for a passing bid.
	public static final int PASS_HASH = 25;
	
	// An integer index for the bid which represents the its rank.
	// If this field is negative, it means the bid is a passing bid.
	private int aBidValue;
	// The number of tricks bid, negative for a passing bid.
	private int aTricks;
	// The trump suit bid, null for a passing bid.
	private Suit aSuit;
	
	/**
	 * Constructs a new standard bid (not a pass) in a trump.
	 * @param pTricks Number of tricks bid. Must be between 6 and 10 inclusive.
	 * @param pSuit Suit bid. 
	 * @pre pTricks >= 6 && pTricks <= 10
	 */
	public Bid(int pTricks, Suit pSuit)
	{
		assert pTricks >= MIN_TRICKS && pTricks <= MAX_TRICKS;
		//Calculates the bid index (based on rank)
		aBidValue = (MAX_TRICKS - MIN_TRICKS + 1)*(pTricks - MIN_TRICKS);
		aTricks = pTricks;
		try
		{
			switch (pSuit)
			{
			case SPADES:
				aSuit = Suit.SPADES;
				break;
			case CLUBS:
				aBidValue = aBidValue + 1;
				aSuit = Suit.CLUBS;
				break;
			case DIAMONDS:
				aBidValue = aBidValue + 2;
				aSuit = Suit.DIAMONDS;
				break;
			case HEARTS:
				aBidValue = aBidValue + 3;
				aSuit = Suit.HEARTS;
				break;
			default:
				aBidValue = aBidValue + NO_TRUMP_VALUE - 1;
				break;
			}
		}
		//If the suit is null, the bid is a "no trump" bid.
		catch(NullPointerException e)
		{
			aBidValue = aBidValue + NO_TRUMP_VALUE - 1;
		}
	}
	
	/**
	 * Constructs a new passing bid.
	 */
	public Bid()
	{
		//Sets the field values to those representing a passing bid.
		aTricks = -1;
		aBidValue = -1;
		aSuit = null;
	}
	
	/**
	 * Creates a bid from an index value between 0 and 24 representing all possible
	 * bids in order of strength.
	 * @param pIndex 0 is the weakest bid (6 spades), 24 is the highest (10 no trump),
	 * and everything in between.
	 * @pre pIndex >= 0 && pIndex <= 24
	 */
	public Bid(int pIndex)
	{
		assert pIndex >= 0 && pIndex <= ((MAX_TRICKS-MIN_TRICKS+1)*NO_TRUMP_VALUE - 1);
		//Sets the index of the bid.
		aBidValue = pIndex;
		//Determines the number of tricks in the bid from the index.
		aTricks = pIndex/(NUM_SUITS+1) + MIN_TRICKS;
		//Determines the trump suit of the bid from the index.
		switch (aBidValue % (NUM_SUITS+1))
		{
		case 0:
			aSuit = Suit.SPADES;
			break;
		case 1:
			aSuit = Suit.CLUBS;
			break;
		case 2:
			aSuit = Suit.DIAMONDS;
			break;
		case 3:
			aSuit = Suit.HEARTS;
			break;
		default:
			aSuit = null;
			break;
		}
	}
	
	/**
	 * @return The suit the bid is in, or null if it is in no-trump.
	 * @throws ModelException if the bid is a pass.
	 */
	public Suit getSuit()
	{
		//If this is a passing bid.
		if(aSuit==null && aTricks < 0)
		{
			throw new ModelException("Error: Attempted to get the suit of a passing bid.");
		}
		else
		{
			return aSuit;
		}
	}
	
	/**
	 * @return The number of tricks bid.
	 * @throws ModelException if the bid is a pass.
	 */
	public int getTricksBid()
	{
		//If this is a passing bid.
		if(aTricks<0)
		{
			throw new ModelException("Error: Attempted to get number of tricks bid from a passing bid.");
		}
		else
		{
			return aTricks;
		}
	}
	
	/**
	 * @return True if this is a passing bid.
	 */
	public boolean isPass()
	{
		return aBidValue < 0;
	}
	
	/**
	 * @return True if the bid is in no trump.
	 */
	public boolean isNoTrump()
	{
		return aSuit == null;
	}

	@Override
	public int compareTo(Bid pBid)
	{
		int result;
		//Compares by the index.
		if(isPass() || pBid.isPass())
		{
			if(isPass() && pBid.isPass())
			{
				result = 0;
			}
			else if(isPass())
			{
				result = -1;
			}
			else
			{ 
				result = 1;
			}
		}	
		else if(aBidValue < pBid.toIndex())
		{
			result = -1;
		}
		else if(aBidValue > pBid.toIndex())
		{
			result = 1;
		}
		else
		{
			result = 0;
		}
		return result;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "" + String.valueOf(aTricks) + " " + aSuit;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pBid)
	{
		Bid lBid;
		//Checks for null Bids.
		if(this == null && pBid == null)
		{
			return true;
		}
		else if(this == null || pBid == null)
		{
			return false;
		}
		
		//Throws a ClassCastException if the parameter object is not castable to a Bid.
		try
		{
			lBid = (Bid)pBid;
			if(lBid.isPass() || isPass())
			{
				return lBid.isPass() && isPass();
			}
			else
			{
				//Compares by the bid index.
				return aBidValue == lBid.toIndex();
			}
		}
		catch(ClassCastException e)
		{
			return false;
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		//Returns the bid index unless this is a passing bid, in which case it
		//returns the hash value for a pass (25).
		if(aBidValue < 0)
		{
			return PASS_HASH;
		}
		else
		{
			return aBidValue;
		}
	}

	/**
	 * Converts this bid to an index in the 0-24 range.
	 * @return 0 for a bid of 6 spades, 24 for a bid of 10 no-trump,
	 * and everything in between.
	 * @throws ModelException if this is a passing bid.
	 */
	public int toIndex()
	{
		if(aBidValue < 0)
		{
			throw new ModelException("Error: tried to get the index of a passing bid.");
		}
		else
		{
			return aBidValue;
		}
	}
	
	/**
	 * Returns the highest bid in pBids. If they are all passing
	 * bids, returns pass.
	 * @param pBids The bids to compare.
	 * @return the highest bid.
	 */
	public static Bid max(Bid[] pBids)
	{
		//Index value of the maximum bid.
		int maxBidValue = -1;
		//Array index of the maximum bid.
		int maxBid = -1;
		//Iterates through the list of Bids and finds the maximum bid.
		for(int i = 0; i < pBids.length; i++)
		{
			try
			{
				if(pBids[i].toIndex() > maxBidValue)
				{
					maxBidValue = pBids[i].toIndex();
					maxBid = i;
				}
			}
			catch(ModelException e)
			{
				//passing bid
			}
		}
		if(maxBid >= 0)
		{
			return pBids[maxBid];
		}
		//Returns a passing bid if all of the bids were passing bids.
		else if(pBids.length>0)
		{
			return pBids[0];
		}
		else
		{
			throw new ModelException("Tried to get the max bid of an empty bid list.");
		}
	}
	
	/**
	 * @return The score associated with this bid.
	 * @throws ModelException if the bid is a pass.
	 */
	public int getScore()
	{
		if(aBidValue < 0)
		{
			throw new ModelException("Error: Attempted to get the score of a passing bid.");
		}
		else
		{
			//Calculates and returns the score of the bid.
			return MIN_BID_SCORE + (aBidValue * BID_SCORE_INCREMENT);
		}
	}
}
