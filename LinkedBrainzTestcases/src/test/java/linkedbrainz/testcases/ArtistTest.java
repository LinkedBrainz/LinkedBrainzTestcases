package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;
import linkedbrainz.testcases.model.TestResult;

import org.junit.Test;

/**
 * 
 * @author zazi
 * 
 */
public class ArtistTest
{

	/**
	 * Fetches 5 music artists from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtists()
	{
		TestResult testResult = Utils.getInstance().checkClass("artist",
				"mo:MusicArtist", "ArtistsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
}
