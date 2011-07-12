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
		TestResult testResult = Utils.getInstance().checkClass("release_group",
				"mo:SignalGroup", "ReleaseGroupsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ReleaseGroupTest.class);
	}

}
