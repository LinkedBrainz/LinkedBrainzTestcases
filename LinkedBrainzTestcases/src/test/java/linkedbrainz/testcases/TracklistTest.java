package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
	 * Fetches 5 track lists from the DB and resolves it via a SPARQL query.
	 * 
	 * ATTENTION: requires to switch off the resultSizeLimit
	 * 
	 */
	@Test
	public void checkTrackLists()
	{
		TestResult testResult = Utils.getInstance()
				.checkClassViaGUIDOrIDAndFragmentId("tracklist", "id",
						"mo:Record", "#_", 5, "TrackListsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}
	
	/**
	 * Fetches 5 track lists from the DB and resolves their track counts against
	 * the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkTrackListsTrackCount()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		
		classTables.add("tracklist");
		
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						"country", "iso_code", "mo:MusicArtist",
						"foaf:based_near", "country", 1, 5, true,
						"ArtistsCountryCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(TracklistTest.class);
	}
}
