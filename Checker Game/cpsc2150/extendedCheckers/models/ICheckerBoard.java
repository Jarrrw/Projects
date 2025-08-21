package cpsc2150.extendedCheckers.models;

import cpsc2150.extendedCheckers.util.DirectionEnum;
import cpsc2150.extendedCheckers.views.CheckersFE;

import java.util.ArrayList;
import java.util.HashMap;

/**
    * An interface that does different actions of a game of checker. The first thing it does it can add
    * viable direction to specific key of the directions hashmap. It can place piece on the board and it can check
    * if a player has won. It can move a piece on the board and jump of piece if necessary. It can modify the piece
    * count hashmap to take pieces away from a player. The interface can scan the positions around a position to
    * check if there are pieces there. It can also return a position that if added to another will result in a move in
    * desired direction.
    *
    * @defines directions: the data structure that store the direction each piece can move
    *          self: the checkerboard that the game will be played
    *          numPieces: the number of pieces that each player has
    *
    * @constraint [the rows and columns of the board are greater than 0 and less than NUM_ROWS and NUM_COLS]
    *
    * @initialization_ensures the viable direction hashmap has been initialized and with all the piece types and their
 *                              directions, the board with all the pieces on their starting squares has been created
 *                              , and the pieceCount hashmap has been initialized with the players set to their starting
 *                              piece count
 */
public interface ICheckerBoard{
    /**
     * This function is used to access the hashmap that holds the viable directions that each player can move
     * @return viableDirections with keys for a player piece and an ArrayList for the enumerated Directions class
     * @Post getViableDirections = directions AND directions = #directions AND numPieces = #numPieces
     * AND self = #self
     */
    public HashMap<Character, ArrayList<DirectionEnum>> getViableDirections();

    /**
     * This function returns a HashMap that links players to the number of pieces they have left
     * @return a HashMap that has keys for each player and values for number of pieces
     * @Post getPieceCounts = [this function returns numPieces] AND numPieces = #numPieces AND directions = #directions
     * and self = #self
     */
    public HashMap<Character, Integer> getPieceCounts();

    /**
     * This function finds what piece is on a specific square on the checkerboard
     * @param pos BoardPosition object that has the row and column of the square that the function should look at
     * @return a character that represent the player at that board position
     * @Pre [the board position needs to be valid for the size of the board that is being played on]
     * @Post whatsAtPos = [returns the character at the pos on the board] AND directions = #directions AND
     * numPieces = #numPieces AND self = #self
     */
    public char whatsAtPos(BoardPosition pos);
    /**
     * This function sets each piece to its starting position on the board
     * @param pos BoardPosition object that tells the function what place on the board the character needs to be placed
     * @param player a character that represents the player
     * @Pre [the character that is passed in is a letter] AND (pos.getColumn >= 0) AND (pos.getColumn < COL_NUM)
     * AND (pos.getRow >= 0) AND (pos.getRow < ROW_NUM)
     * @Post [This function places a piece on the board, it adds pieces to the players total piece count when a piece is
     * being placed, and it removes a piece from the players total when a piece is being removed
     * directions = #directions AND numPieces = [#numPieces with piece count of the player added or subtracted by one
     * AND self = [#self with the character passed to the function added to the board position]
     */
    public void placePiece(BoardPosition pos, char player);

    /**
     * This function will return the number of columns that the function
     * @return an int that represent the total number of columns on the board
     * @Post getColNum = [the function will return the number of columns that the board has as an integer]
     * AND directions = #directions AND numPieces = #numPieces AND self = #self
     */
    public int getColNum();

    /**
     * This function returns the number of rows that the board is made of as an integer
     * @return the integer that corresponds to the number of rows of the checkerboard
     * @post getRowNum = [the program will return the number of rows the board has as an integer]
     * AND directions = #directions AND numPieces = #numPieces AND self = #self
     */
    public int getRowNum();


    /**
     * This function sets the directions that each player can move their pieces in the game
     * @param player that represents the player that getting directions added to
     * @param dir DirectionEnum class that holds the valid directions a player can move
     * @Pre [the char player that is passed in as a parameter must be a letter.]
     * @Post directions = [#directions with the direction passed in as a parameter added
     * to the character] AND numPieces = #numPieces AND self = #self
     */
    public default void addViableDirections(char player, DirectionEnum dir) {
        HashMap<Character, ArrayList<DirectionEnum>> ViableDirections = getViableDirections();
        ArrayList<DirectionEnum> d = ViableDirections.get(player);
        d.add(dir);
        ViableDirections.put(player, d);
    }

    /**
     * This will check and see if all the other players still have pieces on board and if they do not then the player
     * who does wins.
     * @param player a character that represents the player
     * @return True if the player has won and False if the player has not won
     * @Pre [the character that is passed in is a letter of the alphabet.]
     * @Post checkPlayerWin = [the function will run through numPieces and check all the characters
     * that are not the character passed into the function. If every other character does not have any pieces left
     * then the player character passed in as a parameter is the winner the function returns true and if the player
     * has not won the function will return false] AND directions = #directions AND numPieces = #numPieces AND
     * self = #self
     */
    public default boolean checkPlayerWin(Character player) {
        HashMap<Character, Integer> checkMap = this.getPieceCounts();
        char enemy = ' ';

        if (Character.toLowerCase(player) == CheckersFE.getPlayer1()) {
            enemy = CheckersFE.getPlayer2();
        }
        if (Character.toLowerCase(player) == CheckersFE.getPlayer2()) {
            enemy = CheckersFE.getPlayer1();
        }

        int i = checkMap.get(enemy);
        return (i == 0);
    }

    /**
     * This function will move a piece from one position on the board to another position
     * @param startingPos BoardPosition that tells where to find the piece to move
     * @param dir Direction Enum that tells what directions the piece can move
     * @return a BoardPosition object that says the new position of the piece
     * @Pre [the position that is being moved to should be empty and within the bounds of the board]
     * AND [startingPos should be valid (startingPos.getColumn >= 0) && (startingPos.getColumn < COL_NUM) AND
     * (startingPos.getRow >= 0) && (startingPos.getRow < ROW_NUM)]
     * @Post movePiece = [The function will call the getDirection function and add the row and column returned from that function
     * to the row and column of the board position object then it will place the piece on the position returned on the board
     * and return the final position] AND directions = #directions AND numPieces = #numPieces
     * AND self = [#self with character at startingPos moved to the element in the direction dir
     * and if the piece needs to be crowned the char that was moved will be capitalized]
     */
    public default BoardPosition movePiece(BoardPosition startingPos, DirectionEnum dir) {
        BoardPosition finalPos = new BoardPosition(0,0);
        BoardPosition movePos = new BoardPosition(0,0);

        movePos = getDirection(dir);
        finalPos = BoardPosition.add(startingPos, movePos);

        placePiece(finalPos, whatsAtPos(startingPos));
        placePiece(startingPos, ' ');


        if ((whatsAtPos(finalPos) == CheckersFE.getPlayer1()) && (finalPos.getRow() == getRowNum() - 1)) {
            crownPiece(finalPos);
        } else if ((whatsAtPos(finalPos) == CheckersFE.getPlayer2()) && (finalPos.getRow() == 0)) {
            crownPiece(finalPos);
        }

        return finalPos;
    }

    /**
     * This function will be used when the player is trying to jump a piece
     * @param startingPos BoardPosition object that says where to locate the piece that is being moved
     * @param dir DirectionEnum object that holds the direction the piece is going to move
     * @return BoardPosition object that represents the new position after the jump
     * @Pre startingPos.isValid(ROW_NUM, COL_NUM)
     * @Post jumpPiece = [the function will call the getDirection function with the desired direction and add the row and column
     * in the return to the current position. It will then remove the piece in the current position and the call
     * getDirection and do the first step again and place the player character that was doing the jumping in
     * the new position then it will return the final board position.] AND directions = [#directions if
     * the char moved needs to be crowned the direction will change to all of them] AND numPieces = [#numPieces the
     * player that lost a piece losses a loses 1 in the hashmap] AND self = [#self where the piece is moved from current
     * position and moved to its new position and the piece that was jumped is deleted and if the piece that
     * was moved needs to be crown then the piece is capitalized.]
     */
    public default BoardPosition jumpPiece(BoardPosition startingPos, DirectionEnum dir) {
       /*
        A modified version of movePiece that moves a piece by "jumping" an opponent player piece. When a player "jumps"
        an opponent, that player should move two positions in the direction passed in by dir parameter. Remember, not
        only should the piece now appear at the moved location, and the starting position should not be empty, but the
        position that was "jumped" should now also be empty.

        Furthermore, this method should remove 1 from the opponent's pieceCount.
         */
        BoardPosition newPosition = new BoardPosition(0,0);
        BoardPosition finalPosition = new BoardPosition(0,0);
        BoardPosition movement = new BoardPosition(0,0);

        movement = getDirection(dir);
        newPosition = BoardPosition.add(startingPos, movement);
        finalPosition = BoardPosition.add(newPosition, movement);

        char loser = Character.MIN_VALUE;
        char p1 = CheckersFE.getPlayer1();
        char p2 = CheckersFE.getPlayer2();
        if (whatsAtPos(newPosition) == p2 || whatsAtPos(newPosition) == Character.toUpperCase(p2)) {
            loser = p2;
        }
        if (whatsAtPos(newPosition) == p1 || whatsAtPos(newPosition) == Character.toUpperCase(p1)) {
            loser = p1;
        }




        placePiece(finalPosition, whatsAtPos(startingPos));
        placePiece(newPosition, ' ' );
        placePiece(startingPos, ' ');



        if ((whatsAtPos(finalPosition) == p1) && (finalPosition.getRow() == getRowNum() - 1)) {
            crownPiece(finalPosition);
        }
        else if ((whatsAtPos(finalPosition) == p2) && (finalPosition.getRow() == 0)) {
            crownPiece(finalPosition);
        }

        return finalPosition;
    }

    /**
     * This function removes pieces from a player's total pieces
     * @param numPieces which is the number of pieces that are going to be removed from pieceCounts
     * @param player character that the pieces are getting removed from
     * @param pieceCounts hashmap that holds how many pieces the players have left
     * @Pre [char player has to be one of the valid player characters and the function does not remove the piece from
     * the board.]
     * @Post directions = #directions AND numPieces = [#numPieces with integer corresponding
     * to player character subtracted by numPieces] AND self = [#self minus the pieces lost from one player]
     */
    public default void playerLostPieces(int numPieces, char player, HashMap<Character, Integer> pieceCounts) {
        /*
        Removes a numPieces amount of tokens, mapped to the parameter player, from the pieceCounts HashMap.

        Hint: While it may seem redundant, there's a reason there's a parameter named pieceCounts even though we have
        access to the pieceCount instance variable.
         */
        HashMap<Character, Integer> refToPieceCount = getPieceCounts();
        int playerPieces = pieceCounts.get(player);
        playerPieces = playerPieces - numPieces;
        pieceCounts.remove(player);
        pieceCounts.put(player, playerPieces);
        refToPieceCount.remove(player);
        refToPieceCount.put(player, playerPieces);

    }

    /**
     * This function will scan the positions around a board position and return what piece is in every direction around
     * that position
     * @param startingPos BoardPosition object that the function wants to scan all the pieces around
     * @return Hashmap with a direction and a character mapped to the direction
     * @Pre [startingPos must a valid position on the board 0 <= startingPos.getRow < ROW_NUM && 0 <= startingPos.getColumn < COL_NUM]
     * @Post scanSurroundingPositions = [the function will set each direction to the corresponding
     * character in that direction on the checkerboard] AND directions = #directions AND
     * numPieces = #numPieces AND self = #self
     */
    public default HashMap<DirectionEnum, Character> scanSurroundingPositions(BoardPosition startingPos) {
        /*
        "Scans" the indices surrounding the index given by the startingPos parameter. There are a few different ways
        we can use this method, so I won't specify any given one in this description. Still, know that this function
        should return a HashMap mapping the four DirectionEnums to the char located at that position in the respective
        direction.

        For example, the position DirectionEnum.SE of startingPos (2,2) is position (3,3). If position (3,3) is empty,
        this function would return a HashMap where DirectionEnum.SE is mapped to ' '. If position (3,3) contained a
        player, it would map DirectionEnum.SE to the char that represents that player.
         */
        HashMap<DirectionEnum, Character> resultOfScan = new HashMap<>();

        int rPlus = startingPos.getRow() + 1, rMns = startingPos.getRow() - 1;
        int cPlus = startingPos.getColumn() + 1, cMns = startingPos.getColumn() - 1;

        if ((rMns >= 0) && (cMns >= 0)) {
            resultOfScan.put(DirectionEnum.NW, whatsAtPos(new BoardPosition(rMns, cMns)));
        }


        if ((rMns >= 0) && (cPlus < this.getColNum())) {
            resultOfScan.put(DirectionEnum.NE, whatsAtPos(new BoardPosition(rMns, cPlus)));
        }

        if ((rPlus < this.getRowNum()) && (cPlus < this.getColNum())) {
            resultOfScan.put(DirectionEnum.SE, whatsAtPos(new BoardPosition(rPlus, cPlus)));
        }

        if ((rPlus < this.getRowNum()) && (cMns >= 0)) {
            resultOfScan.put(DirectionEnum.SW, whatsAtPos(new BoardPosition(rPlus, cMns)));
        }

        return resultOfScan;
    }

    /**
     * This function returns what to add to the row and column of a function to move it in a specific direction
     * @param dir the direction that a piece wants to move
     * @return the new position with 1 or a -1 added to the row and column
     * @Pre [the direction is one of the valid directions in DirectionEnum that the player can go]
     * @Post getDirection = [the program will take a direction and return +1 or -1 to the row and column depending on
     * the direction. NE will return -1 to the row and +1 to the column. NW will return -1 to the row and -1
     * to the column. SW will return +1 to row and -1 to column. SE will return +1 to the row and +1 to the column.]
     */
    public static BoardPosition getDirection(DirectionEnum dir) {
        /*
        a STATIC function that we can use to return a new BoardPosition that represents movement in the direction given
        by the dir parameter.

        For example, to move a piece DirectionEnum.SE we would have to add 1 to that piece's row position and 1 to that
        pieces column position. As such, if DirectionEnum.SE is passed in as a parameter, this function should return a
        new BoardPosition that could be added to any given piece's position so that the piece now sits one position SE
        of where it began.
         */
        BoardPosition b = new BoardPosition(0, 0);
        if(dir == DirectionEnum.NE){
            b = new BoardPosition(-1,1);
        }
        else if(dir == DirectionEnum.NW){
            b = new BoardPosition(-1,-1);
        }
        else if(dir == DirectionEnum.SW){
            b = new BoardPosition(1,-1);
        }
        else if(dir == DirectionEnum.SE){
            b = new BoardPosition(1,1);
        }
        return b;
    }

    /**
     * This function will be used to crown a checkers piece during the game
     * @param posOfPlayer a BoardPosition object that a has the row and column of the piece that will be crowned
     * @Pre [the board position is valid] AND (pos.getColumn >= 0) && (pos.getColumn < NUM_COLS)
     * AND (pos.getRow >= 0) && (pos.getRow < NUM_ROWS)
     * AND [the player character that is being crowned is one of the valid player characters and the piece
     * has already been moved]
     * @Post directions = #directions AND numPieces = #numPieces AND self = [#self with the char at
     * the posOfPlayer capitalized]
     */
    public default void crownPiece(BoardPosition posOfPlayer) {
        /*
        "crowns" a piece by converting the char at the position on the board, given by the posOfPlayer parameter, to
        the uppercase equivalent of the char.
         */
        char piece = whatsAtPos(posOfPlayer);
        piece = Character.toUpperCase(piece);
        placePiece(posOfPlayer, piece);
    }




}
