package core.components;

import utilities.Utils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class GridBoard<T> extends Component {

    private int width;  // Width of the board
    private int height;  // Height of the board

    private T[][] grid;  // 2D grid representation of this board
    private final Class<T> typeParameterClass;  // Type of this class

    @SuppressWarnings({"unchecked"})
    private GridBoard(int width, int height, Class<T> typeParameterClass){
        super(Utils.ComponentType.BOARD);
        this.width = width;
        this.height = height;
        this.typeParameterClass = typeParameterClass;
        this.grid = (T[][])Array.newInstance(typeParameterClass, width, height);
    }

    @SuppressWarnings({"unchecked"})
    public GridBoard(int width, int height, Class<T> typeParameterClass, T defaultValue){
        this(width, height, typeParameterClass);
        for (int x = 0; x < width; x++)
            Arrays.fill(grid[x], defaultValue);
    }

    public GridBoard(T[][] grid, Class<T> typeParameterClass){
        super(Utils.ComponentType.BOARD);
        this.width = grid.length;
        this.height = grid[0].length;
        this.grid = grid;
        this.typeParameterClass = typeParameterClass;
    }

    public GridBoard(T[][] grid, Class<T> typeParameterClass, int ID){
        super(Utils.ComponentType.BOARD, ID);
        this.width = grid.length;
        this.height = grid[0].length;
        this.grid = grid;
        this.typeParameterClass = typeParameterClass;
    }

    public GridBoard(GridBoard<T> orig){
        super(Utils.ComponentType.BOARD);
        this.width = orig.getWidth();
        this.height = orig.getHeight();
        this.grid = orig.grid.clone();
        this.typeParameterClass = orig.typeParameterClass;
    }

    /**
     * Get the width and height of this grid.
     */
    public int getWidth(){return width; }
    public int getHeight(){return height; }

    /**
     * Sets the element at position (x, y).
     * @param x - x coordinate in the grid.
     * @param y - y coordinate in the grid.
     * @param value - new value for this element.
     * @return - true if coordinates in bounds, false otherwise (and function fails).
     */
    public boolean setElement(int x, int y, T value){
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[x][y] = value;
            return true;
        }
        else
            return false;
    }

    /**
     * Retrieves the element at position (x, y).
     * @param x - x coordinate in the grid.
     * @param y - y coordinate in the grid.
     * @return - element at (x,y) in the grid.
     */
    public T getElement(int x, int y)
    {
        if (x >= 0 && x < width && y >= 0 && y < height)
            return grid[x][y];
        return null;
    }

    /**
     * Retrieves the grid.
     * @return - 2D grid.
     */
    public T[][] getGridValues(){
        return grid;
    }

    @Override
    public GridBoard<T> copy() {
        GridBoard<T> g = new GridBoard<>(grid, typeParameterClass, componentID);
        copyComponentTo(g);
        return g;
    }
}