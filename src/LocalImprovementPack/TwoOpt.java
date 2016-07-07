package LocalImprovementPack;

import Utility.Tour;
import Utility.TSPProblem;

import java.util.*;


public class TwoOpt extends LocalImprovement {

    int[] path;

    public TwoOpt(TSPProblem p, Random random) {
        super(p, random);
    }

    /**
     * @param s Tour that has will be 2-Opt improved
     */
    @Override
    public void improve(Tour s) {

        this.s = s;
        this.path = s.getPath();
        int n = TSPProblem.coordinates.size();
        int best_gain = -1;
        int first = -1, second = -1;

        while (best_gain < 0) {
            best_gain = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i + 2; j < n; j++) {
                    int gain = computeGain(i, j, n);
                    if (gain < best_gain) {
                        first = i;
                        second = j;
                        best_gain = gain;
                    }
                }
            }
            if (best_gain < 0) {
                exchange(first, second, n);
            }
        }
        /**
         * The solution path is modified in place, need to update length and error at the end of the improvement
         */
        s.fastUpdateFields();
    }

    /**
     * @param firstIndex  Index of the first city in the solutionPath
     * @param secondIndex Index of the second city in the solutionPath
     * @param n           Number of cities in the TSProblem
     * @return Difference between old edges and new edges, negative if there is an improvement
     */
    private int computeGain(int firstIndex, int secondIndex, int n) {
        int
                ia = path[firstIndex],
                ib = path[firstIndex + 1],
                oa = path[secondIndex],
                ob = path[(secondIndex + 1) % n];
        return (TSPProblem.distanceMatrix[ia][oa] + TSPProblem.distanceMatrix[ib][ob]) - (TSPProblem.distanceMatrix[ia][ib] + TSPProblem.distanceMatrix[oa][ob]);
    }

    /**
     * @param firstIndex  Index of the first city in the solutionPath
     * @param secondIndex Index of the second city in the solutionPath
     * @param n           Number of cities in the TSProblem
     *                    Modify the solution Path, reversing the segment between firstIndex+1 (City at the end of the first considered edge)
     *                    and secondIndex (City at the begin of the second considered edge).
     *                    Both included, they have to be switched too
     */
    private void exchange(int firstIndex, int secondIndex, int n) {
        /**       reversed
         * [0..i][j...i+1][j+1...nTowns]
         * subList includes the first one, but not the second one, so the secondParamater has to be secondIndex+1
         */
        s.reverse(firstIndex + 1, secondIndex);
    }
}
