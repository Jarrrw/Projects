package cpsc2150.extendedCheckers.models;

import cpsc2150.extendedCheckers.util.DirectionEnum;
import cpsc2150.extendedCheckers.views.CheckersFE;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  This class is an implementation of ICheckerboard This class keeps track of the checkerboard and where all the pieces are on
 *  the board. The class also keeps track of how many pieces the players have left in hashmaps. It also keeps track of the
 *  directions that the pieces can move
 * @invariants [aDimension needs to be an 8, 10, 12, 14, or a 16] AND [the player character must be a letter]
 *
 * @corresponds directions = viableDirections
 *              self = board
 *              numPieces = pieceCount
 */

public class CheckerBoardMem extends AbsCheckerBoard {
    /**
     * A hashmap that holds the board positions of the pieces
     */
    private HashMap<Character, ArrayList<BoardPosition>> board;

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
    public final char EMPTY_POS = ' ';
    public final char BLACK_TILE = '*';

    /*
    Standard Checkers starts with 8 rows and 8 columns. Both players begin with 12 pieces each.
     */
    public final int ROW_NUM;
    public final int COL_NUM;
    public final int STARTING_COUNT = 0;

    /**
     * The constructor for CheckerBoardMem. Creates a board represented by a hashmap, storing pieces in key-value
     * pairs. Black tiles and blank spaces are not stored in the hashmap, making it a more memory efficient
     * implementation of ICheckerBoard.
     * @param aDimension an integer that holds the number of rows and columns the board will have
     * @pre [aDimension needs to be an 8, 10, 12, 14, or a 16] AND [CheckersFE.getPlayer1() must return a letter] AND
     * [CheckersFE.getPlayer2() must return a letter]
     * @post [the function will initialize the board hashmap with all the pieces set to their starting
     * position. It will also initialize the pieceCount hashmap to have the starting count for both players.
     * The function also initializes the viable directions hashmap to have each character set to the directions
     * they can move] AND viableDirections = [#viableDirections with each piece set to the directions they can
     * move] AND pieceCount = [#pieceCount with each player set to the starting piece count] AND board = [#board
     * with each character set to their starting position]
     *
     */
    public CheckerBoardMem(int aDimension) {
        char PLAYER_ONE = CheckersFE.getPlayer1();
        char PLAYER_TWO = CheckersFE.getPlayer2();
        char KINGED_ONE = Character.toUpperCase(PLAYER_ONE);
        char KINGED_TWO = Character.toUpperCase(PLAYER_TWO);
        board = new HashMap<>();
        
        pieceCount = new HashMap<Character, Integer>();
        pieceCount.put(PLAYER_ONE, STARTING_COUNT);
        pieceCount.put(PLAYER_TWO, STARTING_COUNT);

        ROW_NUM = aDimension;
        COL_NUM = aDimension;

        viableDirections = new HashMap<Character, ArrayList<DirectionEnum>>();
        viableDirections.put(PLAYER_ONE, new ArrayList<DirectionEnum>());
        viableDirections.put(PLAYER_TWO, new ArrayList<DirectionEnum>());
        viableDirections.put(KINGED_ONE, new ArrayList<DirectionEnum>());
        viableDirections.put(KINGED_TWO, new ArrayList<DirectionEnum>());


        addViableDirections(PLAYER_ONE, DirectionEnum.SW);
        addViableDirections(PLAYER_ONE, DirectionEnum.SE);

        addViableDirections(PLAYER_TWO, DirectionEnum.NW);
        addViableDirections(PLAYER_TWO, DirectionEnum.NE);

        addViableDirections(KINGED_ONE, DirectionEnum.NW);
        addViableDirections(KINGED_ONE, DirectionEnum.NE);
        addViableDirections(KINGED_ONE, DirectionEnum.SW);
        addViableDirections(KINGED_ONE, DirectionEnum.SE);

        addViableDirections(KINGED_TWO, DirectionEnum.NW);
        addViableDirections(KINGED_TWO, DirectionEnum.NE);
        addViableDirections(KINGED_TWO, DirectionEnum.SW);
        addViableDirections(KINGED_TWO, DirectionEnum.SE);



        board.put(PLAYER_ONE, new ArrayList<BoardPosition>());
        board.put(PLAYER_TWO, new ArrayList<BoardPosition>());
        board.put(KINGED_ONE, new ArrayList<BoardPosition>());
        board.put(KINGED_TWO, new ArrayList<BoardPosition>());
        board.put(BLACK_TILE, new ArrayList<BoardPosition>());

        for (int i = 0; i < getRowNum(); i++) {
            for (int j = 0; j < getColNum(); j++) {
                if (i % 2 == 0 && j % 2 != 0) {
                    placePiece(new BoardPosition(i, j), BLACK_TILE);
                }
                else if (i % 2 != 0 && j % 2 == 0) {
                    placePiece(new BoardPosition(i, j), BLACK_TILE);
                }
            }
        }

        for (int i = 0; i < getRowNum(); i++) {
            if (i < (getRowNum() / 2) - 1) {
                if (i % 2 == 0) {
                    for (int j = 0; j < getColNum(); j = j + 2) {
                        BoardPosition b = new BoardPosition(i, j);
                        placePiece(b, PLAYER_ONE);
                    }
                }
                else {
                    for (int j = 1; j < getColNum(); j = j + 2) {
                        BoardPosition b = new BoardPosition(i, j);
                        placePiece(b, PLAYER_ONE);
                    }
                }
            }
            if (i > (getRowNum() / 2)) {
                if (i % 2 == 0) {
                    for (int j = 0; j < getColNum(); j = j + 2) {
                        BoardPosition b = new BoardPosition(i, j);
                        placePiece(b, PLAYER_TWO);
                    }
                }
                else {
                    for (int j = 1; j < getColNum(); j = j + 2) {
                        BoardPosition b = new BoardPosition(i, j);
                        placePiece(b, PLAYER_TWO);
                    }
                }
            }
        }


    }

    public void placePiece(BoardPosition pos, char player) {
        char PLAYER_ONE = CheckersFE.getPlayer1();
        char PLAYER_TWO = CheckersFE.getPlayer2();
        char KINGED_ONE = Character.toUpperCase(PLAYER_ONE);
        char KINGED_TWO = Character.toUpperCase(PLAYER_TWO);
        ArrayList<BoardPosition> play = new ArrayList<>();
        if (player == BLACK_TILE) {
            play = board.get(player);
            play.add(pos);
            board.put(player, play);
        }

        if (player == EMPTY_POS) {
            if (whatsAtPos(pos) != BLACK_TILE) {
                playerLostPieces(1, Character.toLowerCase(whatsAtPos(pos)), pieceCount);
            }
            play = board.get(whatsAtPos((pos)));
            play.remove(pos);

        }
        else if (player == PLAYER_ONE || player == PLAYER_TWO) {
            playerLostPieces(-1, Character.toLowerCase(player), pieceCount);

            play = board.get(player);

            play.add(pos);
            board.put(player, play);

        }
        else if (player == KINGED_ONE || player == KINGED_TWO) {
            if (whatsAtPos(pos) == Character.toLowerCase(player)) {
                play = board.get(Character.toLowerCase(player));
                play.remove(pos);
                playerLostPieces(1, Character.toLowerCase(player), pieceCount);
            }
            playerLostPieces(-1, Character.toLowerCase(player), pieceCount);
            play = board.get(player);
            play.add(pos);
        }
    }
    public HashMap<Character, Integer> getPieceCounts() {
        return pieceCount;
    }


    public HashMap<Character, ArrayList<DirectionEnum>> getViableDirections() {
        return viableDirections;
    }


    public char whatsAtPos(BoardPosition pos) {
        char PLAYER_ONE = CheckersFE.getPlayer1();
        char PLAYER_TWO = CheckersFE.getPlayer2();
        char KINGED_ONE = Character.toUpperCase(PLAYER_ONE);
        char KINGED_TWO = Character.toUpperCase(PLAYER_TWO);
        char[] player = {PLAYER_ONE, PLAYER_TWO, KINGED_ONE, KINGED_TWO, BLACK_TILE};

        for (int i = 0; i < player.length; i++) {
            ArrayList<BoardPosition> positions = board.get(player[i]);
            if (positions.contains(pos)) {
                return player[i];
            }
        }
        return EMPTY_POS;
    }

    public int getRowNum() {
        return ROW_NUM;
    }

    public int getColNum() {
        return COL_NUM;
    }
}
