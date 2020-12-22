import java.util.*;

public class Graph {

    int numVertices;
    boolean directed;
    double densityFactor;
    int numEdges;
    int[][] matrix;
    LinkedList<Integer>[] edgeList;
    HashMap<Integer, CoordinatePair> nodeLocations; // Node number -> (x, y)
    ArrayList<Shape> vertexGraphics; // Shape stream for vertices
    ArrayList<Shape> edgeGraphics; // Shape stream for edges

    public Graph(int v, double densityFactor, boolean directed) {
        this.numVertices = v;
        this.directed = directed;
        this.densityFactor = densityFactor;
        this.numEdges = this.calculateNumOfEdges();
        this.matrix = new int[v + 1][v + 1];
        this.edgeList = new LinkedList[v + 1];
        for (int i = 1; i < v + 1; i++) {
            this.edgeList[i] = new LinkedList<>();
        }
        this.createRandomEdges();
        this.initializeGraphics();
    }

    private void initializeGraphics() {
        nodeLocations = new HashMap<>(); // Coordinate locations of vertices
        this.mapVertices();
        this.mapEdges();
    }

    public int calculateNumOfEdges() {
        int v = this.numVertices;
        double d = this.densityFactor;
        return this.directed ? (int)(v * (v-1) * d) : (int)(((v * (v-1)) / 2) * d);
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

    public void mapVertices() {
        vertexGraphics = new ArrayList<>();
        HashMap<Integer, Integer> xMap = new HashMap<>();
        HashMap<Integer, Integer> yMap = new HashMap<>();
        for (int i = 1; i <= 25; i++) { // Create shape stream and location (x, y) for each vertex
            int x;
            int y;
            do {
                x = Graph.randomNumberBetween(500, 960, -1);
                y = Graph.randomNumberBetween(40, 500, -1);
            } while (collides(xMap, yMap, x, y));
            Shape node = new Shape(i, "node", x, y);
            if (i <= this.numVertices)
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
                if (this.directed)
                    edgeGraphics.add(new Shape(0, "arrow", jx + 6, jy + 6));
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
    public static int randomNumberBetween(int a, int b, int exc) {
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
        int newNumEdges = this.calculateNumOfEdges();
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
                this.removeEdge(u, v);
            }
            else {
                do {
                    u = randomNumberBetween(1, this.numVertices, -1);
                    v = randomNumberBetween(1, this.numVertices, u);
                }
                while (this.isEdge(u, v));
                this.addEdge(u, v);
            }
        }
        this.mapEdges();
    }

    public void changeVertexCount(int count) {
        vertexGraphics = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Shape node = new Shape(i, "node", nodeLocations.get(i).x, nodeLocations.get(i).y);
            vertexGraphics.add(node);
        }
        this.numVertices = count;
        this.matrix = new int[numVertices + 1][numVertices + 1];
        this.edgeList = new LinkedList[numVertices + 1];
        for (int i = 1; i < numVertices + 1; i++) {
            this.edgeList[i] = new LinkedList<>();
        }
        this.numEdges = this.calculateNumOfEdges();
        this.createRandomEdges();
        this.mapEdges();
    }

    public void directedToUndirected() {
        this.directed = false;
        this.numEdges /= 2;
        this.matrix = new int[numVertices + 1][numVertices + 1];
        this.edgeList = new LinkedList[numVertices + 1];
        for (int i = 1; i < numVertices + 1; i++) {
            this.edgeList[i] = new LinkedList<>();
        }
        this.createRandomEdges();
        this.mapEdges();
    }

    public void undirectedToDirected() {
        this.directed = true;
        this.numEdges *= 2;
        this.matrix = new int[numVertices + 1][numVertices + 1];
        this.edgeList = new LinkedList[numVertices + 1];
        for (int i = 1; i < numVertices + 1; i++) {
            this.edgeList[i] = new LinkedList<>();
        }
        this.createRandomEdges();
        this.mapEdges();
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

    public void generateMatrixGraphic(ArrayList<Shape> visibleShapes) {
        for (int i = 1; i < this.matrix.length; i++) {
            for (int j = 1;  j < this.matrix[i].length; j++) {
                if (this.isEdge(i, j)) {
                    Shape newShape = new Shape(0, "edge", j * 17, i * 17);
                    visibleShapes.add(newShape);
                }
                else {
                    Shape newShape = new Shape(0, "non-edge", j * 17, i * 17);
                    visibleShapes.add(newShape);
                }
            }
        }
    }

    public void generateGraphGraphic(ArrayList<Shape> visibleShapes) {
        visibleShapes.addAll(this.edgeGraphics);
        visibleShapes.addAll(this.vertexGraphics);
        visibleShapes.add(new Shape(0, "box", 480, 20)); //370
    }

    public ArrayList<Shape> dfs(int start) {
        ArrayList<Shape> path = new ArrayList<>();
        boolean[] visited = new boolean[this.numVertices + 1];
        this.dfsUtil(start, visited, path);
        return path;
    }

    public void dfsUtil(int start, boolean[] visited, ArrayList<Shape> path) {
        visited[start] = true;
        path.add(new Shape(start, "dfs-node-found", this.nodeLocations.get(start).x, this.nodeLocations.get(start).y));
        LinkedList<Integer> sortedNeigbors = (LinkedList<Integer>) this.edgeList[start].clone();
        sortedNeigbors.sort(Comparator.naturalOrder());
        for (int i = 0; i < sortedNeigbors.size(); i++) {
            int neighbor = sortedNeigbors.get(i);
            if (!visited[neighbor]) {
                path.add(new Shape("dfs-line-newly-visited",  this.nodeLocations.get(start).x+8,  this.nodeLocations.get(start).y+8,
                        this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
                dfsUtil(neighbor, visited, path);
            }
            else {
                path.add(new Shape("dfs-line-already-visited",  this.nodeLocations.get(start).x+8,  this.nodeLocations.get(start).y+8,
                        this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
            }
            path.add(new Shape("dfs-line",  this.nodeLocations.get(start).x+8,  this.nodeLocations.get(start).y+8,
                    this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
        }
        path.add(new Shape(start, "dfs-node-processed", this.nodeLocations.get(start).x, this.nodeLocations.get(start).y));

    }

    public ArrayList<Shape> bfs(int start) {
        ArrayList<Shape> path = new ArrayList<>();
        boolean[] visited = new boolean[this.numVertices + 1];
        LinkedList<Integer> queue = new LinkedList<>();
        visited[start] = true;
        queue.offer(start);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            path.add(new Shape(node, "dfs-node-found", this.nodeLocations.get(node).x, this.nodeLocations.get(node).y));
            LinkedList<Integer> sortedNeigbors = (LinkedList<Integer>) this.edgeList[node].clone();
            sortedNeigbors.sort(Comparator.naturalOrder());
            for (int i = 0; i < sortedNeigbors.size(); i++) {
                int neighbor = sortedNeigbors.get(i);
                if (!visited[neighbor]) {
                    path.add(new Shape("dfs-line-newly-visited",  this.nodeLocations.get(node).x+8,  this.nodeLocations.get(node).y+8,
                            this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
                else {
                    path.add(new Shape("dfs-line-already-visited",  this.nodeLocations.get(node).x+8,  this.nodeLocations.get(node).y+8,
                            this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
                }
                path.add(new Shape("dfs-line",  this.nodeLocations.get(node).x+8,  this.nodeLocations.get(node).y+8,
                        this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
            }
            path.add(new Shape(node, "dfs-node-processed", this.nodeLocations.get(node).x, this.nodeLocations.get(node).y));

        }
        return path;
    }


    public static void main(String[] args) {
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
