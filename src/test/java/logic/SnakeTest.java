package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class SnakeTest {

    private Snake snake;
    private final int time = 20;

    @BeforeEach
    public void init() {
        snake = new Snake(time);
        snake.setup();
    }

    private MovementSides randomSide() {
        return MovementSides.values()[new Random().nextInt(4)];
    }

    @RepeatedTest(20)
    public void testSides() throws InterruptedException {
        MovementSides side = randomSide();
        snake.apple = new Position(-1, -1); // hide apple
        snake.start(side);

        assertEquals(side, snake.side);

        Thread.sleep(time);

        assertEquals(side, snake.side);

    }

    @RepeatedTest(20)
    public void testDeath() throws InterruptedException {
        MovementSides side = randomSide();
        snake.start(side);

        assertTrue(snake.inGame);

        // время с запасом
        Thread.sleep((long) time * Snake.SIZE * 2 / Snake.SNAKE_SIZE);

        assertFalse(snake.inGame);
    }

    @Test
    public void testPizza() throws InterruptedException {
        /*
                * * -> pizza
                *
         */

        Position[] pos = new Position[Snake.ALL_DOTS];
        pos[0] = new Position(48, 48);
        pos[1] = new Position(32, 48);
        pos[2] = new Position(32, 64);

        snake.pizza = new Position(64, 48);
        snake.apple = new Position(-1, -1); // hide apple
        snake.snake = pos;

        snake.start(MovementSides.RIGHT);

        Thread.sleep(time);

        assertEquals(4, snake.dots);
    }

    @Test
    public void testPizzas() throws InterruptedException {
        /*
            * * -> pizza pizza pizza
            *
         */

        Position[] pos = new Position[Snake.ALL_DOTS];
        pos[0] = new Position(48, 48);
        pos[1] = new Position(32, 48);
        pos[2] = new Position(32, 64);

        snake.pizza = new Position(64, 48);
        snake.apple = new Position(-1, -1); // hide apple
        snake.snake = pos;

        snake.start(MovementSides.RIGHT);
        Thread.sleep(time * 5 / 4);
        snake.pizza = new Position(96, 48);
        Thread.sleep(time * 5 / 4);

        snake.pizza = new Position(112, 48);
        Thread.sleep(time * 5 / 4);

        assertEquals(6, snake.dots);
    }

    @Test
    public void testPizzaFar() throws InterruptedException {
        /*
            * * -> _ _ _ pizza
            *
         */

        Position[] pos = new Position[Snake.ALL_DOTS];
        pos[0] = new Position(48, 48);
        pos[1] = new Position(32, 48);
        pos[2] = new Position(32, 64);

        snake.pizza = new Position(112, 48);
        snake.apple = new Position(-1, -1); // hide apple
        snake.snake = pos;

        snake.start(MovementSides.RIGHT);

        Thread.sleep(time * 4 * 5 / 4);

        assertEquals(4, snake.dots);
    }

    @Test
    public void testApple() throws InterruptedException {
        /*
            * * -> apple
            *
         */

        Position[] pos = new Position[Snake.ALL_DOTS];
        pos[0] = new Position(48, 48);
        pos[1] = new Position(32, 48);
        pos[2] = new Position(32, 64);

        snake.apple = new Position(64, 48);
        snake.pizza = new Position(-1, -1); // hide pizza
        snake.snake = pos;

        snake.start(MovementSides.RIGHT);

        Thread.sleep(time);

        assertEquals(Snake.SNAKE_LIVES - 1, snake.lives);
    }

    @Test
    public void testApples() throws InterruptedException {
        assumeTrue(snake.SNAKE_LIVES >= 3);
        /*
            * * -> apple apple apple
            *
         */

        Position[] pos = new Position[Snake.ALL_DOTS];
        pos[0] = new Position(48, 48);
        pos[1] = new Position(32, 48);
        pos[2] = new Position(32, 64);

        snake.pizza = new Position(-1, -1); // hide pizza
        snake.apple = new Position(64, 48);
        snake.snake = pos;

        snake.start(MovementSides.RIGHT);
        Thread.sleep(time * 5 / 4);

        snake.apple = new Position(96, 48);
        Thread.sleep(time * 5 / 4);

        snake.apple = new Position(112, 48);
        Thread.sleep(time * 5 / 4);

        assertEquals(Snake.SNAKE_LIVES - 3, snake.lives);
    }

    @Test
    public void testApplesDeath() throws InterruptedException {
        assumeTrue(snake.SNAKE_LIVES == 3);
        /*
            * * -> apple apple apple apple
            *
         */

        Position[] pos = new Position[Snake.ALL_DOTS];
        pos[0] = new Position(48, 48);
        pos[1] = new Position(32, 48);
        pos[2] = new Position(32, 64);

        snake.pizza = new Position(-1, -1); // hide pizza
        snake.apple = new Position(64, 48);
        snake.snake = pos;

        snake.start(MovementSides.RIGHT);
        Thread.sleep(time * 5 / 4);

        snake.apple = new Position(96, 48);
        Thread.sleep(time * 5 / 4);

        snake.apple = new Position(112, 48);
        Thread.sleep(time * 5 / 4);

        snake.apple = new Position(128, 48);
        Thread.sleep(time * 5 / 4);

        assertFalse(snake.inGame);
    }

}
