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
public class WorkTest
{
	/**
	 * Fetches 5 works from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkWorks()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUID("work",
				"gid", "mo:MusicalWork", "WorksCheck");

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
		// add
		// "Il dissoluto punito, ossia il Don Giovanni, K. 527: Act I, Scene VII. No. 5 Coro "Giovinette,
		// che fate all'amore" (Zerlina, Coro)" as proof GUID
		TestResult testResult = Utils.getInstance().checkInstanceAliases(
				"work", "mo:MusicalWork",
				"00955628-ace0-3873-9ef2-e0e66b203fc3", "WorkAliasesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(WorkTest.class);
	}

}
