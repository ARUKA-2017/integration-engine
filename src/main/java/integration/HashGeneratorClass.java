package integration;


public class HashGeneratorClass {

    public static Integer generateHashForString(String value){

       return value
               .toUpperCase()
               .replaceAll("[^a-zA-Z0-9]+", "")
               .trim()
               .hashCode();

    }
}
