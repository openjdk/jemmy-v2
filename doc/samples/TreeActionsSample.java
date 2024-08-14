import org.netbeans.jemmy.*;
import org.netbeans.jemmy.explorer.*;
import org.netbeans.jemmy.operators.*;

public class TreeActionsSample implements Scenario {
    public int runIt(Object param) {
        try {
            new ClassReference("org.netbeans.jemmy.explorer.GUIBrowser").startApplication();
            JFrameOperator mainFrame = new JFrameOperator("GUI Browser");

            new JButtonOperator(mainFrame, "Reload In").push();
            new JLabelOperator(mainFrame, "Reloaded");

            JTreeOperator tree = new JTreeOperator(mainFrame);

            //click in the middle of the tree
            tree.clickMouse();

            //collapse node
            tree.collapsePath(tree.findPath("", "|"));

            //expand node
            tree.expandPath(tree.findPath("", "|"));

            //select node
            tree.selectPath(tree.findPath("GUI Browser", "|"));

        } catch(Exception e) {
            e.printStackTrace();
            return(1);
        }
        return(0);
    }
    public static void main(String[] argv) {
        String[] params = {"TreeActionsSample"};
        org.netbeans.jemmy.Test.main(params);
    }
}
