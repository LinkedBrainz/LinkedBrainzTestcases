package linkedbrainz.testcases;

import static org.junit.Assert.*;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.TestResult;

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

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(LabelTest.class);
	}

}
