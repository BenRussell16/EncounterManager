package Creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Creatures.newCreature.Type.Subtype;
import Resources.Source;

public interface newCreature {
	public void constructor(String name, Size size, Type type, Subtype subtype, boolean isShapechanger,
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
	public Subtype getSubtype();
	public boolean isShapechanger();

	public List<Alignment> getAlignment();
		public default boolean isAlign(Alignment a){return getAlignment().contains(a);}
		public default String alignDescription(){
			if(getAlignment().contains(Alignment.UNALIGNED)){return "Unaligned";}
			
			boolean anyGood = getAlignment().contains(Alignment.LG) && getAlignment().contains(Alignment.NG) && getAlignment().contains(Alignment.CG);
			boolean anyEvil = getAlignment().contains(Alignment.LE) && getAlignment().contains(Alignment.NE) && getAlignment().contains(Alignment.CE);
			boolean anyLawful = getAlignment().contains(Alignment.LG) && getAlignment().contains(Alignment.LN) && getAlignment().contains(Alignment.LE);
			boolean anyChaotic = getAlignment().contains(Alignment.CG) && getAlignment().contains(Alignment.CN) && getAlignment().contains(Alignment.CE);
			boolean anyGEneutral = getAlignment().contains(Alignment.LN) && getAlignment().contains(Alignment.TN) && getAlignment().contains(Alignment.CN);
			boolean anyLCneutral = getAlignment().contains(Alignment.NG) && getAlignment().contains(Alignment.TN) && getAlignment().contains(Alignment.NE);

			boolean aGood = getAlignment().contains(Alignment.LG) || getAlignment().contains(Alignment.NG) || getAlignment().contains(Alignment.CG);
			boolean aEvil = getAlignment().contains(Alignment.LE) || getAlignment().contains(Alignment.NE) || getAlignment().contains(Alignment.CE);
			boolean aLawful = getAlignment().contains(Alignment.LG) || getAlignment().contains(Alignment.LN) || getAlignment().contains(Alignment.LE);
			boolean aChaotic = getAlignment().contains(Alignment.CG) || getAlignment().contains(Alignment.CN) || getAlignment().contains(Alignment.CE);
			boolean aGEneutral = getAlignment().contains(Alignment.LN) || getAlignment().contains(Alignment.TN) || getAlignment().contains(Alignment.CN);
			boolean aLCneutral = getAlignment().contains(Alignment.NG) || getAlignment().contains(Alignment.TN) || getAlignment().contains(Alignment.NE);

			if(anyGood && anyGEneutral && anyEvil){return "Any Alignment";}
			
			if(anyGood && anyGEneutral &&!aEvil){return "Any Non-Evil Alignment";}
			if(anyGood && !aGEneutral &&!aEvil){return "Any Good Alignment";}
			if(!aGood && anyGEneutral &&anyEvil){return "Any Non-Good Alignment";}
			if(!aGood && !aGEneutral &&anyEvil){return "Any Evil Alignment";}

			if(anyLawful && anyLCneutral &&!aChaotic){return "Any Non-Chaotic Alignment";}
			if(anyLawful && !aLCneutral &&!aChaotic){return "Any Lawful Alignment";}
			if(!aLawful && anyLCneutral &&anyChaotic){return "Any Non-Lawful Alignment";}
			if(!aLawful && !aLCneutral &&anyChaotic){return "Any Chaotic Alignment";}
			
			String builtString = "";
			boolean first = true;
			for(Alignment a:Alignment.values()){
				if(getAlignment().contains(a)){
					if(!first){builtString+=" or ";}//TODO tidy if theres ever more than 2.
					builtString+=a.toNiceString();
					first = false;
				}
			}
			return builtString;
		}
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
			else{return scoreToMod(getStats().get(s.standardStat));}
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
	public default String toXML() {
		String builtString = "<creature>\n";
		builtString += "\t<name>"+getName()+"</name>\n";
		builtString += "\t<size>"+getSize().toString()+"</size>\n";
		builtString += "\t<type>"+getType().toString();
			if(getSubtype()!=null){builtString+=","+getSubtype().toString();}
			builtString += ","+isShapechanger()+"</type>\n";
		builtString += "\t<align>";
			boolean first = true;
			for(Alignment a:getAlignment()){
				if(!first){builtString+=",";}
				builtString+=a.toString();
				first = false;
			}
			builtString+="</align>\n";
		builtString += "\t<cr>"+getCR()+"</cr>\n";
		builtString += "\t<ac>"+getAC()+"</ac>\n";
		builtString += "\t<hp>"+getHP()+"</hp>\n";
		builtString += "\t<speed>";
			first = true;
			for(Speeds s:getSpeeds().keySet()){
				if(!first){builtString+=",";}
				builtString+=s.toString()+" "+getSpeeds().get(s);
				first = false;
			}
			builtString += "</speed>\n";
		builtString += "\t<stats>";
			first = true;
			for(Stats s:getStats().keySet()){
				if(!first){builtString+=",";}
				builtString+=s.toString()+" "+getStats().get(s);
				first = false;
			}
			builtString += "</stats>\n";
		if(!getSaves().isEmpty()){builtString += "\t<saves>";
			first = true;
			for(Stats s:getSaves().keySet()){
				if(!first){builtString+=",";}
				builtString+=s.toString()+" "+getSaves().get(s);
				first = false;
			}
			builtString += "</saves>\n";}
		if(!getSkills().isEmpty()){builtString += "\t<skills>";
			first = true;
			for(Skills s:getSkills().keySet()){
				if(!first){builtString+=",";}
				builtString+=s.toString()+" "+getSkills().get(s);
				first = false;
			}
			builtString += "</skills>\n";}
		if(!getMultipliers().isEmpty()){builtString += "\t<multipliers>\n";
			for(DamageMultiplier dm:getMultipliers().keySet()){
				if(!getMultipliers().get(dm).isEmpty()){builtString += "\t\t<"+dm.toString()+">";
				first = true;
				for(DamageType d:getMultipliers().get(dm)){
					if(!first){builtString+=",";}
					builtString+=d.toString();
					first = false;
				}
				builtString += "</"+dm.toString()+">\n";}
			}
			builtString += "\t</multipliers>\n";}
		if(!getConditionImmunities().isEmpty()){builtString += "\t<conditions>";
			first = true;
			for(StatusCondition s:getConditionImmunities()){
				if(!first){builtString+=",";}
				builtString+=s.toString();
				first = false;
			}
			builtString += "</conditions>\n";}
		if(!getSenses().isEmpty()){builtString += "\t<senses>";
			first = true;
			for(Senses s:getSenses().keySet()){
				if(!first){builtString+=",";}
				builtString+=s.toString()+" "+getSenses().get(s);
				first = false;
			}
			builtString += "</senses>\n";}
		if(!getLanguages().isEmpty()){builtString += "\t<languages>";
			first = true;
			for(Languages l:getLanguages()){
				if(!first){builtString+=",";}
				builtString+=l.toString();
				first = false;
			}
			builtString += "</languages>\n";}
		builtString += "\t<regions>";
			first = true;
			for(Region r:getRegions()){
				if(!first){builtString+=",";}
				builtString+=r.toString();
				first = false;
			}
			builtString += "</regions>\n";
		if(getLegendaryResistances()>0 || getInnateCasting()!=null || getSpellcasting()!=null || !getPassives().isEmpty()){
			builtString += "\t<passives>\n";
				if(getLegendaryResistances()>0){builtString += "\t\t<legendaryresistance>"+getLegendaryResistances()+"</legendaryresistance>\n";}
				if(getInnateCasting()!=null){builtString += "\t\t<innatecasting>";
					builtString += getInnateCasting().getAbility().toString()+",";
					builtString += getInnateCasting().getToHit().toString()+",";
					builtString += getInnateCasting().getDC().toString()+",";
					builtString += getInnateCasting().getLevel().toString()+",";
					builtString += getInnateCasting().getFile().getAbsolutePath();
					builtString += "</innatecasting>\n";}
				if(getSpellcasting()!=null){builtString += "\t\t<spellcasting>";
					builtString += getSpellcasting().getAbility().toString()+",";
					builtString += getSpellcasting().getToHit().toString()+",";
					builtString += getSpellcasting().getDC().toString()+",";
					builtString += getSpellcasting().getLevel().toString()+",";
					builtString += getSpellcasting().getFile().getAbsolutePath();
					builtString += "</spellcasting>\n";}
				for(String p:getPassives().keySet()){
					builtString += "\t\t<passive>"+p+","+getPassives().get(p)+"</passive>\n";
				}
				builtString += "\t</passives>\n";}
		if(otherNotes()!=null){builtString += "\t<other>"+otherNotes()+"</other>\n";}
		builtString += "\t<actions>\n";
			if(getMultiattack()!=null){builtString += "\t\t<multiattack>"+getMultiattack()+"</multiattack>\n";}
			if(!getAttacks().isEmpty()){for(newAttack a:getAttacks()){
				builtString += "\t\t<attack>"+a.getName()+","+a.getToHit()
					+","+a.getShortRange()+","+a.getLongRange()+","+a.getDescription()+"</attack>\n";
			}}
			if(!getEffects().isEmpty()){for(newEffect e:getEffects()){
				builtString += "\t\t<effect>"+e.getName()+","+e.getLimit()+","+e.getDescription()+"</effect>\n";
			}}
			builtString += "\t</actions>\n";
		if(!getReactions().isEmpty()){builtString += "\t<reactions>\n";
			for(String r:getReactions().keySet()){
				builtString += "\t\t<reaction>"+r+","+getReactions().get(r)+"</reaction>\n";
			}
			builtString += "\t</reactions>\n";}
		if(getLegendaryActionCount()>0){builtString += "\t<legendaryactions>"+getLegendaryActionCount()+"\n";
			for(newLegendaryAction l:getLegendaryActions()){
				builtString += "\t\t<legendaryaction>"+l.getName()+","+l.getCost()+","+l.getDescription()+"</legendaryaction>\n";
			}
			builtString += "\t</legendaryactions>\n";}
		if(!getLairActions().isEmpty()){builtString += "\t<lair>\n";
			for(String l:getLairActions()){
				builtString += "\t\t<lairaction>"+l+"</lairaction>\n";
			}
			builtString += "\t</lair>\n";}
		builtString += "\t<source>";
			first = true;
			for(Source s:getSource()){
				if(!first){builtString+=",";}
				builtString+=s.toString();
				first = false;
			}
			builtString += "</source>\n";
		builtString += "</creature>\n";
		return builtString;
	}
	
	
	
	
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
		STR("Strength"),DEX("Deterity"),CON("Constitution"),
		INT("Intellegence"),WIS("Wisdom"),CHA("Charisma");
		private String niceFormat;
		private Stats(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
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
		FORCE,NECROTIC,RADIANT;
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