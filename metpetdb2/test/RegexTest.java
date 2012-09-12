import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;



public class RegexTest extends TestCase {
	
	public void testQuote()
	{
		Pattern quotePattern = Pattern.compile("\\QSample Number\\E");
		Matcher matcher = quotePattern.matcher("Sample Number");
		
		assertTrue(matcher.matches());

	}

}
