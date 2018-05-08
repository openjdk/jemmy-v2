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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsAndTooltipsApp extends TestFrame {

    private final static int BUTTON_NUMBER = 4;
    private final JLabel buttonLabel;
    private final JProgressBar progress;


    public ButtonsAndTooltipsApp() {
        super("ButtonsAndTooltipsApp");

        getContentPane().setLayout(new BorderLayout());

        buttonLabel = new JLabel("Button has not been pushed yet");
        getContentPane().add(buttonLabel, BorderLayout.NORTH);

        progress = new JProgressBar(0, BUTTON_NUMBER * BUTTON_NUMBER);
        getContentPane().add(progress, BorderLayout.SOUTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));
        getContentPane().add(panel, java.awt.BorderLayout.CENTER);

        JButton butt;
        for (int i = 0; i < BUTTON_NUMBER; i++) {
            for (int j = 0; j < BUTTON_NUMBER; j++) {
                butt = new JButton(Integer.toString(i) + "-" + Integer.toString(j));
                butt.setToolTipText(butt.getText() + " button");
                butt.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        JButton btt = ((JButton) event.getSource());
                        String text = btt.getText();
                        buttonLabel.setText("Button \"" + text + "\" has been pushed");
                        int i = Integer.parseInt(text.substring(0, 1));
                        int j = Integer.parseInt(text.substring(2));
                        progress.setValue(i * BUTTON_NUMBER + j + 1);
                        progress.setString(text);
                    }
                });
                panel.add(butt);
            }
        }

        setSize(400, 400);
    }

    public static void main(String[] argv) {
        (new ButtonsAndTooltipsApp()).show();
    }
}

