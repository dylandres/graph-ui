import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class Graph {

    int numVertices;
    double densityFactor; // 0.0 - 1.0
    int numEdges;
    boolean directed;
    int[][] matrix;
    LinkedList<Integer>[] edgeList;
    HashMap<Integer, CoordinatePair> nodeLocations; // Node number -> (x, y)
    ArrayList<Shape> vertexGraphics; // Shape stream for vertices
    ArrayList<Shape> edgeGraphics; // Shape stream for edges

    public Graph(int v, double densityFactor, boolean directed) {
        this.numVertices = v;
        this.densityFactor = densityFactor;
        this.numEdges = calculateNumOfEdges(v, densityFactor);
        this.directed = directed;
        this.matrix = new int[v + 1][v + 1];
        this.edgeList = new LinkedList[v + 1];
        for (int i = 1; i < v + 1; i++) {
            this.edgeList[i] = new LinkedList<>();
        }
        this.createRandomEdges();
        this.initializeGraphics();
    }

    public static int calculateNumOfEdges(int v, double densityFactor) {
        int maxNumOfEdges = (v * (v-1)) / 2;
        return (int)(maxNumOfEdges * densityFactor);
    }

    public void addEdge(int u, int v) {
        this.matrix[u][v] = 1;
        this.edgeList[u].add(v);
        if (!directed) {
            this.matrix[v][u] = 1;
            this.edgeList[v].add(u);
        }
    }

    public void removeEdge(int u, int v) {
        this.matrix[u][v] = 0;
        this.edgeList[u].remove(Integer.valueOf(v));
        if (!directed) {
            this.matrix[v][u] = 0;
            this.edgeList[v].remove(Integer.valueOf(u));
        }
    }

    public boolean isEdge(int u, int v) {
        return this.matrix[u][v] == 1;
    }

    private void createRandomEdges() {
        for (int i = 0; i < this.numEdges; i++) {
            int u;
            int v;
            do {
                u = randomNumberBetween(1, this.numVertices, -1);
                v = randomNumberBetween(1, this.numVertices, u);
            } while (this.isEdge(u, v));
            this.addEdge(u, v);
        }
    }

    public void initializeGraphics() {
        nodeLocations = new HashMap<>(); // Coordinate locations of vertices
        this.mapVertices();
        this.mapEdges();
    }

    public void mapVertices() {
        vertexGraphics = new ArrayList<>();
        HashMap<Integer, Integer> xMap = new HashMap<>();
        HashMap<Integer, Integer> yMap = new HashMap<>();
        for (int i = 1; i <= this.numVertices; i++) { // Create shape stream and location (x, y) for each vertex
            int x;
            int y;
            do {
                x = Graph.randomNumberBetween(300, 600, -1);
                y = Graph.randomNumberBetween(40, 340, -1);
            } while (collides(xMap, yMap, x, y));
            Shape node = new Shape("node", x, y);
            vertexGraphics.add(node);
            xMap.put(x, x);
            yMap.put(y, y);
            nodeLocations.put(i, new CoordinatePair(x, y));
        }
    }

    public void mapEdges() {
        edgeGraphics = new ArrayList<>();
        for (int i = 1; i < this.edgeList.length; i++) { // Create edge lines and arrows
            CoordinatePair iCoords = nodeLocations.get(i);
            for (int j = 0; j < this.edgeList[i].size(); j++) {
                CoordinatePair jCoords = nodeLocations.get(this.edgeList[i].get(j));
                int ix = iCoords.x;
                int iy = iCoords.y;
                int jx = jCoords.x;
                int jy = jCoords.y;
                Shape newShape = new Shape("line", ix + 8, iy + 8, jx + 8, jy + 8);
                edgeGraphics.add(newShape);
//                if (this.directed)
//                    graphics.add(new Shape("arrow", jx + 6, jy + 6));
//                    visibleShapes.add(new Shape("arrow", jx - 2, jx, jx + 2, jy - 2, jy, jy + 2));
            }
        }
    }

    private static boolean collides(HashMap<Integer, Integer> xMap, HashMap<Integer, Integer> yMap, int x, int y) {
        // Returns true if (x, y) is too close to another node location
        for (int i = 0; i <= 9; i++) {
            if (xMap.containsKey(x + i) || xMap.containsKey(x - i)
                    || yMap.containsKey(y + i) || yMap.containsKey(y - i)) {
                return true;
            }
        }
        return false;
    }

    // Generates a random number [a, b] excluding exc, don't exclude anything if exc == -1
    private static int randomNumberBetween(int a, int b, int exc) {
        int num = (int)(Math.random() * (b - a + 1)) + a;
        if (exc == -1)
            return num;
        while (num == exc) {
            num = (int)(Math.random() * (b - a + 1)) + a;
        }
        return num;
    }

    public void changeDensityFactor(double densityFactor) {
        this.densityFactor = densityFactor;
        int newNumEdges = calculateNumOfEdges(this.numVertices, densityFactor);
        int edgesToMake = newNumEdges - this.numEdges;
        this.numEdges = newNumEdges;
        if (edgesToMake == 0)
            return;
        for (int i = 0; i < Math.abs(edgesToMake); i++) {
            int u;
            int v;
            if (edgesToMake < 0) { // Density was decreased, less edges
                do {
                    u = randomNumberBetween(1, this.numVertices, -1);
                    v = randomNumberBetween(1, this.numVertices, u);
                }
                while (!this.isEdge(u, v));
//                System.out.println("(" + u + ", " + v + ")");
//                System.out.println("Before remove: \n" + this.toString() + "\n");
                this.removeEdge(u, v);
//                System.out.println("After remove: \n" + this.toString() + "\n");
            }
            else {
                do {
                    u = randomNumberBetween(1, this.numVertices, -1);
                    v = randomNumberBetween(1, this.numVertices, u);
                }
                while (this.isEdge(u, v));
//                System.out.println("(" + u + ", " + v + ")");
//                System.out.println("Before add: \n" + this.toString() + "\n");
                this.addEdge(u, v);
//                System.out.println("After add: \n" + this.toString() + "\n");
            }
        }
        this.mapEdges();
//        else { // Density was increased, more edges
//            for (int i = 0; i < edgesToMake; i++) {
//                int u;
//                int v;
//                do {
//                    u = randomNumberBetween(1, this.numVertices, -1);
//                    v = randomNumberBetween(1, this.numVertices, u);
//                }
//                while (this.isEdge(u, v));
//                this.addEdge(u, v);
//            }
//        }
//        this.mapEdges();

    }

    public String toString() {
        String str = "";
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[i].length; j++) {
                str += matrix[i][j] + " ";
            }
            str += "\n";
        }
        return str;
    }

//    public static Graph generateRandomGraph(int density) {
//        int q = randomNumberBetween(10, 10, -1);
//        int e = ((q * (q - 1)) / 2);
//        if (densityFactor == 0) {
//            Graph graph = new Graph(q, 0, false);
//            graph.initializeGraphics();
//            return graph;
//        }
//        double factor = densityFactor / 10.0;
//        double ee = e * factor;
//        e = (int) ee;
//        Graph graph = new Graph(q, e, false);
//        for (int i = 0; i < graph.numEdges; i++) {
//            int u;
//            int v;
//            do {
//                u = randomNumberBetween(1, graph.numVertices, -1);
//                v = randomNumberBetween(1, graph.numVertices, u);
//            } while (graph.isEdge(u, v));
//            graph.addEdge(u, v);
//        }
//        graph.initializeGraphics();
//        return graph;
//    }

    public void generateMatrixGraphic(ArrayList<Shape> visibleShapes) {
        for (int i = 1; i < this.matrix.length; i++) {
            for (int j = 1;  j < this.matrix[i].length; j++) {
                if (this.isEdge(i, j)) {
                    Shape newShape = new Shape("edge", j * 20, i * 20);
                    visibleShapes.add(newShape);
                }
                else {
                    Shape newShape = new Shape("non-edge", j * 20, i * 20);
                    visibleShapes.add(newShape);
                }
            }
        }
    }

    public void generateGraphGraphic(ArrayList<Shape> visibleShapes) {
        visibleShapes.addAll(this.edgeGraphics);
        visibleShapes.addAll(this.vertexGraphics);
        visibleShapes.add(new Shape("box", 280, 20));
    }

    public static void main(String[] args) {
        Graph g = new Graph(10, 1.0, false);
        System.out.println(g);
        for (int i = 1; i < g.numVertices + 1; i++) {
            System.out.println(i + ": " + g.edgeList[i]);
        }
        g.removeEdge(5, 6);
        System.out.println(g);
        for (int i = 1; i < g.numVertices + 1; i++) {
            System.out.println(i + ": " + g.edgeList[i]);
        }
    }

}

class CoordinatePair {

    int x;
    int y;

    public CoordinatePair(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
