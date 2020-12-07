import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class Graph {

    int numVertices;
    int edges;
    int[][] matrix;
    LinkedList<Integer>[] edgeList;
    boolean directed;
    HashMap<Integer, CoordinatePair> nodeLocations;
    ArrayList<Shape> graphics;
    static int densityFactor = 5;

    public Graph(int v, int e, boolean directed) {
        this.numVertices = v;
        this.edges = e;
        this.matrix = new int[v + 1][v + 1];
        this.edgeList = new LinkedList[v + 1];
        for (int i = 1; i < v + 1; i++) {
            this.edgeList[i] = new LinkedList<>();
        }
        this.directed = directed;
    }

    public void addEdge(int u, int v) {
        this.matrix[u][v] = 1;
        this.edgeList[u].add(v);
        if (!directed) {
            this.matrix[v][u] = 1;
            this.edgeList[v].add(u);
        }
    }

    public boolean isEdge(int u, int v) {
        return this.matrix[u][v] == 1;
    }

    public void createGraphics() {
        nodeLocations = new HashMap<>();
        graphics = new ArrayList<>();
        HashMap<Integer, Integer> xMap = new HashMap<>();
        HashMap<Integer, Integer> yMap = new HashMap<>();
        for (int i = 1; i <= this.numVertices; i++) {
            int x;
            int y;
            do {
                x = Graph.randomNumberBetween(300, 600, -1);
                y = Graph.randomNumberBetween(40, 340, -1);
            } while (collides(xMap, yMap, x, y));
            Shape newShape = new Shape("node", x, y);
            graphics.add(newShape);
            xMap.put(x, x);
            yMap.put(y, y);
            nodeLocations.put(i, new CoordinatePair(x, y));
        }
        for (int i = 1; i < this.edgeList.length; i++) {
            for (int j = 0; j < this.edgeList[i].size(); j++) {
                CoordinatePair iCoords = nodeLocations.get(i);
                CoordinatePair jCoords = nodeLocations.get(this.edgeList[i].get(j));
                int ix = iCoords.x;
                int iy = iCoords.y;
                int jx = jCoords.x;
                int jy = jCoords.y;
                Shape newShape = new Shape("line", ix + 8, iy + 8, jx + 8, jy + 8);
                graphics.add(newShape);
                if (this.directed)
                    graphics.add(new Shape("arrow", jx + 6, jy + 6));
//                    visibleShapes.add(new Shape("arrow", jx - 2, jx, jx + 2, jy - 2, jy, jy + 2));
            }
        }
    }

    private static boolean collides(HashMap<Integer, Integer> xMap, HashMap<Integer, Integer> yMap, int x, int y) {
        for (int i = 0; i <= 9; i++) {
            if (xMap.containsKey(x + i) || xMap.containsKey(x - i)
                    || yMap.containsKey(y + i) || yMap.containsKey(y - i)) {
                return true;
            }
        }
        return false;
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

    public static Graph generateRandomGraph(int density) {
        int q = randomNumberBetween(10, 10, -1);
        int e = ((q * (q - 1)) / 2);
        if (densityFactor == 0) {
            Graph graph = new Graph(q, 0, false);
            graph.createGraphics();
            return graph;
        }
        double factor = densityFactor / 10.0;
        double ee = e * factor;
        e = (int) ee;
        Graph graph = new Graph(q, e, false);
        for (int i = 0; i < graph.edges; i++) {
            int u;
            int v;
            do {
                u = randomNumberBetween(1, graph.numVertices, -1);
                v = randomNumberBetween(1, graph.numVertices, u);
            } while (graph.isEdge(u, v));
            graph.addEdge(u, v);
        }
        graph.createGraphics();
        return graph;
    }

    // Generates a random number [a, b] excluding exc
    private static int randomNumberBetween(int a, int b, int exc) {
        int num = (int)(Math.random() * (b - a + 1)) + a;
        if (exc == -1)
            return num;
        while (num == exc) {
            num = (int)(Math.random() * (b - a + 1)) + a;
        }
        return num;
    }

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
        visibleShapes.addAll(this.graphics);
        Shape newShape = new Shape("box", 280, 20);
        visibleShapes.add(newShape);
    }

    public static void main(String[] args) {
//        Graph graph = generateRandomGraph();
//        System.out.println(graph.vertices);
//        System.out.println(graph.edges);
//        for (int i = 1; i < graph.vertices + 1; i++) {
//            System.out.println(i + ": " + graph.edgeList[i].toString());
//        }
//        System.out.println(graph);
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            nums.add(randomNumberBetween(600, 700, -1));
        }
        nums.sort(Comparator.naturalOrder());
        System.out.println(nums);
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
