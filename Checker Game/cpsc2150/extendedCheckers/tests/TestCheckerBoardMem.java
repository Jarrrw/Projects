package cpsc2150.extendedCheckers.tests;
import cpsc2150.extendedCheckers.models.BoardPosition;
import cpsc2150.extendedCheckers.models.CheckerBoardMem;
import cpsc2150.extendedCheckers.models.ICheckerBoard;
import cpsc2150.extendedCheckers.util.DirectionEnum;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static cpsc2150.extendedCheckers.models.ICheckerBoard.getDirection;
import static org.junit.Assert.*;




public class TestCheckerBoardMem {
    private CheckerBoardMem makeBoard(int size) {
        return new CheckerBoardMem(size);
    }

    private char[][] boardWithPieces(int size) {
        char[][] board = new char[size][size];
        for (int i = 0; i < size; i++) {
            if (i < (size / 2) - 1) {
                for (int j = 0; j < size; ++j) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0) {
                            board[i][j] = 'x';
                        } else {
                            board[i][j] = '*';
                        }
                    } else {
                        if (j % 2 == 0) {
                            board[i][j] = '*';
                        } else {
                            board[i][j] = 'x';
                        }
                    }
                }
            } else if (i > (size / 2)) {
                for (int j = 0; j < size; ++j) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0) {
                            board[i][j] = 'o';
                        } else {
                            board[i][j] = '*';
                        }
                    } else {
                        if (j % 2 == 0) {
                            board[i][j] = '*';
                        } else {
                            board[i][j] = 'o';
                        }
                    }
                }
            } else {
                for (int j = 0; j < size; ++j) {
                    if (i % 2 == 0) {
                        if (j % 2 == 0) {
                            board[i][j] = ' ';
                        } else {
                            board[i][j] = '*';
                        }
                    } else {
                        if (j % 2 == 0) {
                            board[i][j] = '*';
                        } else {
                            board[i][j] = ' ';
                        }
                    }
                }
            }

        }

        return board;
    }

    private String convString(char[][] array, int size) {
        String board = "|  ";
        final int SINGLE_MAX = 9;
        for (int i = 0; i < size; i++) {
            if (i <= SINGLE_MAX) {
                board += "| ";
            }
            else {
                board += "|";
            }
            board += i;
        }
        board += "|\n";

        for(int i = 0; i < size; i++){
            board += "|" + i;
            if (i <= SINGLE_MAX) {
                board += " |";
            }
            else {
                board += "|";
            }
            for(int j = 0; j < size; j++){
                board += array[i][j] + " |";
            }
            board += "\n";

        }
        return board;
    }
    @Test
    public void testCheckerBoard_Con_8x8() {
        int size = 8;
        ICheckerBoard cb = makeBoard(size);
        char[][] board = boardWithPieces(size);

        String observed = cb.toString();
        String expected = convString(board, size);
        assertEquals(expected,observed);

    }
    @Test
    public void testCheckerBoard_Con_12x12() {
        int size = 12;
        ICheckerBoard cb = makeBoard(size);
        char[][] board = boardWithPieces(size);


        String observed = cb.toString();
        String expected = convString(board, size);
        assertEquals(expected,observed);

    }
    @Test
    public void testCheckerBoard_Con_16x16() {
        int size = 16;
        ICheckerBoard cb = makeBoard(size);
        char[][] board = boardWithPieces(size);

        String observed = cb.toString();
        String expected = convString(board, size);
        assertEquals(expected,observed);
    }

    @Test
    public void testGetColNum_Size_8x8() {
        int expected = 8;
        ICheckerBoard cb = makeBoard(expected);
        int observed = cb.getColNum();
        assertEquals(observed, expected);
    }

    @Test
    public void testScanSurroundingPositions_Row_7_Col_7() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(7,7);

        HashMap<DirectionEnum, Character> expected = new HashMap<DirectionEnum,Character>();
        expected.put(DirectionEnum.NW, 'o');
        HashMap<DirectionEnum, Character> observed = cb.scanSurroundingPositions(startingPos);

        assertEquals(expected,observed);

    }
    @Test
    public void testScanSurroundingPositions_Row_0_Col_0() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(0,0);

        HashMap<DirectionEnum, Character> expected = new HashMap<DirectionEnum,Character>();
        expected.put(DirectionEnum.SE, 'x');
        HashMap<DirectionEnum, Character> observed = cb.scanSurroundingPositions(startingPos);

        assertEquals(expected,observed);
    }

    @Test
    public void testScanSurroundingPositions_Row_1_Col_3() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(1,3);

        HashMap<DirectionEnum, Character> expected = new HashMap<DirectionEnum,Character>();
        expected.put(DirectionEnum.NW, 'x');
        expected.put(DirectionEnum.NE, 'x');
        expected.put(DirectionEnum.SE, 'x');
        expected.put(DirectionEnum.SW, 'x');
        HashMap<DirectionEnum, Character> observed = cb.scanSurroundingPositions(startingPos);

        assertEquals(expected,observed);

    }
    @Test
    public void testScanSurroundingPositions_Row_0_Col_7() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(0,7);

        HashMap<DirectionEnum, Character> expected = new HashMap<DirectionEnum,Character>();
        expected.put(DirectionEnum.SW, '*');
        HashMap<DirectionEnum, Character> observed = cb.scanSurroundingPositions(startingPos);

        assertEquals(expected,observed);

    }
    @Test
    public void testScanSurroundingPositions_Row_2_Col_0() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(2,0);

        HashMap<DirectionEnum, Character> expected = new HashMap<DirectionEnum,Character>();
        expected.put(DirectionEnum.NE, 'x');
        expected.put(DirectionEnum.SE, ' ');
        HashMap<DirectionEnum, Character> observed = cb.scanSurroundingPositions(startingPos);

        assertEquals(expected,observed);

    }
    @Test
    public void testAddViableDirections_player_x_SW_() {

        ICheckerBoard cb = makeBoard(8);
        HashMap<Character, ArrayList<DirectionEnum>> vd = cb.getViableDirections();
        ArrayList<DirectionEnum> xdir = vd.get('x');
        xdir.clear();
        vd.put('x', xdir);
        // xdir is empty
        assertEquals(xdir, new ArrayList<DirectionEnum>());

        cb.addViableDirections('x', DirectionEnum.SW);
        vd = cb.getViableDirections();
        ArrayList<DirectionEnum> observed = vd.get('x');
        ArrayList<DirectionEnum> expected = new ArrayList<>();
        expected.add(DirectionEnum.SW);

        assertEquals(expected, observed);

    }

    @Test
    public void testWhatsAtPos_MinRow_MaxCol_Black_Tile(){
        ICheckerBoard cb = makeBoard(8);
        BoardPosition pos = new BoardPosition(0, 7);
        Character observed = cb.whatsAtPos(pos);
        Character expected = '*';
        assertEquals(expected, observed);
    }

    @Test
    public void testWhatsAtPos_MaxRow_MaxCol_Player_Piece_o(){
        ICheckerBoard cb = makeBoard(8);
        BoardPosition pos = new BoardPosition(7, 7);
        Character observed = cb.whatsAtPos(pos);
        Character expected = 'o';
        assertEquals(expected, observed);
    }

    @Test
    public void testWhatsAtPos_MaxRow_MinCol_Black_Tile(){
        ICheckerBoard cb = makeBoard(8);
        BoardPosition pos = new BoardPosition(7, 0);
        Character observed = cb.whatsAtPos(pos);
        Character expected = '*';
        assertEquals(expected, observed);
    }

    @Test
    public void testWhatsAtPos_MinRow_MinCol_Player_Piece_x(){
        ICheckerBoard cb = makeBoard(8);
        BoardPosition pos = new BoardPosition(0, 0);
        Character observed = cb.whatsAtPos(pos);
        Character expected = 'x';
        assertEquals(expected, observed);
    }

    @Test
    public void testWhatsAtPos_Row4_Col4_Empty_Space(){
        ICheckerBoard cb = makeBoard(8);
        BoardPosition pos = new BoardPosition(4, 4);
        Character observed = cb.whatsAtPos(pos);
        Character expected = ' ';
        assertEquals(expected, observed);
    }

    @Test
    public void testGetPieceCounts_Num_Of_Pieces_12(){
        ICheckerBoard cb = makeBoard(8);

        HashMap<Character, Integer> observedPieces = cb.getPieceCounts();

        HashMap<Character, Integer> expected = new HashMap<Character,Integer>();
        expected.put('x', 12);
        expected.put('o', 12);

        assertEquals(expected, observedPieces);

    }

    @Test
    public void testGetRowNum_Size_8x8() {
        ICheckerBoard board = makeBoard(8);
        int expected = 8;
        int observed = board.getRowNum();
        assertEquals(expected, observed);
    }

    @Test
    public void testGetDirection_SE() {
        BoardPosition observed = getDirection(DirectionEnum.SE);
        BoardPosition expected = new BoardPosition(1, 1);
        assertEquals(expected, observed);
    }

    @Test
    public void testGetViableDirections() {
        ICheckerBoard board = makeBoard(8);
        HashMap<Character, ArrayList<DirectionEnum>> observed = board.getViableDirections();
        HashMap<Character, ArrayList<DirectionEnum>> expected = new HashMap<Character, ArrayList<DirectionEnum>>();

        ArrayList<DirectionEnum> normalX = new ArrayList<DirectionEnum>();
        ArrayList<DirectionEnum> kingX = new ArrayList<DirectionEnum>();
        ArrayList<DirectionEnum> normalO = new ArrayList<DirectionEnum>();
        ArrayList<DirectionEnum> kingO = new ArrayList<DirectionEnum>();

        normalX.add(DirectionEnum.SW);
        normalX.add(DirectionEnum.SE);

        normalO.add(DirectionEnum.NW);
        normalO.add(DirectionEnum.NE);

        kingX.add(DirectionEnum.NW);
        kingX.add(DirectionEnum.NE);
        kingX.add(DirectionEnum.SW);
        kingX.add(DirectionEnum.SE);

        kingO.add(DirectionEnum.NW);
        kingO.add(DirectionEnum.NE);
        kingO.add(DirectionEnum.SW);
        kingO.add(DirectionEnum.SE);

        expected.put('x', normalX);
        expected.put('o', normalO);
        expected.put('X', kingX);
        expected.put('O', kingO);

        assertEquals(expected, observed);
    }

    @Test
    public void testCrownPiece_Size_8x8_Row_0_Col_0() {
        ICheckerBoard board = makeBoard(8);
        BoardPosition posOfPlayer = new BoardPosition(0, 0);
        board.crownPiece(posOfPlayer);
        char observed = board.whatsAtPos(posOfPlayer);
        char expected = 'X';
        assertEquals(observed, expected);
    }

    @Test
    public void testCrownPiece_Size_16x16_Row_15_Col_15() {
        ICheckerBoard board = makeBoard(16);
        BoardPosition posOfPlayer = new BoardPosition(15, 15);
        board.crownPiece(posOfPlayer);
        char observed = board.whatsAtPos(posOfPlayer);
        char expected = 'O';
        assertEquals(observed, expected);
    }

    @Test
    public void testCrownPiece_Size_8x8_blackTile_Row_3_Col_0() {
        ICheckerBoard board = makeBoard(8);
        BoardPosition posOfPlayer = new BoardPosition(3, 0);
        board.crownPiece(posOfPlayer);
        char observed = board.whatsAtPos(posOfPlayer);
        char expected = '*';
        assertEquals(observed, expected);
    }

    @Test
    public void testCrownPiece_Size_12x12_Row_2_Col_10() {
        ICheckerBoard board = makeBoard(12);
        BoardPosition posOfPlayer = new BoardPosition(2, 10);
        board.crownPiece(posOfPlayer);
        char observed = board.whatsAtPos(posOfPlayer);
        char expected = 'X';
        assertEquals(observed, expected);
    }

    @Test
    public void testMovePiece_Size_8x8_blackTile() {
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPosition = new BoardPosition(3, 2);
        BoardPosition observed = board.movePiece(startingPosition, DirectionEnum.SE);
        BoardPosition expected = new BoardPosition(4, 3);
        assertEquals(observed, expected);

        char obs = board.whatsAtPos(startingPosition);
        char exp = ' ';
        assertEquals(obs, exp);

        obs = board.whatsAtPos(expected);
        exp = '*';
        assertEquals(obs, exp);
    }

    @Test
    public void testMovePiece_Size_8x8_player2() {
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPosition = new BoardPosition(5, 1);
        BoardPosition observed = board.movePiece(startingPosition, DirectionEnum.NE);
        BoardPosition expected = new BoardPosition(4, 2);
        assertEquals(observed, expected);

        char obs = board.whatsAtPos(startingPosition);
        char exp = ' ';
        assertEquals(obs, exp);

        obs = board.whatsAtPos(expected);
        exp = 'o';
        assertEquals(obs, exp);
    }

    @Test
    public void testMovePiece_Size_8x8_player1() {
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPosition = new BoardPosition(2, 0);
        BoardPosition observed = board.movePiece(startingPosition, DirectionEnum.SE);
        BoardPosition expected = new BoardPosition(3, 1);
        assertEquals(observed, expected);

        char obs = board.whatsAtPos(startingPosition);
        char exp = ' ';
        assertEquals(obs, exp);

        obs = board.whatsAtPos(expected);
        exp = 'x';
        assertEquals(obs, exp);
    }

    @Test
    public void testPlayerLostPieces_Remove_1_x(){
        ICheckerBoard cb = makeBoard(8);
        Integer expected = 11;
        HashMap<Character, Integer> observedPieces = cb.getPieceCounts();
        cb.playerLostPieces(1, 'x', observedPieces);
        Integer observed = observedPieces.get('x');
        assertEquals(expected, observed);

        Integer expected2 = 12;
        Integer observed2 = observedPieces.get('o');
        assertEquals(expected2, observed2);
    }

    @Test
    public void testCheckPlayerWin_x_defaultBoard(){
        ICheckerBoard cb = makeBoard(8);
        Boolean expected = false;
        Boolean observed = cb.checkPlayerWin('x');
        assertEquals(expected, observed);
    }

    @Test
    public void testCheckPlayerWin_o_no_X(){
        ICheckerBoard cb = makeBoard(8);
        HashMap<Character, Integer> observedPieces = cb.getPieceCounts();
        cb.playerLostPieces(12, 'x', observedPieces);
        Boolean expected = true;
        Boolean observed = cb.checkPlayerWin('o');
        assertEquals(expected, observed);

    }
    @Test
    public void testPlacePiece_Row0_Col0_blank() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition testPos = new BoardPosition(0,0);
        cb.placePiece(testPos, ' ');
        char expected = ' ';
        char observed = cb.whatsAtPos(testPos);
        assertEquals(expected, observed);
    }

    @Test
    public void testPlacePiece_Row15_Col15_x() {
        ICheckerBoard cb = makeBoard(16);
        BoardPosition testPos = new BoardPosition(15,15);
        cb.placePiece(testPos, 'x');
        char expected = 'x';
        char observed = cb.whatsAtPos(testPos);
        assertEquals(expected, observed);
    }

    @Test
    public void testPlacePiece_Row1_Col15_blank() {
        ICheckerBoard cb = makeBoard(16);
        BoardPosition testPos = new BoardPosition(1,15);
        cb.placePiece(testPos, ' ');
        char expected = ' ';
        char observed = cb.whatsAtPos(testPos);
        assertEquals(expected, observed);
    }

    @Test
    public void testPlacePiece_Row15_Col1_blank() {
        ICheckerBoard cb = makeBoard(16);
        BoardPosition testPos = new BoardPosition(15,1);
        cb.placePiece(testPos, ' ');
        char expected = ' ';
        char observed = cb.whatsAtPos(testPos);
        assertEquals(expected, observed);
    }

    @Test
    public void testPlacePiece_Row0_Col1_blank() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition testPos = new BoardPosition(0,1);
        cb.placePiece(testPos, ' ');
        char expected = ' ';
        char observed = cb.whatsAtPos(testPos);
        assertEquals(expected, observed);
    }

    @Test
    public void testPlacePiece_Row3_Col1_x() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition testPos = new BoardPosition(3,1);
        cb.placePiece(testPos, 'x');
        char expected = 'x';
        char observed = cb.whatsAtPos(testPos);
        assertEquals(expected, observed);
    }

    @Test
    public void testJumpPiece_Row_2_Col_0_SE() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition startPos = new BoardPosition(2, 0);
        BoardPosition removePos = new BoardPosition(5, 1);
        BoardPosition jumpPos = new BoardPosition(3, 1);
        BoardPosition landPos = new BoardPosition(4, 2);
        cb.placePiece(removePos, ' ');
        cb.placePiece(jumpPos, 'o');
        cb.jumpPiece(startPos, DirectionEnum.SE);
        char expected1 = ' ';
        char observed1 = cb.whatsAtPos(jumpPos);
        assertEquals(expected1, observed1);
        char expected2 = 'x';
        char observed2 = cb.whatsAtPos(landPos);
        assertEquals(expected2, observed2);
        int expected3 = 11;
        HashMap<Character, Integer> observedPieces = cb.getPieceCounts();
        int observed3 = observedPieces.get('o');
        assertEquals(expected3, observed3);
    }

    @Test
    public void testJumpPiece_Row_5_Col_3_SW_crown() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition removePos = new BoardPosition(2, 0);
        BoardPosition startPos = new BoardPosition(5, 3);
        BoardPosition endPos = new BoardPosition(7, 1);
        BoardPosition jumpPos = new BoardPosition(6, 2);
        HashMap<Character, Integer> observedPieces = cb.getPieceCounts();
        cb.placePiece(removePos, ' ');
        cb.placePiece(startPos, ' ');
        cb.placePiece(endPos, ' ');
        char expected1 = ' ';
        assertEquals(expected1, cb.whatsAtPos(removePos));
        cb.placePiece(startPos, 'x');
        char expectedx = 'x';
        assertEquals(expectedx, cb.whatsAtPos(startPos));
        int expectedStart = 10;
        int observedStart = observedPieces.get('o');
        assertEquals(expectedStart, observedStart);
        cb.jumpPiece(startPos, DirectionEnum.SW);
        char expectedX = 'X';
        assertEquals(expectedX, cb.whatsAtPos(endPos));
        int expectedEnd = 9;
        int observedEnd = observedPieces.get('o');
        assertEquals(expectedEnd, observedEnd);
        char expectedJump = ' ';
        assertEquals(expectedJump, cb.whatsAtPos(jumpPos));
    }

    @Test
    public void testJumpPiece_Row_2_Col_2_NW_crown() {
        ICheckerBoard cb = makeBoard(8);
        BoardPosition removePos = new BoardPosition(5, 1);
        BoardPosition startPos = new BoardPosition(2, 2);
        BoardPosition endPos = new BoardPosition(0, 0);
        BoardPosition jumpPos = new BoardPosition(1, 1);
        HashMap<Character, Integer> observedPieces = cb.getPieceCounts();
        cb.placePiece(endPos, ' ');
        cb.placePiece(startPos, ' ');
        cb.placePiece(removePos, ' ');
        char expected1 = ' ';
        assertEquals(expected1, cb.whatsAtPos(removePos));
        cb.placePiece(startPos, 'o');
        char expectedo = 'o';
        assertEquals(expectedo, cb.whatsAtPos(startPos));
        int expectedStart = 10;
        int observedStart = observedPieces.get('x');
        assertEquals(expectedStart, observedStart);
        cb.jumpPiece(startPos, DirectionEnum.NW);
        char expectedO = 'O';
        assertEquals(expectedO, cb.whatsAtPos(endPos));
        int expectedEnd = 9;
        int observedEnd = observedPieces.get('x');
        assertEquals(expectedEnd, observedEnd);
        char expectedJump = ' ';
        assertEquals(expectedJump, cb.whatsAtPos(jumpPos));
    }
}
