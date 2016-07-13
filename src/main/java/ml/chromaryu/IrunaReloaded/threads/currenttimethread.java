package ml.chromaryu.IrunaReloaded.threads;

import java.util.Calendar;

/**
 * Created by chroma on 16/07/02.
 */
public class currenttimethread extends Thread {
    Runtime run = Runtime.getRuntime();
    public long time;
    public Calendar cal;
    public boolean sd = false;
    @Override
    public void run() {
        while (!sd) {
            time = System.currentTimeMillis();
            cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            //System.out.println(time);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                //Nothing to do
            }
        }
    }
}
