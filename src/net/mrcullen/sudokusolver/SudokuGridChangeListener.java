package net.mrcullen.sudokusolver;

import java.util.ArrayList;

public interface SudokuGridChangeListener {
	
	public abstract void gridColumnChanged(SudokuGrid source, int col, ArrayList<Integer> valuesUsed);
	public abstract void gridRowChanged(SudokuGrid source, int row, ArrayList<Integer> valuesUsed);

}
