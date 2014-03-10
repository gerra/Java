public class SingletonSum {
    public static void main(String args[]) {
        for (int i = 0; i < args.length; ++i) {
            String str[] = args[i].split("\\s+");
            for (int j = 0; j < str.length; ++j) {
                if (!str[j].isEmpty()) {
                   ArrayQueueSingleton.enqueue(str[j]);
                }
            }
        }
        int sum = 0;
        while (!ArrayQueueSingleton.isEmpty()) {
            sum += Integer.parseInt((String) ArrayQueueSingleton.dequeue());
        }
        System.out.println(sum);
    }
}
