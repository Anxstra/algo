package six;

import java.util.stream.Collectors;

public class CountingDuplicates {

    public static int duplicateCount(String text) {
        text = text.toLowerCase();
        return (int) text.chars()
                .mapToObj(ch -> (char) ch)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .count();
    }

    public static int duplicateCountWithoutStream(String text) {
        text = text.toLowerCase();
        int count = 0;
        while(!text.isEmpty()) {
            String firstChar = text.substring(0, 1);
            text = text.substring(1);
            if(text.contains(firstChar)) {
                count++;
                text = text.replace(firstChar, "");
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String testThousandA = new String(new char[1000]).replace('\0', 'a');
        String testHundredB = new String(new char[100]).replace('\0', 'b');
        String testTenC = new String(new char[10]).replace('\0', 'c');
        String test1CapitalA = new String(new char[1]).replace('\0', 'A');
        String test1d = new String(new char[1]).replace('\0', 'd');
        String test = test1d + test1CapitalA + testTenC +
                testHundredB + testThousandA;

        System.out.println(3 == duplicateCount(test));
        System.out.println(3 == duplicateCountWithoutStream(test));
    }

}
