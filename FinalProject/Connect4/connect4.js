connect4Board = {
    var: board = {},
    const: PLAYER_ONE = "x",
    const: PLAYER_TWO = "o",
    const: BOARD_HEIGHT = 6,
    const: BOARD_WIDTH = 7,

    /**
     * Sets the player to their color and generate the board
     * @PRE None
     * @POST None
     * Description creates the divs of all the player spaces, sets the player to their color, initalizes
     * the array to size of the board with empty spaces, also creates the event for the column to change
     * when hovered over
     */
    connect4() {
        board = Array.from({ length: BOARD_WIDTH }, () => Array(BOARD_HEIGHT).fill(""));
        console.log("hey");
        for (var i = 0; i < BOARD_WIDTH; i++) {
            col = document.createElement("div")
            col.id = "col";
            col.className = "col" + i;
            for (var j = 0; j < BOARD_HEIGHT; j++) {
                space = document.createElement("div");
                space.className = "space"
                space.id = [i][j]
                col.appendChild(space);
            }
            document.getElementById("gameBoard").appendChild(col)
        }
    },

    /**
     * Places a piece at a specific board location
     * @param pos board position where the piece where the piece will be located
     * @param player the player the piece belongs to
     * @Pre the player must be the character x or o and the BoardPosition must be the board position object
     * @Post board[BoardPosition.getRow][BoardPosition.getColumn] = player 
     */
    placePiece(pos, player) {
        board[pos.getRow()][pos.getColumn()] = player;
    },

    /**
     * Returns the player at a specific location
     * @param pos the boardpositon that is being looked at
     * @returns the character at that location
     * @Pre the pos must the be the BoardPosition object
     * @Post returns the character at the row and column in the the board array
     */
    whatsAtPos: function(pos) {
        return board[pos.row][pos.col];
    }
}

document.addEventListener("DOMContentLoaded", function() {
    game = connect4Board;
    pos = BoardPosition;
    pos.BoardPosition(0,1)
    game.connect4();
    game.placePiece(pos, "x")
})