import ai.grakn.concept.ConceptId;
import ai.grakn.concept.Relation;
import ai.grakn.exception.GraknValidationException;
import ai.grakn.graql.InsertQuery;
import ai.grakn.graql.QueryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static ai.grakn.graql.Graql.var;

/**
 * Created by szymon.klarman on 08/02/2017.
 */
public class DataGenerator {

    static int sizeX = 100;
    static int sizeY = 100;

    public static void generateRdf() throws FileNotFoundException {

        String entity1, entity2, entity3;
        int right, bottom;

        File outputFile = new File("grid-dataset.nt");
        PrintWriter out_writer = new PrintWriter(outputFile);

        for (int x = 1; x <= sizeX; x++) {
            for (int y = 1; y <= sizeY; y++) {
                right = x + 1;
                bottom = y + 1;
                entity1 = "<grakn:entity_" + x + "_" + y + ">";
                entity2 = "<grakn:entity_" + right + "_" + y + ">";
                entity3 = "<grakn:entity_" + x + "_" + bottom + ">";
                out_writer.println(entity1 + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <grakn:entity> .");
                if (x < sizeX) out_writer.println(entity1 + " <grakn:horizontal> " + entity2 + " .");
                if (y < sizeY) out_writer.println(entity1 + " <grakn:vertical> " + entity3 + " .");
            }
        }

        out_writer.flush();
        out_writer.close();
    }


    public static void generateRdfSupernode() throws FileNotFoundException {

        String entity1, entity2, entity3;
        int top = 1000000;


        File outputFile = new File("supernode-dataset.nt");
        PrintWriter out_writer = new PrintWriter(outputFile);

        for (int count = 1; count <= top; count++) {
            for (int level1 = 1; level1 <= 2; level1++) {
                entity1 = "<grakn:entity_" + count + "_" + level1 + ">";
                out_writer.println(entity1 + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <grakn:type_" + level1 + "> .");
                for (int level2 = 1; level2 <= 2; level2++) {
                    entity2 = "<grakn:entity_" + count + "_" + level1 + "_" + level2 + ">";
                    out_writer.println(entity2 + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <grakn:type_" + level2 + "> .");
                    out_writer.println(entity1 + " <grakn:relation> " + entity2 + " .");
                    for (int level3 = 1; level3 <= 2; level3++) {
                        entity3 = "<grakn:entity_" + count + "_" + level1 + "_" + level2 + "_" + level3 + ">";
                        out_writer.println(entity3 + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <grakn:type_" + level3 + "> .");
                        out_writer.println(entity2 + " <grakn:relation> " + entity3 + " .");
                    }
                }
            }
        }
        out_writer.flush();
        out_writer.close();
    }

    public static void generateGraqlCsv() throws FileNotFoundException {
        String entity1, entity2, entity3;
        int right, bottom;

        File outputFile1 = new File("grid-entities.csv");
        File outputFile2 = new File("grid-relations.csv");
        PrintWriter out_writer1 = new PrintWriter(outputFile1);
        PrintWriter out_writer2 = new PrintWriter(outputFile2);

        out_writer1.println("name");
        out_writer2.println("name1,name2,relation");

        for (int x = 1; x <= sizeX; x++) {
            for (int y = 1; y <= sizeY; y++) {
                right = x + 1;
                bottom = y + 1;
                entity1 = "entity_" + x + "_" + y;
                entity2 = "entity_" + right + "_" + y;
                entity3 = "entity_" + x + "_" + bottom;
                out_writer1.println(entity1);
                if (x < sizeX) out_writer2.println(entity1 + "," + entity2 + ",horizontal");
                if (y < sizeY) out_writer2.println(entity1 + "," + entity3 + ",vertical");
            }
        }

        out_writer1.flush();
        out_writer1.close();
        out_writer2.flush();
        out_writer2.close();
    }

    public static void generateDataWithGraknCore() {
        Relation[][] grid = new Relation[sizeX+1][sizeY+1];

        QueryBuilder qb = GraknBenchmark.graknGraph.graql().infer(false);
        InsertQuery startRel = qb.insert(var("x").isa("relation1").
                rel("horizontal", var("x")).
                rel("vertical", var("x")));
        grid[sizeX][sizeY] = startRel.iterator().next().get("x").asRelation();
        try {
          //  GraknBenchmark.graknGraph.commit();
        } catch (GraknValidationException e) {
            e.printStackTrace();
        }

        for (int x = sizeX; x >= 1; x--) {
            for (int y = sizeY; y >= 1; y--) {

                if (x < sizeX && y < sizeY) {
                    ConceptId hor = grid[x + 1][y].asRelation().getId();
                    ConceptId ver = grid[x][y + 1].asRelation().getId();
                    InsertQuery nextRel = qb.match(var("hor").id(hor), var("ver").id(ver)).
                            insert(var("rel").isa("relation1").rel("horizontal", var("hor")).rel("vertical", var("ver")));
                    grid[x][y] = nextRel.iterator().next().get("rel").asRelation();
                }

                if (x < sizeX && y == sizeY) {
                    ConceptId hor = grid[x + 1][y].asRelation().getId();
                    InsertQuery nextRel = qb.match(var("hor").id(hor)).
                            insert(var("rel").isa("relation1").rel("horizontal", var("hor")));
                    grid[x][y] = nextRel.iterator().next().get("rel").asRelation();
                }

                if (x == sizeX && y < sizeY) {
                    ConceptId ver = grid[x][y + 1].asRelation().getId();
                    InsertQuery nextRel = qb.match(var("ver").id(ver)).
                            insert(var("rel").isa("relation1").rel("vertical", var("ver")));
                    grid[x][y] = nextRel.iterator().next().get("rel").asRelation();
                }
                try {
                   // GraknBenchmark.graknGraph.commit();
                } catch (GraknValidationException e) {
                    e.printStackTrace();
                }

                System.out.println("x="+ x + " y=" + y);
            }
        }
    }

    public static void generateCypher() throws FileNotFoundException {
        String entity1, entity2, entity3;
        int right, bottom;

        File outputFile = new File("grid-dataset.cyp");
        PrintWriter out_writer = new PrintWriter(outputFile);

        for (int x = 1; x <= sizeX; x++) {
            for (int y = 1; y <= sizeY; y++) {
                entity1 = "entity_" + x + "_" + y;
                out_writer.println("CREATE (" + entity1 + ":Entity {name:'"+entity1+"'})");
            }
        }

        for (int x = 1; x <= sizeX; x++) {
            for (int y = 1; y <= sizeY; y++) {
                right = x + 1;
                bottom = y + 1;
                entity1 = "(entity_" + x + "_" + y + ")";
                entity2 = "(entity_" + right + "_" + y + ")";
                entity3 = "(entity_" + x + "_" + bottom + ")";
                if (x < sizeX) out_writer.println("CREATE " + entity1 + "-[:HORIZONTAL]->" + entity2);
                if (y < sizeY) out_writer.println("CREATE " + entity1 + "-[:VERTICAL]->" + entity3);
            }
        }

        out_writer.flush();
        out_writer.close();
    }

    public static void generateNeoCsv() throws FileNotFoundException {

        int right, bottom;
        String entity1, entity2, entity3;

        File outputFile1 = new File("grid-entities-neo.csv");
        File outputFile2 = new File("grid-horizontal-neo.csv");
        File outputFile3 = new File("grid-vertical-neo.csv");
        PrintWriter out_writer1 = new PrintWriter(outputFile1);
        PrintWriter out_writer2 = new PrintWriter(outputFile2);
        PrintWriter out_writer3 = new PrintWriter(outputFile3);

        out_writer1.println("name");
        out_writer2.println("name1,name2");
        out_writer3.println("name1,name2");

        for (int x = 1; x <= sizeX; x++) {
            for (int y = 1; y <= sizeY; y++) {
                right = x + 1;
                bottom = y + 1;
                right = x + 1;
                bottom = y + 1;
                entity1 = "entity_" + x + "_" + y;
                entity2 = "entity_" + right + "_" + y;
                entity3 = "entity_" + x + "_" + bottom;
                out_writer1.println(entity1);
                if (x < sizeX) out_writer2.println(entity1+","+entity2);
                if (y < sizeY) out_writer3.println(entity1+","+entity3);
            }
        }

        out_writer1.flush();
        out_writer1.close();
        out_writer2.flush();
        out_writer2.close();
        out_writer3.flush();
        out_writer3.close();
    }
}

