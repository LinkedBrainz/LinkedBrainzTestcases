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
public class ReleaseTest
{

	/**
	 * Fetches 5 releases from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkReleases()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUID(
				"release", "gid", "mo:Release", "ReleasesCheck");

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
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("release", "release_name",
						"name", "name", "mo:Release", "dct:title", "title",
						"ReleaseNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(ReleaseTest.class);
	}

}
