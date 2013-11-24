/**
 * @author Dashiel Siegel
 */
package comp303.fivehundred.ai;

import java.util.Observable;
import java.util.Observer;

/**
 * This class is implemented by each type of player (AIs of different difficulty or human).
 * Methods dealing with the name of the player are concretely defined as is a method
 * that checks if the Player is a human or not.
 *
 */
public abstract class Player implements IRobotPlayer, Observer
{	
	private String aName;
	private String aDifficulty;

	/**
	 * Checks if the player is human or not.
	 * @return Returns true if this Player is a human, false otherwise.
	 */
	public boolean isHuman()
	{
		return this.getClass().equals(HumanPlayer.class);
	}
	
	/**
	 * Checks if the player is an advanced AI.
	 * @return Returns true if this Player is an advanced AI, false otherwise.
	 */
	public boolean isAdvanced()
	{
		return this.getClass().equals(AdvancedRobot.class);
	}
	
	/**
	 * Checks if the player is a basic AI.
	 * @return Returns true if this Player is a basic AI, false otherwise.
	 */
	public boolean isBasic()
	{
		return this.getClass().equals(BasicRobot.class);
	}
	
	/**
	 * Checks if the player is a random AI.
	 * @return Returns true if this Player is a random AI, false otherwise.
	 */
	public boolean isRandom()
	{
		return this.getClass().equals(RandomRobot.class);
	}

	/**
	 * Returns the name of the Player.
	 * @return
	 * 			The name of the Player.
	 */
	public String getName()
	{
		return aName;
	}

	/**
	 * Sets the name of the Player.
	 * @param pName
	 * 			The name of the Player.
	 */
	public void setName(String pName)
	{
		this.aName = pName;
	}
	
	/**
	 * Sets the difficulty of the player. Should only be done once.
	 * @param pDifficulty A String representing the difficulty.
	 */
	protected void setDifficulty(String pDifficulty)
	{
		aDifficulty = pDifficulty;
	}
	
	/**
	 * Gets the difficulty of the Player.
	 * @return A string representing the difficulty of the Player.
	 */
	public String getDifficulty()
	{
		return aDifficulty;
	}
	
	/**
	 * Allows players to "remember" game state, if they so choose.
	 * By default, this method does nothing
	 * 
	 * @param pGame The game currently being played.
	 * @param pArg An argument passed by the Observable in its notifyObservers method.
	 * 
	 */
	public void update(Observable pGame, Object pArg)
	{
		
	}
	
	/**
	 * Sets the index of the AdvanceRobot.
	 * @param pIndex The index of the robot.
	 */
	public void setIndex(int pIndex)
	{
		
	}
}
