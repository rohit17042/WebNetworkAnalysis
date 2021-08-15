package graph;

/**
 *
 * @author : Rohit Chaudhary
 *
 */

public class Node {
    private int id;

    public Node(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public String toString(){
        return "Node: id=" + this.id;
    }

}
