package Completed;


import Ant.AntColonyOptimization;
import Ant.MetaHeuristic;
import Ant.MetaParameters;
import BuildingPack.Building;
import BuildingPack.CandidateLists;
import BuildingPack.NearestNeighbor;
import LocalImprovementPack.LocalImprovement;
import LocalImprovementPack.TwoOptCandidates;
import Utility.TSPProblem;
import Utility.Tour;

import java.io.IOException;
import java.util.Random;

public class Seeds {
    public static void eil76() throws IOException {
        long seed = 1;
        String problemName = "eil76.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 20;
        MetaParameters parameters = MetaParameters.from(0.97d, 1, 0.99d, 0.99d, 1);
        executeSingle(problemName, parameters, seed);
    }

    public static void kroA100() throws IOException {
        long seed = 1;
        String problemName = "kroA100.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 20;
        MetaParameters parameters = MetaParameters.from(0.97d, 1, 0.99d, 0.99d, 1);
        executeSingle(problemName, parameters, seed);
    }

    public static void ch130() throws IOException {
        long seed = 1;
        String problemName = "ch130.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 20;
        MetaParameters parameters = MetaParameters.from(0.97d, 1, 0.99d, 0.99d, 1);
        executeSingle(problemName, parameters, seed);
    }

    public static void d198() throws IOException {
        long seed = 1460138283480L;
        String problemName = "d198.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 20;
        MetaParameters parameters = MetaParameters.from(0.1d, 1, 0.95d, 0.1d, 1);
        executeSingle(problemName, parameters, seed);
    }

    public static void lin318() throws IOException {
        long seed = 1460138283480L;
        String problemName = "lin318.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 20;
        MetaParameters parameters = MetaParameters.from(0.1d, 1, 0.95d, 0.1d, 15);
        executeSingle(problemName, parameters, seed);
    }

    public static void pr439() throws IOException {
        long seed = 1461050089816L;
        String problemName = "pr439.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 20;
        MetaParameters parameters = MetaParameters.from(0.1d, 1, 0.95d, 0.1d, 15);
        executeSingle(problemName, parameters, seed);
    }

    public static void pcb442() throws IOException {
        long seed = 1461218187569L;
        String problemName = "pcb442.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 20;
        MetaParameters parameters = MetaParameters.from(0.05d, 1, 0.98d, 0.22000000000000006, 15);
        executeSingle(problemName, parameters, seed);
    }

    public static void rat783() throws IOException {
        long seed = 1461668779556L;
        String problemName = "rat783.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 20;
        MetaParameters parameters = MetaParameters.from(0.05d, 1, 0.99d, 0.13d, 15);
        executeSingle(problemName, parameters, seed);
    }

    public static void u1060() throws IOException {
        long seed = 1461246187669L;
        String problemName = "u1060.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 60;
        MetaParameters parameters = MetaParameters.from(0.1d, 1, 0.99d, 0.12, 15);
        executeSingle(problemName, parameters, seed);
    }

    public static void fl1577() throws IOException {
        long seed = 1461434690710L;
        String problemName = "fl1577.tsp";
        CandidateLists.CANDIDATES_LIST_SIZE = 140;
        MetaParameters parameters = MetaParameters.from(0.15d, 1, 0.99d, 0.15d, 15);
        executeSingle(problemName, parameters, seed);
    }

    private static void executeSingle(String problemName, MetaParameters parameters, long seed) throws IOException {
        System.out.println("================ " + problemName + " ================");
        Random r = new Random(seed);
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
         */
        MetaHeuristic metaHeuristic = new AntColonyOptimization(tsp, r, parameters);
        /**
         * Create the LocalImprovement, used at the end of MetaHeuristic
         */
        LocalImprovement localImprovement = new TwoOptCandidates(tsp, r);

        TSPProblem.setBuilding(building);
        TSPProblem.setLocalImprovement(localImprovement);
        TSPProblem.setMetaHeuristic(metaHeuristic);

        Tour t = tsp.call();
        t.updateFields();
        System.out.println(t);
        TSPProblem.saveSolution(parameters);
    }


    public static void execute(String problemName, long seed, int candidateListSize, MetaParameters parameters) throws IOException {
        CandidateLists.CANDIDATES_LIST_SIZE = candidateListSize;
        executeSingle(problemName, parameters, seed);
    }
}
