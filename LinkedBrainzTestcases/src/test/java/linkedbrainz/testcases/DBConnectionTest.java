package linkedbrainz.testcases;

import static org.junit.Assert.*;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import java.sql.Connection;

public class DBConnectionTest 
{
	
	@Test public void checkDBConnectionTest()
	{
		Connection connection = null;
		
		try
		{
			connection = Utils.getDBConnection();
			
			assertEquals(connection.getMetaData().getDatabaseProductName(), "PostgreSQL");
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			
			assertTrue(false);
		}
		
	}
	
	public static junit.framework.Test suite() 
	{ 
	    return new JUnit4TestAdapter(DBConnectionTest.class); 
	}
}
