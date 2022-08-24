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

import org.netbeans.jemmy.operators.Operator;

import java.awt.Point;
import java.util.Arrays;

public class UIStats {
    /**
     * Indentifier for mouse location dump property. The mouse location is saved on last mouse move performed with
     * this operator.
     * @see org.netbeans.jemmy.util.Dumper#dumpAll(String)
     */
    public static final String LAST_MOUSE_MOVE_DPROP = "Last mouse move location";
    public static final String LAST_MOUSE_MOVE_OPERATOR_DPROP = "Last operator moved the mouse";

    private static volatile Point lastMouseMove;
    private static volatile Operator lastMouseMoveOperator;
    public static void mouseMoved(Operator oper, Point location) {
        Arrays.stream(Thread.currentThread().getStackTrace()).forEach(System.out::println);
        lastMouseMoveOperator = oper;
        lastMouseMove = location;
    }

    public static Point lastMouseMove() {
        return lastMouseMove;
    }

    public static Operator lastMouseMoveOperator() {
        return lastMouseMoveOperator;
    }
}
