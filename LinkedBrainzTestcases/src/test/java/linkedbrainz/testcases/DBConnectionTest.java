package linkedbrainz.testcases;

import static org.junit.Assert.*;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import java.sql.Connection;

/**
 * 
 * @author zazi
 * 
 */
public class DBConnectionTest
{

	/**
	 * Checks the connection to a PostgreSQL database.
	 */
	@Test
	public void checkDBConnectionTest()
	{
		Connection connection = null;

		try
		{
			connection = Utils.getInstance().getDBConnection();

			assertEquals(connection.getMetaData().getDatabaseProductName(),
					"PostgreSQL");
		} catch (Exception e)
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
