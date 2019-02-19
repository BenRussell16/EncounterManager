package EncounterManager.src.Encounters;
import java.util.ArrayList;
import java.util.List;

import EncounterManager.src.Creatures.Action;
import EncounterManager.src.Creatures.Creature;

public class EncounterEntity {
	//TODO limited use actions/spells

	public final String label;
	public final Creature creature;
	private int curHP;
	private List<Creature.StatusCondition> statusEffects = new ArrayList<Creature.StatusCondition>();
	private int legendaryActions;
	private int legendaryResistances;
	private int initiative;
	
	public EncounterEntity(Creature creature, String label) {
		this.creature = creature;
		this.label = label;
		curHP = creature.getHP();
		legendaryActions = creature.getLegendaryAct();
		legendaryResistances = creature.getLegendaryRes();
	}
	
	public void startOfTurn() {
		legendaryActions = creature.getLegendaryAct();
		//TODO regen
		//TODO status reminders
		//TODO recharge state
	}
	
	public int getHP() {return curHP;}
	public void dealDamage(int amount, Action.DamageType type) {
		curHP -= amount*creature.damageMult(type);
		if(curHP<0) {curHP = 0;}
	}
	
	public void setInitiative(int i) {initiative = i+creature.getStats()[1];}
	public int getInitiative() {return initiative;}
	
	public List<Creature.StatusCondition> getStatus(){return statusEffects;}
	public void addStatus(Creature.StatusCondition status) {
		if(!creature.conditionImmune(status) && !statusEffects.contains(status)) {
			statusEffects.add(status);
		}
	}
	public void removeStatus(Creature.StatusCondition status) {statusEffects.remove(status);}
}
