package six;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CountingDuplicates {

    public static int duplicateCount(String text) {
        String lowerCase = text.toLowerCase();
        return (int)Arrays.stream(lowerCase.split(""))
                .collect(Collectors.toMap(str -> str, str -> 1, Integer::sum)).values().stream()
                .filter(value -> value > 1).count();
    }

    public static int duplicateCountWithoutStream(String text) {
        String lowerCase = text.toLowerCase();
        int count = 0;
        while(!lowerCase.isEmpty()) {
            String firstChar = lowerCase.substring(0, 1);
            lowerCase = lowerCase.substring(1);
            if(lowerCase.contains(firstChar)) {
                count++;
                lowerCase = lowerCase.replace(firstChar, "");
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
