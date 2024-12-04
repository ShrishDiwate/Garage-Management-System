import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Ball {
    private int x, y, diameter;
    private int xSpeed = 2, ySpeed = 3;
    private int panelWidth, panelHeight;

    // Constructor to initialize the ball's position and size
    public Ball(int x, int y, int diameter, int panelWidth, int panelHeight) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    // Move method to move the ball, reversing direction if it hits an edge
    public void move() {
        x += xSpeed;
        y += ySpeed;

        if (x <= 0 || x + diameter >= panelWidth) {
            xSpeed = -xSpeed;  // Reverse X direction
        }

        if (y <= 0 || y + diameter >= panelHeight) {
            ySpeed = -ySpeed;  // Reverse Y direction
        }
    }

    // Get the shape of the ball (as an Ellipse2D object)
    public Shape getShape() {
        return new Ellipse2D.Double(x, y, diameter, diameter);
    }
}

// BallPanel class to handle the drawing of the ball and animation
class BallPanel extends JPanel implements Runnable {
    private Ball ball;
    private boolean running = true;

    public BallPanel(Ball ball) {
        this.ball = ball;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fill(ball.getShape());  // Draw the ball
    }

    // Runnable method to animate the ball
    @Override
    public void run() {
        while (running) {
            ball.move();  // Move the ball
            repaint();    // Repaint the panel with the updated ball position
            try {
                Thread.sleep(10);  // Pause for 10 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to stop the animation
    public void stop() {
        running = false;
    }
}

public class BouncingBall extends JFrame {
    private BallPanel ballPanel;
    private Thread ballThread;

    public BouncingBall() {
        setTitle("Bouncing Ball Animation");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the ball object with initial position and size
        Ball ball = new Ball(50, 50, 30, 500, 400);

        // Create the ball panel and start the ball animation
        ballPanel = new BallPanel(ball);
        add(ballPanel, BorderLayout.CENTER);

        // Create a button to stop the animation
        JButton stopButton = new JButton("Stop Ball");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ballPanel.stop();  // Stop the animation when button is clicked
            }
        });
        add(stopButton, BorderLayout.SOUTH);

        // Start the ball thread
        ballThread = new Thread(ballPanel);
        ballThread.start();
    }

    public static void main(String[] args) {
        // Create and show the BouncingBall frame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BouncingBall frame = new BouncingBall();
                frame.setVisible(true);
            }
        });
    }
}
