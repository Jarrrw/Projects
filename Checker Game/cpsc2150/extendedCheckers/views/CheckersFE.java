package cpsc2150.extendedCheckers.views;

import cpsc2150.extendedCheckers.models.BoardPosition;
import cpsc2150.extendedCheckers.models.CheckerBoard;
import cpsc2150.extendedCheckers.models.CheckerBoardMem;
import cpsc2150.extendedCheckers.models.ICheckerBoard;
import cpsc2150.extendedCheckers.util.DirectionEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static cpsc2150.extendedCheckers.models.BoardPosition.add;
import static cpsc2150.extendedCheckers.models.ICheckerBoard.getDirection;


/**
* This is the main function this function tells the program what functions to call in BoardPosition and Checkerboard.
 * The function  also controls the input and output of the program. Any and all input validation occurs in main or an
 * associated helper function in CheckersFE. The general flow of main is that it generates all necessary objects first.
 * These are the board, the HashMap containing the viable directions for pieces, the number of pieces that each player
 * has, and the scan HashMap. A Scanner is generated to grab user input, and the players are set to their desired letter
 * While the win condition hasn't been met for either player, main gets and validates the piece the player wants to
 * move, gets and validates the direction they want to move in, conducts any movement operations necessary
 * given the piece and direction selected, and finally subtracts pieces if necessary.
 * Main then clears necessary data structures and switches players. This flow is continued until one player has
 * taken all the other players pieces. Once this happens, the game ends and the players are prompted, asking if they
 * wish to play again. This input is validated, and the game will either run again or the program will stop.
 *
 * @invariants none
*/
public class CheckersFE
{
    private static char player_one = 'x';
    private static char player_two = 'o';
    private final static int MIN_DIMENSION = 8;
    private final static int MAX_DIMENSION = 16;
    public static void main(String[] args)
    {

        Scanner playerInput = new Scanner(System.in);
        System.out.println("Welcome to Checkers!");
        System.out.println("Player 1, enter your piece: ");
        player_one = playerInput.nextLine().charAt(0);
        boolean validInput = false;
        while (!validInput) {
            if (Character.isAlphabetic(player_one) && Character.isLowerCase(player_one)) {
                validInput = true;
                break;
            }
            System.out.println("Invalid input. Must be a lowercase character from a-z.");
            System.out.println("Player 1, enter your piece: ");
            player_one = playerInput.nextLine().charAt(0);
        }

        System.out.println("Player 2, enter your piece: ");
        player_two = playerInput.nextLine().charAt(0);
        validInput = false;
        while (!validInput) {
            if (Character.isAlphabetic(player_two) && Character.isLowerCase(player_two) && player_two != player_one) {
                validInput = true;
                break;
            }
            System.out.println("Invalid input. Must be a lowercase character from a-z.");
            System.out.println("Player 2, enter your piece: ");
            player_two = playerInput.nextLine().charAt(0);
        }

        char kinged1 = Character.toUpperCase(player_one);
        char kinged2 = Character.toUpperCase(player_two);

        Character version = Character.MIN_VALUE;
        System.out.println("Do you want a fast game (F/f) or a memory efficient game (M/m)?");
        while (version != 'm' && version != 'M' && version != 'f' && version != 'F') {
            version = playerInput.nextLine().charAt(0);
            if (version != 'm' && version != 'M' && version != 'f' && version != 'F') {
                System.out.println("Invalid input. Must be a valid game mode M or F.");
                System.out.println("Please enter a valid game mode.");
            }
        }
        Integer inputDim = 0;
        StringBuilder dimList = new StringBuilder();
        ArrayList<Integer> validDim = new ArrayList<>();
        for (int i = MIN_DIMENSION; i <= MAX_DIMENSION; i+=2) {
            validDim.add(i);
            if (i != MAX_DIMENSION) {
                dimList.append(i);
                dimList.append(", ");
            } else {
                dimList.append("or ");
                dimList.append(i);
                dimList.append(": ");
            }
        }
        System.out.println("Pick a dimension of " + dimList.toString());
        while (!validDim.contains(inputDim)) {
            inputDim = Integer.valueOf(playerInput.nextLine());
            if (!validDim.contains(inputDim)) {
                System.out.println("Invalid input. Number must be " + dimList.toString());
            }
        }
        ICheckerBoard board;
        if (version == 'm' || version == 'M') {
            board = new CheckerBoardMem(inputDim);
        } else {
            board = new CheckerBoard(inputDim);
        }


        HashMap<Character, ArrayList<DirectionEnum>> directionsMap;
        HashMap<DirectionEnum, Character> scan;
        HashMap<Character, Integer> pieceCounts = board.getPieceCounts();
        ArrayList<DirectionEnum> playerDirections = new ArrayList<DirectionEnum>();
        ArrayList<DirectionEnum> validDirections = new ArrayList<DirectionEnum>();
        char currentPlayer;


        currentPlayer = player_one;
        boolean gameWin = false;
        while (!gameWin) {
            if (board.checkPlayerWin(player_one) || board.checkPlayerWin(player_two)) {
                char winner;
                if (currentPlayer == player_one) {
                    winner = player_two;
                } else {
                    winner = player_one;
                }
                System.out.println("Player " + winner + " has won!");
                System.out.println("Would you like to play again? Enter 'Y' or 'N'");
                String input = playerInput.nextLine();
                boolean valid = false;
                while (!valid) {
                   if (input.length() > 1) {
                       System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                       input = playerInput.nextLine();
                   } else if (input.charAt(0) == 'Y') {
                       System.out.println("Welcome to Checkers!");
                       System.out.println("Player 1, enter your piece: ");
                       player_one = playerInput.nextLine().charAt(0);
                       validInput = false;
                       while (!validInput) {
                           if (Character.isAlphabetic(player_one) && Character.isLowerCase(player_one)) {
                               validInput = true;
                               break;
                           }
                           System.out.println("Invalid input. Must be a lowercase character from a-z.");
                           System.out.println("Player 1, enter your piece: ");
                           player_one = playerInput.nextLine().charAt(0);
                       }

                       System.out.println("Player 2, enter your piece: ");
                       player_two = playerInput.nextLine().charAt(0);
                       while (!validInput) {
                           if (Character.isAlphabetic(player_two) && Character.isLowerCase(player_two) && player_two != player_one){
                               validInput = true;
                               break;
                           }
                           System.out.println("Invalid input. Must be a lowercase character from a-z.");
                           System.out.println("Player 2, enter your piece: ");
                           player_two = playerInput.nextLine().charAt(0);
                       }

                       kinged1 = Character.toUpperCase(player_one);
                       kinged2 = Character.toUpperCase(player_two);

                       version = Character.MIN_VALUE;
                       System.out.println("Do you want a fast game (F/f) or a memory efficient game (M/m)?");
                       while (version != 'm' && version != 'M' && version != 'f' && version != 'F') {
                           version = playerInput.nextLine().charAt(0);
                           if (version != 'm' && version != 'M' && version != 'f' && version != 'F') {
                               System.out.println("Invalid input. Must be a valid game mode M or F.");
                               System.out.println("Please enter a valid game mode.");
                           }
                       }
                       inputDim = 0;
                       System.out.println("Pick a dimension of " + dimList.toString());
                       while (!validDim.contains(inputDim)) {
                           inputDim = Integer.valueOf(playerInput.nextLine());
                           if (!validDim.contains(inputDim)) {
                               System.out.println("Invalid input. Number must be " + dimList.toString());
                           }
                       }
                       board = null;
                       if (version == 'm' || version == 'M') {
                           board = new CheckerBoardMem(inputDim);
                       } else {
                           board = new CheckerBoard(inputDim);
                       }

                        directionsMap = new HashMap<Character, ArrayList<DirectionEnum>>();
                        pieceCounts = board.getPieceCounts();
                        playerDirections = new ArrayList<DirectionEnum>();
                        currentPlayer = player_one;
                        valid = true;
                    } else if (input.charAt(0) == 'N') {
                        valid = true;
                        gameWin = true;
                        System.exit(0);
                    } else {
                        System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                        input = playerInput.nextLine();
                    }
                }


            } else {
                System.out.print(board.toString());
                System.out.println();

                if (currentPlayer == player_one) {
                    System.out.println("player " + player_one +", which piece do you wish to move? Enter the\n" +
                            "row followed by a space followed by the column.");

                    String input = playerInput.nextLine();
                    while(!validateUserInput(input)) {
                        System.out.println("player " + player_one +", which piece do you wish to move? Enter the\n" +
                                "row followed by a space followed by the column.");
                        input = playerInput.nextLine();
                    }
                    
                    BoardPosition pos = changeToPosition(input);
                    while (!validateBoardPosition(pos, board, currentPlayer)) {
                        System.out.println("player " + player_one +", which piece do you wish to move? Enter the\n" +
                                "row followed by a space followed by the column.");
                        input = playerInput.nextLine();
                        while (!validateUserInput(input)) {
                            System.out.println("player " + player_one +", which piece do you wish to move? Enter the\n" +
                                    "row followed by a space followed by the column.");
                            input = playerInput.nextLine();
                        }
                        pos = changeToPosition(input);
                    }

                    //printing the directions the player can move the selected piece in
                    System.out.println("In which direction do you wish to move the piece?\n" +
                            "Enter one of these options: ");
                    directionsMap = board.getViableDirections();
                    if (board.whatsAtPos(pos) == player_one) {
                        playerDirections = directionsMap.get(player_one);
                    }
                    else if (board.whatsAtPos(pos) == kinged1) {
                        playerDirections = directionsMap.get(kinged2);
                    }

                    for (int i = 0; i < playerDirections.size(); ++i) {
                        DirectionEnum currentDirection = playerDirections.get(i);
                        BoardPosition validTest = getDirection(currentDirection);
                        BoardPosition newPos = add(pos, validTest);

                        if (newPos.isValid(board.getRowNum(), board.getColNum())) {
                            if (board.whatsAtPos(newPos) != ' ') {
                                if (board.whatsAtPos(newPos) != player_one && board.whatsAtPos(newPos) != kinged1) {
                                    newPos = add(newPos, validTest);
                                    if (newPos.isValid(board.getRowNum(), board.getColNum())) {
                                        if (board.whatsAtPos(newPos) == ' ') {
                                            System.out.println(currentDirection);
                                            validDirections.add(currentDirection);
                                        }
                                    }
                                }
                            }
                            else {
                                System.out.println(currentDirection);
                                validDirections.add(currentDirection);
                            }
                        }
                    }

                    //change the user's input to an enum after grabbing it
                    input = playerInput.nextLine();
                    DirectionEnum movementDirection = changeToEnum(input);
                    if (movementDirection == null) {
                        while (movementDirection == null) {
                            System.out.println("Invalid input. Input must be one of the Direction Enum choices.");
                            input = playerInput.nextLine();
                            movementDirection = changeToEnum(input);
                        }
                    } else {
                        while (!validDirections.contains(movementDirection)) {
                            System.out.println("Invalid input. Input must be a valid direction for this piece.");
                            input = playerInput.nextLine();
                            movementDirection = changeToEnum(input);
                        }
                    }
                    validDirections.clear();

                    //need to check jumping, and if the player takes a piece
                    scan = board.scanSurroundingPositions(pos);
                    if (scan.get(movementDirection) == ' ') {
                        board.movePiece(pos, movementDirection);

                    } else if (scan.get(movementDirection) == player_two || scan.get(movementDirection) == kinged2) {
                        //gets the position to check for jumping.
                        BoardPosition posTwo = getJumpPosition(movementDirection, pos);

                        if (posTwo.isValid(board.getRowNum(), board.getColNum())) {
                            HashMap<DirectionEnum, Character> scanAgain = null;
                            scanAgain = board.scanSurroundingPositions(posTwo);

                            if (scanAgain.get(movementDirection) != null && scanAgain.get(movementDirection) == ' ') {
                                board.jumpPiece(pos, movementDirection);
                            }

                            scanAgain.clear();

                        } else {
                            System.out.println("Invalid Movement Option. Please select another direction.");
                        }
                    }

                    scan.clear();
                    //before continuing, set the current player to the opponent.
                    currentPlayer = player_two;
                } else {
                    System.out.println("player " + player_two + ", which piece do you wish to move? Enter the\n" +
                            "row followed by a space followed by the column.");

                    //grabbing the tile the player wishes to move to and validating that input
                    String input = playerInput.nextLine();
                    while(!validateUserInput(input)) {
                        System.out.println("player " + player_two + ", which piece do you wish to move? Enter the\n" +
                                "row followed by a space followed by the column.");
                        input = playerInput.nextLine();
                    }

                    //board position must be created before continuing validation
                    BoardPosition pos = changeToPosition(input);
                    while (!validateBoardPosition(pos, board, currentPlayer)) {
                        System.out.println("player " + player_two + ", which piece do you wish to move? Enter the\n" +
                                "row followed by a space followed by the column.");
                        input = playerInput.nextLine();
                        while (!validateUserInput(input)) {
                            System.out.println("player " + player_two + ", which piece do you wish to move? Enter the\n" +
                                    "row followed by a space followed by the column.");
                            input = playerInput.nextLine();
                        }
                        pos = changeToPosition(input);
                    }

                    //printing the directions the player can move the selected piece in
                    System.out.println("In which direction do you wish to move the piece?\n" +
                            "Enter one of these options: ");
                    directionsMap = board.getViableDirections();
                    if (board.whatsAtPos(pos) == player_two) {
                        playerDirections = directionsMap.get(player_two);
                    }
                    else if (board.whatsAtPos(pos) == kinged2) {
                        playerDirections = directionsMap.get(kinged2);
                    }

                    //prints and stores the valid directions for the current piece to move in.
                    for (int i = 0; i < playerDirections.size(); ++i) {
                        DirectionEnum currentDirection = playerDirections.get(i);
                        BoardPosition validTest = getDirection(currentDirection);
                        BoardPosition newPos = add(pos, validTest);

                        if (newPos.isValid(board.getRowNum(), board.getColNum())) {
                            if (board.whatsAtPos(newPos) != ' ') {
                                if (board.whatsAtPos(newPos) != player_two && board.whatsAtPos(newPos) != kinged2) {
                                    newPos = add(newPos, validTest);
                                    if (newPos.isValid(board.getRowNum(), board.getColNum())) {
                                        if (board.whatsAtPos(newPos) == ' ') {
                                            System.out.println(currentDirection);
                                            validDirections.add(currentDirection);
                                        }
                                    }
                                }
                            } else {
                                System.out.println(currentDirection);
                                validDirections.add(currentDirection);

                            }
                        }
                    }

                    //changes the user's input to an enum and validates said input
                    input = playerInput.nextLine();
                    DirectionEnum movementDirection = changeToEnum(input);
                    if (movementDirection == null) {
                        while (movementDirection == null) {
                            System.out.println("Invalid input. Input must be one of the Direction Enum choices.");
                            input = playerInput.nextLine();
                            movementDirection = changeToEnum(input);
                        }
                    } else {
                        while (!validDirections.contains(movementDirection)) {
                            System.out.println("Invalid input. Input must be a valid direction for this piece.");
                            input = playerInput.nextLine();
                            movementDirection = changeToEnum(input);
                        }
                    }
                    validDirections.clear();

                    //Checks jumping and handles taking pieces from opponent
                    scan = board.scanSurroundingPositions(pos);
                    if (scan.get(movementDirection) == ' ') {
                        board.movePiece(pos, movementDirection);

                    } else if (scan.get(movementDirection) == player_one || scan.get(movementDirection) == kinged1) {
                        //gets the position to check for jumping.
                        BoardPosition posTwo = getJumpPosition(movementDirection, pos);
                        if (posTwo.isValid(board.getRowNum(), board.getColNum())) {
                            HashMap<DirectionEnum, Character> scanAgain = null;
                            scanAgain = board.scanSurroundingPositions(posTwo);

                            if (scanAgain.get(movementDirection) != null && scanAgain.get(movementDirection) == ' ') {
                                board.jumpPiece(pos, movementDirection);
                            }

                            scanAgain.clear();

                        } else {
                            System.out.println("Invalid Movement Option. Please select another direction.");
                        }
                    }
                    scan.clear();
                    //before continuing, set currentPlayer = to the opponent.
                    currentPlayer = player_one;
                }
            }
        }

    }

    /**
     * This function returns the character that player one chooses
     * @return the character that corresponds to player 1
     * @post getPlayer1 = player1
     */
    public static Character getPlayer1() {
        return player_one;
    }

    /**
     * This function return the character that player two chooses
     * @return the character that corresponds to player 2
     * @post getPlayer2 = player2
     */
    public static Character getPlayer2() {
        return player_two;
    }
    /**
     *This method gets the second position to scan for any jumps conducted. Essentially it just adds and subtracts from
     * the rows and columns in a way that corresponds with the behavior of the indicated direction.
     *
     * @param movementDirection direction to move the BoardPosition in
     * @param pos the original BoardPosition that we are moving from
     *
     * @return [a new BoardPosition representing the position that will be scanned next]
     *
     * @pre movementDirection != null AND [pos is the board position from which you want to move in a specified
     * direction].
     *
     * @post getJumpPosition = [a new board position is generated in the direction that was passed in from the
     * original position].
     */
    public static BoardPosition getJumpPosition(DirectionEnum movementDirection, BoardPosition pos) {
        int row, column;
        if (movementDirection == DirectionEnum.SE) {
            row = pos.getRow() + 1;
            column = pos.getColumn() + 1;

        } else if (movementDirection == DirectionEnum.SW){
            row = pos.getRow() + 1;
            column = pos.getColumn() - 1;

        } else if (movementDirection == DirectionEnum.NE) {
            row = pos.getRow() - 1;
            column = pos.getColumn() + 1;
        } else {
            row = pos.getRow() - 1;
            column = pos.getColumn() - 1;
        }
        return new BoardPosition(row, column);
    }

    /**
     * Changes a string representing input from the stream to a viable BoardDirection. This is accomplished
     * by splitting the string at regex " ", and calling Integer.parseInt on the 0th and 1st indices of the
     * new array of Strings.
     *
     * @param input string representing user inputted board position
     *
     * @return [a BoardPosition represented by the inputted position from the user in the format
     * row, column].
     *
     * @pre [input is a string in the format of "# #" where # are valid numbers (0 <= # < ROW_NUM) and
     * (0 <= # < COL_NUM)]
     *
     * @post changeToPosition = [a new BoardPosition generated from the user's input] AND row = first AND column = second.
     */
    public static BoardPosition changeToPosition(String input) {
        if (input.charAt(1) == ' ') {
            String[] newInput = input.split(" ");
            int first = Integer.parseInt(newInput[0]);
            int second = Integer.parseInt(newInput[1]);
            return new BoardPosition(first, second);
        } else {
            String[] newInput = input.split(" ");
            int first = Integer.parseInt(newInput[0]);
            int second = Integer.parseInt(newInput[2]);
            return new BoardPosition(first, second);
        }
    }

    /**
     * Changes the string obtained from the user's input to a direction enum. This does not perform any input
     * validation. It merely changes the type from string to a direction enum.
     *
     * @param input string representing the user's input for the direction to move in.
     *
     * @return [enum representing the desired movement direction]
     *
     * @pre [input is a string that correlates DIRECTLY to a DirectionEnum type regardless if in uppercase or lowercase].
     *
     * @post changeToEnum = [playerDirection is equal to the direction the user passed in, or null if the input was not one of
     * the possible directions].
     */
    public static DirectionEnum changeToEnum(String input) {
        DirectionEnum playerDirection = null;
        if (input.equals("se") || input.equals("SE")) {
            playerDirection = DirectionEnum.SE;
        } else if (input.equals("sw") || input.equals("SW")) {
            playerDirection = DirectionEnum.SW;
        } else if (input.equals("ne") || input.equals("NE")) {
            playerDirection = DirectionEnum.NE;
        } else if (input.equals("nw") || input.equals("NW")) {
            playerDirection = DirectionEnum.NW;
        } else {
            return null;
        }

        return playerDirection;
    }

    /**
     * This runs the first validation checks for the user's input. Essentially, it verifies that the user didn't input
     * a string instead of two ints, and it verifies that the string is in the proper format of "# #" because if it's
     * not then the input is invalid.
     *
     * @param input String representing the user's input from the terminal
     *
     * @return [boolean value indicating if the input is a valid or not]
     *
     * @pre input != null
     *
     * @post validateUserInput = [The function will return a boolean value indicating false if the input is not too long
     * or does not contain a digit. Returns true if it is valid].
     */
    public static boolean validateUserInput(String input) {
        int validStringLength = 4;
        // validStringLength corresponds to how a string should be inputted ("# #")
        if (input.length() > validStringLength) {
            System.out.println("Improper input format. It should be a row value\n" +
                    "followed by a space followed by a column value. E.x.\n" +
                    ": '4 5'");
            return false;
        } else if (!Character.isDigit(input.charAt(0))) {
            System.out.println("Improper input format. It should be a row value\n" +
                    "followed by a space followed by a column value. E.x.\n" +
                    ": '4 5'");
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method performs the other validation checks that are only possible once the input has been converted to a
     * BoardPosition. It ensures that the BoardPosition is valid, that the user isn't selecting a boundary piece,
     * that the user isn't selecting a piece that belongs to the opponent, and finally that the piece selected actually
     * has valid moves that the user can select.
     *
     * @param pos BoardPosition to be checked
     * @param board the CheckerBoard
     * @param player character representing the current player
     *
     * @return [a boolean representing if the BoardPosition passes all validation checks].
     *
     * @pre pos != null AND board != null AND [player needs to equal either player1 or player2]
     *
     * @post validateBoardPosition = [A scan will be conducted for every piece in a radius of two directions of the
     * original position. The initial position will be checked to ensure that it is valid, if it is not false is
     * returned. Next the surrounding positions will be checked. If there are no possible moves to be made, false will
     * be returned. If the position behaves in a way that is acceptable to the rest of the program, as detailed above,
     * true will be returned].
     */
    public static boolean validateBoardPosition(BoardPosition pos, ICheckerBoard board, char player) {
        char opponent;
        if (player == player_one) {
            opponent = player_two;

        } else {
            opponent = player_one;

        }

        HashMap<DirectionEnum, Character> valid, scanNe, scanNw, scanSe, scanSw;
        valid = board.scanSurroundingPositions(pos);
        BoardPosition neCheck = getDirection(DirectionEnum.NE);
        BoardPosition nwCheck = getDirection(DirectionEnum.NW);
        BoardPosition seCheck = getDirection(DirectionEnum.SE);
        BoardPosition swCheck = getDirection(DirectionEnum.SW);
        neCheck = add(neCheck, pos);
        nwCheck = add(nwCheck, pos);
        seCheck = add(seCheck, pos);
        swCheck = add(swCheck, pos);
        scanNe = board.scanSurroundingPositions(neCheck);
        scanNw = board.scanSurroundingPositions(nwCheck);
        scanSe = board.scanSurroundingPositions(seCheck);
        scanSw = board.scanSurroundingPositions(swCheck);


        if (!pos.isValid(board.getRowNum(), board.getColNum())) {
            System.out.println("Improper input format. It should be a row value\n" +
                    "followed by a space followed by a column value. E.x.\n" +
                    ": '4 5'");
            return false;

        } else if (board.whatsAtPos(pos) == '*') {
            System.out.println("Player " + player + ", that isn't your piece. Pick one of your pieces.");
            return false;

        } else if (board.whatsAtPos(pos) == opponent) {
            System.out.println("Player " + player + ", that isn't your piece. Pick one of your pieces.");
            return false;

        } else if (player == player_one && board.whatsAtPos(pos) != Character.toUpperCase(player_one))  {
            if ((valid.get(DirectionEnum.SE) == null || valid.get(DirectionEnum.SE) == player)
                    && (valid.get(DirectionEnum.SW) == null || valid.get(DirectionEnum.SW) == player)) {

                System.out.println("Player " + player + ", this piece has no valid moves. Pick again.");
                return false;
            } else if ((valid.get(DirectionEnum.SE) != null &&
                    valid.get(DirectionEnum.SE) == opponent && scanSe.get(DirectionEnum.SE) != null &&
                    scanSe.get(DirectionEnum.SE) != ' ')
                    && (valid.get(DirectionEnum.SW) != null &&
                    valid.get(DirectionEnum.SW) == opponent) && scanSw.get(DirectionEnum.SW) != null
                    && scanSw.get(DirectionEnum.SW) != ' ') {

                System.out.println("Player " + player + ", this piece has no valid moves. Pick again.");
                return false;
            } else {
                return true;
            }

        } else if (player == player_two && board.whatsAtPos(pos) != Character.toUpperCase(player_two)){
            if ((valid.get(DirectionEnum.NE) == null || valid.get(DirectionEnum.NE) == player)
                    && (valid.get(DirectionEnum.NW) == null || valid.get(DirectionEnum.NW) == player)) {

                System.out.println("Player " + player + ", this piece has no valid moves. Pick again.");
                return false;
            } else if ((valid.get(DirectionEnum.NE) != null &&
                    valid.get(DirectionEnum.NE) == opponent && scanNe.get(DirectionEnum.NE) != null &&
                    scanNe.get(DirectionEnum.NE) != ' ')
                    && (valid.get(DirectionEnum.NW) != null &&
                    valid.get(DirectionEnum.NW) == opponent) && scanNw.get(DirectionEnum.NW) != null
                    && scanNw.get(DirectionEnum.NW) != ' ') {

                System.out.println("Player " + player + ", this piece has no valid moves. Pick again.");
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
}
