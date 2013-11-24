package comp303.fivehundred.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.util.Card.Suit;

/**
 * A class to store and manage images of the 52 cards.
 */
public final class CardImages 
{
	private static final int MIN_TRICKS = 6;
	private static final String IMAGE_LOCATION = "images/";
	private static final String IMAGE_SUFFIX = ".gif";
	private static final String[] RANK_CODES = {"4", "5", "6", "7", "8", "9", "t", "j", "q", "k", "a" };
	private static final String[] SUIT_CODES = {"s", "c", "d", "h"};	
	private static final String[] JOKER_CODES = {"joker-low", "joker-high"};
	private static final String NO_TRUMP_CODE = "nt";
	private static final String PASS_CODE = "passbid";
	private static final String[] TRICKS = {"six", "seven", "eight", "nine", "ten"};
	private static final String ALL_PASSED = "allpass";
	private static final String CONTRACT = "contract2";
	private static final String GREEN = "green";
	
	private static Map<String, ImageIcon> aCards = new HashMap<String, ImageIcon>();
	
	private CardImages()
	{}
	
	/**
	 * Return the image of a card.
	 * @param pCard the target card
	 * @return An icon representing the chosen card.
	 */
	public static ImageIcon getCard( Card pCard )
	{
		return getCard( getCode( pCard ) );
	}
	
	/**
	 * Return the image representation of a bid.
	 * @param pBid the target bid.
	 * @return An icon representing the chosen bid.
	 */
	public static ImageIcon getBid( Bid pBid )
	{
		return getBid( getCode( pBid ) );
	}
	
	/**
	 * Return an image of the back of a card.
	 * @return An icon representing the back of a card.
	 */
	public static ImageIcon getBack()
	{
		return getCard( "b" );
	}
	
	/**
	 * Returns an image of the message "all passed".
	 * @return An icon representing the message "all passed".
	 */
	public static ImageIcon getAllPass()
	{
		return getCard( ALL_PASSED );
	}
	
	/**
	 * Returns an image of the word "contract".
	 * @return An icon representing the word "contract."
	 */
	public static ImageIcon getContract()
	{
		return getCard( CONTRACT );
	}
	
	/**
	 * Returns an image representing a suit.
	 * @param pSuit The target suit.
	 * @return An icon representing the chosen suit.
	 */
	public static ImageIcon getSuit(Suit pSuit)
	{
		return getCard( getSuitCode( pSuit ) );
	}
	
	/**
	 * Returns an image representing a number of tricks.
	 * @param pTricks The target number.
	 * @return An icon representing the chosen number of tricks.
	 */
	public static ImageIcon getTricks(int pTricks)
	{
		return getCard( getTrickCode( pTricks ) );
	}
	
	/**
	 * Returns an image of a green card.
	 * @return An icon representing a green card.
	 */
	public static ImageIcon getGreen()
	{
		return getCard( GREEN );
	}
	
	private static String getTrickCode(int pTricks)
	{
		return TRICKS[pTricks-MIN_TRICKS];
	}
	
	private static String getSuitCode( Suit pSuit)
	{
		if (pSuit == null)
		{
			return NO_TRUMP_CODE;
		}
		else
		{
			return SUIT_CODES[pSuit.ordinal()];
		}
	}
	
	private static String getCode( Card pCard )
	{
		if( pCard.isJoker() )
		{
			if( pCard.getJokerValue() == Card.Joker.LOW)
			{
				return JOKER_CODES[0];
			}
			else
			{
				return JOKER_CODES[1];
			}
		}
		else
		{
			return RANK_CODES[ pCard.getRank().ordinal() ] + SUIT_CODES[ pCard.getSuit().ordinal() ];		
		}
	}
	
	private static String getCode( Bid pBid )
	{
		if( pBid.isPass() )
		{
			return PASS_CODE;
		}
		else if( pBid.isNoTrump() )
		{
			return RANK_CODES[ pBid.getTricksBid() - 4 ] + NO_TRUMP_CODE + "bid";
		}
		else
		{
			return RANK_CODES[ pBid.getTricksBid() - 4 ] + SUIT_CODES[ pBid.getSuit().ordinal() ] + "bid";		
		}
	}
	
	private static ImageIcon getCard( String pCode )
	{
		ImageIcon lIcon = (ImageIcon) aCards.get( pCode );
		if( lIcon == null )
		{
			lIcon = new ImageIcon(CardImages.class.getClassLoader().getResource( IMAGE_LOCATION + pCode + IMAGE_SUFFIX ));
			aCards.put( pCode, lIcon );
		}
		return lIcon;
	}
	
	private static ImageIcon getBid( String pCode )
	{
		ImageIcon lIcon = (ImageIcon) aCards.get( pCode );
		if( lIcon == null )
		{
			lIcon = new ImageIcon(CardImages.class.getClassLoader().getResource( IMAGE_LOCATION + pCode + IMAGE_SUFFIX ));
			aCards.put( pCode, lIcon );
		}
		return lIcon;
	}
}
