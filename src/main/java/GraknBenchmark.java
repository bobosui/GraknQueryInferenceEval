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

                //queryString = "match $x isa horizontal; limit " + counter + ";";
                //queryString = "match $x isa horizontal;";

                //queryString = "match ('rel-from':$x, 'rel-to':$y) isa horizontal; limit " + counter + ";";
                queryString = "match ('rel-from':$x, 'rel-to':$y) isa horizontal;";

                testQuery = qb.parse(queryString);

                Instant start = Instant.now();

                testQuery.execute();

                Instant end = Instant.now();
                System.out.println("Query: " + queryString + " evaluated in " + Duration.between(start, end));
            }
        }

    public static void reasoningTestGrid() {

        QueryBuilder qb = graknGraph.graql().infer(true);

        for (int counter = 100; counter < 10000; counter = counter + 100) {

            //queryString = "match $x isa diagonal; limit " + counter + ";";
            //queryString = "match $x isa diagonal;";

            queryString = "match ('rel-from':$x, 'rel-to':$y) isa diagonal; limit " + counter + ";";
            //queryString = "match ('rel-from':$x, 'rel-to':$y) isa diagonal;";

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
                queryString = queryString + "('rel-from':$x"+ first +", 'rel-to':$x"+ second +") isa horizontal; ";

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

        for (int counter = 1; counter < 99; counter++) {
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

                System.out.println(queryString);
                Instant start = Instant.now();

                testQuery.execute();

                Instant end = Instant.now();
                System.out.println("Query: " + counter + " evaluated in " + Duration.between(start, end));
            }
        }

    public static void queryingTypes() {

        QueryBuilder qb2 = graknGraph.graql().infer(false);

        queryString = "match ";

        for (int counter = 1; counter < 200; counter++) {

            queryString = queryString + "$x"+counter + " type-name horizontal; " + "$y"+counter + " type-name vertical; " + "$z"+counter + " type-name role-to; ";

            testQuery = qb2.parse(queryString);

            System.out.println(queryString);
            Instant start = Instant.now();

            testQuery.execute();

            Instant end = Instant.now();
            System.out.println("Query: " + counter + " evaluated in " + Duration.between(start, end));
        }
    }
    }



