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
public class ReleaseGroupTest
{
	/**
	 * Fetches 5 release groups from the DB and resolves them via a SPARQL
	 * query.
	 * 
	 */
	@Test
	public void checkReleaseGroups()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUIDSimple(
				"release_group", "gid", "mo:SignalGroup", "ReleaseGroupsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}

	/**
	 * Fetches 5 release groups from the DB and resolves their names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseGroupNames()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("release_group",
						"release_name", "name", "name", "mo:SignalGroup",
						"dct:title", "title", "ReleaseGroupNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) release groups and their music artists from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseGroupsMusicArtistsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release_group");
		classTables.add("artist");
		classTables.add("artist_credit");
		classTables.add("artist_credit_name");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");
		classTableRows.add("artist");

		classNames.add("mo:SignalGroup");
		classNames.add("mo:MusicArtist");

		valueNames.add("releaseGroupURI");
		valueNames.add("artistURI");
		valueNames.add("artistGUID");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" from The Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance().checkURIInversePropertyViaGUIDs(
				classTables, classTableRows, classNames, "foaf:maker",
				valueNames, "9f7a4c28-8fa2-3113-929c-c47a9f7982c3",
				"ReleaseGroupsArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ReleaseGroupTest.class);
	}

}
