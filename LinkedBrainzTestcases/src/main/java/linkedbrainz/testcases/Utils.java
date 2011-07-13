package linkedbrainz.testcases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import linkedbrainz.testcases.model.SPARQLResultSet;
import linkedbrainz.testcases.model.SQLResultSet;
import linkedbrainz.testcases.model.TestResult;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.resultset.ResultSetMem;

/**
 * 
 * @author zazi
 * 
 */
public class Utils
{

	private static Utils instance = null;

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
	public static final long TIMEOUT = 100000;
	private PrefixMapping prefixMapping = null;

	private static final String DB_DRIVER = "org.postgresql.Driver";
	private static final String DB_CONNECTION = "jdbc:postgresql";
	private static final String DB_HOST = "localhost";
	private static final String DB_PORT = "5432";
	private static final String DB = "musicbrainz_db";
	private static final String DB_USER = "musicbrainz";
	private static final String DB_PASSWORD = "musicbrainz";
	private Connection dbConnection = null;

	private SQLResultSet sqlResultSet = null;
	private SPARQLResultSet sparqlResultSet = null;
	private String currentSparqlQuery = null;
	private com.hp.hpl.jena.query.ResultSet sparqlRS = null;
	private int queryCounter = 0;
	private String initSqlQuery = null;
	private String initSparqlQuery = null;
	private int limit = 0;
	private String initSqlFailMsg = "CHECK_NAME failed due to a SQLException";
	private String initSparqlFailMsg = "CHECK_NAME failed due to a SPARQL query execution";
	private String initQueryCounterFailMsg = "dunno - query counter in CHECK_NAME must be not equal to ";
	private String sqlFailMsg = null;
	private String sparqlFailMsg = null;
	private String queryCounterFailMsg = null;

	private Utils()
	{

	}

	/**
	 * Singleton
	 * 
	 * @return the one and only Utils instance
	 */
	public static Utils getInstance()
	{
		if (instance == null)
		{
			instance = new Utils();
		}

		return instance;
	}

	/**
	 * Sets the prefix mapping for ARQ if necessary, otherwise it returns the
	 * already initialised PrefixMapping instance.
	 * 
	 * @return a initialised PrefixMapping instance
	 */
	public PrefixMapping getPrefixMapping()
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

	/**
	 * Runs a SPARQL query against a given SPARQL endpoint.
	 * 
	 * @param queryString
	 *            the preformatted SPARQL query
	 * @param serviceEndpoint
	 *            the given SPARQL endpoint
	 * @return a fresh SPARQLResultSet instance or null if something goes wrong
	 */
	public SPARQLResultSet runSPARQLQuery(String queryString,
			String serviceEndpoint)
	{
		Query query = QueryFactory.create(queryString); // exception happens
		// here
		QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory
				.sparqlService(serviceEndpoint, query);
		qe.addParam("timeout", "10000");

		SPARQLResultSet resultSet = null;
		ResultSetMem tempRS = null;
		ResultSetMem tempRS2 = null;

		try
		{
			System.out.println("[EXEC]  SPARQL query:\n\t\t" + queryString);

			com.hp.hpl.jena.query.ResultSet rs = qe.execSelect();

			// need to create in-memory copies since the Result is of the type
			// com.hp.hpl.jena.sparql.resultset.XMLInputStAX$ResultSetStAX
			tempRS = new ResultSetMem(rs);
			tempRS2 = new ResultSetMem(tempRS, true);

			resultSet = new SPARQLResultSet(tempRS, qe);

			if (tempRS2.hasNext())
			{
				// show the result, more can be done here
				System.out.println("[EXEC] SPARQL query result");
				ResultSetFormatter.out(tempRS2, getPrefixMapping());
			}

			return resultSet;
		} catch (Exception e)
		{
			System.out
					.println("[EXEC] Exception while processing a SPARQL query: "
							+ e.getMessage());

			return null;
		}
	}

	/**
	 * Tries to establish a DB connection to the given parameters that are set
	 * (DB_DRIVER, DB_CONNECTION, DB_HOST, DB_PORT, DB, DB_USER, DB_PASSWORD) or
	 * returns the already established DB connection.
	 * 
	 * @return a DB connection
	 */
	public Connection getDBConnection()
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

	/**
	 * Runs a SQL query against a preset DB connection (would be initialised
	 * automatically via getDBConnection()).
	 * 
	 * @param query
	 *            the given preformatted SPQL query
	 * @return a fresh SQLResultSet instance or null if something goes wrong
	 * @throws SQLException
	 *             if something went wrong
	 */
	public SQLResultSet runSQLQuery(String query) throws SQLException
	{

		Connection dbConnection = null;
		Statement statement = null;

		try
		{
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();

			System.out.println("[EXEC]  DB query:\n\t\t" + query);

			// execute select SQL statement
			java.sql.ResultSet rs = statement.executeQuery(query);

			return new SQLResultSet(rs, statement);

		} catch (SQLException e)
		{
			System.out
					.println("[EXEC] Exception while processing a SQL query: "
							+ e.getMessage());

			return null;
		}
	}

	/**
	 * Fetches 5 instances of a specific type from the DB and resolves them via
	 * the GUID in a SPARQL query.
	 * 
	 * @param table
	 *            the specific table for the SQL query
	 * @param row
	 *            the specific row for the SQL query
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkClassViaGUID(String table, String row,
			String className, String checkName)
	{
		initSqlQuery = "SELECT gid FROM musicbrainz.TABLE LIMIT 5";
		initSparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI "
				+ "WHERE { ?URI rdf:type CLASS_NAME . "
				+ "?URI mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string . } ";
		limit = 5;

		return checkClass(table, row, className, checkName);
	}

	/**
	 * Fetches an instance of a specific type from the DB and resolves it via
	 * the ID in a SPARQL query.
	 * 
	 * @param table
	 *            the specific table for the SQL query
	 * @param row
	 *            the specific row for the SQL query
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkClassViaID(String table, String row,
			String className, String checkName)
	{
		initSqlQuery = "SELECT id FROM musicbrainz.TABLE LIMIT 1";
		initSparqlQuery = Utils.DEFAULT_PREFIXES 
				+ "SELECT DISTINCT ?URI "
				+ "WHERE { ?URI rdf:type CLASS_NAME . "
				+ "FILTER regex(str(?URI), \"/ID_PLACEHOLDER#_\") } ";
		limit = 1;

		return checkClass(table, row, className, checkName);
	}

	/**
	 * Fetches some instances of a specific type from the DB and resolves them
	 * via a SPARQL query.
	 * 
	 * @param table
	 *            the specific table for the SQL query
	 * @param row
	 *            the specific row for the SQL query
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkClass(String table, String row, String className,
			String checkName)
	{
		resetQueryVars();
		initFailMsgs(checkName);

		String initSqlQuery = this.initSqlQuery;
		String sqlQuery = initSqlQuery.replace("TABLE", table);

		List<String> gids = null;

		String initSparqlQuery = this.initSparqlQuery;
		String sparqlQuery = initSparqlQuery.replace("CLASS_NAME", className);

		try
		{
			sqlResultSet = runSQLQuery(sqlQuery);
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());

			return new TestResult(false, sqlFailMsg);
		}

		if (sqlResultSet != null)
		{
			java.sql.ResultSet sqlRS = sqlResultSet.getResultSet();
			gids = new ArrayList<String>();

			try
			{
				while (sqlRS.next())
				{
					gids.add(sqlRS.getString(row));
				}
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());

				return new TestResult(false, sqlFailMsg);
			}

			sqlResultSet.close();
		}

		if (gids.size() == limit)
		{
			for (int i = 0; i < limit; i++)
			{
				sparqlRS = null;

				currentSparqlQuery = sparqlQuery.replace("ID_PLACEHOLDER", gids
						.get(i));

				try
				{
					sparqlResultSet = runSPARQLQuery(currentSparqlQuery,
							SERVICE_ENDPOINT);

					sparqlRS = sparqlResultSet.getResultSet();

					while (sparqlRS.hasNext())
					{
						queryCounter++;
						sparqlRS.next();
					}
				} catch (Exception e)
				{
					System.out.println(e.getMessage());

					return new TestResult(false, sparqlFailMsg);
				}

				sparqlResultSet.close();
			}
		}

		return new TestResult(queryCounter == limit, queryCounterFailMsg);
	}

	/**
	 * Fetches 5 instances from the DB and resolves theirs names against the
	 * result of the related SPARQL query.
	 * 
	 * @param classTable
	 *            the specific class table for the SQL query
	 * @param classNameTable
	 *            the specific class name table for the SQL query
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkInstanceNamesViaGUID(String classTable, String classTableRow,
			String classNameTable, String className, String propertyName,
			String checkName)
	{
		initSqlQuery = "SELECT musicbrainz.CLASS_NAME.name AS name, "
				+ "musicbrainz.CLASS.gid AS id "
				+ "FROM musicbrainz.CLASS "
				+ "INNER JOIN musicbrainz.CLASS_NAME  ON CLASS.CLASS_ROW = CLASS_NAME.id LIMIT 5";
		initSparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI ?name "
				+ "WHERE { ?URI a CLASS_NAME ; "
				+ "mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string ; "
				+ "PROPERTY_NAME ?name . }";
		limit = 5;

		return checkInstanceNames(classTable, classTableRow, classNameTable, className,
				propertyName, checkName);
	}

	/**
	 * Fetches 1 instance from the DB and resolves its name against the result
	 * of the related SPARQL query.
	 * 
	 * @param classTable
	 *            the specific class table for the SQL query
	 * @param classNameTable
	 *            the specific class name table for the SQL query
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkInstanceNamesViaID(String classTable, String classTableRow,
			String classNameTable, String className, String propertyName,
			String checkName)
	{
		initSqlQuery = "SELECT musicbrainz.CLASS_NAME.name AS name, "
				+ "musicbrainz.CLASS.id AS id "
				+ "FROM musicbrainz.CLASS "
				+ "INNER JOIN musicbrainz.CLASS_NAME  ON CLASS.CLASS_ROW = CLASS_NAME.id LIMIT 1";
		initSparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI ?name "
				+ "WHERE { ?URI rdf:type CLASS_NAME ; "
				+ "PROPERTY_NAME ?name . "
				+ "FILTER regex(str(?URI), \"/ID_PLACEHOLDER#_\") } ";
		limit = 1;

		return checkInstanceNames(classTable, classTableRow, classNameTable, className,
				propertyName, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves theirs names against the
	 * result of the related SPARQL query.
	 * 
	 * @param classTable
	 *            the specific class table for the SQL query
	 * @param classTable Row
	 * 			  the specific row in the class table for the SQL query           
	 * @param classNameTable
	 *            the specific class name table for the SQL query
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkInstanceNames(String classTable, String classTableRow,
			String classNameTable, String className, String propertyName,
			String checkName)
	{
		resetQueryVars();
		initFailMsgs(checkName);

		String initSqlQuery = this.initSqlQuery;
		String initSqlQuery2 = initSqlQuery.replace("CLASS_NAME",
				classNameTable);
		String initSqlQuery3 = initSqlQuery2.replace("CLASS_ROW", classTableRow);
		String sqlQuery = initSqlQuery3.replace("CLASS", classTable);

		Map<String, String> names = null;
		String namesKey = null;

		String initSparqlQuery = this.initSparqlQuery;
		String initSparqlQuery2 = initSparqlQuery.replace("CLASS_NAME",
				className);
		String sparqlQuery = initSparqlQuery2.replace("PROPERTY_NAME",
				propertyName);

		try
		{
			sqlResultSet = runSQLQuery(sqlQuery);
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());

			return new TestResult(false, sqlFailMsg);
		}

		if (sqlResultSet != null)
		{
			java.sql.ResultSet sqlRS = sqlResultSet.getResultSet();
			names = new HashMap<String, String>();

			try
			{
				while (sqlRS.next())
				{
					names.put(sqlRS.getString("id"), sqlRS
							.getString("name"));
				}
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());

				return new TestResult(false, sqlFailMsg);
			}

			sqlResultSet.close();
		}

		if (names.size() == limit)
		{
			Iterator<String> iter = names.keySet().iterator();

			for (int i = 0; i < limit; i++)
			{
				sparqlRS = null;
				namesKey = iter.next();

				currentSparqlQuery = sparqlQuery.replace("ID_PLACEHOLDER",
						namesKey);

				try
				{
					sparqlResultSet = runSPARQLQuery(currentSparqlQuery,
							SERVICE_ENDPOINT);

					sparqlRS = sparqlResultSet.getResultSet();

					while (sparqlRS.hasNext())
					{
						if (names.get(namesKey).equals(
								sparqlRS.next().getLiteral("name").getString()))
						{
							queryCounter++;
						}
					}
				} catch (Exception e)
				{
					System.out.println(e.getMessage());

					return new TestResult(false, sparqlFailMsg);
				}

				sparqlResultSet.close();
			}
		}

		return new TestResult(queryCounter == limit, queryCounterFailMsg);
	}

	/**
	 * Reset the required variables of the standard check processing (compare
	 * SQL query results with SPARQL query results).
	 */
	private void resetQueryVars()
	{
		sqlResultSet = null;
		sparqlResultSet = null;
		currentSparqlQuery = null;
		sparqlRS = null;
		queryCounter = 0;
	}

	/**
	 * Initialises the different fail messages with the name of the specific
	 * check.
	 * 
	 * @param checkName
	 *            the name of the specific check
	 */
	private void initFailMsgs(String checkName)
	{
		sqlFailMsg = initSqlFailMsg.replace("CHECK_NAME", checkName);
		sparqlFailMsg = initSparqlFailMsg.replace("CHECK_NAME", checkName);
		initQueryCounterFailMsg = initQueryCounterFailMsg + limit;
		queryCounterFailMsg = initQueryCounterFailMsg.replace("CHECK_NAME",
				checkName);
	}

}
