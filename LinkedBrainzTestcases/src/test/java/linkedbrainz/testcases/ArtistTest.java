package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.d2rs.translator.DBPediaTranslator;
import linkedbrainz.d2rs.translator.DBTuneMySpaceTranslator;
import linkedbrainz.d2rs.translator.DiscogsTranslator;
import linkedbrainz.d2rs.translator.MySpaceTranslator;
import linkedbrainz.d2rs.translator.WikipediaTranslator;
import linkedbrainz.d2rs.translator.YouTubeTranslator;
import linkedbrainz.testcases.model.Condition;
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
	 * resolves them via a SPARQL query to DBPedia resource URIs.
	 * 
	 */
	@Test
	public void checkMusicArtistsDBPedialinksRelations()
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
		valueNames.add("dbpediaURI");
		valueNames.add("dbpediaURI");

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
						new URICondition("link_type", "gid",
								"'29651736-fa6d-48e4-aadc-a557c6add1cb'",
								DBPediaTranslator.ORIGINAL_BASE_URI,
								DBPediaTranslator.LINKED_DATA_BASE_URI, "", "",
								"is:info_service", "isi:dbpedia"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsDBPedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their Wikipedia links from the DB and
	 * resolves them via a SPARQL query to cleaned up Wikipedia URLs.
	 * 
	 */
	@Test
	public void checkMusicArtistsWikipedialinksRelations()
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
						"rdfs:seeAlso",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'29651736-fa6d-48e4-aadc-a557c6add1cb'",
								WikipediaTranslator.ORIGINAL_BASE_URI,
								WikipediaTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:wikipedia"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsWikipedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their Discogs links from the DB and
	 * resolves them via a SPARQL query to DataIncubator Discogs resource URIs.
	 * 
	 */
	@Test
	public void checkMusicArtistsDataIncubatorDiscogslinksRelations()
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
		valueNames.add("dataIncubatorDiscogsURI");
		valueNames.add("dataIncubatorDiscogsURI");

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
						new URICondition("link_type", "gid",
								"'04a5b104-a4c2-4bac-99a1-7b837c37d9e4'",
								"http://www.discogs.com/artist/",
								"http://discogs.dataincubator.org/artist/", "",
								"", "is:info_service",
								"isi:dataincubatordiscogs"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsDataIncubatorDiscogslinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their Discogs links from the DB and
	 * resolves them via a SPARQL query to cleaned up Discogs page URLs.
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
						"rdfs:seeAlso",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'04a5b104-a4c2-4bac-99a1-7b837c37d9e4'",
								DiscogsTranslator.ORIGINAL_BASE_URI,
								DiscogsTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:discogs"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsDiscogslinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their BBC links from the DB and resolves
	 * them via a SPARQL query to BBC resource URIs.
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
		TestResult testResult = Utils
				.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"owl:sameAs",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'d028a975-000c-4525-9333-d3c8425e4b54'", "",
								"", "", "#artist", "is:info_service", "isi:bbc"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsBBClinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their homepages from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsHomepagesRelations()
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
		valueNames.add("homepageURI");
		valueNames.add("homepageURI");

		// add The Beatles as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"foaf:homepage",
						valueNames,
						4,
						5,
						new Condition("link_type", "gid",
								"'fe33d22f-c3b0-4d68-bd53-a856badf2b15'"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsHomepagesReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their MySpace links from the DB and
	 * resolves them via a SPARQL query to DBTune MySpace URIs.
	 * 
	 */
	@Test
	public void checkMusicArtistsDBTuneMySpacelinksRelations()
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
		valueNames.add("dbtuneMyspaceURI");
		valueNames.add("dbtuneMyspaceURI");

		// add The Beatles as proof GUID
		TestResult testResult = Utils
				.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"owl:sameAs",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'bac47923-ecde-4b59-822e-d08f0cd10156'",
								DBTuneMySpaceTranslator.ORIGINAL_BASE_URI,
								DBTuneMySpaceTranslator.LINKED_DATA_BASE_URI,
								"", "", "is:info_service", "isi:dbtunemyspace"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsDBTuneMyspacelinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their MySpace links from the DB and
	 * resolves them via a SPARQL query to cleaned up MySpace page URLs.
	 * 
	 */
	@Test
	public void checkMusicArtistsMySpacelinksRelations()
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
		valueNames.add("myspaceURI");
		valueNames.add("myspaceURI");

		// add The Beatles as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"rdfs:seeAlso",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'bac47923-ecde-4b59-822e-d08f0cd10156'",
								MySpaceTranslator.ORIGINAL_BASE_URI,
								MySpaceTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:myspace"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsMyspacelinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) music artists and their YouTube channels from the DB and
	 * resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistsYouTubeChannelsRelations()
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
		valueNames.add("youTubeChannelURI");
		valueNames.add("youTubeChannelURI");

		// add The Beatles as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"rdfs:seeAlso",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'6a540e5b-58c6-4192-b6ba-dbc71ec8fcf0'",
								YouTubeTranslator.ORIGINAL_BASE_URI,
								YouTubeTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:youtube"),
						"b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
						"ArtistsYouTubeChannelsReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ArtistTest.class);
	}
}
