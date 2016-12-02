
/**
 * A three dimensional maze. Uses a UnionFind to construct the maze with a
 * single possible path from any point to any other point in the maze. Displays
 * the maze as an adjacency list with weighted edges.
 *
 * It is based on the skeleton provided by Ivor Page.
 *
 * @author Benjamin Homer
 */
public class Maze {

    public static final char BLOCK = '\u2588'; //unicode full block
    public static final char PATH = ' ';

    private UnionFind maze;
    private AdjacencyEntry[] adjacencyList;
    private int width,depth,height;
    //number left to right, back to front, top to bottom
    //north is back



    /**
     * One line entry in the Adjacency List for the vertices of the maze.
     * Stores the line as a seven element integer array. The first element is
     * connected to the second through fourth elements with respective weights
     * in the fifth through seventh elements.
     */
    class AdjacencyEntry {
        private int[] line;
        private int numEdges;

        public AdjacencyEntry(int i) {
            line = new int[7];
            line[0] = i;
            numEdges = 0;
        }

        public void addEdge(int e) {
            numEdges++;
            line[numEdges] = e;
            addWeight();
        }

        private void addWeight() {
            line[numEdges+3] = (int)(Math.random()*20)+1;
        }

        public String toString() {
            switch(numEdges) {
                case 0:
                    return "\r";
                case 1:
                    return String.format("%d %d %d%n", line[0], line[1], line[4]);
                case 2:
                    return String.format("%d %d %d %d %d%n", line[0], line[1], line[2], line[4], line[5]);
                case 3:
                    return String.format("%d %d %d %d %d %d %d%n", line[0], line[1], line[2], line[3], line[4], line[5], line[6]);
            }
            return null;
        }
    }


    /**
     * Creates a maze with length width and height as passed. Does so by 
     * initializing a UnionFind with size equal to the number of vertices in
     * the maze, and then adding the number of vertices minus one edges.
     */
    public Maze(int w, int d, int h) {
        width = w;
        depth = d;
        height = h;
        maze = new UnionFind(w*d*h);
        adjacencyList = new AdjacencyEntry[w*d*h];
        for(int i=1; i<maze.arr.length; i++) {
            this.addEdge();
            //System.out.printf(" %d edges\r", i);
            //this.adjacencyList();
        }
    }

    /**
     * Adds a random edge to the maze. Chooses a random point in the maze, then
     * chooses a random direction. The edge from the first chosen point to the
     * vertex in the chosen direction is checked to see if it is within the
     * maze. The edge is then added to the maze if it is not already there.
     */
    private void addEdge() {
        int i,j;
        do {
            i = (int)(Math.random()*maze.arr.length);
            do{
                int d = (int)(Math.random()*6);
                switch(d) {
                    case 0: //up
                        j = i-width*depth;
                        break;
                    case 1: //north
                        j = i-width;
                        break;
                    case 2: //east
                        j = i+1;
                        break;
                    case 3: //south
                        j = i+width;
                        break;
                    case 4: //west
                        j = i-1;
                        break;
                    case 5: //down
                        j = i+width*depth;
                        break;
                    default:
                        j = i;
                }
            }while(!validEdge(i,j));
        }while(!maze.union(i,j));
        if(i<j) {
            if(adjacencyList[i]==null)
                adjacencyList[i] = new AdjacencyEntry(i);
            adjacencyList[i].addEdge(j);
            //adjacencyList[i].print();
        }
        else {
            if(adjacencyList[j]==null)
                adjacencyList[j] = new AdjacencyEntry(j);
            adjacencyList[j].addEdge(i);
            //adjacencyList[j].print();
        }
    }

    /**
     * Checks whether the two points passed are bnoth within the maze and are
     * adjacent to each other. 
     *
     * @param i The start point of the edge. Is guarunteed to be within the maze
     * @param j The end point of the edge.
     *
     * @return Whether the two points passed make a valid edge.
     */
    private boolean validEdge(int i, int j) {
        if(j==i)
            return false;
        //too far up
        if(j<0)
            return false;
        //too far down
        if(j>=maze.arr.length)
            return false;
        //too far north or south
        if(i%width==j%width && i/(width*depth)!=j/(width*depth) && (j==i+width || j==i-width))
            return false;
        //too far east
        if(i%width==width-1 && j==i+1)
            return false;
        //too far west
        if(i%width==0 && j==i-1)
            return false;
        return true;
    }

    /**
     * Prints the adjacency list.
     */
    public void printAdjacencyList() {
        for(int i=0; i<adjacencyList.length; i++) {
            if(adjacencyList[i] != null)
                System.out.print(adjacencyList[i]);
        }
    }

    /**
     * @return The adjacency list array.
     */
    public AdjacencyEntry[] getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * If the maze is 2 dimensional, returns a character array rendering of the
     * maze. If the maze is 3 dimensional, returns null.
     *
     * @return A 2d char array representing the maze.
     */
    public char[][] getFullArray() {
        if(height != 1) {
            return null;
        }
        char[][] fullMaze = new char[2*depth-1][2*width-1];
        for(int i=0; i<fullMaze.length; i++) {
            for(int j=0; j<fullMaze[i].length; j++) {
                fullMaze[i][j] = (j%2==0) ? ((i%2==0) ? ' ' : this.BLOCK) : this.BLOCK;
            }
        }

        for(AdjacencyEntry a: adjacencyList) {
            if(a != null) {
                String[] command = a.toString().split(" ");
                int start = Integer.parseInt(command[0]);
                for(int i=1; i<=command.length/2; i++) {
                    int diff = Integer.parseInt(command[i])-start;
                    if(diff == 1) {
                        fullMaze[start/width*2][start%width*2+1] = this.PATH;
                    }
                    else if(diff == width) {
                        fullMaze[start/width*2+1][start%width*2] = this.PATH;
                    }
                    else {
                        System.out.println("BROKEN INPUT: " + (Integer.parseInt(command[i])-start));
                    }
                }
            }
        }
        return fullMaze;
    }

}
