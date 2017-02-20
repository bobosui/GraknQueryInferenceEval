import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by szymon.klarman on 17/02/2017.
 */
public class AtomMatches extends HashMap<String, Set<Match>> {

    public void newPredicate(String predicate) {
        Set<Match> matchSet = new HashSet<>();
        this.put(predicate, matchSet);
    }
}

class Match {
    HashMap match;

    public void Match(String x, String y) {
        this.match = new HashMap();
        this.match.put("var1", x);
        this.match.put("var2", y);
    }
}
