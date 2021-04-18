package Model;

public class Rule {
    String s;
    String t;
    double value; //value*T=S
    public Rule(String s, String t, double value) {
        this.s = s;
        this.t = t;
        this.value = value;
    }

    public String getS() {
        return s;
    }

    public String getT() {
        return t;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "s='" + s + '\'' +
                ", t='" + t + '\'' +
                ", value=" + value +
                '}';
    }
}
