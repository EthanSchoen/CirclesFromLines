package MainPackage;

import java.awt.*;
import java.util.*;
import java.util.Timer;
import javax.swing.*;
import javax.swing.plaf.synth.SynthSeparatorUI;

public class Frame extends JFrame {

	final int WINDOW_HIEGHT = 750;
	final int WINDOW_WIDTH = 1500;

	int circleRadius = 100;// circle radius
	int cPX = WINDOW_WIDTH / 2;// circle mid point x
	int cPY = WINDOW_HIEGHT / 2;// circle mid point y

	Panel p;

	public Frame() {
		// setup window
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WINDOW_WIDTH, WINDOW_HIEGHT);
		setLocation(200, 100);
		setBackground(Color.white);
		setTitle("Circles from Lines");
		setResizable(false);
		p = new Panel();// panel to use paint with
		add(p);
		setVisible(true);
	}

	public class Panel extends JPanel {
		Color drawColor = Color.BLACK;
		int x0NL, y0NL, x1NL, y1NL; // coordinates for next line

		public Panel() {
			setSize(WINDOW_WIDTH, WINDOW_HIEGHT);// place panel over frame
			setLocation(0, 0);
			repaint();
			setVisible(true);

			Timer t = new Timer();// timer adds lines and repaints
			t.schedule(new TimerTask() {
				int i = 0;

				@Override
				public void run() {
					findLine();
					repaint();

					// fun easter egg
					/*
					 * i++; if(i % 2500 == 0){ drawColor = randomColor(); }
					 */
				}

			}, 500, 1);
		}

		private Color randomColor() {
			Random rn = new Random();
			int r = rn.nextInt(6);
			switch (r) {
			case 0:
				return Color.red;
			case 1:
				return Color.yellow;
			case 2:
				return Color.green;
			case 3:
				return Color.blue;
			case 4:
				return Color.black;
			case 5:
				return Color.white;
			default:
				return Color.DARK_GRAY;
			}
		}

		public void findLine() {
			boolean lineFound = false;
			Random r = new Random();

			int x0 = 0;
			int y0 = 0;
			int x1 = 0;
			int y1 = 0;

			while (!lineFound) {
				boolean vert = r.nextBoolean();
				
				if (vert) {//makes lines that go top of the screen to the bottom
					x0 = r.nextInt(WINDOW_WIDTH);
					y0 = 0;
					x1 = r.nextInt(WINDOW_WIDTH);
					y1 = WINDOW_HIEGHT;
				} else { //makes lines that go from the left of the screen to the right
					x0 = r.nextInt(WINDOW_HIEGHT);
					y0 = 0;
					x1 = r.nextInt(WINDOW_HIEGHT);
					y1 = WINDOW_WIDTH;
				}

				if (disLinePoint(x0, y0, x1, y1, cPX, cPY) >= circleRadius && vert) {//if a vertical line check if line goes through circle or not
					x0NL = x0;//if true next lines are assigned
					y0NL = y0;
					x1NL = x1;
					y1NL = y1;
					lineFound = true;//leaves while loop
				} else if (disLinePoint(x0, y0, x1, y1, cPY, cPX) >= circleRadius && !vert) {//same as for vertical lines but with horizontal lines
					x0NL = y0;//due to the way algorithm works have to test line as it was vertical then 
					y0NL = x0;//pass the line back by swapping the x and y values, line is still good
					x1NL = y1;
					y1NL = x1;
					lineFound = true;//leaves while loop
				}
			}
		}

		public int disLinePoint(int x0, int y0, int x1, int y1, int pX, int pY) {
			int distance;

			//covert two points to standard form for lines
			//constants are calculated below
			double a = (x1 - x0);
			double b = (y1 - y0);
			double c = ((y1 - y0) * x0 + 1) - y0;

			//constants then used in distance from point to line formula
			distance = (int) (Math.abs((pY * a - pX * b + c)) / Math.sqrt(Math.pow(a, 2.0) + Math.pow(b, 2.0)));

			return distance;
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(drawColor);
			g.drawLine(x0NL, y0NL, x1NL, y1NL);//paints line found to be ok by findLine()
		}
	}
}