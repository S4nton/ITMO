package search;

public class BinarySearch {

    // Pred: forall i=1..args.length-1 : args[i] >= args[i + 1] && args.length > 1
    // Post: Res = i : (i >= 0 && i < args.length - 1 && args[0] >= args[i] && i -- минимально) ||
    // (i == args.length - 1 && args[0] < args[args.length - 1])
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] array = new int[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            array[i - 1] = Integer.parseInt(args[i]);
        }
        
        System.out.print(iterativeBinSearch(x, array));
    }


    // Pred: forall i=0..array.length-1 : array[i] >= array[i + 1]
    // Post: Res = i : (i >= 0 && i < array.length && x >= array[i] && i -- минимально) ||
    // (i == array.length && x < array[array.length - 1])
    public static int iterativeBinSearch(int x, int[] array) {
        // Pred: true
        // Post: l == -1;
        int l = -1;
        // Pred: true
        // Post: r == array.length;
        int r = array.length;
        // Pred: l + 1 < r
        // Post: l + 1 == r && (r == array.length || array[r] <= x) && (l == -1 || array[l] > x)
        // Inv: l < r && (r == array.length || array[r] <= x) && (l == -1 || array[l] > x) && (r - l) <= (r' - l')
        while (l + 1 < r) {
            // Pred: true
            // Post: mid == (l + r) / 2
            int mid = (l + r) / 2;
            // Pred: mid >= 0 && mid < array.length
            // Post: l < r && (r == array.length || array[r] <= x) && (l == -1 || array[l] > x)
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
        // Post: Res == r : ((r == array.length && array[array.length - 1] > x) || (array[r] <= x && (r - 1 == -1 || array[r - 1] > x)))
        return r;
    }

    // Pred: forall i=0..array.length-1 : array[i] >= array[i + 1] && l' == -1 && r' == array.length
    // Post: Res = i : (i >= 0 && i < array.length && x >= array[i] && i -- минимально) ||
    // (i == array.length && x < array[array.length - 1])
    // Inv: l < r && (r == array.length || array[r] <= x) && (l == -1 || array[l] > x) && (r - l) <= (r' - l')
    public static int recursiveBinSearch(int x, int[] array, int l, int r) {
        if (l + 1 == r) {
            // Pred: l + 1 == r && (r == array.length || array[r] <= x) && (l == -1 || array[l] > x)
            // Post: Res == r : ((r == array.length && array[array.length - 1] > x) || (array[r] <= x && (r - 1 == -1 || array[r - 1] > x)))
            return r;
        }

        // Pred: true
        // Post: mid == (l + r) / 2
        int mid = (l + r) / 2;
        if (array[mid] > x) {
            // Pred: mid == (l + r) / 2 && array[mid] > x
            // Post: Res == r : ((r == array.length && array[array.length - 1] > x) || (array[r] <= x && (r - 1 == -1 || array[r - 1] > x)))
            return recursiveBinSearch(x, array, mid, r);
        } else {
            // Pred: mid == (l + r) / 2 && array[mid] <= x
            // Post: Res == r : ((r == array.length && array[array.length - 1] > x) || (array[r] <= x && (r - 1 == -1 || array[r - 1] > x)))
            return recursiveBinSearch(x, array, l, mid);
        }
    }

}
