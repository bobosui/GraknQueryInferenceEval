//import com.bigdata.rdf.sail.BigdataSailRepository;
import com.blazegraph.gremlin.embedded.BasicRepositoryProvider;
import com.blazegraph.gremlin.embedded.BlazeGraphEmbedded;
import com.blazegraph.gremlin.embedded.BlazeGraphFactory;
import com.blazegraph.gremlin.structure.BlazeVertex;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.File;
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

        final String sparql =
                "select * where {?x ?y ?z} limit 10";

/*
 * Run the query, auto-translate RDF values to PG values.
 */
        graph.select(sparql).collect().forEach(bin -> System.out.println(bin.toString()));
        graph.ask("g.V().label().groupCount()");

        graph.compute();

        graph.close();

    }


    public static void load() {

        File file = new File("/Users/szymon.klarman/Desktop/blazegraph.jnl");
        String journal = file.getAbsolutePath();


        BlazeGraphEmbedded graph = BlazeGraphFactory.open(journal);

        String entity1, entity2, entity3;
        Vertex v1, v2, v3;
        int top = 10;

        graph.setBulkLoad(true);

        Vertex type1 = graph.addVertex(T.id, "type1");
        Vertex type2 = graph.addVertex(T.id, "type2");

        for (int count = 1; count <= top; count++) {
            System.out.println(count);
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
        }

        graph.setBulkLoad(false);
        graph.tx().commit();
        System.out.println(graph.edgeCount());


        graph.close();

    }
}
