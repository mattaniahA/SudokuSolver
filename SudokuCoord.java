import java.util.ArrayList;

/////////////////////////////////////////////////////////////////////////////////
// CS 430 - Artificial Intelligence
// Project 4 - Sudoku Solver w/ Variable Ordering and Forward Checking
// File: SudokuCoord.java
//
// Description: This class represents a Sudoku coordinate (square), which acts
// as a variable for our constraint satisfaction problem (CSP)
/////////////////////////////////////////////////////////////////////////////////
public class SudokuCoord
{
	public int row;
	public int col;
	public int value;
	public ArrayList<Integer> domain = new ArrayList<Integer>();
	
	SudokuCoord()
	{
		row = 0;
		col = 0;
		value = 0;
		
	}
	
	SudokuCoord(int r, int c, int v)
	{
		row = r;
		col = c;
		value = v;
	}
}