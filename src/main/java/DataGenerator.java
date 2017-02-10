import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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

