package linkedbrainz.testcases;

import static org.junit.Assert.*;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.TestResult;

import org.junit.Test;

/**
 * 
 * @author zazi
 *
 */
public class TrackTest
{
	/**
	 * Fetches 1 track from the DB and resolves it via a SPARQL
	 * query.
	 * 
	 * ATTENTION: requires to switch off the resultSizeLimit
	 * 
	 */
	@Test
	public void checkTracks()
	{
		TestResult testResult = Utils.getInstance().checkClassViaID(
				"track", "id", "mo:Track", "TracksCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}
	
	public static junit.framework.Test suite() 
	{ 
	    return new JUnit4TestAdapter(TrackTest.class); 
	}

}
