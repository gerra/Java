package ru.ifmo.ctddev.berezhko.iterativeparallelism;

import info.kgeorgiy.java.advanced.concurrent.ListIP;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by root on 18.03.15.
 */
public class IterativeParallelism implements ListIP {
    private class MyRunnable<T, R> implements Runnable {
        private List<? extends T> list;
        private Function<List<? extends T>, R> function;
        private R result;

        public MyRunnable(List<? extends T> list, Function<List<? extends T>, R> function) {
            this.list = list;
            this.function = function;
        }

        @Override
        public void run() {
            result = function.apply(list);
        }

        public R getResult() {
            return result;
        }
    }

    private <T, R> List<R> makeListWorkers(int cnt, List<? extends T> list, Function<List<? extends T>, R> function) throws InterruptedException {
        Thread[] threads = new Thread[cnt];
        MyRunnable[] runnables = new MyRunnable[cnt];
        if (list.size() < cnt) {
            cnt = list.size();
        }
        int elementsPerThread = list.size() / cnt;
        for (int i = 0; i < cnt; i++) {
            int firstIndex = i * elementsPerThread;
            int lastIndex = firstIndex + elementsPerThread;
            if (i == cnt - 1 && lastIndex < list.size()) {
                lastIndex = list.size();
            }
            List<? extends T> subList = list.subList(firstIndex, lastIndex);
            runnables[i] = new MyRunnable<>(subList, function);
            threads[i] = new Thread(runnables[i]);
            threads[i].start();
        }
        List<R> results = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            threads[i].join();
            results.add((R) runnables[i].getResult());
        }
        return results;
    }

    @Override
    public String concat(int cnt, List<?> list) throws InterruptedException {
        Function function = new Function<List<?>, String>() {
            @Override
            public String apply(List<?> objects) {
                String result = "";
                for (Object object : objects) {
                    result += object;
                }
                return result;
            }
        };
        List<String> localResults = makeListWorkers(cnt, list, function);
        return (String) function.apply(localResults);
    }

    @Override
    public <T> List<T> filter(int cnt, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        List<List<T>> localResults = makeListWorkers(cnt, list, new Function<List<? extends T>, List<T>>() {
            @Override
            public List<T> apply(List<? extends T> ts) {
                return ts.stream().filter(predicate).collect(Collectors.toList());
            }
        });

        List<T> result = new ArrayList<>();
        for (List<T> subList : localResults) {
            result.addAll(subList);
        }
        return result;
    }

    @Override
    public <T, U> List<U> map(int cnt, List<? extends T> list, Function<? super T, ? extends U> function) throws InterruptedException {
        List<List<U>> localResults = makeListWorkers(cnt, list, new Function<List<? extends T>, List<U>>() {
            @Override
            public List<U> apply(List<? extends T> ts) {
                return ts.stream().map(function).collect(Collectors.toList());
            }
        });

        List<U> result = new ArrayList<>();
        for (List<U> subList : localResults) {
            result.addAll(subList);
        }
        return result;
    }

    @Override
    public <T> T maximum(int cnt, List<? extends T> list, Comparator<? super T> comparator) throws InterruptedException {
        Function function = new Function<List<? extends T>, T>() {
            @Override
            public T apply(List<? extends T> ts) {
                return ts.stream().max(comparator).get();
            }
        };
        List<T> localResults = makeListWorkers(cnt, list, function);
        return (T) function.apply(localResults);
    }

    @Override
    public <T> T minimum(int cnt, List<? extends T> list, Comparator<? super T> comparator) throws InterruptedException {
        Function function = new Function<List<? extends T>, T>() {
            @Override
            public T apply(List<? extends T> ts) {
                return ts.stream().min(comparator).get();
            }
        };
        List<T> localResults = makeListWorkers(cnt, list, function);
        return (T) function.apply(localResults);
    }

    @Override
    public <T> boolean all(int cnt, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        List<Boolean> localResults = makeListWorkers(cnt, list, new Function<List<? extends T>, Boolean>() {
            @Override
            public Boolean apply(List<? extends T> ts) {
                return ts.stream().allMatch(predicate);
            }
        });
        return localResults.stream().allMatch(new Predicate<Boolean>() {
            @Override
            public boolean test(Boolean aBoolean) {
                return aBoolean;
            }
        });
    }

    @Override
    public <T> boolean any(int cnt, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        List<Boolean> localResults = makeListWorkers(cnt, list, new Function<List<? extends T>, Boolean>() {
            @Override
            public Boolean apply(List<? extends T> ts) {
                return ts.stream().anyMatch(predicate);
            }
        });
        return localResults.stream().anyMatch(new Predicate<Boolean>() {
            @Override
            public boolean test(Boolean aBoolean) {
                return aBoolean;
            }
        });
    }
}
