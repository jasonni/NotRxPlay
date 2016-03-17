package com.example.jasonni.rxplay.bean;

/**
 * Created by jasonni on 2016/3/9.
 */
public class Cat implements Comparable<Cat>{
    int cuteness;

    @Override
    public int compareTo(Cat another) {
        return Integer.compare(cuteness, another.cuteness);
    }

}
