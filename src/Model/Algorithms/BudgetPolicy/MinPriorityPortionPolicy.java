package Model.Algorithms.BudgetPolicy;

import Model.Components.Agent;
import Model.Components.Problem;

import java.util.*;

public class MinPriorityPortionPolicy implements IBudgetPolicy {//Type 10


    @Override
    public Map<Agent, Integer> getBudgetMap(Problem problem) {
        Map<Agent, Integer> budgetsForAgents= new HashMap<>();

        Set<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        double totalBudget = problem.getNumberOfNodeToDevelop() * agents.size();
        double sumOfPriorities = sumOfPriority(agents);
        double budget;
        double leftover = 0;
        for(Agent agent: agents)
        {

            budget = (1-(agent.getPriority()/sumOfPriorities))*totalBudget;
            budgetsForAgents.put(agent,(int)budget);
            leftover+= budget - (int)budget;
        }



        PriorityQueue<Agent> queue;
        Agent agent;
        int newBudget;
        leftover = (int)leftover;
        while(leftover>0)
        {
            queue = new PriorityQueue<>(new CompareAgentsPriority());
            queue.addAll(agents);
            while(queue.size()>0 && leftover>0)
            {

                agent = queue.poll();
                newBudget = budgetsForAgents.get(agent);
                budgetsForAgents.put(agent,newBudget+1);
                leftover--;
            }
        }
        return budgetsForAgents;
    }


    private double sumOfPriority(Set<Agent> agents)
    {
        double sum = 0;
        for(Agent agent: agents)
        {
            sum+= agent.getPriority();
        }
        return sum;
    }

    /**
     * This class will compare between agents by comparing their priority
     */
    private class CompareAgentsPriority implements Comparator<Agent>
    {
        public CompareAgentsPriority()
        {

        }

        @Override
        public int compare(Agent o1, Agent o2) {
            double p1 = o1.getPriority();
            double p2 = o2.getPriority();
            if(p1>p2)
                return -1;
            if(p2>p1)
                return 1;
            return 0;
        }
    }
}
