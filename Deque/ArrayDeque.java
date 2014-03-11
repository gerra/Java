// inv: size >= 0
//      elements[head..tail - 1] != null
public class ArrayDeque {
    private int size = 0;
    private int head = 0, tail = 0;
    private Object[] elements = new Object[5];
    
    // pre: element != null
    // post: size = size' + 1
    //       tail = tail' + 1
    //       elements[tail' + 1] == element
    //       elements[head..tail'] = elements'[head..tail']
    public void addLast(Object element) {
        assert element != null;
        ensureCapacity(size + 1);
        elements[tail++] = element;
        if (tail == elements.length) {
            tail = 0;
        }
        ++size;
    }
    
    // pre: element != null
    // post: size = size' + 1
    //       head = head' - 1
    //       elements[head' - 1] == element
    //       elements[head'..tail] = elements'[head'..tail]
    public void addFirst(Object element) {
        assert element != null;
        ensureCapacity(size + 1);
        --head;
        if (head < 0) {
            head = elements.length - 1;
        }
        elements[head] = element;
        ++size;
    }
    
    private void ensureCapacity(int capacity) {
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
    // post: result == elements[head]
    //       size = size' - 1
    //       head = head' + 1
    public Object removeFirst() {
        assert size > 0;
        Object result = elements[head++];
        head %= elements.length;
        --size;
        return result;
    }
    
    // pre: size > 0
    // post: result = elements[tail - 1]
    //       size = size' - 1
    //       tail = tail' - 1
    public Object removeLast() {
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
    // post: result == elements[head]
    public Object peekFirst() {
        assert size > 0;
        return elements[head];
    }
    
    // pre: size > 0
    // post: result == elements[tail - 1]
    public Object peekLast() {
        assert size > 0;
        int position = tail - 1;
        if (position < 0) {
            position = elements.length - 1;
        }
        return elements[position];
    }
    
    // post: result == size
    public int size() {
        return size;
    }
    
    // post: result == (size == 0 > 0)
    public boolean isEmpty() {
        return size == 0;
    }
}
