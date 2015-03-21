package ru.ifmo.ctddev.berezhko.iterativeparallelism;

import info.kgeorgiy.java.advanced.concurrent.ListIP;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by root on 18.03.15.
 */
public class IterativeParallelism implements ListIP {
    @Override
    public String concat(int i, List<?> list) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<T> filter(int cnt, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        return null;
    }

    @Override
    public <T, U> List<U> map(int cnt, List<? extends T> list, Function<? super T, ? extends U> function) throws InterruptedException {

        return null;
    }

    @Override
    public <T> T maximum(int cnt, List<? extends T> list, Comparator<? super T> comparator) throws InterruptedException {
        Thread[] threads = new Thread[cnt];
        final int elementsPerThread = (list.size() + cnt - 1) / cnt;
        final T[] max = (T[]) new Object[]{list.get(0)};
        for (int i = 0; i < cnt; i++) {
            final int finalI = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    T localMax = null;
                    for (int j = elementsPerThread * finalI; j < elementsPerThread * (finalI + 1) && j < list.size(); j++) {
                        if (localMax == null || comparator.compare(list.get(j), localMax) > 0) {
                            localMax = list.get(j);
                        }
                    }
                    synchronized (max) {
                        if (max[0] == null || comparator.compare(localMax, max[0]) > 0) {
                            max[0] = localMax;
                        }
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < cnt; i++) {
            if (!threads[i].isInterrupted()) {
                threads[i].join();
            }
        }
        return max[0];
    }

    @Override
    public <T> T minimum(int cnt, List<? extends T> list, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(cnt, list, comparator.reversed());
    }

    @Override
    public <T> boolean all(int cnt, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        Thread[] threads = new Thread[cnt];
        final int elementsPerThread = (list.size() + cnt - 1) / cnt;
        final int[] res = {0};
        for (int i = 0; i < cnt; i++) {
            final int finalI = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    int localRes = 0;
                    for (int j = elementsPerThread * finalI; j < elementsPerThread * (finalI + 1) && j < list.size(); j++) {
                        if (predicate.test(list.get(j))) {
                            localRes++;
                        }
                    }
                    synchronized (res) {
                        res[0] += localRes;
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < cnt; i++) {
            if (!threads[i].isInterrupted()) {
                threads[i].join();
            }
        }
        return res[0] == list.size();
    }

    @Override
    public <T> boolean any(int cnt, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        Thread[] threads = new Thread[cnt];
        final int elementsPerThread = (list.size() + cnt - 1) / cnt;
        final int[] res = {0};
        for (int i = 0; i < cnt; i++) {
            final int finalI = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    int localRes = 0;
                    for (int j = elementsPerThread * finalI; j < elementsPerThread * (finalI + 1) && j < list.size(); j++) {
                        if (predicate.test(list.get(j))) {
                            localRes++;
                        }
                    }
                    synchronized (res) {
                        res[0] += localRes;
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < cnt; i++) {
            if (!threads[i].isInterrupted()) {
                threads[i].join();
            }
        }
        return res[0] > 0;
    }
}
