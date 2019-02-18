package EncounterManager.src;

public class Attack extends Action{
	
	private final String damage;
	private final int reach;
	private final int attackBonus;
	
	public Attack(String name, int recharge, int uses, Time time, String damage, int reach, int attackBonus){
		super (name, recharge, uses, time);
		this.damage = damage;
		this.reach = reach;
		this.attackBonus = attackBonus;
	}
	
	//TODO figure out long and short range
	public String getDamage() {return damage;}
	public int getReach() {return reach;}
	public int getAttackBonus() {return attackBonus;}
	
	@Override
	public String toString() {
		String s = super.toString();
		s +="\t"+reach+"ft range\t"+damage+"\t";
		if(attackBonus>=0) {s+="+";}
		s+=attackBonus+" to hit";
		return s;
	}
}
