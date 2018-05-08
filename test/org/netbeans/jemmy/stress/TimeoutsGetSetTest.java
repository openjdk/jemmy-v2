/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
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
package org.netbeans.jemmy.stress;

import org.netbeans.jemmy.Timeouts;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.currentTimeMillis;

public class TimeoutsGetSetTest {
    /**
     * Previously there was an NPE while getting a value from hashmap in setTimeout()
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        Timeouts timeouts = new Timeouts();
        String timeoutName = "timeout";
        timeouts.setTimeout(timeoutName, 1);
        long start = currentTimeMillis();
        AtomicBoolean stop = new AtomicBoolean(false);
        new Thread(() -> {
            while (!stop.get()) {
                timeouts.setTimeout(timeoutName, 1);
            }
        }).start();
        new Thread(() -> {
            while (!stop.get()) {
                try {
                    timeouts.getTimeout(timeoutName);
                } catch (NullPointerException e) {
                    System.err.println("NPE in " + (currentTimeMillis() - start));
                    throw e;
                }
            }
        }).start();
        Thread.sleep(10000);
        stop.set(true);
    }
}
