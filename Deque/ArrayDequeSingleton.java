// inv: size >= 0
//      (head < tail && elements[head..tail - 1] != null) ||
//      || (head > tail && elements[head..elements.length - 1] != null && elements[0..tail - 1] != null)
//      || (head == tail)
public class ArrayDequeSingleton {
    private static int size = 0;
    private static int head = 0, tail = 0;
    private static Object[] elements = new Object[5];
    
    // pre: element != null
    //      0 <= tail < elements.length
    // post: size = size' + 1
    //       elements[tail] == element
    //       (tail' + 1 < elements.length && tail == tail' + 1) || (tail' + 1 == elements.length && tail == 0) ||
    //                      || (size' + 1 > elements.length && head == 0 && tail == size')
    public static void addLast(Object element) {
        assert element != null;
        ensureCapacity(size + 1);
        elements[tail++] = element;
        if (tail == elements.length) {
            tail = 0;
        }
        ++size;
    }
    
    // pre: element != null
    //      0 <= tail < elements.length
    // post: size = size' + 1
    //       elements[tail] == element
    //       (tail' + 1 < elements.length && tail == tail' + 1) || (tail' + 1 == elements.length && tail == 0) ||
    //                      || (size' + 1 > elements.length && head == 0 && tail == size')
    public static void addFirst(Object element) {
        assert element != null;
        ensureCapacity(size + 1);
        --head;
        if (head < 0) {
            head = elements.length - 1;
        }
        elements[head] = element;
        ++size;
    }
    
    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] temp = new Object[2 * capacity];
            for (int i = 0; i < elements.length; i++) {
                temp[i] = elements[(head + i) % elements.length];
            }
            elements = temp;
            head = 0;
            tail = size;
        }
    }
    
    // pre: size > 0
    //      0 <= head < elements.length
    // post: result == elements[head]
    //       size = size' - 1
    //       (head' + 1 < elements.length && head == head' + 1) || head == 0
    public static Object removeFirst() {
        assert size > 0;
        Object result = elements[head++];
        head %= elements.length;
        --size;
        return result;
    }
    
    // pre: size > 0
    //      0 <= tail < elements.length
    // post: result == elements[(tail - 1) %= elements.length;]
    //       size = size' - 1
    //       (tail' - 1 > 0 && tail == tail' - 1) || tail == elements.length - 1
    public static Object removeLast() {
        assert size > 0;
        --tail;
        if (tail < 0) {
            tail = elements.length - 1;
        }
        Object result = elements[tail];
        --size;
        return result;
    }
    
    // pre: size > 0
    //      0 <= head < elements.length
    // post: result == elements[head]
    public static Object peekFirst() {
        assert size > 0;
        return elements[head];
    }
    
    // pre: size > 0
    //      0 <= tail < elements.length
    // post: elements[(tail - 1) % elements.length]
    public static Object peekLast() {
        assert size > 0;
        int position = tail - 1;
        if (position < 0) {
            position = elements.length - 1;
        }
        return elements[position];
    }
    
    // post: result == size
    public static int size() {
        return size;
    }
    
    // post: result == 0 > 0
    public static boolean isEmpty() {
        return size == 0;
    }
}
