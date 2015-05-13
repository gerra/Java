package ru.ifmo.ctddev.berezhko.webcrawler;

import info.kgeorgiy.java.advanced.crawler.CachingDownloader;

import java.io.IOException;

/**
 * Created by root on 22.04.15.
 */
public class Tester {
    public static void main(String[] args) throws IOException {
        WebCrawler wc = new WebCrawler(new CachingDownloader(), 1, 1, 1);

    }
}
