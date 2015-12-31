package net.mrcullen.sudokusolver;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

public class SudokuCell implements PropertyChangeListener {

	private TreeSet<Integer> possibleValues;
	private JFormattedTextField textField;
	private ArrayList<SudokuCellChangeListener> listeners;
	
	public SudokuCell () {
		listeners = new ArrayList<SudokuCellChangeListener>();
		
		possibleValues = new TreeSet<Integer>();
		possibleValues.add(Integer.valueOf(1));
		possibleValues.add(Integer.valueOf(2));
		possibleValues.add(Integer.valueOf(3));
		possibleValues.add(Integer.valueOf(4));
		possibleValues.add(Integer.valueOf(5));
		possibleValues.add(Integer.valueOf(6));
		possibleValues.add(Integer.valueOf(7));
		possibleValues.add(Integer.valueOf(8));
		possibleValues.add(Integer.valueOf(9));
		
		NumberFormat digitFormat = NumberFormat.getNumberInstance();
		digitFormat.setMaximumIntegerDigits(1);
		digitFormat.setMinimumIntegerDigits(0);
		digitFormat.setMaximumFractionDigits(0);
		digitFormat.setParseIntegerOnly(true);
		
		textField = new JFormattedTextField(digitFormat);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.addPropertyChangeListener("value", this);
		updateToolTip();
	}
	
	public JFormattedTextField getTextField() {
		return textField;
	}
	
	public Set<Integer> getPossibleValues() {
		return (Set<Integer>)possibleValues.clone();
	}
	
	public void removePossibleValues(Set<Integer> values) {
		if (this.isSolved()) {
			return;
		}
		
		possibleValues.removeAll(values);
		if (isSolved()) {
			textField.setValue(possibleValues.first());
		}
		updateToolTip();
	}
	
	protected void updateToolTip() {
		textField.setToolTipText(possibleValues.toString());
	}

	public boolean isSolved() {
		return (possibleValues.size() == 1);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof JTextField) {
			Object value = ((JFormattedTextField)event.getSource()).getValue();
			
			// Necessary check as boxes empty at start.
			if (value != null) {
				Integer finalValue = Integer.valueOf(((Number)value).intValue());

				if (possibleValues.contains(finalValue)) {
					possibleValues.clear();
					possibleValues.add(finalValue);
					textField.setBackground(Color.WHITE);
					updateToolTip();
					notifyListeners();
				}
				else {
					textField.setBackground(Color.RED);
				}
			}
		}
	}
	
	public void addCellChangeListener (SudokuCellChangeListener target) {
		listeners.add(target);
	}
	
	private void notifyListeners () {
		for (SudokuCellChangeListener target : listeners) {
			target.cellChanged(this);
		}
	}
}
