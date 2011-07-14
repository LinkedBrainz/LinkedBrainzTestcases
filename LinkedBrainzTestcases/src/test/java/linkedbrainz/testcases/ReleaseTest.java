package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.TestResult;

import org.junit.Test;

/**
 * 
 * @author zazi
 * 
 */
public class ReleaseTest
{

	/**
	 * Fetches 5 releases from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkReleases()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUIDSimple(
				"release", "gid", "mo:Release", "ReleasesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 releases from the DB and resolves their names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseNames()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("release", "release_name",
						"name", "name", "mo:Release", "dct:title", "title",
						"ReleaseNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) releases and their music artists from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseMusicArtistsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release");
		classTables.add("artist");
		classTables.add("artist_credit");
		classTables.add("artist_credit_name");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");
		classTableRows.add("artist");

		classNames.add("mo:Release");
		classNames.add("mo:MusicArtist");

		valueNames.add("releaseURI");
		valueNames.add("artistURI");
		valueNames.add("artistGUID");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance().checkURIInversePropertyViaGUIDs(
				classTables, classTableRows, classNames, "foaf:maker",
				valueNames, "44b7cab1-0ce1-404e-9089-b458eb3fa530",
				"ReleasesArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ReleaseTest.class);
	}

}
