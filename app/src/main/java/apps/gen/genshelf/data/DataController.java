package apps.gen.genshelf.data;


import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;

/**
 * Created by gen on 16/5/8.
 */
public class DataController {
    protected String name;
    protected float requestDelay;
    private boolean saveFlag;
    private ArrayList properties;
    private HashMap propertiesIndex;
    private HashMap propertiesValues;

    public String getName() {
        return name;
    }

    public float getRequestDelay() {
        return requestDelay;
    }

    public ArrayList getProperties() {
        return properties;
    }
}
