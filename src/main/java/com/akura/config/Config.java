package com.akura.config;

/**
 * Class containing the app configurations.
 */
public class Config {

    public static final String OWL_FILENAME = "rev_engine_base_ontology.owl";
    public static final String OWL_DYNAMIC_EMPTY_FILENAME = "rev_engine_dynamic_empty_ontology.owl";

    //TODO: rmeove this later. For testing purposes only
    public static final String DYNAMIC_FILENAME = "rev_engine_dynamic_ontology.owl";

    public static final String URI_NAMESPACE = "http://www.akura.com#";

    public static final String ENTITY = URI_NAMESPACE + "Entity";
    public static final String REVIEW = URI_NAMESPACE + "Review";
    public static final String REVIEWER = URI_NAMESPACE + "Reviewer";
    public static final String PROPERTY = URI_NAMESPACE + "Property";
    public static final String COMPARISON = URI_NAMESPACE + "Comparison";
    public static final String BASESCORE = URI_NAMESPACE + "BaseScore";
    public static final String FEATURE = URI_NAMESPACE + "Feature";

    public static final double STRING_SIMILARITY_THRESHOLD = 0.5;

    public static final String DYNAMIC_ONTOLOGY_URI = "urn:absolute:www.akura.com#";
}
