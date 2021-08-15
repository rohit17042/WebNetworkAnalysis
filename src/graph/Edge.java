package graph;

/**
 *
 * @author : Rohit Chaudhary
 *
 */

public class Edge {
    private Node sourceNode, destinationNode;

    public Edge(Node sourceNode, Node destinationNode){
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    public Node getSourceNode(){
        return this.sourceNode;
    }

    public Node getDestinationNode(){
        return this.destinationNode;
    }

    @Override
    public String toString(){
        return "Edge: " + this.sourceNode.getId() + " >---> " + this.destinationNode.getId();
    }

}
