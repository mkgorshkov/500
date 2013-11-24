/**
 * @author Andrew Borodovski, James McCorriston
 */
package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Enters a bid higher than all previous bids, if it is likely that the bid can be won. Passes otherwise.
 * 
 */
public class AdvancedBiddingStrategy implements IBiddingStrategy
{
	//Setting up constants
	public static final int HIGH_JOKER_SCORE = 4;
	public static final int LOW_JOKER_SCORE = 3;
	public static final int TRUMP_JACK_SCORE = 3;
	public static final int CONVERSE_JACK_SCORE = 2;
	public static final int ACE_SCORE = 2;
	public static final int KING_SCORE = 1;
	public static final int QUEEN_SCORE = 1;
	public static final int MAX_HAND_SIZE = 10;
	public static final int PARTNER_SUIT_BONUS = 4;
	public static final int OPPONENT_SUIT_MINUS = 4;
	public static final int WIN_OR_LOSS_BONUS = 4;
	public static final int SIX_STRENGTH_THRESHOLD = 10;
	public static final int SEVEN_STRENGTH_THRESHOLD = 14;
	public static final int EIGHT_STRENGTH_THRESHOLD = 20;
	public static final int NINE_STRENGTH_THRESHOLD = 25;
	public static final int TEN_STRENGTH_THRESHOLD = 30;
	public static final int CARD_SIZE_BONUS_THRESHOLD = 1;
	public static final int TEN_CONTRACT_STRENGTH = 10;
	public static final int NINE_CONTRACT_STRENGTH = 9;
	public static final int EIGHT_CONTRACT_STRENGTH = 8;
	public static final int SEVEN_CONTRACT_STRENGTH = 7;
	public static final int SIX_CONTRACT_STRENGTH = 6;
	public static final int SCORE_TO_WIN = 500;
	public static final int NUMBER_OF_TRICKS = 10;
	public static final int NONCONTRACT_TRICK_SCORE = 10;
	public static final int NUM_SUITS = 4;
	
	//The scores for each suit, indexed by the enum value of each suit.
	private int[] aSuitScores = new int[NUM_SUITS + 1];
	//The strength of the contract to be bid
	private int aContractStrength = 0;
	//The highest scoring suit, or null if all suit scores are high (indicating no trump contract favoured)
	private Suit aHighestSuit = null;
	//The maximum score among the suits
	private int aMaxScore = 0;
	//The highest previous bid
	private Bid aHighestOpponent = new Bid();
	//The scores of all the players, index by bidding order
	private int[] aPlayerScores = new int[2];
	
	
	/**
	 * Builds a robot that passes the specified percentage number of the time.
	 * 
	 * @param pPreviousBids
	 *          An array of Bids already made, whose size may range from 0 to 3.
	 * @param pHand
	 * 			The hand which is to be used in order for the Bid to be calculated. The hand must have 10 cards.
	 * @return
	 * 			Returns a bid that has an acceptable likelihood to be won.
	 */
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		//Required parameters are asserted
		assert pPreviousBids.length <= 3;
		assert pHand.size() == MAX_HAND_SIZE;
		
		//Resets all the global variables.
		aSuitScores = new int[NUM_SUITS + 1];
		aContractStrength = 0;
		aHighestSuit = null;
		aMaxScore = 0;
		aHighestOpponent = new Bid();
		
		//The Bid to be returned
		Bid lBid = new Bid();
		
		//The scores for each suit, where Spades, Clubs, Diamonds and Hearts are at positions 0-3 respectively.
		aSuitScores[3] = suitScore(Suit.HEARTS, pHand);
		aSuitScores[1] = suitScore(Suit.CLUBS, pHand);
		aSuitScores[0] = suitScore(Suit.SPADES, pHand);
		aSuitScores[2] = suitScore(Suit.DIAMONDS, pHand);
		
		//The no trump scores take into account jokers
		if (pHand.getJokers().size() > 0)
		{
			if (pHand.getJokers().contains(new Card(Joker.HIGH)))
			{
				aSuitScores[4] += HIGH_JOKER_SCORE;
			}
			if (pHand.getJokers().contains(new Card(Joker.LOW)))
			{
				aSuitScores[4] += LOW_JOKER_SCORE;
			}
		}

		//The scores are increased if a partner bid the same suit, and decreased if an opponent did.
		//If an opponent made a bid that is going to make him win, the chances of making a bid increase.
		switch (pPreviousBids.length)
		{
		case 3:
			updateScore(true, pPreviousBids[0]);
			winModifier(0, pPreviousBids);
			updateScore(false, pPreviousBids[1]);
			updateScore(true, pPreviousBids[2]);
			winModifier(2, pPreviousBids);
			break;
		case 2:
			updateScore(false, pPreviousBids[0]);
			updateScore(true, pPreviousBids[1]);
			winModifier(1, pPreviousBids);
			break;
		case 1:
			updateScore(true, pPreviousBids[0]);
			winModifier(0, pPreviousBids);
			break;
		default:
		}
		
		//The max score, highest suit and contract strength are updated
		update();
		
		//The bid is chose from the current scores, bids, contract strength, etc.
		lBid = chooseBid(pPreviousBids);
		
		//In the case where winning the contract could lead to a loss, making a contract becomes
		//less likely, unless winning the contract, but not making it would yield a win
		//for the other team.
		if (!lBid.isPass() && pPreviousBids.length > 0 && -lBid.getScore() +
				aPlayerScores[pPreviousBids.length % 2] <= -SCORE_TO_WIN)
		{
			for (int i = 0; i < 4; i++)
			{
				aSuitScores[i] -= WIN_OR_LOSS_BONUS;
			}
			
			//The max score, highest suit and contract strength are updated
			update();
			
			//The bid is chose from the current scores, bids, contract strength, etc.
			lBid = chooseBid(pPreviousBids);
		}
		
		return lBid;
	}
	
	/**
	 * This method takes in an array of scores, index the same way as the bids that were passed in,
	 * and saves it for future use.
	 * 
	 * @param pScores The array of scores
	 */
	
	public void setScores(int[] pScores)
	{
		for (int i = 0; i < 2; i++)
		{
			aPlayerScores[i] = pScores[i];
		}
	}
	
	/**
	 * Updates the max score, the highest suit and the contract strength.
	 */
	private void update()
	{
		//The maximum score is found
		aMaxScore = findMax();
		
		//The contract strength is determined (How many tricks can be won)
		aContractStrength = findContractStrength();
		
		//The suit with the highest score is found.
		aHighestSuit = findHighestSuit();
	}
	
	/**
	 * This method looks at the current bids, the current suit scores and the current
	 * contract strength in order to decide which bid should be made.
	 */
	private Bid chooseBid(Bid[] pPreviousBids)
	{
		Bid lBid = new Bid();
		
		//In the case where the hand was not good enough to bid with, and findContractStrength did not assign a
		//contract strength, a passing bid will be made.
		if (aContractStrength >= SIX_CONTRACT_STRENGTH)
		{
			//Checking to see if any Bids were made previously
			if (pPreviousBids.length == 0)
			{
					lBid = new Bid(aContractStrength, aHighestSuit);
			}
			else
			{
				//The highest bid is found
				aHighestOpponent = Bid.max(pPreviousBids);
				
				//It is determined whether the contract strength and highest scoring suit beat the best
				//previous bid.
				if (aHighestOpponent.isPass())
				{
					lBid = new Bid(aContractStrength, aHighestSuit);
				}
				else if (aContractStrength > aHighestOpponent.getTricksBid())
				{
					lBid = new Bid(aContractStrength, aHighestSuit);
				}
				else if (aContractStrength == aHighestOpponent.getTricksBid())
				{
					if (!(aHighestOpponent.getSuit()==null))
					{
						if (aHighestSuit == null || aHighestSuit.compareTo(aHighestOpponent.getSuit()) > 0)
						{
							lBid = new Bid(aContractStrength, aHighestSuit);
						}
					}
				}
			}
		}
		
		return lBid;
	}
	
	/**
	 * This method takes all the bids made so far and an opponent index, and if the opponent
	 * has the highest bid so far and, if they make the contract with that bid,
	 * they will win the game, then the chances of making any winning bid increase.
	 * @param pIndex The index of the opponent in the bid and score arrays
	 * @param pBids The bids that have been made so far
	 */	
	private void winModifier(int pIndex, Bid[] pBids)
	{
		if (!pBids[pIndex].isPass() && pBids[pIndex].getScore() + aPlayerScores[pIndex % 2] >=
				SCORE_TO_WIN && pBids[pIndex].equals(Bid.max(pBids)))
		{
			for (int i = 0; i < 4; i++)
			{
				aSuitScores[i] += WIN_OR_LOSS_BONUS;
			}
		}
	}
	
	/**
	 * This method takes a boolean, saying whether the given bid was made by a partner or an opponent,
	 * and the bid itself, and then updates the suit scores based on that bid.
	 * 
	 * @param pOpponent
	 * 		If this is true, then the Bid was made by an opponent, otherwise it was made by a partner.
	 * @param pBid
	 * 		This is the Bid made.
	 */
	private void updateScore(boolean pOpponent, Bid pBid)
	{
		int bonus = 0;
		if (pOpponent)
		{
			bonus = -OPPONENT_SUIT_MINUS;
		}
		else
		{
			bonus = PARTNER_SUIT_BONUS;
		}
		if (!pBid.isPass())
		{
			if (!(pBid.isNoTrump()))
			{
				aSuitScores[pBid.getSuit().ordinal()] += bonus;
			}
			else
			{
				aSuitScores[4] += bonus;
			}
		}
	}
	
	/**This helper method takes a score, determines which contract strength threshold it passes,
	*and thereby returns the appropriate contract strength.
	*@param pMaxScore
	*	The maximum score of those determined for the suits
	*@return
	*	The number of tricks that a suit of that score should win
	*
	**/
	private int findContractStrength()
	{
		int lContractStrength = 0;
		
		if (aMaxScore > TEN_STRENGTH_THRESHOLD)
		{
			lContractStrength = TEN_CONTRACT_STRENGTH;
		}
		else if (aMaxScore > NINE_STRENGTH_THRESHOLD)
		{
			lContractStrength = NINE_CONTRACT_STRENGTH;
		}
		else if (aMaxScore > EIGHT_STRENGTH_THRESHOLD)
		{
			lContractStrength = EIGHT_CONTRACT_STRENGTH;
		}
		else if (aMaxScore > SEVEN_STRENGTH_THRESHOLD)
		{
			lContractStrength = SEVEN_CONTRACT_STRENGTH;
		}
		else if (aMaxScore > SIX_STRENGTH_THRESHOLD)
		{
			lContractStrength = SIX_CONTRACT_STRENGTH;
		}
		
		return lContractStrength;
	}
	
	/**This method takes a maximum score, and the scores of the suits used to find it. It then
	 * returns the suit which the highest score belonged to.
	 * In the case of a tie, the suit which yields a contract of greater value at equal strength
	 * is returned.
	 * @param pSuitScores
	 * 		An array of integer scores, organized so that the scores for Spades, Clubs, Diamonds
	 * 		and Hearts are at positions 0 through 3 respectively.
	 * @param pMaxScore
	 * 		The maximum of the scores found in the array
	 * @return
	 * 		The suit that the maximum score corresponds to
	 */
	private Suit findHighestSuit()
	{
		Suit lHighestSuit = null;
		
		if (aMaxScore == aSuitScores[0])
		{
			lHighestSuit = Suit.SPADES;
		}
		if (aMaxScore == aSuitScores[1])
		{
			lHighestSuit = Suit.CLUBS;
		}
		if (aMaxScore == aSuitScores[2])
		{
			lHighestSuit = Suit.DIAMONDS;
		}
		if (aMaxScore == aSuitScores[3])
		{
			lHighestSuit = Suit.HEARTS;
		}
		if(aMaxScore == aSuitScores[4])
		{
			lHighestSuit = null;
		}		
		
		return lHighestSuit;
	}
	
	/**This method takes an array of ints and returns the maximum.
	 * 
	 * @param pInts
	 * 		An array of integers
	 * @return
	 * 		The maximum of the given integers
	 */
	private int findMax()
	{
		int lMaxScore = 0;
		for (int i = 0; i < NUM_SUITS + 1; i++)
		{
			if (lMaxScore < aSuitScores[i])
			{
				lMaxScore = aSuitScores[i];
			}
		}
		return lMaxScore;	
	}
	
	/**This method takes a hand and a suit, and determines a score for the given suit,
	*based on the hand. The score constants declared earlier are used. Also, 1 extra point is
	*given to the suit per card of that suit in the hand, above 5.
	*
	*@param pSuit
	*		A suit for which a score needs to be determined
	*@param pHand
	*		The hand to be used when calculating the score of the suit
	*@return
	*		The score of the given suit.
	*/
	private int suitScore(Card.Suit pSuit, Hand pHand)
	{
		CardList cards = pHand.clone().getTrumpCards(pSuit);
		CardList otherCards = pHand.clone().getNonTrumpCards(pSuit);
		int score = 0;
		
		for (Card card : cards)
		{
			if (card.isJoker())
			{
				if (card.getJokerValue()==Joker.HIGH)
				{
					score += HIGH_JOKER_SCORE;
				}
				else
				{
					if (cards.contains(new Card(Joker.HIGH)))
					{
						score += 1;
					}
					score += LOW_JOKER_SCORE;
				}
			}
			else if (card.getRank()==Rank.ACE)
			{
				if (cards.contains(new Card(Rank.JACK, pSuit.getConverse())))
				{
					score += 1;
				}
				score += ACE_SCORE;
				aSuitScores[4] += ACE_SCORE + 1;
			}
			else if (card.getRank()==Rank.KING)
			{
				if (cards.contains(new Card(Rank.ACE, pSuit)))
				{
					aSuitScores[4] += ACE_SCORE;
					score += 1;
				}
				score += KING_SCORE;
				aSuitScores[4] += 1;
			}
			else if (card.getRank()==Rank.QUEEN)
			{
				if (cards.contains(new Card(Rank.KING, pSuit)))
				{
					aSuitScores[4] += 1;
					score += 1;
				}
				score += QUEEN_SCORE;
			}
			else if (card.getRank()==Rank.JACK)
			{
				if (card.getSuit()==pSuit)
				{
					if (cards.contains(new Card(Joker.LOW)))
					{
						score += 1;
					}
					score += TRUMP_JACK_SCORE;
				}
				else
				{
					if (cards.contains(new Card(Rank.JACK, pSuit)))
					{
						score += 1;
					}
					score += CONVERSE_JACK_SCORE;
				}
			}
		}
		for (int i = cards.size(); i > CARD_SIZE_BONUS_THRESHOLD; i--)
		{
			score ++;
		}
		
		for (Card card: otherCards)
		{
			if (card.getRank() == Rank.ACE)
			{
				score += ACE_SCORE;
			}
			if (card.getRank() == Rank.KING && otherCards.contains(new Card(Rank.ACE, card.getSuit())))
			{
				score += 1;
			}
		}
		
		isInstantWin(pSuit, pHand);
		
		return score;
	}
	
	/**
	 * This method returns true if you can automatically win a 10, no-trump contract.
	 * @param pSuit The current suit being examined
	 * @param pHand The current hand of cards
	 */
	private void isInstantWin(Suit pSuit, Hand pHand)
	{
		if (pHand.getTrumpCards(pSuit).size() == MAX_HAND_SIZE)
		{
			if(pHand.getJokers().size() == 2 && pHand.contains(new Card(Rank.ACE, pSuit)) &&
					pHand.contains(new Card(Rank.KING, pSuit)) &&
					pHand.contains(new Card(Rank.QUEEN, pSuit)))
			{
				aSuitScores[4] += SCORE_TO_WIN;
			}
		}
	}
}
