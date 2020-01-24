package utils;
/**
 * This class represents a simple 1D range of shape [min,max]
 * @author boaz_benmoshe
 *
 */
public class Range {
	private double min, max;
	public Range(double min, double max) {
		set_min(min);
		set_max(max);
	}
	public boolean isIn(double d) {
		boolean inSide = false;
		if(d>=this.get_min() && d<=this.get_max()) {inSide=true;}
		return inSide;
	}
	public String toString() {
		String ans = "["+this.get_min()+","+this.get_max()+"]";
		if(this.isEmpty()) {ans = "Empty Range";}
		return ans;
	}
	public boolean isEmpty() {
		return this.get_min()>this.get_max();
	}
	public double get_max() {
		return max;
	}
	public double get_length() {
		return max-min;
	}
	
	private void set_max(double max) {
		this.max = max;
	}
	public double get_min() {
		return min;
	}
	private void set_min(double min) {
		this.min = min;
	}
	
}


