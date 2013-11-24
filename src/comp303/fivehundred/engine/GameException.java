/**
 * @Author Dashiel Siegel
 */
package comp303.fivehundred.engine;

/**
 * Represents a bug related to the misuse of a GameEngine object.
 */
@SuppressWarnings("serial")
public class GameException extends RuntimeException
{
	/**
	 * Constructor.
	 * @param pMessage The message.
	 * @param pException The Exception.
	 */
	public GameException(String pMessage, Throwable pException)
	{
		super(pMessage, pException);
	}

	/**
	 * Constructor.
	 * @param pMessage The message.
	 */
	public GameException(String pMessage)
	{
		super(pMessage);
	}

	/**
	 * Constructor.
	 * @param pException The exception.
	 */
	public GameException(Throwable pException)
	{
		super(pException);
	}

}
