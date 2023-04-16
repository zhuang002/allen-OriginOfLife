import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

	static int m,n,a,b,c;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		m=sc.nextInt();
		n=sc.nextInt();
		a=sc.nextInt();
		b=sc.nextInt();
		c=sc.nextInt();
		sc.nextLine();
		
		Board board = new Board(m,n);
		board.readIn(sc);
		sc.close();
		
		System.out.println(board.getShortestPrevious());
		
	}

}


class Board {
	char[][] map;

	
	public Board(int m, int n) {
		map = new char[m][n];
	}

	public int getShortestPrevious() {
		// TODO Auto-generated method stub
		ArrayList<Board> current = new ArrayList<>();
		current.add(this);
		ArrayList<Board> next = new ArrayList<>();
		int step = 0;
		Set<Board> usedBoards = new HashSet<>();
		
		while (!current.isEmpty()) {
			for (Board b:current) {
				usedBoards.add(b);
				ArrayList<Board> preBoards = b.getPreBoards(usedBoards);
				if (preBoards.isEmpty()) {
					return step;
				}
				next.addAll(preBoards);
			}
			step++;
			current = next;
			next = new ArrayList<>();
		}
		
		
		return -1;
	}

	private ArrayList<Board> getPreBoards(Set<Board> usedBoards) {
		// TODO Auto-generated method stub
		ArrayList<Board> current = new ArrayList<>();
		ArrayList<Board> next = new ArrayList<>();
		Board firstBoard = new Board(map.length,map[0].length);
		current.add(firstBoard);
		
		for (int i=0;i<map.length;i++) {
			for (int j=0;j<map[0].length;j++) {
				for (Board b:current) {
					b = trySymbol('.',i,j,b);
					if (b!=null) {
						b = b.clone(i,j);
						if (i!=map.length-1 || j!=map[0].length-1 || !usedBoards.contains(b))
							next.add(b);
					}
					b = trySymbol('*',i,j,b);
					if (b!=null) {
						if (i!=map.length-1 || j!=map[0].length-1 || !usedBoards.contains(b))
							next.add(b);
					}
				}
				current = next;
				next = new ArrayList<>();
			}
		}
		
		return current;
	}

	private Board trySymbol(char symb, int row, int col, Board board) {
		// TODO Auto-generated method stub
		board.map[row][col] = symb;
		if (row==0) {
			return board;
		} 
		int r=row-1;
		int c=col-1;
		if (inBoundary(r,c)) {
			if (check(board,r,c)) {
				return board;
			} else {
				return null;
			}
		}
		if (col==map[0].length) {
			r=row-1;
			c = col;
			if (check(board,r,c)) {
				return board;
			} else {
				return null;
			}
		}
		
		if (row == map.length) {
			r=row;
			c = col-1;
			if (inBoundary(r,c)) {
				if (check(board,r,c)) {
					return board;
				} else {
					return null;
				}
			}
		}
		if (row==map.length && col==map[0].length) {
			if (check(board,row,col)) {
				return board;
			} else {
				return null;
			}
		}
		return null;
	}

	private boolean check(Board board, int row, int col) {
		// TODO Auto-generated method stub
		int countLive = 0;
		for (int i=row-1;i<row+1;i++) {
			for (int j=col-1;j<col+1;j++) {
				if (i==row && col==j) {
					continue;
				}
				if (!inBoundary(i,j))
					continue;
				if (board.map[i][j]=='*') {
					countLive++;
				}
			}
		}
		if (board.map[row][col] == '.') {
			if (map[row][col]=='.') {
				if (countLive>Main.c) {
					return false;
				} else {
					return true;
				}
			} else { // current is '*'
				if (countLive>Main.c) {
					return true;
				} else {
					return false;
				}
			}
		} else { //prev is '*'
			if (map[row][col]=='.') {
				if (countLive<Main.a || countLive>Main.b) {
					return true;
				} else {
					return false;
				}
			} else { // current is '*'
				if (countLive<Main.a || countLive>Main.b) {
					return false;
				} else {
					return true;
				}
			}
		}
	}

	private boolean inBoundary(int r, int c) {
		// TODO Auto-generated method stub
		return r>=0 && r<map.length && c>=0 && c<map[0].length;
	}

	private Board clone(int r, int c) {
		// TODO Auto-generated method stub
		Board rt = new Board(map.length,map[0].length);
		for (int i=0;i<r;i++) {
			for (int j=0;j<c;j++) {
				rt.map[i][j] = map[i][j];
			}
		}
		return rt;
	}

	public void readIn(Scanner sc) {
		// TODO Auto-generated method stub
		for (int i=0;i<map.length;i++) {
			String line = sc.nextLine();
			for (int j=0;j<map[0].length;j++) {
				map[i][j]=line.charAt(j);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(map);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.deepEquals(map, other.map))
			return false;
		return true;
	}
	
	
}