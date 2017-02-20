import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.io.FileNotFoundException;

/**
 * Created by szymon.klarman on 07/02/2017.
 */
public class Main {

    public static void main(String[] args) {
    //    Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
     //   logger.setLevel(Level.OFF);

        AltRules.test();
        //NeoBenchmark.queryingTest();
        //NeoBenchmark.queryingTestGrid();
        //NeoBenchmark.transitiveQueryingTest();
        //NeoBenchmark.transitiveQueryingTestGrid();
        //JenaBenchmark.queryingTest();
        //JenaBenchmark.reasoningTestGrid();
        //JenaBenchmark.reasoningTest();
        //JenaBenchmark.transitiveQueryingTest();
        //JenaBenchmark.transitiveQueryingTestGrid();
        //JenaBenchmark.queryingTestGrid();

        //GraknBenchmark.queryingTypes();
        //GraknBenchmark.reasoningTest();
        //GraknBenchmark.reasoningTestGrid();
        //GraknBenchmark.queryingTest();
        //GraknBenchmark.queryingTestGrid();

        //DataGenerator.generateDataWithGraknCore();
        //GraknBenchmark.queryingTestGridSimple();
/*
        try {
            DataGenerator.generateRdf();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/
/*
        try {
            DataGenerator.generateGraqlCsv();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/

/*
        try {
            DataGenerator.generateCypher();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/
/*
        try {
            DataGenerator.generateNeoCsv();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/

    }
}