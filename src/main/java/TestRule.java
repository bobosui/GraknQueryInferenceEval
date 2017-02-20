import org.apache.hadoop.util.hash.Hash;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by szymon.klarman on 17/02/2017.
 */
public class TestRule {
    Set<TestAtom> lhs;
    TestAtom rhs;

    public TestRule(String lhs, String rhs) {

        this.lhs = new HashSet<>();
        String[] parsedLhs= lhs.split(";\\s");
        for (int i=0; i< parsedLhs.length; i++) {
            TestAtom atom = new TestAtom(parsedLhs[i]);
            this.lhs.add(atom);
        }

        this.rhs = new TestAtom(rhs);
    }

    public void printme() {
            lhs.iterator().forEachRemaining(atom -> {
                System.out.print(atom.toString());
                System.out.print(" ");
            });

        System.out.print("-> ");
        System.out.println(this.rhs.toString());
    }
}

class TestAtom {
    String var1;
    String var2;
    String predicate;

    public TestAtom(String full) {
        String[] basic = full.split("\\(|,\\s|\\)");
        this.predicate = basic[1];
        this.var1 = basic[2];
        this.var2 = basic[3];
    }

    public String toString() {
        return "(" + this.predicate + ", " + this.var1 + ", " + this.var2 + ")";
    }
}