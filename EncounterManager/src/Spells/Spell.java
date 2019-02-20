package EncounterManager.src.Spells;

import java.util.List;
import EncounterManager.Resources.Source;

public interface Spell {

	public void constructor(String name, int level, School school, List<Classes> classes, List<Source> sources);
	
	public String getName();
	public int getLevel();
	public School getSchool();
	public List<Classes> getClasses();
	public boolean fromClass(Classes curClass);
	public boolean fromSource(Source source);
	
	public String toString();	
	public default String toXML(){
		String builtString = "<spell>\n";
		builtString += "\t<name>"+getName()+"</name>\n";
		builtString += "\t<level>"+getLevel()+"</level>\n";
		builtString += "\t<school>"+getSchool()+"</school>\n";
		builtString += "\t<classes>";
		int i=0;
		for(Classes c:Classes.values()){
			if(fromClass(c)){
				if(i>0){builtString+=",";}
				builtString+=c.toString();
				i++;
			}
		}
		builtString += "</classes>\n";
		builtString += "\t<source>";
		i=0;
		for(Source s:Source.values()){
			if(fromSource(s)){
				if(i>0){builtString+=",";}
				builtString+=s.toString();
				i++;
			}
		}
		builtString += "</source>\n";
		builtString += "</spell>\n";
		return builtString;
	}
	

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
