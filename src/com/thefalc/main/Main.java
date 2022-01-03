package com.thefalc.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

class Board {
	public static int rows = 15;
	public static int cols = 20;
	
	public static int PEG = 1;
	public static int EMPTY = 2;
	
	List<Board> previous = new ArrayList<Board>();
	
	int board[][] = new int[rows][cols];
	int moves = 0;
	
	int lastR, lastC;
	
	public Board(int board[][], int moves) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				this.board[i][j] = board[i][j];
			}
		}
		this.moves = moves;
	}
	
	public Board(int board[][], int moves, List<Board> previous, int lastR, int lastC) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				this.board[i][j] = board[i][j];
			}
		}
		this.moves = moves;
		this.previous.addAll(previous);
		this.lastR = lastR;
		this.lastC = lastC;
	}
	
	public boolean isWin() {
		boolean found = false;
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(board[i][j] == Board.PEG) {
					if(found) return false;
					
					// final peg in starting hole
					if(i != Main.initR || j != Main.initC) return false;
					
					found = true;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Given the current game state, attempts all possible legal moves and adds
	 * those to the list of next possible game boards.
	 * @return The list of next moves.
	 */
	public List<Board> makeMoves() {
		List<Board> nextBoards = new ArrayList<Board>();
		
		for(int i = 1; i <= 6; i++) {
			for(int j = 1; j <= 10; j++) {
				if(board[i][j] == 1) {
					int next[][] = null;
					int jumpLocR = 0, jumpLocC = 0;
					
					// diagonal down right
					if(board[i+1][j+1] == PEG && board[i+2][j+2] == EMPTY) {
						next = createCopy();
						next[i+1][j+1] = EMPTY;
						next[i+2][j+2] = PEG;
						next[i][j] = EMPTY;
						
						jumpLocR = i + 2;
						jumpLocC = j + 2;
					}
					
					// diagonal down left
					if(board[i+1][j-1] == PEG && board[i+2][j-2] == EMPTY) {
						next = createCopy();
						next[i+1][j-1] = EMPTY;
						next[i+2][j-2] = PEG;
						next[i][j] = EMPTY;
						
						jumpLocR = i + 2;
						jumpLocC = j - 2;
					}
					
					// diagonal up right
					if(board[i-1][j+1] == PEG && board[i-2][j+2] == EMPTY) {
						next = createCopy();
						next[i-1][j+1] = EMPTY;
						next[i-2][j+2] = PEG;
						next[i][j] = EMPTY;
						
						jumpLocR = i - 2;
						jumpLocC = j + 2;
					}
					
					// diagonal up left
					if(board[i-1][j-1] == PEG && board[i-2][j-2] == EMPTY) {
						next = createCopy();
						next[i-1][j-1] = EMPTY;
						next[i-2][j-2] = PEG;
						next[i][j] = EMPTY;
						
						jumpLocR = i - 2;
						jumpLocC = j - 2;
					}
					
					// left move
					if(board[i][j-2] == PEG && board[i][j-4] == EMPTY) {
						next = createCopy();
						next[i][j-2] = EMPTY;
						next[i][j-4] = PEG;
						next[i][j] = EMPTY;
						
						jumpLocR = i;
						jumpLocC = j - 4;
					}
					
					// right move
					if(board[i][j+2] == PEG && board[i][j+4] == EMPTY) {
						next = createCopy();
						next[i][j+2] = EMPTY;
						next[i][j+4] = PEG;
						next[i][j] = EMPTY;
						
						jumpLocR = i;
						jumpLocC = j + 4;
					}
					
					if(next != null) {
						Board b = new Board(next, moves + 1, this.previous, jumpLocR, jumpLocC);
						if(i == lastR && j == lastC) {
							b.moves--;
						}
						
						b.previous.add(this);
						nextBoards.add(b);
					}
				}
			}
		}
		
		return nextBoards;
	}
	
	public int hashCode() { 
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				sb.append(board[i][j] + "");
			}
		}

		return sb.toString().hashCode();
	}

	public boolean equals(Object o) {
		Board board = (Board)o;
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(board.board[i][j] != this.board[i][j]) return false;
			}
		}
		
		return true;
	}
	
	private int[][] createCopy() {
		int copy[][] = new int[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				copy[i][j] = this.board[i][j];
			}
		}
		
		return copy;
	}
	
	public void print() {
		for(int i = 1; i < 6; i++) {
			for(int j = 2; j < 11; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
}

public class Main {
	public static int initR = 0;
	public static int initC = 0;
	
	// A* algorithm to solve Peg Solitaire
	public static void solve(Board board) {
		Set<Board> seen = new HashSet<Board>();
		
		PriorityQueue<Board> queue = new PriorityQueue<Board>(new Comparator<Board>() {
			public int compare(Board x, Board y) {
				if(x.moves < y.moves) return -1;
				if(x.moves > y.moves) return 1;
				return 0;
			}
		});
		queue.add(board);
		
		boolean win = false;
		
		while(!queue.isEmpty()) {
			Board current = queue.poll();
			
			if(seen.contains(current)) continue;
			
			if(current.isWin()) {
				win = true;
				System.out.println("Won in " + current.moves + " moves.");
				
				break;
			}

			seen.add(current);
			
			List<Board> nextBoards = current.makeMoves();
			for(Board b : nextBoards) {
				queue.add(b);
			}
		}

		if(!win) {
			System.out.println("Win is impossible.");
		}
	}

	public static void main(String[] args) {
		// initial board
		int game[][] = new int[Board.rows][Board.cols];
		
		game[1][6] = Board.PEG; 
		game[2][5] = Board.PEG; game[2][7] = Board.PEG;
		game[3][4] = Board.PEG; game[3][6] = Board.PEG; game[3][8] = Board.PEG;
		game[4][3] = Board.PEG; game[4][5] = Board.PEG;
		game[4][7] = Board.PEG; game[4][9] = Board.PEG;
		game[5][2] = Board.PEG; game[5][4] = Board.PEG;
		game[5][6] = Board.PEG; game[5][8] = Board.PEG; game[5][10] = Board.PEG;
		
		initR = 3; initC = 6;
		
//		Board board = new Board(game, 0);
//		board.print();
//		
//		solve(board);
		
		// Trying to solve from all possible starting positions
		for(int i = 1; i <= 15; i++) {
			int total = 0;
			for(int r = 0; r < Board.rows; r++) {
				for(int c = 0; c < Board.cols; c++) {
					if(game[r][c] == Board.PEG) total++;
					
					if(total == i) {
						Board board = new Board(game, 0);
						board.board[r][c] = 2;
						initR = r;
						initC = c;
	
						total++;
						
						solve(board);
					}
				}
			}
		}
	}
}
