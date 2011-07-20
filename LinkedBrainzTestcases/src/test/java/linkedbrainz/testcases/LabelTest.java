package linkedbrainz.testcases;

import static org.junit.Assert.*;

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
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("label", "label_name",
						"name", "name", "mo:Label", "foaf:name", "name", 5,
						false, "LabelNamesCheck");

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
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("label", "label_name",
						"sort_name", "name", "mo:Label", "ov:sortLabel",
						"sortName", 5, false, "LabelSortLabelsCheck");

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
		// add Columbia Records as proof GUID
		TestResult testResult = Utils.getInstance().checkInstanceAliases(
				"label", "mo:Label", "011d1192-6f65-45bd-85c4-0400dd45693e",
				"LabelAliasesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}
	
	/**
	 * Fetches 5 (+1) labels and their MySpace links from the DB and
	 * resolves them via a SPARQL query.
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
								"'240ba9dc-9898-4505-9bf7-32a53a695612'",
								"http://www.myspace.com/",
								"http://dbtune.org/myspace/", "", "",
								"is:info_service", "isi:dbtunemyspace"),
						"19d052fa-570a-4b17-9a3d-8f2f029b7b57",
						"LabelsMyspacelinksReleationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(LabelTest.class);
	}

}
