package net.mrcullen.sudokusolver;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;

public class SudokuGrid implements SudokuCellChangeListener {
	private ArrayList<SudokuCell> cells;
	private ArrayList<Set<Integer>> colValuesUsed;
	private ArrayList<Set<Integer>> rowValuesUsed;
	private JPanel gridPanel;
	
	private ArrayList<SudokuGridChangeListener> listeners;
	
	public SudokuGrid () {
		listeners = new ArrayList<SudokuGridChangeListener> ();
		
		colValuesUsed = new ArrayList<Set<Integer>> ();
		rowValuesUsed = new ArrayList<Set<Integer>> ();
		
		for (int index = 0; index < 3; index++) {
			colValuesUsed.add(new TreeSet<Integer>());
			rowValuesUsed.add(new TreeSet<Integer>());
		}
		
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
		
		if (source.isSolved()) {
			for (SudokuCell cell : cells) {
				if (cell != source) {
					cell.removePossibleValues(source.getPossibleValues());
				}
			}
			
			recalculateGrid();
			System.out.println(rowValuesUsed.toString());
			notifyGridChangeListeners();
		}
	}
	
	public Set<Integer> getUsedRowValues (int row) {
		return rowValuesUsed.get(row);
	}

	public Set<Integer> getUsedColumnValues (int col) {
		return colValuesUsed.get(col);
	}

	protected void recalculateGrid () {
		for (int index = 0; index < 3; index ++) {
			Set<Integer> row = calculateRowValues(index);
			Set<Integer> col = calculateColumnValues(index);
			
			for (int subindex = 0; subindex < 3; subindex++) {
				if (subindex == index) {
					continue;
				}
				
				this.removePossibilitiesFromColumn(subindex, col);
				this.removePossibilitiesFromRow(subindex, row);
			}
			
			rowValuesUsed.remove(index);
			rowValuesUsed.add(index, row);
			colValuesUsed.remove(index);
			colValuesUsed.add(index, col);
			
		}
	}
	
	protected Set<Integer> calculateRowValues (int row) {
		ArrayList<SudokuCell> currentRow = new ArrayList<SudokuCell>();
		
		currentRow.add(cells.get(row*3));
		currentRow.add(cells.get(1 + row*3));
		currentRow.add(cells.get(2 + row*3));
		
		Set<Integer> rowValues = calculateValues(currentRow);
		for (int index = 0; index < 9; index ++) {
			if (index / 3 == row) {
				continue;
			}
			
			cells.get(index).removePossibleValues(rowValues);
		}
		return rowValues;
	}
				
	protected Set<Integer> calculateValues (ArrayList<SudokuCell> cellList) {
		Set<Integer> result = new TreeSet<Integer> ();
		Set<Integer> rowSet = new TreeSet<Integer> ();
		
		for (SudokuCell cell : cellList) {
			if (cell.isSolved()) {
				result.addAll(cell.getPossibleValues());
			}
			
			rowSet.addAll(cell.getPossibleValues());
		}
		 
		if (rowSet.containsAll(result) && rowSet.size() == 3) {
			result.addAll(rowSet);
		}

		return result;
	}
	
	protected Set<Integer> calculateColumnValues (int col) {
		ArrayList<SudokuCell> currentCol = new ArrayList<SudokuCell>();
		
		currentCol.add(cells.get(col));
		currentCol.add(cells.get(col + 3));
		currentCol.add(cells.get(col + 6));
		
		Set<Integer> colValues = calculateValues(currentCol);
		for (int index = 0; index < 9; index ++) {
			if (index % 3 == col) {
				continue;
			}
			
			cells.get(index).removePossibleValues(colValues);
		}
		return colValues; 
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
	
	public void notifyGridChangeListeners () {
		for (SudokuGridChangeListener target : listeners) {
			target.gridChanged(this);
		}
	}
	
	public void removePossibilitiesFromColumn(int col,
			Set<Integer> valuesUsed) {
		for (int index = 0; index < 3; index ++) {
			SudokuCell cell = cells.get(col + (index*3));
			cell.removePossibleValues(valuesUsed);
		}		
	}

	public void removePossibilitiesFromRow(int row,
			Set<Integer> valuesUsed) {
		for (int index = 0; index < 3; index ++) {
			SudokuCell cell = cells.get(index + (row*3));
			cell.removePossibleValues(valuesUsed);
		}		
	}
}
