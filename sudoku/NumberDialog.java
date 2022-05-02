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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author keithc
 */
public class NumberDialog extends JDialog {

    NumberDialog(PuzzleWindow win, boolean modality) {
        super(win, modality);
        myPuzzleWindow = win;
        init();
    }

    final void init() {

        setSize(120, 210);
        this.addWindowListener(new DialogWindowListener());
        GridBagLayout layout = new GridBagLayout();
        JRootPane pane = getRootPane();
        pane.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        int defaultWidth = constraints.gridwidth;
        int defaultHeight = constraints.gridheight;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;

        // add buttons
        for (int b = 0; b < 12; b++) {
            JButton button = new JButton();
            button.setBackground(PuzzleCell.FIELD_COLOR);
            if (b < 9) {
                myButtons[b] = button;
            }
            button.setFont(new Font(BUTTON_FONT, Font.PLAIN, 18));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setPreferredSize(new Dimension(25, 25));
            // set the constraints
            switch (b) {
                case 0:
                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("1");
                    button.setActionCommand("1");
                    button.addActionListener(myButtonListener);
                    break;
                case 1:
                    constraints.gridx = 1;
                    constraints.gridy = 0;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("2");
                    button.setActionCommand("2");
                    button.addActionListener(myButtonListener);
                    break;
                case 2:
                    constraints.gridx = 2;
                    constraints.gridy = 0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.gridheight = defaultHeight;
                    button.setText("3");
                    button.setActionCommand("3");
                    button.addActionListener(myButtonListener);
                    break;
                case 3:
                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("4");
                    button.setActionCommand("4");
                    button.addActionListener(myButtonListener);
                    break;
                case 4:
                    constraints.gridx = 1;
                    constraints.gridy = 1;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("5");
                    button.setActionCommand("5");
                    button.addActionListener(myButtonListener);
                    break;
                case 5:
                    constraints.gridx = 2;
                    constraints.gridy = 1;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.gridheight = defaultHeight;
                    button.setText("6");
                    button.setActionCommand("6");
                    button.addActionListener(myButtonListener);
                    break;
                case 6:
                    constraints.gridx = 0;
                    constraints.gridy = 2;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("7");
                    button.setActionCommand("7");
                    button.addActionListener(myButtonListener);
                    break;
                case 7:
                    constraints.gridx = 1;
                    constraints.gridy = 2;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("8");
                    button.setActionCommand("8");
                    button.addActionListener(myButtonListener);
                    break;
                case 8:
                    constraints.gridx = 2;
                    constraints.gridy = 2;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.gridheight = defaultHeight;
                    button.setText("9");
                    button.setActionCommand("9");
                    button.addActionListener(myButtonListener);
                    break;
                case 9:
                    constraints.gridx = 0;
                    constraints.gridy = 3;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    JButton setButton = button;
                    setButton.setBackground(PuzzleCell.BUTTON_COLOR);
                    setButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setResult();
                        }

                    });
                    setButton.setText("set");
                    setButton.setBorder(BorderFactory.createCompoundBorder( 
                            BorderFactory.createBevelBorder( 
                                    BevelBorder.RAISED, 
                                    PuzzleCell.FIELD_COLOR, 
                                    PuzzleCell.FIELD_COLOR), 
                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
                    setButton.setFont(new Font(BUTTON_FONT, Font.BOLD, 14));
                    break;
                case 10:
                    constraints.gridx = 1;
                    constraints.gridy = 3;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    JButton tryButton = button;
                    tryButton.setBackground(PuzzleCell.BUTTON_COLOR);
                    tryButton.setText("try");
                    tryButton.setBorder(BorderFactory.createCompoundBorder( 
                            BorderFactory.createBevelBorder( 
                                    BevelBorder.RAISED, 
                                    PuzzleCell.FIELD_COLOR, 
                                    PuzzleCell.FIELD_COLOR), 
                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
                    tryButton.setFont(new Font(BUTTON_FONT, Font.BOLD, 14));
                    tryButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            tryResult();
                        }

                    });
                    break;
                case 11:
                    constraints.gridx = 2;
                    constraints.gridy = 3;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.gridheight = defaultHeight;
                    JButton clearButton = button;
                    clearButton.setBackground(PuzzleCell.BUTTON_COLOR);
                    clearButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            clearResult();
                        }

                    });
                    clearButton.setText("clr");
                    clearButton.setBorder(BorderFactory.createCompoundBorder( 
                            BorderFactory.createBevelBorder( 
                                    BevelBorder.RAISED, 
                                    PuzzleCell.FIELD_COLOR, 
                                    PuzzleCell.FIELD_COLOR), 
                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
                    clearButton.setFont(new Font(BUTTON_FONT, Font.BOLD, 14));
                    break;
                default:
                    break;
            }
            layout.setConstraints(button, constraints);
            pane.add(button);
        }

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        JTextArea hintTxt = new JTextArea("Hint");
        hintTxt.setEditable(false);
        hintTxt.setColumns(2);
        Color bg = this.getBackground();
        hintTxt.setBackground(bg);
        pane.add(hintTxt);

        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        JCheckBox hint = new JCheckBox("");
        hint.setSelected(myHint);
        hint.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                myHint = (e.getStateChange() == ItemEvent.SELECTED);
                if (myHint) {
                    showHint();
                } else {
                    for (int b = 0; b < 9; b++) {
                        myButtons[b].setBackground(PuzzleCell.FIELD_COLOR);
                    }
                }
            }
        });
        pane.add(hint);
        showHint();
    }

    void showHint() {
        if (myHint) {
            myPuzzleWindow.computePossibleValues();
            PuzzleCell cell = myPuzzleWindow.getSelectedCell();
            if (cell == null) {
                return;
            }
            for (int v = 0; v < 9; v++) {
                myButtons[v].setBackground(PuzzleCell.ERROR_COLOR);
            }
            if (cell.getType() != PuzzleCell.VALUE_TYPE_AVAILABLE) {
                return;
            }
            for (Integer value : cell.computePossibleValues()) {
                myButtons[value - 1].setBackground(PuzzleCell.FIELD_COLOR);
                if(mySelectedButtonNo == PuzzleCell.UNDEFINED) {
                    myButtons[value - 1].requestFocus();
                    myButtons[value - 1].doClick();
                }
            }
        }
    }

    void selectButton(int no) {

        if (mySelectedButtonNo != PuzzleCell.UNDEFINED) {
            myButtons[mySelectedButtonNo].setBackground(PuzzleCell.FIELD_COLOR);
            if (mySelectedButtonNo == no) {
                return;
            }
        }
        myButtons[no].setBackground(PuzzleCell.HIGHLIGHT_COLOR);
        mySelectedButtonNo = no;
        showHint();
    }

    void setResult() {
        if (mySelectedButtonNo != PuzzleCell.UNDEFINED) {
            int result = myPuzzleWindow.updateCell(
                    myPuzzleWindow.getSelectedCell(),
                    mySelectedButtonNo + 1,
                    PuzzleCell.VALUE_TYPE_SET);
            switch (result) {
                case ROW_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Row Conflict", ERROR_INVALID_CHOICE, JOptionPane.ERROR_MESSAGE);
                    myPuzzleWindow.clearHighlights();
                    return;
                case COL_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Column Conflict", ERROR_INVALID_CHOICE, JOptionPane.ERROR_MESSAGE);
                    myPuzzleWindow.clearHighlights();
                    return;
                case SQ_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Square Conflict", ERROR_INVALID_CHOICE, JOptionPane.ERROR_MESSAGE);
                    myPuzzleWindow.clearHighlights();
                    return;
                default:
                    break;
            }
            myButtons[mySelectedButtonNo].setBackground(PuzzleCell.FIELD_COLOR);
        }
    }

    void tryResult() {
        if (mySelectedButtonNo != PuzzleCell.UNDEFINED) {
            int result = myPuzzleWindow.updateCell(
                    myPuzzleWindow.getSelectedCell(),
                    mySelectedButtonNo + 1,
                    PuzzleCell.VALUE_TYPE_TRY);
            switch (result) {
                case ROW_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Row Conflict", ERROR_INVALID_CHOICE, JOptionPane.ERROR_MESSAGE);
                    myPuzzleWindow.clearHighlights();
                    return;
                case COL_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Column Conflict", ERROR_INVALID_CHOICE, JOptionPane.ERROR_MESSAGE);
                    myPuzzleWindow.clearHighlights();
                    return;
                case SQ_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Square Conflict", ERROR_INVALID_CHOICE, JOptionPane.ERROR_MESSAGE);
                    myPuzzleWindow.clearHighlights();
                    return;
                default:
                    break;
            }
            myButtons[mySelectedButtonNo].setBackground(PuzzleCell.FIELD_COLOR);
            if (myPuzzleWindow.getAvailableCount() == 0) {
                // puzzle solved!
                JOptionPane.showMessageDialog(null,
                        "The puzzle has been solved.",
                        "Puzzle Solved",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        mySelectedButtonNo = PuzzleCell.UNDEFINED;
    }

    void clearResult() {
        if(mySelectedButtonNo < 0 || mySelectedButtonNo > 8) {
            return;
        }
        myPuzzleWindow.updateCell(myPuzzleWindow.getSelectedCell(),
                mySelectedButtonNo + 1,
                PuzzleCell.VALUE_TYPE_UPDATE_CLR);
                myButtons[mySelectedButtonNo].setBackground(PuzzleCell.FIELD_COLOR);
    }

    class DialogButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int no = Integer.parseInt(e.getActionCommand()) - 1;
            selectButton(no);
        }

    }

    class DialogWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            // do nothing
        }

        @Override
        public void windowClosing(WindowEvent e) {
            myPuzzleWindow.closeNumberDialog();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            // do nothing
            myPuzzleWindow.closeNumberDialog();
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

    }

    void reset() {
        if (!myHint) {
            mySelectedButtonNo = PuzzleCell.UNDEFINED;
        }
    }

    /* properties */
    // selection errors
    public static final int SELECTION_OK = 0;
    public static final int ROW_CONFLICT = 1;
    public static final int COL_CONFLICT = 2;
    public static final int SQ_CONFLICT = 3;
    public static final int SET_CONFLICT = 4;
    public static final int COL_FORCED = 5;
    public static final int ROW_FORCED = 6;
    public static final int SELECTION_INVALID = 8;
    public static final int ASSIGNED_CONFLICT = 9;
    
    public static final String BUTTON_FONT = "Arial";
    public static final String ERROR_INVALID_CHOICE = "Invalid Choice";

    private final JButton[] myButtons = new JButton[9];
    private int mySelectedButtonNo = PuzzleCell.UNDEFINED;
    private final PuzzleWindow myPuzzleWindow;
    private final transient ActionListener myButtonListener = new DialogButtonListener();
    private static boolean myHint = false;
    
    private static final long serialVersionUID = 101;
}
