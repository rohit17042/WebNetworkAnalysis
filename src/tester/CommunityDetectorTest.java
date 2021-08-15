package tester;

import graph.CommunityDetector;
import graph.Graph;
import graph.WebGraph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author : Rohit Chaudhary
 *
 */

class CommunityDetectorTest {
    private Graph barbellGraph;             // undirected graph
    private Graph zacharyKarateClub;        // directed graph

    @BeforeEach
    void createBarbellGraph(){
        barbellGraph = new WebGraph();

        int[] nodes = {1,2,3,4,5,6};
        int[][] edges = {{2,3},{1,3},{1,2,4},{3,5,6},{4,6},{4,5}};

        initializeGraph(barbellGraph, nodes, edges);
    }

    @BeforeEach
    void createZacharyKarateClub(){
        zacharyKarateClub = new WebGraph();

        int[] nodes = new int[34];
        for(int i=1; i<=nodes.length; i++){
            nodes[i-1] = i;
        }
        int[][] edges = {{},{1},{1,2},{1,2,3},{1},{1},{1,5,6},{1,2,3,4},{1,3},{3},{1,5,6},{1},{1,4},{1,2,3,4},{},{},{6,7},{1,2},{},{1,2},{},{1,2},{},{},{},{24,25},{},{3,24,25},{3},{24,27},{2,9},{1,25,26,29},{3,9,15,16,19,21,23,24,30,31,32},{9,10,14,15,16,19,20,21,23,24,27,28,29,30,31,32,33}};

        initializeGraph(zacharyKarateClub, nodes, edges);
    }

    @Test
    void detectCommunityForBarbellGraph() {
        int[] nodes1 = {1,2,3};
        int[][] edges1 = {{2,3},{1,3},{1,2}};

        int[] nodes2 = {4,5,6};
        int[][] edges2 = {{5,6},{4,6},{4,5}};

        Graph community1 = new WebGraph();
        Graph community2 = new WebGraph();

        initializeGraph(community1, nodes1, edges1);
        initializeGraph(community2, nodes2, edges2);

        assertEquals(null, CommunityDetector.detectCommunity(barbellGraph, 0));
        assertEquals(1, CommunityDetector.detectCommunity(barbellGraph, 1).size());
        assertEquals(2, CommunityDetector.detectCommunity(barbellGraph, 2).size());
        assertEquals(6, CommunityDetector.detectCommunity(barbellGraph, 6).size());
        assertEquals(null, CommunityDetector.detectCommunity(barbellGraph, 7));


        List<Graph> communities = CommunityDetector.detectCommunity(barbellGraph, 2);
        assertEquals(false, communities==null);

        for(Graph community : communities){
            HashMap<Integer, HashSet<Integer>> adjListMap = community.exportGraph();
            if(adjListMap.containsKey(1)){
                assertEquals(community1.exportGraph(), adjListMap);
            }else{
                assertEquals(community2.exportGraph(), adjListMap);
            }
        }

    }

    @Test
    void detectCommunityForZacharyKarateClub(){
        int[] nodes1 = {1,2,4,5,6,7,8,11,12,13,14,17,18,20,22};
        int[] nodes2 = {3,9,10,15,16,19,21,23,24,25,26,27,28,29,30,31,32,33,34};

        int[][] edges1 = {{},{1},{1,2},{1},{1},{1,5,6},{1,2,4},{1,5,6},{1},{1,4},{1,2,4},{6,7},{1,2},{1,2},{1,2}};
        int[][] edges2 = {{},{3},{3},{},{},{},{},{},{},{},{24,25},{},{3,24,25},{3},{24,27},{9},{25,26,29},{3,9,15,16,19,21,23,24,30,31,32},{9,10,15,16,19,21,23,24,27,28,29,30,31,32,33}};

        Graph community1 = new WebGraph();
        Graph community2 = new WebGraph();

        initializeGraph(community1, nodes1, edges1);
        initializeGraph(community2, nodes2, edges2);

        assertEquals(null, CommunityDetector.detectCommunity(zacharyKarateClub, 0));
        assertEquals(1, CommunityDetector.detectCommunity(zacharyKarateClub, 1).size());
        assertEquals(2, CommunityDetector.detectCommunity(zacharyKarateClub, 2).size());
        assertEquals(34, CommunityDetector.detectCommunity(zacharyKarateClub, 34).size());
        assertEquals(null, CommunityDetector.detectCommunity(zacharyKarateClub, 35));


        List<Graph> communities = CommunityDetector.detectCommunity(zacharyKarateClub, 2);
        assertEquals(false, communities==null);

//        for(Graph community : communities){
//            HashMap<Integer, HashSet<Integer>> adjListMap = community.exportGraph();
//            if(adjListMap.containsKey(1)){
//                assertEquals(community1.exportGraph().keySet(), adjListMap.keySet());
//            }else{
//                assertEquals(community2.exportGraph().keySet(), adjListMap.keySet());
//            }
//        }

//        System.out.println("Community1:" + communities.get(0).exportGraph());
//        System.out.println("Community2:" + communities.get(1).exportGraph());

    }

    void initializeGraph(Graph graph, int[] nodes, int[][] edges){
        for(int i=0; i<nodes.length; i++){
            graph.addVertex(nodes[i]);
        }

        for(int i=0; i<nodes.length; i++){
            int node1 = nodes[i];
            for(int node2 : edges[i]){
                graph.addEdge(node1, node2);
            }
        }
    }

}