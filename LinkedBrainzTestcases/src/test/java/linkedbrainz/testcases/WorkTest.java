package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.Condition;
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
	 * Fetches 5 compositions from the DB and resolves it via a SPARQL query.
	 * 
	 */
	@Test
	public void checkCompositions()
	{
		TestResult testResult = Utils.getInstance()
				.checkClassViaGUIDOrIDAndFragmentId("work", "gid",
						"mo:Composition", "#composition", 5,
						"CompositionsCheck");

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
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("work");
		classTables.add("work_name");

		classTableRows.add("name");
		classTableRows.add("name");

		// add
		// "Il dissoluto punito, ossia il Don Giovanni, K. 527: Act I, Scene VII. No. 5 Coro "Giovinette,
		// che fate all'amore" (Zerlina, Coro)" as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:MusicalWork", "dct:title", "title",
						1, 5, false, false, null,
						"00955628-ace0-3873-9ef2-e0e66b203fc3",
						"WorkNamesCheck");

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
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("work");
		classTables.add("work_alias");
		classTables.add("work_name");

		classTableRows.add("name");
		classTableRows.add("work");
		classTableRows.add("name");

		// add
		// "Il dissoluto punito, ossia il Don Giovanni, K. 527: Act I, Scene VII. No. 5 Coro "Giovinette,
		// che fate all'amore" (Zerlina, Coro)" as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:MusicalWork", "skos:altLabel",
						"alias", 2, 5, false, true, null,
						"00955628-ace0-3873-9ef2-e0e66b203fc3",
						"LabelsAliasesCheck");

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

		classNames.add("mo:Composition");
		classNames.add("mo:MusicArtist");

		valueNames.add("workURI");
		valueNames.add("artistURI");
		valueNames.add("artistGUID");

		// add
		// "Il dissoluto punito, ossia il Don Giovanni, K. 527: Act I, Scene VII. No. 5 Coro "Giovinette,
		// che fate all'amore" (Zerlina, Coro)" as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDs(classTables, classTableRows,
						classNames, "foaf:maker", valueNames, null, 3, 5,
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
		classTableRows.add("entity1");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity0");

		classNames.add("mo:MusicalWork");

		valueNames.add("workURI");
		valueNames.add("dbpediaURI");
		valueNames.add("dbpediaURI");

		// add "Symphony No. 9" from Antonín Dvořák as proof GUID
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
								"'b45a88d6-851e-4a6e-9ec8-9a5f4ebe76ab'", true,
								"is:info_service", "isi:dbpedia",
								"linkedbrainz.d2rs.translator.DBPediaTranslator"),
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
		classTableRows.add("entity1");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity0");

		classNames.add("mo:MusicalWork");

		valueNames.add("workURI");
		valueNames.add("wikipediaURI");
		valueNames.add("wikipediaURI");

		// add "Symphony No. 9" from Antonín Dvořák as proof GUID
		TestResult testResult = Utils
				.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"rdfs:seeAlso",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'b45a88d6-851e-4a6e-9ec8-9a5f4ebe76ab'", true,
								"is:info_service", "isi:wikipedia",
								"linkedbrainz.d2rs.translator.WikipediaTranslator"),
						"aacb1ab0-c740-436a-a782-ed60026cf82b",
						"WorksWikipedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) works and their IBDb links from the DB and resolves them
	 * via a SPARQL query to IBDb page URLs.
	 * 
	 * ATTENTION: this test fails currently for some reason. The proof id should
	 * deliver one result. However, the SPARQL query delivers no result. The
	 * description of the related resource shows the correct mapping via the
	 * SNORQL interface, see
	 * http://localhost:2020/snorql/?describe=http%3A%2F%2F
	 * localhost%3A2020%2Fwork%2Faa8b1e50-d265-301a-8bd9-3336ac4f779c%23_
	 */
	@Test
	public void checkWorksIBDblinksRelations()
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
		classTableRows.add("entity1");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity0");

		classNames.add("mo:MusicalWork");

		valueNames.add("workURI");
		valueNames.add("ibdbURI");
		valueNames.add("ibdbURI");

		// add "West Side Story" (a musical) as proof GUID
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
								"'206cf8e2-0a7c-4c17-b8bb-75722d9b9c6c'", true,
								"is:info_service", "isi:ibdb",
								"linkedbrainz.d2rs.translator.IBDBTranslator"),
						"aa8b1e50-d265-301a-8bd9-3336ac4f779c",
						"WorksIBDblinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) works and their IOBDb links from the DB and resolves them
	 * via a SPARQL query to IOBDb page URLs.
	 * 
	 */
	@Test
	public void checkWorksIOBDblinksRelations()
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
		classTableRows.add("entity1");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity0");

		classNames.add("mo:MusicalWork");

		valueNames.add("workURI");
		valueNames.add("iobdbURI");
		valueNames.add("iobdbURI");

		// add "Sweeney Todd, The Demon Barber of Fleet Street" (a musical) as
		// proof GUID
		TestResult testResult = Utils
				.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"rdfs:seeAlso",
						valueNames,
						4,
						5,
						new URICondition("link_type", "gid",
								"'8845d830-fe9b-4ed6-a084-b1a3f193167a'", true,
								"is:info_service", "isi:iobdb",
								"linkedbrainz.d2rs.translator.IOBDBTranslator"),
						"8fce3022-510f-3e21-bab6-5304595e2c5b",
						"WorksIOBDblinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) compositions and musical works from the DB (currently they
	 * are derived from the same table) and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkCompositionsWorksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("work");
		classTables.add("composition");

		classTableRows.add("gid");

		classNames.add("mo:Composition");
		classNames.add("mo:MusicalWork");

		valueNames.add("compositionURI");
		valueNames.add("workURI");
		valueNames.add("workGUID");

		// add "Symphony No. 9" from Antonín Dvořák as proof GUID
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables, classTableRows, classNames, "mo:produced_work",
				valueNames, "#composition", null, 0, 5, null,
				"aacb1ab0-c740-436a-a782-ed60026cf82b",
				"CompositionsWorksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) musical works and compositions from the DB (currently they
	 * are derived from the same table) and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkWorksCompositionsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("work");
		classTables.add("composition");

		classTableRows.add("gid");

		classNames.add("mo:MusicalWork");
		classNames.add("mo:Composition");

		valueNames.add("workURI");
		valueNames.add("compositionURI");
		valueNames.add("compositionURI");

		// add "Symphony No. 9" from Antonín Dvořák as proof GUID
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables, classTableRows, classNames, "mo:composed_in",
				valueNames, null, "#composition", 0, 5, null,
				"aacb1ab0-c740-436a-a782-ed60026cf82b",
				"WorksCompositionsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) compositions and their composers from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkCompositionsComposersRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("work");
		classTables.add("artist");
		classTables.add("l_artist_work");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("entity1");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity0");

		classNames.add("mo:Composition");
		classNames.add("mo:MusicArtist");

		valueNames.add("compositionURI");
		valueNames.add("composerURI");
		valueNames.add("composerGUID");

		// add "Symphony No. 9" from Antonín Dvořák as proof GUID
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables,
				classTableRows,
				classNames,
				"mo:composer",
				valueNames,
				"#composition",
				null,
				4,
				5,
				new Condition("link_type", "gid",
						"'d59d99ea-23d4-4a80-b066-edca32ee158f'", true),
				"aacb1ab0-c740-436a-a782-ed60026cf82b",
				"CompositionsArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 works from the DB and resolves their ISWC codes against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkWORKsISWCs()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("work");

		classTableRows.add("iswc");

		// add "Symphony No. 9" from Antonín Dvořák as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:MusicalWork", "mo:iswc", "ISWC", 0,
						5, false, false,
						new Condition("work", "iswc", "IS NOT NULL", false),
						"'d59d99ea-23d4-4a80-b066-edca32ee158f'",
						"WorksISWCsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(WorkTest.class);
	}

}
