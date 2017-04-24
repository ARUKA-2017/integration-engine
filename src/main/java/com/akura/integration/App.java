package com.akura.integration;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import static com.akura.integration.support.OntologyTraverserAPI.readOntology;
import static com.akura.integration.support.OntologyTraverserAPI.traverseStart;


public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        OntModel model = ModelFactory.createOntologyModel();
        // read   ontology
        readOntology( "src/main/java/resources/ontology/rev-engine.owl", model );
        // start traverse
        traverseStart( model, null );
    }
}