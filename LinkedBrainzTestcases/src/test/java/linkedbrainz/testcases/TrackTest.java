package linkedbrainz.testcases;

import static org.junit.Assert.*;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.TestResult;

import org.junit.Test;

/**
 * 
 * ATTENTION: requires to switch off the resultSizeLimit; please run these test
 * separately if possible since they cause heavy load on the machine and usually
 * take a bit longer
 * 
 * 
 * @author zazi
 * 
 */
public class TrackTest
{
	/**
	 * Fetches 1 track from the DB and resolves it via a SPARQL query.
	 * 
	 * ATTENTION: requires to switch off the resultSizeLimit
	 * 
	 */
	@Test
	public void checkTracks()
	{
		TestResult testResult = Utils.getInstance().checkClassViaID("track",
				"id", "mo:Track", "TracksCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}

	/**
	 * Fetches 1 track from the DB and resolves its names against the result of
	 * the related SPARQL query.
	 * 
	 * ATTENTION: requires to switch off the resultSizeLimit
	 * 
	 */
	@Test
	public void checkTrackNames()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaIDOnTheLeft("track", "track_name",
						"name", "name", "mo:Track", "dct:title", "title",
						"TrackNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(TrackTest.class);
	}

}
