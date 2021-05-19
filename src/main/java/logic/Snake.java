package logic;


import java.util.Timer;
import java.util.TimerTask;

public class Snake {

    public static final int SIZE = 320;
    public static final int ALL_DOTS = 400;
    public static final int SNAKE_SIZE = 16;
    public static final int SNAKE_WORLD_ACTION_DELAY = 250;
    public static final int SNAKE_LIVES = 3;

    int lives;

    int dots;
    int snakeWorldActionDelay;
    final Timer timer = new Timer("Snake world actions delay");

    Position[] snake = new Position[ALL_DOTS];

    Position pizza;

    Position apple;

    MovementSides side;
    MovementSides lastSide;

    SnakeActionListener listener;

    public Snake() {
        this.snakeWorldActionDelay = SNAKE_WORLD_ACTION_DELAY;
    }

    public Snake(int snakeWorldActionDelay) {
        this.snakeWorldActionDelay = snakeWorldActionDelay;
    }

    boolean inGame = false;
    boolean started = false;

    public void setup() {
        dots = 3;
        lives = SNAKE_LIVES;

        for (int i = 0; i < dots; i++) {
            snake[i] = new Position(48 - i * SNAKE_SIZE, 48);

            if (listener != null)
                listener.onSegmentAdded(snake[i]);
        }


        createPizza();
        createApple();
    }

    public void start(MovementSides side) {
        this.side = side;
        this.lastSide = side;

        inGame = true;
        started = true;

        startTimer();
    }

    void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (inGame) {
                    move();
                    checkPizza();
                    checkApple();

                    checkCollisions();
                }
            }
        }, 0, snakeWorldActionDelay);
    }

    public void createPizza() {
        pizza = Position.Random();
        if (listener != null)
            listener.onPizzaCreated(pizza);
    }

    public void createApple() {
        Position appleRandom;

        do {
            appleRandom = Position.Random();
        } while (appleRandom.equals(pizza));

        apple = appleRandom;

        if (listener != null)
            listener.onAppleCreated(apple);

    }

    public void move() {
        lastSide = side;
        for (int i = dots - 1; i > 0; i--) {
            snake[i] = snake[i - 1];
        }

        switch (side) {
            case LEFT:
                snake[0] = new Position(snake[0].x() - SNAKE_SIZE, snake[0].y());
                break;

            case RIGHT:
                snake[0] = new Position(snake[0].x() + SNAKE_SIZE, snake[0].y());
                break;

            case DOWN:
                snake[0] = new Position(snake[0].x(), snake[0].y() + SNAKE_SIZE);
                break;

            case UP:
                snake[0] = new Position(snake[0].x(), snake[0].y() - SNAKE_SIZE);
                break;
        }

        if (listener != null) {
            listener.onSnakeMoved(side);
        }
    }

    public void checkPizza() {
        if (snake[0].equals(pizza)) {
            dots++;

            if (listener != null) {
                listener.onPizzaEaten(snake[0]);
                listener.onSegmentAdded(snake[0]);
            }

            createPizza();
        }
    }

    public void checkApple() {
        if (snake[0].equals(apple)) {
            if (listener != null) {
                listener.onAppleEaten(snake[0]);
                if (lives != 0)
                    listener.onHeartBroken(lives);
            }

            if (lives == 0) {
                inGame = false;
                timer.cancel();
                if (listener != null) {
                    listener.onLose();
                }
            }

            lives--;

            createApple();
        }
    }

    public void checkCollisions() {
        for (int i = dots - 1; i > 0; i--) {

            if (snake[0].equals(snake[i])) {
                inGame = false;
                timer.cancel();
                if (listener != null) {
                    listener.onLose();
                }
            }
        }
        if (snake[0].x() > SIZE || snake[0].x() < 0 || snake[0].y() > SIZE || snake[0].y() < 0) {
            inGame = false;
            timer.cancel();

            if (listener != null) {
                listener.onLose();
            }
        }

    }

    public void onAskChangeMovementSide(MovementSides side) {
        if (!side.isOpposite(this.lastSide))
            this.side = side;
    }

    public void setListener(SnakeActionListener listener) {
        this.listener = listener;
    }

    public boolean started() {
        return started;
    }

    public void pause() {
        timer.cancel();
    }

    public void resume() {
        startTimer();
    }
}

