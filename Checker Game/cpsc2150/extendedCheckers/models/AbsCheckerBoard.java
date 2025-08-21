package cpsc2150.extendedCheckers.models;

public abstract class AbsCheckerBoard implements ICheckerBoard {

    /**
     * This function converts the whole board with pieces to a string
     * @return the board array as a string
     * @Pre [the pieces must already be set in their proper positions in the array]
     * @Post toString = [the function will construct a string stream of the board and return the string stream]
     * and viableDirections = #viableDirections and pieceCounts = #pieceCounts and board = #board
     */
    public String toString() {
        /*
        returns a String representation of the checkerboard with all the pieces on it and their current positions. there
        should be a "header" line to display all the column numbers and a "header column" that displays all the row
        numbers. In essence, it should look like this:

        |  | 0| 1| 2| 3| 4| 5| 6| 7|
        |0 |x |* |x |* |x |* |x |* |
        |1 |* |x |* |x |* |x |* |x |
        |2 |x |* |x |* |x |* |x |* |
        |3 |* |  |* |  |* |  |* |  |
        |4 |  |* |  |* |  |* |  |* |
        |5 |* |o |* |o |* |o |* |o |
        |6 |o |* |o |* |o |* |o |* |
        |7 |* |o |* |o |* |o |* |o |

        THIS FUNCTION DOES NOT PRINT TO THE CONSOLE OR MAKE ANY KIND OF SYSTEM.OUT.PRINTLN CALLS
         */
        String board = "|  ";
        final int SINGLE_MAX = 9;
        for (int i = 0; i < getColNum(); i++) {
            if (i <= SINGLE_MAX) {
                board += "| ";
            }
            else {
                board += "|";
            }
            board += i;
        }
        board += "|\n";

        for(int i = 0; i < getRowNum(); i++){
            board += "|" + i;
            if (i <= SINGLE_MAX) {
                board += " |";
            }
            else {
                board += "|";
            }
            for(int j = 0; j < getColNum(); j++){
                BoardPosition pos = new BoardPosition(i, j);
                board += whatsAtPos(pos) + " |";
            }
            board += "\n";

        }
        return board;
    }
}
