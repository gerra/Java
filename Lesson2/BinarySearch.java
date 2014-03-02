public class BinarySearch {
    // pre: l >= 0 && r <= a.length
    // post: l <= res < r
    public static int binSearch(int[] a, int x, int l, int r) {
        if (l + 1 == r)
            return r;
        int m = (l + r) / 2;
        if (a[m] >= x)
            return binSearch(a, x, l, m);
        else
            return binSearch(a, x, m, r);
    }
    
    // pre: 
    // post; res == r
    public static int binSearch(int[] a, int x) {
        int l = 0;
        int r = a.length;
        while (l + 1 != r) {
            // inv: l < res <= r
            int m = (l + r) / 2;
            if (a[m] >= x)
                r = m;
            else
                l = m;
        }
        return r;
    }
    
    public static void main(String args[]) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; ++i) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        int res;
        if (a[0] < x)
            res = binSearch(a, x);
        else
            res = 0;
        System.out.println(res);
    }
}