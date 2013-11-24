package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.gui.external.CardPanel;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Creates and runs the GUI.
 * @author dashielms, Maxim Gorshkov, James McCorriston, Andrew Borodovski
 *
 */
public class FHGUI extends JFrame implements Observer
{	
	// The serial ID.
	private static final long serialVersionUID = -6102082204135854322L;

	// The GUIListener
	private GUIListener aGlobalListener = new GUIListener(this);
	
	// The Table and it's Cards.
	private JPanel aTable = new JPanel(new CardLayout());
	private TableSetup aTableSetup = new TableSetup(aGlobalListener);
	private TablePlay aTablePlay = new TablePlay(aGlobalListener);
	
	// The Menu Bar.
	private JMenuBar aMenuBar = new MenuBar(aGlobalListener);
	
	// The Status Bar.
	private StatusBar aLogLine = new StatusBar();
	private JLabel aScore = new JLabel("Score - Your Team: 0\t Opponent Team: 0");
	private JLabel aTricks = new JLabel("Tricks - Your Team: 0\t Opponent Team: 0");
	private int aTeam1Tricks = 0;
	private int aTeam2Tricks = 0;

	/**
	 * FHGUI constructor.
	 */
	public FHGUI()
	{
		//////////////////////////////////////////////////////
		// 	Sets up the core program resources and settings	//
		//////////////////////////////////////////////////////
		
		// Sets the program properties. Adjusts Title, Size, Starting Position, Close Type, and Visibility.
		setTitle("Five Hundred");
		setSize(700, 400);
		setLocation(0, 0);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		// Creates the core panel of the entire program. Everything will eventually be contained within this. 
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel content = new JPanel(new BorderLayout());
		JPanel statusHolder = new JPanel();
		statusHolder.setLayout(new BoxLayout(statusHolder, BoxLayout.X_AXIS));
		//////////////////////////////////////////////////////
		// 		Sets up the SOUTH panel, for Player 1		//
		//////////////////////////////////////////////////////
		
		// Creates the southmost panel, which contains the hand and icon containers for Player 1.
		Box south = Box.createHorizontalBox();
		JPanel southHand = new JPanel();
		
		// Sets size and color properties for the various panels.
		south.setPreferredSize(new Dimension(500, 150));
		
		// Positions the Hand and Icon panels.
		south.add(southHand);
		// Sets the actionlisteners to the south panel
		aTablePlay.setSouth(southHand);
		
		
		//////////////////////////////////////////////////////
		// 		Sets up the WEST panel, for Player 2		//
		//////////////////////////////////////////////////////
	
		// Creates the westmost panel, which contains the hand and icon containers for Player 2.
		Box west = Box.createVerticalBox();
		JPanel westHand = new JPanel();
		//JPanel westIco = new JPanel();
		
		// Sets size and color properties for the various panels.
		west.setPreferredSize(new Dimension(150, 500));
		
		// Positions the Hand and Icon panels.
		west.add(westHand);
		aTablePlay.setWest(westHand);

		
		//////////////////////////////////////////////////////
		// 		Sets up the NORTH panel, for Player 3		//
		//////////////////////////////////////////////////////
	
		// Creates the northmost panel, which contains the hand and icon containers for Player 3.
		Box north = Box.createHorizontalBox();
		JPanel northHand = new JPanel();
		
		// Sets size and color properties for the various panels.
		north.setPreferredSize(new Dimension(500, 150));
		
		// Positions the Hand and Icon panels.
		north.add(northHand);
		aTablePlay.setNorth(northHand);

		
		//////////////////////////////////////////////////////
		// 		Sets up the EAST panel, for Player 4		//
		//////////////////////////////////////////////////////
	
		// Creates the eastmost panel, which contains the hand and icon containers for Player 4.
		Box east = Box.createVerticalBox();
		JPanel eastHand = new JPanel();
		
		// Sets size and color properties for the various panels.
		east.setPreferredSize(new Dimension(150, 500));
		
		// Positions the Hand and Icon panels.
		east.add(eastHand);
		aTablePlay.setEast(eastHand);

		
		//////////////////////////////////////////////////////
		// 			Sets up the CENTER table				//
		//////////////////////////////////////////////////////
		
		// Adjusts the table border
		Border outline = BorderFactory.createLineBorder(Color.black);
		aTable.setBorder(outline);
		aTable.setPreferredSize(new Dimension(700, 400));
		// Adds the 2 modes to the central table.
		aTable.add(aTableSetup, "greencard");
		aTable.add(aTablePlay, "redempty");

		
		//////////////////////////////////////////////////////
		// 		Setup over. Adds items where they belong.	//
		//////////////////////////////////////////////////////
		
		// Now that we have created the 5 main containers, we add them all to the core panel "content"
		mainPanel.add(north, BorderLayout.NORTH);
		mainPanel.add(south, BorderLayout.SOUTH);
		mainPanel.add(east, BorderLayout.EAST);
		mainPanel.add(west, BorderLayout.WEST);
		mainPanel.add(aTable, BorderLayout.CENTER);
		//mainPanel.add(aScreen);		
		mainPanel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		
		// Adds the status bar to the bottom.
		JPanel status = new JPanel();
		status.setBackground(Color.LIGHT_GRAY);
		aLogLine.refresh();
		status.add(aLogLine);
		statusHolder.add(status, BorderLayout.WEST);
		statusHolder.add(aScore, BorderLayout.EAST);
		statusHolder.add(Box.createHorizontalStrut(25));
		statusHolder.add(aTricks, BorderLayout.CENTER);
		statusHolder.add(Box.createHorizontalStrut(25));
		aLogLine.setAlignmentX(LEFT_ALIGNMENT);
		statusHolder.setBackground(Color.LIGHT_GRAY);
		content.add(mainPanel, BorderLayout.CENTER);
		content.add(statusHolder, BorderLayout.NORTH);
		add(content);
		
		// Adds the menu bar to the program.
		setJMenuBar(aMenuBar);
		
		// Packs everything together to finish.
		pack();
	}
	
	/**
	 * Gets the table.
	 * @return The table.
	 */
	public JPanel getTable()
	{
		return aTable;
	}
	
	/**
	 * Gets the play table.
	 * @return The play table.
	 */
	public TablePlay getPlayTable()
	{
		return aTablePlay;
	}
	
	/**
	 * Gets the setup menu.
	 * @return The TableSetup.
	 */
	public TableSetup getSetupMenu()
	{
		return aTableSetup;
	}
	
	/**
	 * Gets the log line.
	 * @return logLine
	 */
	public StatusBar getLogLine()
	{
		return aLogLine;
	}
	
	/**
	 * Gets the score.
	 * @return score
	 */
	public JLabel getScore()
	{
		return aScore;
	}

	/**
	 * The update method for this observer.
	 * @param pObservable
	 * 			The observable that calls this method.
	 * @param pToUpdate
	 * 			The parameters that specify what this update is supposed to do.
	 */
	@Override
	public void update(Observable pObservable, Object pToUpdate)
	{
		assert pObservable instanceof GameEngine;
		assert ((Object[])pToUpdate)[0] instanceof String;
		aLogLine.refresh();

		Object[] toUpdate = (Object[])pToUpdate;
		
		if (((String)toUpdate[0]).equals("addCardPanelsG"))
		{
			ArrayList<CardPanel> hands = aTablePlay.getCardPanels();
			for(CardPanel panel: hands)
			{
				((GameEngine)pObservable).addObserver(panel);
			}
		}
		else if (((String)toUpdate[0]).equals("allPassesG"))
		{
			assert toUpdate[1] instanceof Boolean;
			if ((Boolean)toUpdate[1])
			{
				aTablePlay.setPassLabel();
			}
			else
			{
				aTablePlay.clearPassLabel();
			}
		}
		else if (((String)toUpdate[0]).equals("newGameG"))
		{
			assert toUpdate[1] instanceof String[];
			
			aTablePlay.setGameStage(0);
			//Updates the names (and difficulties) of the players next to each hand.
			aTablePlay.updatePlayerNames((String[]) toUpdate[1]);
			aTablePlay.clearTable();
			
			refreshScore((GameEngine) pObservable);
		}
		else if(((String)toUpdate[0]).equals("newRoundG"))
		{
			assert toUpdate[1] instanceof Integer;
			
			//Places the dealer chip in front of the correct player.
			aTablePlay.setGameStage(0);
			aTablePlay.clearTable();
			aTablePlay.setDealerChip(((Integer)toUpdate[1]).intValue());
			aTablePlay.clearButtons();
			
			//The contract is reset
			aTablePlay.clearContractInfo();
		}
		else if(((String)toUpdate[0]).equals("humanBidG"))
		{
			aTablePlay.setButtonVisibility("Bid", true);
			
			Bid[] bids = ((GameEngine)pObservable).getBids();
			int index = (((GameEngine)pObservable).getDealerIndex() + 1) % 4;
			for(Bid bid : bids)
			{
				aTablePlay.setSpot(index, bid);
				index = (index + 1) % 4;
			}
		}
		else if(((String)toUpdate[0]).equals("humanExchangeG"))
		{
			if(Bid.max(((GameEngine)pObservable).getBids()) != null && !Bid.max(((GameEngine)pObservable).getBids()).isPass())
			{
				aTablePlay.clearContractInfo();
				aTablePlay.setContractInfo(Bid.max(((GameEngine)pObservable).getBids()));
			}
			Bid[] bids = ((GameEngine)pObservable).getBids();
			int index = (((GameEngine)pObservable).getDealerIndex() + 1) % 4;
			for(Bid bid : bids)
			{
				aTablePlay.setSpot(index, bid);
				index = (index + 1) % 4;
			}
			aTablePlay.setGameStage(1);
			aTablePlay.clearButtons();
			aTablePlay.setButtonVisibility("Exchange", true);
		}
		else if(((String)toUpdate[0]).equals("contractMadeG"))
		{
			Bid[] bids = ((GameEngine)pObservable).getBids();
			int index = (((GameEngine)pObservable).getDealerIndex() + 1) % 4;
			for(Bid bid : bids)
			{
				aTablePlay.setSpot(index, bid);
				index = (index + 1) % 4;
			}
			aTablePlay.setGameStage(0);
			aTablePlay.clearButtons();
			aTablePlay.setContractHolderChip(((GameEngine)pObservable).getContractHolderIndex());
			aTablePlay.setLeaderChip(((GameEngine)pObservable).getLeaderIndex());
			aTablePlay.setButtonVisibility("Start Round", true);
			//The contract is displayed
			if(Bid.max(((GameEngine)pObservable).getBids()) != null)
			{
				aTablePlay.clearContractInfo();
				aTablePlay.setContractInfo(Bid.max(((GameEngine)pObservable).getBids()));
			}
		}
		else if(((String)toUpdate[0]).equals("humanPlayG"))
		{
			assert toUpdate[1] instanceof Trick;
			aTablePlay.clearTable();
			aTablePlay.clearButtons();
			aTablePlay.setLeaderChip(((GameEngine)pObservable).getLeaderIndex());
			CardList trick = (CardList)toUpdate[1];
			int index = ((GameEngine)pObservable).getLeaderIndex();
			if(trick.size()<4)
			{
				aTablePlay.setGameStage(2);
				aTablePlay.setButtonVisibility("Play", true);
			}
			else
			{
				Object[] objA = (Object[])pToUpdate;
				Trick theTrick = (Trick)objA[1];
				refreshTricks(theTrick);
				aTablePlay.setGameStage(0);
				if(((GameEngine)pObservable).getHand(0).size() == 0)
				{
					aTablePlay.setButtonVisibility("End Round", true);
				}
				else
				{
					aTablePlay.setButtonVisibility("Next Trick", true);
				}
			}
			for(Card card : trick)
			{
				aTablePlay.setSpot(index, card);
				index = (index + 1) % 4;
			}

		}
		else if(((String)toUpdate[0]).equals("betweenRoundsG"))
		{
			aTablePlay.clearTable();
			aTablePlay.clearButtons();
			aTablePlay.setButtonVisibility("Next Round", true);
			aTeam1Tricks = 0;
			aTeam2Tricks = 0;
			refreshTricks(null);
			refreshScore((GameEngine) pObservable);
		}
		else if(((String)toUpdate[0]).equals("gameOverG"))
		{
			aTablePlay.clearTable();
			aTablePlay.clearButtons();
			
			aTablePlay.updateWinnerAndShow(((GameEngine) pObservable).getScore(0) > ((GameEngine) pObservable).getScore(1));
			
			aTablePlay.setButtonVisibility("End Game", true);
			aTeam1Tricks = 0;
			aTeam2Tricks = 0;
			refreshTricks(null);
			refreshScore((GameEngine) pObservable);
		}
	}
	
	/**
	 * Refreshes the score.
	 * @param pGame
	 * 		The game from which to pull the score.
	 */
	public void refreshScore(GameEngine pGame)
	{
		//Refresh the score
		int team1Score = pGame.getScore(0);
		int team2Score = pGame.getScore(1);
		String newScore = "Score - Your Team: " + team1Score + "\t Opponent Team 2: "  + team2Score;
		aScore.setText(newScore);		
	}

	/**
	 * Refreshes the score.
	 * @param pTrick
	 * 		The game from which to pull the score.
	 */
	public void refreshTricks(Trick pTrick)
	{
		//Refresh the score
		String newTricks = "";
		if (pTrick == null)
		{
			newTricks = "Tricks - Your Team: -- Opponent Team: --";
		}
		else
		{
			if (pTrick.winnerIndex() %2 == 1)
			{
				aTeam1Tricks++;
			}
			else if (pTrick.winnerIndex() %2 == 0)
			{
				aTeam2Tricks++;
			}
			newTricks = "Tricks - Your Team: " + aTeam1Tricks + "\t Opponent Team: "  + aTeam2Tricks;
		}
		aTricks.setText(newTricks);		
	}

	
	/**
	 * Constructs the GUI.
	 * @param args
	 */
	public static void main(String args[])
	{
		new FHGUI();
	}
}