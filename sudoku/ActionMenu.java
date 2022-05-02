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
import javax.swing.JMenuItem;

/**
 *
 * @author keithc
 */
public final class ActionMenu extends Menu {

    public ActionMenu(PuzzleWindow listerWindow) {
        super(listerWindow);
        createMenu();
    }
    
    @Override
    public void updateEnabledMenus(boolean enabled) {

        myClearPuzzleItem.setEnabled(enabled);
        myClearTriesItem.setEnabled(enabled);
        myClearAutoSolvedItem.setEnabled(enabled);
        mySolveItem.setEnabled(enabled);
    }

    @Override
    public void createMenu() {

        setMnemonic(KeyEvent.VK_A);
        setText("Action");

        // create a clear puzzle item
        myClearPuzzleItem = new JMenuItem("Clear all", null);
        myClearPuzzleItem.setMnemonic(KeyEvent.VK_C);
        myClearPuzzleItem.setToolTipText("Clear all puzzle entries");
        myClearPuzzleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                myPuzzleWindow.clearEntries(PuzzleCell.VALUE_TYPE_ALL);
            }
        });
        add(myClearPuzzleItem);
        myClearPuzzleItem.setEnabled(true);

        // create a clear tries item
        myClearTriesItem = new JMenuItem("Clear tries", null);
        myClearTriesItem.setMnemonic(KeyEvent.VK_T);
        myClearTriesItem.setToolTipText("Clear puzzle try entries");
        myClearTriesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                myPuzzleWindow.clearEntries(PuzzleCell.VALUE_TYPE_TRY);
            }
        });
        add(myClearTriesItem);
        myClearTriesItem.setEnabled(true);

        // create a clear auto solved item
        myClearAutoSolvedItem = new JMenuItem("Clear solved", null);
        myClearAutoSolvedItem.setMnemonic(KeyEvent.VK_O);
        myClearAutoSolvedItem.setToolTipText("Clear puzzle try entries");
        myClearAutoSolvedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                myPuzzleWindow.clearEntries(PuzzleCell.VALUE_TYPE_AUTO);
            }
        });
        add(myClearAutoSolvedItem);
        myClearAutoSolvedItem.setEnabled(true);

        // create a solve item
        mySolveItem = new JMenuItem("Solve", null);
        mySolveItem.setMnemonic(KeyEvent.VK_S);
        mySolveItem.setToolTipText("Get solves");
        mySolveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                myPuzzleWindow.solve();
            }
        });
        add(mySolveItem);
        mySolveItem.setEnabled(true);
    }

    /* properties */
    private JMenuItem myClearPuzzleItem;
    private JMenuItem myClearTriesItem;
    private JMenuItem myClearAutoSolvedItem;
    private JMenuItem mySolveItem;
    private static final long serialVersionUID = 100L;
}
