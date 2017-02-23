import java.util.HashSet;
import java.util.Set;

/**
 * Created by szymon.klarman on 17/02/2017.
 */
public class Resolution {
    Goal goal;
    Set<TestRule> ruleset;
    AtomMatches data;
    Set<Match> matches;
    Set<Goal> registeredGoals;

    public Resolution(TestAtom goalAtom, Set<TestRule> rules) {
        this.goal = new Goal(goalAtom, this);
        this.ruleset = rules;
        this.data = new AtomMatches();
        this.matches = new HashSet();
        this.registeredGoals = new HashSet<>();

    }

    public void execute() {
        goal.resolve();
        matches = goal.matches;

    }

    public void ensureData(String predicate) {
        if (data.containsKey(predicate)) {
            System.out.println("Found data for " + predicate);
            return;
        } else {
            Set<Match> dataSet = new HashSet<>();
            data.put(predicate, dataSet);
            for (int i=0; i<10; i++) {
                String var1 = "a"+i;
                int j = i+1;
                String var2 = "a"+j;
                Match newMatch = new Match(var1, var2);
                dataSet.add(newMatch);
            }
        }



    }
}

class Goal {
    Resolution proof;
    Set<TestAtom> goal;
    Match substitution;
    Set<Match> matches;
    Goal parent;

    public Goal (TestAtom atomGoal, Resolution proof) {
        this.proof = proof;
        this.goal = new HashSet<>();
        this.goal.add(atomGoal);
        this.substitution = null;
        this.parent = null;
    }

    public void resolve() {
        for (TestAtom nextAtom : goal) {
            proof.ensureData(nextAtom.predicate);

            for (Match nextMatch : proof.data.get(nextAtom.predicate)) {
                System.out.println(nextMatch.var1 + " and " + nextMatch.var2);
            }
        }
    }

}