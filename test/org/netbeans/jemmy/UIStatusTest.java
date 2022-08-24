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
package org.netbeans.jemmy;

import org.netbeans.jemmy.drivers.DriverManager;
import org.netbeans.jemmy.drivers.LightDriver;
import org.netbeans.jemmy.drivers.input.MouseEventDriver;
import org.netbeans.jemmy.drivers.input.MouseRobotDriver;
import org.netbeans.jemmy.operators.JComboBoxOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JListOperator;
import org.netbeans.jemmy.scenario.ComboBoxesAndListApp;
import org.netbeans.jemmy.scenario.Util;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.testng.Assert.assertEquals;

public class UIStatusTest {
    private static JFrameOperator fo;

    @BeforeClass
    public static void setup() throws InvocationTargetException, NoSuchMethodException, IOException {
        Util.testInfraSetup();
        new ComboBoxesAndListApp().display();
        fo = new JFrameOperator("ComboBoxesAndListTest");
    }

    @DataProvider(name = "mouseDrivers")
    public Object[] mouseDrivers() {
        return new Object[] {new MouseRobotDriver(new Timeout("", 100)), new MouseEventDriver()};
    }

    private void setDriver(Object driver) {
        DriverManager.setDriver(DriverManager.MOUSE_DRIVER_ID, (LightDriver) driver);
        UIStatus.mouseMoved(null, null);
    }

    @Test(dataProvider = "mouseDrivers")
    public void enter(Object driver) {
        setDriver(driver);
        JComboBoxOperator combo = new JComboBoxOperator(fo);
        combo.clickMouse();
        assertEquals(UIStatus.lastMouseMove(), new Point(combo.getCenterXForClick(), combo.getCenterYForClick()));
    }

    @Test(dataProvider = "mouseDrivers")
    public void click(Object driver) {
        setDriver(driver);
        JListOperator list = new JListOperator(fo);
        list.clickMouse();
        assertEquals(UIStatus.lastMouseMove(), new Point(list.getCenterXForClick(), list.getCenterYForClick()));
    }

    @Test(dataProvider = "mouseDrivers")
    public void move(Object driver) {
        setDriver(driver);
        JComboBoxOperator combo = new JComboBoxOperator(fo, 1);
        combo.moveMouse(1, 1);
        assertEquals(UIStatus.lastMouseMove(), new Point(1, 1));
    }

    @Test(dataProvider = "mouseDrivers")
    public void drag(Object driver) {
        setDriver(driver);
        JListOperator list = new JListOperator(fo);
        list.dragNDrop(10, 10, 20, 20);
        assertEquals(UIStatus.lastMouseMove(), new Point(20, 20));
    }

}
