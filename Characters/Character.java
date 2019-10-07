package Characters;

import java.util.List;
import java.util.Map;

import Creatures.Spellcasting;
import Creatures.Creature;
import Creatures.Creature.DamageMultiplier;
import Creatures.Creature.DamageType;
import Creatures.Creature.Senses;
import Creatures.Creature.Size;
import Creatures.Creature.Speeds;
import Resources.Alignment;
import Resources.Classes;
import Resources.Languages;
import Resources.Skills;
import Resources.Stats;

public interface Character {
	public void constructor(String name);

	public String getName();
	public String getPlayer();
	public Race getRace();
	public Race.Subtype getSubrace();
	public Alignment getAlignment();
	//TODO - Background
	public Integer getXP();	//Null is Milestone?
	public Map<Classes,Integer> getLevels();
	public Map<Classes,Classes.Subclass> getArchetypes();
	

	public int getHP();
	public int getTempHP();
	public Map<Boolean,Integer> getDeathSaves();
	public int[][] getHitDice(); //int[4][2] 1st is size, 2nd is max and current.
	public int getAC();
	
	public Map<Speeds,Integer> getSpeeds();
		public default boolean hasSpeed(Speeds s){return getSpeeds().containsKey(s);}
	public Integer getInitiative();
		
	public Map<Stats,Integer> getStats();
	public Map<Stats,Integer> getSaves();
		public default int getSaveMod(Stats s){
			if(getSaves().containsKey(s)){return getSaves().get(s);}
			else{return scoreToMod(getStats().get(s));}
		}
	public Map<Skills,Integer> getSkills();
		public default int getSkillMod(Skills s){
			if(getSkills().containsKey(s)){return getSkills().get(s);}
			else{return scoreToMod(getStats().get(s.getStandardStat()));}
		}
	//TODO - Proficiencies (weapons, tools)
		//TODO - Proficiency modifier
	
	
	public Map<DamageMultiplier, List<DamageType>> getMultipliers();
	public Map<Senses,Integer> getSenses();
		public default boolean hasSense(Senses s){return getSenses().containsKey(s);}
	public List<Languages> getLanguages();
		public default boolean hasLanguage(Languages l){return getLanguages().contains(l);}
	

	public default Size getSize() {return Size.MEDIUM;}
	public default Creature.Type getType() {return Creature.Type.HUMANOID;}
	

	//TODO - Attacks
	public Spellcasting getInnateCasting();
	public Map<Classes,Spellcasting> getSpellcasting();
	//TODO - Features
	public List<Equipment> getEquipment();
	public String[] getPersonality();	//String[4] - Traits, Ideals, Bonds, Flaws
	public String getBackstory();
	
	public String toString();
	public String toXML();
	
	
	
	

	
	public enum Race{	//TODO mechanics.
		DWARF(true),ELF(true),HALFLING(true),HUMAN(true),DRAGONBORN(true),GNOME(true),ORC(true),TIEFLING(true),
		AASIMAR(true),FIRBOLG(false),GOLIATH(false),KENKU(false),LIZARDFOLK(false),TABAXI(false),TRITON(false),
		BUGBEAR(false),GOBLIN(false),HOBGOBLIN(false),KOBOLD(false),YUANTI(false),GITH(true),
		AARAKOCRA(false),GENASI(true),CENTAUR(false),LOXODON(false),MINOTAUR(false),VEDALKEN(false),VERDAN(false);
		//No Halfs.
		//No Simic.
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
		private final boolean hasSub;
		private Race(boolean hasSub){this.hasSub = hasSub;}
		public boolean hasSubtype(){return hasSub;}
		public Subtype[] getSubtype(Race superType){
			if(superType == Race.DWARF){return dwarfSubtype.values();}
			if(superType == Race.ELF){return elfSubtype.values();}
			if(superType == Race.HALFLING){return halflingSubtype.values();}
			if(superType == Race.HUMAN){return humanSubtype.values();}
			if(superType == Race.DRAGONBORN){return dragonbornSubtype.values();}
			if(superType == Race.GNOME){return gnomeSubtype.values();}
			if(superType == Race.ORC){return orcSubtype.values();}
			if(superType == Race.TIEFLING){return tieflingSubtype.values();}
			if(superType == Race.AASIMAR){return aasimarSubtype.values();}
			if(superType == Race.GITH){return githSubtype.values();}
			if(superType == Race.GENASI){return genasiSubtype.values();}
			return null;
		}		
		
		public interface Subtype{
			public String toNiceString();
		}
		private enum dwarfSubtype implements Subtype{
			HILL,MOUNTAIN,DUERGAR;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum elfSubtype implements Subtype{
			HIGH,WOOD,DARK,ELADRIN,SEA,SHADARKAI;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum halflingSubtype implements Subtype{
			LIGHTFOOT,STOUT,GHOSTWISE;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum humanSubtype implements Subtype{
			BASE,VARIANT;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum dragonbornSubtype implements Subtype{
			BLACK,BLUE,GREEN,RED,WHITE,
			BRASS,BRONZE,COPPER,GOLD,SILVER;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum gnomeSubtype implements Subtype{
			FORSET,ROCK,SVIRFNEBLIN;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum orcSubtype implements Subtype{
			BASE,PURE;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum tieflingSubtype implements Subtype{
			BASE,FERAL,DEVILSTONGUE,HELLFIRE,WINGED,
			ASMODEUS,BAALZEBUL,DISPATER,FIERNA,GLASYA,LEVISTUS,MAMMON,MEPHISTOPHOLES,ZARIEL;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum aasimarSubtype implements Subtype{
			PROTECTOR,SCOURGE,FALLEN;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum githSubtype implements Subtype{
			GITHYANKI,GITHZERAI;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
		private enum genasiSubtype implements Subtype{
			AIR,EARTH,FIRE,WATER;
			public String toNiceString(){return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);}
		}
	}
	
	
	public static int scoreToMod(int score) {
		if(score>=10){return (score-10)/2;}
		else{return (score-11)/2;}
	}
}
