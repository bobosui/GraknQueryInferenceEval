import java.time.Duration;
import java.time.Instant;

import ai.grakn.Grakn;
import ai.grakn.GraknGraph;
import ai.grakn.graql.MatchQuery;
import ai.grakn.graql.QueryBuilder;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.query.* ;

/**
 * Created by szymon.klarman on 07/02/2017.
 */
public class Main {
    static String keyspace = "grakn";
    public static GraknGraph graknGraph;

    public static void main(String[] args) {
        Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.OFF);

        /*
        graknGraph = Grakn.factory(Grakn.DEFAULT_URI, keyspace).getGraph();

        MatchQuery testQuery;
        QueryBuilder qb = graknGraph.graql().infer(true);
        String queryString;


        for (int counter = 2000; counter < 10000; counter = counter + 2000) {

          //  queryString = "match $x isa relation1; limit " + counter + ";";

            queryString = "match $x isa relation1;";

            testQuery = qb.parse(queryString);

            Instant start = Instant.now();

            testQuery.execute();

            Instant end = Instant.now();
            System.out.println("Query: " + queryString + " evaluated in " + Duration.between(start, end));
        }
*/
        /*
        QueryBuilder qb2 = graknGraph.graql().infer(false);

        queryString = "match ";

        for (int counter = 1; counter < 100; counter++) {
            int first = counter;
            int second = counter+1;
            queryString = queryString + "('rel-from':$x"+ first +", 'rel-to':$x"+ second +") isa relation1; ";

            testQuery = qb2.parse(queryString);

            Instant start = Instant.now();

            testQuery.execute();

            Instant end = Instant.now();
            System.out.println("Query: " + first + " evaluated in " + Duration.between(start, end));
        }
        */

        Model model = RDFDataMgr.loadModel("transitive-test.rdf");

        /*
        String patternString = "";
        String queryString = "";

        queryString = "select (count(?x) as ?n) where {?x <grakn:relation>+ ?y}";

        Query query = QueryFactory.create(queryString);
        Instant start = Instant.now();
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
           //  for ( ; results.hasNext() ; ) {
           //      QuerySolution soln = results.nextSolution();
           //      System.out.println(soln.toString());
           //  }
        }
        Instant end = Instant.now();
        System.out.println("Sparql query " + queryString + " evaluated in " + Duration.between(start, end));
*/
        String rules = "[rule1: (?a <grakn:relation> ?b) (?b <grakn:relation> ?c) -> (?a <grakn:relation> ?c)]";
        Instant start = Instant.now();
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        InfModel inf = ModelFactory.createInfModel(reasoner, model);

        String queryString = "select (count(?x) as ?n) where {?x <grakn:relation> ?y}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, inf)) {
            ResultSet results = qexec.execSelect();
              for ( ; results.hasNext() ; ) {
                  QuerySolution soln = results.nextSolution();
                  System.out.println(soln.toString());
              }
        }
        Instant end = Instant.now();
        System.out.println("Sparql query " + queryString + " evaluated in " + Duration.between(start, end));

        /*
        for (int counter = 1; counter < 100; counter++) {
            int first = counter;
            int second = counter + 1;
            patternString = patternString +
                    "?x" + first + " <grakn:relation> ?x" + second + " . ";
            queryString = " select (count(?x1) as ?n) where { " + patternString + "}";
            Query query = QueryFactory.create(queryString);
            Instant start = Instant.now();
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
               // for ( ; results.hasNext() ; ) {
               //     QuerySolution soln = results.nextSolution();
               //     System.out.println(soln.toString());
               // }
            }
            Instant end = Instant.now();
            System.out.println("Sparql query " + counter + " evaluated in " + Duration.between(start, end));
        }
        */
    }
}