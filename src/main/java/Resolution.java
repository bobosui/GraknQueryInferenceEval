import java.util.HashSet;
import java.util.Set;

/**
 * Created by szymon.klarman on 17/02/2017.
 */
public class Resolution {
    TestAtom goal;
    Set<TestRule> ruleset;
    Set<Match> results;

    public void Resolution(TestAtom goal, Set<TestRule> ruleset) {
        this.goal = goal;
        this.ruleset = ruleset;
        this.results = new HashSet<>();
    }

    public void execute() {

    }
}

class Goal {
    Set<TestAtom> atoms;
    Set<Match> substitution;
}