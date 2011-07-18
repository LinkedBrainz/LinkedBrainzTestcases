package linkedbrainz.testcases.model;

/**
 * Small helper class that extends the basic Condition helper class to handle
 * URI-specific conditions.
 * 
 * @author zazi
 * 
 */
public class URICondition extends Condition
{
	private String originalBaseURI = "";
	private String linkedDataBaseURI = "";
	private String originalFragementId = "";
	private String linkedDataFragmentId = "";

	public URICondition(String conditionClass, String conditionRow,
			String conditionValue, String originalBaseURI,
			String linkedDataBaseURI)
	{
		super(conditionClass, conditionRow, conditionValue);
		this.originalBaseURI = originalBaseURI;
		this.linkedDataBaseURI = linkedDataBaseURI;
	}

	public URICondition(String conditionClass, String conditionRow,
			String conditionValue, String originalBaseURI,
			String linkedDataBaseURI, String originalFragmentId,
			String linkedDataFragmentId)
	{
		super(conditionClass, conditionRow, conditionValue);
		this.originalBaseURI = originalBaseURI;
		this.linkedDataBaseURI = linkedDataBaseURI;
		this.originalFragementId = originalFragmentId;
		this.linkedDataFragmentId = linkedDataFragmentId;
	}

	public String getOriginalBaseURI()
	{
		return originalBaseURI;
	}

	public void setOriginalBaseURI(String originalBaseURI)
	{
		this.originalBaseURI = originalBaseURI;
	}

	public String getLinkedDataBaseURI()
	{
		return linkedDataBaseURI;
	}

	public void setLinkedDataBaseURI(String linkedDataBaseURI)
	{
		this.linkedDataBaseURI = linkedDataBaseURI;
	}

	public String getOriginalFragementId()
	{
		return originalFragementId;
	}

	public void setOriginalFragementId(String originalFragementId)
	{
		this.originalFragementId = originalFragementId;
	}

	public String getLinkedDataFragmentId()
	{
		return linkedDataFragmentId;
	}

	public void setLinkedDataFragmentId(String linkedDataFragmentId)
	{
		this.linkedDataFragmentId = linkedDataFragmentId;
	}

}
