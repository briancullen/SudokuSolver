package net.mrcullen.sudokusolver;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class SudokuPuzzle implements SudokuGridChangeListener {
	private ArrayList<SudokuGrid> grids;
	private int gridSize;
	private JPanel puzzlePanel;
	
	public SudokuPuzzle (int size) {
		if (size < 3) {
			size = 3;
		}
		
		gridSize = size;
		grids = new ArrayList<SudokuGrid>();
		
		puzzlePanel = new JPanel ();
		puzzlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		puzzlePanel.setLayout(new GridLayout(size, size, 8, 8));

		for (int index = 0; index < gridSize*gridSize; index++) {
			SudokuGrid grid = new SudokuGrid();
			grid.addGridChangeListener(this);
			puzzlePanel.add(grid.getGridPanel());
			grids.add(grid);
		}
	}
	
	public JPanel getPuzzlePanel () {
		return puzzlePanel;
	}

	@Override
	public void gridChanged(SudokuGrid source) {
		int index = grids.indexOf(source);
		if (index == -1) {
			return;
		}

		int gridRow = index / gridSize;
		int gridCol = index % gridSize;
		
		// Do the columns
		for (index = 0; index < gridSize; index ++) {
			SudokuGrid grid = grids.get(gridCol + (index * gridSize));
			if (grid != source) {
				for (int col = 0; col < 3; col ++) {
					grid.removePossibilitiesFromColumn(col, source.getUsedColumnValues(col));
				}
			}
		}

		// Do the rows
		for (index = 0; index < gridSize; index ++) {
			SudokuGrid grid = grids.get(index + (gridRow * gridSize));
			if (grid != source) {
				for (int row = 0; row < 3; row ++) {
					grid.removePossibilitiesFromRow(row, source.getUsedRowValues(row));
				}
			}
		}
	}

	public boolean isSolved () {
		for (SudokuGrid grid : grids) {
			if (!grid.isSolved()) {
				return false;
			}
		}
		return true;
	}

}
