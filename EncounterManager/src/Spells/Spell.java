package EncounterManager.src.Spells;

import EncounterManager.src.Creatures.Creature.Source;
import java.util.List;

public interface Spell {

	public String getName();
	public int getLevel();
	public School getSchool();
	public List<Classes> getClasses();
	public boolean fromClass(Classes classes);
	public boolean fromSource(Source source);

	public enum School{
		ABJURATION,CONJURATION,DIVINATION,ENCHANTMENT,
		EVOCATION,ILLUSION,NECROMANCY,TRANSMUTATION;
	}
	public enum Classes{
		// Barbarian, Bard, Cleric, Druid, Fighter, Monk,
			// Paladin, Ranger, Rogue, Sorcerer, Warlock, Wizard
		BARD,CLERIC,DRUID,PALADIN,RANGER,SORCERER,WARLOCK,WIZARD;
	}
}
