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

    public void draw() {
        revalidate();
        repaint();
    }

    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            for (Shape shape : shapes) {
                switch (shape.name) {
                    case "edge":
                        g.setColor(Color.GREEN);
                        g.fillRect(shape.x, shape.y, 19, 19);
                        break;
                    case "non-edge":
                        g.setColor(Color.RED);
                        g.fillRect(shape.x, shape.y, 19, 19);
                        break;
                    case "node":
                        g.setColor(Color.BLACK);
                        g.fillOval(shape.x, shape.y, 15, 15);
                        break;
                    case "box":
                        g.setColor(Color.BLACK);
                        g.drawRect(shape.x, shape.y, 340, 340);
                        break;
                    case "line":
                        g.setColor(Color.MAGENTA);
                        g.drawLine(shape.x, shape.y, shape.x2, shape.y2);
                        break;
                    case "arrow":
                        g.setColor(Color.MAGENTA);
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
