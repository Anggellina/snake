package logic;

public interface SnakeActionListener {

    void onSegmentAdded(Position pos);

    void onAppleCreated(Position pos);
    void onPizzaCreated(Position pos);

    void onSnakeMoved(MovementSides side);

    void onAppleEaten(Position position);
    void onPizzaEaten(Position position);

    void onHeartBroken(int num);

    void onLose();
}
