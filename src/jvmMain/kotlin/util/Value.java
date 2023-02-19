package util;

public class Value implements Comparable<Value> {

     boolean selected;
     Integer value;
     public Value(int value) {
         
         this.value = value;
     }

    public Value(boolean selected, int value) {
        this.selected = selected;
        this.value = value;
    }
    
	@Override
	public int compareTo(Value v) {
		// TODO Auto-generated method stub
		return  this.value.compareTo(v.value);
	}

    @Override
    public boolean equals(Object v) {
        return v instanceof Value && this.value.equals(((Value) v).value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

}
