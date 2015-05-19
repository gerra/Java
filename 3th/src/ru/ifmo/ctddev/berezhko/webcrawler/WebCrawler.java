package ru.ifmo.ctddev.berezhko.webcrawler;

import info.kgeorgiy.java.advanced.crawler.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WebCrawler implements Crawler {
    private Downloader downloader;
    private int downloaders;
    private int extractors;
    private int perHost;

    private ExecutorService downloaderService;
    private ExecutorService extractorService;
    private ConcurrentMap<String, IOException> exceptions;

    public static void main(String args[]) {
        if (args == null || args.length != 4 || Arrays.stream(args).anyMatch(Predicate.isEqual(null))) {
            System.err.println("Check arguments count");
            return;
        }
        String url = args[0];
        int downloaders = 50;
        int extractors = 50;
        if (args.length >= 1) {
            downloaders = Integer.parseInt(args[1]);
        }
        if (args.length >= 2) {
            extractors = Integer.parseInt(args[2]);
        }
        int perHost = downloaders;
        if (args.length >= 3) {
            perHost = Integer.parseInt(args[3]);
        }
        try (Crawler crawler = new WebCrawler(
                new CachingDownloader(new File("./temp/")),
                downloaders,
                extractors,
                perHost)) {
            Result res = crawler.download(url, 2);
            System.out.println("finish " + res.getDownloaded().size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.downloaders = downloaders;
        this.extractors = extractors;
        this.perHost = perHost;
    }

    @Override
    public Result download(String url, int depth) {
        if (depth == 0) {
            return new Result(new ArrayList<>(), new HashMap<>());
        }
        downloaderService = Executors.newFixedThreadPool(downloaders);
        extractorService = Executors.newFixedThreadPool(extractors);
        final BlockingQueue<Pair<String, Integer>> queue = new PriorityBlockingQueue<>(100, new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        });

        final Semaphore mainSemaphore = new Semaphore(Integer.MAX_VALUE);
        final ConcurrentMap<String, Semaphore> availableHosts = new ConcurrentHashMap<>();
        final ConcurrentMap<String, Object> visited = new ConcurrentHashMap<>();
        exceptions = new ConcurrentHashMap<>();

        try {
            queue.add(new Pair<>(url, depth));
            mainSemaphore.acquire();
            downloaderService.submit(() -> download(visited, queue, availableHosts, mainSemaphore));
        } catch (InterruptedException e) {
            return null;
        }
        try {
            mainSemaphore.acquire(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> links = visited.keySet().stream().collect(Collectors.toList());
        return new Result(links, exceptions);
    }

    private void download(ConcurrentMap<String, Object> visited,
                          BlockingQueue<Pair<String, Integer>> queue,
                          ConcurrentMap<String, Semaphore> availableHosts,
                          Semaphore mainSemaphore) {
        try {
            if (!queue.isEmpty()) {
                Pair<String, Integer> cur = queue.poll();
               // System.out.println(cur);
                String url = cur.getKey();
                int depth = cur.getValue();
                if (!visited.containsKey(url) && !exceptions.containsKey(url)) {
                    try {
                        String host = URLUtils.getHost(url);
                        availableHosts.putIfAbsent(host, new Semaphore(perHost));
                        if (availableHosts.get(host).tryAcquire()) {
                            visited.put(url, new Object());
                            try {
                                Document document = downloader.download(url);
                                mainSemaphore.acquire();
                                extractorService.submit(() -> extract(document, depth, visited, queue, availableHosts, mainSemaphore));
                            } catch (IOException e) {
                                visited.remove(url);
                                System.err.println("Unable to download document");
                                exceptions.put(url, e);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                availableHosts.get(host).release();
                                if (!queue.isEmpty()) {
                                    try {
                                        mainSemaphore.acquire();
                                        downloaderService.submit(() -> download(visited, queue, availableHosts, mainSemaphore));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            queue.add(cur);

                        }
                    } catch (MalformedURLException e) {
                        System.err.println("Unable to get host");
                        exceptions.put(url, e);
                    }
                }
            }
        } finally {
            mainSemaphore.release();
        }
    }

    private void extract(Document document, int depth,
                         ConcurrentMap<String, Object> visited,
                         BlockingQueue<Pair<String, Integer>> queue,
                         ConcurrentMap<String, Semaphore> availableHosts,
                         Semaphore mainSemaphore) {
        try {
            if (depth > 1) {
                List<String> links = document.extractLinks();
                links.parallelStream().filter(s -> !visited.containsKey(s)).distinct().forEach(s -> {
                    try {
                        queue.add(new Pair<>(s, depth - 1));
                        mainSemaphore.acquire();
                        downloaderService.submit(() -> download(visited, queue, availableHosts, mainSemaphore));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Unable to extract links");
        } finally {
            mainSemaphore.release();
        }
    }

    @Override
    public void close() {
        stopService(extractorService);
        stopService(downloaderService);
    }

    private void stopService(ExecutorService service) {
        if (!service.isShutdown()) {
            service.shutdown();
        }
    }
}
