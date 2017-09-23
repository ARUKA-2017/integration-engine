package com.akura.parser.config;

public class Config {

    public static String defaultRelationship = "isPartOf";
    public static String entities = "entities";
    public static String relationships = "relationships";
    public static String arrayListClass = "java.util.ArrayList";
    public static String objectClass = "com.google.gson.internal.LinkedTreeMap";
    public static String stringClass = "java.lang.String";

    public static String RDF_STRING="http://www.w3.org/2001/XMLSchema#string";
    public static final String ONTOLOGY_URI = "urn:absolute:www.akura.com/class#C-";
    public static final String ONTOLOGY_PROP_URI = "urn:absolute:www.akura.com/property#HAS-";

    //todo: give seperate references to literal objects with same key name?
}
