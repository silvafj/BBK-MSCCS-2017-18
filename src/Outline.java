import java.util.Arrays;
import java.util.Comparator;

public class Outline {

    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod";

    public static void main(String... args) {
        String[] strArray = LOREM_IPSUM.split("\\s+");
        System.out.println("Original: " + Arrays.asList(strArray));

        Arrays.sort(strArray, Comparator.comparingInt(String::length));
        System.out.println("Sorted by length: " + Arrays.asList(strArray));

        Arrays.sort(strArray, Comparator.comparingInt(String::length).reversed());
        System.out.println("Sorted by reverse length: " + Arrays.asList(strArray));

        Arrays.sort(strArray, (a, b) -> a.charAt(0) - b.charAt(0));
        System.out.println("Sorted by first character: " + Arrays.asList(strArray));

        Arrays.sort(strArray, Outline::eChecker);
        System.out.println("Sorted by contain 'e' first: " + Arrays.asList(strArray));

        System.out.println("betterString 1: " +
                Outline.betterString("test1", "will return this", (s1, s2) -> s1.length() > s2.length()));

        System.out.println("betterString 2: " +
                Outline.betterString("always this", "test2", (s1, s2) -> true));
    }

    private static int eChecker(String s1, String s2) {
        int i1 = s1.indexOf('e');
        int i2 = s2.indexOf('e');
        if (i1 == -1 && i2 == -1) {
            return 0;
        } else if (i1 >= 0 && i2 == -1) {
            return -1;
        } else if (i1 == -1 && i2 >= 0) {
            return 1;
        } else {
            return i1 - i2;
        }
    }

    private static String betterString(String s1, String s2, TwoStringPredicate checker) {
        return checker.test(s1, s2) ? s1 : s2;
    }
}
