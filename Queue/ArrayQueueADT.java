// inv: size >= 0
//      (head < tail && elements[head..tail - 1] != null) ||
//      || (head > tail && elements[head..elements.length - 1] != null && elements[0..tail - 1] != null)
//      || (head == tail)
public class ArrayQueueADT {
    private /*static*/ int size = 0;
    private /*static*/ int head = 0, tail = 0;
    private /*static*/ Object[] elements = new Object[5];
    
    // pre: element != null
    //      0 <= tail < elements.length
    // post: size = size' + 1
    //       elements[tail] == element
    //       (tail' + 1 < elements.length && tail == tail' + 1) || (tail' + 1 == elements.length && tail == 0) ||
    //                      || (size' + 1 > elements.length && head == 0 && tail == size')
    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail++] = element;
        if (queue.tail == queue.elements.length) {
            queue.tail = 0;
        }
        ++queue.size;
    }
    
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            Object[] temp = new Object[2 * capacity];
            for (int i = 0; i < queue.elements.length; i++) {
                temp[i] = queue.elements[(queue.head + i) % queue.size];
            }
            queue.elements = temp;
            queue.head = 0;
            queue.tail = queue.size;
        }
    }
    
    // pre: size > 0
    //      0 <= head < elements.length
    // post: result == elements[head]
    //       size = size' - 1
    //       (head' + 1 < elements.length && head == head' + 1) || head == 0
    public static Object pop(ArrayQueueADT queue) {
        assert queue.size > 0;
        
        Object result = queue.elements[queue.head++];
        if (queue.head == queue.elements.length) {
            queue.head = 0;
        }
        --queue.size;
        return result;
    }
    
    // pre: size > 0
    //      0 <= head < elements.length
    // post: result == elements[head]
    public static Object front(ArrayQueueADT queue) {
        assert queue.size > 0;
        
        return queue.elements[queue.head];
    }
    
    // post: result == size
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }
    
    // post: result == 0 > 0
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }
}
