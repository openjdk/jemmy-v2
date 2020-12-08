/*
 * Copyright (c) 2018, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.netbeans.jemmy.operators;

import static java.util.Arrays.stream;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.LookAndFeelProvider;
import org.netbeans.jemmy.TimeoutExpiredException;

import static org.netbeans.jemmy.drivers.DriverManager.WINDOW_DRIVER_ID;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.netbeans.jemmy.drivers.DriverManager;
import org.netbeans.jemmy.drivers.WindowDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.beans.PropertyVetoException;

public class JInternalFrameOperatorCloseTest {

    private JFrameOperator frameOper;

    private JInternalFrameOperator internalFrameOper;
    private WindowDriver oldDriver;

    @BeforeMethod
    private void setUp(Object[] args) throws Exception {
        UIManager.setLookAndFeel((String)args[0]);
        JFrame frame = new JFrame();
        JDesktopPane desktop = new JDesktopPane();
        frame.setContentPane(desktop);
        JemmyProperties.setCurrentDispatchingModel(
                JemmyProperties.getCurrentDispatchingModel());
        JInternalFrame internalFrame = new JInternalFrame("JInternalFrameOperatorTest", true, true, true, true);
        internalFrame.setName("JInternalFrameOperatorTest");
        internalFrame.setSize(200, 200);
        internalFrame.setVisible(true);
        desktop.add(internalFrame);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frameOper = new JFrameOperator();
        //override windows driver for the operator to not do anything to close
        oldDriver = DriverManager.getWindowDriver(JInternalFrameOperator.class);
        DriverManager.setDriver(WINDOW_DRIVER_ID, new WindowDriver() {
            @Override
            public void activate(ComponentOperator oper) {
                oldDriver.activate(oper);
            }

            @Override
            public void requestClose(ComponentOperator oper) {
                //do nothing here
            }

            @Override
            public void requestCloseAndThenHide(ComponentOperator oper) {
                //do nothing here
            }

            @Override
            public void close(ComponentOperator oper) {
                //do nothing here
            }

            @Override
            public void move(ComponentOperator oper, int x, int y) {
                oldDriver.move(oper, x, y);
            }

            @Override
            public void resize(ComponentOperator oper, int width, int height) {
                oldDriver.resize(oper, width, height);
            }
        }, JInternalFrameOperator.class);
        internalFrameOper = new JInternalFrameOperator(frameOper);
        internalFrameOper.setVerification(true);
    }

    @AfterMethod
    protected void tearDown() throws Exception {
        frameOper.setVisible(false);
        frameOper.dispose();
        DriverManager.setDriver(WINDOW_DRIVER_ID, oldDriver, JInternalFrameOperator.class);
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testClose(String lookAndFeel) throws Exception {
        try {
            //trying to close the uncloseable frame
            //expected to fail by timeout, hence decreasing timeout
            internalFrameOper.getTimeouts().setTimeout("ComponentOperator.WaitStateTimeout", 5000);
            internalFrameOper.close();
            //that would mean that the exception is not thrown
            fail();
        } catch (TimeoutExpiredException e) {
            //make sure the exception is coming from the right place
            assertTrue(stream(e.getStackTrace()).anyMatch(ste ->
                    ste.getClassName().equals(JInternalFrameOperator.class.getName()) &&
                    ste.getMethodName().equals("waitClosed")));
            System.out.println("This exception has been caught, as expected:");
            e.printStackTrace(System.out);
        }
    }
}
