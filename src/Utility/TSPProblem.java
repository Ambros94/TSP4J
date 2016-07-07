package Utility;

import Ant.MetaHeuristic;
import BuildingPack.Building;
import BuildingPack.CandidateLists;
import LocalImprovementPack.LocalImprovement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public class TSPProblem implements Callable<Tour> {

    public static long seed;
    public static int[][] distanceMatrix;
    static public List<Coordinate> coordinates;
    static String problemName;
    public static Tour bestTour;
    static int bestKnown;
    public static int dimension;

    private static Building building;
    private static LocalImprovement localImprovement;
    private static MetaHeuristic metaHeuristic;

    public static TSPProblem create(String nome, long seed) throws IOException {
        TSPProblem.seed = seed;
        List<String> lines = Files.readAllLines(Paths.get("resources/TSPProblems/" + nome), StandardCharsets.UTF_8);
        return new TSPProblem(lines, nome);
    }

    private TSPProblem(List<String> lines, String nome) {
        /**
         * Algorithms initialization
         */
        problemName = nome;
        coordinates = new ArrayList<>();
        boolean coords = false;
        for (String line : lines) {
            if (coords) {
                if (!line.equals("EOF")) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    tokenizer.nextToken();
                    float x = Float.parseFloat(tokenizer.nextToken());
                    float y = Float.parseFloat(tokenizer.nextToken());
                    coordinates.add(new Coordinate(x, y));
                }
            }
            //The coordinate is started
            if (!coords && line.equals("NODE_COORD_SECTION"))
                coords = true;
            //Looks for best known
            if (!coords && line.contains("BEST_KNOWN")) {
                StringTokenizer tokenizer = new StringTokenizer(line);
                tokenizer.nextToken();
                tokenizer.nextToken();
                bestKnown = Integer.parseInt(tokenizer.nextToken());
            }

        }
        dimension = coordinates.size();
        distanceMatrix = new int[coordinates.size()][coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            for (int j = 0; j < coordinates.size(); j++) {
                int distance = coordinates.get(i).distance(coordinates.get(j));
                distanceMatrix[i][j] = distance;
            }
        }

    }

    public static void setBuilding(Building building) {
        TSPProblem.building = building;
    }

    public static void setLocalImprovement(LocalImprovement localImprovement) {
        TSPProblem.localImprovement = localImprovement;
    }

    public static void setMetaHeuristic(MetaHeuristic metaHeuristic) {
        TSPProblem.metaHeuristic = metaHeuristic;
    }

    public static void saveSolution(Object additionalSave) {
        try {
            bestTour.saveOnFile(seed, additionalSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tour call() {
        Thread t = new Thread(() -> {
            /**
             * One-Time building algorithm
             */
            long start = System.currentTimeMillis();
            Tour newTour = building.buildTour();//Complete tour with the problem itself
            bestTour = newTour;
            /**
             * Builds candidate lists used in LocalOptimizazions
             */
            CandidateLists.findCandidates(this, null);
            localImprovement.improve(bestTour);

            long building = System.currentTimeMillis();
            /**
             * Until the thread is stopped it goes on calculating new solutions
             */
            while (!Thread.currentThread().isInterrupted()) {
                start = System.currentTimeMillis();
                //Use the first computed Tour as MetaHeuristic start
                newTour = metaHeuristic.rebuild(newTour);
                long heuristic = System.currentTimeMillis();

                //localImprovement.improve(newTour);
                long localOpt = System.currentTimeMillis();

                //System.out.println(String.format("Total time %ss, Heuristic %ss, Opt %ss, Error %s", (localOpt - start) / 1000.0, (heuristic - start) / 1000.0, (localOpt - heuristic) / 1000.0, newTour.getError()));
                if (!Thread.currentThread().isInterrupted() && (bestTour == null || (newTour.getLength() < bestTour.getLength()))) {
                    bestTour = newTour;
                    System.out.println("New local best:" + bestTour);
                }
            }
        });
        t.start();
        System.out.println("Time started");
        try {
            Thread.sleep(1000 * 60 * 3);// Should be 60, but we wanna have some margin
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
        System.out.println("Time ENDED");


        return bestTour;

    }

}