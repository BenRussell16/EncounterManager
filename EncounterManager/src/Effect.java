package EncounterManager.src;

public class Effect extends Action{
	
	private final String effect;
	private final int range;
	private final int area;
	private final int secondaryArea;
	private final Shape shape;
	private final int DC;
	private final Creature.Stats save;
	
	public Effect(String name, int recharge, int uses, Time time, String effect, int range, int area, int secondaryArea, Shape shape, int DC, Creature.Stats save) {
		super(name, recharge, uses, time);
		this.effect = effect;
		this.range = range;
		this.area = area;
		this.secondaryArea = secondaryArea;
		this.shape = shape;
		this.DC = DC;
		this.save = save;
	}

	public String getEffect() {return effect;}
	public int getRange() {return range;}
	public int getArea() {return area;}
	public int getSecondaryArea() {return secondaryArea;}
	public Shape getShape() {return shape;}
	public int getDC() {return DC;}
	public Creature.Stats getSave(){return save;}
	
	@Override
	public String toString() {
		String s = super.toString();
		s+="\t";
		if(range>=0) {s+=range+"ft range\t";}
		if(shape!=Action.Shape.SELF && shape!=Action.Shape.SINGLE){
			s+=area+"ft ";
			if(shape==Action.Shape.CYLINDER) {s+="radius, "+secondaryArea+"ft tall ";}
		}
		s+=shape+"\tDC "+DC+" "+save.toString()+" save\t"+effect;
		return s;
	}}
