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

/**
 *
 * @author keithc
 */
public class SudokuUnlocked {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PuzzleWindow puzzleWindow = new PuzzleWindow();
        puzzleWindow.setSize(400, 400);
        puzzleWindow.setVisible(true);
    }    
    
    public static final String VERSION = "Sudoku Unlocked Version 2.0";
    public static final String VERSION_DATE = "April 15, 2022";
}
