package Ant;

import Utility.Tour;


public class WeightMatrix {

    private final double pheromonePersistence;
    private final double[][] weights;
    private final double tao0;
    private final int nCities;


    public WeightMatrix(int length, double pheromonePersistence, long bestTourLength) {
        this.pheromonePersistence = pheromonePersistence;
        weights = new double[length][length];
        nCities = length;
        tao0 = 1d / (bestTourLength * nCities);
        initializeWeights();
    }

    /**
     * //τ ( r , s ) = (1 − ρ ) ⋅ τ ( r , s ) + ρ ⋅ ∆τ ( r , s )
     *
     * @param x Edge start node
     * @param y Edge destination node
     */
    public void localTrailUpdating(int x, int y) {
        weights[x][y] = (1 - pheromonePersistence) * weights[x][y] + pheromonePersistence * tao0;
    }


    /**
     * τ(r,s) = (1−α) ⋅ τ(r,s) + α ⋅ ∆τ(r,s)
     * ∆τ(r,s) = 1 / bestTourLength
     *
     * @param alpha    MetaHeuristic alpha parameters
     * @param bestTour globalBestTour found since here
     */
    public void globalTrailUpdating(double alpha, Tour bestTour) {
        final double tao = 1d / bestTour.getLength();
        int[] bestPath = bestTour.getPath();
        final int nCities = weights.length;
        for (int i = 0; i < nCities; i++) {
            int from, to;
            from = bestPath[i];
            to = bestPath[(i + 1) % nCities];
            weights[from][to] = ((1 - alpha) * weights[from][to]) + (alpha * tao);
            weights[to][from] = ((1 - alpha) * weights[to][from]) + (alpha * tao);
        }
    }

    private void initializeWeights() {
        for (int row = 0; row < nCities; row++) {
            for (int column = 0; column < nCities; column++) {
                weights[row][column] = this.tao0;
            }
        }
    }



    public double getWeight(int x, int y) {
        return weights[x][y];
    }


}
