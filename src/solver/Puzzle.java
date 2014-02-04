package solver;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

interface CellVisitor
{
	public void visit(int value); 
}

public class Puzzle {
	
	class FindDoublicatesVisitor implements CellVisitor{

		public HashMap<Integer, Integer> valuesCount = new HashMap<>();
		
		@Override
		public void visit(int number) {
			if(number == 0)
				return;
			
			if(valuesCount.containsKey(number))
				valuesCount.put(number, valuesCount.get(number) + 1);
			else
				valuesCount.put(number, 0);
		}
		
		public boolean containsDuplicates(){
			Set<Integer> keys = valuesCount.keySet();
			
			for(int key : keys){
				if(valuesCount.get(key) > 1)
					return true;
			}
			
			return false;
		}
		
		public void reset(){
			valuesCount.clear();
		}
	}
	
	class TakeValueVisitor implements CellVisitor{
		public HashSet<Integer> values = new HashSet<>(9);
		
		{  //init with all possible options
			for(int i = 1; i <= 9; ++i)
				values.add(i);
		}

		@Override
		public void visit(int value) {
			values.remove(value); //it does not matter if 0 is removed
		}
	}
	
	public int unsolvedCellCount(){
		int cnt = 0;
		Cell testCell = new Cell(0, 0);
		while(testCell.hasNext()){
			if(grid[testCell.row][testCell.col] == 0)
				++cnt;
			testCell.goNext();
		}
		return cnt;
	}
	
	public static final int GRID_LEN = 9; 

	//private IntGrid grid;
	private int[][] grid;
	public Puzzle(int[][] grid){
		this.grid = grid; //new IntGrid(grid);
	}
	
	public HashSet<Integer> calcOptions(Cell c)
	{
		TakeValueVisitor vis = new TakeValueVisitor();
		visitOptionCells(c, vis);
		return vis.values;
	}
	
	public Puzzle generate(Cell cellToSet, int value){
		int[][] gridClone = new int[GRID_LEN][];
		for(int i = 0; i < GRID_LEN; ++i){
			gridClone[i] = new int[GRID_LEN];
			for(int j = 0; j < GRID_LEN; ++j){
				gridClone[i][j] = grid[i][j];
			}
		}
		
		//IntGrid modifiedGrid = grid.clone();
		gridClone[cellToSet.row][cellToSet.col] = value;
		return new Puzzle(gridClone);
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int row = 0; row < GRID_LEN; ++row){
			if(row == 3 || row == 6)
				sb.append("\n");
			sb.append("\n");
			for(int col = 0; col < GRID_LEN; ++col){
				if(col == 3 || col == 6)
					sb.append(" ");
				sb.append(grid[row] [col]);
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}

	public boolean isValid()
	{
		//test all columns
		HashSet<Integer> testSet = new HashSet<>(9);
		for(int row = 0; row < GRID_LEN; ++row){
			for(int col = 0; col < GRID_LEN; ++col){
				if(grid[row][col] != 0 && ! testSet.add(grid[row][col]))	
					return false;
			}
			testSet.clear();
		}
		
		testSet.clear();
		
		for(int sCol = 0; sCol < GRID_LEN; sCol += 3){
			for(int sRow = 0; sRow < GRID_LEN; sRow += 3){
				for(int cOffset = 0; cOffset < 2; ++cOffset){
					for(int rOffset = 0; rOffset < 2; ++rOffset){
						int val = grid[sRow + rOffset][sCol + cOffset];
						if(val != 0 && ! testSet.add(val))
							return false;
					}
				}
				testSet.clear();
			}
		}
		
		return true;
	}
	
	public Cell findLeastOptionCell(){
		Cell testCell = new Cell(0, 0); 
		Cell minOptionCell = null;
		int minOptionCnt = Integer.MAX_VALUE;
		
		while(testCell.hasNext()){
			if(grid[testCell.row][testCell.col] != 0){
				testCell.goNext();
				continue;
			}
			
			HashSet<Integer> options = calcOptions(testCell);
			if(options.isEmpty())
				return null; //this puzzle cannot be solved
			else if(options.size() == 1)
			{ //we will not get any better than 1 => return
				minOptionCell = testCell.copy();
				minOptionCell.options = options;
				return minOptionCell;
			}
			else if(options.size() < minOptionCnt) {
				minOptionCnt = options.size();
				minOptionCell = testCell.copy();
				minOptionCell.options = options;
			}
			testCell.goNext();
		}
		
		return minOptionCell;
	}
	
	public void visitOptionCells(Cell cell, CellVisitor visitor){
		//count options for row
		//visit all cells of the cells home grid
		int startRow = cell.homeGridStartRow(); 
		int endRow = startRow + 2;
		int startCol = cell.homeGridStartCol(); 
		int endCol = startCol + 2; 
		
		for(int r = startRow; r <= endRow; ++r){
			for(int c = startCol; c <= endCol; ++c)
			{
				visitor.visit(grid[r][c]);
			}
		}
		
		for(int r = 0; r < startRow; ++r){ //visit all cells in the row
			visitor.visit(grid[r][cell.col]);
		}
		
		for(int r = endRow + 1; r < GRID_LEN; ++r){
			visitor.visit(grid[r][cell.col]);
		}
		
		for(int c = 0; c < startCol; ++c){ //visit all cells in the column
			visitor.visit(grid[cell.row][c]);
		}
		
		for(int c = endCol + 1; c < GRID_LEN; ++c){
			visitor.visit(grid[cell.row][c]);
		}
	}
}
