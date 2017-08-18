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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuButtonTextApp extends TestFrame {

    private final JLabel buttonLabel;
    private final JLabel menuLabel;

    public MenuButtonTextApp() {
        super("MenuButtonTextApp");

        getContentPane().setLayout(new FlowLayout());

        JButton button = new JButton("button");
        buttonLabel = new JLabel("Button has not been pushed yet");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                buttonLabel.setText("Button has been pushed");
            }
        });

        getContentPane().add(button);
        getContentPane().add(buttonLabel);

        JTextField field = new JTextField("Text has not been typed yet");

        getContentPane().add(field);

        JMenuItem menuItem = new JMenuItem("subsubsubmenuitem");
        menuLabel = new JLabel("Menu has not been pushed yet");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                menuLabel.setText("subsubsubmenuitem");
            }
        });

        JMenu subsubmenu = new JMenu("subsubmenu");
        subsubmenu.add(menuItem);
        JMenu subsubmenu2 = new JMenu("subsubmenu2");
        subsubmenu2.setEnabled(false);
        JRadioButtonMenuItem subsubradio = new JRadioButtonMenuItem("radio");
        JMenuItem submenuItem = new JMenuItem("subsubmenuitem");
        submenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuLabel.setText("subsubmenuitem");
            }
        });

        JMenu submenu = new JMenu("submenu");
        submenu.add(subsubmenu);
        submenu.add(new JSeparator());
        submenu.add(subsubmenu2);
        submenu.add(new JSeparator());
        submenu.add(subsubradio);
        submenu.add(submenuItem);

        JMenu menu = new JMenu("menu0");
        menu.add(submenu);

        JMenuItem menu0Item = new JMenuItem("submenuitem");
        menu0Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                menuLabel.setText("submenuitem");
            }
        });

        JMenu menu0 = new JMenu("menu1");
        menu0.add(menu0Item);

        JMenuItem menu1Item = new JMenuItem("menuitem");
        menu1Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                menuLabel.setText("menuitem");
            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        menuBar.add(menu0);
        menuBar.add(menu1Item);

        setJMenuBar(menuBar);

        getContentPane().add(menuLabel);

        setSize(300, 200);
    }

    public static void main(String[] argv) {
        (new MenuButtonTextApp()).show();
    }

}
