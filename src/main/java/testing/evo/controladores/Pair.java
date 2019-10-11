package testing.evo.controladores;

public class Pair<U,V>{
    private U first;
    private V second;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Pair<?,?> pair = (Pair<?,?>) o;
        if(!first.equals(pair.first))
            return false;
        return second.equals(pair.second);
    }

    public int hashCode() {
        return 31 * first.hashCode() + second.hashCode();
    }

    public String toString() {
        return "(" + first + ", " + second + ")";
    }

}