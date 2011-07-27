package linkedbrainz.testcases;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import linkedbrainz.testcases.model.Condition;
import linkedbrainz.testcases.model.SPARQLResultSet;
import linkedbrainz.testcases.model.SQLResultSet;
import linkedbrainz.testcases.model.TestResult;
import linkedbrainz.testcases.model.URICondition;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.resultset.ResultSetMem;

import de.fuberlin.wiwiss.d2rq.values.Translator;

/**
 * 
 * @author zazi
 * 
 *         TODO: merge checkSimpleProperty and checkProperty method
 * 
 */
public class Utils
{

	private static Utils instance = null;

	public static final String SERVICE_ENDPOINT = "http://localhost:2020/sparql";
	public static final String DEFAULT_PREFIXES = "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
			+ "PREFIX ov: <http://open.vocab.org/terms/>\n"
			+ "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"
			+ "PREFIX mo: <http://purl.org/ontology/mo/>\n"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
			+ "PREFIX mbz: <http://test.musicbrainz.org/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n"
			+ "PREFIX dct: <http://purl.org/dc/terms/>\n"
			+ "PREFIX d2r: <http://sites.wiwiss.fu-berlin.de/suhl/bizer/d2r-server/config.rdf#>\n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
			+ "PREFIX map: <file:/home/kurtjx/srcs/d2r-server-0.7/mapping.n3#>\n"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX vocab: <http://localhost:2020/vocab/resource/>\n"
			+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
			+ "PREFIX is: <http://purl.org/ontology/is/core#>\n"
			+ "PREFIX isi: <http://purl.org/ontology/is/inst/>\n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";
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
	private String candidatesSqlQuery = null;
	private String initSqlQuery = null;
	private String sqlQueryCondition = null;
	private String sparqlQueryCondition = null;
	private Condition condition = null;
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
			prefixMapping
					.setNsPrefix("is", "http://purl.org/ontology/is/core#");
			prefixMapping.setNsPrefix("isi",
					"http://purl.org/ontology/is/inst/");
			prefixMapping.setNsPrefix("xsd",
					"http://www.w3.org/2001/XMLSchema#");

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
			System.out.println("\n[EXEC]  SPARQL query:\n\t\t" + queryString);

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

			System.out.println("\n[EXEC]  DB query:\n\t\t" + query);

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
	 * @param idString
	 *            the identifier for the id column
	 * @param fragmentId
	 *            the fragment id of the URI that contains the id as well
	 * @param limit
	 *            the result limit of the SQL query
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkClassViaGUIDOrIDAndFragmentId(String table,
			String row, String className, String fragmentId, int limit,
			String checkName)
	{
		String initSqlQuery2 = "SELECT ID_ROW FROM musicbrainz.TABLE LIMIT LIMIT_NUMBER";
		String initSqlQuery3 = initSqlQuery2.replace("ID_ROW", row);
		initSqlQuery = initSqlQuery3.replace("LIMIT_NUMBER", Integer
				.toString(limit));

		String initSparqlQuery2 = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI "
				+ "WHERE { ?URI rdf:type CLASS_NAME . "
				+ "FILTER regex(str(?URI), \"/ID_PLACEHOLDERFRAGMENT_ID\") } ";
		initSparqlQuery = initSparqlQuery2.replace("FRAGMENT_ID", fragmentId);
		this.limit = limit;

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

		System.out
				.println("\n##################################################\n[EXEC]  "
						+ checkName
						+ "\n##################################################\n");

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
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit fo the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param condition
	 *            the condition object that includes specific values that are
	 *            used to initialise the condition statements
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaGUIDOnTheLeft(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			String className, String propertyName, String valueName,
			int numberOfJoins, int limit, boolean comparisonOnResource,
			boolean multipleValues, Condition condition, String proofId,
			String checkName)
	{
		initSqlQueryForCheckSimpleProperty("gid", numberOfJoins, true);

		setCondition(condition);

		return checkSimplePropertyViaGUID(classTables, classTableRows,
				className, propertyName, valueName, limit,
				comparisonOnResource, multipleValues, initCondition(condition),
				proofId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param limit
	 *            the result limit of the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param condition
	 *            the condition object that includes specific values that are
	 *            used to initialise the condition statements
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaGUIDOnTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			String className, String propertyName, String valueName, int limit,
			boolean comparisonOnResource, boolean multipleValues,
			Condition condition, String proofId, String checkName)
	{
		initSqlQueryForCheckSimpleProperty("gid", 1, false);

		setCondition(condition);

		return checkSimplePropertyViaGUID(classTables, classTableRows,
				className, propertyName, valueName, limit,
				comparisonOnResource, multipleValues, initCondition(condition),
				proofId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param limit
	 *            the result limit fo the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param withCondition
	 *            indicates whether a condition is set or not
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkSimplePropertyViaGUID(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			String className, String propertyName, String valueName, int limit,
			boolean comparisonOnResource, boolean multipleValues,
			boolean withCondition, String proofId, String checkName)
	{
		initSparqlQuery = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI ?VALUE_NAME "
				+ "WHERE { ?URI a CLASS_NAME ; "
				+ "mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string ; "
				+ "PROPERTY_NAME ?VALUE_NAME . }";

		initCandidatesSqlQuery(classTables.get(0), "gid", limit);

		return checkSimplePropertyViaGUIOrID(classTables, classTableRows,
				className, propertyName, valueName, limit,
				comparisonOnResource, multipleValues, withCondition, proofId,
				checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the value of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param fragmentId
	 *            the fragment identifier of the URI pattern that includes the
	 *            comparison GUID
	 * @param limit
	 *            the result limit for the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaGUIDOnTheLeftWithFragment(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			String className, String propertyName, String valueName,
			String fragmentId, int limit, boolean comparisonOnResource,
			boolean multipleValues, String proofId, String checkName)
	{
		initSqlQueryForCheckSimpleProperty("gid", 1, true);

		return checkSimplePropertyViaID(classTables, classTableRows, className,
				propertyName, valueName, fragmentId, limit,
				comparisonOnResource, multipleValues, proofId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the value of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param fragmentId
	 *            the fragment identifier of the URI pattern that includes the
	 *            comparison ID
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit for the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaIDOnTheLeft(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			String className, String propertyName, String valueName,
			String fragmentId, int numberOfJoins, int limit,
			boolean comparisonOnResource, boolean multipleValues,
			String proofId, String checkName)
	{
		initSqlQueryForCheckSimpleProperty("id", numberOfJoins, true);

		return checkSimplePropertyViaID(classTables, classTableRows, className,
				propertyName, valueName, fragmentId, limit,
				comparisonOnResource, multipleValues, proofId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the value of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param fragmentId
	 *            the fragment identifier of the URI pattern that includes the
	 *            comparison ID
	 * @param limit
	 *            the result limit for the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkSimplePropertyViaIDOnTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			String className, String propertyName, String valueName,
			String fragmentId, int limit, boolean comparisonOnResource,
			boolean multipleValues, String proofId, String checkName)
	{
		initSqlQueryForCheckSimpleProperty("id", 1, false);

		return checkSimplePropertyViaID(classTables, classTableRows, className,
				propertyName, valueName, fragmentId, limit,
				comparisonOnResource, multipleValues, proofId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the value of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param fragmentId
	 *            the fragment identifier of the URI pattern that includes the
	 *            comparison ID
	 * @param limit
	 *            the result limit for the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkSimplePropertyViaID(ArrayList<String> classTables,
			ArrayList<String> classTableRows, String className,
			String propertyName, String valueName, String fragmentId,
			int limit, boolean comparisonOnResource, boolean multipleValues,
			String proofId, String checkName)
	{
		String initSparqlQuery2 = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?URI ?VALUE_NAME "
				+ "WHERE { ?URI rdf:type CLASS_NAME ; "
				+ "PROPERTY_NAME ?VALUE_NAME . "
				+ "FILTER regex(str(?URI), \"/ID_PLACEHOLDERFRAGMENT_ID\") } ";
		initSparqlQuery = initSparqlQuery2.replace("FRAGMENT_ID", fragmentId);

		initCandidatesSqlQuery(classTables.get(0), "id", limit);

		return checkSimplePropertyViaGUIOrID(classTables, classTableRows,
				className, propertyName, valueName, limit,
				comparisonOnResource, multipleValues, false, proofId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the value of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param limit
	 *            the result limit for the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param withCondition
	 *            indicates whether a condition is set or not
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkSimplePropertyViaGUIOrID(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			String className, String propertyName, String valueName, int limit,
			boolean comparisonOnResource, boolean multipleValues,
			boolean withCondition, String proofId, String checkName)
	{
		// TODO:

		String initSqlQuery2 = null;

		// init conditions where needed
		if (withCondition)
		{
			initCondition(condition);

			initSqlQuery2 = initSqlQuery
					.replace("CONDITION", sqlQueryCondition);
			initSqlQuery = initSqlQuery2;

		} else
		{
			initSqlQuery2 = initSqlQuery.replace("CONDITION", "");
			initSqlQuery = initSqlQuery2;
		}

		return checkSimpleProperty(classTables, classTableRows, className,
				propertyName, valueName, limit, comparisonOnResource,
				multipleValues, proofId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            For a SQL query with one inner join the first table of this
	 *            list might be the table of the left side of the INNER JOIN of
	 *            the SQL query and the second one might be the table of the
	 *            right side of the INNER JOIN of the SQL query
	 * @param classTableRows
	 *            For a SQL query with one inner join the first row this list
	 *            might be the row of of the table of the left side of the INNER
	 *            JOIN of the SQL query and the second one might be the row of
	 *            the table that delivers the values for the specific RDF
	 *            property
	 * @param className
	 *            the class name of the specific RDF class for the SPARQL query
	 * @param propertyName
	 *            the property name of the specific RDF property for the SPARQL
	 *            query
	 * @param valueName
	 *            the value name for the SQL and SPARQL query that is used for
	 *            the comparison
	 * @param limit
	 *            the result limit for the SQL query
	 * @param comparisonOnResource
	 *            if this value is set to 'true' the comparison values are RDF
	 *            resource; otherwise they are RDF literals.
	 * @param multipleValues
	 *            indicates whether multiple values for the query keys are
	 *            expected or not
	 * @param proofId
	 *            a hardcoded Id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This Id should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkSimpleProperty(ArrayList<String> classTables,
			ArrayList<String> classTableRows, String className,
			String propertyName, String valueName, int limit,
			boolean comparisonOnResource, boolean multipleValues,
			String proofId, String checkName)
	{
		resetQueryVars();
		initFailMsgs(checkName);

		TestResult testResult = null;
		this.limit = limit;

		String initSqlQuery = this.initSqlQuery;
		String initSqlQuery2 = replacePlaceholders(classTableRows, "CLASS_ROW",
				initSqlQuery);
		String initSqlQuery3 = replacePlaceholders(classTables, "CLASS",
				initSqlQuery2);
		String initSqlQuery4 = initSqlQuery3.replace("VALUE_NAME", valueName);
		String sqlQuery = null;

		String initSparqlQuery = this.initSparqlQuery;
		String initSparqlQuery2 = initSparqlQuery.replace("VALUE_NAME",
				valueName);
		String initSparqlQuery3 = initSparqlQuery2.replace("CLASS_NAME",
				className);
		String sparqlQuery = initSparqlQuery3.replace("PROPERTY_NAME",
				propertyName);

		int relationsCounter = 0;
		int currentRelationsCounter = 0;

		System.out
				.println("\n##################################################\n[EXEC]  "
						+ checkName
						+ "\n##################################################\n");

		// just execute the normal simple SQL query
		if (!multipleValues)
		{
			sqlQuery = initSqlQuery4.replace("LIMIT_PLACEHOLDER", Integer
					.toString(limit));

			Map<String, String> values = null;
			String valuesKey = null;

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

			// TODO: ?
			// add a hardcoded id, since one could fetch instances that have
			// no relations and the wondering about the results
			// values.put(proofId, "VALUE");

			if (values.size() == limit)
			{
				Iterator<String> iter = values.keySet().iterator();

				for (int i = 0; i < limit; i++)
				{
					sparqlRS = null;
					valuesKey = iter.next();
					currentRelationsCounter = 0;
					relationsCounter = 0;

					currentSparqlQuery = sparqlQuery.replace("ID_PLACEHOLDER",
							valuesKey);

					try
					{
						sparqlResultSet = runSPARQLQuery(currentSparqlQuery,
								SERVICE_ENDPOINT);

						sparqlRS = sparqlResultSet.getResultSet();

						Translator translator = null;
						String value = values.get(valuesKey);

						if (condition.getTranslatorClass() != null)
						{

							/*
							 * System.out.println(
							 * "[EXEC]  here we go in the Translator class case"
							 * );
							 */

							if (getTranslatorInstance(condition
									.getTranslatorClass()) != null)
							{
								translator = getTranslatorInstance(condition
										.getTranslatorClass());

								/*System.out
										.println("[EXEC]  here we go with an instatiated Translator class");*/

							}
						}

						while (sparqlRS.hasNext())
						{
							if (translator != null)
							{
								value = translator.toRDFValue(value);
							}

							if (comparisonOnResource)
							{
								String resourceURI = sparqlRS.next()
										.getResource(valueName).getURI();

								// check the URI against the id it should
								// contain
								if (resourceURI.endsWith("/" + value))
								{
									queryCounter++;
									currentRelationsCounter++;
								}

							} else
							{
								if (value.equals(sparqlRS.next().getLiteral(
										valueName).getString()))
								{
									queryCounter++;
									currentRelationsCounter++;
								}
							}

							relationsCounter++;
						}

						if (relationsCounter != 1)
						{
							return new TestResult(
									false,
									sparqlFailMsg
											+ ".\n"
											+ "The number of relations from the SPARQL query doesn't seem to be equal to the number of fetched rows from the SQL query.\n"
											+ "\nNumber of fetched rows from the SQL query = "
											+ values.size()
											/ limit
											+ ".\n Number of the values from the SPARQL query result = "
											+ currentRelationsCounter
											+ "\nNumber of relations from the SPARQL query = "
											+ relationsCounter + ".");
						}

						System.out
								.println("[EXEC]  The number of relations from the SPARQL query seem to be equal to the number of fetched rows from the SQL query.\n\t\t"
										+ "Number of values from the SQL query result = "
										+ values.size()
										/ limit
										+ ".\n\t\tNumber of the values from the SPARQL query result = "
										+ currentRelationsCounter
										+ ".\n\t\tRelations counter of the SPARQL result set = "
										+ relationsCounter + ".");

					} catch (Exception e)
					{
						System.out.println(e.getMessage());

						return new TestResult(false, sparqlFailMsg);
					}

					sparqlResultSet.close();
				}
			}

			testResult = new TestResult(queryCounter == limit,
					queryCounterFailMsg);
		}
		// prefetch some id values that are utilised in the proper SQL query
		else
		{
			Map<String, List<String>> relations = null;
			String relationsKey = null;

			int overallRelationsCounter = 0;

			try
			{
				System.out
						.println("[EXEC]  Prefetch some id values that are utilised in the proper SQL query.");

				sqlResultSet = runSQLQuery(candidatesSqlQuery);
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
					// init aliases map with empty lists
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

			// add a hardcoded id, since one could fetch instances that have
			// no relations and the wondering about the results
			relations.put(proofId, new ArrayList<String>());

			if (relations.size() == limit + 1)
			{
				Iterator<String> iter = relations.keySet().iterator();

				for (int j = 0; j < relations.size(); j++)
				{
					relationsKey = iter.next();
					sqlQuery = initSqlQuery4.replace("ID_PLACEHOLDER",
							relationsKey);
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
							// fill relations lists with relations
							while (sqlRS.next())
							{

								relations.get(sqlRS.getString("id")).add(
										sqlRS.getString(valueName));
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
					relationsKey = iter.next();
					currentRelationsCounter = 0;
					relationsCounter = 0;

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
									sparqlRS.next().getLiteral(valueName)
											.getString()))
							{
								queryCounter++;
								currentRelationsCounter++;
							}

							relationsCounter++;
						}

						if (currentRelationsCounter != relations.get(
								relationsKey).size())
						{
							return new TestResult(
									false,
									sparqlFailMsg
											+ ".\n The comparision of the values for the key \""
											+ relationsKey
											+ "\" between the SQL query and the SPARQL are showing a difference.\n"
											+ " Number of values from the SQL query result = "
											+ relations.get(relationsKey)
													.size()
											+ ".\n Number of the values from the SPARQL query result = "
											+ currentRelationsCounter
											+ ".\n Relations counter of the SPARQL result set = "
											+ relationsCounter + ".");
						}

						System.out
								.println("[EXEC]  The comparision of the values for the key \""
										+ relationsKey
										+ "\" between the SQL query and the SPARQL has the following results.\n\t\t"
										+ "Number of values from the SQL query result = "
										+ relations.get(relationsKey).size()
										+ ".\n\t\tNumber of the values from the SPARQL query result = "
										+ currentRelationsCounter
										+ ".\n\t\tRelations counter of the SPARQL result set = "
										+ relationsCounter + ".");

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

		return testResult;
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the GUID(s) for the check that are located on the
	 *            right side of the relation and is located on the right side of
	 *            the third INNER JOIN. The third one is located on the left
	 *            side of the INNER JOINs. The fourth one is located on the
	 *            right side of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation. The second one
	 *            delivers the GUID(s) for the check that are located on the
	 *            right side of the relation. The third one is the non-'id' row
	 *            of the first INNER JOIN. The forth one is the non-'id' row of
	 *            the second INNER JOIN. The fifth one is the non-'id' row of
	 *            the third INNER JOIN.
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
	 *            relation. The third one is the value name for the resource
	 *            GUIDs of the right side of the relation.
	 * @param rightSideFragmentId
	 *            the fragment id of the URI of the right side of the relation
	 *            that includes the instance id as well
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param proofID
	 *            a hardcoded GUID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This GUID
	 *            should usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIInversePropertyViaGUIDs(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, String rightSideFragmentId,
			int numberOfJoins, int limit, String proofID, String checkName)
	{
		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, true,
				true, false, false, true, numberOfJoins, limit, proofID, false,
				false, null, rightSideFragmentId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the id(s) for the check that are located on the right
	 *            side of the relation and is located on the right side of the
	 *            third INNER JOIN. The third one is located on the left side of
	 *            the INNER JOINs. The fourth one is located on the right side
	 *            of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
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
	 *            relation. The third one is the value name for the resource ids
	 *            of the right side of the relation.
	 * @param rightSideFragmentId
	 *            the fragment id of the URI of the right side of the relation
	 *            that includes the instance id as well
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param proofID
	 *            a hardcoded GUID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This GUID
	 *            should usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIInversePropertyViaGUIDOnTheLeftAndIDOnTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, String rightSideFragmentId,
			int numberOfJoins, int limit, String proofID, String checkName)
	{
		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, true,
				false, false, false, true, numberOfJoins, limit, proofID, true,
				false, null, rightSideFragmentId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the GUID(s) for the check that are located on the
	 *            right side of the relation and is located on the right side of
	 *            the third INNER JOIN. The third one is located on the left
	 *            side of the INNER JOINs. The fourth one is located on the
	 *            right side of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation. The second one
	 *            delivers the GUID(s) for the check that are located on the
	 *            right side of the relation. The third one is the non-'id' row
	 *            of the first INNER JOIN. The forth one is the non-'id' row of
	 *            the second INNER JOIN. The fifth one is the non-'id' row of
	 *            the third INNER JOIN.
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
	 *            relation. The third one is the value name for the resource
	 *            GUIDs of the right side of the relation.
	 * @param leftSideFragmentId
	 *            the fragment id of the URI of the left side of the relation
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param proofID
	 *            a hardcoded GUID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This GUID
	 *            should usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIInversePropertyViaIDonTheLeftAndGUIDonTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, String leftSideFragmentId,
			int numberOfJoins, int limit, String proofID, String checkName)
	{
		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, false,
				true, false, false, true, numberOfJoins, limit, proofID, false,
				false, leftSideFragmentId, null, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the id(s) for the check that are located on the right
	 *            side of the relation and is located on the right side of the
	 *            third INNER JOIN. The third one is located on the left side of
	 *            the INNER JOINs. The fourth one is located on the right side
	 *            of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
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
	 *            relation. The third one is the value name for the resource ids
	 *            of the right side of the relation.
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param condition
	 *            the condition object that includes specific values that are
	 *            used to initialise the condition statements
	 * @param proofID
	 *            a hardcoded GUID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This GUID
	 *            should usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIInversePropertyViaGUIDOnTheLeftAndURIOnTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, int numberOfJoins, int limit,
			Condition condition, String proofID, String checkName)
	{
		setCondition(condition);

		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, true,
				false, false, true, true, numberOfJoins, limit, proofID, true,
				true, null, null, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the GUID(s) for the check that are located on the
	 *            right side of the relation and is located on the right side of
	 *            the third INNER JOIN. The third one is located on the left
	 *            side of the INNER JOINs. The fourth one is located on the
	 *            right side of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation. The second one
	 *            delivers the GUID(s) for the check that are located on the
	 *            right side of the relation. The third one is the non-'id' row
	 *            of the first INNER JOIN. The forth one is the non-'id' row of
	 *            the second INNER JOIN. The fifth one is the non-'id' row of
	 *            the third INNER JOIN.
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
	 *            relation. The third one is the value name for the resource
	 *            GUIDs of the right side of the relation.
	 * @param rightSideFragmentId
	 *            the fragment id of the URI of the right side of the relation
	 *            that includes the instance id as well
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param condition
	 *            the condition object that includes specific values that are
	 *            used to initialise the condition statements
	 * @param proofID
	 *            a hardcoded GUID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This GUID
	 *            should usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIPropertyViaGUIDs(ArrayList<String> classTables,
			ArrayList<String> classTableRows, ArrayList<String> classNames,
			String propertyName, ArrayList<String> valueNames,
			String leftSideFragmentId, String rightSideFragmentId,
			int numberOfJoins, int limit, Condition condition, String proofID,
			String checkName)
	{
		setCondition(condition);

		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, true,
				true, false, false, false, numberOfJoins, limit, proofID,
				false, initCondition(condition), leftSideFragmentId,
				rightSideFragmentId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the id(s) for the check that are located on the right
	 *            side of the relation and is located on the right side of the
	 *            third INNER JOIN. The third one is located on the left side of
	 *            the INNER JOINs. The fourth one is located on the right side
	 *            of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
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
	 *            relation. The third one is the value name for the resource ids
	 *            of the right side of the relation.
	 * @param rightSideFragmentId
	 *            the fragment id of the URI of the right side of the relation
	 *            that includes the instance id as well
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param proofID
	 *            a hardcoded GUID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This GUID
	 *            should usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIPropertyViaGUIDOnTheLeftAndIDOnTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, String rightSideFragmentId,
			int numberOfJoins, int limit, String proofID, String checkName)
	{
		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, true,
				false, false, false, false, numberOfJoins, limit, proofID,
				true, false, null, rightSideFragmentId, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the id(s) for the check that are located on the right
	 *            side of the relation and is located on the right side of the
	 *            third INNER JOIN. The third one is located on the left side of
	 *            the INNER JOINs. The fourth one is located on the right side
	 *            of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
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
	 *            relation. The third one is the value name for the resource ids
	 *            of the right side of the relation.
	 * @param leftSideFragmentId
	 *            the fragment id of the URI of the left side of the relation
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param proofID
	 *            a hardcoded GUID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This GUID
	 *            should usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIPropertyViaIDOnTheLeftAndGUIDOnTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, String leftSideFragmentId,
			int numberOfJoins, int limit, String proofID, String checkName)
	{
		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, false,
				true, false, false, false, numberOfJoins, limit, proofID,
				false, false, leftSideFragmentId, null, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the id(s) for the check that are located on the right
	 *            side of the relation and is located on the right side of the
	 *            third INNER JOIN. The third one is located on the left side of
	 *            the INNER JOINs. The fourth one is located on the right side
	 *            of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
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
	 *            relation. The third one is the value name for the resource ids
	 *            of the right side of the relation.
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param condition
	 *            the condition object that includes specific values that are
	 *            used to initialise the condition statements
	 * @param proofID
	 *            a hardcoded GUID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This GUID
	 *            should usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIPropertyViaGUIDOnTheLeftAndURIOnTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, int numberOfJoins, int limit,
			Condition condition, String proofID, String checkName)
	{
		setCondition(condition);

		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, true,
				false, false, true, false, numberOfJoins, limit, proofID, true,
				true, null, null, checkName);
	}

	/**
	 * Fetches some instances from the DB and resolves the values of a specific
	 * property against the result of the related SPARQL query.
	 * 
	 * @param classTables
	 *            the list of table for the SQL query currently consists of 4
	 *            items. The first one delivers the GUID(s) for the check that
	 *            are located on the left side of the relation and is located on
	 *            the right side of the first INNER JOIN. The second one
	 *            delivers the id(s) for the check that are located on the right
	 *            side of the relation and is located on the right side of the
	 *            third INNER JOIN. The third one is located on the left side of
	 *            the INNER JOINs. The fourth one is located on the right side
	 *            of the second INNER JOIN.
	 * @param classTableRows
	 *            the list of table rows for the SQL query currently consists of
	 *            5 items. The first one delivers the GUID(s) for the check that
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
	 *            relation. The third one is the value name for the resource ids
	 *            of the right side of the relation.
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param condition
	 *            the condition object that includes specific values that are
	 *            used to initialise the condition statements
	 * @param proofID
	 *            a hardcoded ID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This ID should
	 *            usually deliver an appropriated result.
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIPropertyViaIDOnTheLeftAndURIOnTheRight(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, String leftSideFragmentId,
			int numberOfJoins, int limit, Condition condition, String proofID,
			String checkName)
	{
		setCondition(condition);

		return checkURIPropertyViaGUIDAndOrIDAndOrURI(classTables,
				classTableRows, classNames, propertyName, valueNames, false,
				false, false, true, false, numberOfJoins, limit, proofID, true,
				true, leftSideFragmentId, null, checkName);
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
	 *            relation. The third one is the value name for the resource
	 *            GUIDs of the right side of the relation.
	 * @param leftSideGUID
	 *            indicates whether a GUID is used on the left side of the
	 *            relation for the comparison or not; otherwise it is a simple
	 *            (table) id
	 * @param rightSideGUID
	 *            indicates whether a GUID is used on the right side of the
	 *            relation for the comparison or not; otherwise it is a simple
	 *            (table) id
	 * @param inverseProperty
	 *            indicates whether the property of the relation is an inverse
	 *            property or not; otherwise it is a 'normal' property
	 * @param numberOfJoins
	 *            indicates the number of joins of the SQL query
	 * @param limit
	 *            the result limit of the SQL query
	 * @param proofID
	 *            a hardcoded id, since one could fetch instances that have no
	 *            relations and then wondering about the results. This id should
	 *            usually deliver an appropriated result.
	 * @param comparisonOnResource
	 *            indicates whether the values for the comparison are RDF
	 *            resources or RDF literals
	 * @param withCondition
	 *            indicates whether a condition is set or not
	 * @param leftSideFragmentId
	 *            the fragment id of the URI of the left side of the relation
	 * @param rightSideFragmentId
	 *            the fragment id of the URI of the right side of the relation
	 *            that includes the instance id as well
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	public TestResult checkURIPropertyViaGUIDAndOrIDAndOrURI(
			ArrayList<String> classTables, ArrayList<String> classTableRows,
			ArrayList<String> classNames, String propertyName,
			ArrayList<String> valueNames, boolean leftSideGUID,
			boolean rightSideGUID, boolean leftSideURI, boolean rightSideURI,
			boolean inverseProperty, int numberOfJoins, int limit,
			String proofID, boolean comparisonOnResource,
			boolean withCondition, String leftSideFragmentId,
			String rightSideFragmentId, String checkName)
	{
		boolean URIComparison = false;

		String leftSideFragmentIdAndRightSideGUID = Utils.DEFAULT_PREFIXES
				+ "SELECT DISTINCT ?VALUE_NAME1 ?VALUE_NAME3 "
				+ "WHERE { ?VALUE_NAME1 rdf:type CLASS_NAME1 ; "
				+ "PROPERTY_NAME ?VALUE_NAME2 . "
				+ "?VALUE_NAME2 rdf:type CLASS_NAME2 ; "
				+ "mo:musicbrainz_guid ?VALUE_NAME3 . "
				+ "FILTER regex(str(?VALUE_NAME1), \"/ID_PLACEHOLDERLEFT_SIDE_FRAGMENT_ID\") } ";

		// to init the candidates SQL query and the SPARQL query
		if (leftSideGUID)
		{
			initCandidatesSqlQuery(classTables.get(0), "gid", limit);

			// left side = GUID + right side = GUID +
			// right side GUID property seems to be available
			if (rightSideGUID && !rightSideURI && rightSideFragmentId == null)
			{
				// left side GUID property seems to be available
				if (leftSideFragmentId == null)
				{
					initSparqlQuery = Utils.DEFAULT_PREFIXES
							+ "SELECT DISTINCT ?VALUE_NAME1 ?VALUE_NAME3 "
							+ "WHERE { ?VALUE_NAME1 rdf:type CLASS_NAME1 ; "
							+ "PROPERTY_NAME ?VALUE_NAME2 ; "
							+ "mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string . "
							+ "?VALUE_NAME2 rdf:type CLASS_NAME2 ; "
							+ "mo:musicbrainz_guid ?VALUE_NAME3 . } ";
				}
				// left side GUID property doesn't seem to be available, check
				// has to be done via extracting the GUID from the URI
				else
				{
					initSparqlQuery = leftSideFragmentIdAndRightSideGUID;
				}
			} else
			{
				// left side = GUID + right side = ID
				// or
				// left side = GUID + right side = GUID +
				// right side GUID property doesn't seem to be available, check
				// has to be
				// done via extracting the GUID from the URI
				if ((!rightSideGUID && !rightSideURI)
						|| (rightSideGUID && !rightSideURI && rightSideFragmentId != null))
				{
					// VALUE_NAME2 == VALUE_NAME3 in this case
					initSparqlQuery = Utils.DEFAULT_PREFIXES
							+ "SELECT DISTINCT ?VALUE_NAME1 ?VALUE_NAME3 "
							+ "WHERE { ?VALUE_NAME1 rdf:type CLASS_NAME1 ; "
							+ "PROPERTY_NAME ?VALUE_NAME2 ; "
							+ "mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string . "
							+ "?VALUE_NAME2 rdf:type CLASS_NAME2 . } ";

				}
				// left side = GUID + right side = URI
				else if (rightSideURI)
				{
					// VALUE_NAME2 == VALUE_NAME3 in this case
					initSparqlQuery = Utils.DEFAULT_PREFIXES
							+ "SELECT DISTINCT ?VALUE_NAME1 ?VALUE_NAME3 "
							+ "WHERE { ?VALUE_NAME1 rdf:type CLASS_NAME1 ; "
							+ "PROPERTY_NAME ?VALUE_NAME2 ; "
							+ "mo:musicbrainz_guid \"ID_PLACEHOLDER\"^^xsd:string . "
							+ "CONDITION} ";

					URIComparison = true;
				}
			}
		}
		// left side != URI => left side must be ID
		else if (!leftSideURI)
		{
			initCandidatesSqlQuery(classTables.get(0), "id", limit);

			// TODO: parametrise leftsideFragmentId
			// left side = ID + right side = GUID
			if (rightSideGUID && !rightSideURI)
			{
				initSparqlQuery = leftSideFragmentIdAndRightSideGUID;
			}
			// left side = ID + right side = ID
			else if (!rightSideGUID && !rightSideURI)
			{
				// TODO
			}
			// left side = ID + right side = URI
			else if (rightSideURI)
			{
				initSparqlQuery = Utils.DEFAULT_PREFIXES
						+ "SELECT DISTINCT ?VALUE_NAME1 ?VALUE_NAME3 "
						+ "WHERE { ?VALUE_NAME1 rdf:type CLASS_NAME1 ; "
						+ "PROPERTY_NAME ?VALUE_NAME2 . "
						+ "FILTER regex(str(?VALUE_NAME1), \"/ID_PLACEHOLDERLEFT_SIDE_FRAGMENT_ID\") } ";

				URIComparison = true;
			}
		}
		// left side = URI
		else
		{
			// TODO: handle 'left side = URI' -> right side = GUID or ID or URI
		}

		// to init the SQL query
		if (inverseProperty)
		{
			// TODO:
			switch (numberOfJoins)
			{
			case 0:
				break;
			case 1:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
						+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
						+ "FROM musicbrainz.CLASS1 "
						+ "INNER JOIN musicbrainz.CLASS2 ON CLASS2.CLASS_ROW3 = CLASS1.id "
						+ "WHERE musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
				break;
			case 2:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
						+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
						+ "FROM musicbrainz.CLASS3 "
						+ "INNER JOIN musicbrainz.CLASS1 ON CLASS1.CLASS_ROW3 = CLASS3.id "
						+ "INNER JOIN musicbrainz.CLASS2 ON CLASS2.id = CLASS3.CLASS_ROW4 "
						+ "WHERE musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
				break;
			case 3:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
						+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
						+ "FROM musicbrainz.CLASS3 "
						+ "INNER JOIN musicbrainz.CLASS1 ON CLASS1.CLASS_ROW3 = CLASS3.id "
						+ "INNER JOIN musicbrainz.CLASS4 ON CLASS4.CLASS_ROW4 = CLASS3.id "
						+ "INNER JOIN musicbrainz.CLASS2 ON CLASS2.id = CLASS4.CLASS_ROW5 "
						+ "WHERE musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
				break;
			case 4:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
						+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
						+ "FROM musicbrainz.CLASS2 "
						+ "INNER JOIN musicbrainz.CLASS3 ON CLASS3.CLASS_ROW3 = CLASS2.id "
						+ "INNER JOIN musicbrainz.CLASS4 ON CLASS3.CLASS_ROW4 = CLASS4.id "
						+ "INNER JOIN musicbrainz.CLASS5 ON CLASS4.CLASS_ROW5 = CLASS5.id "
						+ "INNER JOIN musicbrainz.CLASS1 ON CLASS3.CLASS_ROW6 = CLASS1.id "
						+ "WHERE CONDITION "
						+ "musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
				break;
			default:
				break;
			}
		} else
		{
			// currently for normal properties (non-inverse properties)
			switch (numberOfJoins)
			{
			case 0:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS2_id, "
						+ "musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id "
						+ "FROM musicbrainz.CLASS1 "
						+ "WHERE musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
				break;
			case 1:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
						+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
						+ "FROM musicbrainz.CLASS1 "
						+ "INNER JOIN musicbrainz.CLASS2 ON CLASS1.CLASS_ROW3 = CLASS2.id "
						+ "WHERE musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
				break;
			case 2:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
						+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
						+ "FROM musicbrainz.CLASS3 "
						+ "INNER JOIN musicbrainz.CLASS1 ON CLASS3.CLASS_ROW3 = CLASS1.id "
						+ "INNER JOIN musicbrainz.CLASS2 ON CLASS2.id = CLASS3.CLASS_ROW4 "
						+ "WHERE musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
				break;
			case 3:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
						+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
						+ "FROM musicbrainz.CLASS3 "
						+ "INNER JOIN musicbrainz.CLASS1 ON CLASS3.CLASS_ROW3 = CLASS1.id "
						+ "INNER JOIN musicbrainz.CLASS4 ON CLASS3.CLASS_ROW4 = CLASS4.id "
						+ "INNER JOIN musicbrainz.CLASS2 ON CLASS4.id = CLASS2.CLASS_ROW5 "
						+ "WHERE musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";
				break;
			case 4:
				initSqlQuery = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS CLASS1_id, "
						+ "musicbrainz.CLASS2.CLASS_ROW2 AS CLASS2_id "
						+ "FROM musicbrainz.CLASS1 "
						+ "INNER JOIN musicbrainz.CLASS3 ON CLASS3.CLASS_ROW3 = CLASS1.id "
						+ "INNER JOIN musicbrainz.CLASS4 ON CLASS3.CLASS_ROW4 = CLASS4.id "
						+ "INNER JOIN musicbrainz.CLASS5 ON CLASS4.CLASS_ROW5 = CLASS5.id "
						+ "INNER JOIN musicbrainz.CLASS2 ON CLASS3.CLASS_ROW6 = CLASS2.id "
						+ "WHERE CONDITION"
						+ "musicbrainz.CLASS1.CLASS_ROW1 = 'ID_PLACEHOLDER'";

				// TODO: define CONDITION as optional part:
				// "musicbrainz.CONDITION_CLASS.CONDITION_ROW = CONDITION_VALUE AND "
				break;
			default:
				break;
			}
		}

		String initSqlQuery2 = null;
		String initSparqlQuery2 = null;

		// init conditions where needed
		if (withCondition)
		{
			initCondition(condition);

			initSqlQuery2 = initSqlQuery
					.replace("CONDITION", sqlQueryCondition);
			initSqlQuery = initSqlQuery2;

			if (condition instanceof URICondition)
			{
				initSparqlQuery2 = initSparqlQuery.replace("CONDITION",
						sparqlQueryCondition);
				initSparqlQuery = initSparqlQuery2;
			} else
			{
				initSparqlQuery2 = initSparqlQuery.replace("CONDITION", "");
				initSparqlQuery = initSparqlQuery2;
			}
		} else
		{
			initSqlQuery2 = initSqlQuery.replace("CONDITION", "");
			initSqlQuery = initSqlQuery2;

			initSparqlQuery2 = initSparqlQuery.replace("CONDITION", "");
			initSparqlQuery = initSparqlQuery2;
		}

		// init left-side-fragment-id where needed
		if (leftSideFragmentId != null)
		{
			String initSparqlQuery3 = initSparqlQuery.replace(
					"LEFT_SIDE_FRAGMENT_ID", leftSideFragmentId);
			initSparqlQuery = initSparqlQuery3;
		}

		// if a right-side-fragment-id is set, then it must be a comparision on
		// resource
		if (rightSideFragmentId != null)
		{
			comparisonOnResource = true;
		}

		return checkProperty(classTables, classTableRows, classNames,
				propertyName, valueNames, limit, proofID, comparisonOnResource,
				URIComparison, rightSideFragmentId, checkName);
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
	 *            relation. The third one is the value name for the resource ids
	 *            of the right side of the relation.
	 * @param limit
	 *            the result limit of the SQL query
	 * @param proofID
	 *            a hardcoded ID, since one could fetch instances that have no
	 *            relations and then wondering about the results. This id should
	 *            usually deliver an appropriated result.
	 * @param rightSideFragmentId
	 *            the fragment id of the URI of the right side of the relation
	 *            that includes the instance id as well
	 * @param checkName
	 *            the name of the specific check
	 * @return the result of the test (incl. fail message)
	 */
	private TestResult checkProperty(ArrayList<String> classTables,
			ArrayList<String> classTableRows, ArrayList<String> classNames,
			String propertyName, ArrayList<String> valueNames, int limit,
			String proofID, boolean comparisonOnResource,
			boolean URIComparison, String rightSideFragmentId, String checkName)
	{
		resetQueryVars();
		initFailMsgs(checkName);

		String sqlQuery = null;
		this.limit = limit;

		String initSparqlQuery = this.initSparqlQuery;
		String initSparqlQuery2 = replacePlaceholders(classNames, "CLASS_NAME",
				initSparqlQuery);
		String initSparqlQuery3 = replacePlaceholders(valueNames, "VALUE_NAME",
				initSparqlQuery2);
		String sparqlQuery = initSparqlQuery3.replace("PROPERTY_NAME",
				propertyName);

		Map<String, List<String>> relations = null;
		String relationsKey = null;

		int overallRelationsCounter = 0;
		int currentRelationsCounter = 0;
		int relationsCounter = 0;

		System.out
				.println("\n##################################################\n[EXEC]  "
						+ checkName
						+ "\n##################################################\n");

		try
		{
			System.out
					.println("[EXEC]  Prefetch some id values that are utilised in the proper SQL query.");

			sqlResultSet = runSQLQuery(candidatesSqlQuery);
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

			String initSqlQuery = this.initSqlQuery;
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
				currentRelationsCounter = 0;
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
						if (comparisonOnResource)
						{
							ArrayList<String> resources = (ArrayList<String>) relations
									.get(relationsKey);
							String resourceURI = sparqlRS.next().getResource(
									valueNames.get(2)).getURI();

							/*
							 * System.out.println("[EXEC]  resource URI: " +
							 * resourceURI);
							 */

							if (URIComparison)
							{
								/*
								 * System.out
								 * .println("[EXEC]  let's do a URI comparision"
								 * );
								 */

								Translator translator = null;
								String uriFromSqlQuery = null;

								if (condition.getTranslatorClass() != null)
								{
									/*
									 * System.out.println(
									 * "[EXEC]  here we go in the Translator class case"
									 * );
									 */

									if (getTranslatorInstance(condition
											.getTranslatorClass()) != null)
									{
										translator = getTranslatorInstance(condition
												.getTranslatorClass());

										/*
										 * System.out.println(
										 * "[EXEC]  here we go with an instatiated Translator class"
										 * );
										 */
									}
								}

								// check the URI against the URI from SQL query
								for (int k = 0; k < resources.size(); k++)
								{
									if (translator != null)
									{
										uriFromSqlQuery = translator
												.toRDFValue(resources.get(k));
									} else
									{
										uriFromSqlQuery = resources.get(k);
									}

									if (resourceURI.equals(uriFromSqlQuery))

									{
										queryCounter++;
										currentRelationsCounter++;
										break;
									}
								}
							} else
							{
								/*
								 * System.out.println(
								 * "[EXEC]  here we go with a right side URI + fragment id comparison"
								 * );
								 */

								if (rightSideFragmentId == null)
								{
									// init right side fragment id with a
									// default value
									rightSideFragmentId = "#_";
								}

								// check the URI against the id it should
								// contain
								for (int m = 0; m < resources.size(); m++)
								{
									if (resourceURI.contains("/"
											+ resources.get(m)
											+ rightSideFragmentId))

									{
										queryCounter++;
										currentRelationsCounter++;
										break;
									}
								}
							}
						} else
						{
							/*
							 * System.out.println(
							 * "[EXEC]  here we go with a simple full literal comparison"
							 * );
							 */

							// just a simple full literal comparison
							if (relations.get(relationsKey).contains(
									sparqlRS.next().getLiteral(
											valueNames.get(2)).getString()))
							{
								queryCounter++;
								currentRelationsCounter++;
							}
						}

						relationsCounter++;
					}

					if (currentRelationsCounter != relations.get(relationsKey)
							.size())
					{
						return new TestResult(
								false,
								sparqlFailMsg
										+ ".\n The comparision of the values for the key \""
										+ relationsKey
										+ "\" between the SQL query and the SPARQL are showing a difference.\n"
										+ " Number of values from the SQL query result = "
										+ relations.get(relationsKey).size()
										+ ".\n Number of the values from the SPARQL query result = "
										+ currentRelationsCounter
										+ ".\n Relations counter of the SPARQL result set = "
										+ relationsCounter + ".");
					}

					System.out
							.println("[EXEC]  The comparision of the values for the key \""
									+ relationsKey
									+ "\" between the SQL query and the SPARQL has the following results.\n\t\t"
									+ "Number of values from the SQL query result = "
									+ relations.get(relationsKey).size()
									+ ".\n\t\tNumber of the values from the SPARQL query result = "
									+ currentRelationsCounter
									+ ".\n\t\tRelations counter of the SPARQL result set = "
									+ relationsCounter + ".");
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

	/**
	 * Initialises the candidates SQL query that fetches some candidates via
	 * [gu]id.
	 * 
	 * @param classTable
	 *            the table for the SQL query
	 * @param classTableRow
	 *            the row for the SQL query
	 * @param limit
	 *            the result limit for the SQL query
	 */
	private void initCandidatesSqlQuery(String classTable,
			String classTableRow, int limit)
	{
		// fetch some arbitrary [gu]ids for the beginning
		String initCandidatesSqlQuery = "SELECT musicbrainz.CLASS.CLASS_ROW AS id "
				+ "FROM musicbrainz.CLASS " + "LIMIT LIMIT_PLACEHOLDER";
		String initCandidatesSqlQuery2 = initCandidatesSqlQuery.replace(
				"LIMIT_PLACEHOLDER", Integer.toString(limit));
		String initCandidatesSqlQuery3 = initCandidatesSqlQuery2.replace(
				"CLASS_ROW", classTableRow);
		candidatesSqlQuery = initCandidatesSqlQuery3.replace("CLASS",
				classTable);
	}

	/**
	 * Initialises the SQL query for the check-simple-property method
	 * 
	 * @param classIdTableRow
	 *            the row of that includes the ids of the resources whose values
	 *            are use for a property value comparison
	 * @param left
	 *            the position of the table that includes the id row
	 */
	private void initSqlQueryForCheckSimpleProperty(String classIdTableRow,
			int numberOfJoins, boolean left)
	{
		String initSqlQuery2 = null;
		String initSqlQuery3 = null;
		String initSqlQuery4 = null;

		switch (numberOfJoins)
		{
		case 0:
			initSqlQuery4 = "SELECT musicbrainz.CLASS1.CLASS_ROW1 AS VALUE_NAME, "
					+ "musicbrainz.CLASS1.ID_CLASS_ROW AS id "
					+ "FROM musicbrainz.CLASS1 CONDITIONLIMIT LIMIT_PLACEHOLDER";
			break;
		case 1:
			initSqlQuery2 = "SELECT musicbrainz.SELECT_CLASS1.CLASS_ROW2 AS VALUE_NAME, "
					+ "musicbrainz.SELECT_CLASS2.ID_CLASS_ROW AS id "
					+ "FROM musicbrainz.CLASS1 "
					+ "INNER JOIN musicbrainz.CLASS2  ON CLASS1.CLASS_ROW1 = CLASS2.id CONDITIONLIMIT LIMIT_PLACEHOLDER";

			if (left)
			{
				initSqlQuery3 = initSqlQuery2
						.replace("SELECT_CLASS1", "CLASS2");
				initSqlQuery4 = initSqlQuery3
						.replace("SELECT_CLASS2", "CLASS1");
			} else
			{
				initSqlQuery3 = initSqlQuery2
						.replace("SELECT_CLASS1", "CLASS1");
				initSqlQuery4 = initSqlQuery3
						.replace("SELECT_CLASS2", "CLASS2");
			}
			break;
		case 2:
			initSqlQuery4 = "SELECT musicbrainz.CLASS3.CLASS_ROW3 AS VALUE_NAME, "
					+ "musicbrainz.CLASS1.ID_CLASS_ROW AS id "
					+ "FROM musicbrainz.CLASS3 "
					+ "INNER JOIN musicbrainz.CLASS2  ON CLASS2.CLASS_ROW1 = CLASS3.id "
					+ "INNER JOIN musicbrainz.CLASS1 ON CLASS2.CLASS_ROW2 = CLASS1.id "
					+ "WHERE CONDITIONmusicbrainz.CLASS1.ID_CLASS_ROW = 'ID_PLACEHOLDER'";
		default:
			break;
		}

		initSqlQuery = initSqlQuery4.replace("ID_CLASS_ROW", classIdTableRow);
	}

	/**
	 * Initialises an optional condition for a SQL and/or a SPARQL of a test
	 * 
	 * @param condition
	 *            the condition object that includes specific values that are
	 *            used to initialise the condition statements
	 * @return true, if a condition was set; otherwise false
	 */
	private boolean initCondition(Condition condition)
	{
		if (condition != null)
		{
			this.condition = condition;

			initSQLCondition(condition.getConditionClass(), condition
					.getConditionRow(), condition.getConditionValue());

			if (condition instanceof URICondition)
			{
				URICondition uriCondition = (URICondition) condition;

				initSPAQLCondition(uriCondition.getConditionProperty(),
						uriCondition.getConditionPropertyValue());
			}

			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * Initialises a condition of an SQL query
	 * 
	 * @param conditionClass
	 *            the specific table of the condition for the SQL query
	 * @param conditionRow
	 *            the specific row of the condition for the SQL query
	 * @param conditionValue
	 *            the specific value (static (!)) of the condition for the SQL
	 *            query
	 */
	private void initSQLCondition(String conditionClass, String conditionRow,
			String conditionValue)
	{
		if (conditionClass != null)
		{
			String initSQLQueryCondition = null;

			// small hacky workaround :\
			if (initSqlQuery == null)
			{
				initSqlQuery = "";
			}

			// look whether there is already another condition set
			if (initSqlQuery.contains("WHERE"))
			{
				initSQLQueryCondition = "musicbrainz.CONDITION_CLASS.CONDITION_ROWCOMPARISONCONDITION_VALUE AND ";
			} else
			{
				initSQLQueryCondition = "WHERE musicbrainz.CONDITION_CLASS.CONDITION_ROWCOMPARISONCONDITION_VALUE ";
			}

			String initSQLQueryCondition2 = initSQLQueryCondition.replace(
					"CONDITION_CLASS", conditionClass);

			String initSQLQueryCondition3 = null;

			if (condition.isComparison())
			{
				initSQLQueryCondition3 = initSQLQueryCondition2.replace(
						"COMPARISON", " = ");
			} else
			{
				initSQLQueryCondition3 = initSQLQueryCondition2.replace(
						"COMPARISON", " ");
			}

			String initSQLQueryCondition4 = initSQLQueryCondition3.replace(
					"CONDITION_ROW", conditionRow);
			sqlQueryCondition = initSQLQueryCondition4.replace(
					"CONDITION_VALUE", conditionValue);
		} else
		{
			sqlQueryCondition = "";
		}
	}

	/**
	 * Initialises a condition of an SPAQL query
	 * 
	 * @param conditionProperty
	 *            the property resource of the condition relation
	 * @param conditionPropertyValue
	 *            the value of the condition relation
	 */
	private void initSPAQLCondition(String conditionProperty,
			String conditionPropertyValue)
	{
		/*
		 * System.out.println("[EXEC]  conditionProperty: " + conditionProperty
		 * + " :: conditionPropertyValue: " + conditionPropertyValue);
		 */

		if (!conditionProperty.equals(""))
		{
			String initSPAQLQueryCondition = "?VALUE_NAME2 CONDITION_PROPERTY CONDITION_PROPERTY_VALUE . ";
			String initSPAQLQueryCondition2 = initSPAQLQueryCondition.replace(
					"CONDITION_PROPERTY_VALUE", conditionPropertyValue);
			sparqlQueryCondition = initSPAQLQueryCondition2.replace(
					"CONDITION_PROPERTY", conditionProperty);
		} else
		{
			sparqlQueryCondition = "";
		}
	}

	private Translator getTranslatorInstance(String translatorClassString)
	{
		Class translatorClass = null;
		Translator translator = null;
		try
		{
			translatorClass = Class.forName(translatorClassString);
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try
		{
			Constructor c = translatorClass.getConstructor(new Class[] {});
			translator = (Translator) c.newInstance(new Object[] {});
		} catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return translator;
	}

	private Condition getCondition()
	{
		return condition;
	}

	private void setCondition(Condition condition)
	{
		if (condition != null)
		{
			this.condition = condition;
		}
	}
}
