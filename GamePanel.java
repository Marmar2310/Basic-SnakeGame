import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 65;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten = 0;
	int appleX;
	int appleY;
	char direction = 'D';
	boolean running = false;
	Timer timer;
	Random random;
	char pressed = ' ';
	Thread thread = new Thread();

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.gray);
		this.setFocusable(true);
		MyKeyAdapter keyListen = new MyKeyAdapter();
		this.addKeyListener(keyListen);
		startGame();
	}

	public void startGame() {
		createApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (running) {
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(34, 188, 67));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Proxima Nova", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Apples Collected:" + applesEaten,
					(SCREEN_WIDTH - metrics.stringWidth("Apples Collected:" + applesEaten)) / 2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void createApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
		case 'W':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'S':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'A':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'D':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			createApple();
		}
	}

	public void checkCollisions() {
		// checks body collisions
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// check for border collisions
		if ((x[0] < 0) || (x[0] > SCREEN_WIDTH)) {
			running = false;
		}

		if ((y[0] < 0) || (y[0] > SCREEN_HEIGHT)) {
			running = false;
		}

		if (!running) {
			timer.stop();
		}

	}


	public void gameOver(Graphics g) {
		// Game O
		g.setColor(Color.red);
		g.setFont(new Font("Proxima Nova", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("get rekt m8 :)", SCREEN_WIDTH - metrics1.stringWidth("get rekt m8 :)"), SCREEN_HEIGHT / 2);

		g.setColor(Color.red);
		g.setFont(new Font("Proxima Nova", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Apples Collected:" + applesEaten,
				(SCREEN_WIDTH - metrics.stringWidth("Apples Collected:" + applesEaten)) / 2, g.getFont().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				if (direction != 'S') {
					direction = 'W';
					pressed = 'W';
				}
				break;
			case KeyEvent.VK_S:
				if (direction != 'W') {
					direction = 'S';
					pressed = 'S';
				}
				break;
			case KeyEvent.VK_A:
				if (direction != 'D') {
					direction = 'A';
					pressed = 'A';
				}
				break;
			case KeyEvent.VK_D:
				if (direction != 'A') {
					direction = 'D';
					pressed = 'D';
				}
				break;
			}
		}
	}
}
