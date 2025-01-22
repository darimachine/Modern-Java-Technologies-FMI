package bg.sofia.uni.fmi.mjt.newsfeed.json;

import com.google.gson.Gson;

public class GsonSingleton {
    private GsonSingleton() {
    }

    private static class GsonHolder {
        private static final Gson INSTANCE = new Gson();
    }

    public static Gson getInstance() {
        return GsonHolder.INSTANCE;
    }
}
