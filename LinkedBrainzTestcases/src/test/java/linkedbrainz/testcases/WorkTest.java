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
	 * Fetches 5 works from the DB and resolves their names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkWorkNames()
	{
		TestResult testResult = Utils.getInstance().checkInstanceNamesViaGUID(
				"work", "name", "work_name", "mo:MusicalWork", "dct:title",
				"WorkNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(WorkTest.class);
	}

}
