package Utility;

import java.util.*;

public class SpanningThree {


    private final TSPProblem p;

    public SpanningThree(TSPProblem problem) {
        this.p = problem;
    }

    public List<Edge> buildSpanningThree() {
        /**
         * Build a linked list with all Edges
         */
        List<Edge> edges = new ArrayList<>();
        int n = TSPProblem.distanceMatrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j)
                    edges.add(new Edge(i, j, TSPProblem.distanceMatrix[i][j]));
            }
        }
        /**
         * Sort edges in ascending order
         */
        List<Edge> selectedEdges = new ArrayList<>();
        Collections.sort(edges);


        int i = 1;
        while (selectedEdges.size() < TSPProblem.distanceMatrix.length) {
            /**
             * Pick the smallest edge.
             * */
            final Edge shortest = edges.remove(0);
            /**
             * Check if it forms a cycle with the spanning tree
             * formed so far. If cycle is not formed, include this edge. Else, discard it.
             */
            if (!hasLoops(selectedEdges, shortest)) {
                selectedEdges.add(shortest);
            }

        }

        return selectedEdges;


    }

    private boolean hasLoops(List<Edge> selectedEdges, Edge shortest) {
        /*
        Has a loop if the new edge BOTH starts from a node yet in the path and terminate over there
         */
        boolean source = false, destination = false;
        for (Edge edge : selectedEdges) {
            if (shortest.getSource() == edge.getSource() || shortest.getDestination() == edge.getSource()) {
                source = true;
            }
            if (shortest.getSource() == edge.getDestination() || shortest.getDestination() == edge.getDestination()) {
                destination = true;
            }
        }
        return source && destination;
    }

}
