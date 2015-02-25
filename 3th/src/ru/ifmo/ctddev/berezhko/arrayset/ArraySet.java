package ru.ifmo.ctddev.berezhko.arrayset;


import java.util.*;

/**
 * Created by german on 25.02.15.
 */


public class ArraySet<E> implements NavigableSet<E>  {

    private ArrayList<E> set;
    private Comparator<? super E> realComparator;
    private Comparator<? super E> comparator;


    public ArraySet(Collection<? extends E> set, Comparator<? super E> comparator) {
        this.set = new ArrayList<>(set);
        this.realComparator = comparator;
        this.comparator = comparator != null ? comparator : new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return ((Comparable)o1).compareTo(o2);
            }
        };
        this.set.sort(comparator);
        ArrayList<E> unique = new ArrayList<>();
        for (E e : this.set) {
            if (unique.size() == 0 || comparator == null || comparator.compare(unique.get(unique.size() - 1), e) != 0) {
                unique.add(e);
            }
        }
        this.set = unique;
    }

    public ArraySet(Collection<? extends E> set) {
        this(set, null);
    }

    public ArraySet() {
        this(new ArrayList<E>(), null);
    }

    public ArraySet(Comparator<? super E> comparator) {
        this(new ArrayList<E>(), comparator);
    }

    // -1  true: <=
    // -1 false:  <
    //  1 false:  >
    //  1  true: >=
    private int binSearchInd(E x, int signType, boolean canNull) {
        int l = 0;
        int r = size();
        int res = -1;
        while (l != r) {
            int m = (l + r) / 2;
            int sign = comparator.compare(set.get(m), x);

            if (signType == -1) {
                if (sign < 0 && !canNull || sign <= 0 && canNull) {
                    res = m;
                    l = m+1;
                } else {
                    r = m;
                }
            } else {
                if (sign > 0 && !canNull || sign >= 0 && canNull) {
                    res = m;
                    r = m;
                } else {
                    l = m+1;
                }
            }
        }
        if (res == size()) {
            res = -1;
        }
        return res;
    }


    public E binSearch(E x, int signType, boolean canNull) {
        int ind = binSearchInd(x, signType, canNull);
        if (ind == -1) {
            return null;
        } else {
            return set.get(ind);
        }
    }

    @Override
    public E lower(E e) {
        return binSearch(e, -1, false);
    }

    @Override
    public E floor(E e) {
        return binSearch(e, -1, true);
    }

    @Override
    public E ceiling(E e) {
        return binSearch(e, 1, true);
    }

    @Override
    public E higher(E e) {
        return binSearch(e, 1, false);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        E ceiling = ceiling((E)o);
        return (ceiling == null ? false : comparator.compare(ceiling, (E)o) == 0);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex + 1 < size();
            }

            @Override
            public E next() {
                return set.get(currentIndex++);
            }
        };
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public NavigableSet<E> descendingSet() {
        ArrayList<E> descendingSet = new ArrayList<>(set);
        Collections.reverse(descendingSet);
        return new ArraySet<>(descendingSet, realComparator);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (size() == 0) {
            return new ArraySet<>(realComparator);
        }

        int from = binSearchInd(fromElement, 1, fromInclusive);
        int to = binSearchInd(toElement, -1, toInclusive);

        if (from == -1 || to == -1 || from > to) {
            return new ArraySet<>(realComparator);
        }
        ArrayList<E> subList = new ArrayList<>(to-from+1);
        for (int i = from; i <= to; i++) {
            subList.add(set.get(i));
        }
        return new ArraySet<>(subList, realComparator);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        if (size() == 0) {
            return new ArraySet<>(realComparator);
        }
        return subSet(first(), true, toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        if (size() == 0) {
            return new ArraySet<>(realComparator);
        }
        return subSet(fromElement, inclusive, last(), true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return realComparator;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public E first() {
        if (set.size() == 0) {
            throw new NoSuchElementException();
        }
        return set.get(0);
    }

    @Override
    public E last() {
        if (set.size() == 0) {
            throw new NoSuchElementException();
        }
        return set.get(set.size() - 1);
    }

    // Unsupported operations
    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
