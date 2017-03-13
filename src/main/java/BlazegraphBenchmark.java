//import com.bigdata.rdf.sail.BigdataSailRepository;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.blazegraph.gremlin.embedded.BasicRepositoryProvider;
import com.blazegraph.gremlin.embedded.BlazeGraphEmbedded;
import com.blazegraph.gremlin.embedded.BlazeGraphFactory;
import com.blazegraph.gremlin.structure.BlazeVertex;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;


/**
 * Created by szymon.klarman on 10/03/2017.
 */
public class BlazegraphBenchmark {

    public static void test() {

        File file = new File("/Users/szymon.klarman/Desktop/blazegraph.jnl");
        String journal = file.getAbsolutePath();


        BlazeGraphEmbedded graph = BlazeGraphFactory.open(journal);

      //          graph.vertex("type1");

        System.out.println(graph.edgeCount());

     //   final String sparql =
      //          "select * where {?x ?y ?z} limit 10";
/*
 * Run the query, auto-translate RDF values to PG values.
 */
     //   graph.select(sparql).collect().forEach(bin -> System.out.println(bin.toString()));
//        graph.ask("g.V().label().groupCount()");
        Instant start = Instant.now();
        System.out.println(graph.traversal().V("type1").inE().count().next().toString());
        Instant end = Instant.now();
        System.out.println("Query time " + Duration.between(start, end));
        graph.close();

    }


    public static void load() {
     //   Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
     //   logger.setLevel(Level.OFF);
        File file = new File("/Users/szymon.klarman/Desktop/blazegraph.jnl");
        String journal = file.getAbsolutePath();


        BlazeGraphEmbedded graph = BlazeGraphFactory.open(journal);

        String entity1, entity2, entity3;
        Vertex v1, v2, v3;
        int top = 1000000;

        graph.setBulkLoad(true);

        Vertex type1 = graph.addVertex(T.id, "type1");
        Vertex type2 = graph.addVertex(T.id, "type2");
        Instant start = Instant.now();

        for (int count = 1; count <= top; count++) {
            if (count % 1000 ==0){
                System.out.println(count);
            }
            for (int level1 = 1; level1 <= 2; level1++) {
                entity1 = "entity_" + count + "_" + level1;
                v1 = graph.addVertex(T.id, entity1);
                if (level1==1) v1.addEdge("isa", type1);
                if (level1==2) v1.addEdge("isa", type2);

                for (int level2 = 1; level2 <= 2; level2++) {
                    entity2 = "entity_" + count + "_" + level1 + "_" + level2;
                    v2 = graph.addVertex(T.id, entity2);
                    if (level2==1) v2.addEdge("isa", type1);
                    if (level2==2) v2.addEdge("isa", type2);
                    v1.addEdge("relation", v2);
                    for (int level3 = 1; level3 <= 2; level3++) {
                        entity3 = "entity_" + count + "_" + level1 + "_" + level2+ "_" + level3;
                        v3 = graph.addVertex(T.id, entity3);
                        if (level3==1) v3.addEdge("isa", type1);
                        if (level3==2) v3.addEdge("isa", type2);
                        v2.addEdge("relation", v3);
                    }
                }
            }
            if (count % 100000 ==0){
                graph.tx().commit();
            }
        }

        graph.setBulkLoad(false);
        Instant end = Instant.now();
        System.out.println("Loading time " + Duration.between(start, end));
        System.out.println(graph.edgeCount());


        graph.close();

    }
}
