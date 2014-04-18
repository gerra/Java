public class BinarySearch {
    // pre: l >= 0 && r <= a.length
    // post: l <= res <= r
    public static int binSearch(int[] a, int x, int l, int r) {
        if (l == r) {
            return r;
        }
        int m = (l + r) / 2;
        if (a[m] >= x) {
            return binSearch(a, x, l, m);
        }
        else {
            return binSearch(a, x, m + 1, r);
        }
    }
    
    // pre: 
    // post: a[a.length - 1] < x && res == a.length or a[res] >= x && res == r
    public static int binSearch(int[] a, int x) {
        int l = 0;
        int r = a.length;
        while (l != r) {
            // inv: l <= res <= r
            int m = (l + r) / 2;
            if (a[m] >= x) {
                r = m;
            }
            else {
                l = m + 1;
            }
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
        res = binSearch(a, x);
        System.out.println(res);
    }
}
