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
public class LabelTest
{
	/**
	 * Fetches 5 labels from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkLabels()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUIDSimple(
				"label", "gid", "mo:Label", "LabelsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 labels from the DB and resolves their names against the result
	 * of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkLabelNames()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("label");
		classTables.add("label_name");

		classTableRows.add("name");
		classTableRows.add("name");

		// add Columbia Records as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:Label", "foaf:name", "name", 1, 5,
						false, false, "011d1192-6f65-45bd-85c4-0400dd45693e",
						"LabelNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 labels from the DB and resolves their sort labels against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkLabelSortLabels()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("label");
		classTables.add("label_name");

		classTableRows.add("sort_name");
		classTableRows.add("name");

		// add Columbia Records as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:Label", "ov:sortLabel", "sortName",
						1, 5, false, false,
						"011d1192-6f65-45bd-85c4-0400dd45693e",
						"LabelSortLabelsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels from the DB and resolves their aliases against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkLabelAliases()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("label");
		classTables.add("label_alias");
		classTables.add("label_name");

		classTableRows.add("name");
		classTableRows.add("label");
		classTableRows.add("name");

		// add Columbia Records as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:Label", "skos:altLabel", "alias",
						2, 5, false, true,
						"011d1192-6f65-45bd-85c4-0400dd45693e",
						"LabelsAliasesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels and their MySpace links from the DB and resolves
	 * them via a SPARQL query to DBTune MySpace resource URIs.
	 * 
	 */
	@Test
	public void checkLabelsDBTuneMySpacelinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("label");
		classTables.add("url");
		classTables.add("l_label_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Label");

		valueNames.add("labelURI");
		valueNames.add("dbtuneMyspaceURI");
		valueNames.add("dbtuneMyspaceURI");

		// add Universal Music as proof GUID
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
								"'240ba9dc-9898-4505-9bf7-32a53a695612'",
								"is:info_service", "isi:dbtunemyspace",
								"linkedbrainz.d2rs.translator.DBTuneMySpaceTranslator"),
						"19d052fa-570a-4b17-9a3d-8f2f029b7b57",
						"LabelsDBTuneMyspacelinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels and their MySpace links from the DB and resolves
	 * them via a SPARQL query to cleaned up MySpace page URLs.
	 * 
	 */
	@Test
	public void checkLabelsMySpacelinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("label");
		classTables.add("url");
		classTables.add("l_label_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Label");

		valueNames.add("labelURI");
		valueNames.add("myspaceURI");
		valueNames.add("myspaceURI");

		// add Universal Music as proof GUID
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
								"'240ba9dc-9898-4505-9bf7-32a53a695612'",
								"is:info_service", "isi:myspace",
								"linkedbrainz.d2rs.translator.MySpaceTranslator"),
						"19d052fa-570a-4b17-9a3d-8f2f029b7b57",
						"LabelsMyspacelinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels and their Wikipedia links from the DB and resolves
	 * them via a SPARQL query to DBPedia resource URIs.
	 * 
	 */
	@Test
	public void checkLabelsDBPedialinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("label");
		classTables.add("url");
		classTables.add("l_label_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Label");

		valueNames.add("labelURI");
		valueNames.add("dbpediaURI");
		valueNames.add("dbpediaURI");

		// add Columbia Records as proof GUID
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
								"'51e9db21-8864-49b3-aa58-470d7b81fa50'",
								"is:info_service", "isi:dbpedia",
								"linkedbrainz.d2rs.translator.DBPediaTranslator"),
						"011d1192-6f65-45bd-85c4-0400dd45693e",
						"LabelsDBPedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels and their Wikipedia links from the DB and resolves
	 * them via a SPARQL query to cleaned up Wikipedia page URLs.
	 * 
	 */
	@Test
	public void checkLabelsWikipedialinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("label");
		classTables.add("url");
		classTables.add("l_label_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Label");

		valueNames.add("labelURI");
		valueNames.add("wikiURI");
		valueNames.add("wikiURI");

		// add Columbia Records as proof GUID
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
								"'51e9db21-8864-49b3-aa58-470d7b81fa50'",
								"is:info_service", "isi:wikipedia",
								"linkedbrainz.d2rs.translator.WikipediaTranslator"),
						"011d1192-6f65-45bd-85c4-0400dd45693e",
						"LabelsWikipedialinksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels and their YouTube channels from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkLabelsYouTubeChannelsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("label");
		classTables.add("url");
		classTables.add("l_label_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Label");

		valueNames.add("labelURI");
		valueNames.add("youTubeChannelURI");
		valueNames.add("youTubeChannelURI");

		// add Mute Records as proof GUID
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
								"'d9c71059-ba9d-4135-b909-481d12cf84e3'",
								"is:info_service", "isi:youtube",
								"linkedbrainz.d2rs.translator.YouTubeTranslator"),
						"e0b106a5-4add-4839-9e40-c192457e1bf8",
						"LabelsYouTubeChannelsReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels and their Discogs links from the DB and resolves
	 * them via a SPARQL query to cleaned up Discogs page URLs.
	 * 
	 */
	@Test
	public void checkLabelsDiscogslinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("label");
		classTables.add("url");
		classTables.add("l_label_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Label");

		valueNames.add("labelURI");
		valueNames.add("discogsURI");
		valueNames.add("discogsURI");

		// add Mute Records as proof GUID
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
								"'5b987f87-25bc-4a2d-b3f1-3618795b8207'",
								"is:info_service", "isi:discogs",
								"linkedbrainz.d2rs.translator.DiscogsTranslator"),
						"e0b106a5-4add-4839-9e40-c192457e1bf8",
						"LabelsDiscogslinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels and their VGMdb links from the DB and resolves them
	 * via a SPARQL query to cleaned up VGMdb page URLs.
	 * 
	 */
	@Test
	public void checkLabelsVGMdblinksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("label");
		classTables.add("url");
		classTables.add("l_label_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Label");

		valueNames.add("labelURI");
		valueNames.add("vgmdbURI");
		valueNames.add("vgmdbURI");

		// add TOY'S FACTORY as proof GUID
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
								"'8a2d3e55-d291-4b99-87a0-c59c6b121762'",
								"is:info_service", "isi:vgmdb",
								"linkedbrainz.d2rs.translator.VGMDBTranslator"),
						"5f7aa61d-cf77-4c2a-9a43-41682508dccd",
						"LabelsVGMDBlinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) labels and their homepages from the DB and resolves them
	 * via a SPARQL query.
	 * 
	 */
	@Test
	public void checkLabelsHomepagesRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("label");
		classTables.add("url");
		classTables.add("l_label_url");
		classTables.add("link");
		classTables.add("link_type");

		classTableRows.add("gid");
		classTableRows.add("url");
		classTableRows.add("entity0");
		classTableRows.add("link");
		classTableRows.add("link_type");
		classTableRows.add("entity1");

		classNames.add("mo:Label");

		valueNames.add("labelURI");
		valueNames.add("homepageURI");
		valueNames.add("homepageURI");

		// add Warp Records as proof GUID
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
								"'fe108f43-acb9-4ad1-8be3-57e6ec5b17b6'"),
						"46f0f4cd-8aab-4b33-b698-f459faf64190",
						"LabelsHomepagesReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(LabelTest.class);
	}

}
