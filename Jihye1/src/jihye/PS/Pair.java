package jihye.PS;

public class Pair<K, E> {
	private K first;
    private E second;
    
	public Pair(K first, E second) {
    	super();
    	this.first = first;
    	this.second = second;
    }
	
	public K getFirst() {
    	return first;
    }

    public void setFirst(K first) {
    	this.first = first;
    }

    public E getSecond() {
    	return second;
    }

    public void setSecond(E second) {
    	this.second = second;
    }
}
