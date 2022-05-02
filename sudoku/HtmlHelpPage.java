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

/* HtmlHelpPage.java needs no other files. */
import javax.swing.JOptionPane;

public class HtmlHelpPage {

    public HtmlHelpPage() {
        init();
    }

    final void init() {

        JOptionPane.showMessageDialog(null,
                "Go to <html>http://radiocheck.us/sudoku/helpFile.html</html>",
                "Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /* properties */
    private static final long serialVersionUID = 101;

}
