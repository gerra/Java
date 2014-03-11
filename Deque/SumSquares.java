public class SumSquares {
    static double sumSquares(ArrayDeque d) {
        double result = 0.0;
        int sz = d.size();
        for (int i = 0; i < sz; ++i) {
            double y = (Double) d.peekFirst();
            result += y * y;
            d.addLast(d.removeFirst());
        }
        return result;
    }
    
    public static void main(String args[]) {
        ArrayDeque deque = new ArrayDeque();
        for (double i = 0; i < 5; i += 0.5) {
            deque.addFirst(i);
        }
        System.out.println(deque.size());
        System.out.println(sumSquares(deque));
        System.out.println(deque.size());
    }
}
