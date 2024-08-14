import org.netbeans.jemmy.*;
import org.netbeans.jemmy.explorer.*;
import org.netbeans.jemmy.operators.*;

public class ResourceSample implements Scenario {
    public int runIt(Object param) {
        try {
            //load bundle
            Bundle bundle = new Bundle();
            bundle.loadFromFile(System.getProperty("user.dir") +
                                System.getProperty("file.separator") +
                                "resourcesample.txt");

            new ClassReference(bundle.getResource("guibrowser.main_class")).
                startApplication();

            JFrameOperator mainFrame = 
                new JFrameOperator(bundle.getResource("guibrowser.main_window"));

            new JButtonOperator(mainFrame, bundle.getResource("guibrowser.reload_button")).push();
            new JLabelOperator(mainFrame, bundle.getResource("guibrowser.reloaded_label"));
            
            new JTreeOperator(mainFrame).selectRow(1);
            
            new JButtonOperator(mainFrame, bundle.getResource("show_window_hierarchy")).push();

            new JFrameOperator(1);

        } catch(Exception e) {
            e.printStackTrace();
            return(1);
        }
        return(0);
    }
    public static void main(String[] argv) {
        String[] params = {"ResourceSample"};
        org.netbeans.jemmy.Test.main(params);
    }
}
