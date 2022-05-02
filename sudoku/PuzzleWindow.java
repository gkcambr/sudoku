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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author keithc
 */
public class PuzzleWindow extends JFrame {

    PuzzleWindow() {
        super(TITLE);
        this.myHintList = new LinkedList<>();
        init();
    }

    final void init() {
        setLocation(600, 300);
        this.setMinimumSize(new Dimension(400, 620));

        try {
            // load the icon
            String path = new File(".").getCanonicalPath()
                    + File.separator + "sudoku.jpg";
            ImageIcon icon = new ImageIcon(path);
            Image image = icon.getImage();
            this.setIconImage(image);
        } catch (IOException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        // create the content pane
        myContentPane = (JPanel) this.getContentPane();
        myContentPane.setLayout(new BorderLayout(10, 10));

        myContentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        myPuzzlePane.setPreferredSize(new Dimension(400, 460));
        myContentPane.add(myPuzzlePane, BorderLayout.CENTER);
        myInfoPane.setPreferredSize(new Dimension(400, 160));
        myContentPane.add(myInfoPane, BorderLayout.AFTER_LAST_LINE);

        createRows();
        createColumns();
        drawSquares();
        drawCells();
        drawInfoPane();
        setTitle(TITLE);
        createMenuBar();
        getRootPane().setJMenuBar(myMenuBar);
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
                if ((myFileMenu.getOpenFile() != null) && (myFileMenu.isFileDirty())) {
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
                } else {
                    Graphics g = myContentPane.getGraphics();
                    myContentPane.update(g);
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

        myMenuBar = new JMenuBar();
        myFileMenu = new FileMenu(this);
        myMenuBar.add(myFileMenu);
        myActionMenu = new ActionMenu(this);
        myMenuBar.add(myActionMenu);
        myHelpMenu = new HelpMenu(this);
        myMenuBar.add(myHelpMenu);
        myFileMenu.updateEnabledMenus(true);
    }

    void createRows() {
        for (int r = 0; r < 9; r++) {
            myRow[r] = new PuzzleRow();
        }
    }

    void createColumns() {
        for (int c = 0; c < 9; c++) {
            myColumn[c] = new PuzzleColumn();
        }
    }

    void drawSquares() {
        // draw the puzzle pane
        GridBagLayout squareLayout = new GridBagLayout();
        myPuzzlePane.setLayout(squareLayout);

        GridBagConstraints squareConstraints = new GridBagConstraints();
        int defaultWidth = squareConstraints.gridwidth;
        int defaultHeight = squareConstraints.gridheight;
        for (int sq = 0; sq < 9; sq++) {
            mySquare[sq] = new PuzzleSquare(sq);
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
                default:
                    break;
            }

            mySquare[sq].getPanel().setBorder(BorderFactory.createLineBorder(Color.black));
            GridBagLayout buttonLayout = new GridBagLayout();
            mySquare[sq].getPanel().setLayout(buttonLayout);
            mySquare[sq].getPanel().setSize(
                    myContentPane.getWidth() / 3, myContentPane.getHeight() / 3);
            mySquare[sq].getPanel().setVisible(true);
            squareLayout.setConstraints(mySquare[sq].getPanel(), squareConstraints);
            myPuzzlePane.add(mySquare[sq].getPanel());
        }
        myPuzzlePane.setVisible(true);
    }

    void drawInfoPane() {

        // draw the info pane
        myInfoPane.setOpaque(false);
        GridBagLayout infoLayout = new GridBagLayout();
        myInfoPane.setLayout(infoLayout);

        // draw the buttons
        for (int b = 0; b < 18; b++) {
            GridBagConstraints buttonConstraints = new GridBagConstraints();
            int defaultWidth = buttonConstraints.gridwidth;
            int defaultHeight = buttonConstraints.gridheight;

            if (b < 9) {
                myInfoButton[b] = new JButton("" + (b + 1));
            } else {
                myInfoButton[b] = new JButton("0");
            }

            buttonConstraints.weightx = 1.0;
            buttonConstraints.weighty = 1.0;
            buttonConstraints.fill = GridBagConstraints.BOTH;
            myInfoButton[b].setFont(new Font("Arial", Font.PLAIN, 18));
            myInfoButton[b].setMargin(new Insets(0, 0, 0, 0));
            myInfoButton[b].setBorderPainted(false);
            Dimension buttonSize = new Dimension(myInfoPane.getWidth() / 9,
                    myInfoPane.getHeight() / 6);
            myInfoButton[b].setPreferredSize(buttonSize);

            // set the button constraints
            buttonConstraints.gridx = b % 9;
            buttonConstraints.gridy = b / 9;
            buttonConstraints.gridwidth = defaultWidth;
            buttonConstraints.gridheight = defaultHeight;

            myInfoButton[b].setBackground(PuzzleCell.FIELD_COLOR);
            myInfoButton[b].setVisible(true);
            infoLayout.setConstraints(myInfoButton[b], buttonConstraints);
            myInfoPane.add(myInfoButton[b]);
        }

        // draw the text pane
        JTextPane infoText = new JTextPane();
        GridBagConstraints textAreaConstraints = new GridBagConstraints();
        int textAreaWidth = textAreaConstraints.gridwidth * 9;
        int textAreaHeight = textAreaConstraints.gridheight;
        textAreaConstraints.weightx = 1.0;
        textAreaConstraints.weighty = 8.0;
        textAreaConstraints.fill = GridBagConstraints.BOTH;
        Dimension textAreaSize = new Dimension(myInfoPane.getWidth(),
                myInfoPane.getHeight() / 2);
        textAreaConstraints.gridx = 0;
        textAreaConstraints.gridy = 2;
        textAreaConstraints.gridwidth = textAreaWidth;
        textAreaConstraints.gridheight = textAreaHeight;
        infoText.setPreferredSize(textAreaSize);
        String infoString = "Select a cell with the mouse. Enter the puzzle starting point ";
        infoString += "using the 'set' button. Then try your own solution using the 'try' button.";
        infoString += " To solve, select 'solve'.";
        infoString += " You can load puzzles from the file menu, or save your own.\n\n";
        infoString += "For help, select this 'hint' button  ";
        infoText.setText(infoString);
        JButton hintButton = new JButton("hint");
        hintButton.setPreferredSize(new Dimension(50, 20));
        hintButton.setBackground(PuzzleCell.FIELD_COLOR);
        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showHints();
            }
        });
        infoText.setCaretPosition(infoString.length());
        infoText.insertComponent(hintButton);
        infoText.setEditable(false);

        infoLayout.setConstraints(infoText, textAreaConstraints);
        myInfoPane.add(infoText);
        setVisible(true);
    }

    void updateInfoCounts() {
        int[] counts = new int[9];
        for (int i = 0; i < 9; i++) {
            counts[i] = 0;
        }

        for (PuzzleCell cell : myCell) {
            if (cell.getType() != PuzzleCell.VALUE_TYPE_AVAILABLE) {
                counts[cell.getValue() - 1] = 1 + counts[cell.getValue() - 1];
            }
        }
        for (int b = 9; b < 18; b++) {
            myInfoButton[b].setText("" + counts[b - 9]);
        }
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
                    default:
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
                    default:
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
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return sq;
    }

    void drawCells() {

        for (int b = 0; b < 81; b++) {
            int r = b / 9;
            int c = b % 9;
            PuzzleRow row = myRow[r];
            PuzzleColumn col = myColumn[c];
            int sqNo = findSquareNo(r, c);
            PuzzleSquare sq = mySquare[sqNo];
            GridBagConstraints buttonConstraints = new GridBagConstraints();
            int defaultWidth = buttonConstraints.gridwidth;
            int defaultHeight = buttonConstraints.gridheight;

            myCell[b] = new PuzzleCell(b, row, col, sq, this);
            myCell[b].setType(PuzzleCell.VALUE_TYPE_AVAILABLE);
            myCell[b].setMargin(new Insets(0, 0, 0, 0));
            myCell[b].getRow().addMember(myCell[b]);
            myCell[b].getColumn().addMember(myCell[b]);
            myCell[b].getSquare().addMember(myCell[b]);

            buttonConstraints.weightx = 1.0;
            buttonConstraints.weighty = 1.0;
            buttonConstraints.fill = GridBagConstraints.BOTH;
            myCell[b].setFont(new Font("Arial", Font.PLAIN, 18));
            myCell[b].setMargin(new Insets(0, 0, 0, 0));
            myCell[b].setPreferredSize(new Dimension(25, 25));

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
                default:
                    break;
            }

            myCell[b].setBackground(PuzzleCell.FIELD_COLOR);
            myCell[b].setVisible(true);
            GridBagLayout layout = (GridBagLayout) sq.getPanel().getLayout();
            layout.setConstraints(myCell[b], buttonConstraints);
            sq.getPanel().add(myCell[b]);
            setVisible(true);
        }
    }

    void selectCell(PuzzleCell cell) {
        if (mySelectedCell != null) {
            mySelectedCell.highlight(false);
        }
        mySelectedCell = cell;
        mySelectedCell.highlight(true);
        if (myHintCell != null) {
            if (myHintCell.getText().contains("?")) {
                myHintCell.setText("");
            }
            myHintCell = null;
        }
    }

    void openNumberDialog(PuzzleCell cell) {
        if (myNumberDialog != null) {
            myNumberDialog.showHint();
            myNumberDialog.reset();
            if (cell.getType() != PuzzleCell.VALUE_TYPE_AVAILABLE) {
                myNumberDialog.selectButton(cell.getValue() - 1);
            }
            return;
        }
        myNumberDialog = new NumberDialog(this, false);
        Point pt = getLocationOnScreen();
        int ndw = myNumberDialog.getWidth();
        if (pt.x > ndw) {
            pt.x -= ndw;
        } else {
            pt.x += ndw;
        }
        myNumberDialog.setLocation(pt);
        myNumberDialog.showHint();
        myNumberDialog.setVisible(true);
        if (cell.getType() != PuzzleCell.VALUE_TYPE_AVAILABLE) {
            myNumberDialog.selectButton(cell.getValue() - 1);
        }
    }

    void closeNumberDialog() {
        if (myNumberDialog != null) {
            myNumberDialog.dispose();
            myNumberDialog = null;
        }
        clearHighlights();
    }

    void clearHighlights() {
        if (myHighlightedCell != null) {
            myHighlightedCell.highlight(false);
            myHighlightedCell = null;
        }
    }

    int updateCell(PuzzleCell cell, int value, int type) {
        if (cell == null) {
            return NumberDialog.SELECTION_INVALID;
        }
        mySelectedCell = cell;
        if (type == PuzzleCell.VALUE_TYPE_UPDATE_CLR) {
            mySelectedCell.reset();
            return NumberDialog.SELECTION_OK;
        }
        PuzzleCell existingButton = mySelectedCell.getRow().containsValue(value);
        if (existingButton != null) {
            existingButton.showError(true);
            myHighlightedCell = existingButton;
            return NumberDialog.ROW_CONFLICT;
        }
        existingButton = mySelectedCell.getColumn().containsValue(value);
        if (existingButton != null) {
            existingButton.showError(true);
            myHighlightedCell = existingButton;
            return NumberDialog.COL_CONFLICT;
        }
        existingButton = mySelectedCell.getSquare().containsValue(value);
        if (existingButton != null) {
            existingButton.showError(true);
            myHighlightedCell = existingButton;
            return NumberDialog.SQ_CONFLICT;
        }
        myFileMenu.fileIsDirty();
        setCell(mySelectedCell.getNumber(), value, type);
        if (type != PuzzleCell.VALUE_TYPE_AVAILABLE) {
            removeFromHintList(mySelectedCell);
        }
        return NumberDialog.SELECTION_OK;
    }

    void clearAll() {
        clearEntries(PuzzleCell.VALUE_TYPE_ALL);
        myHintList.clear();
        mySelectedCell = null;
        myHighlightedCell = null;
    }

    void clearEntries(int type) {
        for (PuzzleCell cell : myCell) {
            if (type == PuzzleCell.VALUE_TYPE_ALL || cell.getType() == type) {
                cell.reset();
            }
        }
        computePossibleValues();
        updateInfoCounts();
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
                    PuzzleCell[] cells = myRow[r].getCells();
                    for (PuzzleCell cell : cells) {
                        cell.store(oos);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void readFile(String readFile) {
        boolean succeeded = false;
        boolean hdrOk = true;

        clearEntries(PuzzleCell.VALUE_TYPE_ALL);
        PuzzleCell reader = new PuzzleCell(this);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(readFile);
            ois = new ObjectInputStream(fis);
            byte[] fileHdr = FILE_ID.getBytes();
            for (byte ch : fileHdr) {
                byte fileCh = ois.readByte();
                if (ch != fileCh) {
                    hdrOk = false;
                    break;
                }
            }
            if (hdrOk) {
                reader.restoreAll(ois);
                succeeded = true;
                computePossibleValues();
            }
        } catch (IOException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        mySelectedCell = null;
        if (!succeeded) {
            int indx = readFile.lastIndexOf("/");
            String name = readFile.substring(indx);
            JOptionPane.showMessageDialog(null, "File " + name
                    + "\nis not a Sudoku Solver File.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void setCell(int number, int value, int type) {
        PuzzleCell cell = myCell[number];
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
        updateInfoCounts();
    }

    PuzzleCell getSelectedCell() {
        return mySelectedCell;
    }

    int computePossibleValues() {

        myHintList.clear();

        // multiple passes are needed to allow computations to build on previous pass results
        for (int pass = 1; pass < MAX_COMPUTE_PASSES; pass++) {

            // grow the hint list by adding cells with the fewest number
            // of possible values first
            for (PuzzleCell cell : myCell) {
                if ((cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE)
                        && (cell.computePossibleValues().length == pass)) {
                    myHintList.add(cell);
                }
            }
        }
        return myHintList.size();
    }

    PuzzleCell getNextHint() {
        PuzzleCell cell = null;

        Iterator<PuzzleCell> it = myHintList.iterator();
        if (it.hasNext()) {
            cell = it.next();
        }
        return cell;
    }

    void solve() {

        Solver solver = new Solver(this);
        solver.solve();

        // is the puzzle solved?
        if (getAvailableCount() == 0) {
            // puzzle solved!
            JOptionPane.showMessageDialog(null,
                    "The puzzle has been solved.\n"
                    + "It is deterministic, meaning\n"
                    + "this solution is unique.",
                    "Puzzle Solved",
                    JOptionPane.INFORMATION_MESSAGE);
            closeNumberDialog();
        } else {
            // we've exhausted all deterministic solutions
            // we're going to try to solve by exhaustive guess

            int option = JOptionPane.showConfirmDialog(null,
                    "The puzzle has to be solved using\nrecursive trials. "
                    + "it may take some time.\nHit OK to continue.",
                    "Please Wait",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {

                this.requestFocus();
                Cursor cursor = this.getCursor();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                this.invalidate();

                // soltionTree is recursive, don't start another one
                SolutionTree solutionTree = new SolutionTree(this, 0);

                // disable menus while solutionTree is running
                updateEnabledMenus(false);

                if (solutionTree.search()) {
                    // puzzle solved!
                    JOptionPane.showMessageDialog(null,
                            "The puzzle has been solved.\n"
                            + "It was solved by recursive trial.\n"
                            + "There may be other solutions.",
                            "Puzzle Solved",
                            JOptionPane.INFORMATION_MESSAGE);
                    closeNumberDialog();
                } else {
                    // puzzle NOT solved!
                    JOptionPane.showMessageDialog(null,
                            "The puzzle was not solved.\n"
                            + "There may be no solutions.",
                            "Puzzle Not Solved",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                solutionTree.reset();
                this.setCursor(cursor);
            }

            // restore menus
            updateEnabledMenus(true);
        }
        updateInfoCounts();
    }

    void removeFromHintList(PuzzleCell cell) {
        if (myHintList.contains(cell)) {
            myHintList.remove(cell);
        }
    }

    PuzzleSquare[] getSquareRowNeighbors(PuzzleCell cell) {
        PuzzleSquare[] squares = new PuzzleSquare[2];

        int sq1 = 0;
        int sq2 = 0;
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
            default:
                break;
        }
        squares[0] = mySquare[sq1];
        squares[1] = mySquare[sq2];
        return squares;
    }

    PuzzleSquare[] getSquareColumnNeighbors(PuzzleCell cell) {
        PuzzleSquare[] squares = new PuzzleSquare[2];

        int sq1 = 0;
        int sq2 = 0;
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
            default:
                break;
        }
        squares[0] = mySquare[sq1];
        squares[1] = mySquare[sq2];
        return squares;
    }

    void showHints() {

        computePossibleValues();
        Iterator<PuzzleCell> it = myHintList.iterator();
        if (it.hasNext()) {
            PuzzleCell cell = it.next();
            selectCell(cell);
            cell.markAsHint(true);
            myHintCell = cell;
            if (myNumberDialog != null) {
                myNumberDialog.showHint();
            }
        }
    }

    int getAvailableCount() {
        int cnt = 0;

        for (PuzzleCell cell : myCell) {
            if (cell.getType() == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                cnt += 1;
            }
        }
        return cnt;
    }

    PuzzleRow[] getRows() {
        return myRow;
    }

    void updateEnabledMenus(boolean enabled) {
        myFileMenu.updateEnabledMenus(enabled);
        myActionMenu.updateEnabledMenus(enabled);
        myHelpMenu.updateEnabledMenus(enabled);
    }

    LinkedList<PuzzleCell> getHintList() {
        return myHintList;
    }

    /* properties */
    static final String TITLE = "Sudoku Unlocked";
    static final String FILE_ID = "SudokuSolverVersion_1_0";
    private JMenuBar myMenuBar;
    private FileMenu myFileMenu;
    private ActionMenu myActionMenu;
    private HelpMenu myHelpMenu;
    private JPanel myContentPane;
    private final JPanel myPuzzlePane = new JPanel();
    private final JPanel myInfoPane = new JPanel();

    private final PuzzleRow[] myRow = new PuzzleRow[9];
    private final PuzzleColumn[] myColumn = new PuzzleColumn[9];
    private final PuzzleSquare[] mySquare = new PuzzleSquare[9];
    private final PuzzleCell[] myCell = new PuzzleCell[81];
    private final JButton[] myInfoButton = new JButton[18];
    private PuzzleCell mySelectedCell = null;
    private NumberDialog myNumberDialog;
    private PuzzleCell myHighlightedCell = null;

    static final int MAX_COMPUTE_PASSES = 8;

    LinkedList<PuzzleCell> myHintList;
    PuzzleCell myHintCell = null;

    private static final long serialVersionUID = 101;
}
