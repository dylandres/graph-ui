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
                        g.setColor(Color.GREEN);
                        g.fillRect(shape.x, shape.y, 16, 16);
                        break;
                    case "non-edge":
                        g.setColor(Color.RED);
                        g.fillRect(shape.x, shape.y, 16, 16);
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
                        g.setColor(Color.BLUE);
                        g.fillRect(shape.x, shape.y, 5, 5);
//                        g.drawPolygon(new int[] {shape.t1a, shape.t1b, shape.t1c},
//                                new int[] {shape.t2a, shape.t2b, shape.t2c}, 3);
                        break;

                }
            }
        }
        catch (Exception e) {
        }
    }

}
