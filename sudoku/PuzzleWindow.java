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

import helpUtilities.HelpMenu;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author keithc
 */
public class PuzzleWindow extends JFrame {

    PuzzleWindow() {
        super("Sudoku Unlocked");
        init();
    }

    final void init() {
        setLocation(600, 300);
        _pane.setSize(450, 450);
        this.setContentPane(_pane);
        createRows();
        createColumns();
        drawSquares();
        drawButtons();
        setTitle(TITLE);
        createMenuBar();
        getRootPane().setJMenuBar(_menuBar);
        setVisible(true);
        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                // do nothing
            }

            @Override
            public void windowClosing(WindowEvent e) {
                int dlgOption = JOptionPane.OK_OPTION;
                if ((_fileMenu.getOpenFile() != null) && (_fileMenu.isFileDirty())) {
                    Object[] options = {"OK", "CANCEL"};
                    dlgOption = JOptionPane.showOptionDialog(null,
                            "If you click OK you will lose the\n"
                            + "changes made to the current file.",
                            "Warning",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]);
                }
                if (dlgOption == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
                else {
                    Graphics g = _pane.getGraphics();
                    _pane.update(g);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // do nothing
            }

            @Override
            public void windowIconified(WindowEvent e) {
                // do nothing
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // do nothing
            }

            @Override
            public void windowActivated(WindowEvent e) {
                // do nothing
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // do nothing
            }

        });
    }

    void createMenuBar() {

        _menuBar = new JMenuBar();
        _fileMenu = new FileMenu(this);
        _menuBar.add(_fileMenu);
        _actionMenu = new ActionMenu(this);
        _menuBar.add(_actionMenu);
        _helpMenu = new HelpMenu(this);
        _menuBar.add(_helpMenu);
        _fileMenu.updateEnabledMenus(true);
    }

    void createRows() {
        for (int r = 0; r < 9; r++) {
            _row[r] = new PuzzleRow(r);
        }
    }

    void createColumns() {
        for (int c = 0; c < 9; c++) {
            _column[c] = new PuzzleColumn(c);
        }
    }

    void drawSquares() {
        // draw the puzzle pane
        GridBagLayout squareLayout = new GridBagLayout();
        _pane.setLayout(squareLayout);

        GridBagConstraints squareConstraints = new GridBagConstraints();
        int defaultWidth = squareConstraints.gridwidth;
        int defaultHeight = squareConstraints.gridheight;
        for (int sq = 0; sq < 9; sq++) {
            _square[sq] = new PuzzleSquare(sq / 3, sq % 3);
            squareConstraints.weightx = 1.0;
            squareConstraints.weighty = 1.0;
            squareConstraints.fill = GridBagConstraints.BOTH;

            // set the constraints
            switch (sq) {
                case 0:
                    squareConstraints.gridx = 0;
                    squareConstraints.gridy = 0;
                    squareConstraints.gridwidth = defaultWidth;
                    squareConstraints.gridheight = defaultHeight;
                    break;
                case 1:
                    squareConstraints.gridx = 1;
                    squareConstraints.gridy = 0;
                    squareConstraints.gridwidth = defaultWidth;
                    squareConstraints.gridheight = defaultHeight;
                    break;
                case 2:
                    squareConstraints.gridx = 2;
                    squareConstraints.gridy = 0;
                    squareConstraints.gridwidth = GridBagConstraints.REMAINDER;
                    squareConstraints.gridheight = defaultHeight;
                    break;
                case 3:
                    squareConstraints.gridx = 0;
                    squareConstraints.gridy = 1;
                    squareConstraints.gridwidth = defaultWidth;
                    squareConstraints.gridheight = defaultHeight;
                    break;
                case 4:
                    squareConstraints.gridx = 1;
                    squareConstraints.gridy = 1;
                    squareConstraints.gridwidth = defaultWidth;
                    squareConstraints.gridheight = defaultHeight;
                    break;
                case 5:
                    squareConstraints.gridx = 2;
                    squareConstraints.gridy = 1;
                    squareConstraints.gridwidth = GridBagConstraints.REMAINDER;
                    squareConstraints.gridheight = defaultHeight;
                    break;
                case 6:
                    squareConstraints.gridx = 0;
                    squareConstraints.gridy = 2;
                    squareConstraints.gridwidth = defaultWidth;
                    squareConstraints.gridheight = GridBagConstraints.REMAINDER;
                    break;
                case 7:
                    squareConstraints.gridx = 1;
                    squareConstraints.gridy = 2;
                    squareConstraints.gridwidth = defaultWidth;
                    squareConstraints.gridheight = GridBagConstraints.REMAINDER;
                    break;
                case 8:
                    squareConstraints.gridx = 2;
                    squareConstraints.gridy = 2;
                    squareConstraints.gridwidth = GridBagConstraints.REMAINDER;
                    squareConstraints.gridheight = GridBagConstraints.REMAINDER;
                    break;
            }

            _square[sq].setBorder(BorderFactory.createLineBorder(Color.black));
            GridBagLayout buttonLayout = new GridBagLayout();
            _square[sq].setLayout(buttonLayout);
            _square[sq].setVisible(true);
            squareLayout.setConstraints(_square[sq], squareConstraints);
            _pane.add(_square[sq]);
        }
        _pane.setVisible(true);
    }

    int findSquareNo(int row, int col) {
        assert (row > -1 && row < 9);
        assert (col > -1 && col < 9);
        int sq = 0;

        switch (row) {
            case 0:
            case 1:
            case 2:
                switch (col) {
                    case 0:
                    case 1:
                    case 2:
                        sq = 0;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        sq = 1;
                        break;
                    case 6:
                    case 7:
                    case 8:
                        sq = 2;
                        break;
                }
                break;
            case 3:
            case 4:
            case 5:
                switch (col) {
                    case 0:
                    case 1:
                    case 2:
                        sq = 3;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        sq = 4;
                        break;
                    case 6:
                    case 7:
                    case 8:
                        sq = 5;
                        break;
                }
                break;
            case 6:
            case 7:
            case 8:
                switch (col) {
                    case 0:
                    case 1:
                    case 2:
                        sq = 6;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        sq = 7;
                        break;
                    case 6:
                    case 7:
                    case 8:
                        sq = 8;
                        break;
                }
                break;
        }
        return sq;
    }

    void drawButtons() {

        for (int b = 0; b < 81; b++) {
            int r = b / 9;
            int c = b % 9;
            PuzzleRow row = _row[r];
            PuzzleColumn col = _column[c];
            int sqNo = findSquareNo(r, c);
            PuzzleSquare sq = _square[sqNo];
            GridBagConstraints buttonConstraints = new GridBagConstraints();
            int defaultWidth = buttonConstraints.gridwidth;
            int defaultHeight = buttonConstraints.gridheight;

            _cell[b] = new PuzzleCell(b, row, col, sq, this);
            _cell[b].setType(PuzzleCell.VALUE_TYPE_AVAILABLE);
            _cell[b].getRow().addMember(_cell[b]);
            _cell[b].getColumn().addMember(_cell[b]);
            _cell[b].getSquare().addMember(_cell[b]);

            buttonConstraints.weightx = 1.0;
            buttonConstraints.weighty = 1.0;
            buttonConstraints.fill = GridBagConstraints.BOTH;
            _cell[b].setFont(new Font("Arial", Font.PLAIN, 18));
            _cell[b].setMargin(new Insets(0, 0, 0, 0));
            _cell[b].setPreferredSize(new Dimension(25, 25));

            // set the constraints
            switch (b) {
                case 0:
                case 3:
                case 6:
                case 27:
                case 30:
                case 33:
                case 54:
                case 57:
                case 60:
                    buttonConstraints.gridx = 0;
                    buttonConstraints.gridy = 0;
                    buttonConstraints.gridwidth = defaultWidth;
                    buttonConstraints.gridheight = defaultHeight;
                    break;
                case 1:
                case 4:
                case 7:
                case 28:
                case 31:
                case 34:
                case 55:
                case 58:
                case 61:
                    buttonConstraints.gridx = 1;
                    buttonConstraints.gridy = 0;
                    buttonConstraints.gridwidth = defaultWidth;
                    buttonConstraints.gridheight = defaultHeight;
                    break;
                case 2:
                case 5:
                case 8:
                case 29:
                case 32:
                case 35:
                case 56:
                case 59:
                case 62:
                    buttonConstraints.gridx = 2;
                    buttonConstraints.gridy = 0;
                    buttonConstraints.gridheight = defaultHeight;
                    buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;
                    break;
                case 9:
                case 12:
                case 15:
                case 36:
                case 39:
                case 42:
                case 63:
                case 66:
                case 69:
                    buttonConstraints.gridx = 0;
                    buttonConstraints.gridy = 1;
                    buttonConstraints.gridwidth = defaultWidth;
                    buttonConstraints.gridheight = defaultHeight;
                    break;
                case 10:
                case 13:
                case 16:
                case 37:
                case 40:
                case 43:
                case 64:
                case 67:
                case 70:
                    buttonConstraints.gridx = 1;
                    buttonConstraints.gridy = 1;
                    buttonConstraints.gridwidth = defaultWidth;
                    buttonConstraints.gridheight = defaultHeight;
                    break;
                case 11:
                case 14:
                case 17:
                case 38:
                case 41:
                case 44:
                case 65:
                case 68:
                case 71:
                    buttonConstraints.gridx = 2;
                    buttonConstraints.gridy = 1;
                    buttonConstraints.gridheight = defaultHeight;
                    buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;
                    break;
                case 18:
                case 21:
                case 24:
                case 45:
                case 48:
                case 51:
                case 72:
                case 75:
                case 78:
                    buttonConstraints.gridx = 0;
                    buttonConstraints.gridy = 2;
                    buttonConstraints.gridwidth = defaultWidth;
                    buttonConstraints.gridheight = GridBagConstraints.REMAINDER;
                    break;
                case 19:
                case 22:
                case 25:
                case 46:
                case 49:
                case 52:
                case 73:
                case 76:
                case 79:
                    buttonConstraints.gridx = 1;
                    buttonConstraints.gridy = 2;
                    buttonConstraints.gridwidth = defaultWidth;
                    buttonConstraints.gridheight = GridBagConstraints.REMAINDER;
                    break;
                case 20:
                case 23:
                case 26:
                case 47:
                case 50:
                case 53:
                case 74:
                case 77:
                case 80:
                    buttonConstraints.gridx = 2;
                    buttonConstraints.gridy = 2;
                    buttonConstraints.gridheight = GridBagConstraints.REMAINDER;
                    buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;
                    break;
            }

            _cell[b].setBackground(PuzzleCell.FIELD_COLOR);
            _cell[b].setVisible(true);
            GridBagLayout layout = (GridBagLayout) sq.getLayout();
            layout.setConstraints(_cell[b], buttonConstraints);
            sq.add(_cell[b]);
            setVisible(true);
        }
    }

    void selectCell(PuzzleCell cell) {
        if (_selectedCell != null) {
            _selectedCell.highlight(false);
        }
        _selectedCell = cell;
        _selectedCell.highlight(true);
    }

    void openNumberDialog(PuzzleCell cell) {
        if (_numberDialog != null) {
            _numberDialog.showHint();
            _numberDialog.reset();
            return;
        }
        _numberDialog = new NumberDialog(this, false);
        Point pt = getLocationOnScreen();
        int ndw = _numberDialog.getWidth();
        if (pt.x > ndw) {
            pt.x -= ndw;
        } else {
            pt.x += ndw;
        }
        _numberDialog.setLocation(pt);
        _numberDialog.showHint();
        _numberDialog.setVisible(true);
    }

    void closeNumberDialog() {
        _numberDialog = null;
        clearHighlights();
    }

    void clearHighlights() {
        if (_highlightedCell != null) {
            _highlightedCell.highlight(false);
            _highlightedCell = null;
        }
    }

    int updateCell(PuzzleCell cell, int value, int type) {
        if (cell == null) {
            return NumberDialog.SELECTION_INVALID;
        }
        _selectedCell = cell;
        if (type == PuzzleCell.VALUE_TYPE_UPDATE_CLR) {
            _selectedCell.reset();
            return NumberDialog.SELECTION_OK;
        }
        PuzzleCell existingButton = _selectedCell.getRow().containsValue(value);
        if (existingButton != null) {
            existingButton.showError(true);
            _highlightedCell = existingButton;
            return NumberDialog.ROW_CONFLICT;
        }
        existingButton = _selectedCell.getColumn().containsValue(value);
        if (existingButton != null) {
            existingButton.showError(true);
            _highlightedCell = existingButton;
            return NumberDialog.COL_CONFLICT;
        }
        existingButton = _selectedCell.getSquare().containsValue(value);
        if (existingButton != null) {
            existingButton.showError(true);
            _highlightedCell = existingButton;
            return NumberDialog.SQ_CONFLICT;
        }
        _fileMenu.fileIsDirty();
        setCell(_selectedCell.getNumber(), value, type);
        if (type != PuzzleCell.VALUE_TYPE_AVAILABLE) {
            removeFromHintList(_selectedCell);
        }
        return NumberDialog.SELECTION_OK;
    }

    void clearAll() {
        clearEntries(PuzzleCell.VALUE_TYPE_ALL);
        _solver = new Solver(this);
        _hintList.clear();
        _selectedCell = null;
        _highlightedCell = null;
        _solutionCounts = new Integer[]{0, 0, 0, 0, 0, 0, 0};
        _solutionTree = null;
    }

    void clearEntries(int type) {
        for (PuzzleCell cell : _cell) {
            if (type == PuzzleCell.VALUE_TYPE_ALL || cell.getType() == type) {
                cell.reset();
            }
        }
    }

    void saveFile(String saveFile) {
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                byte[] fileHdr = FILE_ID.getBytes();
                for (byte ch : fileHdr) {
                    oos.write(ch);
                }
                for (int r = 0; r < 9; r++) {
                    PuzzleCell[] cells = _row[r].getCells();
                    for (PuzzleCell cell : cells) {
                        cell.writeObject(oos);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void readFile(String readFile) {
        boolean succeeded = false, hdrOk = true;

        clearEntries(PuzzleCell.VALUE_TYPE_ALL);
        PuzzleCell reader = new PuzzleCell(this);
        try {
            FileInputStream fis = new FileInputStream(readFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] fileHdr = FILE_ID.getBytes();
            for (byte ch : fileHdr) {
                byte fileCh = ois.readByte();
                if (ch != fileCh) {
                    hdrOk = false;
                    break;
                }
            }
            if (hdrOk) {
                reader.restoreAll(readFile, ois);
                succeeded = true;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        _selectedCell = null;
        if (!succeeded) {
            int indx = readFile.lastIndexOf("/");
            String name = readFile.substring(indx);
            JOptionPane.showMessageDialog(null, "File " + name
                    + "\nis not a Sudoku Solver File.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void setCell(int number, int value, int type) {
        PuzzleCell cell = _cell[number];
        cell.setValue(value);
        cell.setType(type);
        cell.setBackground(PuzzleCell.FIELD_COLOR);
        switch (type) {
            case PuzzleCell.VALUE_TYPE_SET:
                cell.setForeground(Color.BLACK);
                break;
            case PuzzleCell.VALUE_TYPE_TRY:
                cell.setForeground(Color.BLUE);
                break;
            case PuzzleCell.VALUE_TYPE_AUTO:
                cell.setForeground(Color.MAGENTA);
                break;
            default:
                break;
        }
    }

    PuzzleCell getSelectedCell() {
        return _selectedCell;
    }

    int computePossibleValues() {
        int totalHints = 0;

        // each check has to be completed for the entire cell population
        // before the next check can be undertaken
        for (PuzzleCell cell : _cell) {
            cell.resetPossibles();
        }

        // multiple passes are needed to allow computations to build on previous pass results
        for (int pass = 0; pass < MAX_COMPUTE_PASSES; pass++) {
            int hintsThisPass = 0;

            // eliminate all of the values in the cell's row, col and sq
            for (PuzzleCell cell : _cell) {
                _solver.eliminateAllAssignedValues(cell);
                if (cell.getPossibleValues().length == 1) {
                    if (addToHintList(cell, SOLUTION_EAV)) {
                        hintsThisPass += 1;
                    }
                }
            }

            // check square, row and column cell values for duplicate possibles
            // if there are no duplicates, we've found the value
            // check all columns in a square to see if values assigned
            // in these neighboring columns in other squares force a single
            // value into this cell - as it is the only possible place for
            // the value
            for (PuzzleCell cell : _cell) {
                _solver.checkForcingColumn(cell);
                if (cell.getPossibleValues().length == 1) {
                    if (addToHintList(cell, SOLUTION_CFC)) {
                        hintsThisPass += 1;
                    }
                }
            }

            // check all rows in a square to see if values assigned
            // in these neighboring columns in other squares force a single
            // value into this cell - as it is the only possible place for
            // the value
            for (PuzzleCell cell : _cell) {
                _solver.checkForcingRow(cell);
                if (cell.getPossibleValues().length == 1) {
                    if (addToHintList(cell, SOLUTION_CFR)) {
                        hintsThisPass += 1;
                    }
                }
            }

            // check adjacent squares to see if they have unassigned
            // cells in this cell's row and column that are constrained
            // to a limited set of values. if so, this cell is the only
            // location for the constrained possible value
            // check if the 2 or 3 other available cells in this row have
            // identical possibilities. if so, mark them as unavilable for
            // this cell.
            for (PuzzleCell cell : _cell) {
                _solver.checkConstrainedSets(cell);
                if (cell.getPossibleValues().length == 1) {
                    if (addToHintList(cell, SOLUTION_CCS)) {
                        hintsThisPass += 1;
                    }
                }
            }
            if (hintsThisPass == 0) {
                break;
            }
            totalHints += hintsThisPass;
        }
        return totalHints;
    }

    PuzzleCell getNextHint() {
        PuzzleCell cell = null;

        Iterator<PuzzleCell> it = _hintList.keySet().iterator();
        if (it.hasNext()) {
            cell = it.next();
        }
        return cell;
    }

    void solve() {
        resetSolutionCounts();
        while (computePossibleValues() > 0) {
            // computePossibleValues adds solutions to hintList
            while (!_hintList.isEmpty()) {
                PuzzleCell cell = getNextHint();
                if (cell.getPossibleValues().length == 1) {
                    Integer[] values = cell.getPossibleValues();
                    if (values.length == 1) {
                        updateCell(cell,
                                cell.getPossibleValues()[0] + 1,
                                PuzzleCell.VALUE_TYPE_AUTO);
                    }
                }
                _hintList.remove(cell);
            }
        }
        // is the puzzle solved?
        if (getAvailableCount() == 0) {
            // puzzle solved!
            JOptionPane.showMessageDialog(null,
                    "The puzzle has been solved.\n"
                    + "It is deterministic, meaning\n"
                    + "this solution is inique.",
                    "Puzzle Solved",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // we've exhausted all deterministic solutions
        // we're going to try to solve by exhaustive guess
        if (_solutionTree == null) {           

            // soltionTree is recursive, don't start another one
            _solutionTree = new SolutionTree(this, null);
            new Thread(_solutionTree).start();
        }
    }

    boolean addToHintList(PuzzleCell cell, Integer solutionType) {
        boolean ret = false;

        if (!_hintList.containsKey(cell)
                && cell.getPossibleValues().length == 1) {
            _hintList.put(cell, solutionType);
            ret = true;
        }
        return ret;
    }

    void removeFromHintList(PuzzleCell cell) {
        if (_hintList.containsKey(cell)) {
            Integer solutionNo = _hintList.get(cell);
            _solutionCounts[solutionNo] += 1;
            _hintList.remove(cell);
        }
    }

    PuzzleSquare[] getSquareRowNeighbors(PuzzleCell cell) {
        PuzzleSquare[] squares = new PuzzleSquare[2];

        int sq1 = 0, sq2 = 0;
        int sq0 = cell.getSquare().getNumber();
        switch (sq0) {
            case 0:
                sq1 = 1;
                sq2 = 2;
                break;
            case 1:
                sq1 = 0;
                sq2 = 2;
                break;
            case 2:
                sq1 = 0;
                sq2 = 1;
                break;
            case 3:
                sq1 = 4;
                sq2 = 5;
                break;
            case 4:
                sq1 = 3;
                sq2 = 5;
                break;
            case 5:
                sq1 = 3;
                sq2 = 4;
                break;
            case 6:
                sq1 = 7;
                sq2 = 8;
                break;
            case 7:
                sq1 = 6;
                sq2 = 8;
                break;
            case 8:
                sq1 = 6;
                sq2 = 7;
                break;
        }
        squares[0] = _square[sq1];
        squares[1] = _square[sq2];
        return squares;
    }

    PuzzleSquare[] getSquareColumnNeighbors(PuzzleCell cell) {
        PuzzleSquare[] squares = new PuzzleSquare[2];

        int sq1 = 0, sq2 = 0;
        int sq0 = cell.getSquare().getNumber();
        switch (sq0) {
            case 0:
                sq1 = 3;
                sq2 = 6;
                break;
            case 1:
                sq1 = 4;
                sq2 = 7;
                break;
            case 2:
                sq1 = 5;
                sq2 = 8;
                break;
            case 3:
                sq1 = 0;
                sq2 = 6;
                break;
            case 4:
                sq1 = 1;
                sq2 = 7;
                break;
            case 5:
                sq1 = 2;
                sq2 = 8;
                break;
            case 6:
                sq1 = 0;
                sq2 = 3;
                break;
            case 7:
                sq1 = 1;
                sq2 = 4;
                break;
            case 8:
                sq1 = 2;
                sq2 = 5;
                break;
        }
        squares[0] = _square[sq1];
        squares[1] = _square[sq2];
        return squares;
    }

    void showHints() {

        computePossibleValues();
        Iterator<PuzzleCell> it = _hintList.keySet().iterator();
        while (it.hasNext()) {
            PuzzleCell cell = it.next();
            selectCell(cell);
            cell.markAsHint(true);
            if (_numberDialog != null) {
                _numberDialog.showHint();
            }
            return;
        }
    }

    void resetSolutionCounts() {
        for (Integer sol : _solutionCounts) {
            sol = 0;
        }
    }

    int getAvailableCount() {
        int cnt = 0;

        for (PuzzleRow row : _row) {
            cnt += row.getAvailableCells().length;
        }
        return cnt;
    }

    PuzzleRow[] getRows() {
        return _row;
    }
    
    void updateEnabledMenus(boolean enabled) {
        _fileMenu.updateEnabledMenus(enabled);
        _actionMenu.updateEnabledMenus(enabled);
        _helpMenu.updateEnabledMenus(enabled);
    }

    /* properties */
    final static String TITLE = "Sudoku Unlocked";
    final static String FILE_ID = "SudokuSolverVersion_1_0";
    private JMenuBar _menuBar;
    private FileMenu _fileMenu;
    private ActionMenu _actionMenu;
    private HelpMenu _helpMenu;
    private final JPanel _pane = new JPanel();
    private Solver _solver = new Solver(this);

    private final PuzzleRow[] _row = new PuzzleRow[9];
    private final PuzzleColumn[] _column = new PuzzleColumn[9];
    private final PuzzleSquare[] _square = new PuzzleSquare[9];
    private final PuzzleCell[] _cell = new PuzzleCell[81];
    private PuzzleCell _selectedCell = null;
    private NumberDialog _numberDialog;
    private PuzzleCell _highlightedCell = null;
    private SolutionTree _solutionTree = null;

    private Integer[] _solutionCounts = new Integer[]{0, 0, 0, 0, 0, 0, 0};
    final int SOLUTION_EAV = 0;
    final int SOLUTION_CCS = 1;
    final int SOLUTION_CFC = 2;
    final int SOLUTION_CFR = 3;
    final int SOLUTION_CSP = 4;
    final int SOLUTION_CSCC = 5;
    final int SOLUTION_CSCR = 6;

    final int MAX_COMPUTE_PASSES = 4;

    LinkedHashMap<PuzzleCell, Integer> _hintList = new LinkedHashMap<>();

    private static final long serialVersionUID = 101;
}
