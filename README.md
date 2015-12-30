# Sudoku Solver

As an experiment this is a standalone Swing based Java application that will give you a standard sized sudoku grid and as you fill things in it will try and figure out the blank boxes for you. Any obvious mistakes will be highlighted in red and the program will basically ignore them until they are fixed.

It has been tested on easy puzzles and it does work but it will struggle with more difficult grids. The possible values for each square are shown in a tool tip so if the program can't solve it for you then look at the tool tips (by leaving the mouse over a box) to see what the possible values for that area are.

## Running the Code

At the moment the repository is setup to run in eclipse and will probably work without any difficulty if you try it that way. However if you want to run it some other way the class you need to execute is `net.mrcullen.sudokusolver.SudokuSolver`. Apart from the other classes provided in the same package all of the dependencies are part of the standard java distribution.
