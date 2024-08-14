import org.netbeans.jemmy.*;
import org.netbeans.jemmy.explorer.*;
import org.netbeans.jemmy.operators.*;
import org.netbeans.jemmy.util.*;

public class GrabImageAndXMLSample implements Scenario {
    public int runIt(Object param) {
        try {
            new ClassReference("org.netbeans.jemmy.explorer.GUIBrowser").startApplication();
            JFrameOperator mainFrame = new JFrameOperator("GUI Browser");
            new JMenuBarOperator(mainFrame).pushMenuNoBlock("Tools|Properties", "|");

            //grab image
            PNGEncoder.captureScreen(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "screen.png");
            
            //grab component state
            Dumper.dumpAll(System.getProperty("user.dir") +
                           System.getProperty("file.separator") +
                           "dump.xml");
        } catch(Exception e) {
            e.printStackTrace();
            return(1);
        }
        return(0);
    }
    public static void main(String[] argv) {
        String[] params = {"GrabImageAndXMLSample"};
        org.netbeans.jemmy.Test.main(params);
    }
}
