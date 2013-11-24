/**
 * @author Andrew Borodovski
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
public class BasicBiddingStrategy implements IBiddingStrategy
{
	//Setting up constants
	public static final int HIGH_JOKER_SCORE = 5;
	public static final int LOW_JOKER_SCORE = 4;
	public static final int TRUMP_JACK_SCORE = 3;
	public static final int CONVERSE_JACK_SCORE = 2;
	public static final int ACE_SCORE = 2;
	public static final int KING_SCORE = 1;
	public static final int QUEEN_SCORE = 1;
	public static final int MAX_HAND_SIZE = 10;
	public static final int PARTNER_SUIT_BONUS = 5;
	public static final int OPPONENT_SUIT_MINUS = 5;
	public static final int SIX_STRENGTH_THRESHOLD = 10;
	public static final int SEVEN_STRENGTH_THRESHOLD = 12;
	public static final int EIGHT_STRENGTH_THRESHOLD = 14;
	public static final int NINE_STRENGTH_THRESHOLD = 16;
	public static final int TEN_STRENGTH_THRESHOLD = 18;
	public static final int CARD_SIZE_BONUS_THRESHOLD = 5;
	public static final int NO_TRUMP_SCORE_DIVISOR = 7;
	public static final int TEN_CONTRACT_STRENGTH = 10;
	public static final int NINE_CONTRACT_STRENGTH = 9;
	public static final int EIGHT_CONTRACT_STRENGTH = 8;
	public static final int SEVEN_CONTRACT_STRENGTH = 7;
	public static final int SIX_CONTRACT_STRENGTH = 6;
	
	//The scores for each suit, indexed by the enum value of each suit.
	private int[] aSuitScores = new int[4];
	//The strength of the contract to be bid
	private int aContractStrength = 0;
	//The highest scoring suit, or null if all suit scores are high (indicating no trump contract favoured)
	private Suit aHighestSuit = null;
	//The maximum score among the suits
	private int aMaxScore = 0;
	//The highest previous bid
	private Bid aHighestOpponent = new Bid();
	//A bonus that can be positive or negative, depending on what previous bids were
	private int aNoTrumpBonus = 0;
	
	
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
		aSuitScores = new int[4];
		aContractStrength = 0;
		aHighestSuit = null;
		aMaxScore = 0;
		aHighestOpponent = new Bid();
		aNoTrumpBonus = 0;
		
		//The Bid to be returned
		Bid lBid = new Bid();
		
		//The scores for each suit, where Spades, Clubs, Diamonds and Hearts are at positions 0-3 respectively.
		aSuitScores[3] = suitScore(Suit.HEARTS, pHand);
		aSuitScores[1] = suitScore(Suit.CLUBS, pHand);
		aSuitScores[0] = suitScore(Suit.SPADES, pHand);
		aSuitScores[2] = suitScore(Suit.DIAMONDS, pHand);

		//The scores are increased if a partner bid the same suit, and decreased if an opponent did.
		switch (pPreviousBids.length)
		{
		case 3:
			updateScore(true, pPreviousBids[0]);
			updateScore(false, pPreviousBids[1]);
			updateScore(true, pPreviousBids[2]);
			break;
		case 2:
			updateScore(false, pPreviousBids[0]);
			updateScore(true, pPreviousBids[1]);
			break;
		case 1:
			updateScore(true, pPreviousBids[0]);
			break;
		default:
		}
		
		//The maximum score is found
		aMaxScore = findMax(aSuitScores);
		
		//The contract strength is determined (How many tricks can be won)
		aContractStrength = findContractStrength(aMaxScore);
		
		//The suit with the highest score is found.
		aHighestSuit = findHighestSuit(aSuitScores, aMaxScore);
		
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
				aNoTrumpBonus += bonus;
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
	private int findContractStrength(int pMaxScore)
	{
		int lContractStrength = 0;
		
		if (pMaxScore > TEN_STRENGTH_THRESHOLD)
		{
			lContractStrength = TEN_CONTRACT_STRENGTH;
		}
		else if (pMaxScore > NINE_STRENGTH_THRESHOLD)
		{
			lContractStrength = NINE_CONTRACT_STRENGTH;
		}
		else if (pMaxScore > EIGHT_STRENGTH_THRESHOLD)
		{
			lContractStrength = EIGHT_CONTRACT_STRENGTH;
		}
		else if (pMaxScore > SEVEN_STRENGTH_THRESHOLD)
		{
			lContractStrength = SEVEN_CONTRACT_STRENGTH;
		}
		else if (pMaxScore > SIX_STRENGTH_THRESHOLD)
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
	private Suit findHighestSuit(int[] pSuitScores, int pMaxScore)
	{
		Suit lHighestSuit = null;
		int totalScore = pSuitScores[0] + pSuitScores[1] + pSuitScores[2] + pSuitScores[3];
		
		if (pMaxScore == pSuitScores[0])
		{
			lHighestSuit = Suit.SPADES;
		}
		if (pMaxScore == pSuitScores[1])
		{
			lHighestSuit = Suit.CLUBS;
		}
		if (pMaxScore == pSuitScores[2])
		{
			lHighestSuit = Suit.DIAMONDS;
		}
		if (pMaxScore == pSuitScores[3])
		{
			lHighestSuit = Suit.HEARTS;
		}
		if((int)((totalScore + aNoTrumpBonus)/NO_TRUMP_SCORE_DIVISOR) >= aContractStrength)
		{
			aContractStrength = (totalScore + aNoTrumpBonus)/NO_TRUMP_SCORE_DIVISOR;
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
	private int findMax(int[] pInts)
	{
		int lMaxScore = 0;
		for (int i = 0; i < 4; i++)
		{
			if (lMaxScore < pInts[i])
			{
				lMaxScore = pInts[i];
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
					score += LOW_JOKER_SCORE;
				}
			}
			else if (card.getRank()==Rank.ACE)
			{
				score += ACE_SCORE;
			}
			else if (card.getRank()==Rank.KING)
			{
				score += KING_SCORE;
			}
			else if (card.getRank()==Rank.QUEEN)
			{
				score += QUEEN_SCORE;
			}
			else if (card.getRank()==Rank.JACK)
			{
				if (card.getSuit()==pSuit)
				{
					score += TRUMP_JACK_SCORE;
				}
				else
				{
					score += CONVERSE_JACK_SCORE;
				}
			}
		}
		for (int i = cards.size(); i > CARD_SIZE_BONUS_THRESHOLD; i--)
		{
			score ++;
		}
		
		return score;
	}
}
