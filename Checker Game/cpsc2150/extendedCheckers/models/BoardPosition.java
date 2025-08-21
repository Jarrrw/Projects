package cpsc2150.extendedCheckers.models;
/**
* This class holds information about a position such as row and column. The class also adds board positions,
 * double board positions, checks to see if a position is valid for the board parameters, and the class returns the
 * position as a string
 * @invariants 0 < col < COL_MAX and 0 < row < ROW_MAX
*/ 
public class BoardPosition
{
    /**
     * Row component of the BoardPosition
     */
    private int row;

    /**
     * Column component of the BoardPosition
     */
    private int column;
    /**
    * The function is the constructor for the board position class
     * @param aRow the row number for the board position
     * @param aCol the column number for the board position
    * @Pre [The board must be generated with a 2D array]
    * @Post [This program sets the row instance variable to aRow parameter and the column instance variable to
     * aCol] AND row = aRow AND column = aCol
    */ 
    public BoardPosition(int aRow, int aCol) {
        this.row = aRow;
        this.column = aCol;
    }
    
    /**
    * This function accesses the row instance variable.
    * @return the value of the row instance variable
    * @pre none
    * @post getRow = [the row that position is located] AND row = #row AND column = #column
    */
    public int getRow() {
        return row;
    }
    /**
     * This function returns the column number of specific board position
     * @return the column number
     * @Pre none
     * @Post getColumn = [the column that the position is located] AND column = #column AND row = #row
     */
    public int getColumn() { return column; }
        /*
        Typical accessor for the column instance variable.
         */

    /**
     * This function adds two board positions together based
     * on the values of the rows and columns
     * @param posOne BoardPosition object representing a board position
     * @param posTwo BoardPosition object representing a board position
     * @return the row and column of two board positions added together
     * @pre none
     * @post add = [the function returns a BoardPosition object with its row and column
     * values being based on the sums found when posOne and posTwo row and column
     * values are added together]
     */
    public static BoardPosition add(BoardPosition posOne, BoardPosition posTwo) {
        /*
        A STATIC function for adding two BoardPositions together, passed in via parameter. This function should return
        a new BoardPosition that contains a row equal to the sum of both parameters' rows, and a column equal to the sum
        of both parameters' columns.
         */
        int newRow = posOne.row + posTwo.row;
        int newCol = posOne.column + posTwo.column;
        return new BoardPosition(newRow, newCol);
    }

    /**
     * This function makes the board position double what it
     * originally was
     * @param pos BoardPosition object representing a board position
     * @return the board position passed in doubled
     * @pre [pos is a valid position]
     * @post doubleBoardPosition = [the function returns a BoardPosition object with its row
     * and column value being doubled]
     */
    public static BoardPosition doubleBoardPosition(BoardPosition pos) {
        /*
        a STATIC function for returning a new Board Position that has a row and column equal to double the row and
        column of the parameter BoardPosition.
         */
        pos = add(pos, pos);
        return pos;
    }

    /**
     * This function determines if the board position
     * is in bounds or not
     * @param rowBound an integer used as the maximum row value
     * @param columnBound an integer used as the maximum column value
     * @return boolean value
     * @pre [the user passes in two integers for the rowBound and columnBound]
     * @post isValid = [the function returns true if BoardPosition is between 0
     * and rowBound in terms of the row and between 0 and columnBound
     * in terms of the column, otherwise the function returns false]
     * row = #row AND column = #column
     */
    public boolean isValid(int rowBound, int columnBound) {
        /*
        returns true or false depending on if this BoardPosition is within the bounds of 0 and rowBound and columnBound
        parameter.
         */
        if((row >= 0 && row < rowBound) && (column >= 0 && column < columnBound)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * This function checks if the board position is
     * equal to the compared object
     * @param obj object used to be compared to BoardPosition
     * @return boolean value
     * @pre [the object passed in must be a valid position on the board.]
     * @post equals = [the function returns true if this BoardPosition
     * object is equal to the parameter object. Otherwise, it equals false.]
     * row = #row AND column = #column
     */
    public boolean equals(Object obj) {
        /*
        returns true if this BoardPosition is equal to the parameter object. Two BoardPositions are equal if their row
        and column vlaues are the same.

        hint: it is intentional that this accepts a parameter of type Object. There is a way to check if that parameter
        Object just happens to be an instance of a BoardPosition.
         */
        if(obj instanceof BoardPosition){
            if(row == ((BoardPosition)obj).row && column == ((BoardPosition)obj).column) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function puts the board position into a
     * formatted string
     * @return string representing BoardPosition
     * @pre [the BoardPosition is valid]
     * @post toString = [the BoardPosition in the string format "row,column"]
     * row = #row AND column = #column
     */
    public String toString() {
        /*
        returns a String representation of the BoardPosition in the format of "row,column"
         */
        return row + "," + column;
    }

}
