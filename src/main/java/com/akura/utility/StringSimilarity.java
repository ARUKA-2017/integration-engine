package com.akura.utility;

import java.util.ArrayList;

/**
 * Class representing a StringSimilarity.
 */
public class StringSimilarity {

    /**
     * Method used to get the letter pairs.
     *
     * @param str - string to be tokenize into pairs.
     * @return - Array of pairs.
     */
    public static String[] letterPairs(String str) {

        int numPairs = str.length() - 1;
        String[] pairs = new String[numPairs];

        for (int i = 0; i < numPairs; i++) {
            pairs[i] = str.substring(i, i + 2);
        }

        return pairs;
    }

    /**
     * Methos used to get the word letter pairs.
     *
     * @param str - sentence to be tokenize into pairs.
     * @return - List of all pairs.
     */
    private static ArrayList wordLetterPairs(String str) {
        ArrayList allPairs = new ArrayList();

        // Tokenize the string and put the tokens/words into an array
        String[] words = str.split("\\s");
        // For each word

        for (int w = 0; w < words.length; w++) {
            // Find the pairs of characters
            String[] pairsInWord = letterPairs(words[w]);

            for (int p = 0; p < pairsInWord.length; p++) {
                allPairs.add(pairsInWord[p]);
            }
        }

        return allPairs;
    }

    /**
     * Method used to get the compare strings.
     *
     * @param str1 - string one to be compared.
     * @param str2 - string second to be compared.
     * @return - string compare weight.
     */
    public static double compareStrings(String str1, String str2) {
        ArrayList pairs1 = wordLetterPairs(str1.trim().toUpperCase());
        ArrayList pairs2 = wordLetterPairs(str2.trim().toUpperCase());

        int intersection = 0;
        int union = pairs1.size() + pairs2.size();

        for (int i = 0; i < pairs1.size(); i++) {

            Object pair1 = pairs1.get(i);

            for (int j = 0; j < pairs2.size(); j++) {
                Object pair2 = pairs2.get(j);

                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(j);
                    break;
                }
            }
        }

        return (2.0 * intersection) / union;
    }
}
