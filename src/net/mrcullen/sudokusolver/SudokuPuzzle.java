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
	public void gridColumnChanged(SudokuGrid source, int col,
			ArrayList<Integer> valuesUsed) {
		int index = grids.indexOf(source);
		if (index == -1) {
			return;
		}
		
		int gridCol = index % gridSize;
		
		for (index = 0; index < gridSize; index ++) {
			SudokuGrid grid = grids.get(gridCol + (index * gridSize));
			if (grid != source) {
				grid.removePossibilitiesFromColumn(col, valuesUsed);
			}
		}
	}

	@Override
	public void gridRowChanged(SudokuGrid source, int row,
			ArrayList<Integer> valuesUsed) {
		int index = grids.indexOf(source);
		if (index == -1) {
			return;
		}
		
		int gridRow = index / gridSize;
		
		for (index = 0; index < gridSize; index ++) {
			SudokuGrid grid = grids.get(index + (gridRow * gridSize));
			if (grid != source) {
				grid.removePossibilitiesFromRow(row, valuesUsed);
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
