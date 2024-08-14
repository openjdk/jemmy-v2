import org.netbeans.jemmy.*;
import org.netbeans.jemmy.explorer.*;
import org.netbeans.jemmy.operators.*;

public class TableActionsSample implements Scenario {
    public int runIt(Object param) {
        try {
            new ClassReference("org.netbeans.jemmy.explorer.GUIBrowser").startApplication();
            JFrameOperator mainFrame = new JFrameOperator("GUI Browser");

            new JButtonOperator(mainFrame, "Reload In").push();
            new JLabelOperator(mainFrame, "Reloaded");

            JTreeOperator tree = new JTreeOperator(mainFrame);
            tree.selectPath(tree.findPath("GUI Browser", "|"));

            new JButtonOperator(mainFrame, "View").push();

            JFrameOperator viewFrame = new JFrameOperator("GUIBrowser");

            new JTabbedPaneOperator(viewFrame).selectPage("Properties");

            //find table
            JTableOperator table = new JTableOperator(viewFrame);

            //find row
            int titleRow = table.findCellRow("GUI Browser");
            
            //select cell containing window title
            table.selectCell(titleRow, 1);
                        
            //change table text
            //table.changeCellObject(titleRow, 1, "That was window title :)");

        } catch(Exception e) {
            e.printStackTrace();
            return(1);
        }
        return(0);
    }
    public static void main(String[] argv) {
        String[] params = {"TableActionsSample"};
        org.netbeans.jemmy.Test.main(params);
    }
}
