/**
 * @author James McCorriston
 */
package comp303.fivehundred.ai;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.MemoryList;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Card.Suit;

/**
 * If leading pick a random card (except for the joker, if no trump). If not leading pick the lowest legal card that wins the trick.
 * If you cannot win the trick, play the lowest legal card.
 */
public class AdvancedPlayingStrategy implements IPlayingStrategy
{
	private MemoryList aMemoryList = new MemoryList();
	private Bid[] aBids;
	//The index in the list of bids of the advanced AI player.
	private int aBidIndex;
	private Suit aTrump;
	private CardList aBestLeads = new CardList();
	
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
		
		//Resets aBestLeads.
		aBestLeads = new CardList();
		//Sets the trump suit field.
		aTrump = pTrick.getTrumpSuit();
		//The card to be returned. By default, just the first card in the hand.
		Card lCard = pHand.getFirst();
		//The cards that are legal to play.
		CardList playable = pHand.playableCards(pTrick.getSuitLed(), pTrick.getTrumpSuit());
		//A by suit comparator, with the trump either set to the trump suit, or if none, the leading suit.
		Card.BySuitComparator suitComparator = new Card.BySuitComparator();
		//A by rank comparator
		Card.ByRankComparator rankComparator = new Card.ByRankComparator();
		//A no trump comparator.
		Card.BySuitNoTrumpComparator noTrumpComparator = new Card.BySuitNoTrumpComparator();
		
		//Setting the trump suit for the comparator.
		if (pTrick.getTrumpSuit() == null)
		{
			suitComparator.setTrump(pTrick.getSuitLed());
		}
		else
		{
			suitComparator.setTrump(aTrump);
		}
		
		//Sort the playable cards, using the leading suit as the trump.
		if(pTrick.getTrumpSuit() == null)
		{
			playable = playable.sort(noTrumpComparator);
		}
		else
		{
			playable = playable.sort(suitComparator);
		}
		
		// If the trick is empty, choose a leading card.
		if (pTrick.size() == 0)
		{
			lCard = chooseLead(pHand);
		}
		// If someone has already led and the partner is winning the trick.
		else if((pTrick.size() - pTrick.winnerIndex()) % 2 == 0)
		{
			lCard = partnerWinning(pHand, pTrick);
		}
		//Someone has already led and an opponent is winning the trick.
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
						//Cycle through all playable cards.
						for (Card c : playable)
						{
							//If only one opponent has played so far.
							if(pTrick.size() <= 2)
							{
								//If trump was led, take the first that can beat the current highest card in the trick.
								if(pTrick.getSuitLed() == pTrick.getTrumpSuit() && suitComparator.compare(c, pTrick.highest()) > 0)
								{
									lCard = c;
									//Shouldn't play a card lower than a king.
									if((aMemoryList.getCards(pTrick.getSuitLed(), c).size() < 4) ||
											suitComparator.compare(lCard, new Card(Rank.KING, pTrick.getTrumpSuit())) >= 0)
									{
										break;
									}
								}
								//If non-trump was led, take the first that can beat the current highest card in the trick.
								else if(pTrick.getTrumpSuit() != null && suitComparator.compare(c, pTrick.highest()) > 0)
								{
									lCard = c;
									//Shouldn't play a card lower than a queen.
									if((aMemoryList.getCards(pTrick.getSuitLed(), c).size() < 2) ||
											suitComparator.compare(lCard, new Card(Rank.QUEEN, pTrick.getSuitLed())) >= 0)
									{
										break;
									}
								}
								//Take the first that can beat the current highest card in the trick.
								else if(noTrumpComparator.compare(c, pTrick.highest()) > 0)
								{
									lCard = c;
									//Shouldn't play a card lower than a queen.
									if((aMemoryList.getCards(pTrick.getSuitLed(), c).size() < 2) ||
											suitComparator.compare(lCard, new Card(Rank.QUEEN, pTrick.getSuitLed())) >= 0)
									{
										break;
									}
								}
							}
							//All opponents have played.
							else
							{
								//Take the first that can beat the current highest card in the trick.
								if (suitComparator.compare(c, pTrick.highest()) > 0)
								{
									lCard = c;
									break;
								}
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
	
	/**
	 * Sets the memory list so that cards that have yet to be played (or are not known
	 * to be out of the game) can be considered when deciding each advanced AI move.
	 * @param pMemoryList = the AdvancedPlayer's memory list of cards that other players
	 * 						could have.
	 */
	public void setMemory(MemoryList pMemoryList)
	{
		aMemoryList = pMemoryList;
	}
	
	/**
	 * Stores an array of the bids that were made in this round.
	 * @param pBids = the list of bids that were made in this round.
	 */
	public void setBids(Bid[] pBids)
	{
		aBids = pBids;
	}
	
	/**
	 * Sets the aBidIndex field which represents the index of this advanced AI's bid in
	 * in the array of bids.
	 * 
	 * @param pIndex The bid index.
	 */
	public void setBidIndex(int pIndex)
	{
		aBidIndex = pIndex;
	}
	
	/**
	 * Selects a card to play when your partner is currently winning the trick.
	 * 
	 * @param pHand The advanced AI player's hand.
	 * @param pTrick The current trick.
	 * @return lCard The card to play.
	 */
	private Card partnerWinning(Hand pHand, Trick pTrick)
	{
		Card.BySuitNoTrumpComparator suitNoTrumpComparator = new Card.BySuitNoTrumpComparator();
		Card.BySuitComparator suitComparator = new Card.BySuitComparator();
		suitComparator.setTrump(aTrump);
		Card lCard;
		
		//If the partner is winning the trick and everyone else has played, plays the lowest legal card.
		if(pTrick.size() == 3)
		{
			if(aTrump == null)
			{
				lCard = pHand.playableCards(pTrick.getSuitLed(), aTrump).sort(suitNoTrumpComparator).getFirst();
			}
			else
			{
				lCard = pHand.playableCards(pTrick.getSuitLed(), aTrump).sort(suitComparator).getFirst();
			}
		}
		//If there is a chance that an opponent could beat the partner.
		else if(aMemoryList.getCards(pTrick.highest().getEffectiveSuit(aTrump), 
				pTrick.highest()).size() > 0)
		{
			//Checks to see if you have the highest card in the suit and plays it if you do.
			for(Card card : pHand.playableCards(pTrick.getSuitLed(), pTrick.getTrumpSuit()))
			{
				if(aMemoryList.getCards(card.getEffectiveSuit(aTrump), card).size() == 0)
				{
					lCard = card;
				}
			}
		}
		//Otherwise, plays the lowest legal card.
		if(aTrump == null)
		{
			lCard = pHand.playableCards(pTrick.getSuitLed(), aTrump).sort(suitNoTrumpComparator).getFirst();
		}
		else
		{
			lCard = pHand.playableCards(pTrick.getSuitLed(), aTrump).sort(suitComparator).getFirst();
		}
		return lCard;
	}
	
	/**
	 * Returns the card to lead with.
	 * 
	 * @param pHand = the hand of the robot.
	 * @return The card to lead with.
	 */
	private Card chooseLead(Hand pHand)
	{	
		aBestLeads = new CardList();
		Hand hand;
		Card.BySuitComparator suitComparator = new Card.BySuitComparator();
		Card.BySuitNoTrumpComparator noTrumpComparator = new Card.BySuitNoTrumpComparator();
		Card.ByRankComparator rankComparator = new Card.ByRankComparator();
		//Sorts the hand according to the contract.
		if(aTrump == null)
		{
			hand = (Hand)pHand.sort(noTrumpComparator);
		}
		else
		{
			suitComparator.setTrump(aTrump);
			hand = (Hand)pHand.sort(suitComparator);
		}
		
		//Prioritizes different types of leads and adds them to aBestLeads (the last card added
		//is the best card to lead with).
		noChoice(hand);
		nonTrumpHighLead(hand);
		if(aTrump != null)
		{
			trumpHighLead(hand);
		}
		bankOnPartner(hand);
		if(aBestLeads.size()==0)
		{
			//If a card still has not been chosen, lead with the highest card.
			aBestLeads.add(hand.canLead(aTrump == null).sort(rankComparator).getLast());
		}

		return aBestLeads.getLast();
	}
	
	/**
	 * Checks if pCard is the highest card in its suit by using the memory list.
	 * 
	 * @param pCard = the card being analyzed.
	 * @return true if pCard is the highest card in its suit, false otherwise.
	 */
	private boolean isHighestInSuit(Card pCard)
	{
		return aMemoryList.getCards(pCard.getEffectiveSuit(aTrump), pCard).size() == 0;
	}
	
	/**
	 * Checks if there's a possibility that the given card can be trumped by an opponent.
	 * 
	 * @param pSuit = the suit of the card being analyzed.
	 * @return true if an opponent is void in the card's suit and if they may have a
	 * 		   trump card, false otherwise.
	 */
	private boolean opponentCouldTrump(Suit pSuit)
	{
		//If pSuit is null, the card was a joker so the only way it can be trumped is
		//if the high joker is still in the memory list.
		if(pSuit == null)
		{
			return aMemoryList.contains(new Card(Joker.HIGH));
		}
		else if(aTrump == null)
		{
			return aMemoryList.contains(new Card(Joker.HIGH)) || 
					aMemoryList.contains(new Card(Joker.LOW));
		}
		
		//If the first opponent could trump this card.
		if(!aMemoryList.firstOpponentIsVoid(aTrump))
		{
			return true;
		}
		//If the second opponent could trump this card.
		else
		{
			return !aMemoryList.secondOpponentIsVoid(aTrump);
		}
	}
	
	/**
	 * Checks if there's a possibility that the given card can be trumped by the partner.
	 * 
	 * @param pSuit = the suit of the card being analyzed.
	 * @return true if the partner is void in the card's suit and if they may have a
	 * 		   trump card, false otherwise.
	 */
	private boolean partnerCouldTrump(Suit pSuit)
	{
		//If pSuit is null, the card was a joker so the only way it can be trumped is
		//if the high joker is still in the memory list.
		if(pSuit == null)
		{
			return aMemoryList.contains(new Card(Joker.HIGH));
		}
		else if(aTrump == null)
		{
			return aMemoryList.contains(new Card(Joker.HIGH)) || 
					aMemoryList.contains(new Card(Joker.LOW));
		}
		//If the partner could trump this card.
		return !aMemoryList.partnerIsVoid(aTrump);
	}
	
	/** Attempts to lead with a high card in the non-trump suit. The card selected will
	 * be the highest in its suit or will have a high chance of winning the trick.
	 * 
	 * @param pHand The hand of cards.
	 */
	private void nonTrumpHighLead(Hand pHand)
	{
		if(aBestLeads.size()==0)
		{
			for(Card card : pHand.canLead(aTrump == null))
			{
				if(isHighestInSuit(card) && card.getEffectiveSuit(aTrump) != aTrump)
				{
					if(!opponentCouldTrump(card.getEffectiveSuit(aTrump)))
					{
						//Automatic trick win since you are playing the highest card left in
						//its suit and neither opponent can trump you.
						if((aMemoryList.partnerIsVoid(card.getEffectiveSuit(aTrump)) && 
								aMemoryList.getCards(card.getEffectiveSuit(aTrump), null).size() >= 3) ||
								((!aMemoryList.partnerIsVoid(card.getEffectiveSuit(aTrump)) &&
								aMemoryList.getCards(card.getEffectiveSuit(aTrump), null).size() >= 3+2)))
						{
							aBestLeads.add(card);
						}
					}
				}
			}
		}
		if(aBestLeads.size()==0)
		{
			for(Card card : pHand.canLead(aTrump == null))
			{
				if(isHighestInSuit(card) && card.getEffectiveSuit(aTrump) != aTrump)
				{
					//There is a 7/8 minimum chance that this card will result in a trick win
					//if the partner is void in the suit and at least 3 other cards in the
					//suit are distributed in the opponents' hands.
					if((aMemoryList.partnerIsVoid(card.getEffectiveSuit(aTrump)) && 
							aMemoryList.getCards(card.getEffectiveSuit(aTrump), null).size() >= 3) ||
							((!aMemoryList.partnerIsVoid(card.getEffectiveSuit(aTrump)) &&
							aMemoryList.getCards(card.getEffectiveSuit(aTrump), null).size() >= 3+2)))
					{
						aBestLeads.add(card);
					}
				}
			}
		}
		if(aBestLeads.size()==0)
		{
			for(Card card : pHand.canLead(aTrump == null))
			{
				if(isHighestInSuit(card) && card.getEffectiveSuit(aTrump) != aTrump)
				{
					//Otherwise, lead with a card if it's the highest of its suit.
					aBestLeads.add(card);
				}
			}
		}
	}
	
	/** Attempts to lead with a high card in the trump suit. The card selected will
	 * be the highest in its suit or will have a high chance of winning the trick.
	 * 
	 * @param pHand The hand of cards.
	 */
	private void trumpHighLead(Hand pHand)
	{
		//Highest trump cards
		if(aBestLeads.size()==0)
		{
			for(Card card : pHand.canLead(aTrump == null))
			{
				if(isHighestInSuit(card) && card.getEffectiveSuit(aTrump) == aTrump)
				{
					aBestLeads.add(card);
				}
			}
		}
		//High trump cards
		if(aBestLeads.size()==0)
		{
			for(Card card : pHand.canLead(aTrump == null))
			{
				if(aMemoryList.getCards(card.getEffectiveSuit(aTrump), card).size() < 2 && 
						card.getEffectiveSuit(aTrump) == aTrump)
				{
					aBestLeads.add(card);
				}
			}
		}
	}
	
	/**
	 * If you have no good leads, lead with a card that might be a favorable lead for your partner.
	 * @param pHand The hand of cards of this advanced player.
	 */
	private void bankOnPartner(Hand pHand)
	{
		//If a partner is void in a suit, it is advantageous to lead with that suit
		//if you have no high cards in it so that they can trump it or throw away
		//a weak card. It is also advantageous to play in a suit in which you know neither
		//opponent can trump you.
		if(aBestLeads.size()==0)
		{
			for (int i = 0; i < 4; i++)
			{
				//Opponent won't be able to trump you but your partner could.
				if(!opponentCouldTrump(Suit.values()[i]) &&
					partnerCouldTrump(Suit.values()[i]))
				{
					aBestLeads.add(pHand.playableCards(Suit.values()[i], aTrump).getLast());
				}
			}
		}
		if(aBestLeads.size()==0)
		{
			for (int i = 0; i < 4; i++)
			{
				//Opponent won't be able to trump you but neither could your partner.
				if(!opponentCouldTrump(Suit.values()[i]) &&
						!partnerCouldTrump(Suit.values()[i]))
				{
					//If your partner is not void in the suit.
					if(!aMemoryList.partnerIsVoid(Suit.values()[i]))
					{
						//If the partner bid this suit, they most likely have high cards
						//in it so leading in this suit would be a better play.
						if(!aBids[(aBidIndex + 2) % 4].isPass() && 
								aBids[(aBidIndex + 2) % 4].getSuit() == Suit.values()[i])
						{
							aBestLeads.add(pHand.playableCards(Suit.values()[i], aTrump).getLast());
						}
					}
				}
			}
		}
		if(aBestLeads.size()==0)
		{
			for (int i = 0; i < 4; i++)
			{
				//Opponent might be able to trump you but so could your partner.
				if(opponentCouldTrump(Suit.values()[i]) &&
						partnerCouldTrump(Suit.values()[i]))
				{
					//If your partner is void in the suit.
					if(aMemoryList.partnerIsVoid(Suit.values()[i]))
					{
						//If the partner bid this suit, they most likely have high cards
						//in it so leading in this suit would be a better play.
						if(!aBids[(aBidIndex + 2) % 4].isPass() && 
								aBids[(aBidIndex + 2) % 4].getSuit() == Suit.values()[i])
						{
							aBestLeads.add(pHand.playableCards(Suit.values()[i], aTrump).getLast());
						}
					}
				}
			}
		}
	}
	
	/**
	 * If there is only one card that is a legal leading card, it must be played.
	 * 
	 * @param pHand The hand of cards.
	 */
	private void noChoice(Hand pHand)
	{
		if(pHand.canLead(aTrump == null).size() == 1)
		{
			aBestLeads.add(pHand.canLead(aTrump == null).getFirst());
		}
	}
}
