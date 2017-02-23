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
    String var1;
    String var2;

    public Match(String x, String y) {
        this.var1=x;
        this.var2=y;
    }
}
