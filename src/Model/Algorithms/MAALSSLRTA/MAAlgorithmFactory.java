package Model.Algorithms.MAALSSLRTA;

import Model.Algorithms.BudgetOrientedMALRTA.BudgetOrientedMALRTA;
import Model.Algorithms.BudgetOrientedMALRTA.OnlyTransferLeftoverBudgetOrientedMaLRTA;
import Model.Components.Problem;

public class MAAlgorithmFactory {


    private static MAAlgorithmFactory singleton;

    private MAAlgorithmFactory()
    {

    }


    public static MAAlgorithmFactory getInstance()
    {
        if(singleton == null)
            singleton = new MAAlgorithmFactory();
        return singleton;
    }


    public MAALSSLRTA getAlgorithm(int type, MaAlssLrtaRealTimeSearchManager manager, Problem problem)
    {
        MAALSSLRTA maalsslrta;
        if(type == 8||type == 12||type == 13)
            maalsslrta = new BudgetOrientedMALRTA(problem,manager);
        else
        {
            if(type == 2 ||type == 11||type == 10)
                maalsslrta = new MAALSSLRTA(problem);
            else
            {
                maalsslrta = new OnlyTransferLeftoverBudgetOrientedMaLRTA(problem,manager);//type 9
            }
        }
        return maalsslrta;
    }
}
