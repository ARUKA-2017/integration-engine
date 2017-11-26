package com.akura.retrieval.response;

import com.akura.retrieval.models.Comparison;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;

import java.util.ArrayList;

/**
 * Class representing a ComparisonResponse.
 */
public class ComparisonResponse {

    public ComparisonEntityInstance[] betterThan;
    public ComparisonEntityInstance[] worseThan;


    public ComparisonResponse(OntModel m, String hash) {
        this.setBetterThan(m, hash);
        this.setWorseThan(m, hash);
    }

    /**
     * Method used to set the better than entities.
     *
     * @param m    - Ontology model.
     * @param hash - hash id of the entity.
     */
    public void setBetterThan(OntModel m, String hash) {
        ParameterizedSparqlString ps = new ParameterizedSparqlString();
        ps.setCommandText(
                "PREFIX akura: <http://www.akura.com#>\n" +
                        "SELECT ?comparison \n" +
                        "WHERE { " +
                        "?comparison akura:Good ?y ." +
                        "?y  akura:HashID  ?hash ." +
                        "}"
        );
        ps.setLiteral("hash", hash);
        Query query = QueryFactory.create(ps.toString());
        QueryExecution queryExecution = QueryExecutionFactory.create(query, m);

        ArrayList<ComparisonEntityInstance> compEntityList = new ArrayList<>();

        try {

            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                Individual compInstance = m.getIndividual(solution.get("comparison").toString());

                Comparison comp = new Comparison(m, compInstance);

                compEntityList.add(comp.getBadInstance());

            }
        } finally {

            queryExecution.close();
            this.betterThan = compEntityList.toArray(new ComparisonEntityInstance[compEntityList.size()]);
        }
    }

    /**
     * Method used to set the worse than entities.
     *
     * @param m    - Ontology model.
     * @param hash - hash id of the entity.
     */
    public void setWorseThan(OntModel m, String hash) {
        ParameterizedSparqlString ps = new ParameterizedSparqlString();
        ps.setCommandText(
                "PREFIX akura: <http://www.akura.com#>\n" +
                        "SELECT ?comparison \n" +
                        "WHERE { " +
                        "?comparison akura:Bad ?y ." +
                        "?y  akura:HashID  ?hash ." +
                        "}"
        );
        ps.setLiteral("hash", hash);
        Query query = QueryFactory.create(ps.toString());
        QueryExecution queryExecution = QueryExecutionFactory.create(query, m);

        ArrayList<ComparisonEntityInstance> compEntityList = new ArrayList<>();

        try {

            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                Individual compInstance = m.getIndividual(solution.get("comparison").toString());

                Comparison comp = new Comparison(m, compInstance);

                compEntityList.add(comp.getGoodInstance());

            }
        } finally {

            queryExecution.close();
            this.worseThan = compEntityList.toArray(new ComparisonEntityInstance[compEntityList.size()]);
        }
    }
}
