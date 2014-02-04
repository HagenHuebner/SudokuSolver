package solver;

import java.util.ArrayDeque;

public class Generator {
	private int iterations;
	public int numberOfIterations(){
		return iterations;
	}
	
	public Puzzle solve(int[][] grid)
	{
		ArrayDeque<Puzzle> stack = new ArrayDeque<>();
		Puzzle current = new Puzzle(grid);
		stack.push(current);
		iterations = 0;
		while(! stack.isEmpty()){
			current = stack.pop();
			if(current.unsolvedCellCount() == 0)
				return current;
			Cell nextPermutationCell = current.findLeastOptionCell(); 
			if(nextPermutationCell == null){
				continue;
			}
			for(int option : nextPermutationCell.options){
				//for each possible value generate a new puzzle
				stack.push(current.generate(nextPermutationCell, option));

			}
			++iterations;			
		}
		
		
		return current;
	}
	
}
