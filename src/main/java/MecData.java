
import config.ConfigInit;
import dataprocess.DataClean;

import java.io.IOException;

public class MecData {

    public static void main(String[] args) throws IOException {

        ConfigInit.init();
        DataClean.uploadData();
    }
}
