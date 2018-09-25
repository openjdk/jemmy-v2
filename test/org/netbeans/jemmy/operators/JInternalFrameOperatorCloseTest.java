/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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

import static org.testng.Assert.fail;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.LookAndFeelProvider;
import org.netbeans.jemmy.TimeoutExpiredException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class JInternalFrameOperatorCloseTest {

    private JFrameOperator frameOper;

    private JInternalFrameOperator internalFrameOper;

    private void setUp() throws Exception {
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
        internalFrameOper = new JInternalFrameOperator(frameOper);
        internalFrameOper.setVerification(true);
    }

    @AfterMethod
    protected void tearDown() throws Exception {
        frameOper.setVisible(false);
        frameOper.dispose();
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testClose(String lookAndFeel) throws Exception {
        UIManager.setLookAndFeel(lookAndFeel);
        setUp();
        InternalFrameListener listener = new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                try {
                    this.wait(80000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
            }
        };

        // Making not to close the fame for 1 minute and expecting TimeoutExpiredException
        // from waitClosed()
        try {
            internalFrameOper.addInternalFrameListener(listener);
            internalFrameOper.close();
            fail();
        } catch (TimeoutExpiredException e) {
        } finally {
            internalFrameOper.removeInternalFrameListener(listener);
        }

        // Really closing the frame
        internalFrameOper.close();
    }

}
