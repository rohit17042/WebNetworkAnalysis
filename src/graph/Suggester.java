package graph;

import java.util.*;

/**
 *
 * @author : Rohit Chaudhary
 *
 */

public class Suggester {

    public List<Integer> suggestSimilar(Graph graph, int id){
        return suggestSimilar(graph, id, 5);
    }

    public List<Integer> suggestSimilar(Graph graph, int id, int maxCount){
        if(maxCount<=0)
            return null;

        HashMap<Integer, HashSet<Integer>> adjacencyListMap = graph.exportGraph();
        HashSet<Integer> myNeighbors = findNeighbors(adjacencyListMap, id);

        HashSet<Integer> restNodes = (HashSet<Integer>) adjacencyListMap.keySet();
        restNodes.remove(id);

        PriorityQueue<ScoreCard> pq = new PriorityQueue<>();
        List<Integer> similarNodes = new ArrayList<>();

        for(int nodeId : restNodes){
            HashSet<Integer> nodeNeighbors = findNeighbors(adjacencyListMap, nodeId);
            nodeNeighbors.retainAll(myNeighbors);
            int score = nodeNeighbors.size();

            if(score==0)
                continue;

            if(pq.size()<maxCount){
                // filling priority queue
                ScoreCard card = new ScoreCard(nodeId, score);
                pq.add(card);
            }else if(pq.peek().getScore()<score){
                // updating priority queue
                ScoreCard card = pq.poll();
                card.setId(nodeId);
                card.setScore(score);
                pq.add(card);
            }

        }

        // putting all similar nodes into list
        while(!pq.isEmpty()){
            similarNodes.add(pq.poll().getId());
        }

        return similarNodes;
    }

    private HashSet<Integer> findNeighbors(HashMap<Integer, HashSet<Integer>> adjacencyListMap, int id){
        HashSet<Integer> set = new HashSet<>();

        for(int node : adjacencyListMap.get(id)){
            set.add(node);
        }

        for(int node : adjacencyListMap.keySet()){
            if(node!=id && adjacencyListMap.get(node).contains(id)){
                set.add(node);
            }
        }

        return set;
    }

}

class ScoreCard implements Comparable<ScoreCard>{
    private int id, score;

    public ScoreCard(int id, int score){
        this.id = id;
        this.score = score;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score){
        this.score = score;
    }

    @Override
    public int compareTo(ScoreCard card){
        return this.score- card.score;
    }

    @Override
    public String toString(){
        return "Card: id=" + this.id + ", score=" + this.score;
    }

}