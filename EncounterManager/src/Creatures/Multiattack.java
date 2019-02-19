package EncounterManager.src.Creatures;

public class Multiattack extends Action {
	
	private final String content;

	public Multiattack(String name, int recharge, int uses, Time time, String content) {
		super(name, recharge, uses, time);
		this.content = content;
	}
	
	public String getContent() {return content;}
	@Override public String toString() {return super.toString()+"\t"+content;}

}
