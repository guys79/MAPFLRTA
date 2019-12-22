package Model.Algorithms.BudgetOrientedMALRTA;

import Model.Algorithms.MAALSSLRTA.MaAlssLrtaRealTimeSearchManager;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;

import java.util.List;

public class OnlyTransferLeftoverBudgetOrientedMaLRTA extends BudgetOrientedMALRTA {
    /**
     * The constructor
     *
     * @param problem - The given problem
     * @param maneger
     */
    public OnlyTransferLeftoverBudgetOrientedMaLRTA(Problem problem, MaAlssLrtaRealTimeSearchManager maneger) {
        super(problem, maneger);
    }

    @Override
    protected void aStarPrecedure() {
        super.aStarPrecedure();
        backtracking = false; //No Backtracking
    }
}
