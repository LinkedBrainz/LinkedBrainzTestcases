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
	private String conditionProperty = "";
	private String conditionPropertyValue = "";

	public URICondition(String conditionClass, String conditionRow,
			String conditionValue, String conditionProperty,
			String conditionPropertyValue, String translatorClass)
	{
		super(conditionClass, conditionRow, conditionValue, translatorClass);
		this.conditionProperty = conditionProperty;
		this.conditionPropertyValue = conditionPropertyValue;
	}

	public String getConditionProperty()
	{
		return conditionProperty;
	}

	public void setConditionProperty(String conditionProperty)
	{
		this.conditionProperty = conditionProperty;
	}

	public String getConditionPropertyValue()
	{
		return conditionPropertyValue;
	}

	public void setConditionPropertyValue(String conditionPropertyValue)
	{
		this.conditionPropertyValue = conditionPropertyValue;
	}

}
