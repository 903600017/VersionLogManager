package com.zf.plugins.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSonFactory {

    public static GSonFactory sGsonFactory;

    private Gson gson;

    private GSonFactory() {
        gson= new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public static GSonFactory getInstance() {
        if (sGsonFactory == null) {
            synchronized (GSonFactory.class) {
                if (sGsonFactory == null) {
                    sGsonFactory = new GSonFactory();
                }
            }
        }
        return sGsonFactory;
    }

    public Gson getGson() {
        return gson;
    }
}
