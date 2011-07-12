package linkedbrainz.testcases;

import static org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.SPARQLResultSet;

import org.junit.Test;

/**
 * 
 * @author zazi
 * 
 */
public class AllClassesTest
{

	/**
	 * Tries to fetch all available classes in a dataset.
	 */
	@Test
	public void checkClassAvailability()
	{

		String allClassesQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?class " + "WHERE { [] a ?class } "
				+ "ORDER BY ?class ";
		SPARQLResultSet result = null;

		try
		{
			result = Utils.getInstance().runSPARQLQuery(allClassesQuery,
					Utils.SERVICE_ENDPOINT);

			assertNotNull(result.getResultSet());

			result.close();
		} catch (NullPointerException e)
		{
			System.out.println(e.getMessage());

			fail("AllClassesTest failed.");
		}
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(AllClassesTest.class);
	}

}
