package ml.chromaryu.IrunaReloaded.api;

import java.util.Random;

/**
 * Created by chroma on 16/06/27.
 */
public class RandomNumber {
    public static int showRandomInteger(int aStart, int aEnd) {
        if (aStart > aEnd) throw new IllegalArgumentException("Start cannot exceed End.");
        Random aRandom = new Random();
        //get the range, casting to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        //compute a fraction of the range , 0 <= frac <= range
        long fraction = (long) (range * aRandom.nextDouble());
        return (int) (fraction + aStart);
    }
}
