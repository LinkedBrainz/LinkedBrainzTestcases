package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.SPARQLResultSet;
import linkedbrainz.testcases.model.SQLResultSet;
import linkedbrainz.testcases.model.TestResult;

import org.junit.Test;

/**
 * 
 * @author zazi
 * 
 */
public class ArtistTest
{

	/**
	 * Fetches 5 music artists from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtists()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUID("artist",
				"gid", "mo:MusicArtist", "ArtistsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 music artists from the DB and resolves theirs names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMusicArtistNames()
	{
		String sqlQuery = "SELECT musicbrainz.artist.id AS artist_id, "
				+ "musicbrainz.artist_name.id AS artist_name_id, "
				+ "musicbrainz.artist_name.name AS name, "
				+ "musicbrainz.artist.gid AS gid "
				+ "FROM musicbrainz.artist "
				+ "INNER JOIN musicbrainz.artist_name  ON artist.name = artist_name.id LIMIT 5";
		SQLResultSet sqlResultSet = null;
		String sqlFailMsg = "MusicArtistNamesCheck failed due to a SQLException";
		Map<String, String> artistNames = null;

		String sparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI ?name "
				+ "WHERE { ?URI a mo:MusicArtist ; "
				+ "mo:musicbrainz_guid \"GUID_PLACEHOLDER\"^^xsd:string ; "
				+ "foaf:name ?name . }";
		String currentSparqlQuery = null;
		SPARQLResultSet sparqlResultSet = null;
		com.hp.hpl.jena.query.ResultSet sparqlRS = null;
		int queryCounter = 0;
		String sparqlFailMsg = "CHECK_NAME failed due to a SPARQL query execution";
		String artistNamesKey = null;

		try
		{
			sqlResultSet = Utils.getInstance().runSQLQuery(sqlQuery);
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());

			fail(sqlFailMsg);
		}

		if (sqlResultSet != null)
		{
			java.sql.ResultSet sqlRS = sqlResultSet.getResultSet();
			artistNames = new HashMap<String, String>();

			try
			{
				while (sqlRS.next())
				{
					artistNames.put(sqlRS.getString("gid"), sqlRS
							.getString("name"));
				}
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());

				fail(sqlFailMsg);
			}

			sqlResultSet.close();
		}

		if (artistNames.size() == 5)
		{
			Iterator<String> iter = artistNames.keySet().iterator();

			for (int i = 0; i < 5; i++)
			{
				sparqlRS = null;
				artistNamesKey = iter.next();

				currentSparqlQuery = sparqlQuery.replace("GUID_PLACEHOLDER",
						artistNamesKey);

				try
				{
					sparqlResultSet = Utils.getInstance().runSPARQLQuery(
							currentSparqlQuery, Utils.SERVICE_ENDPOINT);

					sparqlRS = sparqlResultSet.getResultSet();

					while (sparqlRS.hasNext())
					{
						if (artistNames.get(artistNamesKey).equals(
								sparqlRS.next().getLiteral("name").getString()))
						{
							queryCounter++;
						}
					}
				} catch (Exception e)
				{
					System.out.println(e.getMessage());

					fail(sparqlFailMsg);
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
