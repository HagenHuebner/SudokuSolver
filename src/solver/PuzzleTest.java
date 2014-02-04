package solver;

import java.util.Arrays;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class PuzzleTest {

	private static final int[][] SIMPLE = {{0, 0, 0,  2, 6, 0,  7, 0, 1},
								   		   {6, 8, 0,  0, 7, 0,  0, 9, 0},
								   		   {1, 9, 0,  0, 0, 4,  5, 0, 0},
								 
								   		   {8, 2, 0,  1, 0, 0,  0, 4, 0},
								   		   {0, 0, 4,  6, 0, 2,  9, 0, 0},
								   		   {0, 5, 0,  0, 0, 3,  0, 2, 8},
								 
								   		   {0, 0, 9,  3, 0, 0,  0, 7, 4},
								   		   {0, 4, 0,  0, 5, 0,  0, 3, 6},
								   		   {7, 0, 3,  0, 1, 8,  0, 0, 0}};
	
	private static final int[][] INTERMEDIATE = {{0, 2, 0,  6, 0, 8,  0, 0, 0},
		     								     {5, 8, 0,  0, 0, 9,  7, 0, 0},
		     									 {0, 0, 0,  0, 4, 0,  0, 0, 0},

		     									 {3, 7, 0,  0, 0, 0,  5, 0, 0},
		     									 {6, 0, 0,  0, 0, 0,  0, 0, 4},
		     									 {0, 0, 8,  0, 0, 0,  0, 1, 3},

		     									 {0, 0, 0,  0, 2, 0,  0, 0, 0},
		     									 {0, 0, 9,  8, 0, 0,  0, 3, 6},
		     									 {0, 0, 0,  3, 0, 6,  0, 9, 0}};
												
	private static final int[][] HARD = {{0, 2, 0,  0, 0, 0,  0, 0, 0},
		   							     {0, 0, 0,  6, 0, 0,  0, 0, 3},
		   							     {0, 7, 4,  0, 8, 0,  0, 0, 0},

		   							     {0, 0, 0,  0, 0, 3,  0, 0, 2},
		   							     {0, 8, 0,  0, 4, 0,  0, 1, 0},
		   							     {6, 0, 0,  5, 0, 0,  0, 0, 0},

		   							     {0, 0, 0,  0, 1, 0,  7, 8, 0},
		   							     {5, 0, 0,  0, 0, 9,  0, 0, 0},
		   							     {0, 0, 0,  0, 0, 0,  0, 4, 0}};
	
	private static final int[][] HARDEST = {{8, 0, 0,  0, 0, 0,  0, 0, 0},
											{0, 0, 3,  6, 0, 0,  0, 0, 0}, 
											{0, 7, 0,  0, 9, 0,  2, 0, 0}, 
									
											{0, 5, 0,  0, 0, 7,  0, 0, 0}, 
											{0, 0, 0,  0, 4, 5,  7, 0, 0}, 
											{0, 0, 0,  1, 0, 0,  0, 3, 0}, 
									
											{0, 0, 1,  0, 0, 0,  0, 6, 8}, 
											{0, 0, 8,  5, 0, 0,  0, 1, 0}, 
											{0, 9, 0,  0, 0, 0,  4, 0, 0}}; 
	
	
	
	
	@Before
	public void setUp() throws Exception {
	}
	
	public static int[][] cloneArray(int[][] array){
		int[][] ret = new int[array.length][array.length]; 
		for(int i = 0; i < array.length; ++i){
			ret[i] = Arrays.copyOf(array[i], array.length);
		}
		return ret; 
	}
	
	private void expectOptions(HashSet<Integer> result, int... expected)
	{
		HashSet<Integer> cmp = new HashSet<>();
		for(int i : expected)
			cmp.add(i);
		
		Assert.assertEquals(cmp, result);
		
	}
	
	@Test
	public void optionsAreCalculatedCorrectly() {
		Puzzle p = new Puzzle(cloneArray(SIMPLE)); 
		HashSet<Integer> optionsR0C0 = new HashSet<>();
		optionsR0C0.add(4);
		optionsR0C0.add(5);
		optionsR0C0.add(3);
		
		HashSet<Integer> resR0C0 = p.calcOptions(new Cell(0, 0));	
		Assert.assertEquals(resR0C0, optionsR0C0);
		
		HashSet<Integer> optionsR9C9 = new HashSet<>();
		optionsR9C9.add(5);
		optionsR9C9.add(2);
		optionsR9C9.add(9);
		
		Assert.assertEquals(p.calcOptions(new Cell(8, 8)), optionsR9C9);
		
		//for intermediate puzzle
		Puzzle intermediate = new Puzzle(cloneArray(INTERMEDIATE));
		HashSet<Integer> iresR5C1 = intermediate.calcOptions(new Cell(5, 1));
		expectOptions(iresR5C1, 4, 5, 9);
		expectOptions(intermediate.calcOptions(new Cell(4, 7)), 2, 7, 8);
		expectOptions(intermediate.calcOptions(new Cell(4, 4)), 1, 3, 5, 7, 8, 9);
	}
	
	@Test
	public void leastOptionCellIsFound()
	{
		Puzzle p = new Puzzle(cloneArray(SIMPLE));
		Cell loCell = p.findLeastOptionCell();
		Assert.assertTrue(loCell != null);
		HashSet<Integer> options = p.calcOptions(loCell);
		Assert.assertTrue(options.size() == 1);
	}

	
	@Test
	public void correctPuzzleValidatesCorrectly(){
		Puzzle p = new Puzzle(cloneArray(SIMPLE));
		Assert.assertTrue(p.isValid());
	}
	
	@Test 
	public void canSolve(){
		Generator gen = new Generator();
		
		//intermediate
		Puzzle solItermediate = gen.solve(cloneArray(INTERMEDIATE));
		System.out.println("INTERMEDIATE");
		System.out.println(solItermediate);
		System.out.println("took " + gen.numberOfIterations() + " iterations");
		
		
		//hard
		Puzzle testHard = new Puzzle(cloneArray(HARD));
		Assert.assertTrue(testHard.isValid());
		Puzzle solutionHard = gen.solve(cloneArray(HARD));
		Assert.assertTrue(solutionHard.isValid());
		System.out.println("HARD");
		System.out.println(solutionHard);
		System.out.println("took " + gen.numberOfIterations() + " iterations");
	
		Puzzle testHardest = new Puzzle(cloneArray(HARDEST));
		Assert.assertTrue(testHardest.isValid());

	    Puzzle	solutionsHardest = gen.solve(cloneArray(HARDEST));
		Assert.assertTrue(solutionsHardest.isValid());
		System.out.println("HARDEST");
		System.out.println(solutionsHardest);
		System.out.println("took " + gen.numberOfIterations() + " iterations");
	}
}
