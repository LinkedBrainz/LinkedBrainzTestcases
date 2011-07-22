package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.d2rs.translator.DBPediaTranslator;
import linkedbrainz.d2rs.translator.WikipediaTranslator;
import linkedbrainz.testcases.model.TestResult;
import linkedbrainz.testcases.model.URICondition;

import org.junit.Test;

/**
 * 
 * @author zazi
 * 
 */
public class WorkTest
{
	/**
	 * Fetches 5 works from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkWorks()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUIDSimple(
				"work", "gid", "mo:MusicalWork", "WorksCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 works from the DB and resolves their names against the result
	 * of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkWorkNames()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("work", "work_name",
						"name", "name", "mo:MusicalWork", "dct:title", "title",
						5, false, "WorkNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) works from the DB and resolves their aliases against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkWorkAliases()
	{
		// add
		// "Il dissoluto punito, ossia il Don Giovanni, K. 527: Act I, Scene VII. No. 5 Coro "Giovinette,
		// che fate all'amore" (Zerlina, Coro)" as proof GUID
		TestResult testResult = Utils.getInstance().checkInstanceAliases(
				"work", "mo:MusicalWork",
				"00955628-ace0-3873-9ef2-e0e66b203fc3", "WorkAliasesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) works and their music artists from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 * ATTENTION: the 'artist_credit' row in the table 'work' is currently not
	 * used and might be removed in the future (that is why it won't deliver
	 * results at the moment)
	 * 
	 */
	@Test
	public void checkWorkMusicArtistsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("work");
		classTables.add("artist");
		classTables.add("artist_credit");
		classTables.add("artist_credit_name");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");
		classTableRows.add("artist");

		classNames.add("mo:MusicalWork");
		classNames.add("mo:MusicArtist");

		valueNames.add("workURI");
		valueNames.add("artistURI");
		valueNames.add("artistGUID");

		// add
		// "Il dissoluto punito, ossia il Don Giovanni, K. 527: Act I, Scene VII. No. 5 Coro "Giovinette,
		// che fate all'amore" (Zerlina, Coro)" as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDs(classTables, classTableRows,
						classNames, "foaf:maker", valueNames, 3, 5,
						"00955628-ace0-3873-9ef2-e0e66b203fc3",
						"WorksArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) works and their Wikipedia links from the DB and resolves
	 * them via a SPARQL query to DBPedia resource URIs.
	 * 
	 * ATTENTION: this test fails currently for some reason. The proof id should
	 * deliver two results. However, the SPARQL query delivers only one result.
	 * The rather equal Wikipedia query works so far. The description of the
	 * related resource shows the correct mapping via the SNORQL interface, see
	 * http
	 * ://localhost:2020/snorql/?describe=http%3A%2F%2Flocalhost%3A2020%2Fwork
	 * %2Faacb1ab0-c740-436a-a782-ed60026cf82b%23_
	 * 
	 */
	@Test
	public void checkWorksDBPedialinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("work");
		classTables.add("url");
		classTables.add("l_url_work");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:MusicalWork");

		valueNames.add("workURI");
		valueNames.add("dbpediaURI");
		valueNames.add("dbpediaURI");

		// add "Symphony No. 9" from Antonín Dvořák as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"owl:sameAs",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'b45a88d6-851e-4a6e-9ec8-9a5f4ebe76ab'",
								DBPediaTranslator.ORIGINAL_BASE_URI,
								DBPediaTranslator.LINKED_DATA_BASE_URI, "", "",
								"is:info_service", "isi:dbpedia"),
						"aacb1ab0-c740-436a-a782-ed60026cf82b",
						"WorksDBPedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) works and their Wikipedia links from the DB and resolves
	 * them via a SPARQL query to Wikipedia page URLs.
	 * 
	 */
	@Test
	public void checkWorksWikipedialinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("work");
		classTables.add("url");
		classTables.add("l_url_work");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:MusicalWork");

		valueNames.add("workURI");
		valueNames.add("wikipediaURI");
		valueNames.add("wikipediaURI");

		// add "Symphony No. 9" from Antonín Dvořák as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"rdfs:seeAlso",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'b45a88d6-851e-4a6e-9ec8-9a5f4ebe76ab'",
								WikipediaTranslator.ORIGINAL_BASE_URI,
								WikipediaTranslator.ORIGINAL_BASE_URI, "", "",
								"is:info_service", "isi:wikipedia"),
						"aacb1ab0-c740-436a-a782-ed60026cf82b",
						"WorksWikipedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(WorkTest.class);
	}

}
