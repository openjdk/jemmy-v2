import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class MyTest extends TestCase {
    public MyTest(String name) {
	super(name);
    }
    public void testPass() {
	System.out.println("Test supposed to be passed");
    }
    public void testFail() {
	System.out.println("Test supposed to be failed");
	fail();
    }
    /*
    public static TestSuite suite() {
	TestSuite suite = new TestSuite();
	suite.addTest(new MyTest("testPass"));
	suite.addTest(new MyTest("testFail"));
	return(suite);
    }
    */
}
