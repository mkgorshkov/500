package comp303.fivehundred.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * Sets the information pertaining to the players of the game when the use first starts the game or between games. The
 * UI also shows the statistics of the current set of games and lets the user reset the statistics as required.
 * 
 * @author Maxim Gorshkov, Dashiel Siegel, James McCorriston, Andrew Borodovski
 */
public class TableSetup extends JPanel
{
	// Sets the generated serialID.
	private static final long serialVersionUID = 8969219827342133439L;
	// Sets the maximum length of a player name.
	private static final int PLAYERNAME_SIZE = 13;
	// Sets the offset values for positioning the input for the players
	private static final int OFFSET_CORNERS = 5;
	private static final int OFFSET_PLAYER_ONE = 20;
	private static final int OFFSET_PLAYER_ONE_NAME = 45;
	private static final int OFFSET_PLAYER_ONE_TYPE = 70;
	private static final int OFFSET_PLAYER_TWO = 90;
	private static final int OFFSET_PLAYER_TWO_NAME = 115;
	private static final int OFFSET_PLAYER_TWO_TYPE = 150;
	private static final int OFFSET_PLAYER_THREE = 170;
	private static final int OFFSET_PLAYER_THREE_NAME = 195;
	private static final int OFFSET_PLAYER_THREE_TYPE = 230;
	private static final int OFFSET_PLAYER_FOUR_NAME = 250;
	private static final int OFFSET_PLAYER_FOUR_TYPE = 275;
	
	
	// Initializes the text boxes for players
	private JTextField[] aPlayerNames = {new JTextField(PLAYERNAME_SIZE), new JTextField(PLAYERNAME_SIZE),
			new JTextField(PLAYERNAME_SIZE), new JTextField(PLAYERNAME_SIZE), new JTextField(PLAYERNAME_SIZE)};

	// Initializes player types
	private JComboBox[] aPlayerTypes = { new JComboBox(), new JComboBox(), new JComboBox(), new JComboBox()};

	// Initializes the variable to keep track of if the game has begun or not.
	private boolean aGameHasBegun = false;
	
	// Initializes the objects that need to be accessed by GUIListener.
	private JTextArea aStatTextBox = new JTextArea();

	/**
	 * Sets up the main player information for the game. This is the first screen that the user sees
	 * upon starting the application.
	 * @param pGlobalListener - GUIListener responsible for all of the actions that can be done in this panel.
	 */
	public TableSetup(GUIListener pGlobalListener)
	{
		// Adds default constructor functionality
		super(new BorderLayout());
		
		//////////////////////////////////////////////////////
		// 			Sets up the core JPanel 				//
		//////////////////////////////////////////////////////
		
		// Sets the background.
		Color bg = Color.orange;
		setBackground(bg);
		
		// Creates and adds a header.
		JLabel header = new JLabel("Welcome to 500!");
		add(header, BorderLayout.NORTH);
		
		// Creates a panel to divide the settings and the stats sections. Sets its layout to GridLayout.
		GridLayout gridLayout = new GridLayout(1, 2);
		JPanel content = new JPanel();
		content.setLayout(gridLayout);
		
		// Creates a panel to showcase/edit game SETTINGS. Sets its layout to SpringLayout.
		JPanel gridLeftSettings = new JPanel();
		gridLeftSettings.setBorder(BorderFactory.createTitledBorder("Game Settings"));
		gridLeftSettings.setBackground(bg);
		SpringLayout gridLeftSettingsSpringLayout = new SpringLayout();
		gridLeftSettings.setLayout(gridLeftSettingsSpringLayout);

		// Creates a panel to showcase/edit game STATISTICS. Sets its layout to BorderLayout.
		JPanel gridRightStats = new JPanel();
		gridRightStats.setBorder(BorderFactory.createTitledBorder("Statistics"));
		gridRightStats.setLayout(new BorderLayout());
		gridRightStats.setBackground(bg);

		FileWriter aStream = null;
		try
		{
			aStream = new FileWriter(pGlobalListener.getStatsLocation(), false);
			BufferedWriter statsOut = new BufferedWriter(aStream);
			statsOut.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		refreshStatBox(pGlobalListener);
		
		// Creates a panel to hold the TextBox we just created
		JPanel statTextBoxHolder = new JPanel();
		aPlayerNames[0].setText("Player 1");
		aPlayerNames[1].setText("Player 2");
		aPlayerNames[2].setText("Player 3");
		aPlayerNames[3].setText("Player 4");
		statTextBoxHolder.add(aStatTextBox, BorderLayout.WEST);
		statTextBoxHolder.setBackground(bg);
		aStatTextBox.setEditable(false);
		aStatTextBox.setCursor(null);
		aStatTextBox.setOpaque(false);
		aStatTextBox.setFocusable(false);
		aStatTextBox.setTabSize(4);
		refreshStatBox(pGlobalListener);
	
		// Adds the TextBox to its Holder.
		gridRightStats.add(statTextBoxHolder, BorderLayout.NORTH);
		
		//Creates a panel to hold the statistic buttons
		JPanel statsButtons = new JPanel();
		statsButtons.setBackground(bg);
		
		
		//////////////////////////////////////////////////////
		// 		Sets up the player "option-boxes"			//
		//////////////////////////////////////////////////////

		// Sets up the "option-box" for Player 1
		JLabel player1ID = new JLabel("Player One - User");
		JLabel player1NamePrompt = new JLabel("Enter Player Name:");
		JLabel player1TypePrompt = new JLabel("Enter Player Type:");
		String[] player1TypeOptions = { "Human" };
		aPlayerTypes[0] = new JComboBox(player1TypeOptions);
		aPlayerTypes[0].setSelectedIndex(0);
		
		// Sets up the "option-box" for Player 2
		JLabel player2ID = new JLabel("Player Two - Opponent");
		JLabel player2NamePrompt = new JLabel("Enter Player Name:");
		JLabel player2TypePrompt = new JLabel("Enter Player Type:");
		String[] player2TypeOptions = { "Random AI", "Basic AI", "Advanced AI" };
		aPlayerTypes[1] = new JComboBox(player2TypeOptions);
		aPlayerTypes[1].setSelectedIndex(0);
		
		// Sets up the "option-box" for Player 3
		JLabel player3ID = new JLabel("Player Three - Ally");
		JLabel player3NamePrompt = new JLabel("Enter Player Name:");
		JLabel player3TypePrompt = new JLabel("Enter Player Type:");
		String[] player3TypeOptions = { "Random AI", "Basic AI", "Advanced AI" };
		aPlayerTypes[2] = new JComboBox(player3TypeOptions);
		aPlayerTypes[2].setSelectedIndex(0);
		
		// Sets up the "option-box" for Player 4
		JLabel player4ID = new JLabel("Player Four - Opponent");
		JLabel player4NamePrompt = new JLabel("Enter Player Name:");
		JLabel player4TypePrompt = new JLabel("Enter Player Type:");
		String[] player4TypeOptions = { "Random AI", "Basic AI", "Advanced AI" };
		aPlayerTypes[3] = new JComboBox(player4TypeOptions);
		aPlayerTypes[3].setSelectedIndex(0);

		// Adds the player 1 "option-box" to the settings side of the grid
		gridLeftSettings.add(player1ID);
		gridLeftSettings.add(player1NamePrompt);
		gridLeftSettings.add(aPlayerNames[0]);
		gridLeftSettings.add(player1TypePrompt);
		gridLeftSettings.add(aPlayerTypes[0]);

		// Adds the player 2 "option-box" to the settings side of the grid
		gridLeftSettings.add(player2ID);
		gridLeftSettings.add(player2NamePrompt);
		gridLeftSettings.add(aPlayerNames[1]);
		gridLeftSettings.add(player2TypePrompt);
		gridLeftSettings.add(aPlayerTypes[1]);
		
		// Adds the player 3 "option-box" to the settings side of the grid
		gridLeftSettings.add(player3ID);
		gridLeftSettings.add(player3NamePrompt);
		gridLeftSettings.add(aPlayerNames[2]);
		gridLeftSettings.add(player3TypePrompt);
		gridLeftSettings.add(aPlayerTypes[2]);
		
		// Adds the player 4 "option-box" to the settings side of the grid
		gridLeftSettings.add(player4ID);
		gridLeftSettings.add(player4NamePrompt);
		gridLeftSettings.add(aPlayerNames[3]);
		gridLeftSettings.add(player4TypePrompt);
		gridLeftSettings.add(aPlayerTypes[3]);
		
		
		//////////////////////////////////////////////////////
		// 			Creates the Buttons.					//
		//////////////////////////////////////////////////////
		
		// Sets up the "Setup New Game" button.
		JButton setupNewGame = new JButton("Setup New Game");
		setupNewGame.setActionCommand("Setup New Game");
		setupNewGame.addActionListener(pGlobalListener);
		
		// Sets up the "Reset Stats" button
		JButton resetStats = new JButton("Reset Statistics");
		resetStats.setActionCommand("Reset Stats");
		resetStats.addActionListener(pGlobalListener);
		
		// Adds the buttons to their respective panels and the respective panels to the proper side of the GridLayout.
		statsButtons.add(setupNewGame);
		statsButtons.add(resetStats);
		gridRightStats.add(statsButtons, BorderLayout.CENTER);		
		
		//////////////////////////////////////////////////////
		// 	Fine-tunes the positioning of the option-boxes	//
		//////////////////////////////////////////////////////
		
		// Adjust constraints for the input prompt labels. Sets them to (5,5).
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, player1NamePrompt, OFFSET_CORNERS, SpringLayout.WEST, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, player1NamePrompt, OFFSET_PLAYER_ONE, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, player1TypePrompt, OFFSET_CORNERS, SpringLayout.WEST, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, player1TypePrompt, 
				OFFSET_PLAYER_ONE_NAME, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, player2ID, OFFSET_PLAYER_ONE_TYPE, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, player2NamePrompt, OFFSET_CORNERS, SpringLayout.WEST, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, player2NamePrompt, OFFSET_PLAYER_TWO, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, player2TypePrompt, OFFSET_CORNERS, SpringLayout.WEST, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, 
				player2TypePrompt, OFFSET_PLAYER_TWO_NAME, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, player3ID, OFFSET_PLAYER_TWO_TYPE, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, player3NamePrompt, OFFSET_CORNERS, SpringLayout.WEST, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, player3NamePrompt, OFFSET_PLAYER_THREE, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, player3TypePrompt, OFFSET_CORNERS, SpringLayout.WEST, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, 
				player3TypePrompt, OFFSET_PLAYER_THREE_NAME, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, player4ID, OFFSET_PLAYER_THREE_TYPE, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, player4NamePrompt, OFFSET_CORNERS, SpringLayout.WEST, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, 
				player4NamePrompt, OFFSET_PLAYER_FOUR_NAME, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, player4TypePrompt, OFFSET_CORNERS, SpringLayout.WEST, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, 
				player4TypePrompt, OFFSET_PLAYER_FOUR_TYPE, SpringLayout.NORTH, gridLeftSettings);
		
		// Adjust constraints for the text fields and combo-boxes.
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, aPlayerNames[0], OFFSET_CORNERS, SpringLayout.EAST, player1NamePrompt);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, aPlayerNames[0], OFFSET_PLAYER_ONE, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, aPlayerTypes[0], OFFSET_CORNERS, SpringLayout.EAST, player1NamePrompt);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, aPlayerTypes[0], OFFSET_PLAYER_ONE_NAME, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, aPlayerNames[1], OFFSET_CORNERS, SpringLayout.EAST, player2NamePrompt);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, aPlayerNames[1], OFFSET_PLAYER_TWO, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, aPlayerTypes[1], OFFSET_CORNERS, SpringLayout.EAST, player2NamePrompt);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, aPlayerTypes[1], OFFSET_PLAYER_TWO_NAME, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, aPlayerNames[2], OFFSET_CORNERS, SpringLayout.EAST, player3NamePrompt);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, aPlayerNames[2], OFFSET_PLAYER_THREE, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, aPlayerTypes[2], OFFSET_CORNERS, SpringLayout.EAST, player3NamePrompt);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, 
				aPlayerTypes[2], OFFSET_PLAYER_THREE_NAME, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, aPlayerNames[3], OFFSET_CORNERS, SpringLayout.EAST, player4NamePrompt);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, 
				aPlayerNames[3], OFFSET_PLAYER_FOUR_NAME, SpringLayout.NORTH, gridLeftSettings);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, aPlayerTypes[3], OFFSET_CORNERS, SpringLayout.EAST, player4NamePrompt);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.NORTH, 
				aPlayerTypes[3], OFFSET_PLAYER_FOUR_TYPE, SpringLayout.NORTH, gridLeftSettings);

		// Adjust constraints for the content pane.
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, gridLeftSettings, OFFSET_CORNERS, SpringLayout.WEST, aPlayerNames[0]);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, gridLeftSettings, OFFSET_CORNERS, SpringLayout.WEST, aPlayerNames[1]);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, gridLeftSettings, OFFSET_CORNERS, SpringLayout.WEST, aPlayerNames[2]);
		gridLeftSettingsSpringLayout.putConstraint(SpringLayout.WEST, gridLeftSettings, OFFSET_CORNERS, SpringLayout.WEST, aPlayerNames[3]);
		

		//////////////////////////////////////////////////////
		// 		Sets up the core JPanel (ctd)				//
		//////////////////////////////////////////////////////
		
		// Adds the left container to the settings container
		content.add(gridLeftSettings);

		// Adds the right container to the settings container
		content.add(gridRightStats);

		// Adds both of the containers to the main content panel.
		add(content);

	}
	
	/**
	 * Setter method for aGameHasBegun. Tracks whether a game has started yet.
	 * @param pGameHasBegun
	 * 		The new value for aGameHasBegun.
	 */
	public void setGameHasBegun(boolean pGameHasBegun)
	{
		this.aGameHasBegun = pGameHasBegun;
	}

	/**
	 * Retrieves an ArrayList with the names of each of the players.
	 * @return List of player names
	 */
	public ArrayList<String> fillPlayerNames()
	{
		ArrayList<String> playerNames = new ArrayList<String>();
		playerNames.add(aPlayerNames[0].getText());
		playerNames.add(aPlayerNames[1].getText());
		playerNames.add(aPlayerNames[2].getText());
		playerNames.add(aPlayerNames[3].getText());
		return playerNames;
	}
	
	/**
	 * Retrieves an ArrayList with the types of each of the players ex. AI or Human.
	 * @return List of player types
	 */
	public ArrayList<String> fillPlayerTypes()
	{
		ArrayList<String> playerTypes = new ArrayList<String>();
		playerTypes.add(aPlayerTypes[0].getSelectedItem().toString());
		playerTypes.add(aPlayerTypes[1].getSelectedItem().toString());
		playerTypes.add(aPlayerTypes[2].getSelectedItem().toString());
		playerTypes.add(aPlayerTypes[3].getSelectedItem().toString());
		return playerTypes;
	}	
	
	/**
	 * Getter method for aGameHasBegun. Tracks if the game has been set up yet.
	 * @return aGameHasBegun
	 * 		Whether the game has begun or not.
	 */
	public boolean getGameHasBegun()
	{
		return aGameHasBegun;
	}
	
	/**
	 * Creates and/or refreshes the TextBox used to display the statfile.
	 * @param pGod
	 * 			The event handler.
	 */
	public void refreshStatBox(GUIListener pGod)
	{
		// Creates a TextBox to hold the text representation of the game-stats.
		Scanner statsFullText = null;
		try
		{
			File statsFile = new File(pGod.getStatsLocation());
			statsFile.createNewFile();
			statsFullText = new Scanner(statsFile);
		}
		catch (FileNotFoundException e)
		{
		}
		catch (IOException e)
		{
		}
		statsFullText.useDelimiter("\\Z");
		if (statsFullText.hasNext())
		{
			String toSet = "\n";
			toSet += statsFullText.next();
			aStatTextBox.setText(toSet);
		}
		else
		{
			aStatTextBox.setText("*The statistics file is empty!*");
		}
	}
	
	/**
	 * Clears the textboxes containing player names.
	 */
	public void refreshPlayerNames()
	{
		aPlayerNames[0].setText("Player 1");
		aPlayerNames[1].setText("Player 2");
		aPlayerNames[2].setText("Player 3");
		aPlayerNames[3].setText("Player 4");
	}
	
	/**
	 * Clears the comboboxes containing player types.
	 */
	public void refreshPlayerTypes()
	{
		aPlayerTypes[0].setSelectedIndex(0);
		aPlayerTypes[1].setSelectedIndex(1);
		aPlayerTypes[2].setSelectedIndex(1);
		aPlayerTypes[3].setSelectedIndex(1);
	}
}