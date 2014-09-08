package org.telosys.tools.test.velocity.context;


/**
 * Context class for Velocity tests
 *
 */
public class MyClass 
{
	public MyClass() 
	{
	}
    
    public String toStringMethodCodeLines( int iLeftMargin )
    {
    	return "Method 'toStringMethodCodeLinesWithKey(" + iLeftMargin + ")' executed.";
    }
    
    public String toStringMethodCodeLinesWithKey( int iLeftMargin, String embeddedIdName )
    {
    	//return "Method 'toStringMethodCodeLinesWithKey(" + iLeftMargin + ", " + embeddedIdName + ")' executed.";
    	return null ;
    }
    
}
