package tester;

import graph.Graph;
import graph.WebGraph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rohit Chaudhary
 *
 */

class WebGraphTest {

    private WebGraph smallGraph;
    private HashMap<Integer, HashSet<Integer>> adjListMap;

    @BeforeEach
    void initialize(){
        smallGraph = new WebGraph();
        adjListMap = new HashMap<>();

        int[] nodes = {18,23,25,32,44,50,65};
        int[][] edges = {{23, 44}, {18,25}, {23,65}, {44,50}, {50}, {}, {23}};

        for(int node : nodes){
            smallGraph.addVertex(node);
            adjListMap.put(node, new HashSet<>());
        }

        for(int i=0; i< nodes.length; i++){
            int from = nodes[i];
            for(int to : edges[i]){
                smallGraph.addEdge(from, to);
                adjListMap.get(from).add(to);
            }
        }

    }

    @Test
    void testGraphValidation() {
        assertEquals(adjListMap, smallGraph.exportGraph());
    }

    @Test
    void getEgonet() {
        // testing getEgonet(25) for "smallGraph"
        HashMap<Integer, HashSet<Integer>> egonet = new HashMap<>();

        egonet.put(25, new HashSet<>());
        egonet.put(23, new HashSet<>());
        egonet.put(65, new HashSet<>());

        egonet.get(25).add(23);
        egonet.get(25).add(65);
        egonet.get(23).add(25);
        egonet.get(65).add(23);

        assertEquals(egonet, smallGraph.getEgonet(25).exportGraph());
    }

    @Test
    void getSCCs() {
        Graph component1 = new WebGraph();
        component1.addVertex(50);

        Graph component2 = new WebGraph();
        component2.addVertex(44);

        Graph component3 = new WebGraph();
        component3.addVertex(65);
        component3.addVertex(25);
        component3.addVertex(23);
        component3.addVertex(18);
        component3.addEdge(65,23);
        component3.addEdge(25,65);
        component3.addEdge(25,23);
        component3.addEdge(23,25);
        component3.addEdge(23,18);
        component3.addEdge(18,23);

        Graph component4 = new WebGraph();
        component4.addVertex(32);

        List<Graph> expectedSCC = new ArrayList<>();
        expectedSCC.add(component1);
        expectedSCC.add(component2);
        expectedSCC.add(component3);
        expectedSCC.add(component4);

        List<Graph> actualSCC = smallGraph.getSCCs();

        assertEquals(expectedSCC.size(), actualSCC.size());

        for(int i=0; i<4; i++){
            HashMap<Integer, HashSet<Integer>> actualMap = actualSCC.get(i).exportGraph();
            HashMap<Integer, HashSet<Integer>> expectedMap = null;

            if(actualMap.containsKey(50)){
                expectedMap = expectedSCC.get(0).exportGraph();
            }else if(actualMap.containsKey(44)){
                expectedMap = expectedSCC.get(1).exportGraph();
            }else if(actualMap.containsKey(65)){
                expectedMap = expectedSCC.get(2).exportGraph();
            }else{
                expectedMap = expectedSCC.get(3).exportGraph();
            }

            assertEquals(expectedMap, actualMap);
        }

    }

}