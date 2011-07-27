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
	 * Fetches 5 release events from the DB and resolves it via a SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseEvents()
	{
		TestResult testResult = Utils.getInstance()
				.checkClassViaGUIDOrIDAndFragmentId("release", "gid",
						"mo:ReleaseEvent", "#event", 5, "ReleaseEventsCheck");

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
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("release");
		classTables.add("release_name");

		classTableRows.add("name");
		classTableRows.add("name");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The
		// Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:Release", "dct:title", "title", 1,
						5, false, false,
						"44b7cab1-0ce1-404e-9089-b458eb3fa530",
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

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The
		// Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDs(classTables, classTableRows,
						classNames, "foaf:maker", valueNames, null, 3, 5,
						"44b7cab1-0ce1-404e-9089-b458eb3fa530",
						"ReleasesArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 releases from the DB and resolves their countries against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkReleasesCountry()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("release");
		classTables.add("country");

		classTableRows.add("country");
		classTableRows.add("iso_code");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The
		// Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeftWithFragment(classTables,
						classTableRows, "mo:ReleaseEvent", "event:place",
						"country", "#event", 5, true, false,
						"44b7cab1-0ce1-404e-9089-b458eb3fa530",
						"ReleasesCountryCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) releases and their labels from the DB and resolves them
	 * via a SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseLabelsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release");
		classTables.add("label");
		classTables.add("release_label");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("release");
		classTableRows.add("label");

		classNames.add("mo:Release");
		classNames.add("mo:Label");

		valueNames.add("releaseURI");
		valueNames.add("labelURI");
		valueNames.add("labelGUID");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The
		// Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables, classTableRows, classNames, "mo:label",
				valueNames, null, null, 2, 5, null,
				"44b7cab1-0ce1-404e-9089-b458eb3fa530",
				"ReleasesLabelsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) releases and mediums (records) from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseMediumsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release");
		classTables.add("medium");

		classTableRows.add("gid");
		classTableRows.add("id");
		classTableRows.add("release");

		classNames.add("mo:Release");
		classNames.add("mo:Record");

		valueNames.add("releaseURI");
		valueNames.add("recordURI");
		valueNames.add("recordURI");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The
		// Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDOnTheLeftAndIDOnTheRight(
						classTables, classTableRows, classNames, "mo:record",
						valueNames, "#_", 1, 5,
						"44b7cab1-0ce1-404e-9089-b458eb3fa530",
						"ReleasesMediumsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) releases and their Discogs links from the DB and resolves
	 * them via a SPARQL query to cleaned up Discogs page URLs.
	 * 
	 */
	@Test
	public void checkReleasesDiscogslinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release");
		classTables.add("url");
		classTables.add("l_release_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Release");

		valueNames.add("releaseURI");
		valueNames.add("discogsURI");
		valueNames.add("discogsURI");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The
		// Beatles as proof
		// GUID
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
								"'4a78823c-1c53-4176-a5f3-58026c76f2bc'",
								"is:info_service", "isi:discogs",
								"linkedbrainz.d2rs.translator.DiscogsTranslator"),
						"44b7cab1-0ce1-404e-9089-b458eb3fa530",
						"ReleasesDiscogslinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) releases and their VGMdb links from the DB and resolves
	 * them via a SPARQL query to cleaned up VGMdb page URLs.
	 * 
	 */
	@Test
	public void checkReleasesVGMdblinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release");
		classTables.add("url");
		classTables.add("l_release_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Release");

		valueNames.add("releaseURI");
		valueNames.add("vgmdbURI");
		valueNames.add("vgmdbURI");

		// add "THE END OF EVANGELION" (a soundtrack) as proof
		// GUID
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
								"'6af0134a-df6a-425a-96e2-895f9cd342ba'",
								"is:info_service", "isi:vgmdb",
								"linkedbrainz.d2rs.translator.VGMDBTranslator"),
						"2f6dfc7c-5ead-45aa-ae71-44e830de88da",
						"ReleasesVGMDBlinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) release events and releases from the DB (currently they
	 * are derived from the same table) and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseEventsReleasesRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release");
		classTables.add("release_event");

		classTableRows.add("gid");

		classNames.add("mo:ReleaseEvent");
		classNames.add("mo:Release");

		valueNames.add("releaseEventURI");
		valueNames.add("releaseURI");
		valueNames.add("releaseGUID");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The
		// Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance().checkURIPropertyViaGUIDs(
				classTables, classTableRows, classNames, "mo:release",
				valueNames, "#event", null, 0, 5, null,
				"44b7cab1-0ce1-404e-9089-b458eb3fa530",
				"ReleaseEventsReleasesRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) releases from the DB and resolves their release states
	 * against the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkReleasesReleaseStates()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("release");
		classTables.add("release_status");

		classTableRows.add("gid");
		classTableRows.add("id");
		classTableRows.add("status");

		classNames.add("mo:Release");

		valueNames.add("releaseURI");
		valueNames.add("releaseStatusURI");
		valueNames.add("releaseStatusURI");

		// add "Sgt. Pepper’s Lonely Hearts Club Band" (PMC 7027) from The
		// Beatles as proof
		// GUID
		TestResult testResult = Utils
				.getInstance()
				.checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"mo:release_status",
						valueNames,
						1,
						5,
						new Condition(
								"linkedbrainz.d2rs.translator.ReleaseStatusTranslator"),
						"44b7cab1-0ce1-404e-9089-b458eb3fa530",
						"ReleasesReleaseStatesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ReleaseTest.class);
	}

}
