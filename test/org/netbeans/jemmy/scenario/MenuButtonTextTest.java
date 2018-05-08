/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
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
package org.netbeans.jemmy.scenario;


import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.QueueTool;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JMenuBarOperator;
import org.netbeans.jemmy.operators.JMenuItemOperator;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.netbeans.jemmy.operators.JRadioButtonMenuItemOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.Operator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import java.awt.Component;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.testng.Assert.*;

public class MenuButtonTextTest {

    private static Logger logger = Logger.getLogger(ComboBoxesAndListTest.class);
    private static JFrameOperator wino;
    private static JTextFieldOperator tfo;
    private static JMenuBarOperator mbo;
    private static MenuButtonTextApp app;

    @BeforeClass
    public static void setup() throws InvocationTargetException, NoSuchMethodException, IOException {
        Util.testInfraSetup();
        app = new MenuButtonTextApp();
        app.display();
        wino = new JFrameOperator("MenuButtonTextApp");
        tfo = new JTextFieldOperator(wino, "Text");
        mbo = new JMenuBarOperator(wino);
    }

    @BeforeMethod
    public void clearUI() {
        mbo.closeSubmenus();
        wino.getQueueTool().waitEmpty(10);
    }

    @AfterClass
    public static void tearDown() {
        app.setVisible(false);
        app.dispose();
    }

    @Test
    public void testTextField() {
        JTextFieldOperator tf1 = new JTextFieldOperator(wino);
        if (tf1.getSource() != tfo.getSource()) {
            logger.error("Wrong");
            logger.error(tfo.getSource());
            logger.error(tf1.getSource());
            fail();
        }

        tfo.clearText();
        tfo.typeText("Text has been typed");

        new JTextFieldOperator(wino, "has been typed");
    }

    @Test
    public void testRadioMenu() throws InterruptedException {
        JMenuItemOperator radioItem = mbo.showMenuItem("menu0|submenu|radio");

        JRadioButtonMenuItemOperator radio =
                new JRadioButtonMenuItemOperator((JRadioButtonMenuItem) radioItem.getSource());

        JMenuItemOperator menu = mbo.showMenuItem("menu0|submenu");
        mbo.showMenuItems("menu0|submenu");
        JPopupMenuOperator popup =
                new JPopupMenuOperator(((JMenu) menu.getSource()).getPopupMenu());
        JRadioButtonMenuItemOperator radio1 =
                new JRadioButtonMenuItemOperator(popup, "radio");

        assertFalse(radio.isSelected(), "Radio should not be selected");

        Thread.sleep(100); //CODETOOLS-7902051
        mbo.pushMenu("menu0|submenu|radio");

        assertTrue(radio.isSelected(), "Radio should be selected");
    }


    //@Test
    public void testPushMenus() {

        mbo.pushMenu("menuItem");

        JLabelOperator lbo = new JLabelOperator(wino, "menuitem");

        mbo.pushMenu("menu1|submenuitem");

        lbo.waitText("submenuitem");

        mbo.pushMenu("menu0|submenu|subsubmenuitem");

        lbo.waitText("subsubmenuitem");

    }

    //@Test
    public void testShowMenus() {

        assertEquals(mbo.showMenuItems("menu0|submenu").length, 3);

        ComponentChooser[] choosers1 = {
                new MenuItemChooser("menu0"),
                new MenuItemChooser("submenu")};

        assertEquals(mbo.showMenuItems(choosers1).length, 3);

        assertEquals(mbo.showMenuItem(choosers1).getText(), "submenu");

        assertEquals(mbo.showMenuItem("menu").getText(), "menu");

        assertEquals(mbo.showMenuItem("menu0|submenu|subsubmenu|subsubsubmenuitem").getText(), "menuitem");
    }

    public static class MenuItemChooser implements ComponentChooser {
        private String text;

        public MenuItemChooser(String text) {
            this.text = text;
        }

        @Override
        public boolean checkComponent(Component comp) {
            return (comp instanceof JMenuItem &&
                    Operator.getDefaultStringComparator()
                            .equals(text, ((JMenuItem) comp).getText()));
        }

        public String getDescription() {
            return ("MenuItem with \"" + text + "\" text");
        }
    }
}
