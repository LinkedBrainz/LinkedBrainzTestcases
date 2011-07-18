package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.TestResult;
import linkedbrainz.testcases.model.URICondition;

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
						5, false, "ArtistNamesCheck");

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
						"sortName", 5, false, "ArtistSortLabelsCheck");

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
						"gender", 5, false, "ArtistGenderCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their releases from the DB and resolves
	 * them via a SPARQL query.
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
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables, classTableRows, classNames, "foaf:made",
				valueNames, 3, 5, "c0b2500e-0cef-4130-869d-732b23ed9df5",
				"ArtistsReleasesRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their recordings from the DB and
	 * resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsRecordingsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("artist");
		classTables.add("recording");
		classTables.add("artist_credit_name");
		classTables.add("artist_credit");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");

		classNames.add("mo:MusicArtist");
		classNames.add("mo:Signal");

		valueNames.add("artistURI");
		valueNames.add("recordingURI");
		valueNames.add("recordingGUID");

		// add Tori Amos as proof GUID
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables, classTableRows, classNames, "foaf:made",
				valueNames, 3, 5, "c0b2500e-0cef-4130-869d-732b23ed9df5",
				"ArtistsRecordingsReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their works from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 * ATTENTION: the 'artist_credit' row in the table 'work' is currently not
	 * used and might be removed in the future (that is why it won't deliver
	 * results at the moment)
	 * 
	 */
	@Test
	public void checkMusicArtistsWorksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("artist");
		classTables.add("work");
		classTables.add("artist_credit_name");
		classTables.add("artist_credit");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");

		classNames.add("mo:MusicArtist");
		classNames.add("mo:MusicalWork");

		valueNames.add("artistURI");
		valueNames.add("workURI");
		valueNames.add("workGUID");

		// add Tori Amos as proof GUID
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables, classTableRows, classNames, "foaf:made",
				valueNames, 3, 5, "c0b2500e-0cef-4130-869d-732b23ed9df5",
				"ArtistsWorksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their release groups from the DB and
	 * resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsReleaseGroupsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("artist");
		classTables.add("release_group");
		classTables.add("artist_credit_name");
		classTables.add("artist_credit");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");

		classNames.add("mo:MusicArtist");
		classNames.add("mo:SignalGroup");

		valueNames.add("artistURI");
		valueNames.add("releaseGroupURI");
		valueNames.add("releaseGroupGUID");

		// add Tori Amos as proof GUID
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables, classTableRows, classNames, "foaf:made",
				valueNames, 3, 5, "c0b2500e-0cef-4130-869d-732b23ed9df5",
				"ArtistsReleaseGroupsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their tracks from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsTracksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("artist");
		classTables.add("track");
		classTables.add("artist_credit_name");
		classTables.add("artist_credit");

		classTableRows.add("gid");
		classTableRows.add("id");
		classTableRows.add("artist");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");

		classNames.add("mo:MusicArtist");
		classNames.add("mo:Track");

		valueNames.add("artistURI");
		valueNames.add("trackURI");
		valueNames.add("trackURI");

		// add Tori Amos as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndIDOnTheRight(classTables,
						classTableRows, classNames, "foaf:made", valueNames, 3,
						1, "c0b2500e-0cef-4130-869d-732b23ed9df5",
						"ArtistsTracksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 music artists from the DB and resolves their countries against
	 * the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistCountry()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("artist", "country",
						"country", "iso_code", "mo:MusicArtist",
						"foaf:based_near", "country", 5, true,
						"ArtistsCountryCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their Wikipedia links from the DB and
	 * resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsWikilinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("artist");
		classTables.add("url");
		classTables.add("l_artist_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:MusicArtist");

		valueNames.add("artistURI");
		valueNames.add("wikiURI");
		valueNames.add("wikiURI");

		// add The Beatles as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"owl:sameAs",
						valueNames,
						4,
						5,
						new URICondition("link_type", "id", "179",
								"http://en.wikipedia.org/wiki/",
								"http://dbpedia.org/resource/"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsWikilinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) music artists and their Discogs links from the DB and
	 * resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsDiscogslinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("artist");
		classTables.add("url");
		classTables.add("l_artist_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:MusicArtist");

		valueNames.add("artistURI");
		valueNames.add("discogsURI");
		valueNames.add("discogsURI");

		// add The Beatles as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"owl:sameAs",
						valueNames,
						4,
						5,
						new URICondition("link_type", "id", "180",
								"http://www.discogs.com/artist/",
								"http://discogs.dataincubator.org/artist/"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsTracksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) music artists and their BBC links from the DB and
	 * resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsBBClinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("artist");
		classTables.add("url");
		classTables.add("l_artist_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:MusicArtist");

		valueNames.add("artistURI");
		valueNames.add("bbcURI");
		valueNames.add("bbcURI");

		// add The Beatles as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"owl:sameAs",
						valueNames,
						4,
						5,
						new URICondition("link_type", "id", "190",
								"",
								"", "", "#artist"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsTracksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ArtistTest.class);
	}
}
