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

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JEditorPaneOperatorTest {

    private final static String PAGE1 = "page1";
    private final static String PAGE2 = "page2";
    private final static String PAGE1_TEXT = "hi";
    private final static String PAGE2_TEXT = "hello";
    private final URL page1URL = getClass().getResource("resources/page1.html");
    private JFrame frame;
    private HyperlinkListener listener = null;

    @BeforeClass
    protected void setUp() throws Exception {
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);

        listener = event -> {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    ((JEditorPane)event.getSource()).setPage(event.getURL());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @AfterClass
    protected void tearDown() throws Exception {
        frame.setVisible(false);
        frame.dispose();
    }

    @Test
    public void testClickOnReferenceWithScrollPane() throws IOException {
        JEditorPane editorPane = new JEditorPane(page1URL);
        editorPane.setEditable(false);

        JScrollPane scroller = new JScrollPane();
        JViewport vp = scroller.getViewport();
        vp.add(editorPane);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scroller, BorderLayout.CENTER);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        JEditorPaneOperator editorPaneOperator =
                new JEditorPaneOperator(new FrameOperator(frame));
        checkPageLoaded(editorPaneOperator, PAGE1, PAGE1_TEXT);

        // Testing reference which doesn't exist
        String excepMessage = "";
        try {
            editorPaneOperator.clickOnReference(PAGE1);
            fail();
        } catch (IllegalArgumentException e) {
            excepMessage = e.getMessage();
        }
        assertTrue(excepMessage.contains(
                "Reference page1 doesn't exist in the document"));
        try {
            editorPaneOperator.addHyperlinkListener(listener);
            // Testing on a short text page
            editorPaneOperator.clickOnReference(PAGE2);
            checkPageLoaded(editorPaneOperator, PAGE2, PAGE2_TEXT);

            // Testing on a long text page
            editorPaneOperator.clickOnReference(PAGE1);
            editorPaneOperator.waitStateOnQueue(comp
                    -> ((JEditorPane)comp).getPage().toString().contains(PAGE1));
        } finally {
            editorPane.removeHyperlinkListener(listener);
            frame.getContentPane().remove(panel);
        }
    }

    @Test
    public void testClickOnReferenceWithoutScrollPane() throws IOException {
        JEditorPane editorPane = new JEditorPane(page1URL);
        editorPane.setEditable(false);
        frame.getContentPane().add(editorPane);
        frame.setVisible(true);
        JEditorPaneOperator editorPaneOperator =
                new JEditorPaneOperator(new JFrameOperator(frame));
        checkPageLoaded(editorPaneOperator, PAGE1, PAGE1_TEXT);

        try {
            editorPaneOperator.addHyperlinkListener(listener);
            // Testing reference on the visible area
            editorPaneOperator.clickOnReference(PAGE2);
            checkPageLoaded(editorPaneOperator, PAGE2, PAGE2_TEXT);

            // Testing reference on the non-visible area
            String excepMessage = "";
            try {
                editorPaneOperator.clickOnReference(PAGE1);
                fail();
            } catch (IllegalComponentStateException e) {
                excepMessage = e.getMessage();
            }
            assertTrue(excepMessage.equals("Component doesn't contain"
                    + " JScrollPane and Reference is out of visible area"));
        } finally {
            editorPaneOperator.removeHyperlinkListener(listener);
            frame.getContentPane().remove(editorPane);
        }
    }

    private void checkPageLoaded(JEditorPaneOperator editorPaneOperator,
            String page, String text) {
        editorPaneOperator.waitStateOnQueue(comp
                -> ((JEditorPane)comp).getPage().toString().contains(page));
        editorPaneOperator.selectText(text);
        editorPaneOperator.waitStateOnQueue(comp
                -> text.equals(editorPaneOperator.getSelectedText()));
    }
}
