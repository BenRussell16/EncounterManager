package EncounterManager.src.Creatures;

public interface TemplateCreature extends Creature {
	public Creature apply(Creature base);
}
