package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.TestResult;

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
		TestResult testResult = Utils.getInstance().checkClassViaGUID(
				"release_group", "gid", "mo:SignalGroup", "ReleaseGroupsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}
	
	/**
	 * Fetches 5 release groups from the DB and resolves theirs names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkReleaseGroupNames()
	{
		TestResult testResult = Utils.getInstance().checkInstanceNamesViaGUID(
				"release_group", "release_name", "mo:SignalGroup", "dct:title",
				"ReleaseGroupNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ReleaseGroupTest.class);
	}

}
