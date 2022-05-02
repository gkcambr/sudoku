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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author keithc
 */
public class SolutionTree {
    
    SolutionTree(PuzzleWindow win, int cellIndex) {
        myPuzzleWindow = win;
        myCellIndex = cellIndex;
        buildCandidateList();
        if ((DEBUG) && (cellIndex < getCandidateList().size())) {
            String logmsg = String.format("starting index # " + cellIndex
                    + " for cell # " + getCandidateList().get(cellIndex).getNumber());
            logMessage(logmsg);
        }
    }
    
    public static void logMessage(String msg) {
        
        if(myLog == null) {
            try {
                myLog = new PrintWriter(myLogFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SolutionTree.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        myLog.append(msg + "\n");
        myLog.flush();
    }

    public boolean search() {
        boolean ret = false;

        if (buildTryList() < 1) {
            if (DEBUG) {
                String logmsg = String.format(myTryCell.getNumber() + 
                        "try list is empty for cell # ");
                logMessage(logmsg);
            }
            return ret;
        }

        int myTryIndex = 0;

        while (myTryIndex < myTryList.length) {
            int tryValue = myTryList[myTryIndex];

            // try the available value
            myPuzzleWindow.setCell(myTryCell.getNumber(), tryValue, PuzzleCell.VALUE_TYPE_AUTO);

            // recompute possible values
            myPuzzleWindow.computePossibleValues();

            if (DEBUG) {
                String logmsg = String.format("trying value " + 
                        tryValue + " for cell " + myTryCell.getNumber());
                logMessage(logmsg);
            }

            if (myCellIndex == (getCandidateList().size() - 1)) {
                // we assigned a valid value to the last cell.
                // we found a solution for this cell!
                if (DEBUG) {
                    String logmsg = String.format("cell number " + 
                            myTryCell.getNumber() + " has a valid candidates, giving up");
                    logMessage(logmsg);
                }
                ret = true;
                break;
            }

            SolutionTree child = new SolutionTree(myPuzzleWindow, myCellIndex + 1);
            ret = child.search();

            if (ret) {
                // we have found a solution!
                break;
            }

            // the child has no candidates
            // reset the cell
            myTryCell.reset();

            // try the next possible value
            if (++myTryIndex >= myTryList.length) {
                // we tried the last value
                // means this solution can't work, back out
                if (DEBUG) {
                    String logmsg = String.format("cell number " + 
                            myTryCell.getNumber() + " has no more candidates, giving up");
                    logMessage(logmsg);
                }

                // recompute possible values
                myPuzzleWindow.computePossibleValues();
                ret = false;
                break;
            }
        }
        return ret;
    }

    final int buildCandidateList() {

        // populate the list of available cells. we will walk down
        // the list trying different values. keep track of the possible
        // values we have tried. if we move down the list and fail to
        // find a possible value, we must move back to the previous cell
        // in the list and try a different value. continue this process
        // until we have succeeded in trying a valid value in the last
        // cell in the list, or we have exhausted all possibilities.
        if (getCandidateList() == null) {
            setCandidateList( new LinkedList<>() );
            PuzzleRow[] rows = myPuzzleWindow.getRows();
            for (PuzzleRow row : rows) {
                PuzzleCell[] cells = row.getAvailableCells();
                getCandidateList().addAll(Arrays.asList(cells));
            }
        }
        myTryCell = getCandidateList().get(myCellIndex);
        return getCandidateList().size();
    }

    int buildTryList() {
        // populate the list of available values for this cell
        myTryList = myTryCell.computePossibleValues();
        return myTryList.length;

    }

    void reset() {
        setCandidateList(null);
    }
    
    private static void setCandidateList(LinkedList<PuzzleCell> list) {
        myCandidateList = list;
    }
    
    private static LinkedList<PuzzleCell> getCandidateList() {
        return myCandidateList;
    }

    /* properties */
    private final PuzzleWindow myPuzzleWindow;

    // list of cell candidates
    private static LinkedList<PuzzleCell> myCandidateList;

    // list of available values to try
    private Integer[] myTryList;

    // index to this cell
    private final int myCellIndex;
    private PuzzleCell myTryCell;

    static final boolean DEBUG = false;
    static String myLogFile = "sudoku.log";
    static PrintWriter myLog = null;
}
