public class TestAtom {
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
