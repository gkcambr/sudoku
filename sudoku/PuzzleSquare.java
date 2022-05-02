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

import javax.swing.JPanel;

/**
 *
 * @author keithc
 */
public class PuzzleSquare extends SudokuSet {
    
    PuzzleSquare(int square) {
        mySquare = square;
        myPanel = new JPanel();
    }

    public int getNumber() {
        return mySquare;
    }
    
    JPanel getPanel() {
        return myPanel;
    }
    
    /* properties */
    private final int mySquare;
    private final JPanel myPanel;

    private static final long serialVersionUID = 101;
}
