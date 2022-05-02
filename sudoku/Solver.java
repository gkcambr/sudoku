package sudoku;

import java.util.LinkedList;
import java.util.Iterator;

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
/**
 *
 * @author keithc
 */
public class Solver {

    Solver(PuzzleWindow win) {
        myPuzzleWindow = win;
    }

    int solve() {
        int noSolutions;
        int noUpdates;

        int[] sets = {ROW_SET, COL_SET, SQUARE_SET};
        myPuzzleWindow.computePossibleValues();
        do {
            noSolutions = noUpdates = solveSingletons();
            if (noUpdates > 0) {
                myPuzzleWindow.computePossibleValues();
                noSolutions += noUpdates;
            }

            for (int set : sets) {
                noUpdates = updateUnique(set);
                if (noUpdates > 0) {
                    myPuzzleWindow.computePossibleValues();
                    noSolutions += noUpdates;
                }
            }

        } while (noSolutions > 0);

        return noSolutions;
    }

    int solveSingletons() {

        // populate all cells with only one available value
        int totalUpdates = 0;
        int noUpdates;
        do {
            noUpdates = 0;
            LinkedList<PuzzleCell> hintList = myPuzzleWindow.getHintList();
            Iterator<PuzzleCell> it = hintList.iterator();
            while (it.hasNext()) {
                PuzzleCell cell = it.next();
                Integer[] values = cell.computePossibleValues();
                if (values.length == 1) {
                    myPuzzleWindow.setCell(cell.getNumber(), values[0], PuzzleCell.VALUE_TYPE_AUTO);
                    noUpdates += 1;
                }
            }
            totalUpdates += noUpdates;
            myPuzzleWindow.computePossibleValues();
        } while (noUpdates > 0);
        return totalUpdates;
    }

    boolean isUnique(SudokuSet set, PuzzleCell cell, int value) {
        boolean unique = true;

        PuzzleCell[] setCells = set.getCells();
        for (PuzzleCell checkCell : setCells) {
            if (checkCell.getNumber() != cell.getNumber()) {
                Integer[] checkCellPossibles = checkCell.computePossibleValues();
                for (int checkValue : checkCellPossibles) {
                    if (checkValue == value) {
                        // another cell in this row also has this value as a possibility
                        unique = false;
                        break;
                    }
                }
            }
            if (!unique) {
                break;
            }
        }
        return unique;
    }

    int updateUnique(int setId) {
        // find cells where one of the available values only exists in that
        // cell in an associated row, column or square. if that is the only
        // cell that can have that value, it must be the value of the cell.
        int noUpdates;

        noUpdates = 0;
        LinkedList<PuzzleCell> hintList = myPuzzleWindow.getHintList();
        Iterator<PuzzleCell> it = hintList.iterator();
        while (it.hasNext()) {
            PuzzleCell cell = it.next();
            Integer[] values = cell.computePossibleValues();
            if (values.length > 1) {
                for (int value : values) {
                    // check the cell's set. is this value unique to this cell?
                    SudokuSet set = cell.getSquare();
                    if (setId == ROW_SET) {
                        set = cell.getRow();
                    } else if (setId == COL_SET) {
                        set = cell.getColumn();
                    }
                    boolean unique = isUnique(set, cell, value);
                    if (unique) {
                        // this value is a unique possibility in this set.
                        // assign it to the cell
                        myPuzzleWindow.setCell(cell.getNumber(), value, PuzzleCell.VALUE_TYPE_AUTO);
                        noUpdates += 1;
                    }
                }
            }
        }

        return noUpdates;
    }

    /* properties */
    private final PuzzleWindow myPuzzleWindow;
    private static final int ROW_SET = 1;
    private static final int COL_SET = 2;
    private static final int SQUARE_SET = 3;
}
