
public abstract class Action {

	public enum Time{
		ACTION,BONUS,MOVE,REACTION,
		FREE,LAIR,LEGENDARY;
	}
	public enum Shape{
		SELF,SINGLE,CONE,LINE,
		CYLINDER,SPHERE,CUBE;
	}
	public enum DamageType{
		ACID,BLUDGEONING,COLD,FIRE,FORCE,
		LIGHTNING,NECROTIC,PIERCING,POISON,PSYCHIC,
		RADIANT,SLASHING,THUNDER,
		NONMAGICALBASIC,
		HEALING;
	}
	protected final String name;
	protected final int recharge;
	protected final int uses;
	protected final Time time;
	
	public Action(String name, int recharge, int uses, Time time){
		this.name = name;
		this.recharge = recharge;
		this.uses = uses;
		this.time = time;
	}
	
	public String getName() {return name;}
	public int getRecharge() {return recharge;}
	public int getUses() {return uses;}
	public Time getTime() {return time;}
	
	public String toString() {
		String s = name+"\t"+time.toString();
		if(recharge>=0) {
			s+="\tRecharge:"+recharge;
			if(recharge<6) {s+="-6";}
		}
		if(uses>=0) {s+="\tUses:"+uses;}
		return s;
	}
}
