package linkedbrainz.testcases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import linkedbrainz.testcases.model.SPARQLResultSet;
import linkedbrainz.testcases.model.SQLResultSet;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;

public class Utils
{

	public static final String SERVICE_ENDPOINT = "http://localhost:2020/sparql";
	public static final String DEFAULT_PREFIXES = "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
			+ "PREFIX ov: <http://open.vocab.org/terms/>"
			+ "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>"
			+ "PREFIX mo: <http://purl.org/ontology/mo/>"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
			+ "PREFIX mbz: <http://test.musicbrainz.org/>"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+ "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>"
			+ "PREFIX dct: <http://purl.org/dc/terms/>"
			+ "PREFIX d2r: <http://sites.wiwiss.fu-berlin.de/suhl/bizer/d2r-server/config.rdf#>"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
			+ "PREFIX map: <file:/home/kurtjx/srcs/d2r-server-0.7/mapping.n3#>"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			+ "PREFIX vocab: <http://localhost:2020/vocab/resource/>"
			+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>";
	private static PrefixMapping prefixMapping = null;

	private static final String DB_DRIVER = "org.postgresql.Driver";
	private static final String DB_CONNECTION = "jdbc:postgresql";
	private static final String DB_HOST = "[insert here your host name or ip]";
	private static final String DB_PORT = "[insert here your db port]";
	private static final String DB = "[insert here your db name]";
	private static final String DB_USER = "[insert here your db user]";
	private static final String DB_PASSWORD = "[insert here your db password]";
	private static Connection dbConnection = null;

	public static PrefixMapping getPrefixMapping()
	{
		if (prefixMapping == null)
		{
			System.out.println("[PRECONFIG]  set prefixes\n");

			prefixMapping = new PrefixMappingImpl();

			prefixMapping.setNsPrefix("dc", "http://purl.org/dc/elements/1.1/");
			prefixMapping.setNsPrefix("ov", "http://open.vocab.org/terms");
			prefixMapping.setNsPrefix("geo",
					"http://www.w3.org/2003/01/geo/wgs84_pos#");
			prefixMapping.setNsPrefix("mo", "http://purl.org/ontology/mo/");
			prefixMapping.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
			prefixMapping.setNsPrefix("mbz", "http://test.musicbrainz.org/");
			prefixMapping.setNsPrefix("rdfs",
					"http://www.w3.org/2000/01/rdf-schema#");
			prefixMapping.setNsPrefix("event",
					"http://purl.org/NET/c4dm/event.owl#");
			prefixMapping.setNsPrefix("dct", "http://purl.org/dc/terms/");
			prefixMapping
					.setNsPrefix("d2r",
							"http://sites.wiwiss.fu-berlin.de/suhl/bizer/d2r-server/config.rdf#");
			prefixMapping
					.setNsPrefix("xsd", "ttp://www.w3.org/2001/XMLSchema#");
			prefixMapping.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
			prefixMapping.setNsPrefix("map",
					"file:/home/kurtjx/srcs/d2r-server-0.7/mapping.n3#");
			prefixMapping.setNsPrefix("rdf",
					"http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			prefixMapping.setNsPrefix("vocab",
					"http://localhost:2020/vocab/resource/");
			prefixMapping.setNsPrefix("skos",
					"http://www.w3.org/2004/02/skos/core#");

			prefixMapping.lock();
		} else
		{
			System.out.println("[PRECONFIG]  prefixes already set");
		}

		return prefixMapping;
	}

	public static SPARQLResultSet runSPARQLQuery(String queryString,
			String serviceEndpoint)
	{
		Query query = QueryFactory.create(queryString); // exception happens
														// here
		com.hp.hpl.jena.query.QueryExecution qe = QueryExecutionFactory
				.sparqlService(serviceEndpoint, query);

		try
		{
			com.hp.hpl.jena.query.ResultSet rs = qe.execSelect();
			if (rs.hasNext())
			{
				// show the result, more can be done here
				ResultSetFormatter.out(rs, getPrefixMapping());
			}

			return new SPARQLResultSet(rs, qe);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());

			return null;
		}
	}

	public final static Connection getDBConnection()
	{
		if (dbConnection == null)
		{
			System.out.println("[PRECONFIG]  Database JDBC Connection Testing");

			try
			{
				Class.forName(DB_DRIVER);
			} catch (ClassNotFoundException e)
			{
				System.out
						.println("[PRECONFIG]  Where is your Database JDBC Driver? Include in your library path!");
				System.out.println(e.getMessage());

				return null;
			}

			System.out.println("[PRECONFIG]  Database JDBC Driver Registered!");

			Connection connection = null;

			try
			{
				connection = DriverManager.getConnection(DB_CONNECTION + "://"
						+ DB_HOST + ":" + DB_PORT + "/" + DB, DB_USER,
						DB_PASSWORD);
			} catch (SQLException e)
			{
				System.out
						.println("[PRECONFIG]  Connection Failed! Check output console");
				System.out.println(e.getMessage());

				return null;
			}

			if (connection != null)
			{
				System.out
						.println("[PRECONFIG]  You made it, take control your database now!");

				dbConnection = connection;
			} else
			{
				System.out.println("[PRECONFIG]  Failed to make connection!");

				return null;
			}
		}

		return dbConnection;
	}

	public static final SQLResultSet runSQLQuery(String query)
			throws SQLException
	{

		Connection dbConnection = null;
		Statement statement = null;

		// String selectTableSQL = "SELECT USER_ID, USERNAME from DBUSER";

		try
		{
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();

			System.out.println("DB query:\n" + query);

			// execute select SQL statement
			java.sql.ResultSet rs = statement.executeQuery(query);

			/*
			 * while (rs.next()) {
			 * 
			 * String userid = rs.getString("USER_ID"); String username =
			 * rs.getString("USERNAME");
			 * 
			 * System.out.println("userid : " + userid);
			 * System.out.println("username : " + username);
			 * 
			 * }
			 */

			return new SQLResultSet(rs, statement);

		} catch (SQLException e)
		{

			System.out.println(e.getMessage());

			return null;
		}
	}

}
