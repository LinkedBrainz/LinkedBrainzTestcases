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
	 * @param condition
	 *            the condition that specifies the specific RDF class in the SQL
	 *            query
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkClassViaGUIDAndCondition(String table, String row,
			String conditionRow, String condition, String className,
			String checkName)
	{
		String initSqlQuery2 = "SELECT gid FROM musicbrainz.TABLE "
				+ "WHERE musicbrainz.TABLE.CONDITION_ROW = CONDITION "
				+ "LIMIT 5";
		String initSqlQuery3 = initSqlQuery2.replace("CONDITION_ROW",
				conditionRow);
		initSqlQuery = initSqlQuery3.replace("CONDITION", condition);

		return checkClassViaGUID(table, row, className, checkName);
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
	public TestResult checkClassViaGUIDSimple(String table, String row,
			String className, String checkName)
	{
		initSqlQuery = "SELECT gid FROM musicbrainz.TABLE LIMIT 5";

		return checkClassViaGUID(table, row, className, checkName);
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
	private TestResult checkClassViaGUID(String table, String row,
			String className, String checkName)
	{
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
		initSparqlQuery = Utils.DEFAULT_PREFIXES + "SELECT DISTINCT ?URI "
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
	 * Fetches some instances from the DB and resolves their aliases against the
	 * result of the related SPARQL query.
	 * 
	 * @param classTable
	 *            the specific class table for the SQL query
	 * @param classTable
	 *            Row the specific row in the class table for the SQL query
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
	public TestResult checkInstanceAliases(String classTable, String className,
			String proofGUID, String checkName)
	{
		resetQueryVars();
		initFailMsgs(checkName);

		// fetch some arbitrary MBZ gids for the beginning
		String initSqlQuery = "SELECT musicbrainz.CLASS.gid AS id "
				+ "FROM musicbrainz.CLASS " + "LIMIT 5";
		String sqlQuery = initSqlQuery.replace("CLASS", classTable);
		limit = 5;

		Map<String, List<String>> aliases = null;
		String aliasesKey = null;

		String initSparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI ?alias "
				+ "WHERE { ?URI a CLASS_NAME ; "
				+ "mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string ; "
				+ "skos:altLabel ?alias . }";
		String sparqlQuery = initSparqlQuery.replace("CLASS_NAME", className);

		int overallAliasesCounter = 0;

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
			aliases = new HashMap<String, List<String>>();

			try
			{
				// init aliases map with empty lists
				while (sqlRS.next())
				{
					aliases.put(sqlRS.getString("id"), new ArrayList<String>());
				}
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());

				return new TestResult(false, sqlFailMsg);
			}

			sqlResultSet.close();
		}

		// add a hardcoded GUID, since one could fetch instances that have no
		// aliases and the wondering about the results
		aliases.put(proofGUID, new ArrayList<String>());

		if (aliases.size() == limit + 1)
		{
			Iterator<String> iter = aliases.keySet().iterator();

			for (int j = 0; j < aliases.size(); j++)
			{
				aliasesKey = iter.next();
				initSqlQuery = "SELECT musicbrainz.CLASS.gid AS id, "
						+ "musicbrainz.CLASS_alias.name AS CLASS_alias_name_id, "
						+ "musicbrainz.CLASS_name.id AS CLASS_name_id, "
						+ "musicbrainz.CLASS_alias.CLASS AS CLASS_alias_CLASS_id, "
						+ "musicbrainz.CLASS_alias.id AS CLASS_alias_id, "
						+ "musicbrainz.CLASS_name.name AS alias "
						+ "FROM musicbrainz.CLASS_alias "
						+ "INNER JOIN musicbrainz.CLASS_name  ON CLASS_alias.name = CLASS_name.id "
						+ "INNER JOIN musicbrainz.CLASS ON CLASS_alias.CLASS = CLASS.id "
						+ "WHERE CLASS.gid = 'GUID_PLACEHOLDER'";
				String initSqlQuery2 = initSqlQuery
						.replace("CLASS", classTable);
				sqlQuery = initSqlQuery2
						.replace("GUID_PLACEHOLDER", aliasesKey);
				sqlResultSet = null;

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
					try
					{
						// fill alias lists with aliases
						while (sqlRS.next())
						{

							aliases.get(sqlRS.getString("id")).add(
									sqlRS.getString("alias"));
							overallAliasesCounter++;
						}

						System.out.println("[EXEC]  fetched "
								+ aliases.get(aliasesKey).size()
								+ " aliases for GUID " + aliasesKey);
					} catch (SQLException e)
					{
						System.out.println(e.getMessage());

						return new TestResult(false, sqlFailMsg);
					}

					sqlResultSet.close();
				}

			}

			iter = aliases.keySet().iterator();

			for (int i = 0; i < aliases.size(); i++)
			{
				sparqlRS = null;
				aliasesKey = iter.next();

				currentSparqlQuery = sparqlQuery.replace("ID_PLACEHOLDER",
						aliasesKey);

				try
				{
					sparqlResultSet = runSPARQLQuery(currentSparqlQuery,
							SERVICE_ENDPOINT);

					sparqlRS = sparqlResultSet.getResultSet();

					while (sparqlRS.hasNext())
					{
						if (aliases.get(aliasesKey)
								.contains(
										sparqlRS.next().getLiteral("alias")
												.getString()))
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

		return new TestResult(queryCounter == overallAliasesCounter,
				queryCounterFailMsg);
	}

	/**
	 * Fetches 5 instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTable1
	 *            the table of the left side of the INNER JOIN of the SQL query
	 * @param classTable2
	 *            the table of the right side of the INNER JOIN of the SQL query
	 * @param classTableRow1
	 *            the row of of the table of the left side of the INNER JOIN of
	 *            the SQL query
	 * @param classTableRow2
	 *            the row of the table that delivers the values for the specific
	 *            RDF property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaGUIDOnTheLeft(String classTable1,
			String classTable2, String classTableRow1, String classTableRow2,
			String className, String propertyName, String valueName,
			String checkName)
	{
		initSqlQuery = "SELECT musicbrainz.CLASS1.gid AS id, "
				+ "musicbrainz.CLASS2.CLASS_ROW2 AS VALUE_NAME "
				+ "FROM musicbrainz.CLASS1 "
				+ "INNER JOIN musicbrainz.CLASS2 ON CLASS1.CLASS_ROW1 = CLASS2.id "
				+ "LIMIT 5";

		return checkSimplePropertyViaGUID(classTable1, classTable2,
				classTableRow1, classTableRow2, className, propertyName,
				valueName, checkName);
	}

	/**
	 * Fetches 5 instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTable1
	 *            the table of the left side of the INNER JOIN of the SQL query
	 * @param classTable2
	 *            the table of the right side of the INNER JOIN of the SQL query
	 * @param classTableRow1
	 *            the row of the table of the left side of the INNER JOIN of the
	 *            SQL query
	 * @param classTableRow2
	 *            the row of the table that delivers the values for the specific
	 *            RDF property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaGUIDOnTheRight(String classTable1,
			String classTable2, String classTableRow1, String classTableRow2,
			String className, String propertyName, String valueName,
			String checkName)
	{
		initSqlQuery = "SELECT musicbrainz.CLASS2.gid AS id, "
				+ "musicbrainz.CLASS1.CLASS_ROW2 AS VALUE_NAME "
				+ "FROM musicbrainz.CLASS1 "
				+ "INNER JOIN musicbrainz.CLASS2 ON CLASS1.CLASS_ROW1 = CLASS2.id "
				+ "LIMIT 5";

		return checkSimplePropertyViaGUID(classTable1, classTable2,
				classTableRow1, classTableRow2, className, propertyName,
				valueName, checkName);
	}

	/**
	 * Fetches 5 instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTable1
	 *            the table of the left side of the INNER JOIN of the SQL query
	 * @param classTable2
	 *            the table of the right side of the INNER JOIN of the SQL query
	 * @param classTableRow1
	 *            the row of the table of the left side of the INNER JOIN of the
	 *            SQL query
	 * @param classTableRow2
	 *            the row of the table that delivers the values for the specific
	 *            RDF property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkSimplePropertyViaGUID(String classTable1,
			String classTable2, String classTableRow1, String classTableRow2,
			String className, String propertyName, String valueName,
			String checkName)
	{
		initSparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI ?VALUE_NAME "
				+ "WHERE { ?URI a CLASS_NAME ; "
				+ "mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string ; "
				+ "PROPERTY_NAME ?VALUE_NAME . }";

		limit = 5;

		return checkSimpleProperty(classTable1, classTable2, classTableRow1,
				classTableRow2, className, propertyName, valueName, checkName);
	}

	/**
	 * Fetches one instance from the DB and resolves the value of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTable1
	 *            the table of the left side of the INNER JOIN of the SQL query
	 * @param classTable2
	 *            the table of the right side of the INNER JOIN of the SQL query
	 * @param classTableRow1
	 *            the row of of the table of the left side of the INNER JOIN of
	 *            the SQL query
	 * @param classTableRow2
	 *            the row of the table that delivers the values for the specific
	 *            RDF property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaIDOnTheLeft(String classTable1,
			String classTable2, String classTableRow1, String classTableRow2,
			String className, String propertyName, String valueName,
			String checkName)
	{
		initSqlQuery = "SELECT musicbrainz.CLASS2.CLASS_ROW2 AS VALUE_NAME, "
				+ "musicbrainz.CLASS1.id AS id "
				+ "FROM musicbrainz.CLASS1 "
				+ "INNER JOIN musicbrainz.CLASS2  ON CLASS1.CLASS_ROW1 = CLASS2.id LIMIT 1";

		return checkSimplePropertyViaID(classTable1, classTable2,
				classTableRow1, classTableRow2, className, propertyName,
				valueName, checkName);
	}

	/**
	 * Fetches one instance from the DB and resolves the value of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTable1
	 *            the table of the left side of the INNER JOIN of the SQL query
	 * @param classTable2
	 *            the table of the right side of the INNER JOIN of the SQL query
	 * @param classTableRow1
	 *            the row of the table of the left side of the INNER JOIN of the
	 *            SQL query
	 * @param classTableRow2
	 *            the row of the table that delivers the values for the specific
	 *            RDF property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaIDOnTheRight(String classTable1,
			String classTable2, String classTableRow1, String classTableRow2,
			String className, String propertyName, String valueName,
			String checkName)
	{
		initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW2 AS VALUE_NAME, "
				+ "musicbrainz.CLASS2.id AS id "
				+ "FROM musicbrainz.CLASS1 "
				+ "INNER JOIN musicbrainz.CLASS2  ON CLASS1.CLASS_ROW1 = CLASS2.id LIMIT 1";

		return checkSimplePropertyViaID(classTable1, classTable2,
				classTableRow1, classTableRow2, className, propertyName,
				valueName, checkName);
	}

	/**
	 * Fetches one instance from the DB and resolves the value of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTable1
	 *            the table of the left side of the INNER JOIN of the SQL query
	 * @param classTable2
	 *            the table of the right side of the INNER JOIN of the SQL query
	 * @param classTableRow1
	 *            the row of the table of the left side of the INNER JOIN of the
	 *            SQL query
	 * @param classTableRow2
	 *            the row of the table that delivers the values for the specific
	 *            RDF property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkSimplePropertyViaID(String classTable1,
			String classTable2, String classTableRow1, String classTableRow2,
			String className, String propertyName, String valueName,
			String checkName)
	{
		initSparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI ?VALUE_NAME "
				+ "WHERE { ?URI rdf:type CLASS_NAME ; "
				+ "PROPERTY_NAME ?VALUE_NAME . "
				+ "FILTER regex(str(?URI), \"/ID_PLACEHOLDER#_\") } ";

		limit = 1;

		return checkSimpleProperty(classTable1, classTable2, classTableRow1,
				classTableRow2, className, propertyName, valueName, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTable1
	 *            the table of the left side of the INNER JOIN of the SQL query
	 * @param classTable2
	 *            the table of the right side of the INNER JOIN of the SQL query
	 * @param classTableRow1
	 *            the row of of the table of the left side of the INNER JOIN of
	 *            the SQL query
	 * @param classTableRow2
	 *            the row of the table that delivers the values for the specific
	 *            RDF property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkSimpleProperty(String classTable1,
			String classTable2, String classTableRow1, String classTableRow2,
			String className, String propertyName, String valueName,
			String checkName)
	{
		resetQueryVars();
		initFailMsgs(checkName);

		String initSqlQuery = this.initSqlQuery;
		String initSqlQuery2 = initSqlQuery.replace("CLASS1", classTable1);
		String initSqlQuery3 = initSqlQuery2.replace("CLASS2", classTable2);
		String initSqlQuery4 = initSqlQuery3.replace("CLASS_ROW1",
				classTableRow1);
		String initSqlQuery5 = initSqlQuery4.replace("VALUE_NAME", valueName);
		String sqlQuery = initSqlQuery5.replace("CLASS_ROW2", classTableRow2);

		Map<String, String> values = null;
		String valuesKey = null;

		String initSparqlQuery = this.initSparqlQuery;
		String initSparqlQuery2 = initSparqlQuery.replace("VALUE_NAME",
				valueName);
		String initSparqlQuery3 = initSparqlQuery2.replace("CLASS_NAME",
				className);
		String sparqlQuery = initSparqlQuery3.replace("PROPERTY_NAME",
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
			values = new HashMap<String, String>();

			try
			{
				while (sqlRS.next())
				{
					values.put(sqlRS.getString("id"), sqlRS
							.getString(valueName));
				}
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());

				return new TestResult(false, sqlFailMsg);
			}

			sqlResultSet.close();
		}

		if (values.size() == limit)
		{
			Iterator<String> iter = values.keySet().iterator();

			for (int i = 0; i < limit; i++)
			{
				sparqlRS = null;
				valuesKey = iter.next();

				currentSparqlQuery = sparqlQuery.replace("ID_PLACEHOLDER",
						valuesKey);

				try
				{
					sparqlResultSet = runSPARQLQuery(currentSparqlQuery,
							SERVICE_ENDPOINT);

					sparqlRS = sparqlResultSet.getResultSet();

					while (sparqlRS.hasNext())
					{
						if (values.get(valuesKey).equals(
								sparqlRS.next().getLiteral(valueName)
										.getString()))
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
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the id(s) for the check that are
	 *            located on the left side of the relation and is located on the
	 *            right side of the first INNER JOIN. The second one delivers
	 *            the id(s) for the check that are located on the right side of
	 *            the relation and is located on the right side of the third
	 *            INNER JOIN. The third one is located on the left side of the
	 *            INNER JOINs. The fourth one is located on the right side of
	 *            the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the id(s) for the check that
	 *            are located on the left side of the relation. The second one
	 *            delivers the id(s) for the check that are located on the right
	 *            side of the relation. The third one is the non-'id' row of the
	 *            first INNER JOIN. The forth one is the non-'id' row of the
	 *            second INNER JOIN. The fifth one is the non-'id' row of the
	 *            third INNER JOIN.
	 * @param classNames
	 *            the list of class names for the SPARQL query currently
	 *            consists of 2 items. The first one is the class name of the
	 *            resource of the left side of the relation. The second one is
	 *            the class name of the resource of the right side of the
	 *            relation.
	 * @param propertyName
	 *            the property name of the relation.
	 * @param valueNames
	 *            the list of value names for the SPARQL query currently
	 *            consists of 3 items. The first one is the value name for
	 *            resources of the left side of the relation. The second one is
	 *            the value name for resources of the right side of the
	 *            relation. The third one is the value name for resource ids of
	 *            the right side of the relation.
	 * @param proofID
	 *            a hardcoded ID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIProperty(ArrayList<String> classTables,
			ArrayList<String> classTableRows, ArrayList<String> classNames,
			String propertyName, ArrayList<String> valueNames, String proofID,
			String checkName)
	{
		resetQueryVars();
		initFailMsgs(checkName);

		// fetch some arbitrary MBZ gids for the beginning
		String initSqlQuery = "SELECT musicbrainz.CLASS.gid AS id "
				+ "FROM musicbrainz.CLASS " + "LIMIT 5";
		String sqlQuery = initSqlQuery.replace("CLASS", classTables.get(0));
		limit = 5;

		String initSparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?VALUE_NAME1 ?VALUE_NAME3 "
				+ "WHERE { ?VALUE_NAME1 rdf:type CLASS_NAME1 ; "
				+ "PROPERTY_NAME ?VALUE_NAME2 ; "
				+ "mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string . "
				+ "?VALUE_NAME2 rdf:type CLASS_NAME2 ; "
				+ "mo:musicbrainz_guid ?VALUE_NAME3 . } ";
		String initSparqlQuery2 = replacePlaceholders(classNames, "CLASS_NAME",
				initSparqlQuery);
		String initSparqlQuery3 = replacePlaceholders(valueNames, "VALUE_NAME",
				initSparqlQuery2);
		String sparqlQuery = initSparqlQuery3.replace("PROPERTY_NAME",
				propertyName);

		Map<String, List<String>> relations = null;
		String relationsKey = null;

		int overallRelationsCounter = 0;
		int relationsCounter = 0;

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
			relations = new HashMap<String, List<String>>();

			try
			{
				// init relations map with empty lists
				while (sqlRS.next())
				{
					relations.put(sqlRS.getString("id"),
							new ArrayList<String>());
				}
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());

				return new TestResult(false, sqlFailMsg);
			}

			sqlResultSet.close();
		}

		// add a hardcoded ID, since one could fetch instances that have no
		// relations and the wondering about the results
		relations.put(proofID, new ArrayList<String>());

		if (relations.size() == limit + 1)
		{
			Iterator<String> iter = relations.keySet().iterator();

			initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
					+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
					+ "FROM musicbrainz.CLASS3 "
					+ "INNER JOIN musicbrainz.CLASS1 ON CLASS3.CLASS_ROW3 = CLASS1.id "
					+ "INNER JOIN musicbrainz.CLASS4 ON CLASS3.CLASS_ROW4 = CLASS4.id "
					+ "INNER JOIN musicbrainz.CLASS2 ON CLASS4.id = CLASS2.CLASS_ROW5 "
					+ "WHERE musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
			String initSqlQuery2 = replacePlaceholders(classTableRows,
					"CLASS_ROW", initSqlQuery);
			String initSqlQuery3 = replacePlaceholders(classTables, "CLASS",
					initSqlQuery2);

			for (int j = 0; j < relations.size(); j++)
			{
				relationsKey = iter.next();
				sqlQuery = initSqlQuery3
						.replace("ID_PLACEHOLDER", relationsKey);
				sqlResultSet = null;

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
					try
					{
						// fill relation lists with relations
						while (sqlRS.next())
						{

							relations
									.get(
											sqlRS.getString(classTables.get(0)
													+ "_id")).add(
											sqlRS.getString(classTables.get(1)
													+ "_id"));
							overallRelationsCounter++;
						}

						System.out.println("[EXEC]  fetched "
								+ relations.get(relationsKey).size()
								+ " relations for ID " + relationsKey);
					} catch (SQLException e)
					{
						System.out.println(e.getMessage());

						return new TestResult(false, sqlFailMsg);
					}

					sqlResultSet.close();
				}

			}

			iter = relations.keySet().iterator();

			for (int i = 0; i < relations.size(); i++)
			{
				sparqlRS = null;
				relationsCounter = 0;
				relationsKey = iter.next();

				currentSparqlQuery = sparqlQuery.replace("ID_PLACEHOLDER",
						relationsKey);

				try
				{
					sparqlResultSet = runSPARQLQuery(currentSparqlQuery,
							SERVICE_ENDPOINT);

					sparqlRS = sparqlResultSet.getResultSet();

					while (sparqlRS.hasNext())
					{
						if (relations.get(relationsKey).contains(
								sparqlRS.next().getLiteral(valueNames.get(2))
										.getString()))
						{
							queryCounter++;
							relationsCounter++;
						}
					}

					if (relationsCounter != relations.get(relationsKey).size())
					{
						return new TestResult(false, sparqlFailMsg);
					}
				} catch (Exception e)
				{
					System.out.println(e.getMessage());

					return new TestResult(false, sparqlFailMsg);
				}

				sparqlResultSet.close();
			}
		}

		return new TestResult(queryCounter == overallRelationsCounter,
				queryCounterFailMsg);
	}

	/**
	 * Replaces placeholders in a given string with given replacements on basis
	 * of a specific replacement identifier prefix
	 * 
	 * @param replacements
	 *            the list of replacements
	 * @param replacementIdentifierPrefix
	 *            the specific replacement identifier prefix
	 * @param initString
	 *            the initial string that includes the placeholders
	 * @return a string filled with the replacements
	 */
	private String replacePlaceholders(ArrayList<String> replacements,
			String replacementIdentifierPrefix, String initString)
	{
		String tempString = null;
		String replacementString = initString;

		for (int i = 0; i < replacements.size(); i++)
		{
			tempString = replacementString.replace(replacementIdentifierPrefix
					+ (i + 1), replacements.get(i));
			replacementString = tempString;
		}

		return replacementString;
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
