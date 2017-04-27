package com.akura.integration;

import com.akura.integration.utility.FileResourceManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import static com.akura.integration.support.OntologyTraverserAPI.readOntology;
import static com.akura.integration.support.OntologyTraverserAPI.traverseStart;


public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Ontology Traversal Using Jena API." );
        OntModel model = ModelFactory.createOntologyModel();

        // Insatantiate custom file manager
        FileResourceManager fileResourceManager = new FileResourceManager();

        // read   ontology
        readOntology(fileResourceManager.getFilePath("ontology/pizza.owl"), model);

        // start traverse
        traverseStart( model, null );
    }
}