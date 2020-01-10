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
        _win = win;
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
                _buttons[b] = button;
            }
            button.setFont(new Font("Arial", Font.PLAIN, 18));
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
                    button.addActionListener(_buttonListener);
                    break;
                case 1:
                    constraints.gridx = 1;
                    constraints.gridy = 0;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("2");
                    button.setActionCommand("2");
                    button.addActionListener(_buttonListener);
                    break;
                case 2:
                    constraints.gridx = 2;
                    constraints.gridy = 0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.gridheight = defaultHeight;
                    button.setText("3");
                    button.setActionCommand("3");
                    button.addActionListener(_buttonListener);
                    break;
                case 3:
                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("4");
                    button.setActionCommand("4");
                    button.addActionListener(_buttonListener);
                    break;
                case 4:
                    constraints.gridx = 1;
                    constraints.gridy = 1;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("5");
                    button.setActionCommand("5");
                    button.addActionListener(_buttonListener);
                    break;
                case 5:
                    constraints.gridx = 2;
                    constraints.gridy = 1;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.gridheight = defaultHeight;
                    button.setText("6");
                    button.setActionCommand("6");
                    button.addActionListener(_buttonListener);
                    break;
                case 6:
                    constraints.gridx = 0;
                    constraints.gridy = 2;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("7");
                    button.setActionCommand("7");
                    button.addActionListener(_buttonListener);
                    break;
                case 7:
                    constraints.gridx = 1;
                    constraints.gridy = 2;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    button.setText("8");
                    button.setActionCommand("8");
                    button.addActionListener(_buttonListener);
                    break;
                case 8:
                    constraints.gridx = 2;
                    constraints.gridy = 2;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.gridheight = defaultHeight;
                    button.setText("9");
                    button.setActionCommand("9");
                    button.addActionListener(_buttonListener);
                    break;
                case 9:
                    constraints.gridx = 0;
                    constraints.gridy = 3;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    _setButton = button;
                    _setButton.setBackground(PuzzleCell.BUTTON_COLOR);
                    _setButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setResult();
                        }

                    });
                    _setButton.setText("set");
                    _setButton.setBorder(BorderFactory.createCompoundBorder( 
                            BorderFactory.createBevelBorder( 
                                    BevelBorder.RAISED, 
                                    PuzzleCell.FIELD_COLOR, 
                                    PuzzleCell.FIELD_COLOR), 
                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
                    _setButton.setFont(new Font("Arial", Font.BOLD, 14));
                    break;
                case 10:
                    constraints.gridx = 1;
                    constraints.gridy = 3;
                    constraints.gridwidth = defaultWidth;
                    constraints.gridheight = defaultHeight;
                    _tryButton = button;
                    _tryButton.setBackground(PuzzleCell.BUTTON_COLOR);
                    _tryButton.setText("try");
                    _tryButton.setBorder(BorderFactory.createCompoundBorder( 
                            BorderFactory.createBevelBorder( 
                                    BevelBorder.RAISED, 
                                    PuzzleCell.FIELD_COLOR, 
                                    PuzzleCell.FIELD_COLOR), 
                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
                    _tryButton.setFont(new Font("Arial", Font.BOLD, 14));
                    _tryButton.addActionListener(new ActionListener() {
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
                    _clearButton = button;
                    _clearButton.setBackground(PuzzleCell.BUTTON_COLOR);
                    _clearButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            clearResult();
                        }

                    });
                    _clearButton.setText("clr");
                    _clearButton.setBorder(BorderFactory.createCompoundBorder( 
                            BorderFactory.createBevelBorder( 
                                    BevelBorder.RAISED, 
                                    PuzzleCell.FIELD_COLOR, 
                                    PuzzleCell.FIELD_COLOR), 
                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
                    _clearButton.setFont(new Font("Arial", Font.BOLD, 14));
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
        hint.setSelected(_hint);
        hint.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                _hint = (e.getStateChange() == ItemEvent.SELECTED);
                if (_hint == true) {
                    showHint();
                } else {
                    for (int b = 0; b < 9; b++) {
                        _buttons[b].setBackground(PuzzleCell.FIELD_COLOR);
                    }
                }
            }
        });
        pane.add(hint);
        showHint();
    }

    void showHint() {
        if (_hint) {
            _win.computePossibleValues();
            PuzzleCell cell = _win.getSelectedCell();
            if (cell == null) {
                return;
            }
            for (int v = 0; v < 9; v++) {
                _buttons[v].setBackground(PuzzleCell.ERROR_COLOR);
            }
            if (cell.getType() != PuzzleCell.VALUE_TYPE_AVAILABLE) {
                return;
            }
            for (Integer value : cell.getPossibleValues()) {
                _buttons[value].setBackground(PuzzleCell.FIELD_COLOR);
            }
            if (cell.getPossibleValues().length == 1) {
                Integer possible = cell.getPossibleValues()[0];
                _buttons[possible].setBackground(PuzzleCell.HIGHLIGHT_COLOR);
                this._selectedButtonNo = possible;
            }
        }
    }

    void selectButton(int no) {

        if (_selectedButtonNo != PuzzleCell.UNDEFINED) {
            _buttons[_selectedButtonNo].setBackground(PuzzleCell.FIELD_COLOR);
            if (_selectedButtonNo == no) {
                _selectedButtonNo = PuzzleCell.UNDEFINED;
                return;
            }
        }
        _buttons[no].setBackground(PuzzleCell.HIGHLIGHT_COLOR);
        _selectedButtonNo = no;
        showHint();
    }

    void setResult() {
        if (_selectedButtonNo != PuzzleCell.UNDEFINED) {
            int result = _win.updateCell(
                    _win.getSelectedCell(),
                    _selectedButtonNo + 1,
                    PuzzleCell.VALUE_TYPE_SET);
            switch (result) {
                case ROW_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Row Conflict", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                    _win.clearHighlights();
                    return;
                case COL_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Column Conflict", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                    _win.clearHighlights();
                    return;
                case SQ_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Square Conflict", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                    _win.clearHighlights();
                    return;
                default:
                    break;
            }
            _buttons[_selectedButtonNo].setBackground(PuzzleCell.FIELD_COLOR);
        }
    }

    void tryResult() {
        if (_selectedButtonNo != PuzzleCell.UNDEFINED) {
            int result = _win.updateCell(
                    _win.getSelectedCell(),
                    _selectedButtonNo + 1,
                    PuzzleCell.VALUE_TYPE_TRY);
            switch (result) {
                case ROW_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Row Conflict", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                    _win.clearHighlights();
                    return;
                case COL_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Column Conflict", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                    _win.clearHighlights();
                    return;
                case SQ_CONFLICT:
                    JOptionPane.showMessageDialog(null, "Square Conflict", "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                    _win.clearHighlights();
                    return;
                default:
                    break;
            }
            _buttons[_selectedButtonNo].setBackground(PuzzleCell.FIELD_COLOR);
            if (_win.getAvailableCount() == 0) {
                // puzzle solved!
                JOptionPane.showMessageDialog(null,
                        "The puzzle has been solved.",
                        "Puzzle Solved",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    void clearResult() {
        _win.updateCell(_win.getSelectedCell(),
                _selectedButtonNo + 1,
                PuzzleCell.VALUE_TYPE_UPDATE_CLR);
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
            _win.closeNumberDialog();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            // do nothing
            _win.closeNumberDialog();
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
        if (_hint == false) {
            _selectedButtonNo = PuzzleCell.UNDEFINED;
        }
    }

    /* properties */
    // selection errors
    public final static int SELECTION_OK = 0;
    public final static int ROW_CONFLICT = 1;
    public final static int COL_CONFLICT = 2;
    public final static int SQ_CONFLICT = 3;
    public final static int SET_CONFLICT = 4;
    public final static int COL_FORCED = 5;
    public final static int ROW_FORCED = 6;
    public final static int SELECTION_INVALID = 8;
    public final static int ASSIGNED_CONFLICT = 9;

    private final JButton[] _buttons = new JButton[9];
    private JButton _setButton;
    private JButton _tryButton;
    private JButton _clearButton;
    private int _selectedButtonNo = PuzzleCell.UNDEFINED;
    private final PuzzleWindow _win;
    private final ActionListener _buttonListener = new DialogButtonListener();
    private static boolean _hint = false;
    
    private static final long serialVersionUID = 101;
}
