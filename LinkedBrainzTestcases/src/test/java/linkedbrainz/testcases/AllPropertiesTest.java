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
public class AllPropertiesTest
{

	/**
	 * Tries to fetch all available properties in a dataset.
	 */
	/*@Test
	public void checkPropertyAvailability()
	{

		String allPropertiesQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?property " + "WHERE { [] ?property [] ."
				+ "LIMIT 100 } "
				+ "ORDER BY ?property ";
		SPARQLResultSet result = null;

		try
		{
			result = Utils.getInstance().runSPARQLQuery(allPropertiesQuery,
					Utils.SERVICE_ENDPOINT);

			assertNotNull(result.getResultSet());

			result.close();
		} catch (NullPointerException e)
		{
			System.out.println(e.getMessage());

			fail("AllPropertiesTest failed.");
		}
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(AllPropertiesTest.class);
	}*/

}
