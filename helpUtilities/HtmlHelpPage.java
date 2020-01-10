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
package helpUtilities;

/* HtmlHelpPage.java needs no other files. */
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import sudoku.PuzzleWindow;

public class HtmlHelpPage {

    public HtmlHelpPage(PuzzleWindow win) {
        init();
    }

    final void init() {

        // load the html help file
        String workingDir = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();

        int indx = workingDir.indexOf("/");
        workingDir = "/" + workingDir.substring(indx + 1);
        indx = workingDir.lastIndexOf("/");
        if(indx > 0) {
            workingDir = workingDir.substring(0, indx);
        }
        String fileName = workingDir + "/"
                + "docs" + "/" + "sudoku.html";
        File htmlFile = new File(fileName);
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "This desktop may not support java browsing.\n" +
                    "Go to http://radiocheck.us/sudoku/sudoku.html",
                    "Help failure",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /* properties */
    private static final long serialVersionUID = 101;

}
