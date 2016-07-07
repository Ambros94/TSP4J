package Utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Tour {

    private int[] path;
    private int[] positions;

    private boolean valid;
    private long length;
    private float error;

    public Tour() {
        this.path = new int[TSPProblem.dimension];
        this.positions = new int[TSPProblem.dimension];
    }

    public Tour setPath(int[] path, int[] positions) {
        this.path = path;
        this.positions = positions;
        updateFields();
        return this;
    }

    public Tour fastSetPath(int[] path, long length, int[] positions) {
        this.path = path;
        this.positions = positions;
        this.length = length;
        return this;
    }

    public Tour updateFields() {
        valid = validate();
        length = evaluate();
        error = (length - TSPProblem.bestKnown) / (float) TSPProblem.bestKnown * 100;
        return this;
    }

    public Tour fastUpdateFields() {
        length = evaluate();
        error = (length - TSPProblem.bestKnown) / (float) TSPProblem.bestKnown * 100;
        return this;
    }

    private boolean validate() {
        Set<Integer> validatePath = new HashSet<>();
        for (int n : path) {
            if (n >= 0 && n < TSPProblem.dimension + 1 && !validatePath.add(n))//The city was yet present in the path
                return false;
        }
        return validatePath.size() == TSPProblem.dimension;
    }


    private long evaluate() {
        long length = 0;
        for (int i = 0; i < TSPProblem.dimension - 1; i++) {
            length += TSPProblem.distanceMatrix[path[i]][path[i + 1]];
        }
        length += TSPProblem.distanceMatrix[path[0]][path[TSPProblem.dimension - 1]];
        return length;
    }

    @Override
    public String toString() {
        if (valid)
            return "Tour{ IS VALID !!!!  error=" + error + '}';
        else
            return "Tour{ INVALID ,error = " + error + "} ";
    }

    public float getError() {
        return error;
    }

    public long getLength() {
        return length;
    }

    public int[] getPath() {
        return path;
    }

    public int[] getPositions() {
        return positions;
    }


    void saveOnFile(long seed, Object additionalSave) throws IOException {
        BufferedWriter outputWriter;
        outputWriter = new BufferedWriter(new FileWriter("resources/TSPTours/" + TSPProblem.problemName + ".opt.tour"));
        outputWriter.write("NAME : " + TSPProblem.problemName + ".opt.tour\n");
        outputWriter.write("COMMENT : " + additionalSave + "\n");
        outputWriter.write("ERROR% : " + this.error + "\n");
        outputWriter.write("SEED : " + seed + "\n");

        outputWriter.write("TYPE : TOUR\n");
        outputWriter.write("DIMENSION : " + String.valueOf(evaluate()) + "\n");
        outputWriter.write("TOUR_SECTION\n");
        for (int i : path) {
            outputWriter.write((i + 1) + "\n");
        }
        outputWriter.write("-1\n");
        outputWriter.write("EOF\n");
        outputWriter.flush();
        outputWriter.close();
    }


    public void reverse(int from, int to) {
        // FROM = I+1
        // to = j

        int normDistance = to - from;
        int inverseDistance = (TSPProblem.dimension - to - 1 + from - 1);
        if (normDistance <= inverseDistance) {//Reverse from i+1 and j
            int distance = ((normDistance) % 2 == 0) ? normDistance / 2 : (normDistance / 2) + 1;
            for (int i = 0; i < distance; i++) {
                int tmp = path[from + i];

                positions[path[to - i]] = from + i;
                positions[tmp] = to - i;

                path[from + i] = path[to - i];
                path[to - i] = tmp;
            }
        } else {//Reverse from j+1 to i
            //System.out.println("Before" + Arrays.toString(path));
            //System.out.println("[" + from + "][" + to + "]");
            int distance = (inverseDistance % 2 == 0) ? inverseDistance / 2 : (inverseDistance / 2) + 1;
            to++;
            from--;
            from += TSPProblem.dimension;
            for (int i = 0; i < distance; i++) {
                int tmp = path[(to + i) % TSPProblem.dimension];

                positions[path[(from - i) % TSPProblem.dimension]] = (to + i) % TSPProblem.dimension;
                positions[tmp] = (from - i) % TSPProblem.dimension;

                path[(to + i) % TSPProblem.dimension] = path[(from - i) % TSPProblem.dimension];
                path[(from - i) % TSPProblem.dimension] = tmp;
            }
            //System.out.println("After" + Arrays.toString(path));
        }
    }

    public Tour setPositions(int[] positions) {
        this.positions = positions;
        return this;
    }
}
