package Ant;

import LocalImprovementPack.TwoOptCandidates;
import Utility.TSPProblem;
import Utility.Tour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColonyOptimization implements MetaHeuristic {

    private final TSPProblem p;
    private final Random r;

    private int[][] distanceMatrix;
    private WeightMatrix weights;

    private final MetaParameters parameters;

    public AntColonyOptimization(TSPProblem p, Random r, MetaParameters parameters) {
        this.parameters = parameters;
        this.distanceMatrix = TSPProblem.distanceMatrix;
        this.p = p;
        this.r = r;
    }

    @Override
    public Tour rebuild(Tour tour) {
        /**
         * Get the current best Tour, because it's used in Ant Pheromone init
         */
        if (weights == null)
            weights = new WeightMatrix(distanceMatrix.length,
                    parameters.getPheromonePersistence(), tour.getLength());
        /**
         * Create numAgents Agents
         */
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < parameters.getNumAgents(); i++) {
            agents.add(new Agent(r, parameters, weights,
                    distanceMatrix));
        }
        /**
         * Get to the next city for every Agent, City number times
         */
        for (int i = 0; i < TSPProblem.dimension; i++) {
            agents.forEach(Agent::next);//Move every Agent one step forward
        }

        /**
         * Get Agent generated tours, tries to improve and return the best one (once improved)
         */
        Tour bestTour = null;
        TwoOptCandidates localopt = new TwoOptCandidates(p, r);
        for (Agent a : agents) {
            /**
             * Retrieve every tour and slightly improve every Tour found
             */
            Tour t = a.getTour();
            localopt.improve(t);
            if (bestTour == null || t.getLength() < bestTour.getLength()) {
                bestTour = t;
            }
        }

        weights.globalTrailUpdating(parameters.getAlpha(), TSPProblem.bestTour);

        return bestTour;
    }


}
