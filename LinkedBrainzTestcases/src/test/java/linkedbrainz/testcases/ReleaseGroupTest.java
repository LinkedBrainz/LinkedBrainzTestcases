package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.d2rs.translator.DiscogsTranslator;
import linkedbrainz.d2rs.translator.IBDBTranslator;
import linkedbrainz.d2rs.translator.IMDBTranslator;
import linkedbrainz.d2rs.translator.IOBDBTranslator;
import linkedbrainz.testcases.model.TestResult;
import linkedbrainz.testcases.model.URICondition;

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
						"dct:title", "title", 5, false,
						"ReleaseGroupNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) release groups and their music artists from the DB and
	 * resolves them via a SPARQL query.
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
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDs(classTables, classTableRows,
						classNames, "foaf:maker", valueNames, 3, 5,
						"9f7a4c28-8fa2-3113-929c-c47a9f7982c3",
						"ReleaseGroupsArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) release groups and their Wikipedia links from the DB and
	 * resolves them via a SPARQL query to DBPedia resource URIs.
	 * 
	 */
	@Test
	public void checkReleaseGroupsDBPedialinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release_group");
		classTables.add("url");
		classTables.add("l_release_group_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:SignalGroup");

		valueNames.add("releaseGroupURI");
		valueNames.add("dbpediaURI");
		valueNames.add("dbpediaURI");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" from The Beatles as proof
		// GUID
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
								"'6578f0e9-1ace-4095-9de8-6e517ddb1ceb'",
								"http://wikipedia.org/wiki/",
								"http://dbpedia.org/resource/", "", "",
								"is:info_service", "isi:dbpedia"),
						"9f7a4c28-8fa2-3113-929c-c47a9f7982c3",
						"ReleaseGroupsDBPedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) release groups and their Wikipedia links from the DB and
	 * resolves them via a SPARQL query to cleaned up Wikipedia page URLs.
	 * 
	 */
	@Test
	public void checkReleaseGroupsWikipedialinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release_group");
		classTables.add("url");
		classTables.add("l_release_group_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:SignalGroup");

		valueNames.add("releaseGroupURI");
		valueNames.add("wikiURI");
		valueNames.add("wikiURI");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" from The Beatles as proof
		// GUID
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
								"'6578f0e9-1ace-4095-9de8-6e517ddb1ceb'",
								"http://wikipedia.org/wiki/",
								"http://wikipedia.org/wiki/", "", "",
								"is:info_service", "isi:wikipedia"),
						"9f7a4c28-8fa2-3113-929c-c47a9f7982c3",
						"ReleaseGroupsWikipedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) release groups and their Discogs links from the DB and resolves
	 * them via a SPARQL query to cleaned up Discogs page URLs.
	 * 
	 */
	@Test
	public void checkReleaseGroupsDiscogslinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release_group");
		classTables.add("url");
		classTables.add("l_release_group_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:SignalGroup");

		valueNames.add("releaseGroupURI");
		valueNames.add("discogsURI");
		valueNames.add("discogsURI");

		// add "Chances Are" from Bob Marley as proof
		// GUID
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
								"'99e550f3-5ab4-3110-b5b9-fe01d970b126'",
								DiscogsTranslator.ORIGINAL_BASE_URI,
								DiscogsTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:discogs"),
						"5600398c-c9fa-398a-84df-d8b13b009853",
						"ReleaseGroupsDiscogslinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) release groups and their IBDb links from the DB and resolves
	 * them via a SPARQL query to cleaned up IBDb page URLs.
	 * 
	 */
	@Test
	public void checkReleaseGroupsIBDblinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release_group");
		classTables.add("url");
		classTables.add("l_release_group_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:SignalGroup");

		valueNames.add("releaseGroupURI");
		valueNames.add("ibdbURI");
		valueNames.add("ibdbURI");

		// add "Chicago - A Musical Vaudeville" (a musical) as proof
		// GUID
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
								"'a7f96734-716e-48b8-9040-adc5b3256836'",
								IBDBTranslator.ORIGINAL_BASE_URI,
								IBDBTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:ibdb"),
						"5d192683-17da-34c6-ad6b-6e37e8bd449d",
						"ReleaseGroupsIBDblinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) release groups and their IMDb links from the DB and resolves
	 * them via a SPARQL query to cleaned up IMDb page URLs.
	 * 
	 */
	@Test
	public void checkReleaseGroupsIMDblinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release_group");
		classTables.add("url");
		classTables.add("l_release_group_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:SignalGroup");

		valueNames.add("releaseGroupURI");
		valueNames.add("imdbURI");
		valueNames.add("imdbURI");

		// add "Forrest Gump" (the sound track of Forrest Gump) as proof
		// GUID
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
								"'85b0a010-3237-47c7-8476-6fcefd4761af'",
								IMDBTranslator.ORIGINAL_BASE_URI,
								IMDBTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:imdb"),
						"6d79d579-213d-3172-a860-1cd9bde0ecd5",
						"ReleaseGroupsIMDblinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) release groups and their IOBDb links from the DB and resolves
	 * them via a SPARQL query to cleaned up IOBDb page URLs.
	 * 
	 */
	@Test
	public void checkReleaseGroupsIOBDblinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release_group");
		classTables.add("url");
		classTables.add("l_release_group_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:SignalGroup");

		valueNames.add("releaseGroupURI");
		valueNames.add("iobdbURI");
		valueNames.add("iobdbURI");

		// add "One Mo' Time" (a musical) as proof
		// GUID
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
								"'fd87657e-aa2f-44ad-b5d8-d97c0c938a4d'",
								IOBDBTranslator.ORIGINAL_BASE_URI,
								IOBDBTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:iobdb"),
						"0e888dba-0b67-45a5-a1bf-58598ff90156",
						"ReleaseGroupsIOBDblinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ReleaseGroupTest.class);
	}

}
