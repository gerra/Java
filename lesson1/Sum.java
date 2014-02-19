public class Sum {
    static short getShort(String s) {
        short res = 0;
        int j = 0;
        if (s.startsWith("-")) {
            j = 1;
        }
        String t = s.substring(j);
        if (t.startsWith("q") || t.startsWith("Q")) {
            s = s.substring(j + 1);
            if (j == 1) {
                s = "-" + s;
            }
            res = (short)Integer.parseInt(s, 4);
        } else {
            res = (short)Integer.parseInt(s, 10);
        }
        return res;
    }

    public static void main(String args[]) {
        short sum = 0; 
        for (int i = 0; i < args.length; ++i) {
            String str[] = args[i].split("\\s+");
            for (int j = 0; j < str.length; ++j) {
                if (!str[j].isEmpty()) {
                   sum += getShort(str[j]);
                }
            }
        }
        System.out.println(sum);
    }
}