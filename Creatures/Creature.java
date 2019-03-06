package Creatures;

import java.util.List;
import java.util.Map;
import Resources.Source;

public interface Creature {
	public void constructor(String name, Size size, Type type, Alignment align, int ac, int hp, Map<Speeds,Integer> speed,
			int[] stats, Map<Creature.Stats,Integer> saves, Map<Skills,Integer> skills, 
			Map<DamageType, Creature.DamageMultiplier> damageMultipliers, List<StatusCondition> conditionImmunities,
			Map<Senses,Integer> senses, List<Languages> languages, double cr, int legendaryResistances, int regen, 
			List<DamageType> regenBlocks, List<String> passives, List<Action> actions, int legendaryActions, 
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
	public double damageMult(DamageType damageType);
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

	
	
	
	
	
	
	public enum Size {
		TINY(1/4),SMALL(1),MEDIUM(1),LARGE(2),HUGE(3),GARGANTUAN(4);
		private float squares;
		private Size(float squares) {this.squares = squares;}
		public float getSquares() {return squares;}
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	
	public enum Type{
		ABBERATION(false),BEAST(false),CELESTIAL(false),CONSTRUCT(false),
		DRAGON(false),ELEMENTAL(false),FEY(false),FIEND(true),
		GIANT(false),HUMANOID(true),MONSTOSITY(false),OOZE(false),
		PLANT(false),UNDEAD(false);
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}

		private final boolean hasSub;
		private Type(boolean hasSub){this.hasSub = hasSub;}
		public boolean hasSubtype(){return hasSub;}
		public subType[] getSubtype(Type superType){
			if(superType == Type.FIEND){return fiendSubtype.values();}
			if(superType == Type.HUMANOID){return humanoidSubtype.values();}
			return null;
		}		
		
		public interface subType{
			public String toNiceString();
		}
		private enum fiendSubtype implements subType{
			DEMON,DEVIL;
			public String toNiceString(){return name().toUpperCase().substring(0, 1)
					+ name().toLowerCase().substring(1);}
		}
		private enum humanoidSubtype implements subType{
			ANY,HUMAN,ELF,ORC;//TODO humanoid subtypes
			public String toNiceString(){return name().toUpperCase().substring(0, 1)
					+ name().toLowerCase().substring(1);}
		}
	}
	
	public enum Alignment{
		LG("Lawful Good"),NG("Neutral Good"),CG("Chaotic Good"),
		LN("Lawful Neutral"),TN("True Neutral"),CN("Chaotic neutral"),
		LE("Lawful Evil"),NE("Neutral Evil"),CE("Chaotic Evil"),
		UNALIGNED("Unaligned");
		private String niceFormat;
		private Alignment(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	
	public enum Speeds{
		WALK,FLY,SWIM,CLIMB,BURROW;
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	
	public enum Stats{
		STR,DEX,CON,INT,WIS,CHA;
	}
	
	public enum Skills{
		ACROBATICS("Acrobatics"),ANIMALHANDLING("Animal Handling"),ARCANA("Arcana"),ATHLETICS("Athletics"),
		DECEPTION("Deception"),HISTORY("History"),INSIGHT("Insight"),INTIMIDATION("Intimidation"),
		INVESTIGATION("Investigation"),MEDICINE("Medicine"),NATURE("Nature"),PERCEPTION("Perception"),
		PERFORMANCE("Performance"),PERSUASION("Persuasion"),RELIGION("Religion"),SLIGHTOFHAND("Slight of Hand"),
		STEALTH("Stealth"),SURVIVAL("Survival");
		private String niceFormat;
		private Skills(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	
	public enum DamageMultiplier{
		NONE(1),RESISTANCE(0.5),IMMUNITY(0),VULNERABILITY(2),HEALING(-1);
		private double multiplier;
		private DamageMultiplier(double multiplier) {this.multiplier = multiplier;}
		public double getMult() {return multiplier;}
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}

	public enum DamageType{
		BLUDGEONING,PIERCING,SLASHING,NONMAGICALBASIC,
		ACID,COLD,FIRE,LIGHTNING,POISON,PSYCHIC,THUNDER,
		FORCE,NECROTIC,RADIANT,
		HEALING;
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
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
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	
	
	public enum Senses{
		DARKVISION,BLINDSIGHT,TREMORSENSE,TRUESIGHT;
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	
	public enum Languages{//TODO complete
		COMMON,DRACONIC,DWARVISH,ELVISH,//Common languages
		GIANT,GOBLIN,ORCISH,UNDERCOMMON,//Uncommon languages
		CELESTIAL,DEEPSPEECH,DRUIDIC,SYLVAN,PRIMORDIAL,//Rare languages
		ABYSSAL,INFERNAL,//Fiendish languages
		AURAN,AQUAN,IGNAN,TERRAN,//Elemental languages
		ALL,TELEPATHY,//Universal languages
		
		BULLYWUG,GITH,GNOLL,GNOMISH,GRELL,//Other languages
		HOOKHORROR,MODRON,OTYUGH,SAHUAGIN,
		SLAAD,SPHINX,THRIKREEN,TROGLODYTE,
		UMBERHULK,YETI;
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	
	public enum Region{
		ARCTIC,COAST,DESERT,FOREST,
		GRASSLAND,MOUNTAIN,SWAMP,UNDERDARK,
		URBAN;
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
}
