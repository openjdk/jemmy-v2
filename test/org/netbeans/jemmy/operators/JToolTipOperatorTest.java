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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolTip;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.TimeoutExpiredException;
import org.netbeans.jemmy.Timeouts;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JToolTipOperatorTest {


    private JFrame frame = null;

    @BeforeClass
    protected void setUp() throws Exception {
        frame = new JFrame();
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        Timeouts timeouts = Operator.getEnvironmentOperator().getTimeouts();
        timeouts.setTimeout("JToolTipOperator.WaitToolTipTimeout", 5000);
    }

    @AfterClass
    protected void tearDown() throws Exception {
        frame.setVisible(false);
        frame.dispose();
    }

    @Test
    public void testToolTip() {
        final String TOLLTIP_TEXT = "A simple Tooltip";
        final String LABEL_TEXT = "Roll over here to see a tooltip";
        JLabel label = new JLabel(LABEL_TEXT);
        label.setToolTipText(TOLLTIP_TEXT);
        label.setBounds(0, 0, 200, 200);
        frame.add(label);
        frame.setVisible(true);
        JLabelOperator labelOperator = new JLabelOperator(label);
        JToolTipOperator toolTipOperator = new JToolTipOperator(
                labelOperator.showToolTip());
        toolTipOperator.waitTipText(TOLLTIP_TEXT);

        // Testing different constructors
        new JToolTipOperator();
        new JToolTipOperator(labelOperator);
        new JToolTipOperator(TOLLTIP_TEXT);
        ComponentChooser chooser = comp -> ((JLabel)((JToolTip)comp).
                getComponent()).getText().equals(LABEL_TEXT);
        new JToolTipOperator(chooser);
        new JToolTipOperator(labelOperator, TOLLTIP_TEXT);
        new JToolTipOperator(labelOperator, chooser);

        labelOperator.clickMouse();
        try {
            JToolTipOperator.waitJToolTip();
            Assert.fail();
        } catch (TimeoutExpiredException e) {
        }
    }

    @Test
    public void testToolTipConstructorsNegativeScenarios() {
        final String LABEL_TEXT = "Random Text";
        JLabelOperator dummyLabel = new JLabelOperator(new JLabel());
        ComponentChooser chooser = comp -> ((JLabel)((JToolTip)comp).
                getComponent()).getText().equals(LABEL_TEXT);
        try {
            new JToolTipOperator(dummyLabel);
            Assert.fail();
        } catch (TimeoutExpiredException e) {
        }

        try {
            new JToolTipOperator(LABEL_TEXT);
            Assert.fail();
        } catch (TimeoutExpiredException e) {
        }

        try {

            new JToolTipOperator(chooser);
            Assert.fail();
        } catch (TimeoutExpiredException e) {
        }

        try {
            new JToolTipOperator(dummyLabel, LABEL_TEXT);
            Assert.fail();
        } catch (TimeoutExpiredException e) {
        }

        try {
            new JToolTipOperator(dummyLabel, chooser);
            Assert.fail();
        } catch (TimeoutExpiredException e) {
        }
    }
}
