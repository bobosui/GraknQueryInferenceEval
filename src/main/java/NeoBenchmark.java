import org.neo4j.driver.v1.*;

import java.time.Duration;
import java.time.Instant;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by szymon.klarman on 08/02/2017.
 */
public class NeoBenchmark {

    static Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "Simco1!" ) );
    static Session session = driver.session();


    public static void queryingTest() {

        String patternString = "";
        String queryString;

        for (int counter = 1; counter < 100; counter++) {
            int first = counter;
            int second = counter + 1;
            if (1<counter)  patternString = patternString +", ";
            patternString = patternString +
                    "(n" + first + ")-[:HORIZONTAL]->(n" + second + ") ";

            queryString = " MATCH " + patternString + " RETURN n1";

            Instant start = Instant.now();

            StatementResult result = session.run( queryString );

            result.list();

            Instant end = Instant.now();
            System.out.println("Query: " + counter + " evaluated in " + Duration.between(start, end));



        }

        session.close();
        driver.close();
    }

    public static void queryingTestGrid() {

        String patternString = "";
        String queryString;

        for (int counter = 1; counter < 100; counter++) {
            int next = counter+1;

            String first = "(n_"+counter+"_"+counter+")";
            String second = "(n_"+next+"_"+counter+")";
            String third = "(n_"+counter+"_"+next+")";
            String fourth = "(n_"+next+"_"+next+")";

            if (1<counter)  patternString = patternString +", ";

            patternString = patternString +
                    first+"-[:HORIZONTAL]->"+second + ", "+
                    first+"-[:VERTICAL]->"+third + ", "+
                    second+"-[:VERTICAL]->"+fourth + ", "+
                    third+"-[:HORIZONTAL]->"+fourth;

            queryString = " MATCH " + patternString + " RETURN n_1_1";

            Instant start = Instant.now();

            StatementResult result = session.run( queryString );

            result.list();

            Instant end = Instant.now();
            System.out.println("Query: " + counter + " evaluated in " + Duration.between(start, end));



        }

        session.close();
        driver.close();
    }

    public static void transitiveQueryingTest() {

        String queryString = " MATCH (n1)-[:HORIZONTAL*]->(n2) RETURN n1 ";

        Instant start = Instant.now();

        StatementResult result = session.run( queryString );

        result.list();

        Instant end = Instant.now();
        System.out.println("Transitive query evaluated in " + Duration.between(start, end));

        session.close();
        driver.close();
    }
}
