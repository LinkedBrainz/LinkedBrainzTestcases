package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.TestResult;

/**
 * 
 * @author zazi
 * 
 */
public class TracklistTest
{
	/**
	 * Fetches 1 track list from the DB and resolves it via a SPARQL query.
	 * 
	 * ATTENTION: requires to switch off the resultSizeLimit
	 * 
	 */
	@Test
	public void checkTrackLists()
	{
		TestResult testResult = Utils.getInstance().checkClassViaID(
				"tracklist", "id", "mo:Record", "TrackListsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(TracklistTest.class);
	}
}
