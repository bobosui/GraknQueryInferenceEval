import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by szymon.klarman on 08/02/2017.
 */
public class JenaBenchmark {

    static Dataset dataset = TDBFactory.createDataset("tdb");
    static Model model = dataset.getNamedModel("http://grid-test");
    // static Model model = RDFDataMgr.loadModel("grid-dataset.nt");
    static String patternString;
    static String queryString;


    public static void reasoningTest() {

        String rules = "[rule1: (?a <grakn:horizontal> ?b) (?b <grakn:horizontal> ?c) -> (?a <grakn:horizontal> ?c)]";
        Instant start = Instant.now();
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        InfModel inf = ModelFactory.createInfModel(reasoner, model);

        String queryString = "select (count(?x) as ?n) where {?x <grakn:horizontal> ?y}";

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

    }

    public static void reasoningTestGrid() {

        String rules =
                "[rule1: (?a <grakn:horizontal> ?b) (?b <grakn:horizontal> ?c) -> (?a <grakn:horizontal> ?c)] " +
                "[rule2: (?a <grakn:vertical> ?b) (?b <grakn:vertical> ?c) -> (?a <grakn:vertical> ?c)] " +
                "[rule3: (?a <grakn:horizontal> ?b) (?b <grakn:vertical> ?c) -> (?a <grakn:diagonal> ?c)]";
        Instant start = Instant.now();
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        InfModel inf = ModelFactory.createInfModel(reasoner, model);

        String queryString = "select (count(?x) as ?n) where {?x <grakn:diagonal> ?y}";

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

    }

    public static void queryingTest() {

        patternString = "";

        for (int counter = 1; counter < 100; counter++) {
            int first = counter;
            int second = counter + 1;
            patternString = patternString +
                    "?x" + first + " <grakn:horizontal> ?x" + second + " . ";
            //patternString = patternString +
            //                "?rel <grakn:from> ?x" + first + " . " +
            //                "?rel <grakn:to> ?x" + second + " . ";
            queryString = " select (count(?x1) as ?n) where { " + patternString + "}";
            Query query = QueryFactory.create(queryString);
            Instant start = Instant.now();
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                 for ( ; results.hasNext() ; ) {
                     QuerySolution soln = results.nextSolution();
                //     System.out.println(soln.toString());
                 }
            }
            Instant end = Instant.now();
            System.out.println("Sparql query " + counter + " evaluated in " + Duration.between(start, end));
        }
    }

    public static void queryingTestGrid() {
        //TDBLoader.loadModel(model, "grid-dataset.nt");
        patternString = "";

        for (int counter = 1; counter < 100; counter++) {
            int next = counter+1;

            String first = "?x_"+counter+"_"+counter;
            String second = "?x_"+next+"_"+counter;
            String third = "?x_"+counter+"_"+next;
            String fourth = "?x_"+next+"_"+next;

            patternString = patternString +
                    first + " <grakn:horizontal> " + second + " . " +
                    first + " <grakn:vertical> " + third + " . " +
                    second + " <grakn:vertical> " + fourth + " . " +
                    third + " <grakn:horizontal> " + fourth + " . ";
            queryString = " select (count(?x_1_1) as ?n) where { " + patternString + "}";
            Query query = QueryFactory.create(queryString);
            Instant start = Instant.now();
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                 for ( ; results.hasNext() ; ) {
                     QuerySolution soln = results.nextSolution();
                     //System.out.println(soln.toString());
                 }
            }
            Instant end = Instant.now();
            System.out.println("Sparql query " + counter + " evaluated in " + Duration.between(start, end));
        }
    }

    public static void transitiveQueryingTest() {

        queryString = "select (count(?x) as ?n) where {?x <grakn:horizontal>+ ?y}";

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

    }

    public static void transitiveQueryingTestGrid() {

        queryString = "select (count(?x) as ?n) where {?x (<grakn:horizontal>/<grakn:horizontal>/<grakn:vertical>/<grakn:vertical>) ?y}";

        Query query = QueryFactory.create(queryString);
        Instant start = Instant.now();
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
              for ( ; results.hasNext() ; ) {
                  QuerySolution soln = results.nextSolution();
                  System.out.println(soln.toString());
              }
        }
        Instant end = Instant.now();
        System.out.println("Sparql query " + queryString + " evaluated in " + Duration.between(start, end));

    }
}
