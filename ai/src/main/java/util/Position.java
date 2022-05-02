package util;

/**
 * The position of the chess board
 */
public record Position(int x, int y) {

    /**
     * Check if the given x and y are within the bound of the chess board
     * @param x the x
     * @param y the y
     * @return true if it is, false otherwise
     */
    public static boolean isWithinBound(int x, int y) {
        return 0 <= x && x <= 7 && 0 <= y && y <= 7;
    }

    /**
     * Check if the given position is within the bound of the chess board
     * @param position the position
     * @return true if it is, false otherwise
     */
    public static boolean isWithinBound(Position position) {
        return isWithinBound(position.x(), position.y());
    }

    public String toString() {
        return this.x() + ", " + this.y();
    }
}
