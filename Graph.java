import java.util.*;
import java.util.ArrayList;

public class Graph {
    static int i = 0;
    int numVertices;
    boolean directed;
    boolean weighted;
    private double densityFactor;
    private int numEdges;
    private int[][] matrix;
    private int[][] weights;
    private LinkedList<Integer>[] edgeList;
    HashMap<Integer, CoordinatePair> nodeLocations; // Node number -> (x, y)
    ArrayList<Shape> vertexGraphics; // Shape stream for vertices
    ArrayList<Shape> edgeGraphics; // Shape stream for edges
    ArrayList<Shape> matrixGraphics;
    Shape[][] arrowGraphics;
    private static final int MAX_VERTICES = 25;

    public Graph(int v, double densityFactor, boolean directed, boolean weighted) {
        this.numVertices = v;
        this.directed = directed;
        this.weighted = weighted;
        this.densityFactor = densityFactor;
        this.numEdges = this.calculateNumOfEdges();
        this.matrix = new int[v + 1][v + 1];
        this.weights = new int[v + 1][v + 1];
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

    private int calculateNumOfEdges() {
        int v = this.numVertices;
        double d = this.densityFactor;
        return this.directed ? (int)(v * (v-1) * d) : (int)(((v * (v-1)) / 2) * d);
    }

    private void addEdge(int u, int v) {
        // always give a graph weights, weighted or not. Random edge weight [1, 10]
        int weight = randomNumberBetween(1, 10, -1);
        this.matrix[u][v] = 1;
        this.edgeList[u].add(v);
        this.weights[u][v] = weight;
        if (!directed) {
            this.matrix[v][u] = 1;
            this.edgeList[v].add(u);
            this.weights[v][u] = weight;
        }
    }

    private void removeEdge(int u, int v) {
        this.matrix[u][v] = 0;
        this.edgeList[u].remove(Integer.valueOf(v));
        this.weights[u][v] = 0;
        if (!directed) {
            this.matrix[v][u] = 0;
            this.edgeList[v].remove(Integer.valueOf(u));
            this.weights[v][u] = 0;
        }
    }

    private boolean isEdge(int u, int v) {
        return this.matrix[u][v] == 1;
    }

    public int getEdges() {
        return numEdges;
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

    private void mapVertices() {
        vertexGraphics = new ArrayList<>();
        HashMap<Integer, Integer> xMap = new HashMap<>();
        HashMap<Integer, Integer> yMap = new HashMap<>();
        for (int i = 1; i <= MAX_VERTICES; i++) { // Create shape stream and location (x, y) for each vertex
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
//        for (int i = 1; i < this.edgeList.length; i++) { // Create edge lines and arrows
//            CoordinatePair iCoords = nodeLocations.get(i);
//            for (int j = 0; j < this.edgeList[i].size(); j++) {
//                CoordinatePair jCoords = nodeLocations.get(this.edgeList[i].get(j));
//                int ix = iCoords.x;
//                int iy = iCoords.y;
//                int jx = jCoords.x;
//                int jy = jCoords.y;
//                    Shape newShape = new Shape("line", ix + 8, iy + 8, jx + 8, jy + 8);
//                    edgeGraphics.add(newShape);
//            }
//        }
//        System.out.println(this.toString());
        edgeGraphics = new ArrayList<>();
        for (int i = 1; i < this.matrix.length; i++) {
            for (int j = 1; j < this.matrix.length; j++) {
                if (this.isEdge(i, j)) {
                    CoordinatePair iCoords = nodeLocations.get(i);
                    CoordinatePair jCoords = nodeLocations.get(j);
                    int ix = iCoords.x;
                    int iy = iCoords.y;
                    int jx = jCoords.x;
                    int jy = jCoords.y;
                    if (!weighted) {
                        Shape newShape = new Shape("line", ix + 8, iy + 8, jx + 8, jy + 8);
                        edgeGraphics.add(newShape);
                    }
                    else {
//                        if (directed && isEdge(j, i)) { // draw half
//////                            int halfway = (int)(distance(ix, iy, jx, jy) / 2.0);
////                            System.out.println("midpoint calculation: " + i + " to " + j);
////                            System.out.println("origin: (" + ix + ", " + iy + ")");
////                            System.out.println("dest: (" + jx + ", " + jy + ")");
////                            double midpointX = (ix + jx) / 2.0;
////                            double midpointY = (iy + jy) / 2.0;
////                            System.out.println("new origin: (" + midpointX + ", " + midpointY + ")");
//                            if (ix + iy > jx + jy) {
//                                Shape newShape = new Shape("line" + weights[i][j], ix + 7, iy + 7, jx + 8, jy + 8);
//                                edgeGraphics.add(newShape);
//                            }
//                            else {
//                                Shape newShape = new Shape("line" + weights[i][j], ix + 6, iy + 6, jx + 8, jy + 8);
//                                edgeGraphics.add(newShape);
//                            }
//                        }
//                        else {
                            Shape newShape = new Shape("line" + weights[i][j], ix + 8, iy + 8, jx + 8, jy + 8);
                            edgeGraphics.add(newShape);
//                        }
                    }
                }
            }
        }
        this.addArrows();
        this.fillGraphMatrix();
    }

    public void fillGraphMatrix() { // call this method when changing between weighted / unweighted
        matrixGraphics = new ArrayList<>(); // Create new matrix graphic
        for (int i = 1; i < this.matrix.length; i++) {
            for (int j = 1;  j < this.matrix[i].length; j++) {
                if (this.isEdge(i, j)) {
                    if (!weighted) {
                        matrixGraphics.add(new Shape(0, "edge", j * 17, i * 17));
                    }
                    else {
                        matrixGraphics.add(new Shape(0, "" + weights[i][j], j * 17, i * 17));
                    }
                }
                else {
                    matrixGraphics.add(new Shape(0, "non-edge", j * 17, i * 17));
                }
            }
        }
    }

    public void addArrows() {
        arrowGraphics = new Shape[numVertices + 1][numVertices + 1];
        for (int i = 1; i < this.edgeList.length; i++) { // Create edge lines and arrows
            CoordinatePair iCoords = nodeLocations.get(i);
            for (int j = 0; j < this.edgeList[i].size(); j++) {
                CoordinatePair jCoords = nodeLocations.get(this.edgeList[i].get(j));
                int ix = iCoords.x;
                int iy = iCoords.y;
                int jx = jCoords.x;
                int jy = jCoords.y;
                double ijDistance = distance(ix, iy, jx, jy);
                double theta = 90;
                double size = 5;
                double mainAngle = inverseSin((Math.abs(jy - iy)) / (ijDistance));
                double flat = size * sin(theta);
                double barLength = ijDistance - size * cos(theta);
                double entryAngle = inverseTan(flat / barLength);
                ;
                double topAngle = entryAngle + mainAngle;
                double botAngle = mainAngle - entryAngle;
                double travelDist = Math.sqrt(flat * flat + barLength * barLength) - 18;
                int ax, ay, cx, cy;
                if (ix <= jx) { // right
                    if (jy <= iy) { // right and up
                        ax = (int) (travelDist * cos(topAngle) + ix + 8);
                        cx = (int) (travelDist * cos(botAngle) + ix + 8);
                        ay = (int) (-travelDist * sin(topAngle) + iy + 8);
                        cy = (int) (-travelDist * sin(botAngle) + iy + 8);
                        if (!weighted)
                            arrowGraphics[i][this.edgeList[i].get(j)] = new Shape("arrow", ax, jx + 8, cx, ay, jy + 8, cy);
                        else
                            arrowGraphics[i][this.edgeList[i].get(j)] = new Shape("arrow" + weights[i][this.edgeList[i].get(j)], ax, jx + 8, cx, ay, jy + 8, cy);
                    } else { // right and down
                        ax = (int) (travelDist * cos(topAngle) + ix + 8);
                        cx = (int) (travelDist * cos(botAngle) + ix + 8);
                        ay = (int) (travelDist * sin(topAngle) + iy + 8);
                        cy = (int) (travelDist * sin(botAngle) + iy + 8);
                        if (!weighted)
                            arrowGraphics[i][this.edgeList[i].get(j)] = new Shape("arrow", ax, jx + 8, cx, ay, jy + 8, cy);
                        else
                            arrowGraphics[i][this.edgeList[i].get(j)] = new Shape("arrow" + weights[i][this.edgeList[i].get(j)], ax, jx + 8, cx, ay, jy + 8, cy);
                    }

                } else if (ix > jx) { // left

                    if (jy <= iy) { // left and up
                        ax = (int) (-travelDist * cos(topAngle) + ix + 8);
                        cx = (int) (-travelDist * cos(botAngle) + ix + 8);
                        ay = (int) (-travelDist * sin(topAngle) + iy + 8);
                        cy = (int) (-travelDist * sin(botAngle) + iy + 8);
                        if (!weighted)
                            arrowGraphics[i][this.edgeList[i].get(j)] = new Shape("arrow", ax, jx + 8, cx, ay, jy + 8, cy);
                        else
                            arrowGraphics[i][this.edgeList[i].get(j)] = new Shape("arrow" + weights[i][this.edgeList[i].get(j)], ax, jx + 8, cx, ay, jy + 8, cy);
                    } else { // left and down
                        ax = (int) (-travelDist * cos(topAngle) + ix + 8);
                        cx = (int) (-travelDist * cos(botAngle) + ix + 8);
                        ay = (int) (travelDist * sin(topAngle) + iy + 8);
                        cy = (int) (travelDist * sin(botAngle) + iy + 8);
                        if (!weighted)
                            arrowGraphics[i][this.edgeList[i].get(j)] = new Shape("arrow", ax, jx + 8, cx, ay, jy + 8, cy);
                        else
                            arrowGraphics[i][this.edgeList[i].get(j)] = new Shape("arrow" + weights[i][this.edgeList[i].get(j)], ax, jx + 8, cx, ay, jy + 8, cy);
                    }
                }
            }
        }
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    private static double sin(double x) {
        return (Math.sin(Math.toRadians(x)));
    }

    private static double inverseSin(double x) {
        return Math.toDegrees(Math.asin(x));
    }

    private static double cos(double x) {
        return (Math.cos(Math.toRadians(x)));
    }

    private static double inverseTan(double x) {
        return Math.toDegrees(Math.atan(x));
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
        this.weights = new int[numVertices + 1][numVertices + 1];
        this.edgeList = new LinkedList[numVertices + 1];
        for (int i = 1; i < numVertices + 1; i++) {
            this.edgeList[i] = new LinkedList<>();
        }
        this.numEdges = this.calculateNumOfEdges();
        this.createRandomEdges();
        this.mapEdges();
    }

    public void swapWeighted(boolean changeTo) {
        this.weighted = changeTo;
        this.mapEdges();
    }

    public void directedToUndirected() {
        this.directed = false;
        this.numEdges /= 2;
        this.matrix = new int[numVertices + 1][numVertices + 1];
        this.weights = new int[numVertices + 1][numVertices + 1];
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
        this.weights = new int[numVertices + 1][numVertices + 1];
        this.edgeList = new LinkedList[numVertices + 1];
        for (int i = 1; i < numVertices + 1; i++) {
            this.edgeList[i] = new LinkedList<>();
        }
        this.createRandomEdges();
        this.mapEdges();
    }

    public String toString() {
        String str = "MATRIX\n";
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[i].length; j++) {
//                if (!weighted)
                    str += matrix[i][j] + " ";
//                else
//                    str += weights[i][j] + " ";
            }
            str += "\n";
        }
        if (weighted) {
            str += "WEIGHTS\n";
            for (int i = 1; i < matrix.length; i++) {
                for (int j = 1; j < matrix[i].length; j++) {
                    str += weights[i][j] + " ";
                }
                str += "\n";
            }
        }
        return str;
    }

    public void generateMatrixGraphic(ArrayList<Shape> visibleShapes) {
        visibleShapes.addAll(this.matrixGraphics);
    }

    public void generateGraphGraphic(ArrayList<Shape> visibleShapes) {
        visibleShapes.addAll(this.edgeGraphics);
        if (directed) {
            for (int i = 1; i < this.arrowGraphics.length; i++) {
                for (int j = 1; j < this.arrowGraphics.length; j++) {
                    if (arrowGraphics[i][j] != null)
                        visibleShapes.add(arrowGraphics[i][j]);
                }
            }
        }
        visibleShapes.addAll(this.vertexGraphics);
        visibleShapes.add(new Shape(0, "box", 480, 20)); //370
        if (weighted) {
            visibleShapes.add(new Shape(0, "legend", 480, 525));
        }
    }

    public void dfs(int start, ArrayList<Shape> path, ArrayList<Shape> matrixPath, ArrayList<Shape> arrowPath) {
        int origin = start;
        if (origin == 0)
            origin = 1;
        boolean[] visited = new boolean[this.numVertices + 1];
        this.dfsUtil(origin, visited, path, matrixPath, arrowPath);
    }

    private void dfsUtil(int start, boolean[] visited, ArrayList<Shape> path, ArrayList<Shape> matrixPath, ArrayList<Shape> arrowPath) {
        visited[start] = true;
        path.add(new Shape(start, "dfs-node-found", this.nodeLocations.get(start).x, this.nodeLocations.get(start).y));
        matrixPath.add(new Shape(0, "dummy", 0,0));
        arrowPath.add(new Shape(0, "dummy", 0,0));
        LinkedList<Integer> sortedNeigbors = (LinkedList<Integer>) this.edgeList[start].clone();
        sortedNeigbors.sort(Comparator.naturalOrder());
        for (int i = 0; i < sortedNeigbors.size(); i++) {
            int neighbor = sortedNeigbors.get(i);
            if (!visited[neighbor]) {
                path.add(new Shape("dfs-line-newly-visited",  this.nodeLocations.get(start).x+8,  this.nodeLocations.get(start).y+8,
                        this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
                matrixPath.add(new Shape(0, "cell-newly-visited", neighbor * 17, start * 17));
                Shape s = arrowGraphics[start][neighbor];
                arrowPath.add(new Shape("arrow-newly-visited", s.t1a, s.t1b, s.t1c, s.t2a, s.t2b, s.t2c));
                dfsUtil(neighbor, visited, path, matrixPath, arrowPath);
            }
            else {
                path.add(new Shape("dfs-line-already-visited",  this.nodeLocations.get(start).x+8,  this.nodeLocations.get(start).y+8,
                        this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
                matrixPath.add(new Shape(0, "cell-already-visited", neighbor * 17, start * 17));
                Shape s = arrowGraphics[start][neighbor];
                arrowPath.add(new Shape("arrow-already-visited", s.t1a, s.t1b, s.t1c, s.t2a, s.t2b, s.t2c));
            }
            path.add(new Shape("dfs-line",  this.nodeLocations.get(start).x+8,  this.nodeLocations.get(start).y+8,
                    this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
            matrixPath.add(new Shape(0, "cell", neighbor * 17, start * 17));
            Shape s = arrowGraphics[start][neighbor];
            arrowPath.add(new Shape("arrow-done", s.t1a, s.t1b, s.t1c, s.t2a, s.t2b, s.t2c));
        }
        path.add(new Shape(start, "dfs-node-processed", this.nodeLocations.get(start).x, this.nodeLocations.get(start).y));
        matrixPath.add(new Shape(0, "dummy", 0,0));
        arrowPath.add(new Shape(0, "dummy", 0,0));
    }

    public void bfs(int start, ArrayList<Shape> path, ArrayList<Shape> matrixPath, ArrayList<Shape> arrowPath) {
        int origin = start;
        if (origin == 0)
            origin = 1;
        boolean[] visited = new boolean[this.numVertices + 1];
        LinkedList<Integer> queue = new LinkedList<>();
        visited[origin] = true;
        queue.offer(origin);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            path.add(new Shape(node, "dfs-node-found", this.nodeLocations.get(node).x, this.nodeLocations.get(node).y));
            matrixPath.add(new Shape(0, "dummy", 0,0));
            arrowPath.add(new Shape(0, "dummy", 0,0));
            LinkedList<Integer> sortedNeigbors = (LinkedList<Integer>) this.edgeList[node].clone();
            sortedNeigbors.sort(Comparator.naturalOrder());
            for (int i = 0; i < sortedNeigbors.size(); i++) {
                int neighbor = sortedNeigbors.get(i);
                if (!visited[neighbor]) {
                    path.add(new Shape("dfs-line-newly-visited",  this.nodeLocations.get(node).x+8,  this.nodeLocations.get(node).y+8,
                            this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
                    matrixPath.add(new Shape(0, "cell-newly-visited", neighbor * 17, node * 17));
                    Shape s = arrowGraphics[node][neighbor];
                    arrowPath.add(new Shape("arrow-newly-visited", s.t1a, s.t1b, s.t1c, s.t2a, s.t2b, s.t2c));
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
                else {
                    path.add(new Shape("dfs-line-already-visited",  this.nodeLocations.get(node).x+8,  this.nodeLocations.get(node).y+8,
                            this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
                    matrixPath.add(new Shape(0, "cell-already-visited", neighbor * 17, node * 17));
                    Shape s = arrowGraphics[node][neighbor];
                    arrowPath.add(new Shape("arrow-already-visited", s.t1a, s.t1b, s.t1c, s.t2a, s.t2b, s.t2c));
                }
                path.add(new Shape("dfs-line",  this.nodeLocations.get(node).x+8,  this.nodeLocations.get(node).y+8,
                        this.nodeLocations.get(neighbor).x+8, this.nodeLocations.get(neighbor).y+8));
                matrixPath.add(new Shape(0, "cell", neighbor * 17, node * 17));
                Shape s = arrowGraphics[node][neighbor];
                arrowPath.add(new Shape("arrow-done", s.t1a, s.t1b, s.t1c, s.t2a, s.t2b, s.t2c));
            }
            path.add(new Shape(node, "dfs-node-processed", this.nodeLocations.get(node).x, this.nodeLocations.get(node).y));
            matrixPath.add(new Shape(0, "dummy", 0,0));
            arrowPath.add(new Shape(0, "dummy", 0,0));

        }
    }

    public void dijkstra(int origin, int dest) {
        ArrayList<Integer> connected = this.allConnectedNodes(origin);
        ArrayList<Integer> pathFromOrigin = new ArrayList<>();
        int[] dist = new int[numVertices+1];
        Set<Integer> visited = new HashSet<>();
        LinkedList<Integer> candidates = new LinkedList<>();
        int[] parent = new int[numVertices+1];
        // Assume all infinity
        for (int i = 1; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        candidates.add(origin);
        dist[origin] = 0;
        while (visited.size() != connected.size()) {
            int cheapest = Integer.MAX_VALUE;
            int u = candidates.get(0);
            for (int i = 0; i < candidates.size(); i++) {
                if (dist[candidates.get(i)] < cheapest) {
                    cheapest = dist[candidates.get(i)];
                    u = candidates.get(i);
                }
            }
            candidates.remove(Integer.valueOf(u)); // find cheapest in dist out of all in pq
            visited.add(u);
            processNeighbors(u, visited, dist, candidates, parent);
        }

        System.out.println("\npath");
        for (int i = 1; i < dist.length; i++) {
            System.out.println(i + ": " + dist[i]);
        }

        System.out.println("\nparent");
        for (int i = 1; i < parent.length; i++) {
            System.out.println("parent of " + i + ": " + parent[i]);
        }

        int i = dest;
        int last = dest;
        Stack<Integer> stack = new Stack<>();
        while (i != 0) {
            last = i;
            stack.add(i);
            i = parent[i];
        }
        if (last != origin) {
            System.out.println("No path");
        }
        else {
            while (!stack.isEmpty()) {
                pathFromOrigin.add(stack.pop());
            }
            System.out.println("Path: " + pathFromOrigin);
        }
    }

    public void processNeighbors(int u, Set<Integer> visited, int[] dist, LinkedList<Integer> candidates, int[] parent) {
        int edgeDistance = -1;
        int newDistance = -1;
        LinkedList<Integer> sortedNeigbors = (LinkedList<Integer>) this.edgeList[u].clone();
        sortedNeigbors.sort(Comparator.naturalOrder());
        for (int i = 0; i < sortedNeigbors.size(); i++) {
            int neighbor = sortedNeigbors.get(i);
            if (!visited.contains(neighbor)) {
                edgeDistance = weighted ? this.weights[u][neighbor] : this.matrix[u][neighbor];
                newDistance = dist[u] + edgeDistance;
                if (newDistance < 0) {
                    newDistance = Integer.MAX_VALUE;
                }
                if (newDistance < dist[neighbor]) {
                    dist[neighbor] = newDistance;
                    parent[neighbor] = u;
                }
                candidates.add(neighbor);
            }
        }
    }

    public ArrayList<Integer> allConnectedNodes(int origin) {
        ArrayList<Integer> connected = new ArrayList<>();
        boolean[] visited = new boolean[this.numVertices + 1];
        LinkedList<Integer> queue = new LinkedList<>();
        visited[origin] = true;
        queue.offer(origin);
        while (queue.size() != 0) {
            int node = queue.poll();
            connected.add(node);
            for (int i = 0; i < this.edgeList[node].size(); i++) {
                int neighbor = this.edgeList[node].get(i);
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        return connected;
    }


    public static void main(String[] args) {
        Graph g = new Graph(4, 0.8, true, true);
        System.out.println(g);
        g.dijkstra(1, 10); 
        System.out.println(Integer.MAX_VALUE + 1 < 0);
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
