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
		TestResult testResult = Utils.getInstance().checkClassViaGUIDSimple(
				"recording", "gid", "mo:Signal", "RecordingsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}

	/**
	 * Fetches 5 recordings from the DB and resolves their names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordingNames()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("recording", "track_name",
						"name", "name", "mo:Signal", "dct:title", "title",
						"RecordingNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 recordings from the DB and resolves their ISRC codes against
	 * the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordingISRCs()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheRight("isrc", "recording",
						"recording", "isrc", "mo:Signal", "mo:isrc", "ISRC",
						"RecordingISRCsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(RecordingTest.class);
	}
}
