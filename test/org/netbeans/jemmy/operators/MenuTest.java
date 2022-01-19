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
package org.netbeans.jemmy.operators;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MenuTest {

    @Test(dataProvider = "menuTypes")
    public void showMenu(String menuType) {
        MenuApp.main(new String[] {menuType});
        JFrameOperator frame = new JFrameOperator(MenuApp.FRAME_TITLE);
        try {
            JMenuBarOperator menu = new JMenuBarOperator(frame.getJMenuBar());
            JMenuItemOperator item = menu.showMenuItem("menu|submenu|subsubmenu|item");
            assertEquals(item.getText(), "item");
            assertTrue(item.isShowing());
        } finally {
            frame.dispose();
            frame.waitClosed();
        }
    }

    @Test(dataProvider = "menuTypes")
    public void pushMenuNoBlock(String menuType) {
        MenuApp.main(new String[] {menuType});
        JFrameOperator frame = new JFrameOperator(MenuApp.FRAME_TITLE);
        try{
            JMenuBarOperator menu = new JMenuBarOperator(frame.getJMenuBar());
            menu.pushMenuNoBlock("menu|submenu|subsubmenu|item");
            new JLabelOperator(frame, "menu pushed");
        } finally {
            frame.dispose();
            frame.waitClosed();
        }
    }

    @DataProvider
    private static Object[][] menuTypes() {
        return new String[][] {{"java"}/*, {"native"}*/};
    }

}
