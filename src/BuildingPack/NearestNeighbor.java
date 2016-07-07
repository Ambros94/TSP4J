package BuildingPack;

import Utility.TSPProblem;
import Utility.Tour;

import java.util.*;

public class NearestNeighbor extends Building {

    private int startingNode = 0;

    private void setStartingNode(int startingNode) {
        this.startingNode = startingNode;
    }

    public NearestNeighbor(TSPProblem problem, Random r) {
        super(problem, r);
        setStartingNode(r.nextInt(TSPProblem.dimension));
    }

    @Override
    public Tour buildTour() {
        int[] solution = new int[TSPProblem.dimension];
        int[] positions = new int[TSPProblem.dimension];
        /**
         * Creates a List of Integer
         */
        List<Integer> remainingNodes = new ArrayList<>();
        for (int i = 0; i < TSPProblem.coordinates.size(); i++) {
            remainingNodes.add(i);
        }
        /**
         * First node of the path is node startingNode, is no longer a possible destination Node
         */

        solution[0] = startingNode;
        positions[startingNode] = 0;
        remainingNodes.remove(startingNode);

        for (int i = 1; i < TSPProblem.dimension; i++) {
            int min = Integer.MAX_VALUE, position = 0, j = 0;
            for (int n : remainingNodes) {
                if (n != solution[i - 1] && TSPProblem.distanceMatrix[solution[i - 1]][n] < min) {
                    min = TSPProblem.distanceMatrix[solution[i - 1]][n];
                    position = j;
                }
                j++;
            }
            solution[i] = remainingNodes.remove(position);
            positions[solution[i]] = i;
        }
        return new Tour().setPath(solution,positions);
    }
}
