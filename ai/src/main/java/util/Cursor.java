package util;

/**
 * A cursor that can move to a certain direction
 */
public class Cursor {

    protected int x;

    protected int y;

    protected Direction direction;

    public Cursor(int x, int y, Direction direction) {
        if (!Position.isWithinBound(x, y)) {
            throw new IllegalArgumentException("The given position x=" + x + ", y=" + y + " is not valid.");
        }

        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    /**
     * Get the x of the cursor
     * @return the x
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the y of the cursor
     * @return the y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Get the position of the cursor
     * @return the position
     */
    public Position getPosition() {
        return new Position(this.getX(), this.getY());
    }

    /**
     * Check if the cursor can move
     * @return true if it can, false otherwise
     */
    public boolean canMove() {
        return switch (this.direction) {
            case LEFT -> this.canMoveLeft();
            case RIGHT -> this.canMoveRight();
            case UP -> this.canMoveUp();
            case DOWN -> this.canMoveDown();
            case UPLEFT -> this.canMoveUpLeft();
            case UPRIGHT -> this.canMoveUpRight();
            case DOWNLEFT -> this.canMoveDownLeft();
            case DOWNRIGHT -> this.canMoveDownRight();
        };
    }

    /**
     * Move the cursor to the next position
     */
    public void move() {
        if (!this.canMove()) {
            throw new IllegalStateException("The cursor cannot move to " + this.direction + ".");
        }

        switch (this.direction) {
            case LEFT -> this.moveLeft();
            case RIGHT -> this.moveRight();
            case UP -> this.moveUp();
            case DOWN -> this.moveDown();
            case UPLEFT -> this.moveUpLeft();
            case UPRIGHT -> this.moveUpRight();
            case DOWNLEFT -> this.moveDownLeft();
            case DOWNRIGHT -> this.moveDownRight();
        }
    }

    /**
     * Check if the cursor can move left
     * @return true if it can, false otherwise
     */
    protected boolean canMoveLeft() {
        return Position.isWithinBound(x - 1, y);
    }

    /**
     * Move the cursor left
     */
    protected void moveLeft() {
        if (!this.canMoveLeft()) {
            throw new IllegalStateException("The cursor cannot move left.");
        }

        this.x -= 1;
    }

    /**
     * Check if the cursor can move right
     * @return true if it can, false otherwise
     */
    protected boolean canMoveRight() {
        return Position.isWithinBound(x + 1, y);
    }

    /**
     * Move the cursor right
     */
    protected void moveRight() {
        if (!this.canMoveRight()) {
            throw new IllegalStateException("The cursor cannot move right.");
        }

        this.x += 1;
    }

    /**
     * Check if the cursor can move up
     * @return true if it can, false otherwise
     */
    protected boolean canMoveUp() {
        return Position.isWithinBound(x, y - 1);
    }

    /**
     * Move the cursor up
     */
    protected void moveUp() {
        if (!this.canMoveUp()) {
            throw new IllegalStateException("The cursor cannot move up.");
        }

        this.y -= 1;
    }

    /**
     * Check if the cursor can move down
     * @return true if it can, false otherwise
     */
    protected boolean canMoveDown() {
        return Position.isWithinBound(x, y + 1);
    }

    /**
     * Move the cursor down
     */
    protected void moveDown() {
        if (!this.canMoveDown()) {
            throw new IllegalStateException("The cursor cannot move down.");
        }

        this.y += 1;
    }

    /**
     * Check if the cursor can move up and left
     * @return true if it can, false otherwise
     */
    protected boolean canMoveUpLeft() {
        return Position.isWithinBound(x - 1, y - 1);
    }

    /**
     * Move the cursor up and left
     */
    protected void moveUpLeft() {
        if (!this.canMoveUpLeft()) {
            throw new IllegalStateException("The cursor cannot move up and left.");
        }

        this.x -= 1;
        this.y -= 1;
    }

    /**
     * Check if the cursor can move up and right
     * @return true if it can, false otherwise
     */
    protected boolean canMoveUpRight() {
        return Position.isWithinBound(x + 1, y - 1);
    }

    /**
     * Move the cursor up and right
     */
    protected void moveUpRight() {
        if (!this.canMoveUpRight()) {
            throw new IllegalStateException("The cursor cannot move up and right.");
        }

        this.x += 1;
        this.y -= 1;
    }

    /**
     * Check if the cursor can move down and left
     * @return true if it can, false otherwise
     */
    protected boolean canMoveDownLeft() {
        return Position.isWithinBound(x - 1, y + 1);
    }

    /**
     * Move the cursor down and left
     */
    protected void moveDownLeft() {
        if (!this.canMoveDownLeft()) {
            throw new IllegalStateException("The cursor cannot move down and left.");
        }

        this.x -= 1;
        this.y += 1;
    }

    /**
     * Check if the cursor can move down and right
     * @return true if it can, false otherwise
     */
    protected boolean canMoveDownRight() {
        return Position.isWithinBound(x + 1, y + 1);
    }

    /**
     * Move the cursor down and right
     */
    protected void moveDownRight() {
        if (!this.canMoveDownRight()) {
            throw new IllegalStateException("The cursor cannot move down and right.");
        }

        this.x += 1;
        this.y += 1;
    }
}
