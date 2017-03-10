import java.io.FileInputStream;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by szymon.klarman on 09/03/2017.
 */
public class RemoteSparqlBenchmark {

    public static void test() {


        String patternString = "";
        String queryString = "";

        //String sparqlEndpoint = "http://localhost:5820/myDB/query";
        //String sparqlEndpoint = "http://localhost:8890/sparql";
        String sparqlEndpoint = "http://10.0.1.12:9999/sparql";

        HttpAuthenticator authenticator = new SimpleAuthenticator("admin", "admin".toCharArray());

        for (int counter = 1; counter < 45; counter++) {
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

          //  Query query = QueryFactory.create(queryString);
           System.out.println(queryString);

            Instant start = Instant.now();
          //  try (QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryString)) {
           //     System.out.println(qexec.toString());
                //  if (counter>20) qexec.execSelect();
                // ResultSet results = qexec.execSelect();

               /* for (; results.hasNext(); ) {
                    QuerySolution soln = results.nextSolution();
                    //System.out.println(soln.toString());
                } */
            //}
            //catch (Exception e) {}
            try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost:9999")
                    .setPath("/blazegraph/sparql")
                    .setParameter("query", queryString)
                    .build();
            HttpPost httpget = new HttpPost(uri);
           // System.out.println(httpget.getURI());
            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpget);

            System.out.println(httpget);
                            HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println(EntityUtils.toString(entity));
                }
                response.close();
            } catch (Exception e) {System.out.println(e);}

            Instant end = Instant.now();
            System.out.println("Sparql query " + counter + " evaluated in " + Duration.between(start, end));
        }
    }

    public static void sendGet() throws Exception {

        String url = "http://localhost:9999/blazegraph/sparql";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("query:", "select (count(?x_1_1) as ?n) where { ?x_1_1 <grakn:horizontal> ?x_2_1 .}");
        // System.out.println(con.getHeaderField("query:"));
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());


    }


    public static void urlGet()  {



    }

}
