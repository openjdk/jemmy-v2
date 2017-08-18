/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
package org.netbeans.jemmy.scenario;

import org.netbeans.jemmy.operators.AbstractButtonOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JProgressBarOperator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.log4testng.Logger;

import javax.swing.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class ButtonsAndTooltipsTest {
    private static Logger logger = Logger.getLogger(ButtonsAndTooltipsTest.class);
    private static ButtonsAndTooltipsApp app;
    private static JFrameOperator wino;
    private static JFrame win;
    private static JLabelOperator lbo;
    private static JProgressBarOperator progress;

    @BeforeClass
    public static void setup() {
        Util.testInfraSetup();
        app = new ButtonsAndTooltipsApp();
        app.display();
        wino = new JFrameOperator("ButtonsAndTooltipsApp");
        win = (JFrame) wino.getSource();
        lbo = new JLabelOperator(wino, "Button has not been pushed yet");
        progress = new JProgressBarOperator(wino);
    }

    @AfterClass
    public static void tearDown() {
        app.setVisible(false);
        app.dispose();
    }

    @Test
    public void testPush() {
        for(int i = 1; i < 4; i++) {
            for(int j = 1; j < 4; j++) {
                String bText = Integer.toString(i) + "-" + Integer.toString(j);
                JButtonOperator bo = new JButtonOperator((JButton)JButtonOperator.findJComponent(win, bText, false, true));
                AbstractButtonOperator abo = new AbstractButtonOperator(wino, i*4 + j);
                JButtonOperator bo2 = new JButtonOperator(wino, i*4 + j);
                if (bo.getSource() != abo.getSource() ||
                        bo.getSource() != bo2.getSource()) {
                    logger.error("Wrong component found:");
                    logger.error(bo.getSource());
                    logger.error(abo.getSource());
                    logger.error(bo2.getSource());
                    fail();
                }
                JToolTip tt = bo.showToolTip();
                if(!tt.getTipText().equals(bText + " button")) {
                    logger.error("Wrong tip text: " + tt.getTipText());
                    logger.error("Expected      : " + bText + " button");
                    fail();
                }
                bo.push();
                lbo.waitText("Button \"" + bText + "\" has been pushed");
                progress.waitValue(bText);
                progress.waitValue(i*4 + j + 1);
            }
        }

    }

    @Test
    public void testLookups() {
        final JButtonOperator bbo = new JButtonOperator(wino, "0-0");
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                        bbo.getAccessibleContext().
                                setAccessibleDescription("A button to check different finding approaches");
                    bbo.setText("New Text");
                } catch(InterruptedException e) {
                }
            }
        }).start();
        bbo.waitText("New Text");
        assertEquals(wino.
                findSubComponent(new AbstractButtonOperator.
                        AbstractButtonByLabelFinder("New Text")),
                bbo.getSource(),
                "Wrong button found");
    }
}
