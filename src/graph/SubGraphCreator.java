package graph;

import java.util.*;

/**
 *
 * @author: Rohit Chaudhary
 *
 */

public class SubGraphCreator {

    public static Graph createSubGraph(Graph graph, List<Integer> nodeList){
        List<List<Integer>> edgeList = getEdges(graph, nodeList);

        Graph subGraph = new WebGraph();

        for(int id : nodeList){
            subGraph.addVertex(id);
        }

        for(List<Integer> edge : edgeList){
            int node1 = edge.get(0);
            int node2 = edge.get(1);

            subGraph.addEdge(node1, node2);
        }

        return subGraph;
    }

    private static List<List<Integer>> getEdges(Graph graph, List<Integer> nodeList){
        List<List<Integer>> edgeList = new ArrayList<>();
        HashMap<Integer, HashSet<Integer>> adjacencyListMap = graph.exportGraph();

        for(int node1 : nodeList){
            for(int node2 : nodeList){
                if(adjacencyListMap.get(node1).contains(node2)){
                    List<Integer> edge = new ArrayList<>();
                    edge.add(node1);
                    edge.add(node2);
                    edgeList.add(edge);
                }
            }
        }

        return edgeList;
    }

}

