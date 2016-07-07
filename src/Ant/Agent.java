package Ant;

import Utility.TSPProblem;
import Utility.Tour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Agent {

    private final Random r;
    private final MetaParameters parameters;
    private final int[][] distanceMatrix;
    private final int start;
    private final int[] path;
    private final int[] positions;
    private final List<Integer> notVisitedYet;
    private int lastNode;
    private int visitedNodes = -1;
    private static WeightMatrix weightMatrix;
    private long distance = 0L;

    Agent(Random r, MetaParameters parameters, WeightMatrix weights,
          int[][] distanceMatrix) {
        this.r = r;
        this.parameters = parameters;
        Agent.weightMatrix = weights;
        this.distanceMatrix = distanceMatrix;

        this.path = new int[TSPProblem.dimension];
        this.positions = new int[TSPProblem.dimension];
        /*
        Array of nodes that are not visited yet, all nodes in the problem
         */
        this.notVisitedYet = new ArrayList<>();
        for (int i = 0; i < distanceMatrix.length; i++)
            notVisitedYet.add(i);
        /*
        Visit the first node, then everything can go
         */
        this.start = r.nextInt(distanceMatrix.length);
        visitNode(start);
    }

    private void visitNode(int visitedNode) {
        notVisitedYet.remove(new Integer(visitedNode));
        this.lastNode = visitedNode;
        visitedNodes++;
    }

    private int getNextNode(final int lastVisitedNode) {

        if (notVisitedYet.size() == 0)
            return -1;
        /*
        Compute transition probabilities for every possible node
        */
        final double[] transitionProbabilities = new double[notVisitedYet.size()];
        double probabilitiesSum = 0;
        for (int i = 0; i < notVisitedYet.size(); i++) {
            final Integer possibleNext = notVisitedYet.get(i);
            final double chance = computeProbability(lastVisitedNode, possibleNext);
            transitionProbabilities[i] = chance;
            probabilitiesSum += chance;
        }
        for (int i = 0; i < notVisitedYet.size(); i++) {
            transitionProbabilities[i] /= probabilitiesSum;
        }
        /*
        Choose between Exploitation and Exploration
         */
        double q = r.nextDouble();

        if (q < parameters.getQ0()) {// Exploitation
            /*
            Choose the node with the highest probability
             */
            int nextNode = -1;
            double max = -1;
            for (int i = 0; i < transitionProbabilities.length; i++) {
                if (transitionProbabilities[i] > max) {
                    max = transitionProbabilities[i];
                    nextNode = notVisitedYet.get(i);
                }
            }
            return nextNode;
        } else {// Exploration
            /*
            Choose the node with the highest probability
             */
            double yetExtracted = 1;
            for (int i = 0; i < transitionProbabilities.length; i++) {
                double x = r.nextDouble() * yetExtracted;
                if (x < transitionProbabilities[i]) {
                    return notVisitedYet.get(i);
                } else
                    yetExtracted -= transitionProbabilities[i];
            }
            throw new RuntimeException("Should not arrive here!");
        }


    }

    /*
     * (pheromone(from,destination)) * ((1/distance(from,destination)) ^ BETA)
     */
    private double computeProbability(int from, int destination) {
        double trail = weightMatrix.getWeight(from, destination);
        double distanceInverse = 1d / distanceMatrix[from][destination];
        return trail * distanceInverse;
    }

    /*
    The Agent goes to the next node
     */
    void next() {
        int nextNodeToVisit = getNextNode(lastNode);
        if (nextNodeToVisit == -1) {//Is impossible to choose the next node because there are no city to visit
            nextNodeToVisit = start;
        }
        /*
        Updates Path representing variables
         */
        distance += distanceMatrix[lastNode][nextNodeToVisit];
        weightMatrix.localTrailUpdating(lastNode, nextNodeToVisit);
        path[visitedNodes] = nextNodeToVisit;
        positions[nextNodeToVisit] = visitedNodes;
        /*
        Visit the node
         */
        visitNode(nextNodeToVisit);
    }

    public Tour getTour() {
        return new Tour().fastSetPath(path, distance, positions);
    }
}
