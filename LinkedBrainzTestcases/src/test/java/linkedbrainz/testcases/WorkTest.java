package linkedbrainz.testcases;

import static org.junit.Assert.*;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

/**
 * 
 * @author zazi
 *
 */
public class WorkTest
{
	
	public static junit.framework.Test suite() 
	{ 
	    return new JUnit4TestAdapter(TrackTest.class); 
	}

}
