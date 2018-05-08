/*
 * Copyright (c) 1997, 2018, Oracle and/or its affiliates. All rights reserved.
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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.awt.Checkbox;
import java.awt.EventQueue;
import java.awt.Frame;
import java.util.concurrent.atomic.AtomicBoolean;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OperatorTest {

    /**
     * Stores the frame.
     */
    private Frame frame;

    AtomicBoolean isWaitStateOnEDT = new AtomicBoolean(true);

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @BeforeClass
    protected void setUp() throws Exception {
        frame = new Frame();
        frame.setSize(100, 100);
    }

    /**
     * Cleanup after testing.
     *
     * @throws Exception when serious error occurs.
     */
    @AfterClass
    protected void tearDown() throws Exception {
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }

    @Test
    public void testWaitState() {
        CheckboxOperator checkBox = createCheckBox();
        isWaitStateOnEDT.set(true);
        checkBox.waitState(comp -> {
            isWaitStateOnEDT.set(EventQueue.isDispatchThread());
            return ((Checkbox) comp).getState();
        });
        assertFalse(isWaitStateOnEDT.get());
    }

    @Test
    public void testWaitStateOnQueue() {
        CheckboxOperator checkBox = createCheckBox();
        isWaitStateOnEDT.set(false);
        checkBox.waitStateOnQueue(comp -> {
            isWaitStateOnEDT.set(EventQueue.isDispatchThread());
            return ((Checkbox) comp).getState();
        });
        assertTrue(isWaitStateOnEDT.get());
    }

    private CheckboxOperator createCheckBox() {
        frame.add(new Checkbox("Checkbox", false));
        frame.setVisible(true);
        CheckboxOperator checkBox = new CheckboxOperator(new FrameOperator(frame));
        checkBox.setState(true);
        return checkBox;
    }
}
