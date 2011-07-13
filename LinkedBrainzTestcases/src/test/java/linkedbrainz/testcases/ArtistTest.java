package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;
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
		TestResult testResult = Utils.getInstance().checkClassViaGUID("artist",
				"gid", "mo:MusicArtist", "ArtistsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 music artists from the DB and resolves theirs names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistNames()
	{
		TestResult testResult = Utils.getInstance().checkInstanceNamesViaGUID(
				"artist", "name", "artist_name", "mo:MusicArtist", "foaf:name",
				"ArtistNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 music artists from the DB and resolves theirs sort labels against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistSortLabels()
	{
		TestResult testResult = Utils.getInstance().checkInstanceNamesViaGUID(
				"artist", "sort_name", "artist_name", "mo:MusicArtist", "ov:sortLabel",
				"ArtistSortLabelsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ArtistTest.class);
	}
}
