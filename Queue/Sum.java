public class Sum {
    public static void main(String args[]) {
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0; i < args.length; ++i) {
            String str[] = args[i].split("\\s+");
            for (int j = 0; j < str.length; ++j) {
                if (!str[j].isEmpty()) {
                   queue.enqueue(str[j]);
                }
            }
        }
        int sum = 0;
        while (!queue.isEmpty()) {
            sum += Integer.parseInt((String) queue.dequeue());
        }
        System.out.println(sum);
    }
}
