package net.mrcullen.sudokusolver;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SudokuGrid implements SudokuCellChangeListener {
	private ArrayList<SudokuCell> cells;
	private JPanel gridPanel;
	
	private ArrayList<SudokuGridChangeListener> listeners;
	
	public SudokuGrid () {
		listeners = new ArrayList<SudokuGridChangeListener> ();
		
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(3, 3, 3, 3));
		
		cells = new ArrayList<SudokuCell>();
		
		for (int index = 0; index < 9; index++) {
			SudokuCell cell = new SudokuCell();
			cell.addCellChangeListener(this);
			gridPanel.add(cell.getTextField());
			cells.add(cell);
		}
	}
	
	public JPanel getGridPanel () {
		return gridPanel;
	}

	@Override
	public void cellChanged(SudokuCell source) {
		int index = cells.indexOf(source);
		if (index == -1) {
			return;
		}
		
		int changedRow = index / 3;
		int changedCol = index % 3;	
		if (source.isSolved()) {
			for (SudokuCell cell : cells) {
				if (cell != source) {
					cell.removePossibleValues(source.getPossibleValues());
				}
			}
			
			notifyColumnListeners(changedCol, calculateColumnValues(changedCol));
			notifyRowListeners(changedRow, calculateRowValues(changedRow));
		}
	}
	
	protected ArrayList<Integer> calculateRowValues (int row) {
		ArrayList<Integer> result = new ArrayList<Integer> ();
		ArrayList<Integer> possibilities = new ArrayList<Integer>();
		
		for (int index = 0; index < 3; index ++) {
			SudokuCell cell = cells.get(index + (3*row));
			if(cell.isSolved()){
				result.addAll(cell.getPossibleValues());
			}
			else {
				possibilities.addAll(cell.getPossibleValues());
			}
		}
		
		// Tried the catch when we know the number go in the row
		// but not sure of the order yet.
		if (possibilities.size() == 2) {
			Integer possibility1 = possibilities.get(0);
			Integer possibility2 = possibilities.get(1);
			for (int index = 0; index < cells.size(); index++) {
				// Don't check the row we have just done.
				if (index / 3 == row) {
					continue;
				}
				
				SudokuCell cell = cells.get(index);
				if (cell.getPossibleValues().contains(possibility1)
						|| cell.getPossibleValues().contains(possibility2)) {
					possibilities.clear();
					break;
				}
				
			}
			result.addAll(possibilities);
		}

		return result;
	}
	
	protected ArrayList<Integer> calculateColumnValues (int col) {
		ArrayList<Integer> result = new ArrayList<Integer> ();
		ArrayList<Integer> possibilities = new ArrayList<Integer>();
		
		for (int index = 0; index < 3; index ++) {
			SudokuCell cell = cells.get(col + (index*3));
			if(cell.isSolved()){
				result.addAll(cell.getPossibleValues());
			}
			else {
				possibilities.addAll(cell.getPossibleValues());
			}
		}
		
		// Tried the catch when we know the number go in the row
		// but not sure of the order yet.
		if (possibilities.size() == 2) {
			Integer possibility1 = possibilities.get(0);
			Integer possibility2 = possibilities.get(1);
			for (int index = 0; index < cells.size(); index++) {
				// Don't check the col we have just done.
				if (index % 3 == col) {
					continue;
				}
				
				SudokuCell cell = cells.get(index);
				if (cell.getPossibleValues().contains(possibility1)
						|| cell.getPossibleValues().contains(possibility2)) {
					possibilities.clear();
					break;
				}
				
			}
			result.addAll(possibilities);
		}

		return result;
	}
	
	public boolean isSolved () {
		for (SudokuCell cell : cells) {
			if (!cell.isSolved()) {
				return false;
			}
		}
		return true;
	}
	
	public void addGridChangeListener(SudokuGridChangeListener target) {
		listeners.add(target);
	}
	
	public void notifyColumnListeners (int col, ArrayList<Integer> valuesUsed) {
		for (SudokuGridChangeListener target : listeners) {
			target.gridColumnChanged(this, col, valuesUsed);
		}
	}
	
	public void notifyRowListeners (int row, ArrayList<Integer> valuesUsed) {
		for (SudokuGridChangeListener target : listeners) {
			target.gridRowChanged(this, row, valuesUsed);
		}
	}

	public void removePossibilitiesFromColumn(int col,
			ArrayList<Integer> valuesUsed) {
		for (int index = 0; index < 3; index ++) {
			SudokuCell cell = cells.get(col + (index*3));
			cell.removePossibleValues(valuesUsed);
		}		
	}

	public void removePossibilitiesFromRow(int row,
			ArrayList<Integer> valuesUsed) {
		for (int index = 0; index < 3; index ++) {
			SudokuCell cell = cells.get(index + (row*3));
			cell.removePossibleValues(valuesUsed);
		}		
	}
}
