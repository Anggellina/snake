package logic;

public enum MovementSides {
    UP, RIGHT, DOWN, LEFT;

    private MovementSides opposite() {
        switch (this) {
            case UP: return DOWN;
            case RIGHT: return LEFT;
            case DOWN: return UP;
            case LEFT: return RIGHT;
        }
        return null;
    }

    public boolean isOpposite(MovementSides with) {
        return this.opposite() == with;
    }
}
