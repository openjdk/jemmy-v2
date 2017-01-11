/*
 * Copyright (c) 1997, 2016, Oracle and/or its affiliates. All rights reserved.
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

package org.netbeans.jemmy.demo;

import org.netbeans.jemmy.EventDispatcher;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.TestOut;
import org.netbeans.jemmy.TimeoutExpiredException;

/**
 *
 * Class to display step comments.
 *
 * @author Alexandre Iline (alexandre.iline@oracle.com)
 *
 */

public class Demonstrator {

    private static CommentWindow displayer;
    private static CommentWindow nonDisplayer;

    /**
     * Notifies current CommentWindow implementation to change title.
     * @param title new CommentWindow's title
     */
    public static void setTitle(String title) {
        displayer.setTitle(title);
    }

    /**
     * Changes current CommentWindow.
     * @param cw CommentWindow instance.
     */
    public static void setCommentWindow(CommentWindow cw) {
        displayer = cw;
    }

    /**
     * Notifies current CommentWindow implementation to display comments for a new step.
     * @param stepComment New step comments
     */
    public static void nextStep(String stepComment) {
        getDisplayer().nextStep(stepComment);
        while(getDisplayer().isStopped()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        if(getDisplayer() != nonDisplayer) {
            try {
                EventDispatcher.waitQueueEmpty(TestOut.getNullOutput(),
                                               JemmyProperties.getCurrentTimeouts());
            } catch(TimeoutExpiredException e) {
                e.printStackTrace();
            }
        }
        if(getDisplayer().isInterrupted()) {
            getDisplayer().close();
            throw(new DemoInterruptedException(getDisplayer().getInterruptMessage()));
        }
    }

    /**
     * Notifies current CommentWindow implementation to display final comments.
     * @param stepComment New step comments
     */
    public static void showFinalComment(String stepComment) {
        getDisplayer().showFinalComment(stepComment);
        while(getDisplayer().isStopped()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        getDisplayer().close();
        if(getDisplayer() != nonDisplayer) {
            try {
                EventDispatcher.waitQueueEmpty(TestOut.getNullOutput(),
                                               JemmyProperties.getCurrentTimeouts());
            } catch(TimeoutExpiredException e) {
                e.printStackTrace();
            }
        }
    }

    static {
        setCommentWindow(new DefaultCommentWindow());
        setTitle("Step comments");
        nonDisplayer = new NonWindow();
    }

    private static class NonWindow implements CommentWindow {
        public void setTitle(String title) {
        }
        public boolean isStopped() {
            return(false);
        }
        public void nextStep(String stepComment) {
            JemmyProperties.getCurrentOutput().printLine("Step comments:\n" +
                                                         stepComment);
        }
        public void showFinalComment(String stepComment) {
            JemmyProperties.getCurrentOutput().printLine("Final comments:\n" +
                                                         stepComment);
        }
        public boolean isInterrupted() {
            return(false);
        }
        public String getInterruptMessage() {
            return("");
        }
        public void close() {
        }
    }

    private static CommentWindow getDisplayer() {
        if(System.getProperty("jemmy.demo") != null &&
           System.getProperty("jemmy.demo").equals("on")) {
            return(displayer);
        } else {
            return(nonDisplayer);
        }
    }
}
