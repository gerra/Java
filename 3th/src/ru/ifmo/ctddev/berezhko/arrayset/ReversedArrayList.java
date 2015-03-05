package ru.ifmo.ctddev.berezhko.arrayset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by root on 05.03.15.
 */
public class ReversedArrayList<E> extends ArrayList<E> {
    private ArrayList<E> forwardList;

    public ReversedArrayList(ArrayList<E> forwardList) {
        this.forwardList = forwardList;
    }

    @Override
    public E get(int index) {
        return forwardList.get(forwardList.size() - index - 1);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int it = 0;

            @Override
            public boolean hasNext() {
                return it != forwardList.size();
            }

            @Override
            public E next() {
                if (it >= forwardList.size()) {
                    throw new NoSuchElementException();
                }
                return get(it++);
            }
        };
    }
}
