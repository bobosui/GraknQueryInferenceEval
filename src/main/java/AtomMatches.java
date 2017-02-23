import java.security.Key;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by szymon.klarman on 17/02/2017.
 */
public class AtomMatches extends HashMap<String, MatchMap> {

    public void include (String predicate) {
        this.put(predicate, new MatchMap());
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

class MatchMap extends HashMap<String, Set<String>> {

    public void include(String x, String y) {
        if (this.containsKey(x)) this.get(x).add(y);
        else {
            Set<String> set = new HashSet<>();
            set.add(y);
            this.put(x, set);
        }
    }
}
