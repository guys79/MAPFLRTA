package Model.Algorithms.MAALSSLRTA;

import Model.Algorithms.BudgetOrientedMALRTA.BudgetOrientedMALRTA;
import Model.Components.AbstractRealTimeSearchManager;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents a Multi Agent aLSS-LRTA*
 */
public class MaAlssLrtaRealTimeSearchManager extends AbstractRealTimeSearchManager {
    private int maxLength;//The maximum kength of prefix in a certain iteration
    public static int test =0;
    long usedBudget;

    /**
     * The constructor of the class
     *
     * @param problem - The given problem
     */
    public MaAlssLrtaRealTimeSearchManager(Problem problem) {
        super(problem);
        usedBudget = 0;

    }

    public List<Node> getPrefix(Agent agent)
    {
        return this.prefixesForAgents.get(agent);
    }

    @Override
    protected void getPriorities()
    {
      //  longestRoadPriority();
        shortestRoadPriority();
    }

    private void longestRoadPriority()
    {
        Collection<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        for(Agent agent: agents)
        {
            agent.setPriority(agent.getHeuristicValue(agent.getCurrent()));
        }
    }
    private void shortestRoadPriority()
    {
        Collection<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        List<Double> priorities = new ArrayList<>();
        List<Agent> agentList = new ArrayList<>(agents);
        for(Agent agent: agents)
        {
            priorities.add(agent.getHeuristicValue(agent.getCurrent()));
        }
        priorities.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                if(o1>o2)
                    return -1;
                if(o1<o2)
                    return 1;
                return 0;
            }
        });
        agentList.sort(new Comparator<Agent>() {
            @Override
            public int compare(Agent o1, Agent o2) {
                double h1 = o1.getHeuristicValue(o1.getCurrent());
                double h2 = o2.getHeuristicValue(o2.getCurrent());
                if(h1<h2)
                    return -1;
                if(h1>h2)
                    return 1;
                return 0;

            }
        });

        for(int i=0;i<agentList.size();i++)
        {
            agentList.get(i).setPriority(priorities.get(i));
        }


    }


    @Override
    protected void calculatePrefix()
    {
        test++;
        prev = new HashMap<>();

        //getPriorities();
        //Calculate the prefixes for all agents
        for(Agent agent:this.prefixesForAgents.keySet())
        {
                prev.put(agent,agent.getCurrent());
        }


        //Get prefixes
        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Collection<Agent> agents = agent_goal_start.keySet();
        MAALSSLRTA maalsslrta;

        maalsslrta = MAAlgorithmFactory.getInstance().getAlgorithm(problem.getType(),this,problem);



        this.prev.clear();

        Map<Integer,List<Node>> prefixes = maalsslrta.getPrefixes(this.budgetMap);

        if(maalsslrta instanceof BudgetOrientedMALRTA)
        {
            usedBudget+=((BudgetOrientedMALRTA)maalsslrta).getUsedBudget();
            setBudgetUsed(usedBudget);
        }
        /*for(HashMap.Entry<Integer,List<Node>> prefix : prefixes.entrySet())
        {
            System.out.println("Agent "+prefix.getKey()+" prefix "+prefix.getValue());
        }*/

        //If there is no solution
        if(prefixes.containsValue(null))
        {
            List<Node> prefix;
            for(Agent agent: agents)
            {
                prefix = prefixes.get(agent);
                if(prefix == null) {
                    prefixesForAgents.put(agent, null);
                    List<Node> fail = new ArrayList<>();
                    fail.add(agent.getCurrent());
                    prefix = fail;
                }


                //Adding the prefix to the path
                List<Node>path = pathsForAgents.get(agent);
                path.remove(path.size()-1);
                path.addAll(prefix);

            }
            return;
        }

        //Calculate the maximum prefix size
        maxLength = -1;
        for (Agent agent : agents) {
            this.prefixesForAgents.put(agent,prefixes.get(agent.getId()));
            List<Node> prefix = this.prefixesForAgents.get(agent);
            if (prefix == null) {
              //  System.out.println("s;dak;sdkadadadada");
                return;
            }
            if (maxLength < prefix.size())
                maxLength = prefix.size();
        }
        //Converting the prefixes to be with the same length
        for (Agent agent : agents) {
            List<Node> prefix = this.prefixesForAgents.get(agent);
          //  System.out.println("Prefix length "+prefix.size() +" agent id "+agent.getId());
            int length = prefix.size();
            Node last = prefix.get(length-1);
            if(last.getId()!= agent.getGoal().getId())
            {
                for(int i=length;i<maxLength;i++)
                {
                    prefix.add(last);
                }
            }
            //Adding the prefix to the path
            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefixes.get(agent.getId()));
        }


    }

    @Override
    public boolean isDone() {

        if( super.isDone())
            return true;

        //Check if there was a change
        Set<Agent> agents = prev.keySet();
        if(prev.size() ==0)
            return false;
        int count = 0;
        for(Agent agent : agents)
        {
            if(agent.isDone())
            {
                count++;
                if(count>numOfFinish)
                    return false;

            }
            else
            {
                if(this.prev.get(agent).getId() != agent.getCurrent().getId())
                    return false;
            }


        }
        return true;
    }

    @Override
    public void move() {
        PriorityQueue<Agent> agents = new PriorityQueue<>(new MAALSSLRTA.CompareAgentsPriority());
        agents.addAll(problem.getAgentsAndStartGoalNodes().keySet());


        //In each time step:
        Agent agent;
        for (int i = 0; i < maxLength; i++) {

            //For every agent
            while(agents.size()>0)
            {
                agent = agents.poll();


                //If there is a solution
                List<Node> prefix = this.prefixesForAgents.get(agent);

            //    if((test==50 ||test==51) && i==0)
              //      System.out.println("agent "+agent.getId()+" "+prefix);
              /*  int id1 = 5;
                int id2 = 13;
                int iter = 6;
                if(agent.getId() == id1 && test ==iter)
                    System.out.println(prefix +" agent "+id1);
                if(agent.getId() == id2 && test ==iter)
                    System.out.println(prefix  +" agent "+id2);
                    */
                if (prefix == null)
                    return;

                //Try to move the agent
                if (i <= prefix.size() - 1) {
                    if (!agent.moveAgent(prefix.get(i))) {
             //           System.out.println(!agent.moveAgent(prefix.get(i)));
                       System.out.println("Collision between agent " + agent.getId() + " and agent " + prefix.get(i).getOccupationId() + " in " + prefix.get(i) +" id = "+prefix.get(i).getId());
                        prefixesForAgents.put(agent, null);
                        return;
                    }
                    else
                    {
                        if(prefix.size()-1 == i && problem.getAgentsAndStartGoalNodes().get(agent).getValue().getId() == prefix.get(i).getId())
                            prefix.get(i).inhabitate(agent.getId());
                    }
                }
            }

            agents = new PriorityQueue<>(new MAALSSLRTA.CompareAgentsPriority());
            agents.addAll(problem.getAgentsAndStartGoalNodes().keySet());

            //Don't clock way
            for (Agent agent2 : agents) {

                List<Node> prefix = this.prefixesForAgents.get(agent2);

                if (i <= prefix.size() - 1) {
                    int index = i;
                    Node node = prefix.get(index);
                    node.moveOut();
                }

            }
        }

    }


}
