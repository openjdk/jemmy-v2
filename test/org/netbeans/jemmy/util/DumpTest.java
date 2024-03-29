/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
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
package org.netbeans.jemmy.util;

import org.netbeans.jemmy.UIStatus;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.scenario.ComboBoxesAndListApp;
import org.netbeans.jemmy.scenario.Util;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import static org.testng.Assert.assertTrue;

public class DumpTest {
    private static JFrameOperator fo;

    @BeforeClass
    public static void setup() throws InvocationTargetException, NoSuchMethodException, IOException {
        Util.testInfraSetup();
        new ComboBoxesAndListApp().display();
        fo = new JFrameOperator("ComboBoxesAndListTest");
    }

    @Test
    public void common() {
        StringWriter out = new StringWriter();
        Point point = new Point(1, 2);
        UIStatus.mouseMoved(fo, point);
        Dumper.dumpAll(new PrintWriter(out));
        assertTrue(out.toString()
                .contains("<property name=\"" + UIStatus.LAST_MOUSE_MOVE_DPROP + "\" value=\""+point+"\"/>"));
        assertTrue(out.toString()
                .contains("<property name=\"" + UIStatus.LAST_MOUSE_MOVE_OPERATOR_DPROP + "\" value=\""+fo+"\"/>"));
    }
}
