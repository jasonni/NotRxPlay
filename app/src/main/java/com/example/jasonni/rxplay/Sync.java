package com.example.jasonni.rxplay;

import android.net.Uri;

import com.example.jasonni.rxplay.bean.Cat;

import java.util.Collections;
import java.util.List;

/**
 * Created by jasonni on 2016/3/9.
 */
public class Sync {

    public interface Api {
        List<Cat> queryCats(String query);
        Uri store(Cat cat);
    }

    public class CatsHelper {

        Api api;

        public Uri saveTheCutestCat(String query) {
            // TODO Elegant sync world.
            try {
                List<Cat> cats = api.queryCats(query);
                Cat cutest = findCutest(cats);
                Uri savedUri = api.store(cutest);
                return savedUri;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private Cat findCutest(List<Cat> cats) {
            return Collections.max(cats);
        }
    }

}
