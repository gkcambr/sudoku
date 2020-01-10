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

import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author keithc
 */
public class SudokuMap implements SudokuSet {

    SudokuMap() {
        _map = new Stack<>();
    }

    SudokuMap(SudokuMap map) {
        _map = new Stack<>();
    }
    
    @Override
    public void reset() {
        Iterator<PuzzleCell> it = _map.iterator();
        while(it.hasNext()) {
            PuzzleCell cell = it.next();
            cell.setType(PuzzleCell.VALUE_TYPE_AVAILABLE);
        }
    }
    
    @Override
    public boolean containsCell(PuzzleCell cell) {
        boolean ret = false;

        if (_map.search(cell) != UNAVAILABLE) {
            ret = true;
        }
        return ret;
    }

    @Override
    public PuzzleCell containsValue(int value) {
        PuzzleCell ret = null;

        Iterator<PuzzleCell> it = _map.iterator();
        while (it.hasNext()) {
            PuzzleCell cell = it.next();
            if (cell.getValue() == value) {
                ret = cell;
                break;
            }
        }
        return ret;
    }

    @Override
    public SudokuMap findAvailableMembers(SudokuMap otherSet) {
        SudokuMap ret = new SudokuMap();
        Iterator<PuzzleCell> it = _map.iterator();
        while (it.hasNext()) {
            PuzzleCell cell = it.next();
            if (cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                ret.addMember(cell);
            }
        }
        return ret;
    }

    @Override
    public PuzzleCell[] getCells() {
        return _map.toArray(new PuzzleCell[_map.size()]);
    }

    @Override
    public void removeMember(PuzzleCell button) {
        _map.remove(button);
    }

    @Override
    public PuzzleCell[] getAvailableCells() {
        Stack<PuzzleCell> availables = new Stack<>();
        Iterator<PuzzleCell> it = _map.iterator();
        while(it.hasNext()) {
            PuzzleCell cell = it.next();
            if(cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                availables.add(cell);
            }
        }
        PuzzleCell[] ret = availables.toArray(new PuzzleCell[0]);
        return ret;
    }
    
    @Override
    public PuzzleCell[] getUnavailableCells() {
        
        Stack<PuzzleCell> unavailables = new Stack<>();
        Iterator<PuzzleCell> it = _map.iterator();
        while(it.hasNext()) {
            PuzzleCell cell = it.next();
            if(cell.getType() != PuzzleCell.VALUE_TYPE_AVAILABLE) {
                unavailables.add(cell);
            }
        }
        PuzzleCell[] ret = unavailables.toArray(new PuzzleCell[0]);
        return ret;
    }
    
    @Override
    public void addMember(PuzzleCell cell) {
        _map.add(cell);
    }
    
    /* properties */
    public final int UNAVAILABLE = -1;
    public final int SET = 1;
    public final int TRY = 2;
    // store the set of cells in the row, square or column
    private final Stack<PuzzleCell> _map;
}
