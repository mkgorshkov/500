package comp303.fivehundred.gui.external;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardImages;
import comp303.fivehundred.util.CardList;

/**
 * @author This class was obtained from Prof. Martin Robillard 7 November 2012
 * Manages a CardList and supports selection of an individual card.
 * 
 * Modified by James McCorriston
 */
@SuppressWarnings("serial")
public class CardPanel extends JPanel implements GameObserver
{
	private static final int INSET = 10;
	// Indicates whether the cards should be visible to the player, for example,
	// should not be visible for other AI.
	private boolean aVisible = false;
	// Indicates whether the hand being show is in horizontal or vertical orientation
	private boolean aHorizontal = false;
	//Indicates the phase of the game: 0 if bidding, 1 if exchanging, 2 if playing.
	private int aGameStage = 0;
	//Stores the trump suit for sorting purposes.
	private Suit aTrump = null;
	//The index of the player whose hand this is.
	private int aIndex;
	
	// Read-only: not synchronized with the GameEngine
	private HashMap<JLabel, Card> aCards = new HashMap<JLabel, Card>();
	
	/**
	 * Creates the border with player label and sets the inset of the overlap layout.
	 * @param pIndex The player index whose hand this represents.
	 */
	public CardPanel(boolean pVisibleCards, int pHor, int pVer, int pIndex)
	{
		// Sets the overlap of the cards, whether the overlay is towards the bottom or towards the side.
		super(new OverlapLayout(new Point(pHor, pVer)));	
		aVisible = pVisibleCards;
		aIndex = pIndex;
		
		// If the horizontal size is greater than vertical size, it's in the horizontal
		// orientation.
		if(pHor>pVer){
			aHorizontal = true;
		}
		
		if(aVisible){
			Insets ins = new Insets(INSET, 0, 0, 0);
			((OverlapLayout)getLayout()).setPopupInsets(ins);
		}
	}
	
	public void setTitle(String title)
	{
		TitledBorder border = new TitledBorder(title);
		border.setTitleColor(Color.WHITE);
		setBorder(border);
	}
	
	private void initialize(CardList pCards)
	{
		CardList cards;
		if(aTrump == null)
		{
			cards = pCards.sort(new Card.BySuitNoTrumpComparator());
		}
		else
		{
			Card.BySuitComparator suitComparator = new Card.BySuitComparator();
			suitComparator.setTrump(aTrump);
			cards = pCards.sort(suitComparator);
		}
		aCards.clear();
		removeAll();
		for( Card card : cards )
		{
			JLabel lLabel;
			// If the player is able to see the cards, the label shows it
			if(aVisible)
			{
			lLabel = new JLabel( CardImages.getCard(card));
			}
			// Otherwise shows the back of the cards
			else
			{
				if(aHorizontal){
				lLabel = new JLabel(new ImageIcon("src/images/b.gif"));
				}
				else{
				lLabel = new JLabel(new ImageIcon("src/images/bleft.gif"));
				}
			}
			aCards.put(lLabel, card);
			lLabel.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent pEvent)
				{
				    Component c = pEvent.getComponent();
				    Boolean constraint = ((OverlapLayout)getLayout()).getConstraints(c);

				    if (constraint == null || constraint == OverlapLayout.POP_DOWN)
				    {
				    	//Cards cannot be selected in the bidding phase.
				    	if(!(aGameStage == 0))
				    	{
				    		if(aGameStage == 1)
				    		{
				    			//This allows for up to 6 cards to be selected (popped up) during the
				    			//exchange phase.
						    	if(numUp() <= 5)
						    	{
							    	((OverlapLayout)getLayout()).addLayoutComponent(c, OverlapLayout.POP_UP);
						    	}
				    		}
				    		//Only one card can be selected at a time when choosing a card to play.
				    		else
				    		{
				    			//aGUIListener.actionPerformed(new ActionEvent(aCards.get(c), ActionEvent.ACTION_PERFORMED, "Human Play"));
				    			popAllDown();
						    	((OverlapLayout)getLayout()).addLayoutComponent(c, OverlapLayout.POP_UP);
				    		}
				    	}
				    }
				    else
				    {
				    	((OverlapLayout)getLayout()).addLayoutComponent(c, OverlapLayout.POP_DOWN);
				    }
				    c.getParent().invalidate();
				    c.getParent().validate();
				}
			});
			add(lLabel);
		}
		validate();
		repaint();
	}
	
	private void popAllDown()
	{
		Component[] lChildren = getComponents();
		for( Component component : lChildren )
		{
			((OverlapLayout)getLayout()).addLayoutComponent(component, OverlapLayout.POP_DOWN);
		}
	}
	
	/**
	 * @return The card that is up. Null if none.
	 */
	public Card isUp()
	{
		for( Component component : getComponents() )
		{
			Boolean constraint = ((OverlapLayout)getLayout()).getConstraints(component);
			if (constraint != null && constraint == OverlapLayout.POP_UP)
			{
				return aCards.get(component);
			}
		}
		return null;
	}
	
	/**
	 * @return The number of cards that are up.
	 */
	public int numUp()
	{
		int quantity = 0;
		for( Component component : getComponents() )
		{
			Boolean constraint = ((OverlapLayout)getLayout()).getConstraints(component);
			if (constraint != null && constraint == OverlapLayout.POP_UP)
			{
				quantity++;
			}
		}
		return quantity;
	}
	
	/**
	 * Gets all cards that have been selected.
	 * @return A CardList storing all of the selected cards.
	 */
	public CardList getSelectedCards()
	{
		CardList lCards = new CardList();
		
		for( Component component : getComponents() )
		{
			Boolean constraint = ((OverlapLayout)getLayout()).getConstraints(component);
			if (constraint != null && constraint == OverlapLayout.POP_UP)
			{
				lCards.add(aCards.get(component));
			}
		}
		return lCards;
	}
	
	/**
	 * Sets the phase of the game.
	 * @param pGameStage = 0 if bidding, 1 if exchanging, 2 if playing.
	 */
	public void setGameStage(int pGameStage)
	{
		aGameStage = pGameStage;
	}
	
	/**
	 * Sets the trump suit so the hand can be sorted accordingly.
	 * @param pTrump = The current trump suit.
	 */
	public void setTrumpSuit(Suit pTrump)
	{
		aTrump = pTrump;
	}
	
	/**
	 * Sets the visibility of the hand in the CardPanel.
	 */
	public void setHandVisible(boolean pVisible)
	{
		aVisible = pVisible;
		//Re-initializes the card panel.
		CardList list = new CardList();
		for(Card card : aCards.values())
		{
			list.add(card);
		}
		
		initialize(list);
	}
	
	/**
	 * Sets the visibility of the hand in the CardPanel.
	 */
	public boolean isHandVisible()
	{
		return aVisible;
	}

	@Override
	public void newHand(CardList pNewHand)
	{
		initialize(pNewHand);
	}

	@Override
	public void discard(Card pCard)
	{
		JLabel lToRemove = null;
		for( JLabel label : aCards.keySet())
		{
			if( aCards.get(label).equals(pCard))
			{
				lToRemove = label;
				break;
			}
		}
		aCards.remove(lToRemove);
		removeAll();
		CardList lList = new CardList();
		for(Card card : aCards.values())
		{
			lList.add(card);
		}
		if(aTrump == null)
		{
			lList = lList.sort(new Card.BySuitNoTrumpComparator());
		}
		else
		{
			Card.BySuitComparator suitComparator = new Card.BySuitComparator();
			suitComparator.setTrump(aTrump);
			lList = lList.sort(suitComparator);
		}
		initialize(lList);
		validate();
		repaint();
	}
	
	public void update(Observable pObservable, Object pToUpdate)
	{
		assert pObservable instanceof GameEngine;
		assert pToUpdate instanceof Object[];
		assert ((Object[])pToUpdate)[0] instanceof String;
		
		if(((Object[])pToUpdate)[0] == "handsG")
		{
			if(((Object[])pToUpdate)[1] == null)
			{
				aTrump = null;
			}
			else
			{
				aTrump = ((GameEngine) pObservable).getTrumpSuit();
			}
			initialize(((GameEngine) pObservable).getHand(aIndex));
		}
	}
}
