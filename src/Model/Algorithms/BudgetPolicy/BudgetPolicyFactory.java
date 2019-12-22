package Model.Algorithms.BudgetPolicy;

/**
 * This class uses Factory + Singleton DP
 * This class will be responsible for creating BudgetPolicies
 */
public class BudgetPolicyFactory {

    private static BudgetPolicyFactory instance;//The instance


    /**
     * The private constructor
     */
    private BudgetPolicyFactory()
    {

    }

    /**
     * This function will return the instance (Singleton)
     * @return - The instance of the class
     */
    public static BudgetPolicyFactory getInstance()
    {
        if(instance == null)
            instance = new BudgetPolicyFactory();
        return instance;
    }

    /**
     * This function will return the Budget Policy
     * @param type - The given type
     * @return - The Budget Policy
     */
    public IBudgetPolicy getPolicy(int type)
    {
        IBudgetPolicy policy;
        if(type == 2||type == 9)
            policy = new EqualBudget();//Regular
        else
        {
            if(type == 7)
                policy = new LongestPathBudgetPolicy();
            else// type = 8
            {
                if (type == 8) {
                    policy = new PrioritizedPolicy();

                }
                else
                {
                        if (type == 10||type==12)
                            policy = new MinPriorityPortionPolicy();
                        else// type = 11 || type = 13
                        {
                           // policy = new ResortToEqual();
                            policy = new MaxPriorityPortionPolicy();
                        }

                }


            }
        }


        return policy;
    }
}
