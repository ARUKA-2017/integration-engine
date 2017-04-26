package com.akura.integration;

import com.akura.integration.utility.FileManager;
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
        FileManager fileManager = new FileManager();

        // read   ontology
        readOntology(fileManager.getFilePath("ontology/pizza.owl"), model);

        // start traverse
        traverseStart( model, null );
    }
}