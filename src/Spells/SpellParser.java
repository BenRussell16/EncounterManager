package src.Spells;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Resources.Source;
import Resources.Classes.Subclass;
import Resources.Area;
import Resources.Classes;
import src.Spells.Spell.School;

public class SpellParser {
	public List<Spell> Parse() {
		Scanner scan;
		List<Spell> spells = new ArrayList<Spell>();
		try {
			scan = new Scanner(new File("Resources/SpellList"));
			scan.useDelimiter("<|>|\n|\t| |,");
			while(scan.hasNext()) {
				if(scan.hasNext("spell")) {
					//Setting fields to store spell values.
					String name = "";
					int level = 0;
					School school = null;
					
					boolean[] components = new boolean[3];
					boolean gpcost = false;
					String materials = "";
					String casttime = "";
					boolean ritual = false;
					String duration = "";
					Area area = null;
					int range = 0;
					int[] dimensions = null;

					String effect = "";
					List<Classes> classes = new ArrayList<Classes>();
					List<Subclass> archetypes = new ArrayList<Subclass>();
					List<Source> sources = new ArrayList<Source>();
					
					
					
					//Parse the spell.
					while(!scan.hasNext("/spell")) {
						if(scan.hasNext("name")) {
							scan.next();
							while(!scan.hasNext("/name")) {
								if(name.length()>0) {name+=" ";}
								name+=scan.next();
							}
							scan.next();
						}
						if(scan.hasNext("level")) {
							scan.next();
							level = scan.nextInt();
							scan.next();
						}
						if(scan.hasNext("school")) {
							scan.next();
							for(School s: School.values()) {
								if(scan.hasNext(s.toString())) {school = s;}
							}
							scan.next();
							scan.next();
						}
						//TODO add new fields
						if(scan.hasNext("classes")) {
							scan.next();
							while(!scan.hasNext("/classes")) {
								for(Classes c: Classes.values()) {
									if(scan.hasNext(c.toString())) {
										classes.add(c);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("subclasses")) {
							scan.next();
							while(!scan.hasNext("/subclasses")) {
								for(Classes c: Classes.values()) {
									for(Subclass s: c.getSubclass(c)) {
										if(scan.hasNext(s.toString())) {
											archetypes.add(s);
											scan.next();
										}
									}
								}
							}
							scan.next();
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
					//Spell fully parsed here
					
					
					
					
					
					Spell current = new Spell() {
						String name="";
						int level=0;
						School school = null;
						boolean[] components = null;
						boolean gpCost = false;
						String materials = "";
						String castTime = null;
						boolean isRitual = false;
						String duration = null;
						Area area = null;
						int range = 0;
						int[] dimensions = null;
						String effect = null;
						List<Classes> classes = null;
						List<Subclass> archetypes = null;
						List<Source> sources = null;
						
						@Override public void constructor(String name, int level, School school,
								boolean[] components, boolean gold, String materials,
								String time, boolean ritual, String duration,
								Area area, int range, int[] dimensions,
								String effect, List<Classes> classes, List<Subclass> archetypes,
								List<Source> sources) {
							this.name = name;
							this.level = level;
							this.school = school;
							this.components = components;
							this.gpCost = gold;
							this.materials = materials;
							this.castTime = time;
							this.isRitual = ritual;
							this.duration = duration;
							this.area = area;
							this.range = range;
							this.dimensions = dimensions;
							this.effect = effect;
							this.classes = classes;
							this.archetypes = archetypes;
							this.sources = sources;
						}

						@Override public String getName() {return name;}
						@Override public int getLevel() {return level;}
						@Override public School getSchool() {return school;}

						@Override public boolean[] getComponents(){return components;}
						@Override public boolean gpCost(){return gpCost;}
						@Override public String materials(){return materials;}

						@Override public String castTime(){return castTime;}
						@Override public boolean isRitual(){return isRitual;}
						@Override public String duration(){return duration;}

						@Override public Area getArea(){return area;}
						@Override public int getRange(){return range;}
						@Override public int[] getDimensions(){return dimensions;}
						
						@Override public String getEffect(){return effect;}
						
						@Override public List<Classes> getClasses() {return classes;}
						@Override public boolean fromClass(Classes curClass) {return classes.contains(curClass);}
						@Override public boolean fromArchetype(Subclass curClass) {return archetypes.contains(curClass);}
						
						@Override public boolean fromSource(Source source) {return sources.contains(source);}
						
						@Override public String toString() {//TODO add new fields
							String builtString = name+"\n";
							//Nicely formatted level and school
							if(getLevel()==0){builtString += getSchool().toNiceString()+" cantrip\n";}
							else{
								builtString += getLevel();
								switch (getLevel()){
								case 1: builtString+="st";break;
								case 2: builtString+="nd";break;
								case 3:	builtString+="rd";break;
								default:builtString+="th";break;
								}
								builtString += " level "+getSchool().toNiceString()+"\n";
							}
							
							builtString += "Classes: ";
							int i=0;
							for(Classes c:Classes.values()){
								if(fromClass(c)){
									if(i>0){builtString+=", ";}
									builtString+=c.toNiceString();
									i++;
								}
							}
							builtString += "\nSource: ";
							i=0;
							for(Source s:Source.values()){
								if(fromSource(s)){
									if(i>0){builtString+=", ";}
									builtString+=s.toNiceString();
									i++;
								}
							}
							return builtString;
						}
					};
					current.constructor(name, level, school,
							components, gpcost, materials,
							casttime,ritual,duration,
							area,range,dimensions,
							effect,
							classes,archetypes,
							sources);
					spells.add(current);
				}
				else {/*System.out.println(*/scan.next()/*)*/;}//Skip to next spell
			}//where parsing ends
			
			
			
			
			scan.close();
			System.out.println("Spell parsing complete");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return spells;
	}
}
