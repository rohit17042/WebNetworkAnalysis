package graph;

import java.util.*;

/**
 *
 * @author : Rohit Chaudhary
 *
 */

public class WebGraph implements Graph {

    private HashMap<Integer, HashSet<Integer>> adjacencyListMap;
    private int numOfNodes;
    private int numOfEdges;

    public WebGraph(){
        this.adjacencyListMap = new HashMap<>();
        this.numOfNodes = 0;
        this.numOfEdges = 0;
    }

    /* (non-Javadoc)
     * @see graph.Graph#addVertex(int)
     */
    @Override
    public void addVertex(int num) {
        if(this.adjacencyListMap.containsKey(num))
            return;
        this.adjacencyListMap.put(num, new HashSet<>());
        this.numOfNodes++;
    }

    /* (non-Javadoc)
     * @see graph.Graph#addEdge(int, int)
     */
    @Override
    public void addEdge(int from, int to) {
        if(!this.adjacencyListMap.containsKey(from) || !this.adjacencyListMap.containsKey(to))
            return;

        if(this.adjacencyListMap.get(from).contains(to))
            return;

        this.adjacencyListMap.get(from).add(to);
        this.numOfEdges++;
    }

    /* (non-Javadoc)
     * @see graph.Graph#removeEdge(int, int)
     */
    @Override
    public void removeEdge(int from, int to) {
        if(!this.adjacencyListMap.containsKey(from) || !this.adjacencyListMap.containsKey(to))
            return;

        if(!this.adjacencyListMap.get(from).contains(to))
            return;

        this.adjacencyListMap.get(from).remove(to);
        this.numOfEdges--;
    }

    /* (non-Javadoc)
     * @see graph.Graph#getEgonet(int)
     */
    @Override
    public Graph getEgonet(int center) {
        if(!adjacencyListMap.containsKey(center))
            return null;

        List<Integer> nodeList = getConnections(center);
        nodeList.add(center);

        Graph egonet = SubGraphCreator.createSubGraph(this, nodeList);

        return egonet;
    }

    /* (non-Javadoc)
     * @see graph.Graph#getSCCs()
     */
    @Override
    public List<Graph> getSCCs() {
        Stack<Integer> vertices = getAllVertices();

        Stack<Integer> postOrderVertices = dfs(vertices);
        HashMap<Integer, HashSet<Integer>> reverseAdjacencyListMap = getReversedEdges();
        List<List<Integer>> componentsList = findComponents(postOrderVertices, reverseAdjacencyListMap);

        List<Graph> scc = new ArrayList<>();

        for(List<Integer> component : componentsList){
            Graph subGraph = SubGraphCreator.createSubGraph(this, component);
            scc.add(subGraph);
        }

        return scc;
    }

    /* (non-Javadoc)
     * @see graph.Graph#exportGraph()
     */
    @Override
    public HashMap<Integer, HashSet<Integer>> exportGraph() {
        return new HashMap<>(this.adjacencyListMap);
    }

    @Override
    public int getNumOfNodes(){
        return this.numOfNodes;
    }

    @Override
    public int getNumOfEdges(){
        return this.numOfEdges;
    }

    private List<Integer> getConnections(int id){
        List<Integer> nodeList = new ArrayList<>();

        for(int neighbor : adjacencyListMap.get(id)) {
            nodeList.add(neighbor);
        }

        return nodeList;
    }

    private Stack<Integer> getAllVertices(){
        Stack<Integer> stack = new Stack<>();

        for(int id : adjacencyListMap.keySet()){
            stack.push(id);
        }

        return stack;
    }

    private Stack<Integer> dfs(Stack<Integer> vertices){
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> finished = new Stack<>();

        while(!vertices.isEmpty()){
            int id = vertices.pop();
            if(!visited.contains(id)){
                dfsVisit(id, visited, finished);
            }
        }

        return finished;
    }

    private void dfsVisit(int id, Set<Integer> visited, Stack<Integer> finished){
        if(visited.contains(id))
            return;

        visited.add(id);

        for(int neighbor : adjacencyListMap.get(id)){
            dfsVisit(neighbor, visited, finished);
        }

        finished.push(id);
    }

    private HashMap<Integer, HashSet<Integer>> getReversedEdges(){
        HashMap<Integer, HashSet<Integer>> reverseAdjacencyListMap = new HashMap<>();

        for(int id : this.adjacencyListMap.keySet()){
            reverseAdjacencyListMap.put(id, new HashSet<>());
        }

        for(int from : this.adjacencyListMap.keySet()){
            for(int to : this.adjacencyListMap.get(from)){
                reverseAdjacencyListMap.get(to).add(from);
            }
        }

        return reverseAdjacencyListMap;
    }

    private List<List<Integer>> findComponents(Stack<Integer> vertices, HashMap<Integer, HashSet<Integer>> map){
        List<List<Integer>> componentList = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        while(!vertices.isEmpty()){
            int id = vertices.pop();
            if(!visited.contains(id)){
                List<Integer> component = new ArrayList<>();
                explore(id, visited, componentList, component, map);
                componentList.add(component);
            }
        }

        return componentList;
    }

    private void explore(int id, Set<Integer> visited, List<List<Integer>> componentList, List<Integer> component, HashMap<Integer, HashSet<Integer>> map){
        if(visited.contains(id))
            return;

        visited.add(id);
        component.add(id);

        for(int neighbor : map.get(id)){
            explore(neighbor, visited, componentList, component, map);
        }

    }

}
