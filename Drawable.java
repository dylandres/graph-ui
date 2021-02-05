import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;

public class Drawable extends JPanel {

    Collection<Shape> shapes;

    public Drawable() {
        shapes = new ArrayList<>();
    }

    public Drawable(Collection<Shape> shapes) {
        this.shapes = shapes;
    }

    public synchronized void draw() {
        revalidate();
        repaint();
    }

    public void paintComponent(Graphics a) {
        try {
            super.paintComponent(a);
            Graphics2D g = (Graphics2D) a;
            g.setStroke(new BasicStroke(1.8f));
            for (Shape shape : shapes) {
                switch (shape.name) {
                    case "edge":
//                        g.setColor(new Color(0, 153, 0));
                        g.setColor(Color.green.darker());
                        g.fillRect(shape.x, shape.y, 16, 16);
                        break;
                    case "non-edge":
//                        g.setColor(new Color(0, 0, 153));
                        g.setColor(Color.darkGray);
                        g.fillRect(shape.x, shape.y, 16, 16);
                        break;
                    case "cell":
                        g.setColor(Color.CYAN);
                        g.fillRect(shape.x, shape.y, 16, 16);
                        break;
                    case "cell-newly-visited":
                        g.setColor(Color.GREEN);
                        g.fillRect(shape.x, shape.y, 16, 16);
                        break;
                    case "cell-already-visited":
                        g.setColor(Color.RED);
                        g.fillRect(shape.x, shape.y, 16, 16);
                        break;
                    case "origin":
                        g.setColor(Color.RED);
                        g.fillOval(shape.x, shape.y, 15, 15);
                        g.setColor(Color.BLACK);
                        g.drawString(""+shape.num, shape.x-5, shape.y);
                        break;
                    case "dest":
                        g.setColor(Color.BLUE);
                        g.fillOval(shape.x, shape.y, 15, 15);
                        g.setColor(Color.BLACK);
                        g.drawString(""+shape.num, shape.x-5, shape.y);
                        break;
                    case "node":
                        g.setColor(Color.BLACK);
                        g.fillOval(shape.x, shape.y, 15, 15);
                        g.setColor(Color.BLACK);
                        g.drawString(""+shape.num, shape.x-5, shape.y);
                        break;
                    case "dfs-node-found":
                        g.setColor(Color.YELLOW);
                        g.fillOval(shape.x, shape.y, 15, 15);
                        g.setColor(Color.BLACK);
                        g.drawString(""+shape.num, shape.x-5, shape.y);
                        break;
                    case "dfs-node-processed":
                        g.setColor(Color.ORANGE);
                        g.fillOval(shape.x, shape.y, 15, 15);
                        g.setColor(Color.BLACK);
                        g.drawString(""+shape.num, shape.x-5, shape.y);
                        break;
                    case "box":
                        g.setColor(Color.BLACK);
                        g.drawRect(shape.x, shape.y, 500, 500); //450
                        break;
                    case "line":
                        g.setColor(Color.MAGENTA);
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "dfs-line":
                        g.setColor(Color.CYAN);
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "dfs-line-newly-visited":
                        g.setColor(Color.GREEN);
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "dfs-line-already-visited":
                        g.setColor(Color.RED);
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow":
                        g.setColor(Color.MAGENTA);
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "arrow-done":
                        g.setColor(Color.CYAN);
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "arrow-newly-visited":
                        g.setColor(Color.GREEN);
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "arrow-already-visited":
                        g.setColor(Color.RED);
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "line1":
                        g.setColor(new Color(255, 0, 0));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow1":
                        g.setColor(new Color(255, 0, 0));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "1":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("1", shape.x+3, shape.y+13);
                        break;
                    case "line2":
                        g.setColor(new Color(255, 100, 0));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow2":
                        g.setColor(new Color(255, 100, 0));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "2":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("2", shape.x+3, shape.y+13);
                        break;
                    case "line3":
                        g.setColor(new Color(255, 204, 0));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow3":
                        g.setColor(new Color(255, 204, 0));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "3":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("3", shape.x+3, shape.y+13);
                        break;
                    case "line4":
                        g.setColor(new Color(255, 255, 0));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow4":
                        g.setColor(new Color(255, 255, 0));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "4":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("4", shape.x+3, shape.y+13);
                        break;
                    case "line5":
                        g.setColor(new Color(0, 255, 51));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow5":
                        g.setColor(new Color(0, 255, 51));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "5":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("5", shape.x+3, shape.y+13);
                        break;
                    case "line6":
                        g.setColor(new Color(0, 153, 0));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow6":
                        g.setColor(new Color(0, 153, 0));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "6":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("6", shape.x+3, shape.y+13);
                        break;
                    case "line7":
                        g.setColor(new Color(51, 153, 255));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow7":
                        g.setColor(new Color(51, 153, 255));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "7":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("7", shape.x+3, shape.y+13);
                        break;
                    case "line8":
                        g.setColor(new Color(0, 0, 255));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow8":
                        g.setColor(new Color(0, 0, 255));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "8":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("8", shape.x+3, shape.y+13);
                        break;
                    case "line9":
                        g.setColor(new Color(0, 0, 153));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow9":
                        g.setColor(new Color(0, 0, 153));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "9":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("9", shape.x+3, shape.y+13);
                        break;
                    case "line10":
                        g.setColor(new Color(75, 0, 130));
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow10":
                        g.setColor(new Color(75, 0, 130));
                        g.fillPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;
                    case "10":
                        g.setColor(Color.green);
                        g.drawRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("10", shape.x-1, shape.y+13);
                        break;
                    case "legend":
                        g.setColor(new Color(255, 0, 0));
                        g.fillRect(shape.x, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("1", shape.x+4, shape.y+13);
                        g.setColor(new Color(255, 100, 0));
                        g.fillRect(shape.x+16, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("2", shape.x+20, shape.y+13);
                        g.setColor(new Color(255, 204, 0));
                        g.fillRect(shape.x+32, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("3", shape.x+36, shape.y+13);
                        g.setColor(new Color(255, 255, 0));
                        g.fillRect(shape.x+48, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("4", shape.x+52, shape.y+13);
                        g.setColor(new Color(0, 255, 51));
                        g.fillRect(shape.x+64, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("5", shape.x+68, shape.y+13);
                        g.setColor(new Color(0, 153, 0));
                        g.fillRect(shape.x+80, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("6", shape.x+84, shape.y+13);
                        g.setColor(new Color(51, 153, 255));
                        g.fillRect(shape.x+96, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("7", shape.x+100, shape.y+13);
                        g.setColor(new Color(0, 0, 255));
                        g.fillRect(shape.x+112, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("8", shape.x+116, shape.y+13);
                        g.setColor(new Color(0, 0, 153));
                        g.fillRect(shape.x+128, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("9", shape.x+132, shape.y+13);
                        g.setColor(new Color(75, 0, 130));
                        g.fillRect(shape.x+144, shape.y, 16, 16);
                        g.setColor(Color.black);
                        g.drawString("10", shape.x+143, shape.y+13);
                        break;
                }
            }
        }
        catch (Exception e) {
        }
    }

}
