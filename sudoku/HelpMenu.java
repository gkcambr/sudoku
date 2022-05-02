/*
 * Copyright (C) 2020 keithc
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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.swingplus.JHyperlink;

/**
 *
 * @author keithc
 */
public class HelpMenu extends sudoku.Menu {

    public HelpMenu(PuzzleWindow listerWindow) {
        super(listerWindow);
        createMenu();
    }

    @Override
    public void updateEnabledMenus(boolean opened) {
        // do nothing
    }

    @Override
    public final void createMenu() {

        setMnemonic(KeyEvent.VK_A);
        setText("Help");

        // create a clear puzzle item
        myHelpItem = new JMenuItem("Help", null);
        myHelpItem.setMnemonic(KeyEvent.VK_H);
        myHelpItem.setToolTipText("Puzzle tutorial");
        myHelpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String helpURI = "http://radiocheck.us/sudoku/helpFile.html";
                try {
                    Desktop.getDesktop().browse(
                            new URI(helpURI));
                } catch (IOException | URISyntaxException e) {
                    // browser doesn't support getDesktop
                    JHyperlink stackOverflow = new JHyperlink(helpURI, helpURI);

                    JComponent[] messageComponents;
                    messageComponents = new JComponent[]{stackOverflow};

                    JOptionPane.showMessageDialog(null,
                            messageComponents,
                            "Help",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        add(myHelpItem);
        myHelpItem.setEnabled(true);

        // create a clear tries item
        myAboutItem = new JMenuItem("About", null);
        myAboutItem.setMnemonic(KeyEvent.VK_T);
        myAboutItem.setToolTipText("Version information");
        myAboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,
                        sudoku.SudokuUnlocked.VERSION + "\n"
                        + sudoku.SudokuUnlocked.VERSION_DATE,
                        "About",
                        JOptionPane.OK_OPTION);
            }
        });
        add(myAboutItem);
        myAboutItem.setEnabled(true);
    }

    /* properties */
    private JMenuItem myHelpItem;
    private JMenuItem myAboutItem;
    private static final long serialVersionUID = 101;
}
