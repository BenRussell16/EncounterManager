package Creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Resources.Source;

public interface newCreature {
	public void constructor(String name, Size size, Type type, Type.Subtype subtype, boolean isShapechanger,
			List<Alignment> align, double cr, int hp, int ac,
			Map<Speeds,Integer> speed, Map<Stats,Integer> stats, Map<Stats,Integer> saves, Map<Skills,Integer> skills,
			Map<DamageMultiplier, List<DamageType>> damageMultipliers, List<StatusCondition> conditionImmunities,
			Map<Senses,Integer> senses, List<Languages> languages, List<Region> regions,
			int legendaryResistances, newSpellcasting innate, newSpellcasting casting, Map<String,String> passives, String otherNotes,
			String multiattack, List<newAttack> attacks, List<newEffect> otherActions, Map<String,String> reactions,
			int legendaryActionCount, List<newLegendaryAction> legendaryActions, List<String> lairActions,
			List<Source> sources);
	
	public String getName();
	public Size getSize();
	
	public Type getType();
	public Type.Subtype getSubtype();
	public boolean isShapechanger();

	public List<Alignment> getAlignment();
		public default boolean isAlign(Alignment a){return getAlignment().contains(a);}
	public double getCR();
		public default int getXP() {
			if(getCR()==0.125) {return 25;}			else if(getCR()==0.25) {return 50;}		else if(getCR()==0.5) {return 100;}
			else if(getCR()==1) {return 200;}		else if(getCR()==2) {return 450;}		else if(getCR()==3) {return 700;}
			else if(getCR()==4) {return 1100;}		else if(getCR()==5) {return 1800;}		else if(getCR()==6) {return 2300;}
			else if(getCR()==7) {return 2900;}		else if(getCR()==8) {return 3900;}		else if(getCR()==9) {return 5000;}
			else if(getCR()==10) {return 5900;}		else if(getCR()==11) {return 7200;}		else if(getCR()==12) {return 8400;}
			else if(getCR()==13) {return 10000;}	else if(getCR()==14) {return 11500;}	else if(getCR()==15) {return 13000;}
			else if(getCR()==16) {return 15000;}	else if(getCR()==17) {return 18000;}	else if(getCR()==18) {return 20000;}
			else if(getCR()==19) {return 22000;}	else if(getCR()==20) {return 25000;}	else if(getCR()==21) {return 33000;}
			else if(getCR()==22) {return 41000;}	else if(getCR()==23) {return 50000;}	else if(getCR()==24) {return 62000;}
			else if(getCR()==25) {return 75000;}	else if(getCR()==26) {return 90000;}	else if(getCR()==27) {return 105000;}
			else if(getCR()==28) {return 120000;}	else if(getCR()==29) {return 135000;}	else if(getCR()==30) {return 155000;}
			else {return 0;}//cr 0 is worth 0 or 10 xp
		}
	public int getHP();
	public int getAC();
	
	public Map<Speeds,Integer> getSpeeds();
		public default boolean hasSpeed(Speeds s){return getSpeeds().containsKey(s);}
	public Map<Stats,Integer> getStats();
	public Map<Stats,Integer> getSaves();
		public default int getSaveMod(Stats s){
			if(getSaves().containsKey(s)){return getSaves().get(s);}
			else{return scoreToMod(getStats().get(s));}
		}
	public Map<Skills,Integer> getSkills();
		public default int getSkillMod(Skills s){
			if(getSkills().containsKey(s)){return getSkills().get(s);}
			else{return scoreToMod(getStats().get(s));}
		}

	public Map<DamageMultiplier, List<DamageType>> getMultipliers();
		public default List<DamageType> getResistances(){
			return getMultipliers().get(DamageMultiplier.RESISTANCE);}
		public default List<DamageType> getImmunities(){
			return getMultipliers().get(DamageMultiplier.IMMUNITY);}
		public default List<DamageType> getVulnerabilities(){
			return getMultipliers().get(DamageMultiplier.VULNERABILITY);}
		public default double getMultiplier(DamageType d){
			for(DamageMultiplier dm: getMultipliers().keySet()){
				if(getMultipliers().get(dm).contains(d)){return dm.getMult();}}
			return 1;}
	public List<StatusCondition> getConditionImmunities();
	
	public Map<Senses,Integer> getSenses();
		public default boolean hasSense(Senses s){return getSenses().containsKey(s);}
	public List<Languages> getLanguages();
		public default boolean hasLanguage(Languages l){return getLanguages().contains(l);}
	public List<Region> getRegions();
		public default boolean fromRegion(Region r){return getRegions().contains(r);}
	
	
	public int getLegendaryResistances();
	public newSpellcasting getInnateCasting();
	public newSpellcasting getSpellcasting();
	public Map<String,String> getPassives();
	public String otherNotes();
	
	public String getMultiattack();
	public List<newAttack> getAttacks();
	public List<newEffect> getEffects();
		public default List<Object> getActions(){
			List<Object> actions = new ArrayList<Object>();
			actions.add(new newEffect("Multiattack", null, getMultiattack()));
			actions.addAll(getAttacks());
			actions.addAll(getEffects());
			return actions;}
	public Map<String,String> getReactions();
	public int getLegendaryActionCount();
	public List<newLegendaryAction> getLegendaryActions();
	public List<String> getLairActions();
		public default boolean hasLair(){return !getLairActions().isEmpty();}
	
	public List<Source> getSource();
		public default boolean fromSource(Source s){return getSource().contains(s);}
	

	public String toString();
	public String toXML();
	
	
	
	
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
		public Subtype[] getSubtype(Type superType){
			if(superType == Type.FIEND){return fiendSubtype.values();}
			if(superType == Type.HUMANOID){return humanoidSubtype.values();}
			return null;
		}		
		
		public interface Subtype{
			public String toNiceString();
		}
		private enum fiendSubtype implements Subtype{
			DEMON,DEVIL;
			public String toNiceString(){return name().toUpperCase().substring(0, 1)
					+ name().toLowerCase().substring(1);}
		}
		private enum humanoidSubtype implements Subtype{
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
		ACROBATICS("Acrobatics",Stats.DEX),ANIMALHANDLING("Animal Handling",Stats.WIS),ARCANA("Arcana",Stats.INT),ATHLETICS("Athletics",Stats.STR),
		DECEPTION("Deception",Stats.CHA),HISTORY("History",Stats.INT),INSIGHT("Insight",Stats.WIS),INTIMIDATION("Intimidation",Stats.CHA),
		INVESTIGATION("Investigation",Stats.INT),MEDICINE("Medicine",Stats.WIS),NATURE("Nature",Stats.INT),PERCEPTION("Perception",Stats.WIS),
		PERFORMANCE("Performance",Stats.CHA),PERSUASION("Persuasion",Stats.CHA),RELIGION("Religion",Stats.INT),SLIGHTOFHAND("Slight of Hand",Stats.DEX),
		STEALTH("Stealth",Stats.DEX),SURVIVAL("Survival",Stats.WIS);
		private String niceFormat;
		private Stats standardStat;
		private Skills(String niceFormat, Stats standardStat){
			this.niceFormat = niceFormat;
			this.standardStat = standardStat;
		}
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
		HALFLING,HOOKHORROR,MODRON,OTYUGH,SAHUAGIN,
		SLAAD,SPHINX,THRIKREEN,TROGLODYTE,
		UMBERHULK,YETI;
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	
	public enum Region{
		ARCTIC,COAST,DESERT,FOREST,
		GRASSLAND,HILL,MOUNTAIN,SWAMP,UNDERDARK,
		UNDERWATER,URBAN;
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
}
