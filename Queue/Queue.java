public class Queue {
    static int size = 0;
    static int head = 4, tail = 4;
    static Object[] elements = new Object[5];
    
    static void push(Object element) {
        assert element != null;
        
        ensureCapacity(size + 1);
        
        elements[tail--] = element;
        if (tail == -1) {
            tail = elements.length - 1;
        }
        ++size;
    }
    
    static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {            
            Object[] temp = new Object[2 * capacity];
            for (int i = 0; i < size; i++) {
                temp[i] = elements[i];
            }
            elements = temp;
        }
    }
    
    static Object pop() {
        assert size > 0;
        
        int result = elements[head--];
        if (head == -1) {
            head = elements.length - 1;
        }
        --size;
        return result;
    }
    
    static Object front() {
        assert size > 0;
        
        return elements[head];
    }
    
    static int size() {
        return size;
    }
    
    static boolean isEmpty() {
        return size == 0;
    }
}
