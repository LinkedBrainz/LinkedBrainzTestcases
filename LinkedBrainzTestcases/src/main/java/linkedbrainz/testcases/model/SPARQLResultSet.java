package linkedbrainz.testcases.model;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;

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

	public void close()
	{
		if (queryExecution != null)
		{
			queryExecution.close();
		}
	}
}
