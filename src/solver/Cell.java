package solver;
import java.util.HashSet;

public class Cell {
	public int row; 
	public int col; 
	public HashSet<Integer> options; 

	public Cell(int row, int col){
		this.row = row;
		this.col = col;
	}
	
	public boolean hasNext(){
		return row <= 8 && col <= 8; 
	}
	
	public void goNext()
	{
		
		if(col < 8){
			++col;
		}
		else if(col == 8){
			col = 0;
			++row;
		}
		else{
			++row;
		}
	}
	
	public Cell copy(){
		return new Cell(row, col);
	}
	
	private int rowColPartition(int value){
		return value < 3 ? 0 : (value < 6 ? 3 : 6);
	}
	
	public int homeGridStartRow(){
		return rowColPartition(row);
	}
	
	public int homeGridStartCol(){
		return rowColPartition(col);
	}
	
}
