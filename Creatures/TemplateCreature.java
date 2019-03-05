package Creatures;

public interface TemplateCreature extends Creature {
	public Creature apply(Creature base);
}
