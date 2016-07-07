package BuildingPack;

import Utility.SpanningThree;
import Utility.TSPProblem;

import java.util.*;

public class CandidateLists {
    private static final Map<Integer, Set<Integer>> map = new HashMap<>();
    public static int[][] candidatesMap;
    public static int CANDIDATES_LIST_SIZE = 20;

    public static void findCandidates(TSPProblem problem, SpanningThree spanningThree) {
        candidatesMap = new int[TSPProblem.dimension][CANDIDATES_LIST_SIZE];
        for (int i = 0; i < TSPProblem.distanceMatrix.length; i++) {
            Set<Integer> candidates = new HashSet<>();
            /**
             * Distances from i to all other nodes is contained in problem.distanceMatrix[i]
             * I have to choose some nodes
             */
            List<NodeDistance> distances = new ArrayList<>();
            for (int j = 0; j < TSPProblem.distanceMatrix.length; j++) {
                if (i != j)
                    distances.add(new NodeDistance(j, TSPProblem.distanceMatrix[i][j]));
            }
            Collections.sort(distances);
            for (int j = 0; j < CANDIDATES_LIST_SIZE; j++) {
                candidatesMap[i][j] = distances.get(j).getSource();
                candidates.add(distances.get(j).getSource());
            }
            map.put(i, candidates);
        }
        //System.out.println("Candidates list;" + map);
    }


    public static Set<Integer> getCandidates(Integer node) {
        return map.get(node);
    }

    private static class NodeDistance implements Comparable<NodeDistance> {
        final int source;
        final int distance;

        private int getSource() {
            return source;
        }

        private NodeDistance(int source, int distance) {
            this.source = source;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance o) {
            return distance - o.distance;
        }

        @Override
        public String toString() {
            return "NodeDistance{" +
                    "source=" + source +
                    ", distance=" + distance +
                    '}';
        }
    }

}
