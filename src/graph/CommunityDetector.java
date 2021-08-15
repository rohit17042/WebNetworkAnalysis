package graph;

import java.util.*;

/**
 *
 * @author : Rohit Chaudhary
 *
 */

public class CommunityDetector {

    public static List<Graph> detectCommunity(Graph graph, int targetNumOfCommunities){
        // max no. of community possible for any graph = total no. of nodes of that graph
        int maxCommunityPossible = graph.getNumOfNodes();

        // we can't split any graph into more than total no. of nodes, so returning null
        if(targetNumOfCommunities > maxCommunityPossible)
            return null;

        List<Node> nodeList = new ArrayList<>();
        Map<Node, List<Edge>> outgoingEdgeMap = new HashMap<>();
        Map<Node, List<Edge>> incomingEdgeMap = new HashMap<>();
        getAllNodesAndEdges(graph, nodeList, outgoingEdgeMap, incomingEdgeMap);

        int currNumOfCommunity = countNumberOfCommunity(nodeList, outgoingEdgeMap, incomingEdgeMap);

        // if target no. of community < current no. of community; we can't split anymore, so returning null
        if(targetNumOfCommunities < currNumOfCommunity)
            return null;

//        System.out.println("initialCommunityCount:" + currNumOfCommunity);

        Graph cloneGraph = SubGraphCreator.createSubGraph(graph, createNodeIdList(nodeList));

        while(currNumOfCommunity != targetNumOfCommunities){
            Edge edgeToRemove = findNextEdgeCandidate(cloneGraph, nodeList, outgoingEdgeMap);
//            System.out.print(edgeToRemove + ", ");
            removeEdge(cloneGraph, edgeToRemove, outgoingEdgeMap, incomingEdgeMap);
            currNumOfCommunity = countNumberOfCommunity(nodeList, outgoingEdgeMap, incomingEdgeMap);
//            System.out.println("CurrCommunityCount:" + currNumOfCommunity);
        }

        List<Graph> communityList = new ArrayList<>();
        List<List<Integer>> list = getCommunityNodesList(nodeList, outgoingEdgeMap, incomingEdgeMap);
//        System.out.println("CurrCommunities:" + list);

        for(List<Integer> communityNodeList : list){
            Graph community = SubGraphCreator.createSubGraph(cloneGraph, communityNodeList);
            communityList.add(community);
        }

        return communityList;
    }

    private static void getAllNodesAndEdges(Graph graph, List<Node> nodeList, Map<Node, List<Edge>> outgoingEdgeMap, Map<Node, List<Edge>> incomingEdgeMap){
        HashMap<Integer, HashSet<Integer>> adjacencyListMap = graph.exportGraph();
        Map<Integer, Node> nodeMap = new HashMap<>();

        for(int id : adjacencyListMap.keySet()){
            Node node = new Node(id);
            nodeMap.put(id, node);
            nodeList.add(node);
            outgoingEdgeMap.put(node, new ArrayList<>());
            incomingEdgeMap.put(node, new ArrayList<>());
        }

        for(int from : adjacencyListMap.keySet()){
            for(int to : adjacencyListMap.get(from)){
                Node sourceNode = nodeMap.get(from);
                Node destinationNode = nodeMap.get(to);
                Edge edge = new Edge(sourceNode, destinationNode);
                outgoingEdgeMap.get(sourceNode).add(edge);
                incomingEdgeMap.get(destinationNode).add(edge);
            }
        }

    }

    private static List<Integer> createNodeIdList(List<Node> nodeList){
        List<Integer> nodeIdList = new ArrayList<>();

        for(Node node : nodeList){
            nodeIdList.add(node.getId());
        }

        return nodeIdList;
    }

    private static int countNumberOfCommunity(List<Node> nodeList, Map<Node, List<Edge>> outgoingEdgeMap, Map<Node, List<Edge>> incomingEdgeMap){
        Set<Node> visited = new HashSet<>();
        int count = 0;

        for(Node currNode : nodeList){
            if(!visited.contains(currNode)){
                explore(currNode, outgoingEdgeMap, incomingEdgeMap, visited);
                count++;
            }
        }

        return count;
    }

    private static List<List<Integer>> getCommunityNodesList(List<Node> nodeList, Map<Node, List<Edge>> outgoingEdgeMap, Map<Node, List<Edge>> incomingEdgeMap){
        Set<Node> visited = new HashSet<>();
        List<List<Integer>> communityNodeList = new ArrayList<>();

        for(Node currNode : nodeList){
            if(!visited.contains(currNode)){
                List<Integer> list = new ArrayList<>();
                explore(currNode, outgoingEdgeMap, incomingEdgeMap, visited, list);
                communityNodeList.add(list);
            }
        }

        return communityNodeList;
    }

    private static Edge findNextEdgeCandidate(Graph graph, List<Node> nodeList, Map<Node, List<Edge>> outgoingEdgeMap){
        Map<Edge, Double> betweennessMap = computeEdgeBitweennessCentrality(graph, nodeList, outgoingEdgeMap);

        Edge edgeToRemove = null;
        double centralityValue = Double.MIN_VALUE;

        for(Edge currEdge : betweennessMap.keySet()){
            double currEdgeCentrality = betweennessMap.get(currEdge);
            if(currEdgeCentrality > centralityValue){
                edgeToRemove = currEdge;
                centralityValue = currEdgeCentrality;
            }
        }

        return edgeToRemove;
    }

    private static void removeEdge(Graph graph, Edge edgeToRemove, Map<Node, List<Edge>> outgoingEdgeMap, Map<Node, List<Edge>> incomingEdgeMap){
        Node sourceNode = edgeToRemove.getSourceNode();
        Node destinationNode = edgeToRemove.getDestinationNode();

        for(Edge edge : outgoingEdgeMap.get(sourceNode)){
            if(edge.getDestinationNode() == destinationNode){
                outgoingEdgeMap.get(sourceNode).remove(edge);
                incomingEdgeMap.get(destinationNode).remove(edge);
                break;
            }
        }

        graph.removeEdge(sourceNode.getId(), destinationNode.getId());
    }

    private static Map<Edge, Double> computeEdgeBitweennessCentrality(Graph graph, List<Node> nodeList, Map<Node, List<Edge>> outgoingEdgeMap){
        if(graph==null)
            return null;

        // initializes all edges betweenness centrality to 0.0
        Map<Edge, Double> betweennessMap = new HashMap<>();
        for(Node startNode : outgoingEdgeMap.keySet()){
            for(Edge edge : outgoingEdgeMap.get(startNode)){
                betweennessMap.put(edge, 0.0);
            }
        }

        for(Node startNode : nodeList){
            Map<Node, Integer> shortestPathCountMap = new HashMap<>();
            Map<Node, Integer> shortestPathLengthMap = new HashMap<>();
            Map<Node, List<Edge>> shortestPathIncomingEdgeMap = new HashMap<>();

            initializeMaps(startNode, nodeList, shortestPathCountMap, shortestPathLengthMap, shortestPathIncomingEdgeMap);
            Stack<Node> stack = bfs(startNode, outgoingEdgeMap, shortestPathCountMap, shortestPathLengthMap, shortestPathIncomingEdgeMap);
            updateBetweennessMap(stack, outgoingEdgeMap, shortestPathCountMap, shortestPathIncomingEdgeMap, betweennessMap);
        }

        return betweennessMap;
    }

    private static void initializeMaps(Node startNode, List<Node> nodeList, Map<Node, Integer> shortestPathCountMap, Map<Node, Integer> shortestPathLengthMap, Map<Node, List<Edge>> shortestPathIncomingEdgeMap){
        for(Node node : nodeList){
            shortestPathCountMap.put(node, 0);
            shortestPathLengthMap.put(node, -1);
            shortestPathIncomingEdgeMap.put(node, new ArrayList<>());
        }

        shortestPathCountMap.put(startNode, 1);
        shortestPathLengthMap.put(startNode, 0);
    }

    private static Stack<Node> bfs(Node startNode, Map<Node, List<Edge>> outgoingEdgeMap, Map<Node, Integer> shortestPathCountMap, Map<Node, Integer> shortestPathLengthMap, Map<Node, List<Edge>> shortestPathIncomingEdgeMap){
        // stack stores the traversed nodes in reverse order
        Stack<Node> stack = new Stack<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(startNode);

        while(!queue.isEmpty()){
            Node predecessorNode = queue.poll();
            stack.push(predecessorNode);

            for(Edge edge : outgoingEdgeMap.get(predecessorNode)){
                Node destinationNode = edge.getDestinationNode();

                if(shortestPathLengthMap.get(destinationNode)<0){
                    queue.add(destinationNode);
                    shortestPathLengthMap.put(destinationNode, shortestPathLengthMap.get(predecessorNode) + 1);
                }

                if(shortestPathLengthMap.get(destinationNode) == shortestPathLengthMap.get(predecessorNode)+1){
                    shortestPathCountMap.put(destinationNode, shortestPathCountMap.get(destinationNode) + shortestPathCountMap.get(predecessorNode));
                    shortestPathIncomingEdgeMap.get(destinationNode).add(edge);
                }

            }

        }

        return stack;
    }

    private static void updateBetweennessMap(Stack<Node> stack, Map<Node, List<Edge>> outgoingEdgeMap, Map<Node, Integer> shortestPathCountMap, Map<Node, List<Edge>> shortestPathIncomingEdgeMap, Map<Edge, Double> betweennessMap){
        Map<Edge, Double> dependencyMap = new HashMap<>();
        for(Node startNode : outgoingEdgeMap.keySet()){
            for(Edge edge : outgoingEdgeMap.get(startNode)){
                dependencyMap.put(edge, 0.0);
            }
        }

        while(!stack.isEmpty()){
            Node currNode = stack.pop();
            double dependencySum = 0.0;

            for(Edge edge : outgoingEdgeMap.get(currNode)){
                dependencySum += dependencyMap.get(edge);
            }

            for(Edge edge : shortestPathIncomingEdgeMap.get(currNode)){
                double updatedDependency = ((shortestPathCountMap.get(edge.getSourceNode())*1.0)/(shortestPathCountMap.get(currNode))) * (1 + dependencySum);
                dependencyMap.put(edge, updatedDependency);
                betweennessMap.put(edge, betweennessMap.get(edge) + updatedDependency);
            }

        }

    }

    private static void explore(Node currNode, Map<Node, List<Edge>> outgoingEdgeMap, Map<Node, List<Edge>> incomingEdgeMap, Set<Node> visited){
        if(visited.contains(currNode))
            return;

        visited.add(currNode);

        for(Edge edge : outgoingEdgeMap.get(currNode)){
            explore(edge.getDestinationNode(), outgoingEdgeMap, incomingEdgeMap, visited);
        }

        for(Edge edge : incomingEdgeMap.get(currNode)){
            explore(edge.getSourceNode(), outgoingEdgeMap, incomingEdgeMap, visited);
        }

    }

    private static void explore(Node currNode, Map<Node, List<Edge>> outgoingEdgeMap, Map<Node, List<Edge>> incomingEdgeMap, Set<Node> visited, List<Integer> list){
        if(visited.contains(currNode))
            return;

        visited.add(currNode);
        list.add(currNode.getId());

        for(Edge edge : outgoingEdgeMap.get(currNode)){
            explore(edge.getDestinationNode(), outgoingEdgeMap, incomingEdgeMap, visited, list);
        }

        for(Edge edge : incomingEdgeMap.get(currNode)){
            explore(edge.getSourceNode(), outgoingEdgeMap, incomingEdgeMap, visited, list);
        }

    }

}
