import ai.grakn.Grakn;
import ai.grakn.GraknGraph;
import ai.grakn.graql.MatchQuery;
import ai.grakn.graql.QueryBuilder;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by szymon.klarman on 08/02/2017.
 */
public class GraknBenchmark {

        static String keyspace = "grakn";
        static GraknGraph graknGraph = Grakn.factory(Grakn.DEFAULT_URI, keyspace).getGraph();
        static String queryString;
        static MatchQuery testQuery;

        public static void reasoningTest() {

            QueryBuilder qb = graknGraph.graql().infer(true);

            for (int counter = 2000; counter < 10000; counter = counter + 2000) {

                //queryString = "match $x isa relation1; limit " + counter + ";";
                //queryString = "match $x isa relation1;";

                //queryString = "match ('rel-from':$x, 'rel-to':$y) isa relation1; limit " + counter + ";";
                queryString = "match ('rel-from':$x, 'rel-to':$y) isa relation1;";

                testQuery = qb.parse(queryString);

                Instant start = Instant.now();

                testQuery.execute();

                Instant end = Instant.now();
                System.out.println("Query: " + queryString + " evaluated in " + Duration.between(start, end));
            }
        }

        public static void queryingTest() {

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
        }

    public static void queryingTestGrid() {

        QueryBuilder qb2 = graknGraph.graql().infer(false);

        queryString = "match ";

        for (int counter = 1; counter < 100; counter++) {
                int next = counter+1;

                String first = "$x_"+counter+"_"+counter;
                String second = "$x_"+next+"_"+counter;
                String third = "$x_"+counter+"_"+next;
                String fourth = "$x_"+next+"_"+next;

                queryString = queryString +
                        "('rel-from':" + first + ", 'rel-to':" + second + ") isa horizontal; " +
                        "('rel-from':" + first + ", 'rel-to':" + third + ") isa vertical;" +
                        "('rel-from':" + second + ", 'rel-to':" + fourth + ") isa vertical;" +
                        "('rel-from':" + third + ", 'rel-to':" + fourth + ") isa horizontal;";

                testQuery = qb2.parse(queryString);

                Instant start = Instant.now();

                testQuery.execute();

                Instant end = Instant.now();
                System.out.println("Query: " + counter + " evaluated in " + Duration.between(start, end));
            }
        }
    }



