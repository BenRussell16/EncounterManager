package Creatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import Creatures.newCreature.Alignment;
import Creatures.newCreature.DamageMultiplier;
import Creatures.newCreature.DamageType;
import Creatures.newCreature.Languages;
import Creatures.newCreature.Region;
import Creatures.newCreature.Senses;
import Creatures.newCreature.Size;
import Creatures.newCreature.Skills;
import Creatures.newCreature.Speeds;
import Creatures.newCreature.Stats;
import Creatures.newCreature.StatusCondition;
import Creatures.newCreature.Type;
import Creatures.newCreature.Type.Subtype;
import Resources.Source;
import Spells.Spell;

public class newCreatureParser {
	private final List<Spell> spells;
	public newCreatureParser(List<Spell> spells){
		this.spells = spells;
	}
	public List<newCreature> Parse() {
		Scanner scan;
		List<newCreature> creatures = new ArrayList<newCreature>();
		try {
			scan = new Scanner(new File("Resources/CreatureList"));
			scan.useDelimiter("<|>|\n|\t| |,");
			while(scan.hasNext()) {
				if(scan.hasNext("creature")) {
					//Setting fields to store creature values.
					String name = "";
					Size size = null;
					Type type = null;
					Subtype subtype = null;
					boolean isShapechanger = false;
					
					List<Alignment> align = new ArrayList<Alignment>();
					double cr = 0;
					int hp = 0;
					int ac = 0;
					
					Map<Speeds,Integer> speed = new HashMap<Speeds,Integer>();
					Map<Stats,Integer> stats = new HashMap<Stats,Integer>();
					Map<Stats,Integer> saves = new HashMap<Stats,Integer>();
					Map<Skills,Integer> skills = new HashMap<Skills,Integer>();
					
					Map<DamageMultiplier,List<DamageType>> damageMultipliers = new HashMap<DamageMultiplier,List<DamageType>>();
					List<StatusCondition> conditionImmunities = new ArrayList<StatusCondition>();

					Map<Senses,Integer> senses = new HashMap<Senses,Integer>();
					List<Languages> languages = new ArrayList<Languages>();
					List<Region> regions = new ArrayList<Region>();
					
					int legendaryResistances = 0;
					newSpellcasting innate = null;
					newSpellcasting casting = null;
					Map<String,String> passives = new HashMap<String,String>();
					String otherNotes = null;
					
					String multiattack = null;
					List<newAttack> attacks = new ArrayList<newAttack>();
					List<newEffect> otherActions = new ArrayList<newEffect>();
					Map<String,String> reactions = new HashMap<String,String>();

					int legendaryActionCount = 0;
					List<newLegendaryAction> legendaryActions = new ArrayList<newLegendaryAction>();
					List<String> lairActions = new ArrayList<String>();

					List<Source> sources = new ArrayList<Source>();

					
					
					
					//Parse the creature.							//TODO - Label for parsing start
					while(!scan.hasNext("/creature")) {
						if(scan.hasNext("name")) {
							scan.next();
							while(!scan.hasNext("/name")) {
								if(name.length()>0) {name+=" ";}
								name+=scan.next();
							}
							scan.next();
						}
						if(scan.hasNext("size")) {
							scan.next();
							while(!scan.hasNext("/size")) {
								for(Size s:Size.values()) {
									if(scan.hasNext(s.toString())) {
										size = s;
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("type")) {
							scan.next();
							while(!scan.hasNext("/type")) {
								for(Type t:Type.values()) {
									if(scan.hasNext(t.toString())) {
										type = t;
										scan.next();
										if(t.hasSubtype()){
											String next = scan.next();
											if(next.length()>0){
												for(Subtype s:t.getSubtype(t)){
													if(next.equals(s.toString())){
														subtype = s;
													}
												}
											}
										}else{scan.next();}
										isShapechanger = scan.nextBoolean();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("align")) {
							scan.next();
							while(!scan.hasNext("/align")) {
								for(Alignment a:Alignment.values()) {
									if(scan.hasNext(a.name())) {
										align.add(a);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("cr")) {
							scan.next();
							cr = scan.nextDouble();
							scan.next();
						}
						if(scan.hasNext("ac")) {
							scan.next();
							ac = scan.nextInt();
							scan.next();
						}
						if(scan.hasNext("hp")) {
							scan.next();
							hp = scan.nextInt();
							scan.next();
						}
						if(scan.hasNext("speed")) {
							scan.next();
							while(!scan.hasNext("/speed")) {
								for(Speeds s:Speeds.values()) {
									if(scan.hasNext(s.toString())) {
										scan.next();
										speed.put(s, scan.nextInt());
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("stats")) {
							scan.next();
							while(!scan.hasNext("/stats")) {
								for(Stats s:Stats.values()) {
									if(scan.hasNext(s.toString())) {
										scan.next();
										stats.put(s, scan.nextInt());
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("saves")) {
							scan.next();
							while(!scan.hasNext("/saves")) {
								for(Stats s:Stats.values()) {
									if(scan.hasNext(s.toString())) {
										scan.next();
										saves.put(s, scan.nextInt());
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("skills")) {
							scan.next();
							while(!scan.hasNext("/skills")) {
								for(Skills s:Skills.values()) {
									if(scan.hasNext(s.toString())) {
										scan.next();
										skills.put(s, scan.nextInt());
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("multipliers")) {
							scan.next();
							while(!scan.hasNext("/multipliers")) {
								for(DamageMultiplier dm:DamageMultiplier.values()) {
									if(scan.hasNext(dm.toString())) {
										damageMultipliers.put(dm, new ArrayList<DamageType>());
										scan.next();
										while(!scan.hasNext("/"+dm.toString())){
											for(DamageType d:DamageType.values()){
												if(scan.hasNext(d.toString())){
													scan.next();
													damageMultipliers.get(dm).add(d);
												}
											}
										}
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("conditions")) {
							scan.next();
							while(!scan.hasNext("/conditions")) {
								for(StatusCondition s:StatusCondition.values()) {
									if(scan.hasNext(s.toString())) {
										conditionImmunities.add(s);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("senses")) {
							scan.next();
							while(!scan.hasNext("/senses")) {
								for(Senses s:Senses.values()) {
									if(scan.hasNext(s.toString())) {
										scan.next();
										senses.put(s, scan.nextInt());
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("languages")) {
							scan.next();
							while(!scan.hasNext("/languages")) {
								for(Languages l:Languages.values()) {
									if(scan.hasNext(l.toString())) {
										languages.add(l);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("regions")) {
							scan.next();
							while(!scan.hasNext("/regions")) {
								for(Region r:Region.values()) {
									if(scan.hasNext(r.toString())) {
										regions.add(r);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("passives")) {
							scan.next();
							while(!scan.hasNext("/passives")){
								if(scan.hasNext("legendaryresistance")){
									scan.next();
									legendaryResistances = scan.nextInt();
								}else if(scan.hasNext("innatecasting") || scan.hasNext("spellcasting")){
									Stats curAbility = null;
									Integer curToHit = null;
									Integer curDC = null;
									Integer curLevel = null;
									String curFile;
									scan.next();
									Pattern oldDelimiter = scan.delimiter();
									scan.useDelimiter(",");
									for(Stats s:Stats.values()){
										if(scan.hasNext(s.toString())){
											curAbility = s;
											scan.next();
										}
									}
									String curIntString = scan.next();
									if(curIntString.length()>0){curToHit = Integer.parseInt(curIntString);}
									curIntString = scan.next();
									if(curIntString.length()>0){curDC = Integer.parseInt(curIntString);}
									curIntString = scan.next();
									if(curIntString.length()>0){curLevel = Integer.parseInt(curIntString);}
									scan.useDelimiter(">|<");
									curFile = scan.next().substring(1);//Skipping the comma
									scan.useDelimiter(oldDelimiter);
									File file = new File(curFile);
									newSpellcasting curSpellBook = new newSpellcasting(file, curAbility, curToHit, curDC, curLevel, spells);
									if(scan.hasNext("/innatecasting")){innate = curSpellBook;
									}else{casting = curSpellBook;}//Assumes one of the 2.
								}else if(scan.hasNext("passive")){
									String PName;
									String PDesc;
									scan.next();
									Pattern oldDelimiter = scan.delimiter();
									scan.useDelimiter(",");
									PName = scan.next();
									scan.useDelimiter(">|<");
									PDesc = scan.next().substring(1);//Skipping the comma
									scan.useDelimiter(oldDelimiter);
									passives.put(PName, PDesc);
								}
								scan.next();
							}
						}
						if(scan.hasNext("other")) {
							scan.next();
							Pattern oldDelimiter = scan.delimiter();
							scan.useDelimiter(">|<");
							otherNotes = scan.next();
							scan.useDelimiter(oldDelimiter);
							scan.next();
						}
						if(scan.hasNext("actions")) {
							scan.next();
							while(!scan.hasNext("/actions")){
								if(scan.hasNext("multiattack")){
									scan.next();
									Pattern oldDelimiter = scan.delimiter();
									scan.useDelimiter(">|<");
									multiattack = scan.next();
									scan.useDelimiter(oldDelimiter);
								}else if(scan.hasNext("attack")){
									String AName;
									int AToHit;
									int AShortRange;
									Integer ALongRange = null;
									String ADesc;
									scan.next();
									Pattern oldDelimiter = scan.delimiter();
									scan.useDelimiter(",");
									AName = scan.next();
									scan.useDelimiter(oldDelimiter);
									AToHit = scan.nextInt();
									AShortRange = scan.nextInt();
									String LRString = scan.next();
									if(LRString.length()>0){ALongRange = Integer.parseInt(LRString);}
									scan.useDelimiter(">|<");
									ADesc = scan.next().substring(1);//Skipping the comma
									scan.useDelimiter(oldDelimiter);
									attacks.add(new newAttack(AName, AToHit, AShortRange, ALongRange, ADesc));
								} else if(scan.hasNext("effect")){
									String EName;
									String ELimit;
									String EDesc;
									scan.next();
									Pattern oldDelimiter = scan.delimiter();
									scan.useDelimiter(",");
									EName = scan.next();
									ELimit = scan.next();
									scan.useDelimiter(">|<");
									EDesc = scan.next().substring(1);//Skipping the comma
									scan.useDelimiter(oldDelimiter);
									otherActions.add(new newEffect(EName, ELimit, EDesc));
								}
								scan.next();
							}
						}
						if(scan.hasNext("reactions")) {
							scan.next();
							while(!scan.hasNext("/reactions")){
								if(scan.hasNext("reaction")){
									String RName;
									String RDesc;
									scan.next();
									Pattern oldDelimiter = scan.delimiter();
									scan.useDelimiter(",");
									RName = scan.next();
									scan.useDelimiter(">|<");
									RDesc = scan.next().substring(1);//Skipping the comma
									scan.useDelimiter(oldDelimiter);
									reactions.put(RName, RDesc);
								}
								scan.next();
							}
						}
						if(scan.hasNext("legendaryactions")) {
							scan.next();
							legendaryActionCount = scan.nextInt();
							while(!scan.hasNext("/legendaryactions")){
								if(scan.hasNext("legendaryaction")){
									String LAName;
									int LACost;
									String LADesc;
									scan.next();
									Pattern oldDelimiter = scan.delimiter();
									scan.useDelimiter(",");
									LAName = scan.next();
									scan.useDelimiter(oldDelimiter);
									LACost = scan.nextInt();
									scan.useDelimiter(">|<");
									LADesc = scan.next().substring(1);//Skipping the comma
									scan.useDelimiter(oldDelimiter);
									legendaryActions.add(new newLegendaryAction(LAName, LACost, LADesc));
								}
								scan.next();
							}
						}
						if(scan.hasNext("lair")) {
							scan.next();
							while(!scan.hasNext("/lair")){
								if(scan.hasNext("lairaction")){
									scan.next();
									Pattern oldDelimiter = scan.delimiter();
									scan.useDelimiter(">|<");
									lairActions.add(scan.next());
									scan.useDelimiter(oldDelimiter);
								}
								scan.next();
							}
						}
						if(scan.hasNext("source")) {
							scan.next();
							while(!scan.hasNext("/source")) {
								for(Source s:Source.values()) {
									if(scan.hasNext(s.name())) {
										sources.add(s);
										scan.next();
									}
								}
							}
							scan.next();
						}
						else {/*System.out.print(*/scan.next()/*)*/;}
					}
					scan.next();
					//Creature fully parsed here
					
					
					
					
					
					newCreature current = new newCreature() {					//TODO - label for creature creation start.
						String name;
						Size size;
						Type type;
						Subtype subtype;
						boolean isShapechanger;
						List<Alignment> align;
						double cr;
						int hp;
						int ac;
						Map<Speeds,Integer> speed;
						Map<Stats,Integer> stats;
						Map<Stats,Integer> saves;
						Map<Skills,Integer> skills;
						Map<DamageMultiplier,List<DamageType>> damageMultipliers;
						List<StatusCondition> conditionImmunities;
						Map<Senses,Integer> senses;
						List<Languages> languages;
						List<Region> regions;
						int legendaryResistances;
						newSpellcasting innate;
						newSpellcasting casting;
						Map<String,String> passives;
						String otherNotes;
						String multiattack;
						List<newAttack> attacks;
						List<newEffect> otherActions;
						Map<String,String> reactions;
						int legendaryActionCount;
						List<newLegendaryAction> legendaryActions;
						List<String> lairActions;
						List<Source> sources;

						@Override public void constructor(String name, Size size, Type type, Subtype subtype,
								boolean isShapechanger, List<Alignment> align, double cr, int hp, int ac,
								Map<Speeds, Integer> speed, Map<Stats, Integer> stats, Map<Stats, Integer> saves,
								Map<Skills, Integer> skills, Map<DamageMultiplier, List<DamageType>> damageMultipliers,
								List<StatusCondition> conditionImmunities, Map<Senses, Integer> senses,
								List<Languages> languages, List<Region> regions, int legendaryResistances,
								newSpellcasting innate, newSpellcasting casting, Map<String, String> passives,
								String otherNotes, String multiattack, List<newAttack> attacks,
								List<newEffect> otherActions, Map<String, String> reactions, int legendaryActionCount,
								List<newLegendaryAction> legendaryActions, List<String> lairActions,
								List<Source> sources) {
							this.name=name;
							this.size=size;
							this.type=type;
							this.subtype=subtype;
							this.isShapechanger=isShapechanger;
							this.align=align;
							this.cr=cr;
							this.hp=hp;
							this.ac=ac;
							this.speed=speed;
							this.stats=stats;
							this.saves=saves;
							this.skills=skills;
							this.damageMultipliers=damageMultipliers;
							this.conditionImmunities=conditionImmunities;
							this.senses=senses;
							this.languages=languages;
							this.regions=regions;
							this.legendaryResistances=legendaryResistances;
							this.innate=innate;
							this.casting=casting;
							this.passives=passives;
							this.otherNotes=otherNotes;
							this.multiattack=multiattack;
							this.attacks=attacks;
							this.otherActions=otherActions;
							this.reactions=reactions;
							this.legendaryActionCount=legendaryActionCount;
							this.legendaryActions=legendaryActions;
							this.lairActions=lairActions;
							this.sources=sources;
						}

						@Override public String getName() {return name;}
						@Override public Size getSize() {return size;}
						@Override public Type getType() {return type;}
						@Override public Subtype getSubtype() {return subtype;}
						@Override public boolean isShapechanger() {return isShapechanger;}
						@Override public List<Alignment> getAlignment() {return align;}
						@Override public double getCR() {return cr;}
						@Override public int getHP() {return hp;}
						@Override public int getAC() {return ac;}
						@Override public Map<Speeds, Integer> getSpeeds() {return speed;}
						@Override public Map<Stats, Integer> getStats() {return stats;}
						@Override public Map<Stats, Integer> getSaves() {return saves;}
						@Override public Map<Skills, Integer> getSkills() {return skills;}
						@Override public Map<DamageMultiplier, List<DamageType>> getMultipliers() {return damageMultipliers;}
						@Override public List<StatusCondition> getConditionImmunities() {return conditionImmunities;}
						@Override public Map<Senses, Integer> getSenses() {return senses;}
						@Override public List<Languages> getLanguages() {return languages;}
						@Override public List<Region> getRegions() {return regions;}
						@Override public int getLegendaryResistances() {return legendaryResistances;}
						@Override public newSpellcasting getInnateCasting() {return innate;}
						@Override public newSpellcasting getSpellcasting() {return casting;}
						@Override public Map<String, String> getPassives() {return passives;}
						@Override public String otherNotes() {return otherNotes;}
						@Override public String getMultiattack() {return multiattack;}
						@Override public List<newAttack> getAttacks() {return attacks;}
						@Override public List<newEffect> getEffects() {return otherActions;}
						@Override public Map<String, String> getReactions() {return reactions;}
						@Override public int getLegendaryActionCount() {return legendaryActionCount;}
						@Override public List<newLegendaryAction> getLegendaryActions() {return legendaryActions;}
						@Override public List<String> getLairActions() {return lairActions;}
						@Override public List<Source> getSource() {return sources;}
						
						@Override public String toString(){
							String builtString = getName()+"\n";
							builtString += size.toNiceString()+" "+type.toNiceString();
							if(getSubtype()!=null || isShapechanger()){
								builtString += " (";
								if(getSubtype()!=null){builtString += getSubtype().toNiceString();}
								if(getSubtype()!=null && isShapechanger()){builtString += ", ";}
								if(isShapechanger()){builtString += "shapechanger";}
								builtString += ")";
							}
							builtString += ", "+alignDescription()+"\n";
							builtString += "--------------------------------------------------------------\n";

							builtString += "Armour Class: "+getAC()+"\n";
							builtString += "Hit Points: "+getHP()+"\n";
							builtString += "Speed: "+getSpeeds().get(Speeds.WALK)+" ft";
							for(Speeds s:getSpeeds().keySet()){if(s!=Speeds.WALK){
								builtString += ", "+s.toNiceString()+" "+getSpeeds().get(s)+" ft";
							}}
							builtString += "\n";
							builtString += "--------------------------------------------------------------\n";
							
							for(Stats s:Stats.values()){
								builtString += s.toString()+" "+getStats().get(s)+"(";
								if(newCreature.scoreToMod(getStats().get(s))>=0){builtString+="+";}
								builtString += newCreature.scoreToMod(getStats().get(s))+")\t";
							}
							builtString += "\n";
							builtString += "--------------------------------------------------------------\n";

							if(!getSaves().isEmpty()){
								builtString += "Saving Throws: ";
								boolean first = true;
								for(Stats s:getSaves().keySet()){
									if(!first){builtString += ",";}
									builtString += s.toString()+" ";
									if(getSaves().get(s)>=0){builtString += "+";}
									builtString += getSaves().get(s);
									first = false;
								}
								builtString += "\n";
							}
							if(!getSkills().isEmpty()){
								builtString += "Skills: ";
								boolean first = true;
								for(Skills s:getSkills().keySet()){
									if(!first){builtString += ",";}
									builtString += s.toString()+" ";
									if(getSkills().get(s)>=0){builtString += "+";}
									builtString += getSkills().get(s);
									first = false;
								}
								builtString += "\n";
							}
							if(!getVulnerabilities().isEmpty()){
								builtString += "Damage Vulnerabilities: ";
								boolean first = true;
								for(DamageType d:getVulnerabilities()){
									if(!first){builtString += ",";}
									builtString += d.toNiceString();
									first = false;
								}
								builtString += "\n";
							}
							if(!getResistances().isEmpty()){
								builtString += "Damage Resistances: ";
								boolean first = true;
								for(DamageType d:getResistances()){
									if(!first){builtString += ",";}
									builtString += d.toNiceString();
									first = false;
								}
								builtString += "\n";
							}
							if(!getImmunities().isEmpty()){
								builtString += "Damage Immunities: ";
								boolean first = true;
								for(DamageType d:getImmunities()){
									if(!first){builtString += ",";}
									builtString += d.toNiceString();
									first = false;
								}
								builtString += "\n";
							}
							if(!getConditionImmunities().isEmpty()){
								builtString += "Condition Immunities: ";
								boolean first = true;
								for(StatusCondition s:getConditionImmunities()){
									if(!first){builtString += ",";}
									builtString += s.toNiceString();
									first = false;
								}
								builtString += "\n";
							}
							if(!getSenses().isEmpty()){
								builtString += "Senses: ";
								boolean first = true;
								for(Senses s:getSenses().keySet()){
									if(!first){builtString += ",";}
									builtString += s.toString()+" "+ getSkills().get(s)+" ft";
									first = false;
								}
								if(!first){builtString += ", ";}
								builtString += "Passive Perception "+getSkillMod(Skills.PERCEPTION)+"\n";
							}
							if(!getLanguages().isEmpty()){
								builtString += "Languages: ";
								boolean first = true;
								for(Languages l:getLanguages()){
									if(!first){builtString += ",";}
									builtString += l.toNiceString();
									first = false;
								}
								builtString += "\n";
							}
							builtString += "Challenge: "+getCR()+" ("+getXP()+" XP)\n";
							builtString += "--------------------------------------------------------------\n";
							
							if(getLegendaryResistances()>0){
								builtString += "Legendary Resistance ("+getLegendaryResistances()+"/Day). ";
								builtString += "If this creature fails a saving throw, it can choose to succeed instead.\n";
							}
							if(getInnateCasting()!=null){
								builtString += "Innate Spellcasting.";
								builtString += "This creatures spellcasting ability is "+getInnateCasting().getAbility().toNiceString();
								if(getInnateCasting().getDC()!=null || getInnateCasting().getToHit()!=null){
									builtString += " (";
									if(getInnateCasting().getDC()!=null){
										builtString += "spell save DC "+getInnateCasting().getDC();
									}
									if(getInnateCasting().getDC()!=null && getInnateCasting().getToHit()!=null){
										builtString += ", ";
									}
									if(getInnateCasting().getToHit()!=null){
										if(getInnateCasting().getToHit()>=0){builtString += "+";}
										builtString += getInnateCasting().getToHit()+" to hit with spell attacks";
									}
									builtString += ")";
								}
								builtString += ". This creature can innately cast the following spells, ";
								builtString += "requiring no material components:\n";
								builtString += getInnateCasting().getSpellList().toString()+"\n";
							}
							if(getSpellcasting()!=null){
								builtString += "Spellcasting.";
								builtString += "This creature is a ";
								if(getSpellcasting().getLevel()==1){builtString += "1st";}
								else if(getSpellcasting().getLevel()==1){builtString += "2nd";}
								else if(getSpellcasting().getLevel()==1){builtString += "3rd";}
								else{builtString += getSpellcasting().getLevel()+"th";}
								builtString += "-level spellcaster.";
								builtString += "Its spellcasting ability is "+getSpellcasting().getAbility().toNiceString();
								if(getSpellcasting().getDC()!=null || getSpellcasting().getToHit()!=null){
									builtString += " (";
									if(getSpellcasting().getDC()!=null){
										builtString += "spell save DC "+getSpellcasting().getDC();
									}
									if(getSpellcasting().getDC()!=null && getSpellcasting().getToHit()!=null){
										builtString += ", ";
									}
									if(getSpellcasting().getToHit()!=null){
										if(getSpellcasting().getToHit()>=0){builtString += "+";}
										builtString += getSpellcasting().getToHit()+" to hit with spell attacks";
									}
									builtString += ")";
								}
								builtString += ". This creature has the following spells prepared:\n";
								builtString += getSpellcasting().getSpellList().toString()+"\n";
							}
							for(String p:getPassives().keySet()){
								builtString += p+". "+getPassives().get(p)+"\n";
							}
							builtString += "--------------------------------------------------------------\n";

							if(!getActions().isEmpty()){builtString += "Actions\n";}
							for(Object action:getActions()){
								builtString += action.toString()+"\n";
							}
							builtString += "--------------------------------------------------------------\n";
							
							if(!getReactions().isEmpty()){builtString += "Reactions\n";}
							for(String r:getReactions().keySet()){
								builtString += r+". "+getReactions().get(r)+"\n";
							}
							builtString += "--------------------------------------------------------------\n";
							
							if(!getLegendaryActions().isEmpty()){
								builtString += "Legendary Actions\n";
								builtString += "This creature can take "+getLegendaryActionCount()+" legendary actions, choosing from the options below. ";
								builtString += "Only one legendary action option can be used at a time and only at the end of another creatures turn.";
								builtString += "This creature regains spent legendary actions at the start of it's turn.\n";
							}
							for(newLegendaryAction la:getLegendaryActions()){
								builtString += la.toString()+"\n";
							}
							builtString += "--------------------------------------------------------------\n";
							
							if(!getLairActions().isEmpty()){
								builtString += "Lair Actions\n";
								builtString += "On initiative count 20 (losing initiative ties), ";
								builtString += "this creature takes a lair action to cause one of the following effects; ";
								builtString += "this creature can't use the same effect two rounds in a row:\n";
							}
							builtString += "--------------------------------------------------------------\n";

							builtString += "Regions: ";
							boolean first = true;
							for(Region r:getRegions()){
								if(!first){builtString += ",";}
								builtString += r.toNiceString();
								first = false;
							}
							builtString += "\n";
							
							return builtString;
						}
					};
					current.constructor(name, size, type, subtype, isShapechanger,
							align,cr,hp,ac,
							speed,stats,saves,skills,
							damageMultipliers,conditionImmunities,
							senses,languages,regions,
							legendaryResistances,innate,casting,passives,otherNotes,
							multiattack,attacks,otherActions,reactions,
							legendaryActionCount,legendaryActions,lairActions,
							sources);
					creatures.add(current);
				}
				else {/*System.out.println(*/scan.next()/*)*/;}//Skip to next creature
			}//where parsing ends
			
			
			
			
			scan.close();
			System.out.println("Creature parsing complete");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return creatures;
	}
}
