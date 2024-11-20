package com.expense_manager.comman;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void printDuplicateCharacters(String str) {
        char[] chars = str.toCharArray();

        Map<Character, Integer> charMap = new HashMap<>();
        for (char c : chars) {
            charMap.put(c, charMap.getOrDefault(c, 0) + 1);
        }

        for (Map.Entry<Character, Integer> entry : charMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    public static void main(String[] args) {
        String input = "programming";    // Example input
        printDuplicateCharacters(input);

        System.out.println("Is Parindrom: "+isPalindrom("aba"));
    }

    public static boolean isPalindrom(String str) {

        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
