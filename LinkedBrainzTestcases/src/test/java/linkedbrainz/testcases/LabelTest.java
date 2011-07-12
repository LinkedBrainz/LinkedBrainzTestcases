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
		TestResult testResult = Utils.getInstance().checkClass("label",
				"mo:Label", "LabelsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(LabelTest.class);
	}

}
