package ru.ifmo.ctddev.berezhko.arrayset;

import java.util.ArrayList;

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
}
