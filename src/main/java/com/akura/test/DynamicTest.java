package com.akura.test;


import com.akura.config.Config;
import com.akura.integration.service.IntegrateService;
import com.akura.utility.OntologyReader;

/**
 * Class used to test the dynamic ontology.
 */
public class DynamicTest {

    public static void main(String[] args) {
        IntegrateService service = new IntegrateService(OntologyReader.getOntologyModel(Config.DYNAMIC_FILENAME));
        service.integrate();
    }
}
