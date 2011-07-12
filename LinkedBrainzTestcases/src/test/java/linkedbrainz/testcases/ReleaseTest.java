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
		TestResult testResult = Utils.getInstance().checkClass("release", "mo:Release", "ReleasesCheck");
		
		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());			
	}
	
	public static junit.framework.Test suite() 
	{ 
	    return new JUnit4TestAdapter(ReleaseTest.class); 
	}

}
