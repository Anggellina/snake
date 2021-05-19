import logic.MovementSides;
import logic.Position;
import logic.Snake;
import logic.SnakeActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;
import static java.lang.Long.SIZE;
import static logic.Snake.SNAKE_SIZE;


public class SnakeGame extends JPanel implements SnakeActionListener {

    private static final Image snakeImage =  new ImageIcon(SnakeGame.class.getResource("snake.png")).getImage();
    private static final Image appleImage = new ImageIcon(SnakeGame.class.getResource("apple_rip.png")).getImage();
    private static final Image pizzaImage = new ImageIcon(SnakeGame.class.getResource("pizza.png")).getImage();
    private static final Image[] hearts;
    private static final Image brokenHeart = new ImageIcon(SnakeGame.class.getResource("broken_heart.png")).getImage();

    static {
        ImageIcon iih = new ImageIcon(SnakeGame.class.getResource("heart.png"));
        hearts = new Image[Snake.SNAKE_LIVES];


        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = iih.getImage();
        }
    }

    private static final Image line_w = new ImageIcon(SnakeGame.class.getResource("line_w.png")).getImage();
    private static final Image line_h = new ImageIcon(SnakeGame.class.getResource("line_h.png")).getImage();


    private boolean paintLose = false;
    private Position pizza;
    private Position apple;
    private List<Position> points = new ArrayList<>();

    private Snake snake;


    public SnakeGame() {
        setBackground(Color.lightGray);

        snake = new Snake();
        snake.setListener(this);
        snake.setup();

        addKeyListener(new FieldKeyListener());
        setFocusable(true);

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (paintLose) {
            paintLose(g);
        } else {
            g.drawImage(line_w,338,0,this);
            g.drawImage(line_h,0,337,this);

            if (pizza != null) {
                g.drawImage(pizzaImage, pizza.x(), pizza.y(),this);
            }
            if (apple != null) {
                g.drawImage(appleImage, apple.x(), apple.y(),this);
            }

            for (int i = 0; i < hearts.length; i++) {
                g.drawImage(hearts[i], 350, 10 + i * 10, this);
            }

            for (int i = 0; i < points.size(); i++) {
                g.drawImage(snakeImage, points.get(i).x(), points.get(i).y(),this);
            }
            
        }
        
    }

    @Override
    public void onSegmentAdded(Position pos) {
        points.add(pos);
        repaint();
    }

    @Override
    public void onAppleCreated(Position pos) {
        this.apple = pos;
        repaint();
    }

    @Override
    public void onPizzaCreated(Position pos) {
        this.pizza = pos;
        repaint();
    }

    @Override
    public void onSnakeMoved(MovementSides side) {
        for (int i = points.size() - 1; i > 0; i--) {
            points.set(i, points.get(i - 1));
        }
        
        switch (side) {
            case LEFT:
                points.set(0, new Position(points.get(0).x() - SNAKE_SIZE, points.get(0).y()));
                break;

            case RIGHT:
                points.set(0, new Position(points.get(0).x() + SNAKE_SIZE, points.get(0).y()));
                break;

            case DOWN:
                points.set(0, new Position(points.get(0).x(), points.get(0).y() + SNAKE_SIZE));
                break;

            case UP:
                points.set(0, new Position(points.get(0).x(), points.get(0).y() - SNAKE_SIZE));
                break;
        }

        repaint();
    }

    @Override
    public void onAppleEaten(Position position) {
        apple = null;
        repaint();
    }

    @Override
    public void onPizzaEaten(Position position) {
        pizza = null;
        repaint();
    }

    @Override
    public void onHeartBroken(int num) {
        hearts[num - 1] = brokenHeart;
        repaint();
    }

    @Override
    public void onLose() {
        paintLose = true;
        repaint();
    }

    private void paintLose(Graphics graph) {
        String str = "Game Over";
        graph.setColor(Color.black);
        graph.drawString(str, 125, SIZE / 2);
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();

            if (snake.started()) {

                switch (key) {
                    case VK_LEFT:
                        snake.onAskChangeMovementSide(MovementSides.LEFT);
                        break;
                    case VK_RIGHT:
                        snake.onAskChangeMovementSide(MovementSides.RIGHT);
                        break;
                    case VK_UP:
                        snake.onAskChangeMovementSide(MovementSides.UP);
                        break;
                    case VK_DOWN:
                        snake.onAskChangeMovementSide(MovementSides.DOWN);
                        break;
                }
            } else {
                switch (key) {
                    case VK_LEFT:
                        snake.start(MovementSides.LEFT);
                        break;
                    case VK_RIGHT:
                        snake.start(MovementSides.RIGHT);
                        break;
                    case VK_UP:
                        snake.start(MovementSides.UP);
                        break;
                    case VK_DOWN:
                        snake.start(MovementSides.DOWN);
                        break;
                }
            }
        }
    }

}