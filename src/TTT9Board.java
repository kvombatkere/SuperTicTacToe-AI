//Rebecca Van Dyke, Avi Webberman, Karan Vombatkere
//February 2018
//CSC 442: AI Project 01 - Tic Tac Toe

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class TTT9Board implements Serializable{
	
	//class variables
	public TTTBoard [][] boardArray;
	public char [][] gameStatus; //use this array to track win/lose/draw statuses of each board
	//X=x won, O=o won, d=draw, n=not terminated
	public boolean firstMove = true;
	public char nextPlayer;
	public int nextBoardIndex;
	public char overallGameStatus;
	public int moveCounter;
	
	//constructor
	public TTT9Board() {
		this.boardArray = new TTTBoard [3][3];
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				boardArray[i][j] = new TTTBoard();
			}
		}
		this.clearBoard();
		
		this.gameStatus = new char [3][3];
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				gameStatus[i][j] = 'n';
			}
		}
		this.nextPlayer = 'X';
		
		this.overallGameStatus = 'n';
		this.nextBoardIndex = 1;
	}
	
	//clear board
	public void clearBoard() {
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++) {
				this.boardArray[i][j].clearBoard();
			}
		}
		this.moveCounter = 0;
		
	}
	
	//print board
	public void displayBoard() {
		System.err.println("----------------------------------------------");
		System.err.println("Displaying 9-Board Tic Tac Toe Current State:\n");
		
		for(int i=0; i<3; i++) { //3 rows of board array
			for(int j=0; j<3; j++) { //3 rows in each board
				for(int k=0; k<3; k++) { //3 boards in each row
					this.boardArray[i][k].printRow(j);
					if(k != 2) {
						System.err.print("  | ");
					}
				}
				if(j != 2) {
					System.err.println("\n------------ | ------------ | ------------");
				}
			}
			if(i != 2) {
				System.err.println("\n             |              |             ");
				System.err.println("------------------------------------------");
				System.err.println("             |              |             ");

			}
		}
		
		System.err.println("\n");
	}
	
	//get board by index
	public TTTBoard getBoard(int index) {
		return this.boardArray[(index-1)/3][(index-1)%3];
	}
	
	//get next board
	public TTTBoard getNextBoard() {
		return this.boardArray[(this.nextBoardIndex-1)/3][(this.nextBoardIndex-1)%3];
	}
	
	//print status
	public void printWinStatus() {
		System.err.println("----------------------------------------------");
		System.err.println("Displaying 9-Board Tic Tac Toe Win/Lose/Draw State:\n");
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				System.err.print(" " + (gameStatus[i][j]) + " ");
				if(j != 2) {
					System.err.print("|");
				}
			}
			if(i != 2) {
				System.err.println("\n------------");
			}
		}
	}
	
	
	//check if move is allowed in game class, only legal moves will be entered into moveResult
	public TTT9Board moveResult(char player, int boardIndex, int boardPos) {
		
		this.getBoard(boardIndex).moveResult(player, boardPos);
		this.nextBoardIndex=boardPos;
		
		System.err.println(player + " just made move " + boardIndex + " " + boardPos);
		
		//increment move counter
		this.moveCounter++;
		
		//check if you won
		this.checkWin(this.nextPlayer, boardIndex);
			
		//if not, toggle nextPlayer and wait for another move
		if(this.overallGameStatus == 'n') {
			if(this.nextPlayer=='X') {
				this.nextPlayer = 'O';
			}
			else {
				this.nextPlayer = 'X';
			}
		}
		
		return this;

	} //end makeMove()
	
	//check win
	public abstract char checkWin(char player, int boardIndex);
	
	public void printGameResult() {
		switch(this.overallGameStatus) {
		case 'X':
			System.err.println("Game Over! X wins in "+ moveCounter +" moves!");
			break;
		case 'O':
			System.err.println("Game Over! O wins in "+ moveCounter +" moves!");
			break;
		case 'd':
			System.err.println("Game Ended in a Draw!");
			break;
		default:
			System.err.println("Error, invalid game status");
		}
	}
	
	//Method to return set of applicable actions in a given state
	//TODO resolve final applicableActions approach, commented this out for now to avoid error
	//public abstract int[][] applicableActions();
//	public int [][] applicableActions(){
//		//each row has vector of applicable actions for corresponding board index following the convention set in TTTBoard
//		int [][] possibleMoves = new int [10][10];
//		
//		for(int i=0; i<3; i++){
//			for(int j=0; j<3; j++) {
//				possibleMoves[i=j+1] = this.boardArray[i][j].applicableActions();
//			}
//		}
//		
//		return possibleMoves;
//	}
	
	public int[][] applicableActions() {
		int boardSentTo = this.nextBoardIndex;
		int[][] possibleMoves = new int [10][10];
		int total = 0;
		//
		if((boardArray[(this.nextBoardIndex-1)/3][(this.nextBoardIndex-1)%3].isBoardFull() == false)) {
			for(int i=0; i<3; i++) {
				for(int j=0; j<3; j++) {
					if(this.boardArray[(this.nextBoardIndex-1)/3][(this.nextBoardIndex-1)%3].mainBoard[i][j] == ' ') {
						possibleMoves[this.nextBoardIndex][TTTBoard.getboardPosition(i, j)] = 1;
						total++;
					}
				}
			}
		}
		
		else {
			//loop through all boards in 3x3 array of boards
			System.out.println("BOARD WAS FULL");
			for(int i=0; i<3; i++) {
				for(int j=0; j<3; j++) {
				//loop through all squares small board
					for(int k=0; k<3; k++) {
						for(int l=0; l<3; l++) {
							if(boardArray[i][j].mainBoard[k][l] == ' ') {
								total++;
								int[] legalMove = {TTTBoard.getboardPosition(i, j), TTTBoard.getboardPosition(k,  l)};
								possibleMoves[TTTBoard.getboardPosition(i, j)][TTTBoard.getboardPosition(k, l)] = 1;
								
							}
						}
					}
					
				}
			}
			
		}
	//	System.out.println(total);
		return possibleMoves;	
	}
	
	//Method to enable cloning of the object
	//Code Source: https://alvinalexander.com/java/java-deep-clone-example-source-code
	/**
	 * This method makes a "deep clone" of any Java object it is given.
	 */
	 public static Object deepClone(Object object) {
	   try {
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(baos);
	     oos.writeObject(object);
	     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	     ObjectInputStream ois = new ObjectInputStream(bais);
	     return ois.readObject();
	   }
	   catch (Exception e) {
	     e.printStackTrace();
	     return null;
	   }
	 }
	
	public boolean isMoveAllowed(int boardIndex, int pos) {
		//if it's the first move, you can play on any board index
		if(this.firstMove) {
			//it's only the first move one time
			this.firstMove = false;
			return true;
		}
		
		
		if((!this.boardArray[(boardIndex-1)/3][(boardIndex-1)%3].isBoardFull()) && ((boardIndex == this.nextBoardIndex) || (this.boardArray[(this.nextBoardIndex-1)/3][(this.nextBoardIndex-1)%3].isBoardFull()))) {
			
			if(this.getBoard(boardIndex).isMoveAllowed(pos)) {
				return true;
			}
			
		}
		
		System.err.println("Invalid move");
		return false;
	}

	
	//main method for testing
	public static void main(String [] args) {
		/*TTT9Board testBoard = new TTT9Board();
		testBoard.displayBoard();
		testBoard.printWinStatus();
		testBoard.makeMove('O', 4, 8);
		testBoard.displayBoard();
		testBoard.makeMove('X', 8, 2);
		testBoard.displayBoard(); */
		
		AdvancedTTTBoard testBoard = new AdvancedTTTBoard();
		testBoard.moveResult('X', 1, 1);
		testBoard.moveResult('X', 1, 2);
		testBoard.moveResult('O', 1, 3);
		testBoard.moveResult('O', 1, 4);
		testBoard.moveResult('X', 1, 5);
		testBoard.moveResult('X', 1, 6);
		testBoard.moveResult('X', 1, 7);
		testBoard.moveResult('O', 1, 8);
		testBoard.moveResult('O', 1, 9);
		testBoard.nextBoardIndex = 1;
		
		System.out.println(testBoard.nextBoardIndex);
		int[][] moves = testBoard.applicableActions();
		for(int i=1; i<10; i++) {
			for(int j=1; j<10; j++) {
				System.out.print(moves[i][j] + " ");
			}
			System.out.println();
		}
		
	}
	
} //end class TTT9Board
