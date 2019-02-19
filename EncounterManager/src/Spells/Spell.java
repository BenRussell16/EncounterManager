package EncounterManager.src.Spells;

import java.util.List;
import EncounterManager.Resources.Source;
import EncounterManager.src.Spells.Spell.Classes;
import EncounterManager.src.Spells.Spell.School;

public interface Spell {

	public void constructor(String name, int level, School school, List<Classes> classes, List<Source> source);
	
	public String getName();
	public int getLevel();
	public School getSchool();
	public List<Classes> getClasses();
	public boolean fromClass(Classes curClass);
	public boolean fromSource(Source source);
	
	public String toString();
	

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
