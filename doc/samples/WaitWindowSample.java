import org.netbeans.jemmy.*;
import org.netbeans.jemmy.explorer.*;
import org.netbeans.jemmy.operators.*;

public class WaitWindowSample implements Scenario {
    public int runIt(Object param) {
        try {
            //start application
            new ClassReference("org.netbeans.jemmy.explorer.GUIBrowser").startApplication();
            //wait frame
            new JFrameOperator("GUI Browser");
        } catch(Exception e) {
            e.printStackTrace();
            return(1);
        }
        return(0);
    }
    public static void main(String[] argv) {
        String[] params = {"WaitWindowSample"};
        org.netbeans.jemmy.Test.main(params);
    }
}
