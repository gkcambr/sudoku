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

import java.util.Stack;
import javax.swing.JOptionPane;

/**
 *
 * @author keithc
 */
public class SolutionTree implements Runnable {

    SolutionTree(PuzzleWindow win, PuzzleCell parent) {
        _puzzleWindow = win;
        _parentCell = parent;
        _instanceNo = ++_instanceCnt;
        int cellNo = -1;
        if (parent != null) {
            cellNo = parent.getNumber();
        }
        if (DEBUG) {
            System.out.println("cell # " + cellNo + " is starting instance # " + _instanceNo);
        }
    }

    @Override
    public void run() {

        // disable menus while solutionTree is running
        _puzzleWindow.updateEnabledMenus(false);

        boolean result = solve();

        // is the puzzle solved?
        if (result == true && _puzzleWindow.getAvailableCount() == 0) {
            // puzzle solved!
            JOptionPane.showMessageDialog(null,
                    "The puzzle has been solved.\n"
                    + "It was solved by recursive trial.\n"
                    + "There may be other solutions.",
                    "Puzzle Solved",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // puzzle NOT solved!
            JOptionPane.showMessageDialog(null,
                    "The puzzle was not solved.\n"
                    + "There may be no solutions.",
                    "Puzzle Not Solved",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // enable menus
        _puzzleWindow.updateEnabledMenus(true);
    }

    boolean solve() {
        boolean ret = false;

        int candidates = buildCandidateList();
        if (candidates == 0) {
            // no candidates means this solution can't work, back out
            if (DEBUG) {
                System.out.println("instance " + _instanceNo + " has no candidates, giving up");
            }
            return ret;
        }

        int cellNo = -1;
        if (_parentCell != null) {
            cellNo = _parentCell.getNumber();
        }
        if (DEBUG) {
            System.out.print("instance # " + _instanceNo + " cell # " + cellNo + " candidate list:");
            for (PuzzleCell c : _cellCandidates) {
                System.out.print(" " + c.getNumber());
            }
            System.out.print("\n");
        }
        while (_cellCandidates.size() > 0) {
            PuzzleCell cell = getNextCell();
            if (_instanceNo == 1) {
                int f = 1;
            }
            if (cell != null) {
                ret = tryPossible(cell);
                if (ret == false) {
                    _cellCandidates.remove(cell);
                } else {
                    if (DEBUG) {
                        System.out.println("SUCCESS");
                    }
                    break;
                }
            }
        }
        _cellTries.clear();
        return ret;
    }

    int buildCandidateList() {

        // populate a set of stacks according to the number of
        // possible values in the cell members. for example, a
        // cell with two possible values will go into the third
        // stack in the list
        if (_cellCandidates == null) {
            _cellCandidates = new Stack<>();
        }
        for (int pv = 9; pv > 0; pv--) {
            for (PuzzleRow row : _puzzleWindow.getRows()) {
                if (row != null) {
                    PuzzleCell[] avails = row.getAvailableCells();
                    for (PuzzleCell cell : avails) {
                        if (cell.getPossibleValues().length == pv) {
                            _cellCandidates.push(cell);
                        }
                    }
                }
            }
        }
        return _cellCandidates.size();
    }

    PuzzleCell getNextCell() {
        PuzzleCell cell = null;

        if (_cellCandidates == null) {
            _cellCandidates = new Stack<>();
            buildCandidateList();
        }

        if (!_cellCandidates.isEmpty()) {
            cell = _cellCandidates.pop();
        }
        return cell;
    }

    boolean tryPossible(PuzzleCell cell) {
        boolean ret = false;

        _puzzleWindow.computePossibleValues();
        Integer[] values = cell.getPossibleValues();
        for (Integer val : values) {
            int noHints = 0;
            _cellTries.add(cell);
            int result1 = _puzzleWindow.updateCell(
                    cell,
                    val + 1,
                    PuzzleCell.VALUE_TYPE_AUTO);
            if (result1 != NumberDialog.SELECTION_OK) {
                int f = 1;
                continue;
            }
            // process all hints that are generated from this
            // value try assumption
            while (_puzzleWindow.computePossibleValues() > 0) {
                PuzzleCell nextCell = _puzzleWindow.getNextHint();
                while (nextCell != null) {
                    noHints += 1;
                    _cellTries.add(nextCell);
                    int result2 = NumberDialog.SELECTION_INVALID;
                    if (nextCell.getPossibleValues().length == 1) {
                        result2 = _puzzleWindow.updateCell(
                                nextCell,
                                nextCell.getPossibleValues()[0] + 1,
                                PuzzleCell.VALUE_TYPE_AUTO);
                    }
                    if (result2 != NumberDialog.SELECTION_OK) {
                        int f = 1;
                    }
                    _puzzleWindow.removeFromHintList(nextCell);
                    nextCell = _puzzleWindow.getNextHint();
                }
            }
            // did we solve the puzzle?
            if (_puzzleWindow.getAvailableCount() == 0) {
                // puzzled solved!
                ret = true;
                break;
            }
            if (noHints > 0) {
                // we made some progress,
                // try to solve the remainder with another guess
                if (DEBUG) {
                    System.out.println("instance # " + _instanceNo + " trying cell # " + cell.getNumber() + " using value "
                            + cell.getValue() + " generated " + noHints + " hints");
                }
                SolutionTree nextTree = new SolutionTree(_puzzleWindow, cell);
                boolean solved = nextTree.solve();
                if (solved) {
                    // puzzle solved!
                    ret = true;
                    break;
                } else {
                    // puzzle NOT solved, try the next possible
                    restoreCellTries();
                }
            } else {
                // this is a dead end. our guess resulted in
                // no hints at all - no progress
                // restore all of the tried cells and move on
                // to the next possible value for the cell
                restoreCellTries();
                ret = false;
            }
        }
        return ret;
    }

    void restoreCellTries() {
        // put back all of the cells in the try list
        // to the available state
        while (!_cellTries.isEmpty()) {
            int lastCellNo = _cellTries.size() - 1;
            PuzzleCell nextCell = _cellTries.elementAt(lastCellNo);
            _puzzleWindow.updateCell(nextCell,
                    PuzzleCell.UNDEFINED,
                    PuzzleCell.VALUE_TYPE_UPDATE_CLR);
            _cellTries.remove(lastCellNo);
        }
    }

    /* properties */
    private final PuzzleWindow _puzzleWindow;
    // list of cell candidates
    private Stack<PuzzleCell> _cellCandidates;
    // needed to unwind our attempts if we fail
    private final Stack<PuzzleCell> _cellTries = new Stack<>();

    private static int _instanceCnt = 0;
    private int _instanceNo = 0;
    private final PuzzleCell _parentCell;

    static boolean DEBUG = false;
}
