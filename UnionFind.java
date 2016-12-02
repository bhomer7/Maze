
/**
 * Implementation of a UnionFind data structure. Contains an array of disjoint
 * sets. Uses path compression and smart union to keep find operations fast.
 * Supports combining two sets, finding the set a value is in, clearing the set,
 * finding how many disjoint sets there are in the structure, as well as
 * printing various information about the structure and its performance so far.
 *
 * Each element in the array contains the index of another element in the array,
 * unless it is a root. Root indices contain a negative integer with value equal
 * in magnitude to the number of elements in the set.
 *
 * It is based on the skeleton provided by Ivor Page.
 *
 * @author Benjamin Homer
 */
class UnionFind {

    int[] arr;
    int numFinds;
    int pathLength;

    UnionFind(int n) {
        arr = new int[n];
        numFinds = 0;
        pathLength = 0;
        this.clear();
    }

    /**
     * Combines the sets containing i and j. If the sets are the same size, the
     * set containing j is placed in the set containing i. If the sets are
     * different sizes, the smaller set is placed into the larger set. If i and
     * j are already in the same set, the union does not take place.
     *
     * @param i The value in a set to be combined. The default root set.
     * @param j The value in a set to be combined.
     *
     * @return Whether the union actually happened or not.
     */
    boolean union(int i, int j) {
        int iset = find(i);
        int jset = find(j);
        if(iset == jset) {
            return false;
        }
        if(arr[iset] > arr[jset]) {
            arr[jset] += arr[iset];
            arr[iset] = jset;
        }
        else {
            arr[iset] += arr[jset];
            arr[jset] = iset;
        }
        return true;
    }

    /**
     * Finds the index of the root of the set containing y. Employs path
     * compression after finding the root index to shorten later finds. Keeps
     * track of the total number of external calls to find, and the path length
     * of all calls to find in order to track the average path length.
     *
     * @param y The value in a set to find the root of.
     *
     * @return The root of the set.
     */
    int find(int y) {
        pathLength++;
        if(arr[y] < 0) {
            pathLength--;
            numFinds++;
            return y;
        }
        int root = find(arr[y]);
        if(arr[y] != root) {
            arr[y] = root;
        }
        return root;
    }

    /**
     * Gets the number of disjoint sets in the structure. Linearly searches the
     * array for any negative values. Each negative value represents a different
     * set.
     *
     * @return The number of sets.
     */
    int sets() {
        int s = 0;
        for(int n: arr) {
            s += (n<0) ? 1 : 0;
        }
        return s;
    }

    /**
     * Clears all sets. Resets the structure to be only disjoint sets of size 
     * one.
     */
    void clear() {
        for(int i=0; i<arr.length; i++)
            arr[i] = -1;
    }

    /**
     * Prints the array containing the sets as rows of 20 space separated
     * integers. Each integer is the value contained in its position index of
     * the array.
     */
    void printArray() {
        for(int i=0; i<arr.length; i++) {
            System.out.print(arr[i] + " ");
            if((i+1)%20==0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * Prints all sets in the structure. One set per line of space separated
     * integers. The values are the indices of the array in the same set in
     * increasing order.
     */
    void printConnections() {
        String[] sets = new String[arr.length];
        for(int i=0; i<arr.length; i++) {
            sets[i] = "";
        }
        for(int i=0; i<arr.length; i++) {
            int set = find(i);
            sets[set] = sets[set]+i+" ";
        }
        for(String set: sets) {
            if(!set.equals("")) {
                System.out.println(set);
            }
        }
    }

    /**
     * Prints statistics about the performance of the structure. Line one is
     * the number of disjoint sets remaining. Line two is the average path
     * length for a find operation.
     */
    void printStats() {
        System.out.printf("Number of disjoint sets remaining = %4d%n", this.sets());
        System.out.printf("Mean path length of all find operations = %2.2f%n", (double)pathLength/numFinds);
    }

}

