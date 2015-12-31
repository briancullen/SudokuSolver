package net.mrcullen.sudokusolver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


import javax.swing.*;

@SuppressWarnings("serial")
public class SudokuSolver extends JFrame {
	
	public SudokuSolver() {
		super("Sudoku Solver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menubar = new JMenuBar();
        JMenu sudoku = new JMenu("Sudoku");
        sudoku.setMnemonic(KeyEvent.VK_S);
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        sudoku.add(exitMenuItem);
        menubar.add(sudoku);

        setJMenuBar(menubar);
        
        createGrid(3);
        pack();
        
        // Bit of a hack but it looks squashed.
        this.setSize(400, 400);
	}
	
	protected void createGrid(int size) {
		SudokuPuzzle puzzle = new SudokuPuzzle(size);
		
		// Nothing to remove old one!!
		add(puzzle.getPuzzlePanel());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater (new Runnable() {
			@Override
			public void run() {
				SudokuSolver solver = new SudokuSolver();
				solver.setVisible(true);
			}
		});

	}

}
