package LocalImprovementPack;

import BuildingPack.CandidateLists;
import Utility.TSPProblem;
import Utility.Tour;

import java.util.Random;


public class TwoOptCandidates extends LocalImprovement {

    public TwoOptCandidates(TSPProblem p, Random random) {
        super(p, random);
    }

    /**
     * @param s Tour that has will be 2-Opt improved
     */
    @Override
    public void improve(Tour s) {

        this.s = s;
        int[] path = s.getPath();
        int[] positions = s.getPositions();
        int n = TSPProblem.coordinates.size();
        int best_gain = -1;
        int bestFirstIndex = -1, bestSecondIndex = -1;

        while (best_gain < 0) {
            best_gain = 0;
            for (int firstIndex = 0; firstIndex < n; firstIndex++) {

                int i = path[firstIndex];
                int i1 = path[(firstIndex + 1) % TSPProblem.dimension];

                for (int k = 0; k < CandidateLists.CANDIDATES_LIST_SIZE; k++) {
                    int j = CandidateLists.candidatesMap[i][k];//J is one of the candidates1
                    int secondIndex = positions[j];
                    if (Math.abs(secondIndex - firstIndex) < 2)
                        continue;
                    int j1 = path[(secondIndex + 1) % TSPProblem.dimension];

                    int gain = computeGain(i, i1, j, j1);
                    if (gain < best_gain) {
                        bestFirstIndex = firstIndex;
                        bestSecondIndex = secondIndex;
                        best_gain = gain;
                    }
                }
            }
            if (best_gain < 0) {
                if (bestFirstIndex < bestSecondIndex) {
                    exchange(bestFirstIndex + 1, bestSecondIndex);
                } else {
                    exchange(bestSecondIndex + 1, bestFirstIndex);
                }
            }
        }
        /**
         * The solution path is modified in place, need to update length and error at the end of the improvement
         */
        s.fastUpdateFields();
    }

    private int computeGain(int i, int i1, int j, int j1) {
        return (TSPProblem.distanceMatrix[i][j] + TSPProblem.distanceMatrix[i1][j1]) - (TSPProblem.distanceMatrix[i][i1] + TSPProblem.distanceMatrix[j][j1]);
    }

    /**
     * @param firstIndex  Index of the first city in the solutionPath
     * @param secondIndex Index of the second city in the solutionPath
     *                    Modify the solution Path, reversing the segment between firstIndex+1 (City at the end of the first considered edge)
     *                    and secondIndex (City at the begin of the second considered edge).
     *                    Both included, they have to be switched too
     */
    private void exchange(int firstIndex, int secondIndex) {
        /**       reversed
         * [0..i][j...i+1][j+1...nTowns]
         * subList includes the first one, but not the second one, so the secondParamater has to be secondIndex+1
         */
        s.reverse(firstIndex, secondIndex);
    }
}
