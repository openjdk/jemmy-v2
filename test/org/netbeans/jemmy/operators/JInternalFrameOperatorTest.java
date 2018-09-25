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

import static org.netbeans.jemmy.operators.JInternalFrameOperator.CLOSE_BUTTON_TOOLTIP;
import static org.netbeans.jemmy.operators.JInternalFrameOperator.MAXIMIZE_BUTTON_TOOLTIP;
import static org.netbeans.jemmy.operators.JInternalFrameOperator.MINIMIZE_BUTTON_TOOLTIP;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.LookAndFeelProvider;
import org.netbeans.jemmy.util.Platform;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class JInternalFrameOperatorTest {

    private JFrameOperator frameOper;

    private JInternalFrameOperator internalFrameOper;

    private JDesktopPane desktop;

    private final static String OSX_EXCEPT_MESSAGE = "Jemmy doesn't support"
            + " getting or initializing title related operators on Mac OSx";

    private void setUp() throws Exception {
        JFrame frame = new JFrame();
        desktop = new JDesktopPane();
        frame.setContentPane(desktop);
        JemmyProperties.setCurrentDispatchingModel(
                JemmyProperties.getCurrentDispatchingModel());
        JInternalFrame internalFrame = new JInternalFrame(
                "JInternalFrameOperatorTest", true, true, true, true);
        internalFrame.setName("JInternalFrameOperatorTest");
        internalFrame.setSize(200, 200);
        internalFrame.setVisible(true);
        desktop.add(internalFrame);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frameOper = new JFrameOperator();
        internalFrameOper = new JInternalFrameOperator(internalFrame);
        internalFrameOper.setVerification(true);
    }

    @AfterMethod
    protected void tearDown() throws Exception {
        frameOper.setVisible(false);
        frameOper.dispose();
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testConstructors(String lookAndFeel) throws Exception {
        UIManager.setLookAndFeel(lookAndFeel);
        setUp();
        new JInternalFrameOperator(frameOper);
        new JInternalFrameOperator(frameOper, "JInternalFrameOperatorTest");
        new JInternalFrameOperator(frameOper,
                comp -> "JInternalFrameOperatorTest".equals(comp.getName()));

    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testIconify(String lookAndFeel) throws Exception {
        UIManager.setLookAndFeel(lookAndFeel);
        setUp();
        internalFrameOper.iconify();
        internalFrameOper.deiconify();
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testMaximize(String lookAndFeel) throws Exception {
        UIManager.setLookAndFeel(lookAndFeel);
        setUp();
        internalFrameOper.maximize();
        internalFrameOper.demaximize();
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testMove(String lookAndFeel) throws Exception {
        UIManager.setLookAndFeel(lookAndFeel);
        setUp();
        Point originalLocation = internalFrameOper.getLocation();
        internalFrameOper.move(150, 150);
        internalFrameOper.move(originalLocation.x, originalLocation.y);
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testResize(String lookAndFeel) throws Exception {
        UIManager.setLookAndFeel(lookAndFeel);
        setUp();
        Dimension originaSize = internalFrameOper.getSize();
        internalFrameOper.resize(250, 250);
        internalFrameOper.resize(originaSize.width, originaSize.height);
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testActivate(String lookAndFeel) throws Exception {
        UIManager.setLookAndFeel(lookAndFeel);
        setUp();
        internalFrameOper.activate();
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testTitleButtons(String lookAndFeel) throws Exception {
        UIManager.setLookAndFeel(lookAndFeel);
        setUp();
        if(!Platform.isOSX() && !"Motif".equals(UIManager.getLookAndFeel().getID())) {
            // Close, Maximize, and Minimize buttons are adding along with the
            // construction of internal frame itself
            JInternalFrame interanlFrame1 = new JInternalFrame(
                    "JInternalFrameButtonTest1", true, true, true, true);
            verifyTitleButtons(interanlFrame1);

            // Close, Maximize, and Minimize buttons are adding after the
            // construction of internal frame by using APIs
            JInternalFrame interanlFrame2 = new JInternalFrame(
                    "JInternalFrameButtonTest2", false, false, false, false);
            interanlFrame2.setClosable(true);
            interanlFrame2.setMaximizable(true);
            interanlFrame2.setIconifiable(true);
            verifyTitleButtons(interanlFrame2);
        } else if (Platform.isOSX()){
            JInternalFrame interanlFrame = new JInternalFrame(
                    "JInternalFrameButtonTest3", true, true, true, true);
            interanlFrame.setSize(200, 200);
            interanlFrame.setVisible(true);
            desktop.add(interanlFrame);
            JInternalFrameOperator interanlFrameOper = new JInternalFrameOperator(interanlFrame);
            try {
                interanlFrameOper.getMaximizeButton();
                fail();
            } catch (UnsupportedOperationException e) {
                assertEquals(e.getMessage(), OSX_EXCEPT_MESSAGE);
            }
            try {
                interanlFrameOper.getMinimizeButton();
                fail();
            } catch (UnsupportedOperationException e) {
                assertEquals(e.getMessage(), OSX_EXCEPT_MESSAGE);
            }
            try {
                interanlFrameOper.getCloseButton();
                fail();
            } catch (UnsupportedOperationException e) {
                assertEquals(e.getMessage(), OSX_EXCEPT_MESSAGE);
            }
        }
    }

    private void verifyTitleButtons(JInternalFrame interanlFrame) {
        interanlFrame.setSize(200, 200);
        interanlFrame.setVisible(true);
        desktop.add(interanlFrame);
        JInternalFrameOperator interanlFrameOper = new JInternalFrameOperator(interanlFrame);

        // Verify title buttons tooltip texts
        assertEquals(interanlFrameOper.getCloseButton().getToolTipText(),
                CLOSE_BUTTON_TOOLTIP);
        assertEquals(interanlFrameOper.getMaximizeButton().getToolTipText(),
                MAXIMIZE_BUTTON_TOOLTIP);
        assertEquals(interanlFrameOper.getMinimizeButton().getToolTipText(),
                MINIMIZE_BUTTON_TOOLTIP);

        // Verify different actions using title buttons
        interanlFrameOper.getMaximizeButton().push();
        interanlFrameOper.waitMaximum(true);
        interanlFrameOper.getMaximizeButton().push();
        interanlFrameOper.waitMaximum(false);
        interanlFrameOper.getMinimizeButton().push();
        interanlFrameOper.waitIcon(true);
        interanlFrameOper.deiconify();
        interanlFrameOper.waitIcon(false);
        interanlFrameOper.getCloseButton().push();

        desktop.remove(interanlFrame);
        interanlFrame.dispose();
    }

}
