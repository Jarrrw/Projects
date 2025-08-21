BoardPosition = {
    var: row = -1,
    var: column = -1,

    BoardPosition(row, column) {
        this.row = row;
        this.column = column
    },
    getRow() {
        return row;
    },
    setRow(input) {
        this.row = input
    },
    getColumn() {
        return column;
    },
    setColumn(input) {
        this.column = input;
    },
}