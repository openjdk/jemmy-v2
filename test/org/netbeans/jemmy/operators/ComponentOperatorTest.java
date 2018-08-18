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

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.netbeans.jemmy.TimeoutExpiredException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ComponentOperatorTest {


    private JFrame frame = null;

    @BeforeClass
    protected void setUp() throws Exception {
        frame = new JFrame();
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
    }

    @AfterClass
    protected void tearDown() throws Exception {
        frame.setVisible(false);
        frame.dispose();
    }

    @Test
    public void testWaitComponentLocationOnScreen() {
        final int locationDelta = 50;
        final String LABEL_TEXT = "Sample Text";
        JLabel label = new JLabel(LABEL_TEXT);
        frame.add(label);
        frame.setVisible(true);
        JLabelOperator labelOperator = new JLabelOperator(label);

        Point currentLocation = labelOperator.getLocation();
        Point currentScreenLocation = labelOperator.getLocationOnScreen();
        Point newScreenLocation = new Point(currentScreenLocation.x
                + locationDelta, currentScreenLocation.y  + locationDelta);
        labelOperator.setLocation(currentLocation.x + locationDelta,
                currentLocation.y + locationDelta);
        labelOperator.waitComponentLocationOnScreen(newScreenLocation);

        // Negative scenario
        try {
            labelOperator.waitComponentLocationOnScreen(new Point(0, 0));
            Assert.fail();
        } catch (TimeoutExpiredException e) {
        }
    }
}
