// inv: size >= 0
//      (head < tail && elements[head..tail - 1] != null) ||
//      || (head > tail && elements[head..elements.length - 1] != null && elements[0..tail - 1] != null)
//      || (head == tail)
public class ArrayDequeADT {
    private int size = 0;
    private int head = 0, tail = 0;
    private Object[] elements = new Object[5];
    
    // pre: element != null
    //      0 <= tail < elements.length
    // post: size = size' + 1
    //       elements[tail] == element
    //       (tail' + 1 < elements.length && tail == tail' + 1) || (tail' + 1 == elements.length && tail == 0) ||
    //                      || (size' + 1 > elements.length && head == 0 && tail == size')
    public static void addLast(ArrayDequeADT deque, Object element) {
        assert element != null;
        ensureCapacity(deque, deque.size + 1);
        deque.elements[deque.tail++] = element;
        if (deque.tail == deque.elements.length) {
            deque.tail = 0;
        }
        ++deque.size;
    }
    
    // pre: element != null
    //      0 <= tail < elements.length
    // post: size = size' + 1
    //       elements[tail] == element
    //       (tail' + 1 < elements.length && tail == tail' + 1) || (tail' + 1 == elements.length && tail == 0) ||
    //                      || (size' + 1 > elements.length && head == 0 && tail == size')
    public static void addFirst(ArrayDequeADT deque, Object element) {
        assert element != null;
        ensureCapacity(deque, deque.size + 1);
        --deque.head;
        if (deque.head < 0) {
            deque.head = deque.elements.length - 1;
        }
        deque.elements[deque.head] = element;
        ++deque.size;
    }
    
    private static void ensureCapacity(ArrayDequeADT deque, int capacity) {
        if (capacity > deque.elements.length) {
            Object[] temp = new Object[2 * capacity];
            for (int i = 0; i < deque.elements.length; i++) {
                temp[i] = deque.elements[(deque.head + i) % deque.elements.length];
            }
            deque.elements = temp;
            deque.head = 0;
            deque.tail = deque.size;
        }
    }
    
    // pre: size > 0
    //      0 <= head < elements.length
    // post: result == elements[head]
    //       size = size' - 1
    //       (head' + 1 < elements.length && head == head' + 1) || head == 0
    public static Object removeFirst(ArrayDequeADT deque) {
        assert deque.size > 0;
        Object result = deque.elements[deque.head++];
        deque.head %= deque.elements.length;
        --deque.size;
        return result;
    }
    
    // pre: size > 0
    //      0 <= tail < elements.length
    // post: result == elements[(tail - 1) %= elements.length;]
    //       size = size' - 1
    //       (tail' - 1 > 0 && tail == tail' - 1) || tail == elements.length - 1
    public static Object removeLast(ArrayDequeADT deque) {
        assert deque.size > 0;
        --deque.tail;
        if (deque.tail < 0) {
            deque.tail = deque.elements.length - 1;
        }
        Object result = deque.elements[deque.tail];
        --deque.size;
        return result;
    }
    
    // pre: size > 0
    //      0 <= head < elements.length
    // post: result == elements[head]
    public static Object peekFirst(ArrayDequeADT deque) {
        assert deque.size > 0;
        return deque.elements[deque.head];
    }
    
    // pre: size > 0
    //      0 <= tail < elements.length
    // post: elements[(tail - 1) % elements.length]
    public static Object peekLast(ArrayDequeADT deque) {
        assert deque.size > 0;
        int position = deque.tail - 1;
        if (position < 0) {
            position = deque.elements.length - 1;
        }
        return deque.elements[position];
    }
    
    // post: result == size
    public static int size(ArrayDequeADT deque) {
        return deque.size;
    }
    
    // post: result == 0 > 0
    public static boolean isEmpty(ArrayDequeADT deque) {
        return deque.size == 0;
    }
}
