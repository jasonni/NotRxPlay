package com.example.jasonni.rxplay;

import android.net.Uri;

import com.example.jasonni.rxplay.bean.Cat;

import java.util.Collections;
import java.util.List;

/**
 * Created by jasonni on 2016/3/11.
 */
public class ProAsync {
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
        void queryCats(String query, Callback<List<Cat>> catsQueryCallback);
        void store(Cat cat, Callback<Uri> storeCallback);
    }

    public abstract class AsyncJob<T> {
        public abstract void start(Callback<T> callback);
    }

    public class ApiWrapper {
        Api api;
/*
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
        */
        public AsyncJob<List<Cat>> queryCats(final String query){
            return new AsyncJob<List<Cat>>() {
                @Override
                public void start(final Callback<List<Cat>> callback) {
                    api.queryCats(query, new Callback<List<Cat>>() {
                        @Override
                        public void onResult(List<Cat> result) {
                            callback.onResult(result);
                        }

                        @Override
                        public void onError(Exception e) {
                            callback.onError(e);
                        }
                    });
                }
            };
        }
/*
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
        */
        public AsyncJob<Uri> store(final Cat cat){
            return new AsyncJob<Uri>() {
                @Override
                public void start(final Callback<Uri> callback) {
                    api.store(cat, new Callback<Uri>() {
                        @Override
                        public void onResult(Uri result) {
                            callback.onResult(result);
                        }

                        @Override
                        public void onError(Exception e) {
                            callback.onError(e);
                        }
                    });
                }
            };
        }
    }

    public class CatsHelper {

        ApiWrapper api;
/*

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
*/
        public AsyncJob<Uri> saveTheCutestCat(final String query) {
            return new AsyncJob<Uri>() {
                @Override
                public void start(final Callback<Uri> callback) {
                    api.queryCats(query).start(new Callback<List<Cat>>() {
                        @Override
                        public void onResult(List<Cat> result) {
                            Cat cutest = findCutest(result);
                            api.store(cutest).start(new Callback<Uri>() {
                                @Override
                                public void onResult(Uri result) {
                                    callback.onResult(result);
                                }

                                @Override
                                public void onError(Exception e) {
                                    callback.onError(e);
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            callback.onError(e);
                        }
                    });
                }
            };
        }

        private Cat findCutest(List<Cat> cats) {
            return Collections.max(cats);
        }
    }
}
