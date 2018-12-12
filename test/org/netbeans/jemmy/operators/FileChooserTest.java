/*
 * Copyright (c) 2017, 2018, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.LookAndFeelProvider;
import org.netbeans.jemmy.util.Dumper;
import org.netbeans.jemmy.util.LookAndFeel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FileChooserTest {

    private JFrameOperator frame;
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
    }

    @BeforeMethod
    public void setUpBeforeMethod(Object[] args) throws Exception {
        UIManager.setLookAndFeel((String)args[0]);
        FileChooserApp.show(dir);
        frame = new JFrameOperator("Sample File Chooser");
        fileChooser = new JFileChooserOperator(
                JFileChooserOperator.findJFileChooser((Container) frame.getSource()));
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws FileNotFoundException {
        frame.setVisible(false);
        frame.dispose();
        if(!result.isSuccess())
            Dumper.dumpAll(new File(UIManager.getLookAndFeel().getClass().getSimpleName()
                    + "_" + result.getMethod() + "-dump.xml").getAbsolutePath());
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testSelection(String lookAndFeel) throws Exception {
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

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testCount(String lookAndFeel) throws Exception {
        assertTrue(fileChooser.getFileCount() >= 3);
    }

    @Test(dataProvider = "availableLookAndFeels", dataProviderClass = LookAndFeelProvider.class)
    public void testGoHome(String lookAndFeel) throws Exception {
        // In Aqua, GTK and Motif L&Fs, JFileChooser does not have
        // "Go Home" button.
        if (!LookAndFeel.isAqua() && !LookAndFeel.isMotif() && !LookAndFeel.isGTK()) {
            File previousDirectory = fileChooser.getCurrentDirectory();
            fileChooser.goHome();
            fileChooser.waitState(chooser -> fileChooser.getCurrentDirectory().getPath().equals(
                    FileSystemView.getFileSystemView().getHomeDirectory().getPath()));
            fileChooser.setCurrentDirectory(previousDirectory);
            fileChooser.rescanCurrentDirectory();
        }
    }
}
