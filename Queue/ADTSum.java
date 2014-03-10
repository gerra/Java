public class ADTSum {
    public static void main(String args[]) {
        ArrayQueueADT queue = new ArrayQueueADT();
        for (int i = 0; i < args.length; ++i) {
            String str[] = args[i].split("\\s+");
            for (int j = 0; j < str.length; ++j) {
                if (!str[j].isEmpty()) {
                   ArrayQueueADT.enqueue(queue, str[j]);
                }
            }
        }
        int sum = 0;
        while (!ArrayQueueADT.isEmpty(queue)) {
            sum += Integer.parseInt((String) ArrayQueueADT.dequeue(queue));
        }
        System.out.println(sum);
    }
}

