package LocalImprovementPack;


import Utility.Tour;
import Utility.TSPProblem;

import java.util.Random;

public abstract class LocalImprovement {

    TSPProblem p;
    Tour s;
    Random r;

    LocalImprovement(TSPProblem p, Random r) {
        this.p = p;
        this.r = r;
    }

    public abstract void improve(Tour s);
}
