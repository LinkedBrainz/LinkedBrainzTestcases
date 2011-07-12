package linkedbrainz.testcases.model;

/**
 * Small helper class to transfer a test result to a test class.
 * 
 * @author zazi
 *
 */
public class TestResult
{
	
	private boolean isSucceeded;
	private String failMsg;
	
	public TestResult(boolean isSucceeded, String failMsg)
	{
		this.isSucceeded = isSucceeded;
		this.failMsg = failMsg;
	}
	
	public boolean isSucceeded()
	{
		return isSucceeded;
	}
	public void setIsSucceeded(boolean isSucceeded)
	{
		this.isSucceeded = isSucceeded;
	}
	public String getFailMsg()
	{
		return failMsg;
	}
	public void setFailMsg(String failMsg)
	{
		this.failMsg = failMsg;
	}
	
	

}
