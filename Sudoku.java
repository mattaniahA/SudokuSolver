/////////////////////////////////////////////////////////////////////////////////
// CS 430 - Artificial Intelligence
// Project 4 - Sudoku Solver w/ Variable Ordering and Forward Checking
// File: Sudoku.java
//
// Group Member Names: Mattaniah Aytenfsu & Elyse Shackleton
// Due Date: December 5th, 2017, 11:55PM
// 
//
// Description: A Backtracking program in Java to solve the Sudoku problem.
// Code derived from a C++ implementation at:
// http://www.geeksforgeeks.org/backtracking-set-7-suduku/
/////////////////////////////////////////////////////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Sudoku{
	// Constants
	final static int UNASSIGNED = 0; //UNASSIGNED is used for empty cells in sudoku grid	
	final static int N = 9;//N is used for size of Sudoku grid. Size will be NxN
	static int numBacktracks = 0;
	
	/////////////////////////////////////////////////////////////////////
	// Main function used to test solver.
	public static void main(String[] args) throws FileNotFoundException{
		Scanner scan = new Scanner(System.in);
		//Gather input for variable ordering
		System.out.println("TEST CASE OPTIONS");
		System.out.println("-----------------");
		System.out.println("1: Test Case 1");
		System.out.println("2: Test Case 2");
		System.out.println("3: Test Case 3");
		System.out.println("4: Test Case 4");
		System.out.println("5: Test Case 5");
		System.out.print("Please input your choice (#): ");
		int testCase = scan.nextInt();
		System.out.println();
		
		//Gather input for variable ordering
		System.out.println("VARIABLE ORDERING OPTIONS");
		System.out.println("---------------");
		System.out.println("1: Default Static Ordering");
		System.out.println("2: Original Static Ordering");
		System.out.println("3: Original Random Ordering");
		System.out.println("4: Min Remaining Value Ordering");
		System.out.println("5: Max Remaining Value Ordering");
		System.out.print("Please input your choice (#): ");
		int variableOrdering = scan.nextInt();
		System.out.println();
				
		// Gather input for algorithm selection
		System.out.println("ALGORITHM OPTIONS");
		System.out.println("-----------------");
		System.out.println("1: Regular Backtracking");
		System.out.println("2: Forward Checking");
		int algSelect = scan.nextInt();
		System.out.println();
		
		// Reads in from selected test case (sample sudoku puzzle).
		// 0 means unassigned cells - You can search the internet for more test cases.
		Scanner fileScan;
		if (testCase == 1)
			fileScan = new Scanner(new File("case1.txt"));
		else if (testCase == 2)
			fileScan = new Scanner(new File("case2.txt"));
		else if (testCase == 3)
			fileScan = new Scanner(new File("case3.txt"));
		else if (testCase == 4)
			fileScan = new Scanner(new File("case4.txt"));
		else
			fileScan = new Scanner(new File("case5.txt"));

		// Reads case into grid 2D int array
			int grid[][] = new int[9][9];
			for (int r = 0; r < 9; r++){
				String row = fileScan.nextLine();
				String [] cols = row.split(",");
				for (int c = 0; c < cols.length; c++)
					grid[r][c] = Integer.parseInt(cols[c].trim());
			}
		// Prints out the unsolved sudoku puzzle (as is)
			System.out.println("Unsolved sudoku puzzle:");
			printGrid(grid);
	
		

		// Setup timer - Obtain the time before solving
		long stopTime = 0L;
		long startTime = System.currentTimeMillis();
		
//		// Create a two dimensional array of SudokuCoords to use alongside the regular grid
//		SudokuCoord[][] sudoGrid = new SudokuCoord[9][9];
		
//		// Load in the regular grid to the sudoGrid
//		for (int row = 0; row < 9; row++){
//			for (int col = 0; col < 9; col++){
//				sudoGrid[row][col] = new SudokuCoord(row, col, grid[row][col]);
//			}
//		}
		boolean algSuccess;
		if (algSelect == 1)
			algSuccess = SolveSudoku(grid, variableOrdering);
		else
			algSuccess = forwardCheck(grid, variableOrdering);
		
		if ( algSuccess == true){
			// Get stop time once the algorithm has completed solving the puzzle
			stopTime = System.currentTimeMillis();
			System.out.println("Algorithmic runtime: " + (stopTime - startTime) + "ms");
			System.out.println("Number of backtracks: " + numBacktracks);

			// Sanity check to make sure the computed solution really IS solved
			if (!isSolved(grid)){
				System.err.println("An error has been detected in the solution.");
				System.exit(0);
			}
			System.out.println("\n\nSolved sudoku puzzle:");
			printGrid(grid);
		}
		else
			System.out.println("No solution exists");

		fileScan.close();
	}

	/////////////////////////////////////////////////////////////////////
	// Write code here which returns true if the sudoku puzzle was solved
	// correctly, and false otherwise. In short, it should check that each
	// row, column, and 3x3 square of 9 cells maintain the ALLDIFF constraint.
	private static boolean isSolved(int[][] grid){

		Set<Integer> things = new HashSet<Integer>();

		// checks rows
		for(int r = 0; r < 9; r++){
			for(int c = 0; c < 9; c++){
				if( things.contains(grid[r][c]) )		
					return false;
				things.add(grid[r][c]);
			}
			things.clear();
		}

		// checks columns
		for(int r = 0; r < 9; r++){
			for(int c = 0; c < 9; c++){
				if( things.contains(grid[c][r]) )		
					return false;
				things.add(grid[c][r]);
			}
			things.clear();
		}

		//////////////////
		// checks boxes //
		//////////////////


		//box1
		for(int r = 0; r < 3; r++){
			for (int c = 0; c < 3; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();

		// box 2
		for(int r = 3; r < 6; r++){
			for (int c = 0; c < 3; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();


		// box 3
		for(int r = 0; r < 3; r++){
			for (int c = 6; c < 9; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();

		// box 4
		for(int r = 3; r < 6; r++){
			for (int c = 0; c < 3; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();

		//box 5
		for(int r = 3; r < 6; r++){
			for (int c = 3; c < 6; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();


		// box 6
		for(int r = 3; r < 6; r++){
			for (int c = 6; c < 9; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();

		// box 7
		for(int r = 0; r < 3; r++){
			for (int c = 6; c < 9; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();

		// box 8
		for(int r = 3; r < 6; r++){
			for (int c = 6; c < 9; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();

		//box9
		for(int r = 6; r < 9; r++){
			for (int c = 6; c < 9; c++){
				if( things.contains(grid[c][r])){
					return false;
				}
				things.add(grid[c][r]);
			}
		}
		things.clear();

		return true; 
	}


	/////////////////////////////////////////////////////////////////////
	// Takes a partially filled-in grid and attempts to assign values to
	// all unassigned locations in such a way to meet the requirements
	// for Sudoku solution (non-duplication across rows, columns, and boxes)
	/////////////////////////////////////////////////////////////////////
	static boolean forwardCheck(int grid[][], int userInput){
		// Select next unassigned variable
		System.out.println("	Forward Check is broken. We're not sure why!");
		SudokuCoord variable;

		// TODO: Here, you will create an IF-ELSEIF-ELSE statement to select 
		// the next variables using 1 of the 5 orderings selected by the user.
		// By default, it is hardcoded to the method FindUnassignedVariable(), 
		// which corresponds to the "1) Default static ordering" option.
		if (userInput == 1){
			variable = FindUnassignedVariable(grid); }
		else if (userInput == 2){ 
			variable = MyOriginalStaticOrderingOpt2(grid);}
		else if (userInput == 3){
			variable = MyOriginalRandomOrderingOpt3(grid);}
		else if (userInput == 4){
			variable = MyMinRemainingValueOrderingOpt4(grid);}
		else{
			variable = MyMaxRemainingValueOrderingOpt5(grid);}

		// If there is no unassigned location, we are done
		if (variable == null)
			return true; // success!

		int row = variable.row;
		int col = variable.col;

		// consider digits 1 to 9
		for (int num = 1; num <= 9; num++){
			// if looks promising
			if (isSafe(grid, row, col, num)){
				// make tentative assignment
				grid[row][col] = num;
				
				// See if any unassigned variable domains are empty
				for (int i = 0; i < 9; i++){
					for (int j = 0; j < 9; j++){
						if (grid[i][j] == UNASSIGNED){
							int domainCount = 0;
							for (int value = 1; value <= 9; value++){
								if (isSafe(grid, i, j, value)){
									domainCount++;
								}
							}
							if (domainCount == 0){ // If encountered an empty domain, backtrack
								numBacktracks++;
								grid[row][col] = UNASSIGNED;
								numBacktracks++;
								return false;
							}
						}
					}
				}

				// return, if success, yay!
				if (forwardCheck(grid, userInput))
					return true;

				// failure, un-assign & try again
				grid[row][col] = UNASSIGNED;
			}
		}

		// Increment the number of backtracks
		numBacktracks++;
		return false; // This triggers backtracking
	}
	
	static boolean SolveSudoku(int grid[][], int userInput){
		// Select next unassigned variable
		SudokuCoord variable;
		
		if (userInput == 1){
			variable = FindUnassignedVariable(grid); }
		else if (userInput == 2){ 
			variable = MyOriginalStaticOrderingOpt2(grid);}
		else if (userInput == 3){
			variable = MyOriginalRandomOrderingOpt3(grid);}
		else if (userInput == 4){
			variable = MyMinRemainingValueOrderingOpt4(grid);}
		else{
			variable = MyMaxRemainingValueOrderingOpt5(grid);}

		// If there is no unassigned location, we are done
		if (variable == null)
			return true; // success!

		int row = variable.row;
		int col = variable.col;

		// consider digits 1 to 9
		for (int num = 1; num <= 9; num++){
			// if looks promising
			if (isSafe(grid, row, col, num)){
				// make tentative assignment
				grid[row][col] = num;

				// return, if success, yay!
				if (SolveSudoku(grid, userInput))
					return true;

				// failure, un-assign & try again
				grid[row][col] = UNASSIGNED;
			}
		}

		// Increment the number of backtracks
		numBacktracks++;
		return false; // This triggers backtracking
	}
	static boolean domainCheck(int grid[][], SudokuCoord[][] sudoGrid){
		for (int row = 0; row < 9; row++){
			for (int col = 0; col < 9; col++){
				// If an empty space has no domain remaining, early failure
				if (grid[row][col] == UNASSIGNED && sudoGrid[row][col].domain.size() == 0){
					return false;
				}
			}
		}
		return true;
	}
	static ArrayList<Integer> buildDomain(int grid[][], int row, int col){
		ArrayList<Integer> domain = new ArrayList<Integer>();
		
		for (int num = 1; num <= 9; num++){
			if (isSafe(grid, row, col, num)){
				domain.add(num);
			}
		}
		
		return domain;
	}
	
	

	/////////////////////////////////////////////////////////////////////
	// Searches the grid to find an entry that is still unassigned. If
	// found, the reference parameters row, col will be set the location
	// that is unassigned, and true is returned. If no unassigned entries
	// remain, null is returned.
	/////////////////////////////////////////////////////////////////////
	static SudokuCoord FindUnassignedVariable(int grid[][]){
		for (int row = 0; row < N; row++)
			for (int col = 0; col < N; col++)
				if (grid[row][col] == UNASSIGNED)
					return new SudokuCoord(row, col, grid[row][col]);
		return null;
	}

	/////////////////////////////////////////////////////////////////////
	// TODO: Implement the following orderings, as specified in the
	// project description. You MAY feel free to add extra parameters if
	// needed (you shouldn't need to for the first two, but it may prove
	// helpful for the last two methods).
	/////////////////////////////////////////////////////////////////////
	static SudokuCoord MyOriginalStaticOrderingOpt2(int grid[][]){
		for (int row = 0; row < N; row++)
			for (int col = 0; col < N; col++)
				if (grid[col][row] == UNASSIGNED)
					return new SudokuCoord(col, row, grid[row][col]);

		return null;
	}


	static SudokuCoord MyOriginalRandomOrderingOpt3(int grid[][]){
		ArrayList<SudokuCoord> unassList = new ArrayList<SudokuCoord>();
		for (int row = 0; row < N; row++)
			for (int col = 0; col < N; col++)
				if (grid[row][col] == UNASSIGNED){
					unassList.add( new SudokuCoord(row, col, grid[row][col]) );
				}

		SudokuCoord var;
		Random rand = new Random();
		if(unassList.size()>0){
			int rInd = rand.nextInt(unassList.size());
			var = unassList.get( rInd );
		}else{
			var = null;
		}

		return var;
	}


	//TODO: should it backtrack less than the OG static ordering
	static SudokuCoord MyMinRemainingValueOrderingOpt4(int grid[][]){
		ArrayList<SudokuCoord> unassList = new ArrayList<SudokuCoord>();
		int constraintCounter = 0;
		int mrv = 0;
		SudokuCoord chosenOne = null;

		for (int row = 0; row < N; row++)			// add all unassigned values to a list
			for (int col = 0; col < N; col++)
				if (grid[row][col] == UNASSIGNED){
					unassList.add( new SudokuCoord(row, col, grid[row][col]) );
				}

		for(SudokuCoord i : unassList){		// checks each unassigned value

			for (int col = 0; col < N; col++){		// counts number of assigned values in row
				if ( grid[i.row][col] > 0 )
					constraintCounter++;
			}

			for (int row = 0; row < N; row++){		// counts number of assigned values in col
				if ( grid[row][i.col] > 0 )
					constraintCounter++;
			}

			//TODO: make a function

			///////////// BOX 1 ///////////////
			if(i.row >= 0 && i.row < 3 && i.col >= 0 && i.col < 3)		
				for(int r = 0; r < 3; r++)
					for (int c = 0; c < 3; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 2 ///////////////
			if(i.row >= 0 && i.row < 3 && i.col >= 3 && i.col < 6)
				for(int r = 0; r < 3; r++)
					for (int c = 3; c < 6; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 3 ///////////////
			if(i.row >= 0 && i.row < 3 && i.col >= 6 && i.col < 9)
				for(int r = 0; r < 3; r++)
					for (int c = 6; c < 9; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 4 ///////////////
			if(i.row >= 3 && i.row < 6 && i.col >= 0 && i.col < 3)		
				for(int r = 3; r < 6; r++)
					for (int c = 0; c < 3; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 5 ///////////////
			if(i.row >= 3 && i.row < 6 && i.col >= 3 && i.col < 6)		
				for(int r = 3; r < 6; r++)
					for (int c = 3; c < 6; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 6 ///////////////
			if(i.row >= 3 && i.row < 6 && i.col >= 6 && i.col < 9)		
				for(int r = 3; r < 6; r++)
					for (int c = 6; c < 9; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 7 ///////////////
			if(i.row >= 6 && i.row < 9 && i.col >= 0 && i.col < 3)		
				for(int r = 6; r < 9; r++)
					for (int c = 0; c < 3; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 8 ///////////////
			if(i.row >= 6 && i.row < 9 && i.col >= 3 && i.col < 6)		
				for(int r = 6; r < 9; r++)
					for (int c = 3; c < 6; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 9 ///////////////
			if(i.row >= 6 && i.row < 9 && i.col >= 6 && i.col < 9)		
				for(int r = 6; r < 9; r++)
					for (int c = 6; c < 9; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;


			//						System.out.println("conCount: " + constraintCounter);
			//						System.out.println("mrv: " + mrv);
			if (constraintCounter > mrv){		// coordinate i has more constraints than the previous
				mrv = constraintCounter;
				chosenOne = i;
			}
			constraintCounter = 0;
		}


		return chosenOne;
	}
	static SudokuCoord MyMaxRemainingValueOrderingOpt5(int grid[][]){
		ArrayList<SudokuCoord> unassList = new ArrayList<SudokuCoord>();
		int constraintCounter = 0;
		int mrv = 10000000;
		SudokuCoord chosenOne = null;

		for (int row = 0; row < N; row++)			// add all unassigned values to a list
			for (int col = 0; col < N; col++)
				if (grid[row][col] == UNASSIGNED){
					unassList.add( new SudokuCoord(row, col, grid[row][col]) );
				}

		for(SudokuCoord i : unassList){		// checks each unassigned value

			for (int col = 0; col < N; col++){		// counts number of assigned values in row
				if ( grid[i.row][col] > 0 )
					constraintCounter++;
			}

			for (int row = 0; row < N; row++){		// counts number of assigned values in col
				if ( grid[row][i.col] > 0 )
					constraintCounter++;
			}

			//TODO: make a function

			///////////// BOX 1 ///////////////
			if(i.row >= 0 && i.row < 3 && i.col >= 0 && i.col < 3)		
				for(int r = 0; r < 3; r++)
					for (int c = 0; c < 3; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 2 ///////////////
			if(i.row >= 0 && i.row < 3 && i.col >= 3 && i.col < 6)
				for(int r = 0; r < 3; r++)
					for (int c = 3; c < 6; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 3 ///////////////
			if(i.row >= 0 && i.row < 3 && i.col >= 6 && i.col < 9)
				for(int r = 0; r < 3; r++)
					for (int c = 6; c < 9; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 4 ///////////////
			if(i.row >= 3 && i.row < 6 && i.col >= 0 && i.col < 3)		
				for(int r = 3; r < 6; r++)
					for (int c = 0; c < 3; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 5 ///////////////
			if(i.row >= 3 && i.row < 6 && i.col >= 3 && i.col < 6)		
				for(int r = 3; r < 6; r++)
					for (int c = 3; c < 6; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 6 ///////////////
			if(i.row >= 3 && i.row < 6 && i.col >= 6 && i.col < 9)		
				for(int r = 3; r < 6; r++)
					for (int c = 6; c < 9; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 7 ///////////////
			if(i.row >= 6 && i.row < 9 && i.col >= 0 && i.col < 3)		
				for(int r = 6; r < 9; r++)
					for (int c = 0; c < 3; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 8 ///////////////
			if(i.row >= 6 && i.row < 9 && i.col >= 3 && i.col < 6)		
				for(int r = 6; r < 9; r++)
					for (int c = 3; c < 6; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;

			///////////// BOX 9 ///////////////
			if(i.row >= 6 && i.row < 9 && i.col >= 6 && i.col < 9)		
				for(int r = 6; r < 9; r++)
					for (int c = 6; c < 9; c++)
						if( grid[c][r] != UNASSIGNED )
							constraintCounter++;


			//			System.out.println("conCount: " + constraintCounter);
			//			System.out.println("mrv: " + mrv);
			if (constraintCounter < mrv){		// coordinate i has more constraints than the previous
				mrv = constraintCounter;
				chosenOne = i;
			}
			constraintCounter = 0;
		}
		return chosenOne;

	}

	/////////////////////////////////////////////////////////////////////
	// Returns a boolean which indicates whether any assigned entry
	// in the specified row matches the given number.
	/////////////////////////////////////////////////////////////////////
	static boolean UsedInRow(int grid[][], int row, int num){
		for (int col = 0; col < N; col++)
			if (grid[row][col] == num)
				return true;
		return false;
	}

	/////////////////////////////////////////////////////////////////////
	// Returns a boolean which indicates whether any assigned entry
	// in the specified column matches the given number.
	/////////////////////////////////////////////////////////////////////
	static boolean UsedInCol(int grid[][], int col, int num){
		for (int row = 0; row < N; row++)
			if (grid[row][col] == num) 
				return true;
		return false;
	}

	/////////////////////////////////////////////////////////////////////
	// Returns a boolean which indicates whether any assigned entry
	// within the specified 3x3 box matches the given number.
	/////////////////////////////////////////////////////////////////////
	static boolean UsedInBox(int grid[][], int boxStartRow, int boxStartCol, int num){
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++)
				if (grid[row+boxStartRow][col+boxStartCol] == num)
					return true;
		return false;
	}

	/////////////////////////////////////////////////////////////////////
	// Returns a boolean which indicates whether it will be legal to assign
	// num to the given row, col location.
	/////////////////////////////////////////////////////////////////////
	static boolean isSafe(int grid[][], int row, int col, int num){
		// Check if 'num' is not already placed in current row,
		// current column and current 3x3 box
		return !UsedInRow(grid, row, num) &&
				!UsedInCol(grid, col, num) &&
				!UsedInBox(grid, row - row%3 , col - col%3, num);
	}

	/////////////////////////////////////////////////////////////////////
	// A utility function to print grid
	/////////////////////////////////////////////////////////////////////
	static void printGrid(int grid[][]){
		for (int row = 0; row < N; row++){
			for (int col = 0; col < N; col++){
				if (grid[row][col] == 0)
					System.out.print("- ");
				else
					System.out.print(grid[row][col] + " ");

				if ((col+1) % 3 == 0)
					System.out.print(" ");
			}	    	   
			System.out.print("\n");
			if ((row+1) % 3 == 0)
				System.out.println();
		}
	}
}