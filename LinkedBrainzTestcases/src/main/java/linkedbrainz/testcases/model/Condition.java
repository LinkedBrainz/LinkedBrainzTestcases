package linkedbrainz.testcases.model;

/**
 * Small helper class to hold the value for a condition of an SQL query.
 * 
 * @author zazi
 * 
 */
public class Condition
{
	private String conditionClass;
	private String conditionRow;
	private String conditionValue;
	private String translatorClass = null;

	public Condition(String translatorClass)
	{
		this.translatorClass = translatorClass;
	}

	public Condition(String conditionClass, String conditionRow,
			String conditionValue)
	{
		this.conditionClass = conditionClass;
		this.conditionRow = conditionRow;
		this.conditionValue = conditionValue;
	}

	public Condition(String conditionClass, String conditionRow,
			String conditionValue, String translatorClass)
	{
		this.conditionClass = conditionClass;
		this.conditionRow = conditionRow;
		this.conditionValue = conditionValue;
		this.translatorClass = translatorClass;
	}

	public String getConditionClass()
	{
		return conditionClass;
	}

	public void setConditionClass(String conditionClass)
	{
		this.conditionClass = conditionClass;
	}

	public String getConditionRow()
	{
		return conditionRow;
	}

	public void setConditionRow(String conditionRow)
	{
		this.conditionRow = conditionRow;
	}

	public String getConditionValue()
	{
		return conditionValue;
	}

	public void setConditionValue(String conditionValue)
	{
		this.conditionValue = conditionValue;
	}

	public String getTranslatorClass()
	{
		return translatorClass;
	}

	public void setTranslatorClass(String translatorClass)
	{
		this.translatorClass = translatorClass;
	}

}
