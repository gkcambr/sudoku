#
# sudokuUnlocked shell script
# this script assumes the sudokuUnlocked directory is in
# the user's home directory. If it is in another location,
# change the ${HOME} variable in the script to the absolute
# path of the installed directory
#
# place this shell script in a location, such as /usr/local/bin
# that is in the environmental PATH variable
#
cd ${HOME}/sudoku
java -jar ${HOME}/sudoku/sudoku.jar
