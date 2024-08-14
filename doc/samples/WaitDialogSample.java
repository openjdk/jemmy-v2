import org.netbeans.jemmy.*;
import org.netbeans.jemmy.explorer.*;
import org.netbeans.jemmy.operators.*;

public class WaitDialogSample implements Scenario {
    public int runIt(Object param) {
        try {
            //start application
            new ClassReference("org.netbeans.jemmy.explorer.GUIBrowser").startApplication();
            //wait frame
            JFrameOperator mainFrame = new JFrameOperator("GUI Browser");
            //push menu
            //pushMenuNoBlock is used, because dialog is modal
            //see tutorial for more information
            new JButtonOperator(mainFrame, "Dump").pushNoBlock();
            //wait dialog
            new JDialogOperator(mainFrame, "Save");
        } catch(Exception e) {
            e.printStackTrace();
            return(1);
        }
        return(0);
    }
    public static void main(String[] argv) {
        String[] params = {"WaitDialogSample"};
        org.netbeans.jemmy.Test.main(params);
    }
}
