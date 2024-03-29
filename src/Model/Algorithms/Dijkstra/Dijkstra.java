package Model.Algorithms.Dijkstra;

import Model.Components.GridNode;
import Model.Components.Node;

import java.util.*;

/**
 * This class represents the Dijkstra algorithm
 * Implemented using Singleton DP
 */
public class Dijkstra {

    private Node origin;//The origin node
    private static Dijkstra dijkstra;;//The instance of the class
    private Map<Integer,Double> needsToBeUpdated;
    private Set<Integer>teset = new HashSet<>();

    /**
     * The constructor
     */
    private Dijkstra()
    {
        this.needsToBeUpdated = new HashMap<>();
    }


    /**
     * This function will return the instance of the class
     * @return - The instance of the class
     */
    public static Dijkstra getInstance()
    {
        if(dijkstra == null)
            dijkstra = new Dijkstra();
        return dijkstra;
    }

    /**
     * This function will calculate the costs from the origin node to the other nodes
     * @param origin - the origin node
     * @return - The costs
     */
    public Map<Integer,Double> calculateCosts(Node origin)
    {
        this.origin = origin;
        this.needsToBeUpdated = new HashMap<>();
        search();

        return needsToBeUpdated;
    }

    /**
     * This function will preform the Dijkstra algorithm
     */
    private void search()
    {

        DijkstraSearchNode originNode = new DijkstraSearchNode(origin);
        originNode.setDistance(0d);
        this.needsToBeUpdated.put(originNode.getNode().getId(),0d);
        PriorityQueue <DijkstraSearchNode> open = new PriorityQueue(new DijkstraComperator());
        open.add(originNode);

        while(open.size()>0)
        {

            //System.out.println(open.size());
            DijkstraSearchNode node = open.poll();


            Set<Node> neighbors = node.getNode().getNeighbors().keySet();
            double help;
            double dis = node.getDistance();

            for(Node neighbor : neighbors)
            {
                //System.out.println(count);
                DijkstraSearchNode neigh = transformNode(neighbor);

                double neighDis = node.getNode().getWeight(neighbor);
                help = dis + neighDis;//New distance val


                double neighDistance = neigh.getDistance();//old val

                //Will not update distance
                if(neighDistance != Double.MAX_VALUE && help >= neighDistance)
                {
                    continue;

                }

                neigh.setDistance(help);
                this.needsToBeUpdated.put(neigh.getNode().getId(),help);
                add(open,neigh);


            }

        }


    }

    /**
     * This function will add the node to the open list
     * @param open - the given open list
     * @param dijkstraSearchNode - The given node
     */
    private void add(PriorityQueue<DijkstraSearchNode> open, DijkstraSearchNode dijkstraSearchNode)
    {
        open.remove(dijkstraSearchNode);
        open.add(dijkstraSearchNode);
    }

    /**
     * This function will transform a Node instance into the right DijkstaSearchNode instance
     * @param node - the given node
     * @return - The right DijkstraSearchNode instance
     */
    private DijkstraSearchNode transformNode(Node node)
    {

        DijkstraSearchNode dijkstraSearchNode = new DijkstraSearchNode(node);
        dijkstraSearchNode.setDistance(this.needsToBeUpdated.get(node.getId()));
        return dijkstraSearchNode;
    }

    /**
     * This class will compare two DijkstraSearchNodes using their distance from the origin n
     */
    class DijkstraComperator implements Comparator<DijkstraSearchNode>
    {

        @Override
        public int compare(DijkstraSearchNode o1, DijkstraSearchNode o2) {
            double dis1 = o1.getDistance();
            double dis2 = o2.getDistance();

            if(dis1 == dis2)
                return 0;
            if(dis1<dis2)
                return -1;
            return 1;
        }
    }





}
