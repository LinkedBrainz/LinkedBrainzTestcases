package linkedbrainz.testcases.model;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * A model class for SPARQL result sets. Holds the result set and the related
 * query execution object.
 * 
 * @author zazi
 * 
 */
public class SPARQLResultSet
{

	private ResultSet resultSet = null;
	private QueryExecution queryExecution = null;

	public SPARQLResultSet(ResultSet resultSet, QueryExecution queryExecution)
	{
		this.resultSet = resultSet;
		this.queryExecution = queryExecution;
	}

	public ResultSet getResultSet()
	{
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet)
	{
		this.resultSet = resultSet;
	}

	public QueryExecution getQueryExecution()
	{
		return queryExecution;
	}

	public void setQueryExecution(QueryExecution queryExecution)
	{
		this.queryExecution = queryExecution;
	}

	/**
	 * Closes the related query execution object.
	 */
	public void close()
	{
		if (queryExecution != null)
		{
			queryExecution.close();
		}
	}
}
