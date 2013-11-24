/**
 * @author James McCorriston
 */

package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import java.util.Random;

/**
 * Enters a valid but random bid. Passes a configurable number of times. If the robot does not pass, it uses a universal
 * probability distribution across all bids permitted.
 */
public class RandomBiddingStrategy implements IBiddingStrategy
{
	public static final int MAX_PERCENT = 100;
	public static final int MAX_BID_INDEX = 24;
	public static final int MAX_HAND_SIZE = 10;
	public static final int DEFAULT_PASS_FREQUENCY = 50;
	private int aPassFrequency;
	private Bid aBid;
	private Random aGenerator = new Random();

	/**
	 * Builds a robot that passes 50% of the time and bids randomly otherwise.
	 */
	public RandomBiddingStrategy()
	{
		// 50% chance for the robot to pass
		aPassFrequency = DEFAULT_PASS_FREQUENCY;
	}

	/**
	 * Builds a robot that passes the specified percentage number of the time.
	 * 
	 * @param pPassFrequency
	 *            A percentage point (e.g., 50 for 50%) of the time the robot will pass. Must be between 0 and 100
	 *            inclusive. Whether the robot passes is determined at random.
	 */
	public RandomBiddingStrategy(int pPassFrequency)
	{
		aPassFrequency = pPassFrequency;
	}

	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		assert pPreviousBids.length <= 3;
		assert pHand.size() == MAX_HAND_SIZE;
		
		//Used for determining which bids are higher than the highest so far.
		int minimumBidIndex;
		
		//If this is the first bid or if all bids so far have been a pass.
		if(pPreviousBids.length < 1 || Bid.max(pPreviousBids).isPass())
		{
			minimumBidIndex = 0;
		}
		else
		{
			minimumBidIndex = Bid.max(pPreviousBids).toIndex() + 1;
		}
		
		//Randomly decides to bid (if possible).
		if (aGenerator.nextInt(MAX_PERCENT) > aPassFrequency && minimumBidIndex < MAX_BID_INDEX)
		{
			//Generates a random, valid (higher than the current highest) bid.
			aBid = new Bid(MAX_BID_INDEX - aGenerator.nextInt(MAX_BID_INDEX - minimumBidIndex));
		}
		//Randomly decides to pass.
		else
		{
			//Generates a passing bid.
			aBid = new Bid();
		}
		return aBid;
	}

}
