package search;

public class BinarySearchSpan {

    // Pred: forall i=1..args.length-2 : int(args[i]) >= int(args[i + 1]) &&
    //       args.length > 0 && args[i] = int
    // Post: Res = i : (args[i + 1] <= x && i -- минимально)
    //       Res = j : (array[i + j - 1] == x && j > 0 && j -- максимально) || (j == 0)
    public static void main(final String[] args) {
        final int x = Integer.parseInt(args[0]);
        final int[] array = new int[args.length - 1];

        // Pred: forall i=1..args.length-2 : int(args[i]) >= int(args[i + 1]) && args[i] = int
        // Post: forall i=0..array.length-1 : array[i] >= array[i + 1] && array[-1] = +INF && array[array.length] = -INF
        for (int i = 1; i < args.length; i++) {
            array[i - 1] = Integer.parseInt(args[i]);
        }

        // Pred: forall i=0..array.length-1 : array[i] >= array[i + 1] && array[-1] = +INF && array[array.length] = -INF
        // Post: left = i : (array[i] <= x && i -- минимально)
        final int left = iterativeBinSearch(x, array);
        // Pred: forall i=0..array.length-1 : array[i] >= array[i + 1] && array.length > 0
        // Post: len = j : (array[left + j - 1] == x && j > 0 && j -- максимально) || (j == 0)
        final int len = recursiveBinSearch(x, array, -1, array.length) - left;
        System.out.print(left + " " + len);
    }


    // Let n == array.length
    // Pred: forall i=0..n-1 : array[i] >= array[i + 1]
    // Post: Res = i : (array[i] <= x && i -- минимально)
    public static int iterativeBinSearch(final int x, final int[] array) {
        // Pred: true
        // Post: l == -1;
        int l = -1;
        // Pred: true
        // Post: r == n
        int r = array.length;
        // Pred: l + 1 <= r
        // Post: l + 1 == r && array[r] <= x && array[l] > x
        // Inv: l < r && array[r] <= x && array[l] > x && (r - l) <= (r' - l')
        while (l + 1 < r) {
            // Pred: true
            // Post: mid == (l + r) / 2
            final int mid = (l + r) / 2;
            // Pred: mid >= 0 && mid < n && forall i=0..n-2 : array[i] >= array[i + 1]
            // Post: l < r && array[r] <= x && array[l] > x && (r - l) <= (r' - l')
            if (array[mid] > x) {
                // Pred: array[mid] > x
                // Post: l == mid && r == r' && l < r
                l = mid;
            } else {
                // Pred: array[mid] <= x
                // Post: l == l' && r == mid && l < r
                r = mid;
            }
        }
        // Pred: l + 1 == r
        // Post: Res = r : (array[r] <= x && i -- минимально)
        return r;
    }

    // Let n == array.length
    // :NOTE: l == r == Integer.MIN_VALUE
    // Pred: forall i=0..n-1 : array[i] >= array[i + 1] && l < r && array[r] < x && array[l] >= x
    // Post: Res = i : (array[i] <= x && i -- минимально)
    public static int recursiveBinSearch(final int x, final int[] array, final int l, final int r) {
        if (l + 1 == r) {
            // Pred: l + 1 == r && array[r] < x && array[l] >= x
            // Post: Res == r : array[r] < x && array[r - 1] >= x
            return r;
        }

        // :NOTE: Отсортированность?
        // Pred: true
        // Post: mid == (l + r) / 2
        int mid = (l + r) / 2;
        // Pred: mid == (l + r) / 2 && forall i=0..n-1 : array[i] >= array[i + 1]
        // Post: Res == r : array[r] < x && array[r - 1] >= x
        if (array[mid] >= x) {
            mid = l;
            // Pred: array[mid] >= x
            return recursiveBinSearch(x, array, mid, r);
        } else {
            // Pred: array[mid] < x
            return recursiveBinSearch(x, array, l, mid);
        }
    }

}
