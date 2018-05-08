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
package org.netbeans.jemmy.operators;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.util.Dumper;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.swing.JFileChooser;
import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class FileChooserTest {

    private JFileChooserOperator fileChooser;
    private File dir, file;

    @BeforeClass
    public void setup() throws IOException {
        dir = File.createTempFile("testDir", "");
        dir.delete(); dir.mkdirs();
        File.createTempFile("aestFile", ".txt", dir).deleteOnExit();
        file = File.createTempFile("testFile", ".txt", dir);
        file.deleteOnExit();
        File.createTempFile("zestFile", ".txt", dir).deleteOnExit();
        FileChooserApp.show(dir);
        JFrameOperator frame = new JFrameOperator("Sample File Chooser");
        fileChooser = new JFileChooserOperator(
                JFileChooserOperator.findJFileChooser((Container) frame.getSource()));
    }
    @AfterMethod
    public void tearDown(ITestResult result) throws FileNotFoundException {
        if(!result.isSuccess())
            Dumper.dumpAll(new File(result.getMethod() + "-dump.xml").getAbsolutePath());
    }
    @Test
    public void testSelection() {
        fileChooser.selectFile(file.getName());
        fileChooser.waitState(new ComponentChooser() {
            @Override
            public boolean checkComponent(Component comp) {
                return ((JFileChooser)comp).getSelectedFile() != null &&
                        fileChooser.getSelectedFile().getName().equals(file.getName());
            }

            @Override
            public String getDescription() {
                return "test file is selected";
            }
        });
    }
    @Test
    public void testCount() {
        assertTrue(fileChooser.getFileCount() >= 3);
    }
}
