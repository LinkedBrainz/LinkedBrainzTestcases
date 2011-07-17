package linkedbrainz.testcases.model;

/**
 * Small helper class that extends tha basic Condition helper class to handle
 * URI-specific conditions.
 * 
 * @author zazi
 * 
 */
public class URICondition extends Condition
{
	private String originalBaseURI;
	private String linkedDataBaseURI;

	public URICondition(String conditionClass, String conditionRow,
			String conditionValue, String originalBaseURI,
			String linkedDataBaseURI)
	{
		super(conditionClass, conditionRow, conditionValue);
		this.originalBaseURI = originalBaseURI;
		this.linkedDataBaseURI = linkedDataBaseURI;
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

}
