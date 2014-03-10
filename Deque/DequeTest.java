public class DequeTest {
    public static void main(String args[]) {
        ArrayDeque deque = new ArrayDeque();
        for (int i = 0; i < 10; ++i) {
            deque.addFirst(2 * i);
            deque.addLast(2 * i + 1);
        }
        while (!deque.isEmpty()) {
            System.out.println(
                deque.size() + " " +
                deque.peekFirst() + " " +
                deque.peekLast() + " " +
                deque.removeFirst() + " " +
                deque.removeLast()
            );
        }
    }
}
