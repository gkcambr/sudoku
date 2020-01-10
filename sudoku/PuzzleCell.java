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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author keithc
 */
public class PuzzleCell extends JButton implements Comparable<PuzzleCell>, Serializable {

    PuzzleCell(PuzzleWindow win) {
        // only used for reader
        super();
        _win = win;
        _row = null;
        _col = null;
        _square = null;
    }

    PuzzleCell(
            int no,
            PuzzleRow row,
            PuzzleColumn col,
            PuzzleSquare square,
            PuzzleWindow win) {
        super();
        _number = no;
        _row = row;
        _col = col;
        _square = square;
        _win = win;
        addListener();
    }

    void resetPossibles() {
        if (_type == VALUE_TYPE_AVAILABLE) {
            for (int p = 0; p < 9; p++) {
                _possibleValues[p] = VALUE_TYPE_AVAILABLE;
            }
        }
    }

    void reset() {
        _value = UNDEFINED;
        _type = VALUE_TYPE_AVAILABLE;
        resetPossibles();
        setText("");
        setBackground(PuzzleCell.FIELD_COLOR);
    }

    final void addListener() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PuzzleCell cell = (PuzzleCell) e.getSource();
                _win.selectCell(cell);
                _win.openNumberDialog(cell);
            }

        });
        resetPossibles();
    }

    void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.write(RW_OFFSET + _number);
        out.write(RW_OFFSET + _value);
        out.write(RW_OFFSET + _type);
    }

    int restoreAll(String readFile, ObjectInputStream ois) {
        int ret = 0;

        try {
            readObject(ois);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PuzzleCell.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    int readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        int storedNumbers = 0;
        int number = 0, value = 0;
        int rd = in.read();
        int cnt = 0;
        while (rd > 0) {
            rd -= RW_OFFSET;
            switch (cnt++) {
                case 0:
                    number = rd;
                    break;
                case 1:
                    value = rd;
                    break;
                case 2:
                    int type = rd;
                    _win.setCell(number, value, type);
                    storedNumbers += 1;
                    cnt = 0;
                    break;
            }
            rd = in.read();
        }
        return storedNumbers;
    }

    void readObjectNoData()
            throws ObjectStreamException {

    }

    @Override
    public int compareTo(PuzzleCell c) {
        int ret = -1;

        if (this.hashCode() == c.hashCode()) {
            ret = 0;
        }
        return ret;
    }

    void showError(boolean er) {
        if (er) {
            setBackground(PuzzleCell.ERROR_COLOR);
        } else {
            setBackground(PuzzleCell.FIELD_COLOR);
        }
    }

    void highlight(boolean hi) {
        if (hi) {
            setBackground(PuzzleCell.HIGHLIGHT_COLOR);
        } else {
            setBackground(PuzzleCell.FIELD_COLOR);
        }
    }
    
    void markAsHint(boolean hint) {
        if(hint) {
            setText("?");
            setForeground(PuzzleCell.HINT_COLOR);
        }
        else {
            setText("");
        }
    }

    PuzzleRow getRow() {
        return _row;
    }

    PuzzleColumn getColumn() {
        return _col;
    }

    PuzzleSquare getSquare() {
        return _square;
    }

    void setValue(int value) {
        if (value > 0 && value < 10) {
            _value = value;
            _possibleValues[value - 1] = NumberDialog.ASSIGNED_CONFLICT;
            setText("" + value);
        } else {
            setText("");
        }
    }

    int getValue() {
        return _value;
    }

    void setType(int type) {
        _type = type;
    }

    int getType() {
        return _type;
    }

    int getNumber() {
        return _number;
    }
    
    void setPossibleValue(Integer member, Integer value) {
       if(member >= 0 && member < 9) {
           _possibleValues[member] = value;
       }
    }
    
    boolean arePossiblesIdentical(PuzzleCell otherCell) {
        boolean ret = true;
        
        int noPossibles = _possibleValues.length;
        int othersPossibles = otherCell.getPossibleValues().length;
        if(noPossibles != othersPossibles) {
            ret = false;
        }
        else {
            for(Integer v : otherCell.getPossibleValues()) {
                if(!Objects.equals(_possibleValues[v], v)) {
                    ret = false;
                    break;
                }
            }
        }
        
        return ret;
    }

    Integer[] getPossibleValues() {
        // pass by value
        Integer[] av = new Integer[0];

        if(_type != VALUE_TYPE_AVAILABLE) {
            return av;
        }
        
        int availCnt = 0;
        Integer[] availableValues = new Integer[9];
        for (int v = 0; v < 9; v++) {
            if (_possibleValues[v] == PuzzleCell.VALUE_TYPE_AVAILABLE) {
                availableValues[availCnt++] = v;
            }
        }
        av = new Integer[availCnt];
        System.arraycopy(availableValues, 0, av, 0, availCnt);

        return av;
    }

    boolean ValueIsAvailable(Integer v) {
        boolean ret = false;

        Integer[] availables = getPossibleValues();
        for (Integer av : availables) {
            if (Objects.equals(av, v)) {
                ret = true;
                break;
            }
        }
        return ret;
    }
    
    boolean availablesMatch(PuzzleCell cell) {
        // the two lists must be identical for this
        // method to return true;
        boolean ret = true;
        
        if(cell == null) {
            return false;
        }
        
        if (cell.getPossibleValues().length != getPossibleValues().length) {
            return false;
        }
        
        for(Integer value : getPossibleValues()) {
            if(cell.ValueIsAvailable(value) == false) {
                return false;
            }
        }
        
        for(Integer value : cell.getPossibleValues()) {
            if(ValueIsAvailable(value) == false) {
                return false;
            }
        }
        
        return ret;
    }

    /* properties */
    public final static Color BUTTON_COLOR = new Color(173, 216, 230);
    public final static Color FIELD_COLOR = Color.WHITE;
    public final static Color HIGHLIGHT_COLOR = Color.YELLOW;
    public final static Color ERROR_COLOR = Color.RED;
    public final static Color HINT_COLOR = Color.RED;

    // button value types
    public final static int VALUE_TYPE_ALL = -1;
    public final static int VALUE_TYPE_AVAILABLE = 0;
    public final static int VALUE_TYPE_SET = 1;
    public final static int VALUE_TYPE_TRY = 2;
    public final static int VALUE_TYPE_EXCLUDED = 3;
    public final static int VALUE_TYPE_AUTO = 4;
    public final static int VALUE_TYPE_UPDATE_CLR = 5;
    public final static int UNDEFINED = -1;

    private final PuzzleRow _row;
    private final PuzzleColumn _col;
    private final PuzzleSquare _square;
    private final PuzzleWindow _win;
    private int _number;
    private int _value = UNDEFINED;
    private int _type = VALUE_TYPE_AVAILABLE;
    private final Integer[] _possibleValues = new Integer[9];
    private final int RW_OFFSET = 10;
    
    private static final long serialVersionUID = 101;
}
