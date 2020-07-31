/*Author: Abel Tabor 
 *Date: 7/20/2020
 *File name: Project4.java
 *Purpose: This class generates a sudoku puzzle in a 3d array. Depth is each 3x3 sqaure and row and col relate to y and x respectivly.
 *failCount and absFailCount are used to avoid StackOverFlow as much possible
 *				
 */
import java.util.*;
public class SudokuGen {
	//static fields 
	private static final int ROW = 3;
	private static final int COL = 3;
	private static final int DEPTH = 9;
	
	//fields 
	private int[][][] filledSudoku = new int[DEPTH][ROW][COL];
	private int[][][] finishedSudoku = new int[DEPTH][ROW][COL];
	private int[][] Sudoku = new int[9][9];
	private int failCount = 0;
	private int absFailCount = 0;
	
	//cons 
	public SudokuGen() {
		emptySudoku();
		fillInSudoku();
		finishSudoku();
	}
	
	//methods
	
	//checking methods 
	private boolean squareCheck(int ranNum, int z) {
		//method checks in a given 3x3 square, returns true if value given is unique within parameters
		
		//local variables, used to emulate x and y values and increments based on count 
		int firstX = 0;
		int firstY = 0;
		int count = 0;
		boolean squareBool = true;
		
		for (int i = 0; i < 9; i++) {
			//count 
			count++; 
			
			//increments firstX and firstY
			if (count > 1 && count < 4) {
				firstX++;
			}
			if (count == 4) {
				firstX = 0; 
				firstY = 1;
			}
			if (count > 4 && count < 7) {
				firstX++;
			}
			if (count == 7) {
				firstX = 0;
				firstY = 2;
			}
			if (count > 7) {
				firstX++;
			}
			
			
			if (ranNum != filledSudoku[z][firstY][firstX]) {
				continue;
			}
			
			if (ranNum == filledSudoku[z][firstY][firstX]) {
				squareBool = false;
			}
			
		}
		return squareBool;
	}
	
	private boolean horzCheck(int ranNum, int z, int y) {
		//method checks in a given horz line, returns true if value given is unique within parameters
		
		//local variables, used to emulate x and z values and increments based on count 
		int firstX1 = 0;
		int firstZ = 0;
		int count1 = 0;
		boolean horzBool = true; 
		
		//adjusts firstZ depending on current z value 
		if (z <= 2) {
			firstZ = 0;
		}
		if (z <= 5 && z > 2) {
			firstZ = 3;
		}
		if (z <= 8 && z > 5) {
			firstZ = 6;
		}
		
		for (int i = 0; i < 9; i++) {
			count1++; 
			
			if (count1 != 1 && count1 != 4 && count1 != 7) {
				firstX1++;
			}
			
			
			//increments firztZ depending on count
			if (count1 == 4 || count1 == 7) {
				firstZ++;
				firstX1 = 0;
			}
			
			if (ranNum != filledSudoku[firstZ][y][firstX1]) {
				continue;
			}
			if (ranNum == filledSudoku[firstZ][y][firstX1]) {
				horzBool = false;
				break;
			}
		}

		return horzBool;
	}
	
	private boolean vertCheck(int ranNum, int z, int x) {
		//method checks in a given vert line, returns true if value given is unique within parameters
		
		//local variables, used to emulate y and z values and increments based on count 
		int firstY1 = 0; 
		int firstZ1 = 0;
		int count2 = 0;
		boolean vertBool = true;
		
		//adjusts firstZ1 value depending on value of z
		if (z == 0 || z == 3 ||z == 6) {
			firstZ1 = 0;
		}
		if (z == 1 || z == 4 ||z == 7) {
			firstZ1 = 1;
		}
		if (z == 2 || z == 5 ||z == 8) {
			firstZ1 = 2;
		}
		
		for (int i = 0; i < 9; i++) {
			count2++;
			
			if (count2 != 1 && count2 != 4 && count2 != 7) {
				firstY1++;
			}
			
			if (count2 == 4 || count2 == 7) {
				firstZ1 += 3;
				firstY1 = 0;
			}
			
			if (ranNum != filledSudoku[firstZ1][firstY1][x]) {
				continue;
			}
			if (ranNum == filledSudoku[firstZ1][firstY1][x]) {
				vertBool = false;
				break;
			}
		}
		return vertBool;
	}
	
	private boolean isFull() {
		//for loop checks to see if any 0 values exist in the array, if any exist returns false, if no 0 values returns true
		for (int z = 0; z < DEPTH; z++) {
			for (int y = 0; y < ROW; y++) {
				for (int x = 0; x < COL; x++) {
					if (filledSudoku[z][y][x] == 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	//editing sudoku
	private ArrayList<Integer> randomNumbers() {
		//returns ArrayList of integers 1-9 in a random order
		ArrayList<Integer> num = new ArrayList<>();
		for (int i = 1; i < 10; i++) {
			num.add(i);
		}
		Collections.shuffle(num);
		return num;
	}
	
	private void emptySudoku() {
		//turns all values in the array to 0
		for (int z = 0; z < DEPTH; z++) {
			for (int y = 0; y < ROW; y++) {
				for (int x = 0; x < COL; x++) {
					filledSudoku[z][y][x] = 0;
				}
			}
		}
	}
	
	private boolean recursiveSudoku(int z, int y, int x) {
		//base case, if isFull is true then sudoku array is filled
		if (isFull()) {
			return false;
		}
		
		//local variables, 
		//boolean value adjusts if the code recurses forward or needs to backtrack
		//ArrayList allows numbers to be checked in a psuedo-random manner without any duplicates 
		boolean notPass = false;
		ArrayList<Integer> nums = randomNumbers();
		
		//inputs values to empty slots
		if (filledSudoku[z][y][x] == 0) {
 			//repeats 9 times to check all values in nums
			for (int i = 0; i < 9; i++) {
				int ranNum = nums.get(i);
 				
				//if value passes all checks, it is written in to passed coords
				if (squareCheck(ranNum, z) && horzCheck(ranNum, z, y) && vertCheck(ranNum, z, x)) {
					filledSudoku[z][y][x] = ranNum;
					notPass = false;
					break;
				}
				//if all values dont work changes notPass to true for later in the code
				if (i == 8) {
					notPass = true;
					//failCount allows the code to recongize when it needs to backtrack more than one value
					failCount++;
					
					if (failCount > 50) {
						//resets failCount, increments absFailCount turns the last 2 squares into 0s, then recurses from that point
						failCount = 0;
						absFailCount++;
						
						//avoids stackoverflow
						if (absFailCount == 5) {
							return true;
						}
						
						//turns last 2 squares into empty values
						for (int z1 = z; z1 > z - 3; z1--) {
							for (int y1 = 0; y1 < 3; y1++) {
								for (int x1 = 0; x1 < 3; x1++) {
									if (z1 < 0) {
										continue;
									}
									filledSudoku[z1][y1][x1] = 0;
								}//end x
							}//end y
						}//end z
						if (z < 2) {
							recursiveSudoku(z - 1, 0, 0);
						}
						else {
							recursiveSudoku(z - 3, 0, 0);
						}
					}//end if failcount
					
				}//end if i = 8
			}//end for

		}
		//if failCount is below 50, this section is run
		//decrements current value depending on what value code is at, sets it to zero and recurses
		if (notPass) {
			if (x > 0 && x < 3 && y < 3) {
				x--;
			}
			else if (x == 0 && y > 0) {
				x = 2;
				y--;
			}
			else if (x == 0 && y == 0) {
				x = 2;
				y = 2;
				z--;
			}
			else if (x == 2 && y > 0 && y < 2) {
				x = 0; 
				y--;
			}
			else if (x == 2 && y == 2) {
				x--;
			}
			
			filledSudoku[z][y][x] = 0;
			recursiveSudoku(z, y, x);
		}
		//increments current value and recurses forward
		else {
			if (x < 2 && y <= 2) {
				x++;
			}
			else if (x == 2 && y < 2) {
				x = 0; 
				y++;
			}
			else if (x == 2 && y == 2) {
				x = 0;
				y = 0; 
				z++;
			}
			recursiveSudoku(z, y, x);
		}
		return false;	
	}
	
	private void fillInSudoku() {
		//catches stackoverflow and indexoutofbounds if random array values end up not
		try {
			recursiveSudoku(0, 0, 0);
		} catch (StackOverflowError sof) {
			fillInSudoku();
		} catch (IndexOutOfBoundsException iob) {
			fillInSudoku();
		}
	}
	
	private void finishSudoku() {
		Random rand = new Random();
		finishedSudoku = filledSudoku;
		int z; 
		int y; 
		int x;
		for (int i = 0; i < 30; i++) {
			z = rand.nextInt(9);
			y = rand.nextInt(3);
			x = rand.nextInt(3);
			finishedSudoku[z][y][x] = 0;
		}
	}


	
	//getters
	public int[][][] getFilledSudoku() {
		return filledSudoku;
	}
	public int[][][] getFinishedSudoku() {
		return finishedSudoku;
	}
	public int[][] getSudoku() {
		return Sudoku;
	}
	

}
