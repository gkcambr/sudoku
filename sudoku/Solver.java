package sudoku;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
import java.util.TreeMap;

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
        _win = win;
    }

    void checkSetPossibles(PuzzleCell cell) {

        // check the assignable values of cells in this set.
        // this may be the only cell that can accept this value.
        if (cell != null
                && cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE
                && cell.getPossibleValues().length != 1) {
            PuzzleCell[] setCells = cell.getSquare().getAvailableCells();
            int conflictType = NumberDialog.SQ_CONFLICT;
            for (int s = 0; s < 3; s++) {
                switch (s) {
                    case 0:
                        break;
                    case 1:
                        setCells = cell.getColumn().getAvailableCells();
                        conflictType = NumberDialog.COL_CONFLICT;
                        break;
                    case 2:
                        setCells = cell.getRow().getAvailableCells();
                        conflictType = NumberDialog.ROW_CONFLICT;
                        break;
                }

                for (Integer value : cell.getPossibleValues()) {
                    boolean isAvailable = false;
                    for (PuzzleCell nextCell : setCells) {
                        if (nextCell.getNumber() == cell.getNumber()) {
                            // don't check the nextCell against itself!
                            continue;
                        }
                        if (nextCell.ValueIsAvailable(value)) {
                            isAvailable = true;
                            break;
                        }
                    }
                    if (isAvailable == false) {
                        // this cell is the only cell that can accept
                        // this value. set all of the other availables to conflicts
                        for (int no = 0; no < 9; no++) {
                            if (no++ != value) {
                                cell.setPossibleValue(no, conflictType);
                            }
                        }
                        // we have our solution!
                        return;
                    }
                }
            }
        }
    }

    void checkForcingColumn(PuzzleCell cell) {

        // check the possible values of cells in this column.
        // if other columns in this square have any of the possibles
        // assigned, this cell must have this value
        if (cell != null
                && cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE
                && cell.getPossibleValues().length != 1) {

            // get the set of cells that are assigned in the other columns
            // that are in the same square as this cell's colum.
            PuzzleColumn[] otherCols = new PuzzleColumn[2];
            int cnt = 0;
            for (PuzzleCell rowCell : cell.getRow().getCells()) {
                if (rowCell.getSquare() == cell.getSquare()) {
                    // same square, test for same column
                    if (rowCell.getColumn() != cell.getColumn()) {
                        otherCols[cnt++] = rowCell.getColumn();
                        if (cnt > 1) {
                            // we found both columns
                            break;
                        }
                    }
                }
            }
            Stack<Integer> columnForcedValues = new Stack<>();
            for (PuzzleCell col0 : otherCols[0].getUnavailableCells()) {
                // excluded values that are in the test cell's square!!
                if (cell.getSquare().containsValue(col0.getValue()) != null) {
                    continue;
                }
                for (PuzzleCell col1 : otherCols[1].getUnavailableCells()) {
                    // excluded values that are in the test cell's square!!
                    if (cell.getSquare().containsValue(col1.getValue()) != null) {
                        continue;
                    }
                    if (col0.getValue() == col1.getValue()) {
                        // we have a match of values that must be in our
                        // test cell's column
                        columnForcedValues.add(col0.getValue());
                    }
                }
            }
            if (columnForcedValues.isEmpty()) {
                // didn't find any forced values
                return;
            }

            // get the other cells in this column that are also in this square
            // eliminate any forced values that can be met by neighbors
            PuzzleCell[] neighbors = new PuzzleCell[2];
            cnt = 0;
            for (PuzzleCell n : cell.getColumn().getCells()) {
                if (n == cell) {
                    continue;
                }
                if (n.getSquare() == cell.getSquare()) {
                    neighbors[cnt++] = n;
                    if (cnt > 1) {
                        // both neighbors accounted for
                        break;
                    }
                }
            }
            Integer[] values;
            values = columnForcedValues.toArray(new Integer[0]);
            for (Integer value : values) {
                if (neighbors[0].ValueIsAvailable(value - 1)) {
                    columnForcedValues.remove(value);
                    continue;
                }
                if (neighbors[0].getValue() == value) {
                    // this value is already assigned
                    columnForcedValues.remove(value);
                    continue;
                }
                if (neighbors[1].ValueIsAvailable(value - 1)) {
                    columnForcedValues.remove(value);
                    continue;
                }
                if (neighbors[1].getValue() == value) {
                    // this value is already assigned
                    columnForcedValues.remove(value);
                }
            }
            if (columnForcedValues.size() == 1) {
                // we have a forced value!
                Integer forcedValue = columnForcedValues.firstElement();
                for (int i = 0; i < 9; i++) {
                    if (i != forcedValue - 1) {
                        cell.setPossibleValue(i, NumberDialog.COL_FORCED);
                    }
                }
            }
        }
    }

    void checkForcingRow(PuzzleCell cell) {

        // check the possible values of cells in this row.
        // if other rows in this square have any of the possibles
        // assigned, this cell must have this value
        if (cell != null
                && cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE
                && cell.getPossibleValues().length != 1) {

            // get the set of cells that are assigned in the other rows
            // that are in the same square as this cell's row.
            PuzzleRow[] otherRows = new PuzzleRow[2];
            int cnt = 0;
            for (PuzzleCell rowCell : cell.getColumn().getCells()) {
                if (rowCell.getSquare() == cell.getSquare()) {
                    // same square, test for same row
                    if (rowCell.getRow() != cell.getRow()) {
                        otherRows[cnt++] = rowCell.getRow();
                        if (cnt > 1) {
                            // we found both rows
                            break;
                        }
                    }
                }
            }
            Stack<Integer> rowForcedValues = new Stack<>();
            for (PuzzleCell row0 : otherRows[0].getUnavailableCells()) {
                // excluded cells that are in the test cell's square!!
                if (cell.getSquare().containsValue(row0.getValue()) != null) {
                    continue;
                }
                for (PuzzleCell row1 : otherRows[1].getUnavailableCells()) {
                    // excluded cells that are in the test cell's square!!
                    if (cell.getSquare().containsValue(row1.getValue()) != null) {
                        continue;
                    }
                    if (row0.getValue() == row1.getValue()) {
                        // we have a match of values that must be in out
                        // test cell's row
                        rowForcedValues.add(row0.getValue());
                    }
                }
            }
            if (rowForcedValues.isEmpty()) {
                // didn't find any forced values
                return;
            }

            // get the other cells in this row that are also in this square
            // eliminate any forced values that can be met by neighbors
            PuzzleCell[] neighbors = new PuzzleCell[2];
            cnt = 0;
            for (PuzzleCell n : cell.getRow().getCells()) {
                if (n == cell) {
                    continue;
                }
                if (n.getSquare() == cell.getSquare()) {
                    neighbors[cnt++] = n;
                    if (cnt > 1) {
                        // both neighbors accounted for
                        break;
                    }
                }
            }

            // make sure neither of them has this value assigned,
            // and that the value is not available
            Integer[] values;
            values = rowForcedValues.toArray(new Integer[0]);
            for (Integer value : values) {
                if (neighbors[0].getValue() == value) {
                    // this value is already assigned
                    rowForcedValues.remove(value);
                    continue;
                }
                if (neighbors[0].ValueIsAvailable(value - 1)) {
                    rowForcedValues.remove(value);
                    continue;
                }
                if (neighbors[1].getValue() == value) {
                    // this value is already assigned
                    rowForcedValues.remove(value);
                    continue;
                }
                if (neighbors[1].ValueIsAvailable(value - 1)) {
                    rowForcedValues.remove(value);
                }
            }
            if (rowForcedValues.size() == 1) {
                // we have a forced value!
                Integer forcedValue = rowForcedValues.firstElement();
                for (int i = 0; i < 9; i++) {
                    if (i != forcedValue - 1) {
                        cell.setPossibleValue(i, NumberDialog.ROW_FORCED);
                    }
                }
            }
        }
    }

    void eliminateAllAssignedValues(PuzzleCell cell) {

        if (cell != null
                && cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE
                && cell.getPossibleValues().length != 1) {

            // check all cells in this set to eliminate values
            PuzzleRow row = cell.getRow();
            PuzzleCell[] buttons = row.getCells();
            for (PuzzleCell b : buttons) {
                if (b.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                    continue;
                }
                cell.setPossibleValue(b.getValue() - 1, NumberDialog.ROW_CONFLICT);
            }
            // check all cells in this column to eliminate values
            PuzzleColumn col = cell.getColumn();
            buttons = col.getCells();
            for (PuzzleCell b : buttons) {
                if (b.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                    continue;
                }
                cell.setPossibleValue(b.getValue() - 1, NumberDialog.COL_CONFLICT);
            }
            // check all cells in this square to eliminate values
            PuzzleSquare sq = cell.getSquare();
            buttons = sq.getCells();
            for (PuzzleCell b : buttons) {
                if (b.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                    continue;
                }
                cell.setPossibleValue(b.getValue() - 1, NumberDialog.SQ_CONFLICT);
            }
        }
    }

    void checkSquareConstrainedRows(PuzzleCell cell) {

        if (cell != null
                && cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE
                && cell.getPossibleValues().length != 1) {

            // check all cells in this set in neighboring squares to eliminate values
            // build a list of values that are implicitly required in nextCell in this
            // set, that are in neighboring squares.
            PuzzleSquare[] nsq = _win.getSquareRowNeighbors(cell);
            PuzzleRow row = cell.getRow();
            for (PuzzleSquare neighborSquare : nsq) {
                PuzzleCell[] neighborCells = new PuzzleCell[3];
                int noCells = 0;
                for (PuzzleCell rowCell : row.getCells()) {
                    if (rowCell.getSquare() == neighborSquare) {
                        if (rowCell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                            neighborCells[noCells++] = rowCell;
                        }
                    }
                }
                // this test only works if two or three cells in this set,
                // and the neighbor nextCell, are available for value assignment

                if (noCells == 2) {
                    // both cells must have exactly two values available
                    // compute possible values for these cells
                    checkSetPossibles(neighborCells[0]);
                    checkSetPossibles(neighborCells[1]);
                    Integer[] pv0 = neighborCells[0].getPossibleValues();
                    Integer[] pv1 = neighborCells[1].getPossibleValues();
                    if (pv0.length == 2 && pv1.length == 2) {
                        // the two available values must be identical
                        boolean match = true;
                        for (int v = 0; v < 2; v++) {
                            if (!Objects.equals(pv0[v], pv1[v])) {
                                match = false;
                                break;
                            }
                        }
                        if (match == true) {
                            cell.setPossibleValue(pv0[0], NumberDialog.ROW_CONFLICT);
                            cell.setPossibleValue(pv0[1], NumberDialog.ROW_CONFLICT);
                        }
                    }
                } else if (noCells == 3) {
                    // all three cells must have exactly three values available
                    // compute possible values for these cells
                    checkSetPossibles(neighborCells[0]);
                    checkSetPossibles(neighborCells[1]);
                    checkSetPossibles(neighborCells[2]);
                    Integer[] pv0 = neighborCells[0].getPossibleValues();
                    Integer[] pv1 = neighborCells[1].getPossibleValues();
                    Integer[] pv2 = neighborCells[2].getPossibleValues();
                    if (pv0.length == 3 && pv1.length == 3 && pv2.length == 3) {
                        // the three available values must be identical
                        boolean match = true;
                        for (int v = 0; v < 3; v++) {
                            if (!Objects.equals(pv0[v], pv1[v])) {
                                match = false;
                                break;
                            }
                            if (!Objects.equals(pv1[v], pv2[v])) {
                                match = false;
                                break;
                            }
                        }
                        if (match == true) {
                            cell.setPossibleValue(pv0[0], NumberDialog.ROW_CONFLICT);
                            cell.setPossibleValue(pv0[1], NumberDialog.ROW_CONFLICT);
                            cell.setPossibleValue(pv0[2], NumberDialog.ROW_CONFLICT);
                        }
                    }
                }
            }
        }
    }

    void checkSquareConstrainedColumns(PuzzleCell cell) {

        if (cell != null
                && cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE
                && cell.getPossibleValues().length != 1) {

            // check all cells in this column in neighboring squares to eliminate values
            // build a list of values that are implicitly required in nextCell in this
            // column, that are in neighboring squares.
            PuzzleColumn col = cell.getColumn();
            PuzzleSquare[] nsq = _win.getSquareColumnNeighbors(cell);
            for (PuzzleSquare neighborSquare : nsq) {
                PuzzleCell[] neighborCells = new PuzzleCell[3];
                int noCells = 0;
                for (PuzzleCell colCell : col.getCells()) {
                    if (colCell.getSquare() == neighborSquare) {
                        if (colCell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                            neighborCells[noCells++] = colCell;
                        }
                    }
                }
                // this test only works if two or three cells in this column,
                // and the neighbor nextCell, are available for value assignment

                if (noCells == 2) {
                    // both cells must have exactly two values available
                    // compute possible values for these cells
                    checkSetPossibles(neighborCells[0]);
                    checkSetPossibles(neighborCells[1]);
                    Integer[] pv0 = neighborCells[0].getPossibleValues();
                    Integer[] pv1 = neighborCells[1].getPossibleValues();
                    if (pv0.length == 2 && pv1.length == 2) {
                        // the two available values must be identical
                        boolean match = true;
                        for (int v = 0; v < 2; v++) {
                            if (!Objects.equals(pv0[v], pv1[v])) {
                                match = false;
                                break;
                            }
                        }
                        if (match == true) {
                            cell.setPossibleValue(pv0[0], NumberDialog.COL_CONFLICT);
                            cell.setPossibleValue(pv0[1], NumberDialog.COL_CONFLICT);
                        }
                    }
                } else if (noCells == 3) {
                    // all three cells must have exactly three values available
                    // compute possible values for these cells
                    checkSetPossibles(neighborCells[0]);
                    checkSetPossibles(neighborCells[1]);
                    checkSetPossibles(neighborCells[2]);
                    Integer[] pv0 = neighborCells[0].getPossibleValues();
                    Integer[] pv1 = neighborCells[1].getPossibleValues();
                    Integer[] pv2 = neighborCells[2].getPossibleValues();
                    if (pv0.length == 3 && pv1.length == 3 && pv2.length == 3) {
                        // the three available values must be identical
                        boolean match = true;
                        for (int v = 0; v < 3; v++) {
                            if (!Objects.equals(pv0[v], pv1[v])) {
                                match = false;
                                break;
                            }
                            if (!Objects.equals(pv1[v], pv2[v])) {
                                match = false;
                                break;
                            }
                        }
                        if (match == true) {
                            cell.setPossibleValue(pv0[0], NumberDialog.COL_CONFLICT);
                            cell.setPossibleValue(pv0[1], NumberDialog.COL_CONFLICT);
                            cell.setPossibleValue(pv0[2], NumberDialog.COL_CONFLICT);
                        }
                    }
                }
            }
        }
    }

    void checkConstrainedSets(PuzzleCell cell) {

        if (cell != null
                && cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE
                && cell.getPossibleValues().length != 1) {

            PuzzleCell[] setCells = cell.getSquare().getAvailableCells();
            int conflictType = NumberDialog.SQ_CONFLICT;
            for (int s = 0; s < 3; s++) {
                switch (s) {
                    case 0:
                        break;
                    case 1:
                        setCells = cell.getColumn().getAvailableCells();
                        conflictType = NumberDialog.COL_CONFLICT;
                        break;
                    case 2:
                        setCells = cell.getRow().getAvailableCells();
                        conflictType = NumberDialog.ROW_CONFLICT;
                        break;
                }

                // construct an array of all of the available cells that are in this
                // set, but do not included the test cell
                PuzzleCell[] nc = new PuzzleCell[8];
                int noCells = 0;
                for (PuzzleCell setCell : setCells) {
                    if (setCell == cell) {
                        continue;
                    }
                    if (setCell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                        nc[noCells++] = setCell;
                    }
                }
                PuzzleCell[] neighborCells = new PuzzleCell[noCells];
                System.arraycopy(nc, 0, neighborCells, 0, noCells);
                
                // this test only works if the tested cells, are available for value assignment
                // and have exactly the same possible value sets.
                for (int setSize = neighborCells.length; setSize > 1; setSize--) {
                    PuzzleCell[] matchList = new PuzzleCell[neighborCells.length];
                    int noMatches = 0;
                    for (PuzzleCell neighbor : neighborCells) {
                        if (neighbor.getPossibleValues().length == setSize) {
                            matchList[noMatches++] = neighbor;
                        }
                    }
                    if (noMatches >= setSize) {
                        // very rare for this to be greater than 3
                        // see if there are exactly n cells that have identical
                        // available lists. n must be equal to the setSize
                        // we have to do a bubble sort to check all possibilities
                        TreeMap<PuzzleCell, ArrayList<PuzzleCell>> matchedPairs = new TreeMap<>();
                        for (int n = 0; n < noMatches - 1; n++) {
                            for (int c = n + 1; c < noMatches; c++) {
                                if (matchList[n].availablesMatch(matchList[c]) == true) {
                                    if (matchedPairs.containsKey(matchList[n])) {
                                        // already have at least one match
                                        matchedPairs.get(matchList[n]).add(matchList[c]);
                                    } else {
                                        // start a new list
                                        ArrayList<PuzzleCell> list = new ArrayList<>();
                                        // put both cells in the list
                                        list.add(matchList[n]);
                                        list.add(matchList[c]);
                                        matchedPairs.put(matchList[n], list);
                                    }
                                }
                            }
                        }
                        while (matchedPairs.size() > 0) {
                            PuzzleCell key = matchedPairs.firstKey();
                            ArrayList<PuzzleCell> cellList = matchedPairs.get(key);
                            if (cellList.size() == setSize) {
                                // we have a match! these cells have an identical
                                // list of possible values and the list length matches the
                                // set size. the values must exist in these cells,
                                // although we don't know the order. we can eliminate
                                // them as possiblilities for our test cell.
                                Integer[] possibles = key.getPossibleValues();
                                for (Integer value : possibles) {
                                    cell.setPossibleValue(value, conflictType);
                                }
                                // remove all of the cells in this list from the
                                // matched pair set
                                Iterator<PuzzleCell> it = cellList.iterator();
                                while(it.hasNext()) {
                                    matchedPairs.remove(it.next());
                                }
                            }
                            matchedPairs.remove(key);
                        }
                    }
                }
            }
        }
    }

    /* properties */
    private final PuzzleWindow _win;
}
