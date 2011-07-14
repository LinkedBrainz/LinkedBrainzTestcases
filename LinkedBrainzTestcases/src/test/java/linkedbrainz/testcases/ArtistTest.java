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
public class ArtistTest
{

	/**
	 * Fetches 5 music artists from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtists()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUIDSimple(
				"artist", "gid", "mo:MusicArtist", "ArtistsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 solo music artists from the DB and resolves them via a SPARQL
	 * query.
	 * 
	 */
	@Test
	public void checkSoloMusicArtists()
	{
		TestResult testResult = Utils.getInstance()
				.checkClassViaGUIDAndCondition("artist", "gid", "type", "1",
						"mo:SoloMusicArtist", "SoloArtistsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 music groups from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicGroups()
	{
		TestResult testResult = Utils.getInstance()
				.checkClassViaGUIDAndCondition("artist", "gid", "type", "2",
						"mo:MusicGroup", "GroupsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 music artists from the DB and resolves their names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistNames()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("artist", "artist_name",
						"name", "name", "mo:MusicArtist", "foaf:name", "name",
						"ArtistNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 music artists from the DB and resolves their sort labels
	 * against the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistSortLabels()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("artist", "artist_name",
						"sort_name", "name", "mo:MusicArtist", "ov:sortLabel",
						"sortName", "ArtistSortLabelsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists from the DB and resolves their aliases
	 * against the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistAliases()
	{
		// add The Beatles as proof GUID
		TestResult testResult = Utils.getInstance().checkInstanceAliases(
				"artist", "mo:MusicArtist",
				"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d", "ArtistAliasesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 music artists from the DB and resolves their genders against
	 * the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistGender()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("artist", "gender",
						"gender", "name", "mo:MusicArtist", "foaf:gender",
						"gender", "ArtistGenderCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 music artists from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsReleasesRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("artist");
		classTables.add("release");
		classTables.add("artist_credit_name");
		classTables.add("artist_credit");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");

		classNames.add("mo:MusicArtist");
		classNames.add("mo:Release");

		valueNames.add("artistURI");
		valueNames.add("releaseURI");
		valueNames.add("releaseGUID");

		// add Tori Amos as proof GUID
		TestResult testResult = Utils.getInstance().checkURIProperty(
				classTables, classTableRows, classNames, "foaf:made",
				valueNames, "c0b2500e-0cef-4130-869d-732b23ed9df5", "ArtistsReleasesRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ArtistTest.class);
	}
}
