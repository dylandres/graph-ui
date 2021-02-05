public class Shape extends Drawable {

    int num;
    String name;
    int x, y;
    int x2, y2;
    int t1a, t1b, t1c, t2a, t2b, t2c;

    // Polygons
    public Shape(int num, String name, int x, int y) {
        this.num = num;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    // Lines
    public Shape(String name, int x,  int y, int x2, int y2) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    // Triangle - test
    public Shape(String name, int t1a, int t1b, int t1c, int t2a, int t2b, int t2c) {
        this.name = name;
        this.t1a = t1a;
        this.t1b = t1b;
        this.t1c = t1c;
        this.t2a = t2a;
        this.t2b = t2b;
        this.t2c = t2c;

    }

    public String toString() {
        String s = "none";
        switch(name) {
            case "node":
                s = "Node: " + "(" + x + ", " + y + ")";
                break;
            case "dfs-node":
                s = "DFS-Node: " + "(" + x + ", " + y + ")";
                break;
            case "line":
                s = "Line: " + "[(" + x + ", " + y + "), (" + x2 + ", " + y2 + ")]";
                s = "PINK";
                break;
            case "dfs-line":
                s = "DFS-Line: " + "[(" + x + ", " + y + "), (" + x2 + ", " + y2 + ")]";
                break;
            case "dfs-line-newly-visited":
                s = "DFS-Line-NEW: " + "[(" + x + ", " + y + "), (" + x2 + ", " + y2 + ")]";
                break;
            case "dfs-line-already-visited":
                s = "DFS-Line-VISITED: " + "[(" + x + ", " + y + "), (" + x2 + ", " + y2 + ")]";
                break;
            case "arrow":
                s = "ARROW <-------------- " + "x: " + x + ", y: " + y;
                break;
            default:
                s = "COLOR " + name;
                break;
        }
        return s;
    }

}
