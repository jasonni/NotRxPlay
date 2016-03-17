package com.example.jasonni.rxplay;

import android.net.Uri;

import com.example.jasonni.rxplay.bean.Cat;

import java.util.Collections;
import java.util.List;

/**
 * Created by jasonni on 2016/3/9.
 */
public class Async {
    /*
    Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ...
        }
    });
    */

    public interface Api {
//        List<Cat> queryCats(String query);
//        Uri store(Cat cat);

        interface CatsQueryCallback {
            void onCatListReceived(List<Cat> cats);
            void onQueryFailed(Exception e);
        }

        interface StoreCallback{
            void onCatStored(Uri uri);
            void onStoreFailed(Exception e);
        }

        void queryCats(String query, CatsQueryCallback catsQueryCallback);
        void store(Cat cat, StoreCallback storeCallback);
    }

    public static class CatsHelper {

        interface CutestCatCallback {
            void onCutestCatSaved(Uri uri);
            void onError(Exception e);
        }

        Api api;

        /*
        public Uri saveTheCutestCat(String query) {
            // TODO Elegant sync world.
            List<Cat> cats = api.queryCats(query);
            Cat cutest = findCutest(cats);
            Uri savedUri = api.store(cutest);
            return savedUri;
        }
        */
        public void saveTheCutestCat(String query, final CutestCatCallback cutestCatCallback) {
            // TODO Callback hell.
            api.queryCats(query, new Api.CatsQueryCallback() {
                @Override
                public void onCatListReceived(List<Cat> cats) {
                    Cat cutest = findCutest(cats);
                    api.store(cutest, new Api.StoreCallback() {
                        @Override
                        public void onCatStored(Uri uri) {
                            cutestCatCallback.onCutestCatSaved(uri);
                        }

                        @Override
                        public void onStoreFailed(Exception e) {
                            cutestCatCallback.onError(e);
                        }
                    });
                }

                @Override
                public void onQueryFailed(Exception e) {
                    cutestCatCallback.onError(e);
                }
            });
        }

        private Cat findCutest(List<Cat> cats) {
            return Collections.max(cats);
        }
    }

}
