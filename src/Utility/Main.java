package Utility;

import Completed.Seeds;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Main class
 * Starts some common problems to show algorithm performance
 */

public class Main {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        /**
         * Execute some common problems with best params, to get the best result obtained with this algorithm
         */
        Seeds.eil76();
        Seeds.kroA100();
        Seeds.ch130();
        Seeds.d198();
        Seeds.lin318();
        Seeds.pr439();
        Seeds.pcb442();
        Seeds.rat783();
        Seeds.u1060();
        Seeds.fl1577();
        /*
         * Script execution for the competition
        String problemName = args[0];
        int candidateListSize = Integer.parseInt(args[1]);
        double alpha = Double.parseDouble(args[2]);
        double q = Double.parseDouble(args[3]);
        double persistence = Double.parseDouble(args[4]);
        int numAnts = Integer.parseInt(args[5]);
        long seed = Long.parseLong(args[6]);
        MetaParameters parameters = MetaParameters.from(alpha, 1, q, persistence, numAnts);
        Seeds.execute(problemName, seed, candidateListSize, parameters);
        */
    }
}
