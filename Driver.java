import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Driver extends JPanel {

    static ArrayList<Shape> visibleShapes;
    static JFrame frame;
    static Graph graph;
    static int width = 800, height = 800;

    public static void main(String[] args) throws InterruptedException {
        startUI();
        generateGraphics();
    }

    public static void startUI() {
        graph = Graph.generateRandomGraph(5);
        frame = new JFrame("Graph");
        JButton graphButton = new JButton("Generate Graph");
        graphButton.setBounds(20, 150, 120, 40);
        graphButton.addActionListener(e -> {
            graph = Graph.generateRandomGraph(5);
            generateGraphics();
        });
        JButton updateButton = new JButton("Update Graph");
        updateButton.setBounds(20, 150, 120, 40);
        updateButton.addActionListener(e -> {
            graph.createGraphics();
            generateGraphics();
        });
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 10, Graph.densityFactor);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put( 0, new JLabel("0.0") );
        labelTable.put( 1, new JLabel("0.1") );
        labelTable.put(2, new JLabel("0.2") );
        labelTable.put(3, new JLabel("0.3") );
        labelTable.put(4, new JLabel("0.4") );
        labelTable.put(5, new JLabel("0.5") );
        labelTable.put(6, new JLabel("0.6") );
        labelTable.put(7, new JLabel("0.7") );
        labelTable.put(8, new JLabel("0.8") );
        labelTable.put(9, new JLabel("0.9") );
        labelTable.put(10, new JLabel("1.0") );
        slider.setLabelTable(labelTable);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(400, 100));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                Graph.densityFactor = source.getValue();
            }
        });
        JPanel panel = new JPanel();
        panel.add(graphButton);
        //panel.add(updateButton);
        panel.add(slider);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
        generateGraphics();
    }

    public static void generateGraphics() {
        visibleShapes = new ArrayList<>();
        graph.generateMatrixGraphic(visibleShapes);
        graph.generateGraphGraphic(visibleShapes);
        Drawable d = new Drawable(visibleShapes);
        frame.add(d);
        d.draw();
    }

}
