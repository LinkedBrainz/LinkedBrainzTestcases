package linkedbrainz.testcases;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
	/*@Test
	public void checkTracks()
	{
		TestResult testResult = Utils.getInstance().checkClassViaID("track",
				"id", "mo:Track", "TracksCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}

	*//**
	 * Fetches 1 track from the DB and resolves its names against the result of
	 * the related SPARQL query.
	 * 
	 * ATTENTION: requires to switch off the resultSizeLimit
	 * 
	 *//*
	@Test
	public void checkTrackNames()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaIDOnTheLeft("track", "track_name",
						"name", "name", "mo:Track", "dct:title", "title",
						"TrackNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}*/
	
	/**
	 * Fetches 1 (+1) track(s) and their music artists from the DB and resolves them via
	 * a SPARQL query.
	 * 
	 * ATTENTION: requires to switch off the resultSizeLimit
	 * 			  causes Java heap space error ATM :\
	 * 
	 */
	@Test
	public void checkTracksMusicArtistsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("track");
		classTables.add("artist");
		classTables.add("artist_credit");
		classTables.add("artist_credit_name");

		classTableRows.add("id");
		classTableRows.add("gid");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");
		classTableRows.add("artist");

		classNames.add("mo:Track");
		classNames.add("mo:MusicArtist");

		valueNames.add("trackURI");
		valueNames.add("artistURI");
		valueNames.add("artistGUID");

		// add ""Five Man Army" from Massive Attack as proof id
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaIDonTheLeftAndGUIDonTheRight(classTables, classTableRows,
						classNames, "foaf:maker", valueNames,
						"11",
						"TracksArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(TrackTest.class);
	}

}
