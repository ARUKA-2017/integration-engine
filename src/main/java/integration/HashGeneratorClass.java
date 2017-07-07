package integration;


public class HashGeneratorClass {

    public static String generateHashForString(String value){

       return  "HASH" + value
               .toUpperCase()
               .replaceAll("[^a-zA-Z0-9]+", "")
               .trim()
               .hashCode();
    }
}
