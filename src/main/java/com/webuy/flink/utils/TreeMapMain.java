package com.webuy.flink.utils;

import java.util.Comparator;
import java.util.TreeMap;

public class TreeMapMain {

    public static void main(String[] args) {
        TreeMap<Integer, Integer> treemap = new TreeMap<>(new Comparator<Integer>() {

            @Override
            public int compare(Integer y, Integer x) {
                return (x < y) ? -1 : 1;
            }

        });

        treemap.put(333,2);

        treemap.put(456,6);

        for (Integer integer : treemap.keySet()) {
            System.out.println(treemap.get(integer) + ":" + integer);
        }
    }
}
