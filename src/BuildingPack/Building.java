package BuildingPack;

import Utility.Tour;
import Utility.TSPProblem;

import java.util.Random;

public abstract class Building {

    TSPProblem p;
    private Random r;

    Building(TSPProblem problem, Random r) {
        this.p = problem;
        this.r = r;
    }

    public abstract Tour buildTour();

}
