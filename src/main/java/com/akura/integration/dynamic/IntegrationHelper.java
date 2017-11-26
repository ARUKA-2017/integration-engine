package com.akura.integration.dynamic;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class representing an IntegrationHelper.
 */
public class IntegrationHelper {

    /**
     * Method used to search a property.
     *
     * @param propList - list of properties.
     * @param name     - name of the property.
     * @return - Property.
     */
    public static Property searchProperty(ArrayList<Property> propList, String name) {

        Iterator<Property> iterator = propList.iterator();

        Property prop = null;
        while (iterator.hasNext()) {
            Property temp = iterator.next();

            if (temp.getLocalName().equals(name)) {
                prop = temp;
                break;
            }
        }

        return prop;
    }

    /**
     * Method used to list properties to an array list.
     *
     * @param instance - individual instance.
     * @return - list of properties.
     */
    public static ArrayList<Property> listPropertiesToArrayList(Individual instance) {
        ArrayList<Property> properties = new ArrayList<>();
        StmtIterator iter = instance.listProperties();

        while (iter.hasNext()) {
            Property prop = iter.next().getPredicate();
            if (prop.hasURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                continue;
            }

            properties.add(prop);
        }
        return properties;
    }

    /**
     * Method used get the literal property value.
     *
     * @param propList - list of property.
     * @param name     - name of the relevant property.
     * @param instance - individual instance.
     * @return - string value.
     * @throws Exception - exception.
     */
    public static String getLiteralPropertyValue(ArrayList<Property> propList, String name, Individual instance) throws Exception {
        String value = null;
        Property prop = searchProperty(propList, name);

        if (prop == null) {

            throw new Exception("Property Null " + name);
        }

        return instance.getProperty(prop).getLiteral().toString();
    }

    /**
     * Method used to get the float literal property values.
     *
     * @param propList - list of properties.
     * @param name     - name of the property.
     * @param instance - individual instance.
     * @return - float value.
     * @throws Exception - exception.
     */
    public static Float getLiteralPropertyValueFloat(ArrayList<Property> propList, String name, Individual instance) throws Exception {
        String value = null;
        Property prop = searchProperty(propList, name);

        if (prop == null) {
            throw new Exception("Property Null " + name);
        }

        return instance.getProperty(prop).getLiteral().getFloat();
    }

    /**
     * Method used to get the object property value.
     *
     * @param dynamic  - dynamic ontology.
     * @param propList - list of properties.
     * @param name     - name of the property.
     * @param instance - individual instance.
     * @return - Individual.
     * @throws Exception - exception.
     */
    public static Individual getObjectPropertyValue(OntModel dynamic, ArrayList<Property> propList, String name, Individual instance) throws Exception {

        Property prop = searchProperty(propList, name);

        if (prop == null) {

            throw new Exception("Object Property Null " + name);
        }

        ObjectProperty objProp = dynamic.getObjectProperty(prop.toString());
        StmtIterator iter = instance.listProperties(objProp);
        if (iter.hasNext()) {

            return dynamic
                    .getIndividual(iter.next().getResource().toString());

        } else {
            throw new Exception("No Property Instances " + name);
        }
    }

    /**
     * Method used to get the object property list.
     *
     * @param dynamic  - dynamic ontology.
     * @param propList - list properties.
     * @param name     - name of the entity.
     * @param instance - individual instance.
     * @return - list of individuals.
     * @throws Exception - exception.
     */
    public static ArrayList<Individual> getObjectPropertyList(OntModel dynamic, ArrayList<Property> propList, String name, Individual instance) throws Exception {

        Property prop = searchProperty(propList, name);

        ArrayList<Individual> indList = new ArrayList<>();

        if (prop == null) {
            return indList;
        }

        ObjectProperty objProp = dynamic.getObjectProperty(prop.toString());
        StmtIterator iter = instance.listProperties(objProp);
        while (iter.hasNext()) {

            indList.add(dynamic
                    .getIndividual(iter.next().getResource().toString()));

        }

        return indList;
    }

    /**
     * Method used to get the property by URI.
     *
     * @param dynamic - dynamic ontology.
     * @param name    - name of the property.
     * @return - string value.
     */
    public static String getPropertyURI(OntModel dynamic, String name) {

        String uri = "";
        ExtendedIterator<ObjectProperty> dynIter = dynamic.listObjectProperties();
        while (dynIter.hasNext()) {
            ObjectProperty prop = dynIter.next();
            if (prop.getLocalName().equals(name)) {
                uri = prop.getURI().toString();

            }
        }

        return uri;
    }
}
