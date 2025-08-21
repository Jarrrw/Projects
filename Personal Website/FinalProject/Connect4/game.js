game = {
    var: gameBoard = null, 

    /**
     * Initializes the game board
     * @Pre none
     * @Post gameBoard = connect4Board.connect4();
     */
    game() {
        gameBoard = connect4Board.connect4();
    },

    /**
     * Checks to see if the player wins
     * @param pos the board position that is being checked around
     * @param player the character that corresponds to the player
     * @return true if the player has won and false if the player has not won
     * @Pre pos must be the board position object and player must be an x or o
     * @Post checks all direction from the position to see if there are four in a row.
     *       if there are four in a row it will return true else it will return false
     * 
     */
    CheckWin() {

    },

    /**
     * Places a piece in a specific column
     * @param col column number that the piece will be place in
     * @param player character of the piece
     * @Pre col must be an integer 0 through 6 and player must be a character x or o
     * @Post find the first empty space in a column and places the pieces there
     */
    DropPiece() {
    
    },

    /**
     * Displays the current board to the user
     * @Pre none
     * @Post changes each space id to the color corresponding to the player and leaving 
     *       it white for black characters
     */
    DisplayBoard() {

    }

}

