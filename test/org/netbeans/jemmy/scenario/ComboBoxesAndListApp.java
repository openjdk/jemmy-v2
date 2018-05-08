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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by shura on 6/30/17.
 */
public class ComboBoxesAndListApp extends TestFrame {

    private final JComboBox editable;
    private final DefaultComboBoxModel editableModel;

    public ComboBoxesAndListApp() {
        super("ComboBoxesAndListTest");

        getContentPane().setLayout(new BorderLayout());

        JPanel pane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        pane.setLayout(gridbag);

        getContentPane().add(new JScrollPane(pane), BorderLayout.CENTER);

        String[] editable_contents = {"editable_one", "editable_two", "editable_three", "editable_four"};
        editableModel = new DefaultComboBoxModel(editable_contents);
        editable = new JComboBox(editableModel);
        editable.setEditable(true);
        editable.getEditor().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editableModel.addElement(editable.getEditor().getItem());
            }
        });
        editable.setName("editable");

        c.fill = GridBagConstraints.CENTER;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.weighty = 1.0;
        gridbag.setConstraints(editable, c);
        pane.add(editable);

        String[] list_contents = {"list_one", "list_two", "list_three", "list_four"};
        JList list = new JList(list_contents);
        list.setName("list");

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 2;
        c.weighty = 1.0;
        gridbag.setConstraints(list, c);
        pane.add(list);

        String[] non_editable_contents = {"non_editable_one", "non_editable_two", "non_editable_three", "non_editable_four"};
        JComboBox non_editable = new JComboBox(non_editable_contents);
        non_editable.setEditable(false);
        non_editable.setName("non_editable");

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.weighty = 1.0;
        gridbag.setConstraints(non_editable, c);
        pane.add(non_editable);

        setSize(200, 200);
    }

    public static void main(String[] args) {
        new ComboBoxesAndListApp().setVisible(true);
    }
}
