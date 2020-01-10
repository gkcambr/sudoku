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
        _puzzleWindow.setTitle(PuzzleWindow.TITLE + " - untitled.spf");
    }

    @Override
    @SuppressWarnings("Convert2Lambda")
    public final void createMenu() {
        setMnemonic(KeyEvent.VK_F);
        setText("File");

        // create a new Lister file item
        _fileNewItem = new JMenuItem("New", null);
        _fileNewItem.setMnemonic(KeyEvent.VK_N);
        _fileNewItem.setToolTipText("Create a new sudoku file");
        _fileNewItem.addActionListener(new ActionListener() {
            int dlgOption = JOptionPane.OK_OPTION;

            @Override
            public void actionPerformed(ActionEvent event) {
                if (_openFile != null && _fileIsDirty) {
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
                    _puzzleWindow.setTitle(PuzzleWindow.TITLE + " - untitled.spf");
                    _puzzleWindow.clearAll();
                    _fileIsDirty = false;
                }
            }
        });

        // open a puzzle file
        _fileOpenItem = new JMenuItem("Open", null);
        _fileOpenItem.setMnemonic(KeyEvent.VK_O);
        _fileOpenItem.setToolTipText("Open a puzzle file");
        _fileOpenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (_workingDir == null) {

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
                    _workingDir = workingDir;
                }
                int dlgOption = JOptionPane.OK_OPTION;
                if (_openFile != null && _fileIsDirty) {
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
                    JFileChooser chooser = new JFileChooser(_workingDir);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Sudoku puzzle files", "spf");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        _puzzleWindow.clearAll();
                        _puzzleWindow.readFile(file.getAbsolutePath());
                        _workingDir = chooser.getCurrentDirectory().getAbsolutePath();
                        _puzzleWindow.setTitle(PuzzleWindow.TITLE + " - " + file.getName());
                        _openFile = file;
                        _fileIsDirty = false;
                    }
                }
            }
        });

        // save a puzzle file
        _fileSaveItem = new JMenuItem("Save", null);
        _fileSaveItem.setMnemonic(KeyEvent.VK_S);
        _fileSaveItem.setToolTipText("Save puzzle to file");
        _fileSaveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (_workingDir == null) {
                    _workingDir = System.getProperty("user.dir");
                }
                JFileChooser chooser = new JFileChooser(_workingDir);
                if (_openFile != null) {
                    chooser.setSelectedFile(_openFile);
                }
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Sudoku puzzle files", "spf");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    _puzzleWindow.saveFile(file.getAbsolutePath());
                    _puzzleWindow.setTitle(PuzzleWindow.TITLE + " - " + file.getName());
                    _fileIsDirty = false;
                }
            }
        });
        _fileSaveItem.setEnabled(false);

        // exit
        JMenuItem exitItem = new JMenuItem("Exit", null);
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Exit application");
        exitItem.addActionListener((ActionEvent event) -> {
            _puzzleWindow.dispatchEvent(
                    new WindowEvent(_puzzleWindow, WindowEvent.WINDOW_CLOSING));
        });

        add(_fileNewItem);
        add(_fileOpenItem);
        add(_fileSaveItem);
        add(exitItem);
    }

    void fileIsDirty() {
        _fileIsDirty = true;
    }

    boolean isFileDirty() {
        return _fileIsDirty;
    }

    File getOpenFile() {
        return _openFile;
    }

    @Override
    public void updateEnabledMenus(boolean enabled) {

        _fileNewItem.setEnabled(enabled);
        _fileOpenItem.setEnabled(enabled);
        _fileSaveItem.setEnabled(enabled);
    }

    // Properties
    private JMenuItem _fileNewItem;
    private JMenuItem _fileOpenItem;
    private JMenuItem _fileSaveItem;
    private String _workingDir;
    private File _openFile = null;
    private boolean _fileIsDirty = false;
    private static final long serialVersionUID = 100L;
}
