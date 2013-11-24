package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import comp303.fivehundred.gui.external.CardPanel;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardImages;
import comp303.fivehundred.util.CardList;

/**
 * Sets the main stage for the game of 500. Uses components between the GUIListener and the FHGUI classes to make the
 * game playable as either a human or as different levels of AI.
 * 
 * @author Maxim Gorshkov, Dashiel Siegel, James McCorriston, Andrew Borodovski
 */
public class TablePlay extends JPanel
{
	// Sets the generated serialID
	private static final long serialVersionUID = -4841022761731955663L;
	// Sets the constant for the overlay of cards when hands are shown
	private static final int OVERLAY = 30;
	// Initializes the human action buttons.
	private JButton aBidButton = new JButton("Bid");
	private JButton aPassButton = new JButton("Pass");
	private JButton aExchangeButton = new JButton("Exchange");
	private JButton aPlayButton = new JButton("Play");
	private JButton aNextTrickButton = new JButton("Next Trick");
	private JButton aStartRoundButton = new JButton("Start Round");
	private JButton aEndRoundButton = new JButton("End Round");
	private JButton aNextRoundButton = new JButton("Next Round");
	private JButton aEndGameButton = new JButton("End Game");
	private JButton aAutoPlayButton = new JButton("Auto Play");
	private JButton aAutoBidButton = new JButton("Auto Bid");
	private JButton aAutoExchangeButton = new JButton("Auto Exchange");

	private String[] aHumanBidOptions = { "6", "7", "8", "9", "10" };
	private String[] aHumanSuitOptions = { "Spades", "Clubs", "Diamonds", "Hearts", "No Trump" };
	private JComboBox aTrickSelect = new JComboBox(aHumanBidOptions);
	private JComboBox aSuitSelect = new JComboBox(aHumanSuitOptions);

	// Initializes the spots on the table where cards played will be displayed.
	private JPanel aSouthSpot;
	private JPanel aWestSpot;
	private JPanel aNorthSpot;
	private JPanel aEastSpot;

	// The south player's CardPanel.
	private CardPanel aSouthCardPanel;
	private CardPanel aWestCardPanel;
	private CardPanel aNorthCardPanel;
	private CardPanel aEastCardPanel;

	// Sets which players have the dealer chip
	private JLabel aPlayer0Dealer = new JLabel(new ImageIcon("src/images/dealer.gif"));
	private JLabel aPlayer1Dealer = new JLabel(new ImageIcon("src/images/dealer.gif"));
	private JLabel aPlayer2Dealer = new JLabel(new ImageIcon("src/images/dealer.gif"));
	private JLabel aPlayer3Dealer = new JLabel(new ImageIcon("src/images/dealer.gif"));
	
	// Sets which players have the leader chip
	private JLabel aPlayer0Leader = new JLabel(new ImageIcon("src/images/leader.gif"));
	private JLabel aPlayer1Leader = new JLabel(new ImageIcon("src/images/leader.gif"));
	private JLabel aPlayer2Leader = new JLabel(new ImageIcon("src/images/leader.gif"));
	private JLabel aPlayer3Leader = new JLabel(new ImageIcon("src/images/leader.gif"));
	
	// Sets which players have the contract holder chip
	private JLabel aPlayer0ContractHolder = new JLabel(new ImageIcon("src/images/contract.gif"));
	private JLabel aPlayer1ContractHolder = new JLabel(new ImageIcon("src/images/contract.gif"));
	private JLabel aPlayer2ContractHolder = new JLabel(new ImageIcon("src/images/contract.gif"));
	private JLabel aPlayer3ContractHolder = new JLabel(new ImageIcon("src/images/contract.gif"));
	
	// Sets the contract information label
	private JLabel aContractInfo = new JLabel(CardImages.getAllPass());
	private JLabel aContractInfo2 = new JLabel(CardImages.getContract());
	private JLabel aPassLabel = new JLabel(CardImages.getAllPass());
	
	// Fills the center of the table with some text.
	private	JPanel aCenter = new JPanel();
	
	// The text displaying the winner of the game.
	private JLabel aWinnerLabel = new JLabel("");

	/**
	 * Sets up the main middle stage for the game. This will hold all of the cards which get played, discarded, and
	 * exchanged over the course of the game.
	 * 
	 * @param pListener
	 *            The GUIListener. Gets added as an action listener to all buttons.
	 */
	public TablePlay(GUIListener pListener)
	{
		// Carries over the layout from FHGUI
		super(new BorderLayout());

		// Sets the color of the table
		Color bg = Color.green;

		// Fills the panel for player 1.
		aSouthSpot = new JPanel();
		aSouthSpot.setBackground(bg);
		
		JPanel southTable = new JPanel();
		southTable.setBackground(bg);
		southTable.add(aSouthSpot, BorderLayout.NORTH);
		
		aPlayer0Dealer.setVisible(false);
		southTable.add(aPlayer0Dealer, BorderLayout.SOUTH);
		aPlayer0Leader.setVisible(false);
		southTable.add(aPlayer0Leader, BorderLayout.SOUTH);
		aPlayer0ContractHolder.setVisible(false);
		southTable.add(aPlayer0ContractHolder, BorderLayout.SOUTH);

		// Fills the panel for player 2.
		aWestSpot = new JPanel();
		aWestSpot.setBackground(bg);
		
		JPanel westTable = new JPanel();
		westTable.setBackground(bg);
		westTable.add(aWestSpot, BorderLayout.EAST);

		aPlayer1Dealer.setVisible(false);
		westTable.add(aPlayer1Dealer, BorderLayout.WEST);
		aPlayer1Leader.setVisible(false);
		westTable.add(aPlayer1Leader, BorderLayout.WEST);
		aPlayer1ContractHolder.setVisible(false);
		westTable.add(aPlayer1ContractHolder, BorderLayout.WEST);

		// Fills the panel for player 3.
		aNorthSpot = new JPanel();
		aNorthSpot.setBackground(bg);
		
		JPanel northTable = new JPanel();
		northTable.setBackground(bg);
		northTable.add(aNorthSpot, BorderLayout.NORTH);

		aPlayer2Dealer.setVisible(false);
		northTable.add(aPlayer2Dealer, BorderLayout.NORTH);
		aPlayer2Leader.setVisible(false);
		northTable.add(aPlayer2Leader, BorderLayout.NORTH);
		aPlayer2ContractHolder.setVisible(false);
		northTable.add(aPlayer2ContractHolder, BorderLayout.NORTH);
	
		// Fills the panel for player 4.
		aEastSpot = new JPanel();
		aEastSpot.setBackground(bg);
	
		JPanel eastTable = new JPanel();
		eastTable.setBackground(bg);
		eastTable.add(aEastSpot, BorderLayout.WEST);

		aPlayer3Dealer.setVisible(false);
		eastTable.add(aPlayer3Dealer, BorderLayout.EAST);
		aPlayer3Leader.setVisible(false);
		eastTable.add(aPlayer3Leader, BorderLayout.EAST);
		aPlayer3ContractHolder.setVisible(false);
		eastTable.add(aPlayer3ContractHolder, BorderLayout.EAST);

		aBidButton.setVisible(false);
		aBidButton.setActionCommand("Bid");
		aBidButton.addActionListener(pListener);

		aPassButton.setVisible(false);
		aPassButton.setActionCommand("Pass");
		aPassButton.addActionListener(pListener);

		aExchangeButton.setVisible(false);
		aExchangeButton.setActionCommand("Exchange");
		aExchangeButton.addActionListener(pListener);

		aPlayButton.setVisible(false);
		aPlayButton.setActionCommand("Play");
		aPlayButton.addActionListener(pListener);

		aNextRoundButton.setVisible(false);
		aNextRoundButton.setActionCommand("Next Round");
		aNextRoundButton.addActionListener(pListener);

		aNextTrickButton.setVisible(false);
		aNextTrickButton.setActionCommand("Next Trick");
		aNextTrickButton.addActionListener(pListener);

		aStartRoundButton.setVisible(false);
		aStartRoundButton.setActionCommand("Start Round");
		aStartRoundButton.addActionListener(pListener);
		
		aEndRoundButton.setVisible(false);
		aEndRoundButton.setActionCommand("End Round");
		aEndRoundButton.addActionListener(pListener);
		
		aEndGameButton.setVisible(false);
		aEndGameButton.setActionCommand("End Game");
		aEndGameButton.addActionListener(pListener);
		
		aAutoPlayButton.setVisible(false);
		aAutoPlayButton.setActionCommand("Auto Play");
		aAutoPlayButton.addActionListener(pListener);
		
		aAutoBidButton.setVisible(false);
		aAutoBidButton.setActionCommand("Auto Bid");
		aAutoBidButton.addActionListener(pListener);
		
		aAutoExchangeButton.setVisible(false);
		aAutoExchangeButton.setActionCommand("Auto Exchange");
		aAutoExchangeButton.addActionListener(pListener);

		aCenter.add(aExchangeButton, BorderLayout.CENTER);
		aCenter.add(aTrickSelect);
		aCenter.add(aSuitSelect);
		aCenter.add(aBidButton);
		aCenter.add(aPassButton);
		aCenter.add(aPlayButton, BorderLayout.CENTER);
		aCenter.add(aNextTrickButton, BorderLayout.CENTER);
		aCenter.add(aStartRoundButton, BorderLayout.CENTER);
		aCenter.add(aNextRoundButton, BorderLayout.CENTER);
		aCenter.add(aEndRoundButton, BorderLayout.CENTER);
		aCenter.add(aEndGameButton, BorderLayout.CENTER);
		aCenter.add(aAutoPlayButton, BorderLayout.SOUTH);
		aCenter.add(aAutoBidButton, BorderLayout.SOUTH);
		aCenter.add(aAutoExchangeButton, BorderLayout.SOUTH);
		aCenter.add(aContractInfo);
		aCenter.add(aContractInfo2);
		aCenter.add(aWinnerLabel, BorderLayout.CENTER);
		aCenter.setBackground(bg);

		// Adds the boxes and the center table to the actual table.
		add(southTable, BorderLayout.SOUTH);
		add(westTable, BorderLayout.WEST);
		add(northTable, BorderLayout.NORTH);
		add(eastTable, BorderLayout.EAST);
		add(aCenter, BorderLayout.CENTER);
	}

	/**
	 * Sets the hand of player 1 (found on the south-most side of the table).
	 * 
	 * @param pSouthHand
	 *            - container for the cardPanel which holds the cards that can be played
	 */
	public void setSouth(JPanel pSouthHand)
	{

		aSouthCardPanel = new CardPanel(true, OVERLAY, 0, 0);
		aSouthCardPanel.setVisible(true);
		aSouthCardPanel.setBackground(Color.RED);
		pSouthHand.add(aSouthCardPanel);
	}

	/**
	 * Sets the hand of player 3 (found on the north-most side of the table).
	 * 
	 * @param pNorthHand
	 *            - container for the cardPanel which holds the cards that can be played
	 */
	public void setNorth(JPanel pNorthHand)
	{
		aNorthCardPanel = new CardPanel(false, OVERLAY, 0, 2);
		aNorthCardPanel.setBackground(Color.RED);
		pNorthHand.add(aNorthCardPanel);
	}

	/**
	 * Sets the hand of player 2 (found on the west-most side of the table).
	 * 
	 * @param pWestHand
	 *            - container for the cardPanel which holds the cards that can be played
	 */
	public void setWest(JPanel pWestHand)
	{
		aWestCardPanel = new CardPanel(false, 0, OVERLAY, 1);
		aWestCardPanel.setBackground(Color.BLUE);
		pWestHand.add(aWestCardPanel);
	}

	/**
	 * Sets the hand of player 4 (found on the east-most side of the table).
	 * 
	 * @param pEastHand
	 *            - container for the cardPanel which holds the cards that can be played
	 */
	public void setEast(JPanel pEastHand)
	{
		aEastCardPanel = new CardPanel(false, 0, OVERLAY, 3);
		aEastCardPanel.setBackground(Color.BLUE);
		pEastHand.add(aEastCardPanel);
	}

	/**
	 *  Setter method for specified button visibility.
	 *  
	 *  @param pButton Specifies which button to modify visibility for.
	 *  @param pFlag The new parameter for the button visibility.
	 */
	public void setButtonVisibility(String pButton, boolean pFlag)
	{
		if(pButton.equals("Bid") || pButton.equals("Pass"))
		{
			aBidButton.setVisible(pFlag);
			aSuitSelect.setVisible(pFlag);
			aTrickSelect.setVisible(pFlag);
			aAutoBidButton.setVisible(pFlag);
			aPassButton.setVisible(pFlag);
		}
		else if(pButton.equals("Exchange"))
		{
			aExchangeButton.setVisible(pFlag);
			aAutoExchangeButton.setVisible(pFlag);
		}
		else if(pButton.equals("Play"))
		{
			aPlayButton.setVisible(pFlag);
			aAutoPlayButton.setVisible(pFlag);
		}
		else if(pButton.equals("Next Trick"))
		{
			aNextTrickButton.setVisible(pFlag);
		}
		else if(pButton.equals("Next Round"))
		{
			aNextRoundButton.setVisible(pFlag);
		}
		else if(pButton.equals("Start Round"))
		{
			aStartRoundButton.setVisible(pFlag);
		}
		else if(pButton.equals("End Round"))
		{
			aEndRoundButton.setVisible(pFlag);
		}
		else if(pButton.equals("End Game"))
		{
			aEndGameButton.setVisible(pFlag);
		}
	}
	
	/**
	 * Sets all buttons to be invisible.
	 */
	public void clearButtons()
	{
		aBidButton.setVisible(false);
		aSuitSelect.setVisible(false);
		aTrickSelect.setVisible(false);
		aPassButton.setVisible(false);
		aExchangeButton.setVisible(false);
		aPlayButton.setVisible(false);
		aNextTrickButton.setVisible(false);
		aNextRoundButton.setVisible(false);
		aEndRoundButton.setVisible(false);
		aStartRoundButton.setVisible(false);
		aAutoPlayButton.setVisible(false);
		aAutoBidButton.setVisible(false);
		aAutoExchangeButton.setVisible(false);
		aWinnerLabel.setVisible(false);
	}

	/**
	 * Getter method for aHumanBid.
	 * 
	 * @return The value of Bid().
	 */
	public Bid getHumanBid()
	{
		// Defines a suit that the human player can have
		Suit currentHumanSuit;

		// Depending on the input from user, selects the right suit.
		switch (getHumanSuit())
		{

		case 0:
			currentHumanSuit = Suit.SPADES;
			break;
		case 1:
			currentHumanSuit = Suit.CLUBS;
			break;

		case 2:
			currentHumanSuit = Suit.DIAMONDS;
			break;

		case 3:
			currentHumanSuit = Suit.HEARTS;
			break;

		default:
			currentHumanSuit = null;
			break;
		}

		// Combines the value from tricks with the offset with the suit chosen.
		return new Bid(getHumanTricks(), currentHumanSuit);
	}

	/**
	 * Sets the phase of the game in the CardPanel.
	 * 
	 * @param pGameStage
	 *            = 0 if bidding, 1 if exchanging, 2 if playing.
	 */
	public void setGameStage(int pGameStage)
	{
		aSouthCardPanel.setGameStage(pGameStage);
	}

	/**
	 * Initializes the list of cards in CardPanel to pCards.
	 * 
	 * @param pCards
	 *            = The list of cards to be set in CardPanel.
	 */
	public void setCardPanel(CardList pCards)
	{
		aSouthCardPanel.newHand(pCards);

	}
	
	/**
	 * Gets the card panels.
	 * @return An array list containing the 4 card panels.
	 */
	public ArrayList<CardPanel> getCardPanels()
	{
		return new ArrayList<CardPanel>(Arrays.asList(aSouthCardPanel, aWestCardPanel, aNorthCardPanel, aEastCardPanel));
	}

	/**
	 * Sets the trump suit of the CardPanel for sorting purposes.
	 * 
	 * @param pTrump
	 *            The current trump suit.
	 */
	public void setHandTrump(Suit pTrump)
	{
		aSouthCardPanel.setTrumpSuit(pTrump);
	}

	/**
	 * Discards the given card from the south player's hand of cards.
	 * 
	 * @param pCard
	 *            The card to discard.
	 */
	public void discardFromPanel(Card pCard)
	{
		aSouthCardPanel.discard(pCard);
	}

	/**
	 * Gets the number of selected cards.
	 * 
	 * @return The number of cards currently selected.
	 */
	public int numSelected()
	{
		return aSouthCardPanel.numUp();
	}

	/**
	 * Returns the number of tricks human player bids. Adds an offset from the value in the array to the actual value.
	 * 
	 * @return aHumanTricks.
	 */
	public int getHumanTricks()
	{
		return aTrickSelect.getSelectedIndex() + 6;
	}

	/**
	 * Returns the suit that human player bids.
	 * 
	 * @return aHumanSuit.
	 */
	public int getHumanSuit()
	{
		return aSuitSelect.getSelectedIndex();
	}

	/**
	 * Sets the player names using the GUIListener.
	 * 
	 * @param pPlayerNames
	 *            - passes the player names.
	 */
	public void updatePlayerNames(String[] pPlayerNames)
	{
		assert pPlayerNames.length == 8;
		aSouthCardPanel.setTitle("(" + pPlayerNames[4] + ") " + pPlayerNames[0]);
		aWestCardPanel.setTitle("(" + pPlayerNames[5] + ") " + pPlayerNames[1]);
		aNorthCardPanel.setTitle("(" + pPlayerNames[6] + ") " + pPlayerNames[2]);
		aEastCardPanel.setTitle("(" + pPlayerNames[7] + ") " + pPlayerNames[3]);
	}

	/**
	 * Shows the dealer chip for the player who is currently the dealer.
	 * 
	 * @param pPlayerIndex
	 *            - takes in the player index.
	 */
	public void setDealerChip(int pPlayerIndex)
	{
		switch (pPlayerIndex)
		{
		case 0:
			aPlayer0Dealer.setVisible(true);
			aPlayer1Dealer.setVisible(false);
			aPlayer2Dealer.setVisible(false);
			aPlayer3Dealer.setVisible(false);
			break;
		case 1:
			aPlayer0Dealer.setVisible(false);
			aPlayer1Dealer.setVisible(true);
			aPlayer2Dealer.setVisible(false);
			aPlayer3Dealer.setVisible(false);
			break;
		case 2:
			aPlayer0Dealer.setVisible(false);
			aPlayer1Dealer.setVisible(false);
			aPlayer2Dealer.setVisible(true);
			aPlayer3Dealer.setVisible(false);
			break;
		case 3:
			aPlayer0Dealer.setVisible(false);
			aPlayer1Dealer.setVisible(false);
			aPlayer2Dealer.setVisible(false);
			aPlayer3Dealer.setVisible(true);
			break;
		default:
			break;
		}
	}

	/**
	 * Shows the leader chip for the player who is the contract holder.
	 * 
	 * @param pPlayerIndex
	 *            - takes in the player index.
	 */
	public void setContractHolderChip(int pPlayerIndex)
	{
		switch (pPlayerIndex)
		{
		case 0:
			aPlayer0ContractHolder.setVisible(true);
			aPlayer1ContractHolder.setVisible(false);
			aPlayer2ContractHolder.setVisible(false);
			aPlayer3ContractHolder.setVisible(false);
			break;
		case 1:
			aPlayer0ContractHolder.setVisible(false);
			aPlayer1ContractHolder.setVisible(true);
			aPlayer2ContractHolder.setVisible(false);
			aPlayer3ContractHolder.setVisible(false);
			break;
		case 2:
			aPlayer0ContractHolder.setVisible(false);
			aPlayer1ContractHolder.setVisible(false);
			aPlayer2ContractHolder.setVisible(true);
			aPlayer3ContractHolder.setVisible(false);
			break;
		case 3:
			aPlayer0ContractHolder.setVisible(false);
			aPlayer1ContractHolder.setVisible(false);
			aPlayer2ContractHolder.setVisible(false);
			aPlayer3ContractHolder.setVisible(true);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Shows the leader chip for the player who is currently the dealer.
	 * 
	 * @param pPlayerIndex
	 *            - takes in the player index.
	 */
	public void setLeaderChip(int pLeaderIndex)
	{
		switch (pLeaderIndex)
		{
		case 0:
			aPlayer0Leader.setVisible(true);
			aPlayer1Leader.setVisible(false);
			aPlayer2Leader.setVisible(false);
			aPlayer3Leader.setVisible(false);
			break;
		case 1:
			aPlayer0Leader.setVisible(false);
			aPlayer1Leader.setVisible(true);
			aPlayer2Leader.setVisible(false);
			aPlayer3Leader.setVisible(false);
			break;
		case 2:
			aPlayer0Leader.setVisible(false);
			aPlayer1Leader.setVisible(false);
			aPlayer2Leader.setVisible(true);
			aPlayer3Leader.setVisible(false);
			break;
		case 3:
			aPlayer0Leader.setVisible(false);
			aPlayer1Leader.setVisible(false);
			aPlayer2Leader.setVisible(false);
			aPlayer3Leader.setVisible(true);
			break;
		default:
			break;
		}
	}

	/**
	 * Gets the cards that are selected.
	 * 
	 * @return A CardList storing the cards that are selected.
	 */
	public CardList selectedCards()
	{
		return aSouthCardPanel.getSelectedCards();
	}

	/**
	 * Sets the spot on the table at pIndex to pCard or makes it blank if pCard is null.
	 * 
	 * @param pIndex
	 *            The spot index to modify.
	 * @param pCard
	 *            The card to set in the spot, leaves the spot blank if pCard is null.
	 */
	public void setSpot(int pIndex, Object pObject)
	{		
		if (pObject == null)
		{
			switch (pIndex)
			{
			case 0:
				aSouthSpot.removeAll();
				aSouthSpot.add(new JLabel(CardImages.getGreen()));
				break;
			case 1:
				aWestSpot.removeAll();
				aWestSpot.add(new JLabel(CardImages.getGreen()));
				break;
			case 2:
				aNorthSpot.removeAll();
				aNorthSpot.add(new JLabel(CardImages.getGreen()));
				break;
			case 3:
				aEastSpot.removeAll();
				aEastSpot.add(new JLabel(CardImages.getGreen()));
				break;
			}
		}
		else
		{
			assert (pObject instanceof Card) || (pObject instanceof Bid);
			if(pObject instanceof Card)
			{
				switch (pIndex)
				{
				case 0:
					aSouthSpot.removeAll();
					aSouthSpot.add(new JLabel(CardImages.getCard(((Card)pObject))));
					break;
				case 1:
					aWestSpot.removeAll();
					aWestSpot.add(new JLabel(CardImages.getCard(((Card)pObject))));
					break;
				case 2:
					aNorthSpot.removeAll();
					aNorthSpot.add(new JLabel(CardImages.getCard(((Card)pObject))));
					break;
				case 3:
					aEastSpot.removeAll();
					aEastSpot.add(new JLabel(CardImages.getCard(((Card)pObject))));
					
					break;
				}
			}
			else
			{
				switch (pIndex)
				{
				case 0:
					aSouthSpot.removeAll();
					aSouthSpot.add(new JLabel(CardImages.getBid(((Bid)pObject))));
					break;
				case 1:
					aWestSpot.removeAll();
					aWestSpot.add(new JLabel(CardImages.getBid(((Bid)pObject))));
					break;
				case 2:
					aNorthSpot.removeAll();
					aNorthSpot.add(new JLabel(CardImages.getBid(((Bid)pObject))));
					break;
				case 3:
					aEastSpot.removeAll();
					aEastSpot.add(new JLabel(CardImages.getBid(((Bid)pObject))));					
					break;
				}	
			}
		}
	}
	
	/**
	 * Removes all cards from the table.
	 */
	public void clearTable()
	{
		//Removes the card visuals from the table.
		for(int i=0; i<4; i++)
		{
			setSpot(i, null);
		}
		aWinnerLabel.setVisible(false);
	}
	
	/**
	 * Sets the images of ContactInfo. Clear contract info before setting it!
	 * @param pBid The winning bid
	 */
	public void setContractInfo(Bid pBid)
	{
		aContractInfo = new JLabel(CardImages.getTricks(pBid.getTricksBid()));
		aContractInfo2 = new JLabel(CardImages.getSuit(pBid.getSuit()));
		aCenter.add(aContractInfo);
		aCenter.add(aContractInfo2);
		
	}
	
	/**
	 * Clears the ContractInfo.
	 */
	public void clearContractInfo()
	{
		aCenter.remove(aContractInfo);
		aCenter.remove(aContractInfo2);
		aCenter.getParent().setSize(700, 400);
	}
	
	/**
	 * Sets the label saying that all players passed.
	 */
	public void setPassLabel()
	{
		aCenter.add(aPassLabel);
	}
	
	/**
	 * Clears the label saying that all players passed.
	 */
	public void clearPassLabel()
	{
		aCenter.remove(aPassLabel);
		aCenter.getParent().setSize(700, 400);
	}

	/**
	 * Updates the label to display the current winner and makes him visible. 
	 * @param pPlayerWon
	 * 		True if the human won, false otherwise.
	 */
	public void updateWinnerAndShow(boolean pPlayerWon)
	{
		aWinnerLabel.setVisible(true);
		
		if(pPlayerWon)
		{
			aWinnerLabel.setText("YOU WIN!");
		}
		else
		{
			aWinnerLabel.setText("SORRY, YOU LOSE!");
		}
	}
	
	/**
	 * Toggles whether or not all computer player hands can be seen.
	 * @param pVisible True if the hands will become visible, false if they will become hidden.
	 */
	public void toggleOpenHandMode(boolean pVisible)
	{
		aNorthCardPanel.setHandVisible(pVisible);
		aEastCardPanel.setHandVisible(pVisible);
		aWestCardPanel.setHandVisible(pVisible);
	}
}
