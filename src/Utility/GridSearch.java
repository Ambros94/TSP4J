package Utility;

import Ant.AntColonyOptimization;
import Ant.MetaHeuristic;
import Ant.MetaParameters;
import BuildingPack.Building;
import BuildingPack.CandidateLists;
import BuildingPack.NearestNeighbor;
import LocalImprovementPack.LocalImprovement;
import LocalImprovementPack.TwoOptCandidates;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class GridSearch {

    private static final double alpha_initial = 0.05d;
    private static final double alpha_end = 0.15d;
    private static final double alpha_increment = 0.01d;


    private static final double exploitation_initial = 0.99d;
    private static final double exploitation_end = 0.99d;
    private static final double exploitation_increment = 0.01d;

    private static final double persistence_initial = 0.01d;
    private static final double persistence_end = 0.15d;
    private static final double persistence_increment = 0.02d;


    private static final int candidate_initial = 20;
    private static final int candidate_end =60;
    private static final int candidate_increment = 80;

    private static final int NUM_TRY = 5;


    public static void analyze() throws IOException {
        /**
         * Tries every params combination
         */
        for (double alpha = alpha_initial; alpha <= alpha_end; alpha += alpha_increment) {
                for (double persistence = persistence_initial; persistence <= persistence_end; persistence += persistence_increment) {
                    for (double exploitation = exploitation_initial; exploitation <= exploitation_end; exploitation += exploitation_increment) {
                        for (int candidate_size = candidate_initial; candidate_size <= candidate_end; candidate_size += candidate_increment) {

                        MetaParameters params = MetaParameters.from(alpha, 1, exploitation, persistence, 15);
                        Tour paramsBestTour = null;
                        CandidateLists.CANDIDATES_LIST_SIZE = candidate_size;
                        String problemName = "rat783.tsp";
                        System.out.println("================ " + problemName + " ================");

                        double[] errors = new double[NUM_TRY];
                        long bestSeed = 0;
                        for (int i = 0; i < NUM_TRY; i++) {
                            long seed = System.currentTimeMillis();
                            System.out.println("Try " + (i + 1) + " started");
                            Random r = new Random(seed);
                            Tour t = executeOnce(params, problemName, r, seed);
                            errors[i] = t.getError();
                            if (paramsBestTour == null || paramsBestTour.getError() > t.getError()) {
                                paramsBestTour = t;
                                bestSeed = seed;
                            }
                            System.out.println("Try " + (i + 1) + " COMPLETED");
                            System.out.println("*****************************");

                        }
                        Statistics s = new Statistics(errors);

                        saveOnFile(s.getMean(), s.getVariance(), problemName, paramsBestTour, bestSeed, params, candidate_size);

                    }
                }
            }
        }

    }

    private static void saveOnFile(double mean, double variance, String problemName, Tour paramsBestTour, long seed, MetaParameters parameters, int candidate_size) {
        try (FileWriter fw = new FileWriter("statistics" + problemName + ".txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println("===========" + problemName + "===========");
            out.println("Parameters:" + parameters);
            out.println("Candidates:" + candidate_size);
            out.println("Media:" + mean);
            out.println("Varianza:" + variance);
            out.println("Best tour :" + paramsBestTour.getError() + " with seed : " + seed);
            out.println("================================");

            System.out.println("===========" + problemName + "===========");
            System.out.println("Parameters:" + parameters);
            System.out.println("Candidates:" + candidate_size);
            System.out.println("Media:" + mean);
            System.out.println("Varianza:" + variance);
            System.out.println("Best tour :" + paramsBestTour.getError() + " with seed : " + seed);
            System.out.println("================================");
        } catch (IOException ignored) {
        }
        try (FileWriter fw = new FileWriter("statistics" + problemName + ".csv", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s", parameters.getAlpha(), parameters.getQ0(), parameters.getPheromonePersistence(), candidate_size, mean, variance, paramsBestTour.getError(), paramsBestTour.getLength(), seed).replace('.', ','));
        } catch (IOException ignored) {
            System.out.println(ignored);
        }


    }

    private static Tour executeOnce(MetaParameters parameters, String problemName, Random r, long seed) throws IOException {
        /**
         * Create the problem
         */
        TSPProblem tsp = TSPProblem.create(problemName, seed);
        /**
         * Create a first solution, mandatory to initialize pheromone Matrix
         */
        Building building = new NearestNeighbor(tsp, r);
        /**
         * Create the metaHeuristic
         * ant number 1 5 10
         */

        MetaHeuristic metaHeuristic = new AntColonyOptimization(tsp, r, parameters);
        /**
         * Create the LocalImprovement, used at the end of MetaHeuristic
         */
        LocalImprovement localImprovement = new TwoOptCandidates(tsp, r);
        TSPProblem.setBuilding(building);
        TSPProblem.setLocalImprovement(localImprovement);
        TSPProblem.setMetaHeuristic(metaHeuristic);

        return tsp.call();
    }

    private static class Statistics {
        double[] data;
        int size;

        Statistics(double[] data) {
            this.data = data;
            size = data.length;
        }

        double getMean() {
            double sum = 0.0;
            for (double a : data)
                sum += a;
            return sum / size;
        }

        double getVariance() {
            double mean = getMean();
            double temp = 0;
            for (double a : data)
                temp += (mean - a) * (mean - a);
            return temp / size;
        }

        double getStdDev() {
            return Math.sqrt(getVariance());
        }

        public double median() {
            Arrays.sort(data);

            if (data.length % 2 == 0) {
                return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
            } else {
                return data[data.length / 2];
            }
        }
    }
}
