import java.security.Key;
import java.util.HashMap;
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
            data.include(predicate);
            for (int i=0; i<10; i++) {
                String var1 = "a"+i;
                int j = i+1;
                String var2 = "a"+j;
                data.get(predicate).include(var1, var2);
                data.get(predicate).include(var1, var1);
            }
        }



    }
}

class Goal {
    Resolution proof;
    Set<TestAtom> goal;
    HashMap<String, String> substitution;
    Set<Match> matches;
    Goal parent;

    public Goal (TestAtom atomGoal, Resolution proof) {
        this.proof = proof;
        this.goal = new HashSet<>();
        this.goal.add(atomGoal);
        this.substitution = new HashMap<>();
        this.parent = null;

    }

    public void resolve() {
        goal.forEach(
                (TestAtom atom) -> {
                    proof.ensureData(atom.predicate);
                    MatchMap matches = proof.data.get(atom.predicate);
                    matches.keySet().forEach(
                    (String key) ->  {
                            System.out.println("x=" + key);
                            for (String nextVar : matches.get(key)) {
                                System.out.println("\t y=" + nextVar);
                            }
                            //try to make a new goal by unifying with the head of some rule and resolve.


            });
        });
    }

}