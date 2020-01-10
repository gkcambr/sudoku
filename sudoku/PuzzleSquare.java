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
public class PuzzleSquare extends JPanel implements SudokuSet {
    
    PuzzleSquare(int row, int col) {
        super();
        _row = row;
        _col = col;
        _square = row * 3 + col;
    }

    @Override
    public void reset() {
        _map.reset();
    }

    @Override
    public boolean containsCell(PuzzleCell button) {
        return _map.containsCell(button);
    }

    @Override
    public PuzzleCell containsValue(int value) {
        return _map.containsValue(value);
    }
    
    @Override
    public SudokuMap findAvailableMembers(SudokuMap otherSet) {
        return _map.findAvailableMembers(otherSet);
    }

    @Override
    public void removeMember(PuzzleCell button) {
        _map.removeMember(button);
    }
    
    @Override
    public PuzzleCell[] getCells() {
        return _map.getCells();
    }
    
    public int getNumber() {
        return _square;
    }
    
    @Override
    public PuzzleCell[] getAvailableCells() {
        return _map.getAvailableCells();
    }
    
    @Override
    public PuzzleCell[] getUnavailableCells() {
        return _map.getUnavailableCells();
    }
    
    @Override
    public void addMember(PuzzleCell cell) {
        _map.addMember(cell);
    }
    
    /* properties */
    private final SudokuMap _map = new SudokuMap();
    private final int _row;
    private final int _col;
    private final int _square;

    private static final long serialVersionUID = 101;
}
