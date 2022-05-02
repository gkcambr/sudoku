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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import static java.lang.Math.pow;
import java.util.LinkedList;
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
        myPuzzleWindow = win;
        myRow = null;
        myCol = null;
        mySquare = null;
    }

    PuzzleCell(
            int no,
            PuzzleRow row,
            PuzzleColumn col,
            PuzzleSquare square,
            PuzzleWindow win) {
        super();
        myNumber = no;
        myRow = row;
        myCol = col;
        mySquare = square;
        myPuzzleWindow = win;
        addListener();
    }

    void reset() {
        myValue = UNDEFINED;
        myType = VALUE_TYPE_AVAILABLE;
        setText("");
        setBackground(PuzzleCell.FIELD_COLOR);
    }

    final void addListener() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PuzzleCell cell = (PuzzleCell) e.getSource();
                myPuzzleWindow.selectCell(cell);
                myPuzzleWindow.openNumberDialog(cell);
            }

        });
    }
    
    void store(java.io.ObjectOutputStream out) throws IOException {
        writeObject(out);
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.write(RW_OFFSET + myNumber);
        out.write(RW_OFFSET + myValue);
        out.write(RW_OFFSET + myType);
    }

    int restoreAll(ObjectInputStream ois) {
        int ret = 0;

        try {
            readObject(ois);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PuzzleWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private int readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        int storedNumbers = 0;
        int number = 0;
        int value = 0;
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
                    myPuzzleWindow.setCell(number, value, type);
                    storedNumbers += 1;
                    cnt = 0;
                    break;
                default:
                    break;
            }
            rd = in.read();
        }
        return storedNumbers;
    }

    private void readObjectNoData()
            throws ObjectStreamException {
        // do nothing
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
        return myRow;
    }

    PuzzleColumn getColumn() {
        return myCol;
    }

    PuzzleSquare getSquare() {
        return mySquare;
    }
    
    void updateAvailableValues() {
            myRow.updateAvailableValues();
            myCol.updateAvailableValues();
            mySquare.updateAvailableValues();
    }

    void setValue(int value) {
        if (value > 0 && value < 10) {
            myValue = value;
            setText("" + value);
            updateAvailableValues();
        } else {
            setText("");
        }
        updateAvailableValues();
    }

    int getValue() {
        return myValue;
    }

    void setType(int type) {
        myType = type;
    }

    int getType() {
        return myType;
    }

    int getNumber() {
        return myNumber;
    }

    Integer[] computePossibleValues() {
        // pass by value
        Integer[] av = new Integer[0];

        updateAvailableValues();
        if(myType != VALUE_TYPE_AVAILABLE) {
            return av;
        }
        
        int cellAvs = myRow.getValues();
        cellAvs &= myCol.getValues();
        cellAvs &= mySquare.getValues();
        LinkedList<Integer> intAvs = new LinkedList<>();
        for(int bit = 0; bit < 9; bit++) {
            if(((int) pow(2,bit) & cellAvs) > 0) {
                intAvs.add(bit + 1);
            }
        }
        av = intAvs.toArray(new Integer[intAvs.size()]);
        return av;
    }

    boolean valueIsAvailable(Integer v) {
        boolean ret = false;

        Integer[] availables = computePossibleValues();
        for (Integer av : availables) {
            if (Objects.equals(av, v)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /* properties */
    public static final Color BUTTON_COLOR = new Color(173, 216, 230);
    public static final Color FIELD_COLOR = Color.WHITE;
    public static final Color HIGHLIGHT_COLOR = Color.YELLOW;
    public static final Color ERROR_COLOR = Color.RED;
    public static final Color HINT_COLOR = Color.RED;

    // button value types
    public static final int VALUE_TYPE_ALL = -1;
    public static final int VALUE_TYPE_AVAILABLE = 0;
    public static final int VALUE_TYPE_SET = 1;
    public static final int VALUE_TYPE_TRY = 2;
    public static final int VALUE_TYPE_EXCLUDED = 3;
    public static final int VALUE_TYPE_AUTO = 4;
    public static final int VALUE_TYPE_UPDATE_CLR = 5;
    public static final int UNDEFINED = -1;

    private final PuzzleRow myRow;
    private final PuzzleColumn myCol;
    private final PuzzleSquare mySquare;
    private final PuzzleWindow myPuzzleWindow;
    private int myNumber;
    private int myValue = UNDEFINED;
    private int myType = VALUE_TYPE_AVAILABLE;
    private static final int RW_OFFSET = 10;
    
    private static final long serialVersionUID = 101;
}
