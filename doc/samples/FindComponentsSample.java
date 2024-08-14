import java.io.FileNotFoundException;
import org.netbeans.jemmy.*;
import org.netbeans.jemmy.explorer.*;
import org.netbeans.jemmy.operators.*;
import org.netbeans.jemmy.util.Dumper;

public class FindComponentsSample implements Scenario {
    public int runIt(Object param) {
        try {
            new ClassReference("org.netbeans.jemmy.explorer.GUIBrowser").startApplication();
            JFrameOperator mainFrame = new JFrameOperator("GUI Browser");

            new JButtonOperator(mainFrame, "Reload in").push();
            new JLabelOperator(mainFrame, "Reloaded");

            //find JTree
            //we can find any:
            JTreeOperator tree = new JTreeOperator(mainFrame);

            //or by selected node:
            tree.selectPath(tree.findPath("GUI Browser", "|"));
            new JTreeOperator(mainFrame, "GUI Browser");
            
            //and couple of text components:
            new JTextComponentOperator(mainFrame, "0");
            new JTextComponentOperator(mainFrame, "toString");

            //but one of them is a test field:
            new JTextFieldOperator(mainFrame);

            //and another one is a text area:
            new JTextAreaOperator(mainFrame);

            //it's good idea to search buttons by text:
            new AbstractButtonOperator(mainFrame, "View");

            //but not necessarily
            new JButtonOperator(mainFrame);
        } catch(Exception e) {
            e.printStackTrace();
            try {
                Dumper.dumpAll("/tmp/aaa.xml");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            return(1);
        }
        return(0);
    }
    public static void main(String[] argv) {
        String[] params = {"FindComponentsSample"};
        org.netbeans.jemmy.Test.main(params);
    }
}
