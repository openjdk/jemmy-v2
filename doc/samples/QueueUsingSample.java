import org.netbeans.jemmy.*;
import org.netbeans.jemmy.explorer.*;
import org.netbeans.jemmy.operators.*;

public class QueueUsingSample implements Scenario {
    public int runIt(Object param) {
        try {
            //start application
            new ClassReference("org.netbeans.jemmy.explorer.GUIBrowser").startApplication();
            //wait frame
            JFrameOperator mainFrame = new JFrameOperator("GUI Browser");
            
            //wait tree in it
            JTreeOperator tree = new JTreeOperator(mainFrame);
            
            //get soumething to be shown
            new JButtonOperator(mainFrame, "Reload In").pushNoBlock();
            
            //wait for the operation to be completed
            new QueueTool().waitEmpty(100);
            
            //since queue is empty, frame tree has already been loaded and displayed.
            //print number of lines. Should be 2
            System.out.println("Lines in the tree: " + tree.getRowCount());

            //select something
            tree.selectRow(1);
            
            //push menu
            new JButtonOperator(mainFrame, "View").push();

            //wait for queue to be empty
            new QueueTool().waitEmpty(100);

            //since queue is empty, frame has already been created and displayed.
            //find (not wait) second window using low-level functionality
            System.out.println("Second frame title: " + 
                    JFrameOperator.findJFrame(new JFrameOperator.FrameByTitleFinder(""), 1).getTitle());
        } catch(Exception e) {
            e.printStackTrace();
            return(1);
        }
        return(0);
    }
    public static void main(String[] argv) {
        String[] params = {"QueueUsingSample"};
        org.netbeans.jemmy.Test.main(params);
    }
}
