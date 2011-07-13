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
public class RecordingTest
{

	/**
	 * Fetches 5 recordings from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordings()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUID(
				"recording", "gid", "mo:Signal", "RecordingsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}
	
	/**
	 * Fetches 5 recordings from the DB and resolves theirs names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistNames()
	{
		TestResult testResult = Utils.getInstance().checkInstanceNamesViaGUID(
				"recording", "track_name", "mo:Signal", "dct:title",
				"RecordingNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(RecordingTest.class);
	}
}
