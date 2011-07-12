package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.SPARQLResultSet;
import linkedbrainz.testcases.model.SQLResultSet;

import org.junit.Test;

/**
 * 
 * @author zazi
 *
 */
public class ArtistTest
{
	
	/**
	 * Fetches 5 music artist from the DB and resolves them via a SPARQL query. 
	 * 
	 */
	@Test public void checkMusicArtists()
	{
		String sqlQuery = "SELECT gid FROM musicbrainz.artist LIMIT 5";
		SQLResultSet sqlResultSet = null;
		
		List<String> gids = null;
		
		String sparqlQuery = Utils.DEFAULT_PREFIXES +
							"SELECT DISTINCT ?artistURI " +
							"WHERE { " +
							"?artistURI rdf:type mo:MusicArtist . " +
							"?artistURI mo:musicbrainz_guid \"GUID_PLACEHOLDER\"^^xsd:string . } ";
		String currentSparqlQuery = null;
		SPARQLResultSet sparqlResultSet = null;
		com.hp.hpl.jena.query.ResultSet sparqlRS = null;
		int queryCounter = 0;
		
		try
		{
			sqlResultSet = Utils.getInstance().runSQLQuery(sqlQuery);
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
			
			fail("MusicArtistsCheck failed due to a SQLException");
		}
		
		if(sqlResultSet != null)
		{
			java.sql.ResultSet sqlRS = sqlResultSet.getResultSet();
			gids = new ArrayList<String>();
			
			try
			{
				while(sqlRS.next())
				{
					gids.add(sqlRS.getString("gid"));
				}
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
				
				fail("MusicArtistsCheck failed due to a SQLException");
			}
			
			sqlResultSet.close();
		}
		
		if(gids.size() == 5)
		{
			for( int i = 0; i < 5; i++)
			{
				sparqlRS = null;
			
				currentSparqlQuery = sparqlQuery.replace("GUID_PLACEHOLDER", gids.get(i));
				
				try
				{
					sparqlResultSet = Utils.getInstance().runSPARQLQuery(currentSparqlQuery, Utils.SERVICE_ENDPOINT);
					
					sparqlRS = sparqlResultSet.getResultSet();
					
					while(sparqlRS.hasNext())
					{
						queryCounter++;
						sparqlRS.next();
					}
				} catch (Exception e)
				{
					System.out.println(e.getMessage());
					
					fail("MusicArtistsCheck failed due to a SPARQL query execution");
				}
				
				sparqlResultSet.close();
			}
		}
		
		assertTrue(queryCounter == 5);
			
	}
	
	
	public static junit.framework.Test suite() 
	{ 
	    return new JUnit4TestAdapter(ArtistTest.class); 
	}

}
