package me.huntifi.castlesiege.data_types;

public class Tuple<K, V> {
    private K first;
    private V second;

    public Tuple(K first, V second){
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}
