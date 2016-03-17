package com.example.jasonni.rxplay;

import android.net.Uri;

import com.example.jasonni.rxplay.bean.Cat;

import java.util.Collections;
import java.util.List;

/**
 * Created by jasonni on 2016/3/9.
 */
public class GenericAsync {
    /*
    Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ...
        }
    });
    */

    /**
     * Use same callback.
     *
     * @param <T>
     */
    public interface Callback<T> {
        void onResult(T result);
        void onError(Exception e);
    }

    public interface Api {

//        interface CatsQueryCallback {
//            void onCatListReceived(List<Cat> cats);
//            void onQueryFailed(Exception e);
//        }

//        interface StoreCallback{
//            void onCatStored(Uri uri);
//            void onStoreFailed(Exception e);
//        }

//        void queryCats(String query, CatsQueryCallback catsQueryCallback);
//        void store(Cat cat, StoreCallback storeCallback);
        void queryCats(String query, Callback<List<Cat>> catsQueryCallback);
        void store(Cat cat, Callback<Uri> storeCallback);
    }

    public class ApiWrapper {
        Api api;

        public void queryCats(String query, final Callback<List<Cat>> catsCallback){
            api.queryCats(query, new Callback<List<Cat>>() {
                @Override
                public void onResult(List<Cat> result) {
                    catsCallback.onResult(result);
                }

                @Override
                public void onError(Exception e) {
                    catsCallback.onError(e);
                }

            });
        }

        public void store(Cat cat, final Callback<Uri> uriCallback){
            api.store(cat, new Callback<Uri>() {
                @Override
                public void onResult(Uri result) {
                    uriCallback.onResult(result);
                }

                @Override
                public void onError(Exception e) {
                    uriCallback.onError(e);
                }
            });
        }
    }

    public static class CatsHelper {

//        interface CutestCatCallback {
//            void onCutestCatSaved(Uri uri);
//            void onError(Exception e);
//        }

//        Api api;
        ApiWrapper api;

//        public void saveTheCutestCat(String query, final CutestCatCallback cutestCatCallback) {
//            // TODO Callback hell.
//            api.queryCats(query, new Api.CatsQueryCallback() {
//                @Override
//                public void onCatListReceived(List<Cat> cats) {
//                    Cat cutest = findCutest(cats);
//                    api.store(cutest, new Api.StoreCallback() {
//                        @Override
//                        public void onCatStored(Uri uri) {
//                            cutestCatCallback.onCutestCatSaved(uri);
//                        }
//
//                        @Override
//                        public void onStoreFailed(Exception e) {
//                            cutestCatCallback.onError(e);
//                        }
//                    });
//                }
//
//                @Override
//                public void onQueryFailed(Exception e) {
//                    cutestCatCallback.onError(e);
//                }
//            });
//        }
        public void saveTheCutestCat(String query, final Callback<Uri> cutestCatCallback) {
            // TODO Callback hell.
            api.queryCats(query, new Callback<List<Cat>>() {
                @Override
                public void onResult(List<Cat> result) {
                    Cat cutest = findCutest(result);
                    api.store(cutest, cutestCatCallback);
                }

                @Override
                public void onError(Exception e) {
                    cutestCatCallback.onError(e);
                }

            });
        }

        private Cat findCutest(List<Cat> cats) {
            return Collections.max(cats);
        }
    }

}
