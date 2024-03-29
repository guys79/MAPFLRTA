package Model.Algorithms;

import Model.Algorithms.ALSSLRTA.AlssLrtaRealTimeSearchManager;
import Model.Algorithms.ALSSLRTAHALT.AlssLrtaHaltRealTimeManager;
import Model.Algorithms.ALSSLRTAIGNOREOTHERS.AlssLrtaIgnoreOthersRealTimeManager;
import Model.Algorithms.CentrelizedLRTA.CentrelizedLRTARealTimeSearchManager;
import Model.Algorithms.LRTA.RealTimeSearchManager;
import Model.Algorithms.MAALSSLRTA.MaAlssLrtaRealTimeSearchManager;
import Model.Components.IRealTimeSearchManager;
import Model.Components.Problem;

/**
 * This class will use the Factory DP + Singleton DP to create IRealTImeSearchManager
 */
public class FactoryRealTimeManager {


    private static FactoryRealTimeManager factoryRealTimeManager;//The instance

    /**
     * The private constructor
     */
    private FactoryRealTimeManager() {

    }

    /**
     * This function will return the instance of the class
     *
     * @return - The instance
     */
    public static FactoryRealTimeManager getInstance() {
        if (factoryRealTimeManager == null)
            factoryRealTimeManager = new FactoryRealTimeManager();
        return factoryRealTimeManager;
    }

    /**
     * This function will return  new IProblemCreator using the given type and number of agents
     *
     * @param type    - The given type
     * @param problem - The given problem
     * @return - A new IRealTimeSearchManager
     */
    public IRealTimeSearchManager getProblemCreator(int type, Problem problem) {

        IRealTimeSearchManager realTimeSearchManager;
        if (type == 0) {

            realTimeSearchManager = new RealTimeSearchManager(problem);
        } else {
            if (type == 1) {

                realTimeSearchManager = new AlssLrtaRealTimeSearchManager(problem);
            }
            else {
                if (type == 2 || type == 5 || type == 7 || type == 8||type==9||type==10||type==11||type==12||type==13) {

                    realTimeSearchManager = new MaAlssLrtaRealTimeSearchManager(problem);
                }
                else {
                    if (type == 3)
                    {

                        realTimeSearchManager = new AlssLrtaIgnoreOthersRealTimeManager(problem);
                    }
                    else {

                        if (type == 6)
                        {

                            realTimeSearchManager = new CentrelizedLRTARealTimeSearchManager(problem);
                        }
                        else {

                            realTimeSearchManager = new AlssLrtaHaltRealTimeManager(problem);
                        }
                    }

                }
            }

        }
        return realTimeSearchManager;
    }
}
