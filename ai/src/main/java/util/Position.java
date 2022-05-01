package util;

/**
 * The position of the chess board
 */
public class Position {
    protected int x;
    protected int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

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
        return isWithinBound(position.getX(), position.getY());
    }

    /**
     * Get the x position
     * @return the x position
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the y position
     * @return the y position
     */
    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }

        return this.getX() == ((Position) obj).getX() && this.getY() == ((Position) obj).getY();
    }
}
