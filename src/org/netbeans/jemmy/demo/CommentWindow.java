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

/**
 *
 * Interface implementation defines a way to display step comments
 * during demo or test debug.
 *
 * @author Alexandre Iline (alexandre.iline@oracle.com)
 *
 *
 */

public interface CommentWindow {
    /**
     * Defines either test(demo) has been stopped or not.
     * @return true if test (demo) execution has been stopped.
     * Like when user is reading step comments.
     * false if test execution can be continued.
     */
    public boolean isStopped();

    /**
     * Defines either test execution should be interrupted or not.
     * @return If true, execution will be interrupted.
     */
    public boolean isInterrupted();

    /**
     * Defines window title.
     * @param title Title to display.
     */
    public void setTitle(String title);

    /**
     * Should display next step comment.
     * @param stepComment Comments to be displayed.
     */
    public void nextStep(String stepComment);

    /**
     * Method is invoked at the end of test(demo).
     * @param stepComment Comment to be displayed.
     */
    public void showFinalComment(String stepComment);

    /**
     * Closes the window.
     */
    public void close();

    /**
     * Returns a message for a case when test needs to be interrupted.
     * @return Interrupted message if test should be interrupted.
     */
    public String getInterruptMessage();
}
