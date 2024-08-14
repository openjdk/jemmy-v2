import javax.swing.tree.TreePath;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.JemmyProperties;

import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JListOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JTreeOperator;

/*
 * Here is an example how Jemmy could be used for GUI application testing.
 * This examples tests some basic functionality of junit.swingui.TestRunner GUI.
 *
 * Test uses MyTest which contains two tests: one of them passes, another one
 * fails.
 *
 * What it does is:
 * - testWrongTestName test:
 *   Types unexisting test, pushs "Run" name and checks that TestRunner shows
 *   correct status.
 * - testMyTest test
 *   Runs MyTest test and checks footer and failed test list.
 *   Switch to tree page, check that failed test is selected.
 * - testGoodSubTest
 *   Selects and reruns passed test. Checks footer.
 * - testBadSubTest
 *   Selects and reruns failed test. Checks footer.
 */
public class RunnerTest extends TestCase {
    public RunnerTest(String name) {
        super(name);
    }
    public void testWrongTestName() {
        //find main window
        JFrameOperator mainWindow = new JFrameOperator("JUnit");

        //and text field
        JTextFieldOperator testNameField = new JTextFieldOperator(mainWindow);

        //type unexisting test name
        testNameField.clearText();
        testNameField.typeText("wrong.test.name");

        //push "Run" button
        new JButtonOperator(mainWindow, "Run").pushNoBlock();

        //check footer
        new JTextFieldOperator(mainWindow, "Class not found \"wrong.test.name\"");
    }
    public void testMyTest() {
        //find main window
        JFrameOperator mainWindow = new JFrameOperator("JUnit");

        //and text field
        JTextFieldOperator testNameField = new JTextFieldOperator(mainWindow);

        //type a test name
        //MyTest class has two methods
        testNameField.clearText();
        testNameField.typeText("MyTest");

        //push "Run" button.
        new JButtonOperator(mainWindow, "Run").push();

        //synchronization.
        //we just know that we will see both "2/2" and
        //"Finished" labels (those label are really textfields)
        new JTextFieldOperator(mainWindow, "Finished:");
        new JTextFieldOperator(mainWindow, "2/2");

        //check that list contains "testFail(MyTest)"
        new JListOperator(mainWindow, "testFail(MyTest)");

        //switch to hierarchy page
        JTabbedPaneOperator resultTab = new JTabbedPaneOperator(mainWindow);
        resultTab.selectPage("Test Hierarchy");

        //check that right (failed) node is selected
        JTreeOperator testTree = new JTreeOperator(mainWindow, "testFail", -1, 0);
    }
    public void testGoodSubTest() {
        //find main window
        JFrameOperator mainWindow = new JFrameOperator("JUnit");

        //and tree
        JTreeOperator testTree = new JTreeOperator(mainWindow);

        //select failed test and run it again
        TreePath failTest = testTree.findPath("testPass", "|");
        testTree.selectPath(failTest);
        new JButtonOperator(mainWindow, "Run", 1).push();

        //check footer
        new JTextFieldOperator(mainWindow, "testPass(MyTest) was successful");
    }
    public void testBadSubTest() {
        //find main window
        JFrameOperator mainWindow = new JFrameOperator("JUnit");

        //and tree
        JTreeOperator testTree = new JTreeOperator(mainWindow);

        //select passed test and run it again
        TreePath failTest = testTree.findPath("testFail", "|");
        testTree.selectPath(failTest);
        new JButtonOperator(mainWindow, "Run", 1).push();

        //check footer
        new JTextFieldOperator(mainWindow, "testFail(MyTest) had a failure");
    }
    public static Test suite() {
        //start the application first
        //ClassReference uses reflection so we don't need
        //to really import application classes
        try {
            new ClassReference("junit.swingui.TestRunner").startApplication();

            //increase timeouts values to see what's going on
            //otherwise everything's happened very fast
            JemmyProperties.getCurrentTimeouts().loadDebugTimeouts();
        } catch(Exception e) {
            e.printStackTrace();
        }

        //create suite
        TestSuite suite = new TestSuite();
        suite.addTest(new RunnerTest("testWrongTestName"));
        suite.addTest(new RunnerTest("testMyTest"));
        suite.addTest(new RunnerTest("testGoodSubTest"));
        suite.addTest(new RunnerTest("testBadSubTest"));
        return(suite);
    }
}
