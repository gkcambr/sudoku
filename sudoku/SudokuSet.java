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

import static java.lang.Math.pow;
import java.util.LinkedList;

/**
 *
 * @author keithc
 */
public class SudokuSet {

    SudokuSet() {
        reset();
    }

    final void reset() {
        mySetCells.clear();
        myAvailableValues = 0;
    }

    void removeMember(PuzzleCell remcell) {
        for (PuzzleCell cell : mySetCells) {
            if (cell.getValue() == remcell.getValue()) {
                mySetCells.remove(cell);
            }
        }
    }

    void addMember(PuzzleCell cell) {
        mySetCells.add(cell);
    }

    PuzzleCell containsValue(int value) {
        PuzzleCell ret = null;
        for (PuzzleCell cell : mySetCells) {
            if (value == cell.getValue()) {
                ret = cell;
                break;
            }
        }
        return ret;
    }

    PuzzleCell[] getCells() {
        return mySetCells.toArray(new PuzzleCell[9]);
    }
    
    PuzzleCell[] getAvailableCells() {
        LinkedList<PuzzleCell> avails = new LinkedList<>();
        for(PuzzleCell cell: getCells()) {
            if(cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                avails.add(cell);
            }
        }
        return avails.toArray(new PuzzleCell[avails.size()]);
    }
    
    void updateAvailableValues() {
        myAvailableValues=ALL_VALUES;
        for(PuzzleCell cell : mySetCells) {
            if(cell.getValue() != PuzzleCell.UNDEFINED) {
                int andValue = (int) (ALL_VALUES - pow(2,(cell.getValue() - 1)));
                myAvailableValues &= andValue;
            }
        }
    }
    
    int getValues() {
        return myAvailableValues;
    }

    /* properties */
    static final int ALL_VALUES = (int) (pow(2,9) - 1);
    private final LinkedList<PuzzleCell> mySetCells = new LinkedList<>();
    private int myAvailableValues = ALL_VALUES;
}
