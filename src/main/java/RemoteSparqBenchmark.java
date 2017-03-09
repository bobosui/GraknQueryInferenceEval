import java.io.FileInputStream;
import java.time.Duration;
import java.time.Instant;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;

import org.apache.jena.atlas.web.auth.HttpAuthenticator;
import org.apache.jena.atlas.web.auth.SimpleAuthenticator;

/**
 * Created by szymon.klarman on 09/03/2017.
 */
public class StardogBenchmark {

    public static void test() {


        String patternString = "";
        String queryString = "";

        //String sparqlEndpoint = "http://localhost:5820/myDB/query";
        //String sparqlEndpoint = "http://localhost:8890/sparql";
        String sparqlEndpoint = "http://10.0.1.12:9999/sparql";

        HttpAuthenticator authenticator = new SimpleAuthenticator("admin", "admin".toCharArray());

        for (int counter = 1; counter < 100; counter++) {
            int next = counter + 1;

            String first = "?x_" + counter + "_" + counter;
            String second = "?x_" + next + "_" + counter;
            String third = "?x_" + counter + "_" + next;
            String fourth = "?x_" + next + "_" + next;

            patternString = patternString +
                    first + " <grakn:horizontal> " + second + " . " +
                    first + " <grakn:vertical> " + third + " . " +
                    second + " <grakn:vertical> " + fourth + " . " +
                    third + " <grakn:horizontal> " + fourth + " . ";
            queryString = " select (count(?x_1_1) as ?n) where { " + patternString + "}";
            //queryString = " select (count(?x_1_1) as ?n) where {  graph <grid-dataset> {" + patternString + "}}";

            Query query = QueryFactory.create(queryString);
            //System.out.println(queryString);

            Instant start = Instant.now();
            try (QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryString)) {
                ResultSet results = qexec.execSelect();
                for (; results.hasNext(); ) {
                    QuerySolution soln = results.nextSolution();
                    //System.out.println(soln.toString());
                }
            }
            Instant end = Instant.now();
            System.out.println("Sparql query " + counter + " evaluated in " + Duration.between(start, end));
            }
    }
}
