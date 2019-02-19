package EncounterManager.src.Creatures;

import java.util.List;
import java.util.Map;
import EncounterManager.Resources.Source;

public interface Creature {
	public void constructor(String name, Size size, Type type, Alignment align, int ac, int hp, Map<Speeds,Integer> speed,
			int[] stats, Map<Creature.Stats,Integer> saves, Map<Skills,Integer> skills, 
			Map<Action.DamageType, Creature.DamageMultiplier> damageMultipliers, List<StatusCondition> conditionImmunities,
			Map<Senses,Integer> senses, List<Languages> languages, double cr, int legendaryResistances, int regen, 
			List<Action.DamageType> regenBlocks, List<String> passives, List<Action> actions, int legendaryActions, 
			List<Source> sources);
	
	public String getName();
	public int getHP();
	public int getAC();
	public Size getSize();
	public Map<Speeds,Integer> getSpeed();
	
	public int[] getStats();
	public Map<Stats,Integer> getSaves();
	public Map<Skills,Integer> getSkills();
	
	public List<String> passiveEffects();
	public int getRegen();//TODO figure out blocks
	
	public boolean conditionImmune(StatusCondition condition);
	public double damageMult(Action.DamageType damageType);
	public int getLegendaryRes();
	public int getLegendaryAct();
	public List<Action> getActions();
	public List<Action> getLairActions();
	public List<String>[] getSpells();
	public int[] getSpellSlots();

	public List<Languages> getLanguages();
	public List<Senses> getSenses();//TODO figure out ranges
	
	public double getCR();
	public int getXP();
	public Alignment getAlign();
	public boolean isType(Type type);//TODO figure out subtypes
	public boolean inRegion(Region area);
	public boolean fromSource(Source source);
	
	public String toString();
	
	public static int scoreToMod(int score) {return (score-10)/2;}

	public enum DamageMultiplier{
		IMMUNITY(0),RESISTANCE(0.5),NONE(1),VULNERABILITY(2),HEALING(-1);
		private double multiplier;
		private DamageMultiplier(double multiplier) {this.multiplier = multiplier;}
		public double getMult() {return multiplier;}
	}
	public enum Size {
		TINY(1/4),SMALL(1),MEDIUM(1),LARGE(2),HUGE(3),GARGANTUAN(4);
		private float squares;
		private Size(float squares) {this.squares = squares;}
		public float getSquares() {return squares;}
	}
	public enum Type{//TODO humanoid subtypes
		ABBERATION,BEAST,CELESTIAL,CONSTRUCT,
		DRAGON,ELEMENTAL,FEY,FIEND,
		GIANT,HUMANOID,MONSTOSITY,OOZE,
		PLANT,UNDEAD;
	}
	public enum Region{
		ARCTIC,COAST,DESERT,FOREST,
		GRASSLAND,MOUNTAIN,SWAMP,UNDERDARK,
		URBAN;
	}
	public enum Alignment{
		LG,NG,CG,
		LN,TN,CN,
		LE,NE,CE,
		UNALIGNED;
	}
	public enum Speeds{//TODO complete
		WALK,SWIM,CLIMB,FLY,BURROW;
	}
	public enum StatusCondition{
		BLINDED,CHARMED,
		DEAFENED,FRIGHTENED,
		GRAPPLED,INCAPACITATED,
		INVISIBLE,PARALYZED,
		PETRIFIED,POISONED,
		PRONE,RESTRAINED,
		STUNNED,UNCONCIOUS,
		EXHAUSTION;
	}
	public enum Stats{
		STR,DEX,CON,INT,WIS,CHA;
	}
	public enum Skills{
		ACROBATICS,ANIMALHANDLING,ARCANCA,ATHLETICS,
		DECEPTION,HISTORY,INSIGHT,INTIMIDATION,
		INVESTIGATION,MEDICINE,NATURE,PERCEPTION,
		PERFORMANCE,PERSUASION,RELIGION,SLIGHTOFHAND,
		STEALTH,SURVIVAL;
	}
	public enum Senses{//TODO complete
		DARKVISION,BLINDSIGHT,TRUESIGHT;
	}
	public enum Languages{//TODO complete
		AQUAN,COMMON,DRACONIC,DWARVISH,ELVEN,GOBLIN,ORCISH;
	}
}
