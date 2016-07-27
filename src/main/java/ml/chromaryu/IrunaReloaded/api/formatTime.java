package ml.chromaryu.IrunaReloaded.api;

/**
 * Created by chroma on 16/07/02.
 */
public class formatTime {
    public static String formattime(int time) {
        if (time < 10) {
            return 0 + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }
}
