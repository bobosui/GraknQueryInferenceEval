import ai.grakn.Grakn;
import ai.grakn.GraknGraph;
import ai.grakn.concept.Concept;
import ai.grakn.concept.Rule;
import ai.grakn.graql.MatchQuery;
import ai.grakn.graql.QueryBuilder;
import ai.grakn.graql.VarName;
import ai.grakn.graql.internal.reasoner.query.QueryAnswers;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by szymon.klarman on 17/02/2017.
 */
public class AltRules {
   // static String keyspace = "grakn";
   // public static GraknGraph graknGraph = Grakn.factory(Grakn.DEFAULT_URI, keyspace).getGraph();
   // static String queryString;
   // static MatchQuery testQuery;

    public static void test() {
        /*
        QueryBuilder qb = graknGraph.graql().infer(false);

        queryString = "match $x isa inference-rule;";


        QueryAnswers answers = queryAnswers(qb.parse(queryString));

        Rule rule = answers.iterator().next().get(VarName.of("x")).asRule();
        System.out.println(rule.getLHS().toString());

       */
        Set<TestRule> rules = new HashSet<>();

        rules.add(new TestRule("(relation1, x, y); (relation1, y, z)", "(relation1, x, z)"));
        rules.add(new TestRule("(aaaa, x, y); (aaaa, y, z)", "(aaaa, x, z)"));

        for (TestRule nextRule:rules) nextRule.printme();




    }

    private static QueryAnswers queryAnswers(MatchQuery query) {
        return new QueryAnswers(query.admin().results());
    }

    }

