package src.Creatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import Resources.Area;
import Resources.Source;
import src.Creatures.Creature.DamageType;


public class CreatureParser {
	public List<Creature> Parse() {
		Scanner scan;
		List<Creature> creatures = new ArrayList<Creature>();
		try {
			scan = new Scanner(new File("Resources/CreatureList"));
			scan.useDelimiter("<|>|\n|\t| |,");
			while(scan.hasNext()) {
				if(scan.hasNext("creature")) {
					String name="";
					Creature.Size size = null;
					Creature.Type type = null;
					Creature.Alignment align = null;
					int ac = -1;
					int hp = -1;
					Map<Creature.Speeds,Integer> speed = new HashMap<Creature.Speeds,Integer>();
					int[] stats = new int[6];
					Map<Creature.Stats,Integer> saves = new HashMap<Creature.Stats,Integer>();
					Map<Creature.Skills,Integer> skills = new HashMap<Creature.Skills,Integer>();
					Map<DamageType, Creature.DamageMultiplier> damageMultipliers = new HashMap<DamageType, Creature.DamageMultiplier>();
					List<Creature.StatusCondition> conditionImmunities = new ArrayList<Creature.StatusCondition>();
					Map<Creature.Senses,Integer> senses = new HashMap<Creature.Senses,Integer>();
					List<Creature.Languages> languages = new ArrayList<Creature.Languages>();
					double cr = -1;
					int legendaryResistances = 0;
					int legendaryActions = 0;
					int regen = 0;
					List<DamageType> regenBlocks = new ArrayList<DamageType>();
					List<String> passives = new ArrayList<String>();
					List<Action> actions = new ArrayList<Action>();
					List<Source> sources = new ArrayList<Source>();
					scan.next();
					
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
							for(Creature.Size s:Creature.Size.values()) {
								if(scan.hasNext(s.toString().toLowerCase())) {size = s;}
							}
							scan.next();
							scan.next();
						}
						if(scan.hasNext("type")) {
							scan.next();
							for(Creature.Type t:Creature.Type.values()) {
								if(scan.hasNext(t.toString().toLowerCase())) {type = t;}
							}
							scan.next();
							scan.next();
						}
						if(scan.hasNext("alignment")) {
							String strAlign = "";
							scan.next();
							while(!scan.hasNext("/alignment")) {
								if(strAlign.length()>0) {strAlign+=" ";}
								strAlign+=scan.next();
							}
							if(strAlign.equals("lawful good")) {align = Creature.Alignment.LG;}
							else if(strAlign.equals("lawful neutral")) {align = Creature.Alignment.LN;}
							else if(strAlign.equals("lawful evil")) {align = Creature.Alignment.LE;}
							else if(strAlign.equals("neutral good")) {align = Creature.Alignment.NG;}
							else if(strAlign.equals("neutral")) {align = Creature.Alignment.TN;}
							else if(strAlign.equals("neutral evil")) {align = Creature.Alignment.NE;}
							else if(strAlign.equals("chaotic good")) {align = Creature.Alignment.CG;}
							else if(strAlign.equals("chaotic neutral")) {align = Creature.Alignment.CN;}
							else if(strAlign.equals("chaotic evil")) {align = Creature.Alignment.CE;}
							else if(strAlign.equals("unaligned")) {align = Creature.Alignment.UNALIGNED;}
							else {System.out.println("Invalid alignment: "+strAlign);}
							scan.next();
						}
						if(scan.hasNext("ac")) {
							scan.next();
							if(scan.hasNextInt()) {ac = scan.nextInt();}
							else {System.out.println("Needed int for AC, instead got "+scan.next());}
							scan.next();
						}
						if(scan.hasNext("hp")) {
							scan.next();
							if(scan.hasNextInt()) {hp = scan.nextInt();}
							else {System.out.println("Needed int for HP, instead got "+scan.next());}
							scan.next();
						}
						if(scan.hasNext("speed")) {
							scan.next();
							if(scan.hasNextInt()) {speed.put(Creature.Speeds.WALK, scan.nextInt());}
							else {System.out.println("Needed int for speed, instead got "+scan.next());}
							while(!scan.hasNext("/speed")) {
								for(Creature.Speeds s:Creature.Speeds.values()) {
									if(scan.hasNext(s.toString().toLowerCase())) {
										scan.next();
										if(scan.hasNextInt()) {speed.put(s, scan.nextInt());}
										else {System.out.println("Expected a value for speed "+s.toString().toLowerCase());}
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("stats")) {
							scan.next();
							int i=0;
							while(scan.hasNextInt()) {
								stats[i] = scan.nextInt();
								i++;
							}
							if(i!=6) {System.out.println("Wrong number of stats input, recieved "+i);}
							scan.next();
						}
						if(scan.hasNext("saves")) {
							scan.next();
							while(!scan.hasNext("/saves")) {
								for(Creature.Stats s:Creature.Stats.values()) {
									if(scan.hasNext(s.toString().toLowerCase())) {
										scan.next();
										if(scan.hasNextInt()) {saves.put(s, scan.nextInt());}
										else {System.out.println("Expected a value for save "+s.toString().toLowerCase());}
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("skills")) {
							scan.next();
							while(!scan.hasNext("/skills")) {
								for(Creature.Skills s:Creature.Skills.values()) {
									if(scan.hasNext(s.toString().toLowerCase())) {
										scan.next();
										if(scan.hasNextInt()) {skills.put(s, scan.nextInt());}
										else {System.out.println("Expected a value for skill "+s.toString().toLowerCase());}
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("vulnerabilities")||scan.hasNext("resistances")||scan.hasNext("immunities")||scan.hasNext("healed")) {
							String tag = scan.next();
							Creature.DamageMultiplier mult = null;
							if(tag.equals("vulnerabilities")) {mult = Creature.DamageMultiplier.VULNERABILITY;}
							else if(tag.equals("resistances")) {mult = Creature.DamageMultiplier.RESISTANCE;}
							else if(tag.equals("immunities")) {mult = Creature.DamageMultiplier.IMMUNITY;}
							else if(tag.equals("healed")) {mult = Creature.DamageMultiplier.HEALING;}
							else {System.out.println("Unknown multiplier: "+tag);}
							while(!scan.hasNext("/"+tag)) {
								for(DamageType d:DamageType.values()) {
									if(scan.hasNext(d.toString().toLowerCase())) {
										damageMultipliers.put(d, mult);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("conditions")) {
							scan.next();
							while(!scan.hasNext("/conditions")) {
								for(Creature.StatusCondition c:Creature.StatusCondition.values()) {
									if(scan.hasNext(c.toString().toLowerCase())) {
										conditionImmunities.add(c);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("senses")) {
							scan.next();
							while(!scan.hasNext("/senses")) {
								for(Creature.Senses s:Creature.Senses.values()) {
									if(scan.hasNext(s.toString().toLowerCase())) {
										scan.next();
										if(scan.hasNextInt()) {senses.put(s, scan.nextInt());}
										else {System.out.println("Expected a range for sense "+s.toString().toLowerCase());}
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("languages")) {
							scan.next();
							while(!scan.hasNext("/languages")) {
								for(Creature.Languages l:Creature.Languages.values()) {
									if(scan.hasNext(l.toString().toLowerCase())) {
										languages.add(l);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("cr")) {
							scan.next();
							if(scan.hasNextDouble()) {cr = scan.nextDouble();}
							else {System.out.println("Needed double for CR, instead got "+scan.next());}
							scan.next();
						}
						if(scan.hasNext("legendaryresistance")) {
							scan.next();
							if(scan.hasNextInt()) {legendaryResistances = scan.nextInt();}
							else {System.out.println("Needed int for legendary resistances, instead got "+scan.next());}
							scan.next();
						}
						if(scan.hasNext("regen")) {
							scan.next();
							regen = scan.nextInt();
							while(!scan.hasNext("/regen")) {
								for(DamageType d:DamageType.values()) {
									if(scan.hasNext(d.toString().toLowerCase())) {
										regenBlocks.add(d);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("passive")) {
							scan.next();
							Pattern delimiter = scan.delimiter();
							scan.useDelimiter(">|<");
							passives.add(scan.next());
							scan.useDelimiter(delimiter);
							scan.next();
						}
						if(scan.hasNext("actions")||scan.hasNext("bonus")||scan.hasNext("free")||scan.hasNext("legendaryactions")||scan.hasNext("lair")) {
							String tag = scan.next();
							Action.Time time = null;
							if(tag.equals("actions")) {time = Action.Time.ACTION;}
							else if(tag.equals("bonus")) {time = Action.Time.BONUS;}
							else if(tag.equals("free")) {time = Action.Time.FREE;}
							else if(tag.equals("legendaryactions")) {time = Action.Time.LEGENDARY;}
							else if(tag.equals("lair")) {time = Action.Time.LAIR;}
							else {System.out.println("Unknown duration: "+tag);}
							scan.next();
							scan.next();
							scan.next();
							if(time == Action.Time.LEGENDARY) {legendaryActions = scan.nextInt();}
							scan.next();
							while(!scan.hasNext("/"+tag)) {
								int legendaryCost = 0;
								if(time == Action.Time.LEGENDARY && scan.hasNextInt()) {legendaryCost = scan.nextInt();}
								if(scan.hasNext("attack")) {
									scan.next();
									Pattern delimiter = scan.delimiter();
									scan.useDelimiter(">|,");
									String actionName = scan.next();
									scan.useDelimiter(delimiter);
									int recharge = -1;
									int limit = -1;
									if(scan.hasNext("recharge")) {
										scan.next();
										recharge = scan.nextInt();
										scan.next();
									}else if(scan.hasNext("limit")) {
										scan.next();
										limit = scan.nextInt();
										scan.next();
									}
									int attackBonus = scan.nextInt();
									int reach = scan.nextInt();
									String damage = "";
									while(!scan.hasNext("/attack")) {damage+=scan.next()+" ";}
									scan.next();
									Action newAction = new Attack(actionName, recharge, limit, time, damage, reach, attackBonus);
									if(time == Action.Time.LEGENDARY) {newAction = new LegendaryAction(newAction, legendaryCost);}
									actions.add(newAction);
								}
								else if(scan.hasNext("effect")) {
									scan.next();
									Pattern delimiter = scan.delimiter();
									scan.useDelimiter(">|,");
									String actionName = scan.next();
									scan.useDelimiter(delimiter);
									int recharge = -1;
									int limit = -1;
									if(scan.hasNext("recharge")) {
										scan.next();
										recharge = scan.nextInt();
									}else if(scan.hasNext("limit")) {
										scan.next();
										limit = scan.nextInt();
									}
									Area shape = null;
									int area = 0;
									int secondaryArea = 0;
									for(Area s:Area.values()) {if(scan.hasNext(s.toString().toLowerCase())) {shape = s;}}
									scan.next();
									if(scan.hasNextInt() && shape!=Area.SINGLE) {area = scan.nextInt();}
									if(scan.hasNextInt() && shape==Area.CYLINDER) {secondaryArea = scan.nextInt();}
									int range = -1;
									if(scan.hasNextInt()) {range = scan.nextInt();}
									scan.next();
									int dc=-1;
									if(scan.hasNextInt()) {dc = scan.nextInt();}
									Creature.Stats save = null;
									for(Creature.Stats s:Creature.Stats.values()) {if(scan.hasNext(s.toString().toLowerCase())) {save = s;}}
									scan.next();
									scan.next();
									scan.useDelimiter(">|<");
									String effect = scan.next();
									scan.useDelimiter(delimiter);
									scan.next();
									Action newAction = new Effect(actionName, recharge, limit, time, effect, range, area, secondaryArea, shape, dc, save);
									if(time == Action.Time.LEGENDARY) {newAction = new LegendaryAction(newAction, legendaryCost);}
									actions.add(newAction);
								}
								else if(scan.hasNext("buff")) {
									scan.next();
									Pattern delimiter = scan.delimiter();
									scan.useDelimiter(">|,");
									String actionName = scan.next();
									scan.useDelimiter(delimiter);
									int recharge = -1;
									int limit = -1;
									scan.next();
									if(scan.hasNext("recharge")) {
										scan.next();
										recharge = scan.nextInt();
										scan.next();
									}else if(scan.hasNext("limit")) {
										scan.next();
										limit = scan.nextInt();
										scan.next();
									}
									Area shape = null;
									int area = 0;
									for(Area s:Area.values()) {if(scan.hasNext(s.toString().toLowerCase())) {shape = s;}}
									scan.next();
									if(scan.hasNextInt()) {area = scan.nextInt();}
									String effect = "";
									while(!scan.hasNext("/buff")) {effect+=scan.next()+" ";}
									scan.next();
									Action newAction = new Buff(actionName, recharge, limit, time, shape, area, effect);
									if(time == Action.Time.LEGENDARY) {newAction = new LegendaryAction(newAction, legendaryCost);}
									actions.add(newAction);
								}
								else if(scan.hasNext("multi")) {
									scan.next();
									int recharge = -1;
									int limit = -1;
									if(scan.hasNext("recharge")) {
										scan.next();
										recharge = scan.nextInt();
										scan.next();
									}else if(scan.hasNext("limit")) {
										scan.next();
										limit = scan.nextInt();
										scan.next();
									}
									Pattern delimiter = scan.delimiter();
									scan.useDelimiter(">|<");
									String content = scan.next();
									scan.useDelimiter(delimiter);
									scan.next();
									Action newAction = new Multiattack("Multiattack: ", recharge, limit, time, content);
									if(time == Action.Time.LEGENDARY) {newAction = new LegendaryAction(newAction, legendaryCost);}
									actions.add(newAction);
								}
								else {/*System.out.print(*/scan.next()/*)*/;}
							}
							scan.next();
						}
						if(scan.hasNext("source")) {
							scan.next();
							while(!scan.hasNext("/source")) {
								for(Source s:Source.values()) {
									if(scan.hasNext(s.toString().toLowerCase())) {
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
//					System.out.println();
					//creature fully parsed here
					Creature current = new Creature() {
						String name="";
						Size size = null;
						Type type = null;
						Alignment align = null;
						int ac = -1;
						int hp = -1;
						Map<Creature.Speeds,Integer> speed = new HashMap<Creature.Speeds,Integer>();
						int[] stats = new int[6];
						Map<Creature.Stats,Integer> saves = new HashMap<Creature.Stats,Integer>();
						Map<Skills,Integer> skills = new HashMap<Skills,Integer>();
						Map<DamageType, Creature.DamageMultiplier> damageMultipliers = new HashMap<DamageType, Creature.DamageMultiplier>();
						List<Creature.StatusCondition> conditionImmunities = new ArrayList<Creature.StatusCondition>();
						Map<Senses,Integer> senses = new HashMap<Senses,Integer>();
						List<Languages> languages = new ArrayList<Languages>();
						double cr = -1;
						int legendaryResistances = 0;
						int regen = 0;
						List<DamageType> regenBlocks = new ArrayList<DamageType>();
						List<String> passives = new ArrayList<String>();
						List<Action> actions = new ArrayList<Action>();
						int legendaryActions = 0;
						List<Source> sources = new ArrayList<Source>();
						
						public void constructor(String name, Size size, Type type, Alignment align, int ac, int hp, Map<Creature.Speeds,Integer> speed,
								int[] stats, Map<Creature.Stats,Integer> saves,Map<Skills,Integer> skills,
								Map<DamageType, Creature.DamageMultiplier> damageMultipliers, List<Creature.StatusCondition> conditionImmunities,
								Map<Senses,Integer> senses, List<Languages> languages, double cr, int legendaryResistances,  int regen, 
								List<DamageType> regenBlocks, List<String> passives, List<Action> actions,  int legendaryActions, 
								List<Source> sources) {
							this.name = name;
							this.size = size;
							this.type = type;
							this.align = align;
							this.ac = ac;
							this.hp = hp;
							this.speed = speed;
							this.stats = stats;
							this.saves = saves;
							this.skills = skills;
							this.damageMultipliers = damageMultipliers;
							this.conditionImmunities = conditionImmunities;
							this.senses = senses;
							this.languages = languages;
							this.cr = cr;
							this.legendaryResistances = legendaryResistances;
							this.regen = regen;
							this.regenBlocks = regenBlocks;
							this.passives = passives;
							this.actions = actions;
							this.legendaryActions = legendaryActions;
							this.sources = sources;
						}
						
						@Override public String getName() {return name;}
						@Override public int getHP() {return hp;}
						@Override public int getAC() {return ac;}
						@Override public Size getSize() {return size;}
						@Override public Map<Creature.Speeds,Integer> getSpeed() {return speed;}
						@Override public int[] getStats() {return stats;}
						@Override public Map<Creature.Stats,Integer> getSaves() {return saves;}
						@Override public Map<Creature.Skills,Integer> getSkills() {return skills;}
						@Override public List<String> passiveEffects() {return passives;}
						@Override public int getRegen() {return regen;}
						@Override public boolean conditionImmune(Creature.StatusCondition condition) {return conditionImmunities.contains(condition);}
						@Override public double damageMult(DamageType damageType) {
							if(damageType == DamageType.HEALING) {return -1;}
							if(!damageMultipliers.containsKey(damageType)) {return 1;}
							return damageMultipliers.get(damageType).getMult();
						}
						@Override public int getLegendaryRes() {return legendaryResistances;}
						@Override public int getLegendaryAct() {return legendaryActions;}
						@Override public List<Action> getActions() {return actions;}
						@Override public List<Action> getLairActions() {
							List<Action> lairActions = new ArrayList<Action>();
							for(Action a:actions) {if(a.time==Action.Time.LAIR) {lairActions.add(a);}}
							return lairActions;
						}

						@Override
						public List<String>[] getSpells() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public int[] getSpellSlots() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override public List<Languages> getLanguages() {return languages;}
						@Override public List<Senses> getSenses() {
							List<Senses> creatureSenses = new ArrayList<Senses>();
							for(Senses s:Senses.values()) {if(senses.containsKey(s)) {creatureSenses.add(s);}}
							return creatureSenses;}
						@Override public double getCR() {return cr;}
						@Override public int getXP() {
							if(cr==0.125) {return 25;}
							else if(cr==0.25) {return 50;}
							else if(cr==0.5) {return 100;}
							else if(cr==1) {return 200;}
							else if(cr==2) {return 450;}
							else if(cr==3) {return 700;}
							else if(cr==4) {return 1100;}
							else if(cr==5) {return 1800;}
							else if(cr==6) {return 2300;}
							else if(cr==7) {return 2900;}
							else if(cr==8) {return 3900;}
							else if(cr==9) {return 5000;}
							else if(cr==10) {return 5900;}
							else if(cr==11) {return 7200;}
							else if(cr==12) {return 8400;}
							else if(cr==13) {return 10000;}
							else if(cr==14) {return 11500;}
							else if(cr==15) {return 13000;}
							else if(cr==16) {return 15000;}
							else if(cr==17) {return 18000;}
							else if(cr==18) {return 20000;}
							else if(cr==19) {return 22000;}
							else if(cr==20) {return 25000;}
							else if(cr==21) {return 33000;}
							else if(cr==22) {return 41000;}
							else if(cr==23) {return 50000;}
							else if(cr==24) {return 62000;}
							else if(cr==25) {return 75000;}
							else if(cr==26) {return 90000;}
							else if(cr==27) {return 105000;}
							else if(cr==28) {return 120000;}
							else if(cr==29) {return 135000;}
							else if(cr==30) {return 155000;}
							else {return 0;}//cr 0 is worth 0 or 10 xp
						}
						@Override public Alignment getAlign() {return align;}
						@Override public boolean isType(Type type) {return this.type == type;}

						@Override
						public boolean inRegion(Region area) {
							// TODO Auto-generated method stub
							return false;
						}
						
						@Override public boolean fromSource(Source source) {return sources.contains(source);}
						
						@Override
						public String toString() {
							String builtString = "Name: "+name+"\tSize: "+size.toString()+"\tType: "+type.toString()+
									"\tAlignment: "+align.toString()+"\tCR: "+cr+"("+getXP()+"XP)\nAC: "+ac+"\tHP: "+hp+"\tSpeed: "+speed.get(Creature.Speeds.WALK);
							for(Creature.Speeds s:speed.keySet()) {
								if(s!=Creature.Speeds.WALK) {builtString+=", "+s.toString()+" "+speed.get(s);}
							}
							builtString+="\n"+
									"STR:"+stats[0]+"("+Creature.scoreToMod(stats[0])+") DEX:"+stats[1]+"("+Creature.scoreToMod(stats[1])+
									") CON:"+stats[2]+"("+Creature.scoreToMod(stats[2])+") INT:"+stats[3]+"("+Creature.scoreToMod(stats[3])+
									") WIS:"+stats[4]+"("+Creature.scoreToMod(stats[4])+") CHA:"+stats[5]+"("+Creature.scoreToMod(stats[5])+
									")\n";
							if(!saves.isEmpty()) {builtString+="Saves: ";}
							for(Creature.Stats s: saves.keySet()) {builtString+=s.toString()+": "+saves.get(s)+"\t";}
							if(!saves.isEmpty()) {builtString+="\n";}
							if(!skills.isEmpty()) {builtString+="Skills: ";}
							for(Creature.Skills s: skills.keySet()) {builtString+=s.toString()+": "+skills.get(s)+"\t";}
							if(!skills.isEmpty()) {builtString+="\n";}

							List<DamageType> vuln =new ArrayList<DamageType>();
							List<DamageType> res =new ArrayList<DamageType>();
							List<DamageType> immune =new ArrayList<DamageType>();
							List<DamageType> heal =new ArrayList<DamageType>();
							for(DamageType d:damageMultipliers.keySet()) {
								Creature.DamageMultiplier mult = damageMultipliers.get(d);
								if(mult==Creature.DamageMultiplier.VULNERABILITY) {vuln.add(d);}
								if(mult==Creature.DamageMultiplier.RESISTANCE) {res.add(d);}
								if(mult==Creature.DamageMultiplier.IMMUNITY) {immune.add(d);}
								if(mult==Creature.DamageMultiplier.HEALING) {heal.add(d);}
							}
							if(!vuln.isEmpty()) {builtString+="Vulnerabilities:";}
							for(DamageType d: vuln) {builtString+=" "+d.toString();}
							if(!vuln.isEmpty()) {
								if(res.isEmpty()&&immune.isEmpty()) {builtString+="\n";}
								else{builtString+="\t";}}
							if(!res.isEmpty()) {builtString+="Resistances:";}
							for(DamageType d: res) {builtString+=" "+d.toString();}
							if(!res.isEmpty()) {
								if(immune.isEmpty()) {builtString+="\n";}
								else{builtString+="\t";}}
							if(!immune.isEmpty()) {builtString+="Immunities:";}
							for(DamageType d: immune) {builtString+=" "+d.toString();}
							if(!immune.isEmpty()) {builtString+="\n";}
							if(!heal.isEmpty()) {builtString+="Healed by:";}
							for(DamageType d: heal) {builtString+=" "+d.toString();}
							if(!heal.isEmpty()) {builtString+="\n";}
							if(!conditionImmunities.isEmpty()) {
								builtString+="Condition immunities:";
								for(Creature.StatusCondition c:conditionImmunities) {builtString+=" "+c.toString();}
								builtString+="\n";
							}
							
							if(legendaryResistances>0) {builtString+="Legendary Resistances: "+legendaryResistances+"\n";}
							for(Creature.Senses s: senses.keySet()) {builtString+=s.toString()+": "+senses.get(s)+" ";}
							builtString+="\tLanguages: ";
							for(Creature.Languages l: languages) {builtString+=l.toString()+" ";}
							if(regen>0) {
								builtString+="\nRegen "+regen;
								if(!regenBlocks.isEmpty()) {
									builtString+=", blocked for the turn by:";
									for(DamageType d:regenBlocks) {builtString+=" "+d.toString();}
								}
							}
							for(String s:passives) {builtString+="\n"+s;}
							builtString+="\nActions:\n";
							for(Action a:actions) {if(a.time!=Action.Time.LAIR && a.time!=Action.Time.LEGENDARY){builtString+=a.toString()+"\n";}}
							if(getLegendaryAct()>0) {builtString+="Legendary Actions: "+legendaryActions+" each turn\n";}
							for(Action a:actions) {if(a.time==Action.Time.LEGENDARY){builtString+=a.toString()+"\n";}}
							if(!getLairActions().isEmpty()) {builtString+="Lair Actions:\n";}
							for(Action a:actions) {if(a.time==Action.Time.LAIR){builtString+=a.toString()+"\n";}}
							builtString+="From: ";
							for(Source s:sources) {builtString+=s.toString()+"\t";}
							return builtString;
						}
					};
					current.constructor(name, size, type, align, ac, hp, speed, stats, saves, skills, damageMultipliers, conditionImmunities,
							senses, languages, cr, legendaryResistances, regen, regenBlocks, passives, actions, legendaryActions, sources);
					creatures.add(current);
//					System.out.println(current.toString());
				}//where creature ends
				//TODO template creatures
				else {/*System.out.println(*/scan.next()/*)*/;}
			}//where parsing ends
			scan.close();
			System.out.println("Creature parsing complete");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return creatures;
	}
}
