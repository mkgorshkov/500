/**
 * @author Andrew Borodovski, James McCorriston
 */
package comp303.fivehundred.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Scores cards based on the circumstances and discards the cards of least worth.
 */
public class AdvancedCardExchangeStrategy implements ICardExchangeStrategy
{
	public static final int HANDSIZE = 10;
	public static final int INITIAL_HAND_SIZE = 16;
	public static final int ACE_HIGH = 9;
	public static final int ACE_MID = 6;
	public static final int ACE_LOW = 0;
	public static final int KING_LOW = 3;
	public static final int TRUMP_BASE = 30;
	public static final int BASE_WIDOW_SIZE = 6;
	public static final int VOID_PENALTY = 2;
	
	//The value by which the score of card is changed if another player made a bid
	//of the same suit as that card.
	private int[] aBidFactor = new int[4];
	//An array of lists of scores which correlate to the cards in aCardArray
	private ArrayList<LinkedList<Integer>> aScoreArray;
	//The cards in the hand are organized in decreasing rank, by suit
	private ArrayList<LinkedList<Card>> aCardArray;
	//The trump suit of the given contract
	private Suit aTrump;
	//The number of cards left to discard
	private int aCardsLeft;

	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		//The trump suit is set
		aTrump = pBids[pIndex].getSuit();
		//The cards that will be discarded
		CardList lDiscards = new CardList();
		//In the case of a no-trump contract, with no Jokers in the hand, the
		//cards with the lowest rank are discarded.
		if (aTrump == null && pHand.getJokers().size() == 0)
		{
			// pHandClone is a copy of the hand.
			CardList pHandClone = pHand.clone();

			// If there is no trump suit go through and just remove lowest rank
			// Removes 10 cards from the copy of the hand leaving 6 (to be discarded).
			for (int i = 0; i < INITIAL_HAND_SIZE - HANDSIZE; i++)
			{
				Card lowest = ((Hand) pHandClone).selectLowest(aTrump);
				lDiscards.add(lowest);
				pHandClone.remove(lowest);
			}
			return lDiscards;
		}
		//The card array is initialized
		aCardArray = new ArrayList<LinkedList<Card>>();
		//The score array is initialized
		aScoreArray = new ArrayList<LinkedList<Integer>>();
		//The number of cards left to discard is reset
		aCardsLeft = BASE_WIDOW_SIZE;
		//A comparator for sorting cards when there is a trump suit
		Card.BySuitComparator trumpComparator = new Card.BySuitComparator();
		//A comparator for sorting cards when there is no trump suit
		Card.BySuitNoTrumpComparator noTrumpComparator = new Card.BySuitNoTrumpComparator();
		//The trump suit of the trump comparator is set
		trumpComparator.setTrump(aTrump);
		Card discard;
		CardList hand;
		//The hand is sorted
		if (aTrump == null)
		{
			hand = pHand.getNonJokers().sort(noTrumpComparator);
		}
		else
		{
			hand = pHand.sort(trumpComparator);
		}
		//The bid factor is calculated based on the bids of all other players
		for (Bid bid : pBids)
		{
			if (!bid.isPass() && !bid.isNoTrump() && !bid.equals(pBids[pIndex]))
			{
				//The bid factor is -1 if another player made a bid of the given suit
				aBidFactor[bid.getSuit().ordinal()] = -1;
			}
		}
		//The lists inside the score and card arrays are reset
		for (int i = 0; i < 4; i++)
		{
			aCardArray.add(new LinkedList<Card>());
			aScoreArray.add(new LinkedList<Integer>());
		}
		
		//The card array is filled with cards in the proper order.
		for (Card card : hand)
		{
			aCardArray.get(card.getEffectiveSuit(aTrump).ordinal()).add(0, card);
		}
		
		//The cards to discard are chosen
		for (int i = 0; i < BASE_WIDOW_SIZE; i++)
		{
			//The scores for all cards in the hand are found
			updateScores(hand);
			//A card is chosen to discard
			discard = chooseDiscard();
			//The given card is added to the lists of discards
			lDiscards.add(discard);
			//The card array is updated
			aCardArray.get(discard.getEffectiveSuit(aTrump).ordinal()).removeLast();
			//The hand is updated
			hand.remove(discard);
			//The number of cards left to discard is decremented
			aCardsLeft --;			
		}
		
		return lDiscards;
	}
	/**
	 * This method chooses a card to discard based on the bid factors and the scores
	 * of all of the cards.
	 * @return The card which should be discarded
	 */
	private Card chooseDiscard()
	{
		//The card to be discarded
		Card lCard;
		//The indices of the suits which have cards that will be considered for discarding
		ArrayList<Integer> indexLowest = new ArrayList<Integer>();
		//The indices are found
		for (int i = 0; i < 4; i++)
		{
			//If the index list is currently empty, the current index is added to it.
			if (aScoreArray.get(i).size() > 0)
			{
				if (indexLowest.size() == 0)
				{
					indexLowest.add(new Integer(i));
				}
				else
				{
					//Otherwise, if at the current index, the highest card in the suit has a lower
					//score than the highest cards of the suits indexed, then the index list is cleared
					//and the current index is added.
					if (aScoreArray.get(indexLowest.get(0).intValue()).getFirst().intValue() > aScoreArray.get(i).getFirst().intValue())
					{
						indexLowest.clear();
						indexLowest.add(new Integer(i));
					}
					//If the score of the highest card in the indexed suit matches the highest cards
					//of the suits already index, in score, then the current index is added to the
					//list of indices.
					else if (aScoreArray.get(indexLowest.get(0).intValue()).getFirst().equals(aScoreArray.get(i).getFirst()))
					{
						indexLowest.add(new Integer(i));
					}
				}
			}
		}
		//By default, the card to be returned is set to the lowest card of the first indexed suit.
		lCard = aCardArray.get(indexLowest.get(0).intValue()).getLast();
		
		//The tie break method is used to determined which of the indexed suits the card to be
		//returned belongs to.
		for (int i = 1; i < indexLowest.size(); i++)
		{
			lCard = tieBreak(lCard, aCardArray.get(indexLowest.get(i).intValue()).getLast());
		}
		
		return lCard;
	}
	
	/**
	 * This method uses the score method to score each card and store the resulting scores.
	 * @param pHand The hand of cards to be scored.
	 */
	private void updateScores(CardList pHand)
	{
		//The list of score arrays for each suit are reset
		for (int i = 0; i < 4; i++)
		{
			aScoreArray.set(i, new LinkedList<Integer>());
		}
		//New scores are calculated for each card
		for (Card card : pHand)
		{
			aScoreArray.get(card.getEffectiveSuit(aTrump).ordinal()).add(0, new Integer(score(card)));
		}
	}
	
	/**
	 * This method takes in a card, and scores it based on the other cards in the hand,
	 * as well as the trump suit, the cards suit, and the card's rank.
	 * @param pCard The card to be scored
	 * @return The score for the given card
	 */
	private int score(Card pCard)
	{
		//The default score is zero
		int lScore = 0;
		//All trump cards score higher than all non-trump cards. Within the trump suit,
		//as the rank of a card increases, so does its score
		if (pCard.getEffectiveSuit(aTrump) == aTrump)
		{
			if (pCard.isJoker())
			{
				lScore = TRUMP_BASE + pCard.hashCode(); 
			}
			else
			{
				lScore = TRUMP_BASE + pCard.getRank().ordinal();
			}
		}
		//Aces are cards worthy of score.
		else if (pCard.getRank() == Rank.ACE)
		{
			//Aces get high scores if they are the only card of the given suit in the hand
			if (aCardArray.get(pCard.getSuit().ordinal()).size() == 1)
			{
				lScore += ACE_HIGH;
			}
			//Aces get medium scores if there are at most 4 cards of that suit in the hand
			else if (aCardArray.get(pCard.getSuit().ordinal()).size() <= 4)
			{
				lScore += ACE_MID;
			}
			//Aces get low scores if there are many cards of their suit in the hand
			else
			{
				lScore += ACE_LOW;
			}
		}
		//Kings are worthy of score as well
		else if (pCard.getRank() == Rank.KING)
		{
			//A threshold of cards of the king's suit in the hand,
			//determining the worth of the king
			int threshold = 4;
			
			//The threshold depends on the number of cards of the suit out there
			//Therefore, when the king is of the converse suit, the threshold is lower
			if (aTrump != null && pCard.getEffectiveSuit(aTrump) != aTrump.getConverse())
			{
				threshold += 1;
			}
			
			//The king is only worth a lot if the hand also contains the ace. Otherwise,
			//the kind may be beaten by that ace and so is worth less.
			if ((aCardArray.get(pCard.getSuit().ordinal())).get(0).getRank() == Rank.ACE &&
					aCardArray.get(pCard.getSuit().ordinal()).size() <= threshold)
			{
				lScore += ACE_MID;
			}
			
			//If many cards of the king's suit are present, or only the king is present
			//then the king is worth less.
			else if (aCardArray.get(pCard.getSuit().ordinal()).size() > threshold || 
						aCardArray.get(pCard.getSuit().ordinal()).size() == 1)
			{
				lScore += ACE_LOW;
			}
			
			//If the ace is not present, bu there are a few cards of the suit in the hand,
			//the king is worth an intermediate amount.
			else
			{
				lScore += KING_LOW;
			}
		}
		
		//It is advantageous to go void in a suit, so if it is possible to go void in the
		//given suit with the number of discards still available, discarding from that
		//suit will be preferred.
		if (aCardArray.get(pCard.getEffectiveSuit(aTrump).ordinal()).size() <= aCardsLeft)
		{
			lScore -= VOID_PENALTY;
		}
		
		//The bids of opponents and partners are taken into account
		lScore += aBidFactor[pCard.getEffectiveSuit(aTrump).ordinal()];
		
		return lScore;
	}
	
	/**
	 * This method takes two cards which are lowest cards of suits with the lowest scores
	 * and returns the one which should be discarded.
	 * @param pCard1 One card to be considered
	 * @param pCard2 The other card to be considered
	 * @return The card which should be discarded
	 */
	private Card tieBreak(Card pCard1, Card pCard2)
	{
		//The card to be returned
		Card lCard;
		
		//The card from the suit with fewer cards should be discarded
		if (aCardArray.get(pCard1.getSuit().ordinal()).size() < aCardArray.get(pCard2.getSuit().ordinal()).size())
		{
			lCard = pCard1;
		}
		else if (aCardArray.get(pCard2.getSuit().ordinal()).size() < aCardArray.get(pCard1.getSuit().ordinal()).size())
		{
			lCard = pCard2;
		}
		
		//If both suits have the same number of cards, card rank is considered
		else
		{
			Card.ByRankComparator comparator = new Card.ByRankComparator();
			
			//The card of lower rank is discarded
			if (comparator.compare(pCard1, pCard2) < 0)
			{
				lCard = pCard1;
			}
			else if (comparator.compare(pCard1, pCard2) > 0)
			{
				lCard = pCard2;
			}
			//If the cards have the same rank, the rank of the highest cards of their suits are compared
			else
			{
				//The card with the highest card in the suit of lower rank is discarded
				if (comparator.compare(aCardArray.get(pCard2.getSuit().ordinal()).getFirst(),
						aCardArray.get(pCard1.getSuit().ordinal()).getFirst()) > 0)
				{
					lCard = pCard1;
				}
				else if (comparator.compare(aCardArray.get(pCard2.getSuit().ordinal()).getFirst(),
						aCardArray.get(pCard1.getSuit().ordinal()).getFirst()) < 0)
				{
					lCard = pCard2;
				}
				//If the highest cards in both suits are the same, then it is checked if one of the cards is of the
				//inverse trump suit. If one is, then it is kept. Otherwise, the first card is returned.
				else if (pCard1.getSuit() == aTrump.getConverse())
				{
					lCard = pCard2;
				}
				else
				{
					lCard = pCard1;
				}
			}
		}
		
		return lCard;
	}
}
