import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class GameGUI extends JFrame{
	//inner classes
	static class GuiPanel extends JPanel {
		//fields 
		private GuiPuzzle pPanel = new GuiPuzzle(this);
		private GuiButtons bPanel = new GuiButtons(this);

		
		public GuiPanel() {
			adjustLayout();
		}
		
		//methods 
		private void adjustLayout() {
			//adjusts layout 
			setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			
			//pPanel 
			cons.gridx = 0;
			cons.gridy = 0;
			cons.anchor = GridBagConstraints.NORTH;
			add(pPanel, cons);
			
			//bPanel 
			cons.gridx = 0;
			cons.gridy = 1;
			cons.anchor = GridBagConstraints.SOUTH;
			add(bPanel, cons);
		}
	}
	
	static class GuiButtons extends JPanel {
		//fields 
		private GuiPanel gp;
		private SudokuGen sud;
		private int sudokuGames = 0;
		private int[][][] correctAnswer = new int[9][3][3];
		private int[][][] userAnswer = new int[9][3][3];
		
		//buttons
		private JButton genButton = new JButton("Generate new puzzle");
		private JButton checkButton = new JButton("Check answer");
		
		//textfields 
		private JLabel sudokuGamesText = new JLabel("Number of games played: " + sudokuGames);
		
		//cons 
		public GuiButtons(GuiPanel gp) {
			this.gp = gp;
			
			adjustLayout();
			
			//actionlisteners for buttons 
			//gen button
			genButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a) {
					//increms the amount of games played and enables various features the first time button is pressed 
					//displays number of sudokugames made
					sudokuGames++;
					sudokuGamesText.setText("Number of games played: " + sudokuGames);
					if (sudokuGames == 1) {
						checkButton.setEnabled(true);
						for (int i = 0; i < 81; i++) {
							gp.pPanel.buttons.get(i).setVisible(true);
						}
					}
					//generate instance of SudokuGen and set correctAnswer 
					sud = new SudokuGen();
					correctAnswer = sud.getFilledSudoku();
					
					//clear buttons 
					for (int n = 0; n < 81; n++) {
						gp.pPanel.buttons.get(n).setEnabled(true);
						gp.pPanel.buttons.get(n).setText("");
					}
					
					
					//takes 3d array and displays it on 9x9 square of buttons
					int[][][] puzzle = sud.getFinishedSudoku();

					int start = 0;
					for (int z = 0; z < 9; z++) {
						if (z == 3) {
							start = 27;
						}
						else if (z == 6) {
							start = 54;
						}
						enterSudoku(z, start, puzzle);
						start = start + 3;
					}
					
			
				}
			});
			
			checkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a) {
					//like gen, except it fills in a 3d array from user inputs
					int start = 0;
					for (int z = 0; z < 9; z++) {
						if (z == 3) {
							start = 27;
						}
						else if (z == 6) {
							start = 54;
						}
						try {
							createUserAnswer(z, start);
						} catch (NumberFormatException nfe) {
							//TODO
							System.out.println("Enter in valid numbers only");
						}
						createUserAnswer(z, start);
						start = start + 3;
					}
					//checks answer with boolean and for loop
					boolean isCorrect = true;
					outer: for (int z = 0; z < 9; z++) {
						for (int y = 0; y < 3; y++) {
							for (int x = 0; x < 3; x++) {
								if (userAnswer[z][y][x] != correctAnswer[z][y][x]) {
									isCorrect = false;
									break outer;
								}
							}
						}
					}
					
					if (isCorrect) {
						JOptionPane.showMessageDialog(null, "Correct!", "Checking...", JOptionPane.INFORMATION_MESSAGE);;
					}
					else {
						JOptionPane.showMessageDialog(null, "Incorrect", "Checking...", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});

			
		}
		
		//methods 
		private void adjustLayout() {
			//adjusts the layout
			setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			
			//sudokuGamesText 
			cons.gridx = 0;
			cons.gridy = 10;
			cons.insets = (new Insets(10, 200, 0, 0));
			add(sudokuGamesText, cons);
			
			//genButton 
			cons.gridx = 0;
			cons.gridy = 0;
			cons.anchor = GridBagConstraints.NORTH;
			cons.insets = (new Insets(20, 0, 0, 20));
			add(genButton, cons);
			
			//checkButton 
			cons.gridx = 1;
			cons.gridy = 0;
			cons.anchor = GridBagConstraints.SOUTH;
			cons.insets = (new Insets(20, 0, 0, 200));
			add(checkButton, cons);
			checkButton.setEnabled(false);
		}
		
		private void enterSudoku(int z, int start, int[][][] puzzle) {
			int firstX = 0;
			int firstY = 0;
			int count = 0;
			int secondLine = start + 9;
			int thirdLine = start + 9 + 9;
			
			for (int l = 0; l < 9; l++) {
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
				
				
				if (count < 4) {
					String str = Integer.toString(puzzle[z][firstY][firstX]);
					if (str.equals("0")) {
						addUseButton(gp.pPanel.buttons.get(start));
					}
					else {
						gp.pPanel.buttons.get(start).setText(Integer.toString(puzzle[z][firstY][firstX]));
						gp.pPanel.buttons.get(start).setEnabled(false);
					}
					start++;
				}
				else if (count >= 4 && count < 7) {
					String str = Integer.toString(puzzle[z][firstY][firstX]);
					if (str.equals("0")) {
						addUseButton(gp.pPanel.buttons.get(secondLine));
					}
					else {
						gp.pPanel.buttons.get(secondLine).setText(Integer.toString(puzzle[z][firstY][firstX]));
						gp.pPanel.buttons.get(secondLine).setEnabled(false);
					}
					secondLine++;
				}
				else if (count >= 7) {
					String str = Integer.toString(puzzle[z][firstY][firstX]);
					if (str.equals("0")) {
						addUseButton(gp.pPanel.buttons.get(thirdLine));
					}
					else {
						gp.pPanel.buttons.get(thirdLine).setText(Integer.toString(puzzle[z][firstY][firstX]));
						gp.pPanel.buttons.get(thirdLine).setEnabled(false);
					}
					thirdLine++;
				}				
				
			}
		}
		
		private void createUserAnswer(int z,int start) {
			int firstX = 0;
			int firstY = 0;
			int count = 0;
			int secondLine = start + 9;
			int thirdLine = start + 9 + 9;
			
			for (int l = 0; l < 9; l++) {
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
				
				
				if (count < 4) {
					String userIn = gp.pPanel.buttons.get(start).getText();
					if (userIn.equals("")) {
						userAnswer[z][firstY][firstX] = 25;
					}
					else {
						userAnswer[z][firstY][firstX] = Integer.parseInt(userIn);
					}
					start++;
				}
				else if (count >= 4 && count < 7) {
					String userIn = gp.pPanel.buttons.get(secondLine).getText();
					if (userIn.equals("")) {
						userAnswer[z][firstY][firstX] = 0;
					}
					else {
						userAnswer[z][firstY][firstX] = Integer.parseInt(userIn);
					}
					secondLine++;
				}
				else if (count >= 7) {
					String userIn = gp.pPanel.buttons.get(thirdLine).getText();
					if (userIn.equals("")) {
						userAnswer[z][firstY][firstX] = 0;
					}
					else {
						userAnswer[z][firstY][firstX] = Integer.parseInt(userIn);
					}
					thirdLine++;
				}					
			}
		}
		
		private void addUseButton(JButton button) {
			//passed button param gets an actionListener that opens a new frame 
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a) {
					
					//create frame, add panel 
					GameGUI numPadFrame = new GameGUI("Numpad", 300, 300);
					numPadFrame.setVisible(true);
					
					//panel and layout
					JPanel inputPanel = new JPanel();
					inputPanel.setLayout(new GridLayout(3, 3));
				
					
					//buttons
					ArrayList<JButton> numPad = new ArrayList<>();
					for (int i = 1; i < 10; i++) {
						numPad.add(new JButton(Integer.toString(i)));
						int z = i - 1;
						numPad.get(i - 1).addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent a) {
								button.setText(numPad.get(z).getText()); 
								numPadFrame.dispose();
							}
						});
						
					}
					
					//adds buttons to panel
					for (int i = 0; i < 9; i++) {
						inputPanel.add(numPad.get(i));
					}
					
					//adds panel to frame
					numPadFrame.add(inputPanel);
					
				}
			});
		}
		
	}

	static class GuiPuzzle extends JPanel {
		//fields 
		private GuiPanel gp;
		
		//buttons 
		private ArrayList<JButton> buttons = new ArrayList<>();
		
		
		//cons 
		public GuiPuzzle(GuiPanel gp) {
			this.gp = gp;
			
			createButtons();
			adjustLayout();
			
			buttons.get(27).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a) {
					System.out.println("Hello");
				}
			});
		}
		
		//methods 
		
		private void createButtons() {
			for (int i = 1; i < 82; i++) {
				buttons.add(new JButton("   "));
			}
		}
		
		private void adjustLayout() {
			//layout 
			GridLayout gl = new GridLayout(9, 9, 5, 5);
			setLayout(gl);
			
			for (int i = 0; i < 81; i++) {
				add(buttons.get(i));
				buttons.get(i).setVisible(false);
			}
			
		}
		
	}
	
	/*
	static class GuiMain extends Gui {
		//cons 
		public GuiMain() {
			super();
			add(new GuiPanel());
		}
	}
	*/
	public GameGUI() {
		super("Sudoku");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		add(new GuiPanel());
	}
	public GameGUI(String str ,int w, int l) {
		super(str);
		setSize(w, l);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	//methods 
	public void display() {
		setVisible(true);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
