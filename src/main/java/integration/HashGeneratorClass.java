package integration;


public class HashGeneratorClass {

    public static String generateHashForString(String value, String prefix) {

        return prefix + value
                .toUpperCase()
                .replaceAll("[^a-zA-Z0-9]+", "")
                .trim()
                .hashCode();
    }
}
