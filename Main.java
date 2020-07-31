
public class Main {

	public static void main(String[] args) {
		GameGUI g1 = new GameGUI();
		g1.display();
		/*
		for (int i = 0; i < 10; i++) {	
			int[][][] sudoku = (new SudokuGen()).getFilledSudoku();
			for (int z = 0; z < 9 ; z++) {
				for (int y = 0; y < 3; y++) {
					for (int x = 0; x < 3; x++) {
						if (i < 0) {
							System.out.print("\n");
						}
						System.out.print(sudoku[z][y][x] + " ");
						}	
					}
				}
		}
		*/

	}

}
