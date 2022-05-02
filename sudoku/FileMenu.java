/*
 * Copyright (C) 2019 G. Keith Cambron
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sudoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author keithc
 */
public class FileMenu extends Menu {

    FileMenu(PuzzleWindow listerWindow) {
        super(listerWindow);
        createMenu();
        myPuzzleWindow.setTitle(PuzzleWindow.TITLE + " - untitled.spf");
    }
    
    void createNewFileItem() {
        
        // create a new Lister file item
        myFileNewItem = new JMenuItem("New", null);
        myFileNewItem.setMnemonic(KeyEvent.VK_N);
        myFileNewItem.setToolTipText("Create a new sudoku file");
        myFileNewItem.addActionListener(new ActionListener() {
            int dlgOption = JOptionPane.OK_OPTION;

            @Override
            public void actionPerformed(ActionEvent event) {
                if (myOpenFile != null && myFileIsDirty) {
                    Object[] options = {"OK", "CANCEL"};
                    dlgOption = JOptionPane.showOptionDialog(null,
                            "If you click OK you will lose the\n"
                            + "changes made to the current file.",
                            "Warning",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]);
                }
                if (dlgOption == JOptionPane.OK_OPTION) {
                    myPuzzleWindow.setTitle(PuzzleWindow.TITLE + " - untitled.spf");
                    myPuzzleWindow.clearAll();
                    myFileIsDirty = false;
                }
            }
        });
    }
    
    void createOpenFileItem() {
        
        // open a puzzle file
        myFileOpenItem = new JMenuItem("Open", null);
        myFileOpenItem.setMnemonic(KeyEvent.VK_O);
        myFileOpenItem.setToolTipText("Open a puzzle file");
        myFileOpenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (myWorkingDir == null) {

                    // find the application directory
                    String workingDir = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
                    int indx = workingDir.indexOf("/");
                    if(indx > 0) {
                        workingDir = "/" + workingDir.substring(indx + 1);
                        indx = workingDir.lastIndexOf("/");
                        if(indx > 0) {
                            workingDir = workingDir.substring(0, indx);
                            workingDir = workingDir + "/" + "puzzles";
                        }
                    }
                    myWorkingDir = workingDir;
                }
                int dlgOption = JOptionPane.OK_OPTION;
                if (myOpenFile != null && myFileIsDirty) {
                    Object[] options = {"OK", "CANCEL"};
                    dlgOption = JOptionPane.showOptionDialog(null,
                            "If you click OK you will lose the\n"
                            + "changes made to the current file.",
                            "Warning",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]);
                }
                if (dlgOption == JOptionPane.OK_OPTION) {
                    JFileChooser chooser = new JFileChooser(myWorkingDir);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Sudoku puzzle files", "spf");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        myPuzzleWindow.clearAll();
                        myPuzzleWindow.readFile(file.getAbsolutePath());
                        myWorkingDir = chooser.getCurrentDirectory().getAbsolutePath();
                        myPuzzleWindow.setTitle(PuzzleWindow.TITLE + " - " + file.getName());
                        myOpenFile = file;
                        myFileIsDirty = false;
                    }
                }
            }
        });
    }
    
    void createSaveFileItem() {
        
        // save a puzzle file
        myFileSaveItem = new JMenuItem("Save", null);
        myFileSaveItem.setMnemonic(KeyEvent.VK_S);
        myFileSaveItem.setToolTipText("Save puzzle to file");
        myFileSaveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (myWorkingDir == null) {
                    myWorkingDir = System.getProperty("user.dir");
                }
                JFileChooser chooser = new JFileChooser(myWorkingDir);
                if (myOpenFile != null) {
                    chooser.setSelectedFile(myOpenFile);
                }
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Sudoku puzzle files", "spf");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    myPuzzleWindow.saveFile(file.getAbsolutePath());
                    myPuzzleWindow.setTitle(PuzzleWindow.TITLE + " - " + file.getName());
                    myFileIsDirty = false;
                }
            }
        });
        myFileSaveItem.setEnabled(false);
    }
    
    void createExitFileItem() {
        
        // exit
        myExitItem = new JMenuItem("Exit", null);
        myExitItem.setMnemonic(KeyEvent.VK_E);
        myExitItem.setToolTipText("Exit application");
        myExitItem.addActionListener((ActionEvent event) -> {
            myPuzzleWindow.dispatchEvent(
                    new WindowEvent(myPuzzleWindow, WindowEvent.WINDOW_CLOSING));
        });
    }

    @Override
    @SuppressWarnings("Convert2Lambda")
    public final void createMenu() {
        setMnemonic(KeyEvent.VK_F);
        setText("File");

        createNewFileItem();
        createOpenFileItem();
        createSaveFileItem();
        createExitFileItem();

        add(myFileNewItem);
        add(myFileOpenItem);
        add(myFileSaveItem);
        add(myExitItem);
    }

    void fileIsDirty() {
        myFileIsDirty = true;
    }

    boolean isFileDirty() {
        return myFileIsDirty;
    }

    File getOpenFile() {
        return myOpenFile;
    }

    @Override
    public void updateEnabledMenus(boolean enabled) {

        myFileNewItem.setEnabled(enabled);
        myFileOpenItem.setEnabled(enabled);
        myFileSaveItem.setEnabled(enabled);
    }

    // Properties
    private JMenuItem myFileNewItem;
    private JMenuItem myFileOpenItem;
    private JMenuItem myFileSaveItem;
    private JMenuItem myExitItem;
    private String myWorkingDir;
    private File myOpenFile = null;
    private boolean myFileIsDirty = false;
    private static final long serialVersionUID = 100L;
}
