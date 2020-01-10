/*
 * Copyright (C) 2020 keithc
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
package helpUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import sudoku.PuzzleWindow;

/**
 *
 * @author keithc
 */
public class HelpMenu extends sudoku.Menu {

    public HelpMenu(PuzzleWindow listerWindow) {
        super(listerWindow);
        createMenu();
    }
    
    @Override
    public void updateEnabledMenus(boolean opened) {
        // do nothing
    }

    @Override
    public final void createMenu() {
        
        setMnemonic(KeyEvent.VK_A);
        setText("Help");

        // create a clear puzzle item
        _helpItem = new JMenuItem("Help", null);
        _helpItem.setMnemonic(KeyEvent.VK_H);
        _helpItem.setToolTipText("Puzzle tutorial");
        _helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            HtmlHelpPage helpPage = new HtmlHelpPage(_puzzleWindow);
            }
        });
            }
        });
        add(_helpItem);
        _helpItem.setEnabled(true);

        // create a clear tries item
        _aboutItem = new JMenuItem("About", null);
        _aboutItem.setMnemonic(KeyEvent.VK_T);
        _aboutItem.setToolTipText("Version information");
        _aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,
                    sudoku.SudokuUnlocked.VERSION + "\n" +
                            sudoku.SudokuUnlocked.VERSION_DATE, 
                    "About", 
                    JOptionPane.OK_OPTION);
            }
        });
        add(_aboutItem);
        _aboutItem.setEnabled(true);
    }
    
    /* properties */ 
    private JMenuItem _helpItem;
    private JMenuItem _aboutItem;
    private static final long serialVersionUID = 101;
}
