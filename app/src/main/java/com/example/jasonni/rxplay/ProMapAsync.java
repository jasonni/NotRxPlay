package com.example.jasonni.rxplay;

import android.net.Uri;

import com.example.jasonni.rxplay.bean.Cat;

import java.util.Collections;
import java.util.List;

/**
 * Created by jasonni on 2016/3/11.
 */
public class ProMapAsync {
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

    public interface Func<T, R> {
        R call(T t);
    }

    public abstract class AsyncJob<T> {
        public abstract void start(Callback<T> callback);
/*
        final AsyncJob<Cat> cutestCatAsyncJob = new AsyncJob<Cat>() {
            @Override
            public void start(final Callback<Cat> callback) {
                catsAsyncJob.start(new Callback<List<Cat>>() {
                    @Override
                    public void onResult(List<Cat> result) {
                        callback.onResult(findCutest(result));
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        };
        */
        public <R> AsyncJob<R> map(final Func<T, R> func) {
            final AsyncJob<T> source = this;
            return new AsyncJob<R>() {
                @Override
                public void start(final Callback<R> callback) {
                    source.start(new Callback<T>() {
                        @Override
                        public void onResult(T result) {
                            R mapped = func.call(result);
                            callback.onResult(mapped);
                        }

                        @Override
                        public void onError(Exception e) {
                            callback.onError(e);
                        }
                    });
                }
            };
        }

        public <R> AsyncJob<R> flatMap(final Func<T, AsyncJob<R>> func) {
            final AsyncJob<T> source = this;
            return new AsyncJob<R>() {
                @Override
                public void start(final Callback<R> callback) {
                    source.start(new Callback<T>() {
                        @Override
                        public void onResult(T result) {
                            AsyncJob<R> mapped = func.call(result);
                            mapped.start(new Callback<R>() {
                                @Override
                                public void onResult(R result) {
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
    }

    public class ApiWrapper {
        Api api;

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

        public AsyncJob<Uri> saveTheCutestCat(final String query) {

            final AsyncJob<List<Cat>> catsAsyncJob = api.queryCats(query);
/*
            final AsyncJob<Cat> cutestCatAsyncJob = new AsyncJob<Cat>() {
                @Override
                public void start(final Callback<Cat> callback) {
                    catsAsyncJob.start(new Callback<List<Cat>>() {
                        @Override
                        public void onResult(List<Cat> result) {
                            callback.onResult(findCutest(result));
                        }

                        @Override
                        public void onError(Exception e) {
                            callback.onError(e);
                        }
                    });
                }
            };
*/
            final AsyncJob<Cat> cutestCatAsyncJob = catsAsyncJob.map(new Func<List<Cat>, Cat>() {
                @Override
                public Cat call(List<Cat> cats) {
                    return findCutest(cats);
                }
            });
/*
            final AsyncJob<Uri> saveCatAsyncJob = new AsyncJob<Uri>() {
                @Override
                public void start(final Callback<Uri> callback) {
                    cutestCatAsyncJob.start(new Callback<Cat>() {
                        @Override
                        public void onResult(Cat result) {
                            api.store(result).start(new Callback<Uri>() {
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
*/
            final AsyncJob<Uri> saveCatAsyncJob = cutestCatAsyncJob.flatMap(new Func<Cat,AsyncJob<Uri>>() {
                @Override
                public AsyncJob<Uri> call(Cat cat) {
                    return api.store(cat);
                }
            });

            return saveCatAsyncJob;
        }

        private Cat findCutest(List<Cat> cats) {
            return Collections.max(cats);
        }
    }
}
