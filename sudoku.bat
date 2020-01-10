REM
REM sudokuUnlocked shell script
REM this script assumes the sudokuUnlocked directory is in
REM the user's home directory. If it is in another location,
REM change the ${HOME} variable in the script to the absolute
REM path of the installed directory
REM
REM place this shell script in a location
REM that is in the environmental PATH variable
REM or run the application from the SudokuUnlocked directory
REM
cd %HOMEPATH%/sudoku
java -jar %HOMEPATH%/sudoku/SudokuUnlocked.jar
