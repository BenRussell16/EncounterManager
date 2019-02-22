package src.Creatures;

import Resources.Area;

public class Buff extends Action {
	
	private final String effect;
	private final Area shape;
	private final int size;

	public Buff(String name, int recharge, int uses, Time time, Area shape, int size, String effect) {
		super(name, recharge, uses, time);
		this.shape = shape;
		this.size = size;
		this.effect = effect;
	}

	public String getEffect() {return effect;}
	@Override public String toString() {
		String s = super.toString()+"\t";
		if(shape == Area.SELF) {s+=shape.toString();}
		else {s+=size+"ft "
				+shape.toString();}
		s+="\t"+effect;
		return s;
	}
}
