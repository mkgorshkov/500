/**
 * @author Andrew Borodovski
 */
package comp303.fivehundred.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp303.fivehundred.ai.TestBasicBiddingStrategy;
import comp303.fivehundred.ai.TestBasicCardExchangeStrategy;
import comp303.fivehundred.ai.TestBasicPlayingStrategy;
import comp303.fivehundred.ai.TestRandomBiddingStrategy;
import comp303.fivehundred.ai.TestRandomCardExchangeStrategy;
import comp303.fivehundred.ai.TestRandomPlayingStrategy;
import comp303.fivehundred.engine.TestGameEngine;
import comp303.fivehundred.model.TestBid;
import comp303.fivehundred.model.TestHand;
import comp303.fivehundred.model.TestTrick;
import comp303.fivehundred.util.TestByRankComparator;
import comp303.fivehundred.util.TestBySuitComparator;
import comp303.fivehundred.util.TestBySuitNoTrumpComparator;
import comp303.fivehundred.util.TestCard;
import comp303.fivehundred.util.TestCardList;
import comp303.fivehundred.util.TestDeck;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestCard.class, 
	TestDeck.class,
	TestByRankComparator.class,
	TestBySuitComparator.class,
	TestBySuitNoTrumpComparator.class,
	TestCardList.class,
	TestBid.class,
	TestHand.class,
	TestTrick.class,
	TestRandomBiddingStrategy.class,
	TestRandomCardExchangeStrategy.class,
	TestRandomPlayingStrategy.class,
	TestBasicBiddingStrategy.class,
	TestBasicPlayingStrategy.class,
	TestBasicCardExchangeStrategy.class,
	TestGameEngine.class,
	})
public class AllTests
{

}
