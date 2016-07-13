package ml.chromaryu.IrunaReloaded.api;

import java.text.DecimalFormat;

/**
 * Created by chroma on 16/07/02.
 */
public class formatFileSize {
    public static String formatFileSize(long size) {
        String hrSize;

        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format((double) size).concat(" B");
        }

        return hrSize;
    }
}
