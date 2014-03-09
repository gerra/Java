public class QueueTest {
    static void testSingleton() {
        for (int i = 0; i < 10; ++i) {
            ArrayQueueSingleton.push(i);
        }
        while (!ArrayQueueSingleton.isEmpty()) {
            System.out.println(
                ArrayQueueSingleton.size() + " " +
                ArrayQueueSingleton.front() + " " +
                ArrayQueueSingleton.pop()
            );
        }
    }
    
    
    static void testADT() {
        ArrayQueueADT queue = new ArrayQueueADT();
        for (int i = 0; i < 10; ++i) {
            ArrayQueueADT.push(queue, i);
        }
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(
                ArrayQueueADT.size(queue) + " " +
                ArrayQueueADT.front(queue) + " " +
                ArrayQueueADT.pop(queue)
            );
        }
    }
    
    static void test() {
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0; i < 10; ++i) {
            queue.push(i);
        }
        while (!queue.isEmpty()) {
            System.out.println(
                queue.size() + " " +
                queue.front() + " " +
                queue.pop()
            );
        }
    }
    
    public static void main(String[] args) {
        test();
        //test(new ArrayQueueSingleton());
        //test(new ArrayStack());
        //test(new LinkedStack());
    }
}
/*
 4 3 2 1 0 null null 9 8 7 6 5
*/
