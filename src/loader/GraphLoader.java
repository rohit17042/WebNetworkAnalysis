package loader;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author : Rohit Chaudhary
 *
 */

public class GraphLoader {

    /**
     * Loads graph with data from a file.
     * The file should consist of lines with 2 integers each, corresponding
     * to a "from" vertex and a "to" vertex.
     */

    public static void loadGraph(graph.Graph g, String filename) {
        Set<Integer> seen = new HashSet<Integer>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Iterate over the lines in the file, adding new
        // vertices as they are found and connecting them with edges.
        while (scanner.hasNextInt()) {
            int node1 = scanner.nextInt();
            int node2 = scanner.nextInt();
            if (!seen.contains(node1)) {
                g.addVertex(node1);
                seen.add(node1);
            }
            if (!seen.contains(node2)) {
                g.addVertex(node2);
                seen.add(node2);
            }
            g.addEdge(node1, node2);
        }

        scanner.close();
    }

}
