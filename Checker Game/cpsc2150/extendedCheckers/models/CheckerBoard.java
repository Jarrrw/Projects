package cpsc2150.extendedCheckers.models;

import cpsc2150.extendedCheckers.util.DirectionEnum;
import cpsc2150.extendedCheckers.views.CheckersFE;

import java.util.ArrayList;
import java.util.HashMap;
/**
* This class keeps track of the checkerboard and where all the pieces are on the board. The class also keeps track
 * of how many pieces the players have left in hashmaps. It also keeps track of the directions that the pieces can move
 * @invariants [aDimension needs to be an 8, 10, 12, 14, or a 16] AND [the player character must be a letter]
 * @corresponds directions = viableDirections
 *              self = board
 *              numPieces = pieceCount
*/
public class CheckerBoard extends AbsCheckerBoard
{
    /**
     * A 2D array of characters used to represent our checkerboard.
     */
    private char[][] board;

    /**
     * A HashMap, with a Character key and an Integer value, that is used to map a player's char to the number of
     * tokens that player still has left on the board.
     */
    private HashMap<Character, Integer> pieceCount;

    /**
     * A HashMap, with a Character key and an ArrayList of DirectionEnums value, used to map a player (and its king
     * representation) to the directions that player can viably move in. A non-kinged (standard) piece can only move
     * in the diagonal directions away from its starting position. A kinged piece can move in the same directions the
     * standard piece can move in plus the opposite directions the standard piece can move in.
     */
    private HashMap<Character, ArrayList<DirectionEnum>> viableDirections;
    public static final char EMPTY_POS = ' ';
    public static final char BLACK_TILE = '*';

    public final int ROW_NUM;
    public final int COL_NUM;
    public final int STARTING_COUNT = 0;
    /**
     * This function is the constructor function for the Checkerboard class. It creates a board, represented as a 2D
     * array, filled with characters representing the players, black tiles, and blank spaces where appropriate.
     * @param aDimension integer representing the dimensions of the board
     * @pre [aDimension needs to be an 8, 10, 12, 14, or a 16] AND [CheckersFE.getPlayer1() must return a letter] AND
     * [CheckersFE.getPlayer2() must return a letter]
     * @Post [The function will initialize the piece count hashmap and the viable directions hashmap to map the players
     * to their starting directions. The function will also initialize each position on the board to a player
     * character, an asterisk,or a space] AND viableDirections = [#viableDirections with the proper direction mapped
     * to each player] AND pieceCount = [#pieceCount with the players mapped to their starting pieceCount] AND
     * board = [#board with each position mapped to an appropriate character]
     */
    public CheckerBoard(int aDimension) {
        char PLAYER_ONE = CheckersFE.getPlayer1();
        char PLAYER_TWO = CheckersFE.getPlayer2();
        char KINGED_ONE = Character.toUpperCase(PLAYER_ONE);
        char KINGED_TWO = Character.toUpperCase(PLAYER_TWO);
        // Initialize Viable Directions Hashmap
        viableDirections = new HashMap<Character, ArrayList<DirectionEnum>>();
        // Initialize Piece Count Hashmap
        pieceCount = new HashMap<Character, Integer>();
        final int numDirections = 4;
        ROW_NUM = aDimension;
        COL_NUM = aDimension;
        board = new char[ROW_NUM][COL_NUM];

        viableDirections.put(PLAYER_ONE, new ArrayList<DirectionEnum>());
        viableDirections.put(PLAYER_TWO, new ArrayList<DirectionEnum>());
        viableDirections.put(KINGED_TWO, new ArrayList<DirectionEnum>());
        viableDirections.put(KINGED_ONE, new ArrayList<DirectionEnum>());

        addViableDirections(PLAYER_ONE, DirectionEnum.SW);
        addViableDirections(PLAYER_ONE, DirectionEnum.SE);

        addViableDirections(PLAYER_TWO, DirectionEnum.NW);
        addViableDirections(PLAYER_TWO, DirectionEnum.NE);

        addViableDirections(KINGED_TWO, DirectionEnum.NW);
        addViableDirections(KINGED_TWO, DirectionEnum.NE);
        addViableDirections(KINGED_TWO, DirectionEnum.SW);
        addViableDirections(KINGED_TWO, DirectionEnum.SE);

        addViableDirections(KINGED_ONE, DirectionEnum.NW);
        addViableDirections(KINGED_ONE, DirectionEnum.NE);
        addViableDirections(KINGED_ONE, DirectionEnum.SW);
        addViableDirections(KINGED_ONE, DirectionEnum.SE);

        // Adds the starting piece count to the pieceCount hashmap
        pieceCount.put(PLAYER_ONE, STARTING_COUNT);
        pieceCount.put(PLAYER_TWO, STARTING_COUNT);

        for (int i = 0; i < getRowNum(); i++) {
            if (i < (getRowNum() / 2) - 1) {
                for (int j = 0; j < getColNum(); ++j) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0) {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, PLAYER_ONE);
                        } else {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, BLACK_TILE);
                        }
                    } else {
                       if (j % 2 == 0) {
                           BoardPosition b = new BoardPosition(i, j);
                           placePiece(b, BLACK_TILE);
                       } else {
                           BoardPosition b = new BoardPosition(i, j);
                           placePiece(b, PLAYER_ONE);
                       }
                    }
                }
            } else if (i > (getRowNum() / 2)) {
                for (int j = 0; j < getColNum(); ++j) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0) {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, PLAYER_TWO);
                        } else {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, BLACK_TILE);
                        }
                    } else {
                        if (j % 2 == 0) {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, BLACK_TILE);
                        } else {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, PLAYER_TWO);
                        }
                    }
                }
            } else {
                for (int j = 0; j < getColNum(); ++j) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0) {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, EMPTY_POS);
                        } else {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, BLACK_TILE);
                        }
                    } else {
                        if (j % 2 == 0) {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, BLACK_TILE);
                        } else {
                            BoardPosition b = new BoardPosition(i, j);
                            placePiece(b, EMPTY_POS);
                        }
                    }
                }
            }

        }

    }



    

    public HashMap<Character, ArrayList<DirectionEnum>> getViableDirections() {
        return viableDirections;
    }

    public HashMap<Character, Integer> getPieceCounts() {
        return pieceCount;
    }

    public void placePiece(BoardPosition pos, char player) {
        char PLAYER_ONE = CheckersFE.getPlayer1();
        char PLAYER_TWO = CheckersFE.getPlayer2();
        char KINGED_ONE = Character.toUpperCase(PLAYER_ONE);
        char KINGED_TWO = Character.toUpperCase(PLAYER_TWO);
        char prev = board[pos.getRow()][pos.getColumn()];
        if (player == EMPTY_POS) {
            if (Character.toLowerCase(prev) == PLAYER_ONE || Character.toLowerCase(prev) == PLAYER_TWO) {
                playerLostPieces(1, Character.toLowerCase(prev), pieceCount);
            }
        }
        else if (Character.toLowerCase(player) == PLAYER_ONE || Character.toLowerCase(player) == PLAYER_TWO) {
            if (player != Character.toUpperCase(whatsAtPos(pos))) {
                playerLostPieces(-1, Character.toLowerCase(player), pieceCount);
            }
        }

        board[pos.getRow()][pos.getColumn()] = player;

    }

    public char whatsAtPos(BoardPosition pos) {
        return board[pos.getRow()][pos.getColumn()];
    }

    public int getRowNum() {
        return ROW_NUM;
    }

    public int getColNum() {
        return COL_NUM;
    }

}
