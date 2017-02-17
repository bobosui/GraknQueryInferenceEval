import java.util.Set;

/**
 * Created by szymon.klarman on 17/02/2017.
 */
public class TestRule {
    Set<TestAtom> lhs;
    TestAtom rhs;

    public TestRule(String lhs, String rhs) {

        String[] parsedLhs= lhs.split(",");
        for (int i=0, i<)

    }

}

class TestAtom {
    String var1;
    String var2;
    String predicate;

    public TestAtom(String full) {
        full.split("\(").
    }
    public TestAtom(String predicate, String var1, String var2) {
        this.predicate = predicate;
        this.var1 = var1;
        this.var2 = var2;
    }
}