
import config.ConfigInit;
import dataprocess.DataClean;

import java.io.IOException;

public class MecData {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        ConfigInit.init();
        DataClean.uploadSingleThread();
        long stop = System.currentTimeMillis();
        System.out.println("process finished: "+(stop-start)+" ms");
    }
}
