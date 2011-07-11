package linkedbrainz.testcases;

import static org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.SPARQLResultSet;

import org.junit.Test;

public class AllClassesTest {
	
	@Test public void checkClassAvailability() 
	{
	
		String allClassesQuery = Utils.DEFAULT_PREFIXES +
								 "SELECT DISTINCT ?class " +
								 "WHERE { [] a ?class } " +
								 "ORDER BY ?class ";
		SPARQLResultSet result = null;
		
		try
		{
			result = Utils.runSPARQLQuery(allClassesQuery, Utils.SERVICE_ENDPOINT);
			
			assertNotNull(result.getResultSet());
		}
		catch (NullPointerException e)
		{
			System.out.println(e.getMessage());
			
			assertTrue(false);
		}
	}
	
	public static junit.framework.Test suite() 
	{ 
	    return new JUnit4TestAdapter(AllClassesTest.class); 
	}

}
