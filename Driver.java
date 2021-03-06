import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Driver extends JPanel {

    static ArrayList<Shape> visibleShapes;
    static JFrame frame;
    static Graph graph;
    static int width = 1000, height = 800;
    static double globalDensity = 0.5;
    static int globalVertexCount = 10;
    static boolean globalDirected = true;
    static boolean globalWeighted = false;
    static boolean traversalOccurring = false;
    static Timer timer;
    static int speed = 110;
    static int counter = 0;
    static ArrayList<Shape> path;
    static ArrayList<Shape> matrixPath;
    static ArrayList<Shape> arrowPath;
    static JButton clearGraph;
    static int origin = 0;
    static int dest = 0;

    public static void main(String[] args) {
        startUI();
        generateGraphics();
//        spin(100);
    }

    public static void spin(int speed) {
        while (true) {
            graph = new Graph(25, globalDensity, false, globalWeighted);
            generateGraphics();
            try {
                Thread.sleep(speed);
            }
            catch (InterruptedException e) {
            }
        }
    }

    public static void startUI() {
        graph = new Graph(globalVertexCount, 0.5, globalDirected, globalWeighted);
        frame = new JFrame("Graph");
        JButton generateGraph = new JButton("Generate New Graph");
        JLabel density = new JLabel("  Density Factor: " + globalDensity);
        JLabel vCount = new JLabel("  Vertex Count: " + globalVertexCount);
        JLabel eCount = new JLabel("  Edge Count: " + graph.getEdges());
        ButtonGroup group = new ButtonGroup();
        JRadioButton dir = new JRadioButton("Directed");
        JRadioButton undir = new JRadioButton("Un-directed");
        dir.addActionListener(e -> {
            if (!traversalOccurring && !globalDirected && dir.isSelected()) {
                globalDirected = true;
                graph.undirectedToDirected();
                clearTraversal(graph);
                eCount.setText("  Edge Count: " +graph.getEdges());
            }
        });
        undir.addActionListener(e -> {
            if (!traversalOccurring && globalDirected && undir.isSelected()) {
                globalDirected = false;
                graph.directedToUndirected();
                clearTraversal(graph);
                eCount.setText("  Edge Count: " + graph.getEdges());
            }
        });
        group.add(dir);
        group.add(undir);
        dir.setSelected(true);
        ButtonGroup group2 = new ButtonGroup();
        JRadioButton weighted = new JRadioButton("Weighted");
        JRadioButton unweighted = new JRadioButton("Un-weighted");
        weighted.addActionListener(e -> {
            if (!traversalOccurring && !globalWeighted && weighted.isSelected()) {
                globalWeighted = true;
                graph.swapWeighted(true);
                clearTraversal(graph);
            }
        });
        unweighted.addActionListener(e -> {
            if (!traversalOccurring && globalWeighted && unweighted.isSelected()) {
                globalWeighted = false;
                graph.swapWeighted(false);
                clearTraversal(graph);
            }
        });
        group2.add(weighted);
        group2.add(unweighted);
        unweighted.setSelected(true);
        generateGraph.setBounds(20, 150, 120, 40);
        generateGraph.addActionListener(e -> {
            if (!traversalOccurring) {
                graph = new Graph(globalVertexCount, globalDensity, globalDirected, globalWeighted);
                generateGraphics();
                density.setText("  Density Factor: " + globalDensity);
                vCount.setText("  Vertex Count: " + globalVertexCount);
                eCount.setText("  Edge Count: " + graph.getEdges());
            }
        });
        JButton dfsButton = new JButton("Run DFS");
        JButton bfsButton = new JButton("Run BFS");
        JButton dijkstraButton = new JButton("Run Dijkstra's");
        //JButton kruskalButton = new JButton("Run Kruskal's");
        clearGraph = new JButton("Clear");
        clearGraph.addActionListener(e -> {
            if (timer != null) {
                if (timer.isRunning())
                    timer.stop();
            }
            traversalOccurring = false;
            clearGraph.setText("Clear");
            clearTraversal(graph);
        });

        dfsButton.addActionListener(e -> {
            clearGraph.setText("Stop");
            if (!traversalOccurring)
                traversal("dfs", origin);
        });
        bfsButton.addActionListener(e -> {
            clearGraph.setText("Stop");
            if (!traversalOccurring)
                traversal("bfs", origin);
        });
        dijkstraButton.addActionListener(e -> {
            graph.dijkstra(origin, dest);
        });
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 10, (int)(globalDensity * 10.0));
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
                if (!traversalOccurring) {
                    JSlider source = (JSlider) e.getSource();
                    globalDensity = source.getValue() / 10.0;
                    graph.changeDensityFactor(source.getValue() / 10.0);
                    generateGraphics();
                    clearTraversal(graph);
                    density.setText("  Density Factor: " + globalDensity);
                    vCount.setText("  Vertex Count: " + globalVertexCount);
                    eCount.setText("  Edge Count: " + graph.getEdges());
                }
            }
        });
        JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 1, 25, globalVertexCount);
        Hashtable<Integer, JLabel> labelTable2 = new Hashtable<>();
        labelTable2.put( 1, new JLabel("1") );
        labelTable2.put(2, new JLabel("2") );
        labelTable2.put(3, new JLabel("3") );
        labelTable2.put(4, new JLabel("4") );
        labelTable2.put(5, new JLabel("5") );
        labelTable2.put(6, new JLabel("6") );
        labelTable2.put(7, new JLabel("7") );
        labelTable2.put(8, new JLabel("8") );
        labelTable2.put(9, new JLabel("9") );
        labelTable2.put(10, new JLabel("10") );
        labelTable2.put(11, new JLabel("11") );
        labelTable2.put(12, new JLabel("12") );
        labelTable2.put(13, new JLabel("13") );
        labelTable2.put(14, new JLabel("14") );
        labelTable2.put(15, new JLabel("15") );
        labelTable2.put(16, new JLabel("16") );
        labelTable2.put(17, new JLabel("17") );
        labelTable2.put(18, new JLabel("18") );
        labelTable2.put(19, new JLabel("19") );
        labelTable2.put(20, new JLabel("20") );
        labelTable2.put(21, new JLabel("21") );
        labelTable2.put(22, new JLabel("22") );
        labelTable2.put(23, new JLabel("23") );
        labelTable2.put(24, new JLabel("24") );
        labelTable2.put(25, new JLabel("25") );
        slider2.setLabelTable(labelTable2);
        slider2.setMajorTickSpacing(1);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);
        slider2.setPreferredSize(new Dimension(550, 100));
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!traversalOccurring) {
                    JSlider source = (JSlider) e.getSource();
                    globalVertexCount = source.getValue();
                    graph.changeVertexCount(globalVertexCount);
                    generateGraphics();
                    clearTraversal(graph);
                    density.setText("  Density Factor: " + globalDensity);
                    vCount.setText("  Vertex Count: " + globalVertexCount);
                    eCount.setText("  Edge Count: " + graph.getEdges());
                }
            }
        });
        JSlider slider3 = new JSlider(JSlider.HORIZONTAL, 0, 1000, 1010-speed);
        Hashtable<Integer, JLabel> labelTable3 = new Hashtable<>();
        labelTable3.put( 0, new JLabel("Pause") );
        labelTable3.put( 110, new JLabel("Slow") );
        labelTable3.put(1000, new JLabel("Fast") );
        slider3.setLabelTable(labelTable3);
        slider3.setPaintLabels(true);
        slider3.setPreferredSize(new Dimension(400, 100));
        slider3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (source.getValue() == 0)
                    speed = Integer.MAX_VALUE;
                else
                    speed = 1010 - source.getValue();
//                System.out.println(speed);
                if (traversalOccurring)
                    changeSpeed();
            }
        });
        JPanel masterPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(generateGraph);
        buttonPanel.add(density);
        buttonPanel.add(vCount);
        buttonPanel.add(eCount);
        buttonPanel.add(dir);
        buttonPanel.add(undir);
        buttonPanel.add(weighted);
        buttonPanel.add(unweighted);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setForeground(Color.black);
        JPanel card1 = new JPanel();
        JPanel card2 = new JPanel();
        JPanel card3 = new JPanel();
        card3.setLayout(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 0;
        c1.gridy = 1;
        c1.gridwidth = 5;
        card3.add(slider3, c1);
        c1.gridx = 0;
        c1.gridy = 0;
        c1.gridwidth = 1;
        card3.add(dfsButton, c1);
        c1.gridx = 1;
        card3.add(bfsButton, c1);
        c1.gridx = 2;
        card3.add(dijkstraButton, c1);
        c1.gridx = 3;
//        card3.add(kruskalButton, c1);
        c1.gridx = 4;
        card3.add(clearGraph, c1);
        card1.add(slider);
        card2.add(slider2);
        tabs.addTab("Change Density Factor", card1);
        tabs.addTab("Change Vertex Count", card2);
        tabs.addTab("Algorithms", card3);
        masterPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        // c.weightx = 0.1;
        masterPanel.add(buttonPanel, c);
        c.gridx = 3;
        c.gridy = 0;
//        c.gridwidth = 3;
        // c.weightx = 0.9;
        masterPanel.add(tabs, c);
        frame.setBackground(Color.GREEN);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.add(masterPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
        generateGraphics();
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!traversalOccurring) {
                    super.mouseClicked(e);
                    int x = e.getX();
                    int y = e.getY();
                    for (int node = 1; node <= graph.numVertices; node++) {
                        if (origin == node || dest == node)
                            continue;
                        CoordinatePair coords = graph.nodeLocations.get(node);
                        int nodeX = coords.x;
                        int nodeY = coords.y;
                        if (nodeX - 7 < x - 8 && x - 8 < nodeX + 7 && nodeY - 7 < y - 38 && nodeY + 7 > y - 38) {
                            if (origin == 0) {
                                origin = node;
                                for (int i = 0; i < graph.vertexGraphics.size(); i++) {
                                    if (graph.vertexGraphics.get(i).name.equals("node") && graph.vertexGraphics.get(i).x == nodeX && graph.vertexGraphics.get(i).y == nodeY) {
                                        graph.vertexGraphics.set(i, new Shape(node, "origin", nodeX, nodeY));
                                        break;
                                    }
                                }
                            } else {
                                if (dest == 0) {
                                    dest = node;
                                    for (int i = 0; i < graph.vertexGraphics.size(); i++) {
                                        if (graph.vertexGraphics.get(i).name.equals("node") && graph.vertexGraphics.get(i).x == nodeX && graph.vertexGraphics.get(i).y == nodeY) {
                                            graph.vertexGraphics.set(i, new Shape(node, "dest", nodeX, nodeY));
                                            break;
                                        }
                                    }
                                } else {
                                    origin = node;
                                    for (int i = 0; i < graph.vertexGraphics.size(); i++) {
                                        Shape s = graph.vertexGraphics.get(i);
                                        if (s.name.equals("origin")) {
                                            graph.vertexGraphics.set(i, new Shape(s.num, "node", s.x, s.y));
                                            break;
                                        }
                                    }
                                    for (int i = 0; i < graph.vertexGraphics.size(); i++) {
                                        if (graph.vertexGraphics.get(i).name.equals("node") && graph.vertexGraphics.get(i).x == nodeX && graph.vertexGraphics.get(i).y == nodeY) {
                                            graph.vertexGraphics.set(i, new Shape(node, "origin", nodeX, nodeY));
                                            break;
                                        }
                                    }
                                    dest = 0;
                                    for (int i = 0; i < graph.vertexGraphics.size(); i++) {
                                        Shape s = graph.vertexGraphics.get(i);
                                        if (s.name.equals("dest")) {
                                            graph.vertexGraphics.set(i, new Shape(s.num, "node", s.x, s.y));
                                            break;
                                        }
                                    }
                                    for (int i = 0; i < graph.vertexGraphics.size(); i++) {
                                        if (graph.vertexGraphics.get(i).name.equals("node") && graph.vertexGraphics.get(i).x == nodeX && graph.vertexGraphics.get(i).y == nodeY) {
                                            graph.vertexGraphics.set(i, new Shape(node, "dest", nodeX, nodeY));
                                            break;
                                        }
                                    }
                                }
                            }
                            generateGraphics();
                            break;
                        }
                    }
                }
            }
        });
    }

    public static void generateGraphics() {
        visibleShapes = new ArrayList<>();
        graph.generateGraphGraphic(visibleShapes);
        graph.generateMatrixGraphic(visibleShapes);
        Drawable d = new Drawable(visibleShapes);
        frame.add(d);
        d.draw();
    }


    public static void changeSpeed() {
        timer.stop();
        timer = new Timer(speed, e -> {
            if (counter == path.size() - 1) {
                traversalOccurring = false;
                ((Timer) e.getSource()).stop();
                clearGraph.setText("Clear");
            }
            if (path.get(counter).name.equals("dfs-node-processed") || path.get(counter).name.equals("dfs-node-found")) {
                for (int i = 0; i < graph.vertexGraphics.size(); i++) {
                    if (graph.vertexGraphics.get(i).x == path.get(counter).x && graph.vertexGraphics.get(i).y == path.get(counter).y) {
                        graph.vertexGraphics.set(i, path.get(counter));
                        break;
                    }
                }
            }
            else {
                for (int i = 0; i < graph.edgeGraphics.size(); i++) {
                    if (graph.edgeGraphics.get(i).x == path.get(counter).x && graph.edgeGraphics.get(i).y == path.get(counter).y &&
                            graph.edgeGraphics.get(i).x2 == path.get(counter).x2 && graph.edgeGraphics.get(i).y2 == path.get(counter).y2) {
                        graph.edgeGraphics.set(i, path.get(counter));
                    }
                    if (graph.edgeGraphics.get(i).x == path.get(counter).x2 && graph.edgeGraphics.get(i).y == path.get(counter).y2 &&
                            graph.edgeGraphics.get(i).x2 == path.get(counter).x && graph.edgeGraphics.get(i).y2 == path.get(counter).y) {
                        graph.edgeGraphics.set(i, path.get(counter));
                    }
                }
                for (int i = 0; i < graph.matrixGraphics.size(); i++) {
                    if (graph.matrixGraphics.get(i).x == matrixPath.get(counter).x && graph.matrixGraphics.get(i).y == matrixPath.get(counter).y) {
                        graph.matrixGraphics.set(i, matrixPath.get(counter));
                    }
                }
                if (graph.directed) {
                    for (int i = 1; i < graph.arrowGraphics.length; i++) {
                        for (int j = 1; j < graph.arrowGraphics.length; j++) {
                            if (graph.arrowGraphics[i][j] != null) {
                                Shape s = graph.arrowGraphics[i][j];
                                if (s.t1a == arrowPath.get(counter).t1a && s.t1b == arrowPath.get(counter).t1b && s.t1c == arrowPath.get(counter).t1c &&
                                        s.t2a == arrowPath.get(counter).t2a && s.t2b == arrowPath.get(counter).t2b && s.t2c == arrowPath.get(counter).t2c) {
                                    graph.arrowGraphics[i][j] = arrowPath.get(counter);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            generateGraphics();
            counter++;
        });
        timer.setRepeats(true);
        timer.start();
    }

    public static void traversal(String traversal, int start) {
        counter = 0;
        origin = 0;
        dest = 0;
        path = new ArrayList<>();
//        System.out.println(traversalOccurring);
        if (!traversalOccurring) {
            traversalOccurring = true;
            path = new ArrayList<>();
            matrixPath = new ArrayList<>();
            arrowPath = new ArrayList<>();
            if (traversal.equals("dfs")) {
                graph.dfs(start, path, matrixPath, arrowPath);
            }
            else {
                graph.bfs(start, path, matrixPath, arrowPath);
            }
            timer = new Timer(speed, new ActionListener() { //100
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (counter == path.size() - 1) {
                        traversalOccurring = false;
                        ((Timer) e.getSource()).stop();
                        clearGraph.setText("Clear");
                    }
                    if (path.get(counter).name.equals("dfs-node-processed") || path.get(counter).name.equals("dfs-node-found")) {
                        for (int i = 0; i < graph.vertexGraphics.size(); i++) {
                            if (graph.vertexGraphics.get(i).x == path.get(counter).x && graph.vertexGraphics.get(i).y == path.get(counter).y) {
                                graph.vertexGraphics.set(i, path.get(counter));
                                break;
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < graph.edgeGraphics.size(); i++) {
                            if (graph.edgeGraphics.get(i).x == path.get(counter).x && graph.edgeGraphics.get(i).y == path.get(counter).y &&
                                    graph.edgeGraphics.get(i).x2 == path.get(counter).x2 && graph.edgeGraphics.get(i).y2 == path.get(counter).y2) {
                                graph.edgeGraphics.set(i, path.get(counter));
                            }
                            if (graph.edgeGraphics.get(i).x == path.get(counter).x2 && graph.edgeGraphics.get(i).y == path.get(counter).y2 &&
                                    graph.edgeGraphics.get(i).x2 == path.get(counter).x && graph.edgeGraphics.get(i).y2 == path.get(counter).y) {
                                graph.edgeGraphics.set(i, path.get(counter));
                            }
                        }
                        for (int i = 0; i < graph.matrixGraphics.size(); i++) {
                            if (graph.matrixGraphics.get(i).x == matrixPath.get(counter).x && graph.matrixGraphics.get(i).y == matrixPath.get(counter).y) {
                                graph.matrixGraphics.set(i, matrixPath.get(counter));
                            }
                        }
                        if (graph.directed) {
                            for (int i = 1; i < graph.arrowGraphics.length; i++) {
                                for (int j = 1; j < graph.arrowGraphics.length; j++) {
                                    if (graph.arrowGraphics[i][j] != null) {
                                        Shape s = graph.arrowGraphics[i][j];
                                        if (s.t1a == arrowPath.get(counter).t1a && s.t1b == arrowPath.get(counter).t1b && s.t1c == arrowPath.get(counter).t1c &&
                                                s.t2a == arrowPath.get(counter).t2a && s.t2b == arrowPath.get(counter).t2b && s.t2c == arrowPath.get(counter).t2c) {
                                            graph.arrowGraphics[i][j] = arrowPath.get(counter);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    generateGraphics();
                    counter++;
                }
            });
            clearTraversal(graph);
            timer.setRepeats(true);
            timer.start();
        }
    }

    public static void clearTraversal(Graph g) {
        origin = 0;
        dest = 0;
        for (int i = 0; i < graph.vertexGraphics.size(); i++) {
            graph.vertexGraphics.set(i, new Shape(graph.vertexGraphics.get(i).num, "node", graph.vertexGraphics.get(i).x, graph.vertexGraphics.get(i).y));
        }
//        for (int i = 0; i < graph.edgeGraphics.size(); i++) {
//            if (!graph.edgeGraphics.get(i).name.equals("arrow"))
//                graph.edgeGraphics.set(i, new Shape("line", graph.edgeGraphics.get(i).x, graph.edgeGraphics.get(i).y,
//                    graph.edgeGraphics.get(i).x2, graph.edgeGraphics.get(i).y2));
//        }
//        graph.mapEdges
//        graph.fillGraphMatrix();
//        graph.addArrows();
        graph.mapEdges();
        generateGraphics();
    }

//    @Override
//    public void mouseClicked(MouseEvent e) {
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseExited(MouseEvent e) {
//
//    }
}
