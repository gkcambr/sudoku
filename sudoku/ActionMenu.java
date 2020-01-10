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

        _clearPuzzleItem.setEnabled(enabled);
        _clearTriesItem.setEnabled(enabled);
        _clearAutoSolvedItem.setEnabled(enabled);
        _hintItem.setEnabled(enabled);
        _solveItem.setEnabled(enabled);
    }

    @Override
    public void createMenu() {

        setMnemonic(KeyEvent.VK_A);
        setText("Action");

        // create a clear puzzle item
        _clearPuzzleItem = new JMenuItem("Clear all", null);
        _clearPuzzleItem.setMnemonic(KeyEvent.VK_C);
        _clearPuzzleItem.setToolTipText("Clear all puzzle entries");
        _clearPuzzleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                _puzzleWindow.clearEntries(PuzzleCell.VALUE_TYPE_ALL);
            }
        });
        add(_clearPuzzleItem);
        _clearPuzzleItem.setEnabled(true);

        // create a clear tries item
        _clearTriesItem = new JMenuItem("Clear tries", null);
        _clearTriesItem.setMnemonic(KeyEvent.VK_T);
        _clearTriesItem.setToolTipText("Clear puzzle try entries");
        _clearTriesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                _puzzleWindow.clearEntries(PuzzleCell.VALUE_TYPE_TRY);
            }
        });
        add(_clearTriesItem);
        _clearTriesItem.setEnabled(true);

        // create a clear auto solved item
        _clearAutoSolvedItem = new JMenuItem("Clear solved", null);
        _clearAutoSolvedItem.setMnemonic(KeyEvent.VK_O);
        _clearAutoSolvedItem.setToolTipText("Clear puzzle try entries");
        _clearAutoSolvedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                _puzzleWindow.clearEntries(PuzzleCell.VALUE_TYPE_AUTO);
            }
        });
        add(_clearAutoSolvedItem);
        _clearAutoSolvedItem.setEnabled(true);

        // create a hint item
        _hintItem = new JMenuItem("Show hint", null);
        _hintItem.setMnemonic(KeyEvent.VK_H);
        _hintItem.setToolTipText("Get hints");
        _hintItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                _puzzleWindow.showHints();
            }
        });
        add(_hintItem);
        _hintItem.setEnabled(true);

        // create a solve item
        _solveItem = new JMenuItem("Solve", null);
        _solveItem.setMnemonic(KeyEvent.VK_S);
        _solveItem.setToolTipText("Get solves");
        _solveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                _puzzleWindow.solve();
            }
        });
        add(_solveItem);
        _solveItem.setEnabled(true);
    }

    /* properties */
    private JMenuItem _clearPuzzleItem;
    private JMenuItem _clearTriesItem;
    private JMenuItem _clearAutoSolvedItem;
    private JMenuItem _hintItem;
    private JMenuItem _solveItem;
    private static final long serialVersionUID = 100L;
}
