package comp303.fivehundred.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * The menu bar that exists at the top of the core JPanel in FHGUI.
 * @author Maxim Gorshkov, Dashiel Siegel
 *
 */
public class MenuBar extends JMenuBar
{
	private static final long serialVersionUID = -7518571474337025757L;
	private GUIListener aGlobalListener = null;
	
	/**
	 * Sets the parameters for the menu bar at the top of the GUI. This includes settings for the game and logging. Each
	 * Menu (item which is seen on the top level) contains a MenuItem which appears on the click. An actionListener is
	 * required to perform anything.
	 * @param pGod
	 * 		The GUIListener that performs event-handling.
	 */
	public MenuBar(GUIListener pGlobalListener)
	{
		super();
		
		aGlobalListener = pGlobalListener;
	
		// Sets up Game, Settings, and Test menus.
		JMenu gamePlay = new JMenu("Game");
		JMenu gameSettings = new JMenu("Settings");

		// Sets up the "Game > New Game" menu option.
		JMenuItem newGameItem = new JMenuItem("New Game");
		newGameItem.setToolTipText("Starts a new game of Five Hundred.");
		newGameItem.addActionListener(aGlobalListener);
		
		// Sets up the "Game > Exit" menu option.
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setToolTipText("Exits Five Hundred");
		exitMenuItem.addActionListener(aGlobalListener);
		
		// Sets up the "Games > Show Statistics" menu option.
		JMenuItem showStatsItem = new JMenuItem("Show Statistics");
		showStatsItem.setToolTipText("Shows the current game statistics.");
		showStatsItem.addActionListener(aGlobalListener);
		
		// Sets up the "Settings > Fully Automated Gameplay" menu option.
		JMenuItem toggleOpenHand = new JMenuItem("Toggle Hand Visibility");
		toggleOpenHand.setToolTipText("Toggles the visibility of all computer hands.");
		toggleOpenHand.addActionListener(aGlobalListener);

		// Sets up the "Settings > Change Log Location" menu option.
		JMenuItem logLocationItem = new JMenuItem("Change Log Location");
		logLocationItem.setToolTipText("Specifies the directory to store the log file.");
		logLocationItem.addActionListener(aGlobalListener);

		// Sets up the "Settings > Move Statistics Statistics" menu option.
		JMenuItem moveStatsItem = new JMenuItem("Change Statistics File Location");
		moveStatsItem.setToolTipText("Moves the statistics file.");
		moveStatsItem.addActionListener(aGlobalListener);
				
		
		// Adds the secondary-level option under Game.
		gamePlay.add(newGameItem);
		gamePlay.add(moveStatsItem);
		gamePlay.add(exitMenuItem);

		gameSettings.add(logLocationItem);
		gameSettings.add(moveStatsItem);
		gameSettings.add(toggleOpenHand);
		
		
		// Adds the "game" options with sub-options to the menu bar
		add(gamePlay);
		add(gameSettings);	
	}
}
