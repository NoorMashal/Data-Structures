package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife 
{

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {
        StdIn.setFile(file);
        int row=StdIn.readInt();
        int column=StdIn.readInt();
        grid=new boolean[row][column];
        for (int i=0;i<row;i++){
            for (int j=0; j<column;j++){
                grid[i][j]=StdIn.readBoolean();
            }
        }

        // WRITE YOUR CODE HERE
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {
        if (grid[row][col])
        return true;
        return false;
        // WRITE YOUR CODE HERE
         // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {
        
        for (int i=0;i<grid.length;i++){
            for (int j=0; j<grid[0].length;j++){
                if (grid[i][j])
                return true;
            }
        }

        // WRITE YOUR CODE HERE
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        int count = 0;
        for (int i = 0; i < dirs.length; i++) {
            int currRow = row + dirs[i][0];
            int currCol = col + dirs[i][1];
            if (currRow < 0) {
                currRow = grid.length - 1;
            }
            else if (currRow >= grid.length) {
                currRow = 0;
            }

            if (currCol < 0) {
                currCol = grid[0].length - 1;
            }
            else if (currCol >= grid[0].length) {
                currCol = 0;
            }

            if (grid[currRow][currCol]) {
                count++;
            }
        }

        // WRITE YOUR CODE HERE <>
        return count; // update this line, provided so that code compiles
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        for (int i=0;i < grid.length; i++){
            for (int j = 0; j<grid[0].length; j++){
                if (grid[i][j])
                {
                    if (numOfAliveNeighbors(i, j) <= 1 || numOfAliveNeighbors(i, j) >= 4)
                        newGrid[i][j] = false;
                    else
                        newGrid[i][j] = true;
                }
                else
                {
                    if (numOfAliveNeighbors(i, j) == 3)
                        newGrid[i][j] = true;
                }

            }
        }

        // WRITE YOUR CODE HERE
        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
            grid = computeNewGrid();
        // WRITE YOUR CODE HERE
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {
        for(int i = 0; i < n; i++)
            grid = computeNewGrid();
        // WRITE YOUR CODE HERE
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    private void helper(ArrayList<Integer> roots, WeightedQuickUnionUF u, int[][] dirs)
    {
        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[0].length; j++)
            {
                if (grid[i][j])
                {


                    for (int k = 0; k < dirs.length; k++) 
                    {
                        int currRow = i + dirs[k][0];
                        int currCol = j + dirs[k][1];
                        if (currRow < 0) {
                            currRow = grid.length - 1;
                        }
                        else if (currRow >= grid.length) {
                            currRow = 0;
                        }
            
                        if (currCol < 0) {
                            currCol = grid[0].length - 1;
                        }
                        else if (currCol >= grid[0].length) {
                            currCol = 0;
                        }
            
                        if  (grid[currRow][currCol])
                        {
                            if (roots.contains(u.find(currRow, currCol)))
                            {
                                roots.remove(Integer.valueOf(u.find(i, j)));
                                u.union(currRow, currCol, i, j);
                            }
                            else
                            {
                                u.union(i, j, currRow, currCol);
                            }

                        } 
                    }
                    if (!(roots.contains(u.find(i,j))))
                    roots.add(u.find(i, j));
                }

            }
        }
    }

    public int numOfCommunities() 
    {
        ArrayList<Integer> roots = new ArrayList<Integer>();
        WeightedQuickUnionUF u = new WeightedQuickUnionUF(grid.length,grid[0].length);
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    helper(roots, u, dirs);
    roots.clear();
    helper(roots,u,dirs);
    return roots.size();
    }

}


